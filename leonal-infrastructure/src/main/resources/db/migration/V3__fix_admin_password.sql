-- Actualizar password de admin con hash válido generado por BCrypt (cost 10)
-- Hash para 'admin123': $2a$10$T11ZqzL1ERztGhKeqnfn3.0cKVgN4qEfWoi6FW6PY4XjWHcW.0GB2

UPDATE usuarios 
SET password_hash = '$2a$10$T11ZqzL1ERztGhKeqnfn3.0cKVgN4qEfWoi6FW6PY4XjWHcW.0GB2'
WHERE username = 'admin';
