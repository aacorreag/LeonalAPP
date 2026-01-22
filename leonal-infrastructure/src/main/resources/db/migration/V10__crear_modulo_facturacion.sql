-- V10__crear_modulo_facturacion.sql
-- Migración para crear el módulo de Facturación y Caja

-- Tabla de Facturas
CREATE TABLE IF NOT EXISTS facturas (
    id UUID PRIMARY KEY NOT NULL,
    numero VARCHAR(20) NOT NULL UNIQUE,
    fecha_emision DATE NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    orden_id UUID NOT NULL,
    paciente_id UUID NOT NULL,
    paciente_nombre VARCHAR(255) NOT NULL,
    paciente_documento VARCHAR(50),
    subtotal NUMERIC(10,2) NOT NULL,
    descuento NUMERIC(10,2) DEFAULT 0,
    impuesto NUMERIC(10,2) DEFAULT 0,
    total NUMERIC(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'EMITIDA', -- BORRADOR, EMITIDA, PAGADA, ANULADA
    observaciones TEXT,
    usuario_creacion_id UUID,
    usuario_modificacion_id UUID,
    fecha_modificacion TIMESTAMP,
    CONSTRAINT fk_facturas_orden FOREIGN KEY (orden_id) REFERENCES ordenes(id),
    CONSTRAINT fk_facturas_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_facturas_usuario_creacion FOREIGN KEY (usuario_creacion_id) REFERENCES usuarios(id),
    CONSTRAINT fk_facturas_usuario_modificacion FOREIGN KEY (usuario_modificacion_id) REFERENCES usuarios(id)
);

-- Índices para Facturas
CREATE INDEX idx_facturas_numero ON facturas(numero);
CREATE INDEX idx_facturas_orden_id ON facturas(orden_id);
CREATE INDEX idx_facturas_paciente_id ON facturas(paciente_id);
CREATE INDEX idx_facturas_estado ON facturas(estado);
CREATE INDEX idx_facturas_fecha_emision ON facturas(fecha_emision);

-- Tabla de Pagos
CREATE TABLE IF NOT EXISTS pagos (
    id UUID PRIMARY KEY NOT NULL,
    factura_id UUID NOT NULL,
    monto NUMERIC(10,2) NOT NULL,
    forma_pago VARCHAR(50) NOT NULL, -- EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, CHEQUE, TRANSFERENCIA, OTRO
    fecha TIMESTAMP NOT NULL,
    referencia VARCHAR(100),
    observaciones TEXT,
    usuario_registro_id UUID NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO', -- REGISTRADO, PROCESADO, RECHAZADO
    fecha_creacion TIMESTAMP NOT NULL,
    CONSTRAINT fk_pagos_factura FOREIGN KEY (factura_id) REFERENCES facturas(id),
    CONSTRAINT fk_pagos_usuario FOREIGN KEY (usuario_registro_id) REFERENCES usuarios(id)
);

-- Índices para Pagos
CREATE INDEX idx_pagos_factura_id ON pagos(factura_id);
CREATE INDEX idx_pagos_estado ON pagos(estado);
CREATE INDEX idx_pagos_fecha ON pagos(fecha);
CREATE INDEX idx_pagos_usuario_registro_id ON pagos(usuario_registro_id);

-- Tabla de Sesiones de Caja
CREATE TABLE IF NOT EXISTS caja_sessions (
    id UUID PRIMARY KEY NOT NULL,
    usuario_cajero_id UUID NOT NULL,
    usuario_cajero_nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    hora_apertura TIMESTAMP NOT NULL,
    hora_cierre TIMESTAMP,
    monto_inicial NUMERIC(10,2) NOT NULL,
    monto_final NUMERIC(10,2),
    total_ingresos NUMERIC(10,2) NOT NULL DEFAULT 0,
    total_egresos NUMERIC(10,2) NOT NULL DEFAULT 0,
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTA', -- ABIERTA, CERRADA
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL,
    CONSTRAINT fk_caja_sessions_usuario FOREIGN KEY (usuario_cajero_id) REFERENCES usuarios(id)
);

-- Índices para Caja Sessions
CREATE INDEX idx_caja_sessions_usuario_cajero_id ON caja_sessions(usuario_cajero_id);
CREATE INDEX idx_caja_sessions_fecha ON caja_sessions(fecha);
CREATE INDEX idx_caja_sessions_estado ON caja_sessions(estado);
CREATE INDEX idx_caja_sessions_usuario_fecha ON caja_sessions(usuario_cajero_id, fecha);

-- Comentarios de tablas
COMMENT ON TABLE facturas IS 'Almacena las facturas generadas a partir de órdenes de pacientes';
COMMENT ON TABLE pagos IS 'Almacena los pagos realizados para cada factura';
COMMENT ON TABLE caja_sessions IS 'Almacena las sesiones de caja diarias abierto/cerrado';

-- Comentarios de columnas - Facturas
COMMENT ON COLUMN facturas.numero IS 'Número de factura único, formato: FAC-YYYY-NNNNNN';
COMMENT ON COLUMN facturas.estado IS 'Estado de la factura: BORRADOR (editable), EMITIDA (generada), PAGADA, ANULADA';
COMMENT ON COLUMN facturas.subtotal IS 'Subtotal antes de descuentos e impuestos';
COMMENT ON COLUMN facturas.descuento IS 'Monto de descuento aplicado';
COMMENT ON COLUMN facturas.impuesto IS 'Impuesto aplicado (IVA u otro)';
COMMENT ON COLUMN facturas.total IS 'Total = subtotal - descuento + impuesto';

-- Comentarios de columnas - Pagos
COMMENT ON COLUMN pagos.forma_pago IS 'Forma de pago: EFECTIVO, TARJETA_CREDITO, TARJETA_DEBITO, CHEQUE, TRANSFERENCIA, OTRO';
COMMENT ON COLUMN pagos.referencia IS 'Referencia de pago (número de cheque, número de transacción, etc.)';
COMMENT ON COLUMN pagos.estado IS 'Estado del pago: REGISTRADO, PROCESADO, RECHAZADO';

-- Comentarios de columnas - Caja Sessions
COMMENT ON COLUMN caja_sessions.estado IS 'Estado de la caja: ABIERTA (en uso), CERRADA (finalizada)';
COMMENT ON COLUMN caja_sessions.total_ingresos IS 'Suma de todos los pagos registrados durante la sesión';
COMMENT ON COLUMN caja_sessions.total_egresos IS 'Suma de retiros o gastos registrados durante la sesión';
