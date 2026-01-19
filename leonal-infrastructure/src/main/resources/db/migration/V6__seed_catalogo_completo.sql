-- Migración para cargar el catálogo completo de exámenes sin duplicados
-- Química Clínica
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('GLU02', 'Glucosa Postprandial', 'Enzimático Hexoquinasa', 'mg/dL', 'NUMERICO', 12000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('HDL01', 'Colesterol HDL', 'Enzimático Directo', 'mg/dL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('LDL01', 'Colesterol LDL', 'Cálculo / Enzimático', 'mg/dL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('UREA01', 'Urea', 'Enzimático UV', 'mg/dL', 'NUMERICO', 14000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('ACUR01', 'Ácido Úrico', 'Enzimático Colorimétrico', 'mg/dL', 'NUMERICO', 14000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('TGO01', 'AST (TGO)', 'Cinético UV', 'U/L', 'NUMERICO', 16000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('TGP01', 'ALT (TGP)', 'Cinético UV', 'U/L', 'NUMERICO', 16000) ON CONFLICT (codigo_interno) DO NOTHING;

-- Hematología
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('HB01', 'Hemoglobina', 'Cianometahemoglobina', 'g/dL', 'NUMERICO', 10000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('HTC01', 'Hematocrito', 'Microcentrífuga', '%', 'NUMERICO', 10000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('VSG01', 'Velocidad de Sedimentación (VSG)', 'Westergren', 'mm/h', 'NUMERICO', 12000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PLAQ01', 'Recuento de Plaquetas', 'Automatizado', 'x10³/µL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;

-- Uroanálisis y Coprológico
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('URO02', 'Orina Simple', 'Tira Reactiva', 'Escala', 'SELECCION', 12000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('COP01', 'Coprológico', 'Microscopia Directa', 'campo', 'TEXTO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('COP02', 'Coproparasitoscópico Seriado', 'Concentración', 'Presencia', 'TEXTO', 30000) ON CONFLICT (codigo_interno) DO NOTHING;

-- Inmunología / Serología
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PCR01', 'Proteína C Reactiva', 'Inmunoturbidimetría', 'mg/L', 'NUMERICO', 20000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('ASO01', 'Anti-estreptolisina O', 'Aglutinación', 'UI/mL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('FR01', 'Factor Reumatoide', 'Aglutinación', 'UI/mL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('VDRL01', 'VDRL', 'Floculación', 'Reactivo', 'SELECCION', 15000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('HCG01', 'Embarazo en Sangre', 'Inmunocromatografía', 'mUI/mL', 'NUMERICO', 18000) ON CONFLICT (codigo_interno) DO NOTHING;

-- Pruebas Rápidas (POCT)
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('HCG02', 'Embarazo en Orina', 'Inmunocromatografía', 'Pos/Neg', 'SELECCION', 10000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('COVID01', 'COVID-19 Antígeno', 'Inmunocromatografía', 'Pos/Neg', 'SELECCION', 25000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('INFL01', 'Influenza A/B', 'Inmunocromatografía', 'Pos/Neg', 'SELECCION', 25000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('GLU03', 'Glucosa Capilar', 'Glucómetro', 'mg/dL', 'NUMERICO', 5000) ON CONFLICT (codigo_interno) DO NOTHING;

-- Paquetes
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PACK01', 'Perfil Lipídico', 'Varios', 'Múltiples', 'NUMERICO', 60000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PACK02', 'Perfil Renal Básico', 'Varios', 'Múltiples', 'SELECCION', 40000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PACK03', 'Chequeo Básico', 'Varios', 'Múltiples', 'TEXTO', 50000) ON CONFLICT (codigo_interno) DO NOTHING;
INSERT INTO examenes (codigo_interno, nombre, metodo, unidad_medida, tipo_resultado, precio) VALUES
('PACK04', 'Perfil Hepático', 'Varios', 'Múltiples', 'NUMERICO', 30000) ON CONFLICT (codigo_interno) DO NOTHING;
