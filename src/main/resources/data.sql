-- =========================================
-- Sistema de Botica - Datos de ejemplo
-- =========================================

-- Categorias
INSERT INTO categorias (nombre, descripcion)
SELECT 'Analgesicos', 'Medicamentos para el alivio del dolor'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Analgesicos');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Antibioticos', 'Medicamentos para tratar infecciones bacterianas'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Antibioticos');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Vitaminas', 'Suplementos vitaminicos y minerales'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Vitaminas');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Cuidado Personal', 'Productos de higiene y cuidado personal'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Cuidado Personal');

-- Usuarios
INSERT INTO usuarios (nombre, apellido, email, password, rol)
SELECT 'Carlos', 'Ramirez', 'carlos.ramirez@botica.com', '123456', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'carlos.ramirez@botica.com');

INSERT INTO usuarios (nombre, apellido, email, password, rol)
SELECT 'Maria', 'Lopez', 'maria.lopez@botica.com', '123456', 'VENDEDOR'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'maria.lopez@botica.com');

INSERT INTO usuarios (nombre, apellido, email, password, rol)
SELECT 'Jose', 'Torres', 'jose.torres@botica.com', '123456', 'VENDEDOR'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'jose.torres@botica.com');

-- Productos
INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Paracetamol 500mg', 'Caja x 100 tabletas', 12.50, 150, 'Genfar',
       (SELECT id FROM categorias WHERE nombre = 'Analgesicos')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Paracetamol 500mg');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Ibuprofeno 400mg', 'Caja x 50 tabletas', 18.90, 100, 'Bayer',
       (SELECT id FROM categorias WHERE nombre = 'Analgesicos')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Ibuprofeno 400mg');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Amoxicilina 500mg', 'Caja x 20 capsulas', 25.00, 80, 'Pfizer',
       (SELECT id FROM categorias WHERE nombre = 'Antibioticos')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Amoxicilina 500mg');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Azitromicina 500mg', 'Caja x 3 tabletas', 22.40, 60, 'Roche',
       (SELECT id FROM categorias WHERE nombre = 'Antibioticos')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Azitromicina 500mg');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Vitamina C 1000mg', 'Frasco x 30 tabletas efervescentes', 15.80, 120, 'Bayer',
       (SELECT id FROM categorias WHERE nombre = 'Vitaminas')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Vitamina C 1000mg');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Complejo B', 'Frasco x 60 capsulas', 19.50, 90, 'Genfar',
       (SELECT id FROM categorias WHERE nombre = 'Vitaminas')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Complejo B');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Alcohol en Gel 500ml', 'Antiseptico para manos', 9.90, 200, 'Cetco',
       (SELECT id FROM categorias WHERE nombre = 'Cuidado Personal')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Alcohol en Gel 500ml');

INSERT INTO productos (nombre, descripcion, precio, stock, laboratorio, categoria_id)
SELECT 'Mascarillas Quirurgicas', 'Caja x 50 unidades', 14.00, 300, 'Medical Pro',
       (SELECT id FROM categorias WHERE nombre = 'Cuidado Personal')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mascarillas Quirurgicas');

-- Venta de ejemplo (solo se inserta si no existen ventas previas)
INSERT INTO ventas (fecha, total, usuario_id)
SELECT CURRENT_TIMESTAMP, 31.30, (SELECT id FROM usuarios WHERE email = 'maria.lopez@botica.com')
WHERE NOT EXISTS (SELECT 1 FROM ventas);

INSERT INTO detalle_ventas (cantidad, precio_unitario, subtotal, venta_id, producto_id)
SELECT 1, 12.50, 12.50,
       (SELECT id FROM ventas ORDER BY id ASC LIMIT 1),
       (SELECT id FROM productos WHERE nombre = 'Paracetamol 500mg')
WHERE NOT EXISTS (SELECT 1 FROM detalle_ventas);

INSERT INTO detalle_ventas (cantidad, precio_unitario, subtotal, venta_id, producto_id)
SELECT 1, 18.90, 18.90,
       (SELECT id FROM ventas ORDER BY id ASC LIMIT 1),
       (SELECT id FROM productos WHERE nombre = 'Ibuprofeno 400mg')
WHERE (SELECT COUNT(*) FROM detalle_ventas) <= 1;
