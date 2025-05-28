package com.example.admin_project.menu.dto;

import com.example.admin_project.menu.entity.Menu;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuUpdateRequest  extends MenuRequest {
    @NotNull(message = "메뉴 id는 필수입니다")
    private Long id;

    public void toUpdateEntity(Menu entity) {
        entity.updateMenu(menuName, sortOrder, isUse);
    }

}
