package com.example.admin_project.menu.service;

import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.entity.Menu;
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

}
