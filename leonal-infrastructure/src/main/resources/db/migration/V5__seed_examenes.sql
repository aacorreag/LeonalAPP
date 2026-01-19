-- Seed Data for Examenes
INSERT INTO examenes (id, codigo_interno, nombre, metodo, tipo_resultado, unidad_medida, precio, activo) VALUES
(uuid_generate_v4(), 'GLU01', 'Glucosa Basal', 'Enzimático Hexoquinasa', 'NUMERICO', 'mg/dL', 12000.00, true),
(uuid_generate_v4(), 'COL01', 'Colesterol Total', 'Enzimático Colorimétrico', 'NUMERICO', 'mg/dL', 15000.00, true),
(uuid_generate_v4(), 'TRIG01', 'Triglicéridos', 'Enzimático Colorimétrico', 'NUMERICO', 'mg/dL', 15000.00, true),
(uuid_generate_v4(), 'HEM01', 'Hemograma Completo', 'Citometría de Flujo', 'NUMERICO', 'N/A', 25000.00, true),
(uuid_generate_v4(), 'URO01', 'Uroanálisis', 'Microscopía / Tira', 'TEXTO', 'N/A', 18000.00, true),
(uuid_generate_v4(), 'CREA01', 'Creatinina', 'Jaffé Cinético', 'NUMERICO', 'mg/dL', 14000.00, true),
(uuid_generate_v4(), 'TSH01', 'TSH Ultrasensible', 'Quimioluminiscencia', 'NUMERICO', 'mUI/L', 35000.00, true);
