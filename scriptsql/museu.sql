
CREATE TABLE museu (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    capacidade_por_horario INTEGER NOT NULL,

    CONSTRAINT ck_museu_capacidade_positiva
        CHECK (capacidade_por_horario > 0)
);

CREATE TABLE horario_museu (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    museu_id BIGINT NOT NULL,
    hora_inicio TIME WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_horario_museu
        FOREIGN KEY (museu_id) REFERENCES museu (id),

    CONSTRAINT uq_horario_museu
        UNIQUE (museu_id, hora_inicio)
);

BEGIN;

CREATE TABLE agendamento (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    horario_museu_id BIGINT NOT NULL,
    data_visita DATE NOT NULL,
    email_responsavel VARCHAR(254) NOT NULL,
    codigo_confirmacao UUID NOT NULL,
    termo_aceito BOOLEAN NOT NULL,

    CONSTRAINT fk_agendamento_horario
        FOREIGN KEY (horario_museu_id) REFERENCES horario_museu (id),

    CONSTRAINT uq_agendamento_codigo
        UNIQUE (codigo_confirmacao),

    CONSTRAINT ck_agendamento_dia_visita
        CHECK (EXTRACT(ISODOW FROM data_visita) BETWEEN 2 AND 7),

    CONSTRAINT ck_agendamento_termo_aceito
        CHECK (termo_aceito = TRUE)
);

CREATE TABLE pessoa_agendada (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    agendamento_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    tipo_ingresso VARCHAR(30) NOT NULL,

    CONSTRAINT fk_pessoa_agendamento
        FOREIGN KEY (agendamento_id) REFERENCES agendamento (id)
        ON DELETE CASCADE,

    CONSTRAINT ck_pessoa_cpf_formato
        CHECK (cpf ~ '^[0-9]{11}$'),

    CONSTRAINT ck_pessoa_tipo_ingresso
        CHECK (tipo_ingresso IN (
            'INTEIRO',
            'MEIA_ENTRADA',
            'ISENTO',
            'MORADOR_CADASTRADO',
            'CONDUTOR_CREDENCIADO'
        ))
);

CREATE TABLE registro_comparecimento (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pessoa_agendada_id BIGINT NOT NULL,
    compareceu BOOLEAN NOT NULL,

    CONSTRAINT fk_comparecimento_pessoa
        FOREIGN KEY (pessoa_agendada_id) REFERENCES pessoa_agendada (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_comparecimento_pessoa
        UNIQUE (pessoa_agendada_id)
);

COMMIT;

BEGIN;

CREATE TABLE usuario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(60) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(15) NOT NULL,

    CONSTRAINT uq_usuario_login
        UNIQUE (login),

    CONSTRAINT ck_usuario_perfil
        CHECK (perfil IN ('ADMIN', 'FUNCIONARIO'))
);

CREATE TABLE funcionario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(11) NOT NULL,
    museu_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,

    CONSTRAINT fk_funcionario_museu
        FOREIGN KEY (museu_id) REFERENCES museu (id),

    CONSTRAINT fk_funcionario_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id),

    CONSTRAINT uq_funcionario_usuario
        UNIQUE (usuario_id),

    CONSTRAINT uq_funcionario_cpf
        UNIQUE (cpf),

    CONSTRAINT ck_funcionario_cpf_formato
        CHECK (cpf ~ '^[0-9]{11}$')
);

COMMIT;

BEGIN;

INSERT INTO museu (nome, capacidade_por_horario)
VALUES
    ('Museu Municipal de Barbacena', 5),
    ('Museu da Loucura', 10);

INSERT INTO horario_museu (museu_id, hora_inicio)
SELECT m.id, make_time(h.hora, 0, 0)
FROM museu AS m
JOIN (
    VALUES
        ('Museu Municipal de Barbacena', 8, 18),
        ('Museu da Loucura', 9, 17)
) AS faixas(nome, primeira_hora, ultima_hora)
    ON faixas.nome = m.nome
CROSS JOIN LATERAL
    generate_series(faixas.primeira_hora, faixas.ultima_hora) AS h(hora);

INSERT INTO usuario (login, senha_hash, perfil)
VALUES (
    'all',
    '$2a$12$xKZPvKjnto2QfyKMsWrSOO05IfSq2CNxhEdw1yIR8K0v6fkyv4aZS',
    'ADMIN'
);

COMMIT;

