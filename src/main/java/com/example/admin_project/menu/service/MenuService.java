package com.example.admin_project.menu.service;

import com.example.admin_project.menu.dto.MenuCreateRequest;
import com.example.admin_project.menu.dto.MenuRequest;
import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.dto.MenuUpdateRequest;
import com.example.admin_project.menu.entity.Menu;
import com.example.admin_project.menu.exception.*;
import com.example.admin_project.menu.mapper.MenuMapper;
import com.example.admin_project.menu.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    @Transactional(readOnly = true)
    public List<MenuResponse> findMenuList() {
        List<Menu> menuEntity = menuRepository.findAllWithChildren();
        return menuMapper.toDtoList(menuEntity);
    }

    @Transactional
    public void createMenu(MenuCreateRequest menuCreateRequest) {
        Menu parentMenu = findParentMenu(menuCreateRequest.getParentId());
        validateMenu(parentMenu, menuCreateRequest);

        Menu menuEntity = menuCreateRequest.toCreateEntity(parentMenu);
        menuRepository.save(menuEntity);
    }

    @Transactional
    public void updateMenu(MenuUpdateRequest menuUpdateRequest) {
        Menu parentMenu = findParentMenu(menuUpdateRequest.getParentId());
        validateMenu(parentMenu, menuUpdateRequest);

        Menu entity = menuRepository.findById(menuUpdateRequest.getId()).
                orElseThrow(() -> new MenuNotFoundException("해당 메뉴가 존재하지 않습니다."));
        menuUpdateRequest.toUpdateEntity(entity);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu entity = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException("해당 메뉴가 존재하지 않습니다."));
        entity.softDeleteMenu();
    }

    private Menu findParentMenu(Long parentId) {
        return parentId == null ? null : menuRepository.findById(parentId).orElseThrow(() -> new MenuNotFoundException("부모 메뉴가 존재하지 않습니다."));
    }

    private void validateMenu(Menu parentMenu, MenuRequest menuRequest) {
        if(menuRequest.getParentId() == null) {
            validateParentMenu(menuRequest);
        } else {
            validateChildrenMenu(parentMenu, menuRequest);
        }
    }

    private void validateChildrenMenu(Menu parentMenu, MenuRequest menuRequest) {
        if (menuRepository.existsByParentIdAndSortOrder(parentMenu.getId(), menuRequest.getSortOrder())) {
            throw new DuplicateChildrenSortOrderException("자식 메뉴 순서가 중복됩니다.");
        }

        if (menuRepository.existsByParentIdAndMenuName(parentMenu.getId(), menuRequest.getMenuName())) {
            throw new DuplicateChildrenMenuNameException("자식 메뉴 이름이 중복됩니다.");
        }
    }

    private void validateParentMenu(MenuRequest menuRequest) {
        if (menuRepository.existsByParentIsNullAndMenuName(menuRequest.getMenuName())) {
            throw new DuplicateParentMenuNameException("최상위 메뉴 이름이 중복됩니다.");
        }
        if (menuRepository.existsByParentIsNullAndSortOrder(menuRequest.getSortOrder())) {
            throw new DuplicateParentSortOrderException("최상위 메뉴 순서가 중복됩니다.");
        }
    }
}
