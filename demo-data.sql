-- Демо данные для тестирования
INSERT INTO users (id, login, password, status) VALUES
('d3d29d70-1d25-11e8-8c75-000c29ff8f7c', 'vasya', '$2a$10$TMS0xMzU3MDY2MjAwMDAwO.9rF2d6U.QpRc.9bbdk6Vp5pQZz6J/K', 'active'),
('d3d29d71-1d25-11e8-8c75-000c29ff8f7c', 'petya', '$2a$10$TMS0xMzU3MDY2MjAwMDAwO.9rF2d6U.QpRc.9bbdk6Vp5pQZz6J/K', 'active');

INSERT INTO cards (id, user_id, number, balance_in_kopecks) VALUES
('d3d29d72-1d25-11e8-8c75-000c29ff8f7c', 'd3d29d70-1d25-11e8-8c75-000c29ff8f7c', '5559 0000 0000 0001', 1000000),
('d3d29d73-1d25-11e8-8c75-000c29ff8f7c', 'd3d29d70-1d25-11e8-8c75-000c29ff8f7c', '5559 0000 0000 0002', 1000000),
('d3d29d74-1d25-11e8-8c75-000c29ff8f7c', 'd3d29d71-1d25-11e8-8c75-000c29ff8f7c', '5559 0000 0000 0003', 1000000);