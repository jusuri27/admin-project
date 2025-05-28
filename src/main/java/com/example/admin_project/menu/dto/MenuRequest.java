package com.example.admin_project.menu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class MenuRequest {
    @NotBlank(message = "메뉴 이름은 필수입니다")
    protected String menuName;
    @Min(value = 0, message = "정렬 순서는 0 이상이어야 합니다")
    protected int sortOrder;
    @NotNull(message = "사용 여부는 필수입니다")
    protected Boolean isUse;
    protected Long parentId;
}
