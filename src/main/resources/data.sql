INSERT INTO alunos (nome, email, telefone, cidade, data_cadastro, ativo)
VALUES
    ('Julia Nogueira', 'julia.nogueira@exemplo.com', '41987654321', 'Curitiba', CURRENT_TIMESTAMP, TRUE),
    ('Pedro Almeida', 'pedro.almeida@exemplo.com', '11987654321', 'Sao Paulo', CURRENT_TIMESTAMP, TRUE),
    ('Mariana Costa', 'mariana.costa@exemplo.com', '31987654321', 'Belo Horizonte', CURRENT_TIMESTAMP, TRUE),
    ('Lucas Ferreira', 'lucas.ferreira@exemplo.com', '21987654321', 'Rio de Janeiro', CURRENT_TIMESTAMP, TRUE),
    ('Beatriz Santos', 'beatriz.santos@exemplo.com', '41912345678', 'Curitiba', CURRENT_TIMESTAMP, TRUE),
    ('Rafael Oliveira', 'rafael.oliveira@exemplo.com', '11912345678', 'Sao Paulo', CURRENT_TIMESTAMP, FALSE),
    ('Camila Rodrigues', 'camila.rodrigues@exemplo.com', '31912345678', 'Belo Horizonte', CURRENT_TIMESTAMP, TRUE),
    ('Gabriel Martins', 'gabriel.martins@exemplo.com', '21912345678', 'Rio de Janeiro', CURRENT_TIMESTAMP, TRUE)
    ON CONFLICT (email) DO NOTHING;


INSERT INTO cursos (codigo, nome_curso, instrumento, preco_padrao, ativo)
VALUES
    ('VIO-01', 'Violao iniciante', 'VIOLAO', 100.00, TRUE),
    ('VIO-02', 'Violao intermediario', 'VIOLAO', 130.00, TRUE),
    ('PIA-01', 'Piano iniciante', 'PIANO', 150.00, TRUE),
    ('CAN-01', 'Canto iniciante', 'CANTO', 120.00, TRUE),
    ('BAT-01', 'Bateria iniciante', 'BATERIA', 160.00, TRUE),
    ('PIA-02', 'Piano avancado', 'PIANO', 180.00, FALSE)
    ON CONFLICT (codigo) DO NOTHING;


