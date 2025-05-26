package com.example.admin_project.menu.service;

import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.entity.Menu;
import com.example.admin_project.menu.mapper.MenuMapper;
import com.example.admin_project.menu.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    public List<MenuResponse> findMenuList() {
        List<Menu> menuEntity = menuRepository.findAll();
        return menuMapper.toDtoList(menuEntity);
    }
}
