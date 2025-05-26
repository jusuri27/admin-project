package com.example.admin_project.menu.mapper;

import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "children", qualifiedByName = "mapChildren")
    MenuResponse toDto(Menu menu);

    List<MenuResponse> toDtoList(List<Menu> menuList);

    // 자식 메뉴를 재귀적으로 변환
    @Named("mapChildren")
    default List<MenuResponse> mapChildren(List<Menu> children) {
        return children != null ? toDtoList(children) : null;
    }
}
