-- data.sql
INSERT INTO p_restaurant (id, name, category_id, owner_id, location_id, status, created_at, updated_at, created_by, modified_by, deleted_at, deleted_by)
VALUES
    ('e16b2674-b5a3-4b23-b299-7d29ed2c6bfa', 'Pizza Place', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:00:00', '2025-02-17 12:00:00', 'admin', 'admin', NULL, NULL),
    ('d32b8f29-4c85-4b4a-bd6b-739b8b22a6e1', 'Sushi World', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:05:00', '2025-02-17 12:05:00', 'admin', 'admin', NULL, NULL),
    ('fda2d091-ef1d-47b3-bb5c-3d6d0f34c6b4', 'Burger King', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', true, '2025-02-17 12:10:00', '2025-02-17 12:10:00', 'admin', 'admin', NULL, NULL),
    ('bfa3e8cc-5a56-44e5-b622-f1e55be87d2e', 'Pasta House', 'a1234567-89ab-cdef-0123-456789abcdef', 'b1234567-89ab-cdef-0123-456789abcdef', 'c1234567-89ab-cdef-0123-456789abcdef', false, '2025-02-17 12:15:00', '2025-02-17 12:15:00', 'admin', 'admin', NULL, NULL);
