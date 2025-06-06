package com.example.admin_project.menu;

import com.example.admin_project.menu.dto.MenuCreateRequest;
import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.dto.MenuUpdateRequest;
import com.example.admin_project.menu.entity.Menu;
import com.example.admin_project.menu.exception.*;
import com.example.admin_project.menu.mapper.MenuMapper;
import com.example.admin_project.menu.repository.MenuRepository;
import com.example.admin_project.menu.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private <T> Map<String, T> loadJsonMap(String path, TypeReference<Map<String, T>> typeRef) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {

            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + path);
            }
            return objectMapper.readValue(is, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load json from " + path, e);
        }
    }

    @BeforeAll
    void beforeAll() {
        // 전체 테스트 전에 한 번만 실행
        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json", new TypeReference<Map<String, MenuCreateRequest>>() {});
        MenuCreateRequest parentRequest = requestMap.get("parentMenu");
        MenuCreateRequest childRequest = requestMap.get("childMenu");

        menuService.createMenu(parentRequest);
        menuService.createMenu(childRequest);
    }

    @Test
    void 메뉴_전체조회(){
        // when
        List<MenuResponse> menuList = menuService.findMenuList();

        // then
        assertThat(menuList).hasSize(1);
        assertThat(menuList.get(0).getMenuName()).isEqualTo("사용자 관리");
        assertThat(menuList.get(0).getChildren()).hasSize(1);
        assertThat(menuList.get(0).getChildren().get(0).getMenuName()).isEqualTo("대시보드");
    }

    @Test
    void 동일한_정렬순서로_부모메뉴를_추가하면_예외가_발생한다() {
        // given
        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json", new TypeReference<Map<String, MenuCreateRequest>>() {});
        MenuCreateRequest parentMenuSameOrder = requestMap.get("parentMenuSameOrder");

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(parentMenuSameOrder))
                .isInstanceOf(DuplicateParentSortOrderException.class)
                .hasMessage("최상위 메뉴 순서가 중복됩니다.");
    }

    @Test
    void 동일한_메뉴이름의_부모메뉴를_추가하면_예외가_발생한다() {
        // given
        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json", new TypeReference<Map<String, MenuCreateRequest>>() {});
        MenuCreateRequest parentMenuSameName = requestMap.get("parentMenuSameName");

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(parentMenuSameName))
                .isInstanceOf(DuplicateParentMenuNameException.class)
                .hasMessage("최상위 메뉴 이름이 중복됩니다.");
    }

    @Test
    void 동일한_정렬순서로_자식메뉴를_추가하면_예외가_발생한다() {
        // given
        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json", new TypeReference<Map<String, MenuCreateRequest>>() {});
        MenuCreateRequest childMenuSameOrder = requestMap.get("childMenuSameOrder");

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(childMenuSameOrder))
                .isInstanceOf(DuplicateChildrenSortOrderException.class)
                .hasMessage("자식 메뉴 순서가 중복됩니다.");
    }

    @Test
    void 동일한_메뉴이름의_자식메뉴를_추가하면_예외가_발생한다() {
        // given
        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json", new TypeReference<Map<String, MenuCreateRequest>>() {});
        MenuCreateRequest childMenuSameName = requestMap.get("childMenuSameName");

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(childMenuSameName))
                .isInstanceOf(DuplicateChildrenMenuNameException.class)
                .hasMessage("자식 메뉴 이름이 중복됩니다.");
    }

    @Test
    void 부모메뉴_수정() {
        // given
        Map<String, MenuUpdateRequest> requestMap = loadJsonMap("data/menu-update.json", new TypeReference<Map<String, MenuUpdateRequest>>() {});
        MenuUpdateRequest updateParentMenu = requestMap.get("updateParentMenu");

        // when
        menuService.updateMenu(updateParentMenu);

        // then
        Menu updated = menuRepository.findById(updateParentMenu.getId()).orElseThrow();
        assertThat(updated.getMenuName()).isEqualTo(updateParentMenu.getMenuName());
        assertThat(updated.getSortOrder()).isEqualTo(updateParentMenu.getSortOrder());
    }

    @Test
    void 자식메뉴_수정() {
        // given
        Map<String, MenuUpdateRequest> requestMap = loadJsonMap("data/menu-update.json", new TypeReference<Map<String, MenuUpdateRequest>>() {});
        MenuUpdateRequest updateChildMenu = requestMap.get("updateChildMenu");

        // when
        menuService.updateMenu(updateChildMenu);

        // then
        Menu updated = menuRepository.findById(updateChildMenu.getId()).orElseThrow();
        assertThat(updated.getMenuName()).isEqualTo(updateChildMenu.getMenuName());
        assertThat(updated.getSortOrder()).isEqualTo(updateChildMenu.getSortOrder());
    }

    @Test
    void 부모메뉴_삭제() {
        // when
        menuService.deleteMenu(1L);
        List<MenuResponse> menuList = menuService.findMenuList();

        // then
        assertThat(menuList).hasSize(0);
    }

    @Test
    void 자식메뉴_삭제() {
        // when
        menuService.deleteMenu(2L);
        List<MenuResponse> menuList = menuService.findMenuList();

        // then
        assertThat(menuList).hasSize(1);
        assertThat(menuList.get(0).getMenuName()).isEqualTo("사용자 관리");
        assertThat(menuList.get(0).getChildren()).hasSize(0);
    }

    @Test
    void 잘못된ID_메뉴_삭제시() {
        // when & then
        assertThatThrownBy(() -> menuService.deleteMenu(3L))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("해당 메뉴가 존재하지 않습니다.");
    }
}
