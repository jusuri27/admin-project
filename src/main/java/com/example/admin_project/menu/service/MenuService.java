package com.example.admin_project.menu.service;

import com.example.admin_project.menu.dto.MenuCreateRequest;
import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.entity.Menu;
import com.example.admin_project.menu.exception.MenuNotFoundException;
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
        Menu menuEntity = menuCreateRequest.toEntity(parentMenu);
        menuRepository.save(menuEntity);
    }
}
