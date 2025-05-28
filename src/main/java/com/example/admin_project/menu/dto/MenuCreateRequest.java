package com.example.admin_project.menu.dto;

import com.example.admin_project.menu.entity.Menu;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuCreateRequest {
    @NotBlank(message = "메뉴 이름은 필수입니다")
    private String menuName;
    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
    private int sortOrder;
    @NotNull(message = "사용 여부는 필수입니다")
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
