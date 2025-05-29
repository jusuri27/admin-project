package com.example.admin_project.menu.controller;

import com.example.admin_project.menu.dto.MenuCreateRequest;
import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.dto.MenuUpdateRequest;
import com.example.admin_project.menu.service.MenuService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuResponse> getMenuList() {
        return menuService.findMenuList();
    }

    @PostMapping
    public void createMenu(@RequestBody @Validated MenuCreateRequest menuCreateRequest) {
        menuService.createMenu(menuCreateRequest);
    }

    @PatchMapping
    public void updateMenu(@RequestBody @Validated MenuUpdateRequest menuUpdateRequest) {
        menuService.updateMenu(menuUpdateRequest);
    }

    @DeleteMapping("/{menuId}")
    public void deleteMenu(@PathVariable("menuId") Long menuId) {
        menuService.deleteMenu(menuId);
    }
}
