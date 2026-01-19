-- Add MEDICO role if missing
INSERT INTO roles (nombre, descripcion)
VALUES ('MEDICO', 'Médico solicitante con acceso limitado')
ON CONFLICT (nombre) DO NOTHING;
