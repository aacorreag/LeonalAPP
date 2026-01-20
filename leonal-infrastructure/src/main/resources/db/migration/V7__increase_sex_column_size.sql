-- Ampliar el tamaño del campo sexo en pacientes y rangos_referencia
ALTER TABLE pacientes DROP CONSTRAINT IF EXISTS pacientes_sexo_check;
ALTER TABLE pacientes ALTER COLUMN sexo TYPE VARCHAR(20);
ALTER TABLE pacientes ADD CONSTRAINT pacientes_sexo_check CHECK (sexo IN ('Masculino', 'Femenino', 'Otro', 'M', 'F', 'O'));

ALTER TABLE rangos_referencia ALTER COLUMN sexo TYPE VARCHAR(20);
