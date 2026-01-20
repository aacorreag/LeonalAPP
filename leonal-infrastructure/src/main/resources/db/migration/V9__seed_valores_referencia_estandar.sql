-- Migración para cargar valores de referencia estándar para los exámenes existentes
UPDATE examenes SET valores_referencia = '70 - 110 mg/dL' WHERE codigo_interno IN ('GLU01', 'GLU02', 'GLU03');
UPDATE examenes SET valores_referencia = '40 - 60 mg/dL' WHERE codigo_interno = 'HDL01';
UPDATE examenes SET valores_referencia = '0 - 100 mg/dL' WHERE codigo_interno = 'LDL01';
UPDATE examenes SET valores_referencia = '15 - 45 mg/dL' WHERE codigo_interno = 'UREA01';
UPDATE examenes SET valores_referencia = '3.5 - 7.2 mg/dL' WHERE codigo_interno = 'ACUR01';
UPDATE examenes SET valores_referencia = '0 - 40 U/L' WHERE codigo_interno = 'TGO01';
UPDATE examenes SET valores_referencia = '0 - 41 U/L' WHERE codigo_interno = 'TGP01';
UPDATE examenes SET valores_referencia = '12.1 - 15.1 g/dL' WHERE codigo_interno = 'HB01';
UPDATE examenes SET valores_referencia = '36.1% - 44.3%' WHERE codigo_interno = 'HTC01';
UPDATE examenes SET valores_referencia = '0 - 20 mm/h' WHERE codigo_interno = 'VSG01';
UPDATE examenes SET valores_referencia = '150,000 - 450,000 /µL' WHERE codigo_interno = 'PLAQ01';
UPDATE examenes SET valores_referencia = '0.0 - 5.0 mg/L' WHERE codigo_interno = 'PCR01';
UPDATE examenes SET valores_referencia = '0 - 200 UI/mL' WHERE codigo_interno = 'ASO01';
UPDATE examenes SET valores_referencia = '0 - 20 UI/mL' WHERE codigo_interno = 'FR01';
UPDATE examenes SET valores_referencia = 'Negativo' WHERE codigo_interno IN ('VDRL01', 'HCG02', 'COVID01', 'INFL01');
UPDATE examenes SET valores_referencia = 'Ver informe detallado' WHERE codigo_interno IN ('URO02', 'COP01', 'COP02');
UPDATE examenes SET valores_referencia = 'Múltiples rangos (ver detalle)' WHERE codigo_interno LIKE 'PACK%';
