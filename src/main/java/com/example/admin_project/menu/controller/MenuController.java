package com.example.admin_project.menu.controller;

import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    @GetMapping()
    public List<MenuResponse> getMenuList() {
        return menuService.findMenuList();
    }
}
