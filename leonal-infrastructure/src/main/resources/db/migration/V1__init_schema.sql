-- Habilitar extensión UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ROLES
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

INSERT INTO roles (nombre, descripcion) VALUES 
('ADMIN', 'Administrador del sistema'),
('RECEPCION', 'Recepcionista'),
('BIOANALISTA', 'Técnico de laboratorio'),
('VALIDADOR', 'Bioanalista supervisor que valida'),
('CONSULTA', 'Usuario solo lectura');

-- USUARIOS
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ROLES_USUARIOS
CREATE TABLE usuarios_roles (
    usuario_id UUID REFERENCES usuarios(id),
    role_id UUID REFERENCES roles(id),
    PRIMARY KEY (usuario_id, role_id)
);

-- AUDITORIA
CREATE TABLE auditoria_eventos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID REFERENCES usuarios(id),
    accion VARCHAR(50) NOT NULL, -- UPDATE, INSERT, DELETE, LOGIN
    entidad_afectada VARCHAR(50),
    entidad_id UUID,
    detalle_cambio TEXT, -- JSON o Texto
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_origen VARCHAR(45)
);

-- MEDICOS / SOLICITANTES
CREATE TABLE medicos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50),
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

-- PACIENTES
CREATE TABLE pacientes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tipo_documento VARCHAR(20) NOT NULL DEFAULT 'CEDULA',
    numero_documento VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(1) CHECK (sexo IN ('M', 'F', 'O')),
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_paciente_doc UNIQUE (tipo_documento, numero_documento)
);

-- EXAMENES (CATALOGO)
CREATE TABLE examenes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    codigo_interno VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    metodo VARCHAR(50),
    tipo_resultado VARCHAR(20) NOT NULL DEFAULT 'NUMERICO', -- NUMERICO, TEXTO, SELECCION
    unidad_medida VARCHAR(20),
    precio DECIMAL(10, 2) DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
);

-- RANGOS DE REFERENCIA
CREATE TABLE rangos_referencia (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    examen_id UUID REFERENCES examenes(id),
    sexo VARCHAR(1), -- NULL = Ambos
    edad_min_dias INT DEFAULT 0,
    edad_max_dias INT DEFAULT 36500, -- ~100 años
    valor_min DECIMAL(10, 4),
    valor_max DECIMAL(10, 4),
    texto_referencia TEXT, -- Para resultados no numéricos
    activo BOOLEAN DEFAULT TRUE
);

-- ORDENES
CREATE TABLE ordenes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    numero_orden SERIAL, -- Generador simple, luego se formatea en APP
    codigo_orden VARCHAR(20) UNIQUE NOT NULL,
    paciente_id UUID REFERENCES pacientes(id) NOT NULL,
    medico_id UUID REFERENCES medicos(id),
    fecha_recepcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'PENDIENTE', -- PENDIENTE, PROCESO, VALIDADO, ENTREGADO
    usuario_creacion_id UUID REFERENCES usuarios(id),
    total DECIMAL(10,2) DEFAULT 0
);

-- DETALLE ORDEN (Items)
CREATE TABLE orden_detalles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    orden_id UUID REFERENCES ordenes(id) NOT NULL,
    examen_id UUID REFERENCES examenes(id) NOT NULL,
    precio_cobrado DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE' -- PENDIENTE, PROCESO, VALIDADO
);

-- RESULTADOS
CREATE TABLE resultados (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    orden_detalle_id UUID REFERENCES orden_detalles(id) UNIQUE NOT NULL,
    valor VARCHAR(500),
    observacion_interna TEXT,
    observacion_reporte TEXT,
    es_patologico BOOLEAN DEFAULT FALSE,
    fecha_resultado TIMESTAMP,
    usuario_resultado_id UUID REFERENCES usuarios(id),
    fecha_validacion TIMESTAMP,
    usuario_validacion_id UUID REFERENCES usuarios(id)
);

-- HISTORIAL VERSIONES RESULTADOS
CREATE TABLE resultado_versiones (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    resultado_id UUID REFERENCES resultados(id),
    valor_anterior VARCHAR(500),
    motivo_cambio TEXT,
    usuario_modifico_id UUID REFERENCES usuarios(id),
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
