package com.example.admin_project.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Menu {

    @Comment("식별자")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("메뉴 이름")
    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Comment("메뉴 순서")
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Comment("사용 여부")
    @Column(name = "is_use", nullable = false)
    private Boolean isUse;

    @Comment("부모 메뉴")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id")
    private Menu parent;

    @Comment("자식 메뉴")
    @OneToMany(mappedBy = "parent")
    @OrderBy("sortOrder ASC")
    private List<Menu> children = new ArrayList<>();
}
