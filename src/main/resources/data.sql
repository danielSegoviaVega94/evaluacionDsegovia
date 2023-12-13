-- Insertar el usuario admin
INSERT INTO users (id, name, email, password, created, modified, last_login, token, is_active)
VALUES ('6e249af3-ccfe-4811-89b5-1a8ab373ee19', 'admin', 'admin@admin.com', '$2a$12$RhCGEdWdiUA.tsPfeNEK4eu51xR48qEYa3CpanmgwZvkXjh6tdaym', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true);

-- Insertar teléfonos asociados al usuario admin
INSERT INTO phones (number, citycode, contrycode, id)
VALUES ('123456789', '001', '001', '6e249af3-ccfe-4811-89b5-1a8ab373ee19')
