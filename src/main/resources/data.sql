-- 상위 메뉴 1
INSERT INTO menu (menu_name, sort_order, is_use, parent_menu_id)
VALUES ('대시보드', 1, true, NULL);

-- 상위 메뉴 2
INSERT INTO menu (menu_name, sort_order, is_use, parent_menu_id)
VALUES ('관리자 설정', 2, true, NULL);

-- 하위 메뉴 1 (대시보드 하위)
INSERT INTO menu (menu_name, sort_order, is_use, parent_menu_id)
VALUES ('통계 보기', 1, true, 1);

-- 하위 메뉴 2 (대시보드 하위)
INSERT INTO menu (menu_name, sort_order, is_use, parent_menu_id)
VALUES ('로그 확인', 2, true, 1);

-- 하위 메뉴 3 (관리자 설정 하위)
INSERT INTO menu (menu_name, sort_order, is_use, parent_menu_id)
VALUES ('권한 관리', 1, true, 2);