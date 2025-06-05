package com.example.admin_project.menu;

import com.example.admin_project.menu.dto.MenuCreateRequest;
import com.example.admin_project.menu.dto.MenuResponse;
import com.example.admin_project.menu.mapper.MenuMapper;
import com.example.admin_project.menu.repository.MenuRepository;
import com.example.admin_project.menu.service.MenuService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
public class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, MenuCreateRequest> loadJsonMap(String path) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            return objectMapper.readValue(is, new TypeReference<>() {});
        }
    }

    @BeforeEach
    void setup() throws IOException {
//        menuRepository.deleteAll();

        Map<String, MenuCreateRequest> requestMap = loadJsonMap("data/menu-create.json");
        MenuCreateRequest parentRequest = requestMap.get("parentMenu");
        MenuCreateRequest childRequest = requestMap.get("childMenu");

        menuService.createMenu(parentRequest);
        menuService.createMenu(childRequest);
    }

    @Test
    void 메뉴_조회_서비스(){
        // when
        List<MenuResponse> menuList = menuService.findMenuList();

        // then
        assertThat(menuList).hasSize(1);
        assertThat(menuList.get(0).getMenuName()).isEqualTo("사용자 관리");
        assertThat(menuList.get(0).getChildren()).hasSize(1);
        assertThat(menuList.get(0).getChildren().get(0).getMenuName()).isEqualTo("대시보드");
    }
}
