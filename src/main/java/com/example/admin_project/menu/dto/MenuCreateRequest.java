package com.example.admin_project.menu.dto;

import com.example.admin_project.menu.entity.Menu;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuCreateRequest extends MenuRequest {

    public Menu toCreateEntity(Menu parent) {
        return Menu.builder()
                .parent(parent)
                .menuName(menuName)
                .sortOrder(sortOrder)
                .isUse(isUse)
                .build();
    }
}
