package com.example.admin_project.menu.service;

import com.example.admin_project.menu.dto.MenuCreateRequest;
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
        Menu parentMenu = null;
        if(menuCreateRequest.getParentId() != null) {
            parentMenu = menuRepository.findById(menuCreateRequest.getParentId())
                    .orElseThrow(() -> new MenuNotFoundException("부모 메뉴가 존재하지 않습니다."));
        }
        validateMenu(parentMenu, menuCreateRequest);
        Menu menuEntity = menuCreateRequest.toCreateEntity(parentMenu);
        menuRepository.save(menuEntity);
    }

    @Transactional
    public void updateMenu(MenuUpdateRequest menuUpdateRequest) {
        Menu entity = menuRepository.findById(menuUpdateRequest.getId()).
                orElseThrow(() -> new MenuNotFoundException("부모 메뉴가 존재하지 않습니다."));
        menuUpdateRequest.toUpdateEntity(entity);
    }

    private void validateMenu(Menu parentMenu, MenuCreateRequest menuCreateRequest) {
        if(menuCreateRequest.getParentId() == null) {
            validateParentMenu(menuCreateRequest);
        } else {
            validateChildrenMenu(parentMenu, menuCreateRequest);
        }
    }

    private void validateChildrenMenu(Menu parentMenu, MenuCreateRequest menuCreateRequest) {
        if (menuRepository.existsByParentIdAndSortOrder(parentMenu.getId(), menuCreateRequest.getSortOrder())) {
            throw new DuplicateChildrenSortOrderException("자식 메뉴 순서가 중복됩니다.");
        }

        if (menuRepository.existsByParentIdAndMenuName(parentMenu.getId(), menuCreateRequest.getMenuName())) {
            throw new DuplicateChildrenMenuNameException("자식 메뉴 이름이 중복됩니다.");
        }
    }

    private void validateParentMenu(MenuCreateRequest menuCreateRequest) {
        if (menuRepository.existsByParentIsNullAndMenuName(menuCreateRequest.getMenuName())) {
            throw new DuplicateParentMenuNameException("최상위 메뉴 이름이 중복됩니다.");
        }
        if (menuRepository.existsByParentIsNullAndSortOrder(menuCreateRequest.getSortOrder())) {
            throw new DuplicateParentSortOrderException("최상위 메뉴 순서가 중복됩니다.");
        }
    }
}
