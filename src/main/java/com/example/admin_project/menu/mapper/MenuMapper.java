package com.example.admin_project.menu.mapper;

import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    /****
     * Converts a Menu entity to a MenuResponse DTO, mapping the parent menu's ID to the parentId field and recursively mapping child menus.
     *
     * @param menu the Menu entity to convert
     * @return the corresponding MenuResponse DTO with parent and children fields mapped
     */
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "children", qualifiedByName = "mapChildren")
    MenuResponse toDto(Menu menu);

    /**
 * Converts a list of Menu entities to a list of MenuResponse DTOs.
 *
 * @param menuList the list of Menu entities to convert
 * @return a list of MenuResponse DTOs corresponding to the input entities
 */
List<MenuResponse> toDtoList(List<Menu> menuList);

    /**
     * Recursively converts a list of child Menu entities to a list of MenuResponse DTOs.
     *
     * @param children the list of child Menu entities, or null
     * @return a list of MenuResponse DTOs representing the children, or null if the input is null
     */
    @Named("mapChildren")
    default List<MenuResponse> mapChildren(List<Menu> children) {
        return children != null ? toDtoList(children) : null;
    }
}
