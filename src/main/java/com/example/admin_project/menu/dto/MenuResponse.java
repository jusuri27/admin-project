package com.example.admin_project.menu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuResponse {
    private Long id;
    private String menuName;
    private int sortOrder;
    private Boolean isUse;
    private Long parentId;
    private List<MenuResponse> children;
}
