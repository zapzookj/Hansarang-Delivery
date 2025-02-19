-- data.sql
INSERT INTO p_restaurant (id, name, category_id, owner_id, location_id, status, created_at, updated_at, created_by, modified_by, deleted_at, deleted_by)
VALUES
    ('e16b2674-b5a3-4b23-b299-7d29ed2c6bfa', 'Pizza Place', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('d32b8f29-4c85-4b4a-bd6b-739b8b22a6e1', 'Sushi World', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:05:00', '2025-02-17 12:05:00', 'admin', 'admin', NULL, NULL),
    ('fda2d091-ef1d-47b3-bb5c-3d6d0f34c6b4', 'Burger King', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:10:00', '2025-02-17 12:10:00', 'admin', 'admin', NULL, NULL),
    ('bfa3e8cc-5a56-44e5-b622-f1e55be87d2e', 'Pasta House', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', false, '2025-02-17 12:15:00', '2025-02-17 12:15:00', 'admin', 'admin', NULL, NULL);

-- Category 테이블에 5개의 데이터 삽입
INSERT INTO p_category (id, name, created_at, updated_at, created_by, modified_by, deleted_at, deleted_by)
VALUES
    ('f816c9b7-b6da-4a3f-9c39-bb6b66b4a7b9', 'Food', '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('27dbfd73-12c1-42ed-bc07-48cc7d9030fa', 'Beverage', '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('73ecfa1c-7a64-42a2-bc0a-c2d268842426', 'Dessert', '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('c7dfb1e1-2f56-4c36-b0bb-d7c5fd5131b5', 'Snacks', '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('b34d412d-22d3-44d2-bc9e-c15b17d7c8a7', 'Healthy', '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL);
-- Location 테이블에 5개의 데이터 삽입
INSERT INTO p_location (id, law_code, city, district, sub_district, main_lot_number, sub_lot_number, road_name_code, building_main_number, building_sub_number)
VALUES
    ('a3fbdc2c-4628-4a60-bccf-d963b876ab1e', '1101052000', '서울특별시', '강남구', '역삼동', '123', '456', '1010101010', '10', '20'),
    ('bce748fe-f24c-4a69-87cf-5d40f6d5c4bc', '2203071000', '부산광역시', '해운대구', '우동', '789', '012', '2020202020', '30', '40'),
    ('6ab257c9-1012-43c4-bcf7-f8fa27d09be9', '4403073000', '인천광역시', '남동구', '구월동', '234', '567', '3030303030', '50', '60'),
    ('d7609ed6-5489-43a9-a2e1-39de4d9fef47', '3301003000', '대구광역시', '수성구', '범어동', '345', '678', '4040404040', '70', '80'),
    ('ecf7fa88-d62c-4e43-b02e-4655c9a2fe6b', '6104021000', '광주광역시', '남구', '양림동', '567', '890', '5050505050', '90', '100');
