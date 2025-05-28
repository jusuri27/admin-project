package com.example.admin_project.menu.repository;

import com.example.admin_project.menu.entity.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @EntityGraph(attributePaths = {"children"})
    @Query("SELECT c FROM Menu c WHERE c.parent IS NULL")
    List<Menu> findAllWithChildren();

    boolean existsByParentIdAndSortOrder(Long id, int sortOrder);

    boolean existsByParentIdAndMenuName(Long id, String menuName);

    boolean existsByParentIsNullAndMenuName(String menuName);

    boolean existsByParentIsNullAndSortOrder(int sortOrder);
}
