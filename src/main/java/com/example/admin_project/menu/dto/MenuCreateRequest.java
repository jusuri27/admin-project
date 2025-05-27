package com.example.admin_project.menu.dto;

import com.example.admin_project.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuCreateRequest {
    private String menuName;
    private int sortOrder;
    private Boolean isUse;
    private Long parentId;

    public Menu toEntity(Menu parent) {
        return Menu.builder()
                .parent(parent)
                .menuName(menuName)
                .sortOrder(sortOrder)
                .isUse(isUse)
                .build();
    }
}
