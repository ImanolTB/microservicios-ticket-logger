

-- Insertar datos de ejemplo para 'roles'
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MANAGER'),
(3, 'ROLE_USER');


-- Insertar datos de ejemplo para 'users'. La contraseña de cada usuario es password
INSERT IGNORE INTO users (id, username, password, enabled, first_name, last_name, image, created_date, last_modified_date, last_password_change_date) VALUES
(1, 'Admin', '$2y$04$XeVZzl79cnxBOeztMU1XX.9YdGzUI.Lh.uhN0xv4JUsH/FaqaJDR.', true, 'Admin', 'User', '/images/admin.jpg', NOW(), NOW(), NOW()),
(2, 'Manager', '$2y$04$7xeGPNhQqXkTj9LARtfNhuSga.UhH3bgUyMgfaaccy4WbUXNS0J6W', true, 'Manager', 'User', '/images/manager.jpg', NOW(), NOW(), NOW()),
(3, 'Anonimo', '$2y$04$hElStlebbkj3JmKhtujuLuhOE9c1VPDTzXm11qd4qJRIJeTPyIuLe', true, 'Regular', 'User', '/images/user.jpg', NOW(), NOW(), NOW());

-- Asignar el rol de administrador al usuario con id 1
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1);
-- Asignar el rol de gestor al usuario con id 2
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(2, 2);
-- Asignar el rol de usuario normal al usuario con id 3
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(3, 3);
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(4, 3);

