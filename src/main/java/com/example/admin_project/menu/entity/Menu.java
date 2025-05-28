package com.example.admin_project.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;


// 부모 메뉴끼리 메뉴 이름과, 메뉴 순서 중복 방지하기 위해서는
// 수동으로 제약 조건 추가 필요함
// (H2는 where 절이 포함된 인덱스는 지원 안함)
// 적용할려면 mysql에서 확인 가능


// 부모 메뉴들끼리 menu_name 중복 방지
//CREATE UNIQUE INDEX uq_parent_menu_name
//ON menu(menu_name)
//WHERE parent_menu_id IS NULL;
//
// 부모 메뉴들끼리 sort_order 중복 방지
//CREATE UNIQUE INDEX uq_parent_sort_order
//ON menu(sort_order)
//WHERE parent_menu_id IS NULL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "menu", uniqueConstraints = {
        @UniqueConstraint(name = "uq_menu_parent_sort_order", columnNames = {"parent_menu_id", "sort_order"}),
        @UniqueConstraint(name = "uq_menu_parent_menu_name", columnNames = {"parent_menu_id", "menu_name"})
})
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

    public void updateMenu(String menuName, int sortOrder, Boolean isUse) {
        this.menuName = menuName;
        this.sortOrder = sortOrder;
        this.isUse = isUse;
    }
}