WITH aulas_demo (email_aluno, codigo_curso, data_hora, modalidade, valor_aula, status) AS (
    VALUES
        ('julia.nogueira@exemplo.com', 'VIO-01', TIMESTAMP '2026-09-01 09:00:00', 'PRESENCIAL', 100.00, 'REALIZADA'),
        ('pedro.almeida@exemplo.com', 'PIA-01', TIMESTAMP '2026-09-02 10:00:00', 'ONLINE', 150.00, 'REALIZADA'),
        ('mariana.costa@exemplo.com', 'CAN-01', TIMESTAMP '2026-09-03 14:00:00', 'PRESENCIAL', 120.00, 'REALIZADA'),
        ('lucas.ferreira@exemplo.com', 'BAT-01', TIMESTAMP '2026-09-04 15:00:00', 'ONLINE', 160.00, 'REALIZADA'),
        ('beatriz.santos@exemplo.com', 'VIO-02', TIMESTAMP '2026-09-05 09:00:00', 'PRESENCIAL', 130.00, 'REALIZADA'),
        ('camila.rodrigues@exemplo.com', 'PIA-01', TIMESTAMP '2026-09-06 10:00:00', 'ONLINE', 150.00, 'REALIZADA'),
        ('gabriel.martins@exemplo.com', 'VIO-01', TIMESTAMP '2026-09-07 14:00:00', 'PRESENCIAL', 100.00, 'REALIZADA'),

        ('julia.nogueira@exemplo.com', 'VIO-02', TIMESTAMP '2026-09-15 09:00:00', 'ONLINE', 130.00, 'REALIZADA'),
        ('pedro.almeida@exemplo.com', 'PIA-01', TIMESTAMP '2026-09-17 10:00:00', 'PRESENCIAL', 150.00, 'REALIZADA'),
        ('mariana.costa@exemplo.com', 'CAN-01', TIMESTAMP '2026-09-19 14:00:00', 'ONLINE', 120.00, 'REALIZADA'),
        ('lucas.ferreira@exemplo.com', 'BAT-01', TIMESTAMP '2026-09-21 15:00:00', 'PRESENCIAL', 160.00, 'REALIZADA'),
        ('beatriz.santos@exemplo.com', 'VIO-01', TIMESTAMP '2026-09-23 09:00:00', 'ONLINE', 100.00, 'REALIZADA'),
        ('camila.rodrigues@exemplo.com', 'PIA-01', TIMESTAMP '2026-09-25 10:00:00', 'PRESENCIAL', 150.00, 'REALIZADA'),
        ('gabriel.martins@exemplo.com', 'VIO-02', TIMESTAMP '2026-09-27 14:00:00', 'ONLINE', 130.00, 'REALIZADA'),

        ('julia.nogueira@exemplo.com', 'VIO-01', TIMESTAMP '2026-10-01 09:00:00', 'PRESENCIAL', 100.00, 'CANCELADA'),
        ('pedro.almeida@exemplo.com', 'PIA-01', TIMESTAMP '2026-10-03 10:00:00', 'ONLINE', 150.00, 'CANCELADA'),
        ('mariana.costa@exemplo.com', 'CAN-01', TIMESTAMP '2026-10-05 14:00:00', 'PRESENCIAL', 120.00, 'CANCELADA'),
        ('lucas.ferreira@exemplo.com', 'BAT-01', TIMESTAMP '2026-10-07 15:00:00', 'ONLINE', 160.00, 'CANCELADA'),
        ('beatriz.santos@exemplo.com', 'VIO-02', TIMESTAMP '2026-10-09 09:00:00', 'PRESENCIAL', 130.00, 'CANCELADA'),
        ('camila.rodrigues@exemplo.com', 'PIA-01', TIMESTAMP '2026-10-11 10:00:00', 'ONLINE', 150.00, 'CANCELADA'),
        ('gabriel.martins@exemplo.com', 'VIO-01', TIMESTAMP '2026-10-13 14:00:00', 'PRESENCIAL', 100.00, 'CANCELADA'),

        ('julia.nogueira@exemplo.com', 'VIO-02', TIMESTAMP '2026-10-20 09:00:00', 'ONLINE', 130.00, 'AGENDADA'),
        ('pedro.almeida@exemplo.com', 'PIA-01', TIMESTAMP '2026-10-22 10:00:00', 'PRESENCIAL', 150.00, 'AGENDADA'),
        ('mariana.costa@exemplo.com', 'CAN-01', TIMESTAMP '2026-10-24 14:00:00', 'ONLINE', 120.00, 'AGENDADA'),
        ('lucas.ferreira@exemplo.com', 'BAT-01', TIMESTAMP '2026-10-26 15:00:00', 'PRESENCIAL', 160.00, 'AGENDADA'),
        ('beatriz.santos@exemplo.com', 'VIO-01', TIMESTAMP '2026-10-28 09:00:00', 'ONLINE', 100.00, 'AGENDADA'),
        ('camila.rodrigues@exemplo.com', 'PIA-01', TIMESTAMP '2026-10-30 10:00:00', 'PRESENCIAL', 150.00, 'AGENDADA'),
        ('gabriel.martins@exemplo.com', 'VIO-02', TIMESTAMP '2026-11-01 14:00:00', 'ONLINE', 130.00, 'AGENDADA'),
        ('julia.nogueira@exemplo.com', 'VIO-01', TIMESTAMP '2026-11-05 09:00:00', 'PRESENCIAL', 100.00, 'AGENDADA'),
        ('pedro.almeida@exemplo.com', 'PIA-01', TIMESTAMP '2026-11-10 10:00:00', 'ONLINE', 150.00, 'AGENDADA')
)
INSERT INTO aulas (aluno_id, curso_id, data_hora, modalidade, valor_aula, status)
SELECT
    a.id,
    c.id,
    d.data_hora,
    d.modalidade,
    d.valor_aula,
    d.status
FROM aulas_demo d
         JOIN alunos a ON a.email = d.email_aluno
         JOIN cursos c ON c.codigo = d.codigo_curso
WHERE a.ativo = TRUE
  AND c.ativo = TRUE
  AND NOT EXISTS (
    SELECT 1
    FROM aulas existente
    WHERE existente.aluno_id = a.id
      AND existente.curso_id = c.id
      AND existente.data_hora = d.data_hora
      AND existente.modalidade = d.modalidade
      AND existente.valor_aula = d.valor_aula
      AND existente.status = d.status
);
