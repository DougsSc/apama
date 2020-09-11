-- CRIAR BASE DE DADOS petsys MANUALMENTE, PARA DEPOIS USAR ESSE SCRIPT

-- CREATE DATABASE petsys;
-- USE petsys;


-- -----------------------------------------------------
-- Table animal_porte
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal_porte CASCADE;

CREATE TABLE IF NOT EXISTS animal_porte (
  id SERIAL,
  descricao VARCHAR(60) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table animal_status
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal_status CASCADE;

CREATE TABLE IF NOT EXISTS animal_status (
  id SERIAL,
  descricao VARCHAR(20) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table cidade
-- -----------------------------------------------------
DROP TABLE IF EXISTS cidade CASCADE;

CREATE TABLE IF NOT EXISTS cidade (
  id SERIAL,
  descricao VARCHAR(45) NULL,
  uf CHAR(2) NULL,
  estado VARCHAR(80) NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table endereco
-- -----------------------------------------------------
DROP TABLE IF EXISTS endereco CASCADE;

CREATE TABLE IF NOT EXISTS endereco (
  id SERIAL,
  id_cidade INT NOT NULL,
  logradouro VARCHAR(80) NULL,
  numero VARCHAR(50) NULL,
  complemento VARCHAR(55) NULL,
  bairro VARCHAR(80) NULL,
  cep CHAR(8) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_endereco_cidade
    FOREIGN KEY (id_cidade)
    REFERENCES cidade (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_endereco_cidade_idx ON endereco (id_cidade ASC);


-- -----------------------------------------------------
-- Table animal
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal CASCADE;

CREATE TABLE IF NOT EXISTS animal (
  id SERIAL,
  id_animal_porte INT NOT NULL,
  id_animal_status INT NOT NULL,
  data_resgate DATE NULL,
  data_adocao DATE NULL,
  nome VARCHAR(45) NOT NULL,
  observacao TEXT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_animal_animal_porte
    FOREIGN KEY (id_animal_porte)
    REFERENCES animal_porte (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_animal_animal_status
    FOREIGN KEY (id_animal_status)
    REFERENCES animal_status (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_animal_animal_porte_idx ON animal (id_animal_porte ASC);
CREATE INDEX fk_animal_animal_status_idx ON animal (id_animal_status ASC);


-- -----------------------------------------------------
-- Table imagem
-- -----------------------------------------------------
DROP TABLE IF EXISTS imagem CASCADE;

CREATE TABLE IF NOT EXISTS imagem (
  id SERIAL,
  arquivo VARCHAR(90) NOT NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table animal_imagem
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal_imagem CASCADE;

CREATE TABLE IF NOT EXISTS animal_imagem (
  id SERIAL,
  id_animal INT NOT NULL,
  id_imagem INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_animal_has_imagem_animal
    FOREIGN KEY (id_animal)
    REFERENCES animal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_animal_has_imagem_imagem
    FOREIGN KEY (id_imagem)
    REFERENCES imagem (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_animal_has_imagem_imagem_idx ON animal_imagem (id_imagem ASC);
CREATE INDEX fk_animal_has_imagem_animal_idx ON animal_imagem (id_animal ASC);


-- -----------------------------------------------------
-- Table tratamento
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_tratamento CASCADE;

CREATE TABLE IF NOT EXISTS tipo_tratamento (
  id SERIAL,
  descricao VARCHAR(80) NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table animal_tratamento
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal_tratamento CASCADE;

CREATE TABLE IF NOT EXISTS animal_tratamento (
  id SERIAL,
  id_tipo_tratamento INT NOT NULL,
  id_animal INT NOT NULL,
  observacao VARCHAR(200) NULL,
  data_tratamento DATE NULL,
  proxima_data_tratamento DATE NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_vacina_has_animal_vacina
    FOREIGN KEY (id_tipo_tratamento)
    REFERENCES tipo_tratamento (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_vacina_has_animal_animal
    FOREIGN KEY (id_animal)
    REFERENCES animal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_vacina_has_animal_animal_idx ON animal_tratamento (id_animal ASC);
CREATE INDEX fk_vacina_has_animal_vacina_idx ON animal_tratamento (id_tipo_tratamento ASC);


-- -----------------------------------------------------
-- Table tipo_pessoa
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_pessoa CASCADE;

CREATE TABLE IF NOT EXISTS tipo_pessoa (
  id SERIAL,
  descricao VARCHAR(80) NOT NULL,
  acessa_sistema INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table pessoa
-- -----------------------------------------------------
DROP TABLE IF EXISTS pessoa CASCADE;

CREATE TABLE IF NOT EXISTS pessoa (
  id SERIAL,
  id_tipo_pessoa INT NOT NULL,
  status INT NOT NULL DEFAULT 1,			-- se  for 1 = ativo, 0 = inativo
  liberar_adocao INT NOT NULL DEFAULT 1,	-- se  for 1 = liberar adoção, 0 = não liberar
  PRIMARY KEY (id),
  CONSTRAINT fk_pessoa_tipo_pessoa
    FOREIGN KEY (id_tipo_pessoa)
    REFERENCES tipo_pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_pessoa_tipo_id_pessoa ON pessoa (id_tipo_pessoa ASC);


-- -----------------------------------------------------
-- Table pessoa_fisica
-- -----------------------------------------------------
DROP TABLE IF EXISTS pessoa_fisica CASCADE;

CREATE TABLE IF NOT EXISTS pessoa_fisica (
  id_pessoa SERIAL,
  nome VARCHAR(80) NOT NULL,
  cpf CHAR(11) NOT NULL,
  PRIMARY KEY (id_pessoa),
  CONSTRAINT fk_pessoa_fisica_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_pessoa_fisica_id_pessoax ON pessoa_fisica (id_pessoa ASC);


-- -----------------------------------------------------
-- Table pessoa_juridica
-- -----------------------------------------------------
DROP TABLE IF EXISTS pessoa_juridica CASCADE;

CREATE TABLE IF NOT EXISTS pessoa_juridica (
  id_pessoa SERIAL,
  razao_social VARCHAR(55) NOT NULL,
  nome_fantasia VARCHAR(50) NULL,
  cnpj CHAR(14) NOT NULL,
  PRIMARY KEY (id_pessoa),
  CONSTRAINT fk_pessoa_juridica_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table tipo_contato
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_contato CASCADE;

CREATE TABLE IF NOT EXISTS tipo_contato (
  id SERIAL,
  descricao VARCHAR(50) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table contato
-- -----------------------------------------------------
DROP TABLE IF EXISTS contato CASCADE;

CREATE TABLE IF NOT EXISTS contato (
  id SERIAL,
  id_pessoa INT NOT NULL,
  id_tipo_contato INT NOT NULL,
  contato VARCHAR(100) NOT NULL,
  observacao TEXT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_contato_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_contato_tipo_contato
    FOREIGN KEY (id_tipo_contato)
    REFERENCES tipo_contato (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_contato_id_pessoax ON contato (id_pessoa ASC);
CREATE INDEX fk_contato_id_tipo_contatox ON contato (id_tipo_contato ASC);


-- -----------------------------------------------------
-- Table tipo_usuario
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_usuario CASCADE;

CREATE TABLE IF NOT EXISTS tipo_usuario (
  id SERIAL,
  descricao VARCHAR(80) NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table usuario
-- -----------------------------------------------------
DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE IF NOT EXISTS usuario (
  id_pessoa SERIAL,
  id_tipo_usuario INT NOT NULL,
  login VARCHAR(60) NULL,
  senha VARCHAR(55) NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id_pessoa),
  CONSTRAINT fk_usuario_pessoa_fisica
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa_fisica (id_pessoa)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_usuario_tipo_usuario
    FOREIGN KEY (id_tipo_usuario)
    REFERENCES tipo_usuario (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_usuario_tipo_usuario_idx ON usuario (id_tipo_usuario ASC);


-- -----------------------------------------------------
-- Table token
-- -----------------------------------------------------
DROP TABLE IF EXISTS token CASCADE;

CREATE TABLE IF NOT EXISTS token (
  id SERIAL,
  id_pessoa INT NOT NULL,
  token VARCHAR(255) NULL,
  data_validade VARCHAR(30) NULL,
  PRIMARY KEY (id),
  UNIQUE (token),
  CONSTRAINT fk_token_id_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES usuario (id_pessoa)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_token_id_pessoa_idx ON token (id_pessoa ASC);


-- -----------------------------------------------------
-- Table adocao_status
-- -----------------------------------------------------
DROP TABLE IF EXISTS adocao_status CASCADE;

CREATE TABLE IF NOT EXISTS adocao_status (
  id SERIAL,
  descricao VARCHAR(45) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table adocao
-- -----------------------------------------------------
DROP TABLE IF EXISTS adocao CASCADE;

CREATE TABLE IF NOT EXISTS adocao (
  id SERIAL,
  id_animal INT NOT NULL,
  id_pessoa_tutor INT NOT NULL,
  id_pessoa_usuario INT NOT NULL,
  id_adocao_status INT NOT NULL,
  data_registro TIMESTAMP WITHOUT TIME ZONE NOT NULL NOT NULL,
  data_adocao DATE,
  observacao VARCHAR(200) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_adocao_animal
    FOREIGN KEY (id_animal)
    REFERENCES animal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_adocao_pessoa
    FOREIGN KEY (id_pessoa_tutor)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_adocao_usuario
    FOREIGN KEY (id_pessoa_usuario)
    REFERENCES usuario (id_pessoa)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_adocao_status
    FOREIGN KEY (id_adocao_status)
    REFERENCES adocao_status (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_adocao_animal_idx ON adocao (id_animal ASC);
CREATE INDEX fk_adocao_id_pessoax ON adocao (id_pessoa_tutor ASC);
CREATE INDEX fk_adocao_usuadocaoario_idx ON adocao (id_pessoa_usuario ASC);
CREATE INDEX fk_adocao_status_idx ON adocao (id_adocao_status ASC);


-- -----------------------------------------------------
-- Table tipo_doacao
-- -----------------------------------------------------
DROP TABLE IF EXISTS adocao_imagem CASCADE;

CREATE TABLE IF NOT EXISTS adocao_imagem (
  id SERIAL,
  id_adocao INT NOT NULL,
  id_imagem INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_adocao_has_imagem_adocao
    FOREIGN KEY (id_adocao)
    REFERENCES adocao (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_adocao_has_imagem_imagem
    FOREIGN KEY (id_imagem)
    REFERENCES imagem (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_adocao_has_imagem_imagem_idx ON adocao_imagem (id_imagem ASC);
CREATE INDEX fk_adocao_has_imagem_adocao_idx ON adocao_imagem (id_adocao ASC);


-- -----------------------------------------------------
-- Table tipo_doacao
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_doacao CASCADE;

CREATE TABLE IF NOT EXISTS tipo_doacao (
  id SERIAL,
  descricao VARCHAR(90) NULL,
  unidade_medida VARCHAR(45) NULL,
  mascara VARCHAR(5) NULL,
  exige_valor INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table doacao
-- -----------------------------------------------------
DROP TABLE IF EXISTS doacao CASCADE;

CREATE TABLE IF NOT EXISTS doacao (
  id SERIAL,
  id_pessoa INT NOT NULL,
  id_tipo_doacao INT NOT NULL,
  valor DECIMAL(10,2) NULL,
  observacao TEXT NULL,
  data DATE NULL,
  status INT NOT NULL DEFAULT 1,
  justificativa TEXT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_doacao_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_doacao_tipo_doacao
    FOREIGN KEY (id_tipo_doacao)
    REFERENCES tipo_doacao (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE INDEX fk_doacao_id_pessoax ON doacao (id_pessoa ASC);
CREATE INDEX fk_doacao_tipo_doacao_idx ON doacao (id_tipo_doacao ASC);


-- -----------------------------------------------------
-- Table tipo_permissao
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_permissao CASCADE;

CREATE TABLE IF NOT EXISTS tipo_permissao (
  id SERIAL,
  descricao VARCHAR(80) NULL,
  PRIMARY KEY (id));


-- -----------------------------------------------------
-- Table tipo_usuario_permissao
-- -----------------------------------------------------
DROP TABLE IF EXISTS tipo_usuario_permissao CASCADE;

CREATE TABLE IF NOT EXISTS tipo_usuario_permissao (
  id SERIAL,
  id_tipo_usuario INT NOT NULL,
  id_tipo_permissao INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_tipo_usuario_has_tipo_permissao_tipo_usuario
    FOREIGN KEY (id_tipo_usuario)
    REFERENCES tipo_usuario (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_tipo_usuario_has_tipo_permissao_tipo_permissao
    FOREIGN KEY (id_tipo_permissao)
    REFERENCES tipo_permissao (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_tipo_usuario_has_tipo_permissao_tipo_permissao_idx ON tipo_usuario_permissao (id_tipo_permissao ASC);
CREATE INDEX fk_tipo_usuario_has_tipo_permissao_tipo_usuario_idx ON tipo_usuario_permissao (id_tipo_usuario ASC);


-- -----------------------------------------------------
-- Table animal_endereco
-- -----------------------------------------------------
DROP TABLE IF EXISTS animal_endereco CASCADE;

CREATE TABLE IF NOT EXISTS animal_endereco (
  id SERIAL,
  id_animal INT NOT NULL,
  id_endereco INT NOT NULL,
  data_ativacao TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  data_inativacao TIMESTAMP WITHOUT TIME ZONE,
  PRIMARY KEY (id),
  CONSTRAINT fk_animal_endereco_animal
    FOREIGN KEY (id_animal)
    REFERENCES animal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_animal_endereco_endereco
    FOREIGN KEY (id_endereco)
    REFERENCES endereco (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_animal_endereco_animal_idx ON animal_endereco (id_animal ASC);
CREATE INDEX fk_animal_endereco_endereco_idx ON animal_endereco (id_endereco ASC);


-- -----------------------------------------------------
-- Table pessoa_endereco
-- -----------------------------------------------------
DROP TABLE IF EXISTS pessoa_endereco CASCADE;

CREATE TABLE IF NOT EXISTS pessoa_endereco (
  id SERIAL,
  id_pessoa INT NOT NULL,
  id_endereco INT NOT NULL,
  data_ativacao TIMESTAMP WITHOUT TIME ZONE NOT NULL NOT NULL,
  data_inativacao TIMESTAMP WITHOUT TIME ZONE,
  PRIMARY KEY (id),
  CONSTRAINT fk_pessoa_endereco_pessoa
    FOREIGN KEY (id_pessoa)
    REFERENCES pessoa (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_pessoa_endereco_endereco
    FOREIGN KEY (id_endereco)
    REFERENCES endereco (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE INDEX fk_pessoa_endereco_pessoa_idx ON pessoa_endereco (id_pessoa ASC);
CREATE INDEX fk_pessoa_endereco_endereco_idx ON pessoa_endereco (id_endereco ASC);


-- INSERTs TABELA tipo_permissao
INSERT INTO public.tipo_permissao (descricao) VALUES ('Inserir');
INSERT INTO public.tipo_permissao (descricao) VALUES ('Visualizar');
INSERT INTO public.tipo_permissao (descricao) VALUES ('Editar');
INSERT INTO public.tipo_permissao (descricao) VALUES ('Excluir');


-- INSERTs TABELA tipo_pessoa
INSERT INTO public.tipo_pessoa (descricao, acessa_sistema) VALUES ('Voluntário', 1);
INSERT INTO public.tipo_pessoa (descricao, acessa_sistema) VALUES ('Tutor', 1);
INSERT INTO public.tipo_pessoa (descricao, acessa_sistema) VALUES ('Doador', 1);
INSERT INTO public.tipo_pessoa (descricao, acessa_sistema) VALUES ('Administrador', 1);

-- INSERTs TABELA animal_status
INSERT INTO public.animal_status (descricao, status) VALUES ('Disponível', 1);
INSERT INTO public.animal_status (descricao, status) VALUES ('Em tratamento', 1);
INSERT INTO public.animal_status (descricao, status) VALUES ('Adotado', 1);
INSERT INTO public.animal_status (descricao, status) VALUES ('Óbito', 1);

-- INSERTs TABELA animal_porte
INSERT INTO public.animal_porte (descricao, status) VALUES ('Pequeno', 1);
INSERT INTO public.animal_porte (descricao, status) VALUES ('Médio', 1);
INSERT INTO public.animal_porte (descricao, status) VALUES ('Grande', 1);

-- INSERTs TABELA adocao_status
INSERT INTO public.adocao_status (descricao, status) VALUES ('Aguardando aprovação', 1);
INSERT INTO public.adocao_status (descricao, status) VALUES ('Ausência de informações', 1);
INSERT INTO public.adocao_status (descricao, status) VALUES ('Aprovado', 1);
INSERT INTO public.adocao_status (descricao, status) VALUES ('Rejeitado', 1);
INSERT INTO public.adocao_status (descricao, status) VALUES ('Cancelado/Inativo', 1);

-- INSERTs TABELA tipo_usuario
INSERT INTO public.tipo_usuario (descricao) VALUES ('Admin - APAMA');
INSERT INTO public.tipo_usuario (descricao) VALUES ('Voluntário - APAMA');

-- INSERTs TABELA cidade
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio do Meio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cruzeiro do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lajeado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Marques de Souza','RS','Rio Grande do Sul');
/*
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Aceguá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Água Santa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Agudo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ajuricaba','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alecrim','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alegrete','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alegria','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Almirante Tamandaré do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alpestre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alto Alegre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alto Feliz','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Alvorada','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Amaral Ferrador','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ametista do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('André da Rocha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Anta Gorda','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Antônio Prado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arambaré','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Araricá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Aratiba','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio do Meio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio do Padre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio do Sal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio do Tigre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio dos Ratos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arroio Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Arvorezinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Augusto Pestana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Áurea','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bagé','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Balneário Pinhal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barão de Cotegipe','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barão do Triunfo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barra do Guarita','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barra do Quaraí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barra do Ribeiro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barra do Rio Azul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barra Funda','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barracão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Barros Cassal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Benjamin Constant do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bento Gonçalves','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boa Vista das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boa Vista do Buricá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boa Vista do Cadeado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boa Vista do Incra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boa Vista do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bom Jesus','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bom Princípio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bom Progresso','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bom Retiro do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Boqueirão do Leão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bossoroca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Bozano','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Braga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Brochier','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Butiá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caçapava do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cacequi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cachoeira do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cachoeirinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cacique Doble','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caibaté','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caiçara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Camaquã','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Camargo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cambará do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campestre da Serra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campina das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campinas do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campo Bom','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campo Novo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Campos Borges','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Candelária','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cândido Godói','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Candiota','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Canela','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Canguçu','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Canoas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Canudos do Vale','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capão Bonito do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capão da Canoa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capão do Cipó','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capão do Leão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capela de Santana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capitão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Capivari do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caraá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Carazinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Carlos Barbosa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Carlos Gomes','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Casca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caseiros','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Catuípe','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Caxias do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Centenário','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cerrito','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cerro Branco','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cerro Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cerro Grande do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cerro Largo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Chapada','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Charqueadas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Charrua','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Chiapetta','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Chuí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Chuvisca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cidreira','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ciríaco','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Colinas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Colorado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Condor','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Constantina','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coqueiro Baixo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coqueiros do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coronel Barros','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coronel Bicaco','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coronel Pilar','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cotiporã','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Coxilha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Crissiumal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cristal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cristal do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cruz Alta','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cruzaltense','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Cruzeiro do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('David Canabarro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Derrubadas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dezesseis de Novembro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dilermando de Aguiar','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dois Irmãos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dois Irmãos das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dois Lajeados','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dom Feliciano','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dom Pedrito','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dom Pedro de Alcântara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Dona Francisca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Doutor Maurício Cardoso','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Doutor Ricardo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Eldorado do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Encantado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Encruzilhada do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Engenho Velho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Entre Rios do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Entre-Ijuís','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Erebango','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Erechim','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ernestina','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Erval Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Erval Seco','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Esmeralda','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Esperança do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Espumoso','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Estação','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Estância Velha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Esteio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Estrela','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Estrela Velha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Eugênio de Castro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Fagundes Varela','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Farroupilha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Faxinal do Soturno','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Faxinalzinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Fazenda Vilanova','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Feliz','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Flores da Cunha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Floriano Peixoto','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Fontoura Xavier','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Formigueiro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Forquetinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Fortaleza dos Valos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Frederico Westphalen','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Garibaldi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Garruchos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gaurama','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('General Câmara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gentil','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Getúlio Vargas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Giruá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Glorinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gramado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gramado dos Loureiros','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gramado Xavier','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Gravataí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Guabiju','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Guaíba','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Guaporé','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Guarani das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Harmonia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Herval','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Herveiras','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Horizontina','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Hulha Negra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Humaitá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ibarama','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ibiaçá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ibiraiaras','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ibirapuitã','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ibirubá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Igrejinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ijuí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ilópolis','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Imbé','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Imigrante','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Independência','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Inhacorá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ipê','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ipiranga do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Iraí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itaara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itacurubi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itapuca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itaqui','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itati','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Itatiba do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ivorá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ivoti','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jaboticaba','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jacuizinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jacutinga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jaguarão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jaguari','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jaquirana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jari','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Jóia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Júlio de Castilhos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lagoa Bonita do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lagoa dos Três Cantos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lagoa Vermelha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lagoão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lajeado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lajeado do Bugre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lavras do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Liberato Salzano','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Lindolfo Collor','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Linha Nova','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Maçambara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Machadinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mampituba','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Manoel Viana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Maquiné','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Maratá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Marau','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Marcelino Ramos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mariana Pimentel','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mariano Moro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Marques de Souza','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mata','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mato Castelhano','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mato Leitão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mato Queimado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Maximiliano de Almeida','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Minas do Leão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Miraguaí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Montauri','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Monte Alegre dos Campos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Monte Belo do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Montenegro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mormaço','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Morrinhos do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Morro Redondo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Morro Reuter','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Mostardas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Muçum','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Muitos Capões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Muliterno','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Não-Me-Toque','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nicolau Vergueiro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nonoai','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Alvorada','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Araçá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Bassano','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Boa Vista','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Bréscia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Candelária','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Esperança do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Hartz','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Pádua','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Palma','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Petrópolis','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Prata','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Ramada','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Roma do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Nova Santa Rita','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Barreiro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Cabrais','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Hamburgo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Machado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Tiradentes','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Novo Xingu','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Osório','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Paim Filho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Palmares do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Palmeira das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Palmitinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Panambi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pantano Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Paraí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Paraíso do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pareci Novo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Parobé','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Passa Sete','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Passo do Sobrado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Passo Fundo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Paulo Bento','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Paverama','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pedras Altas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pedro Osório','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pejuçara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pelotas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Picada Café','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pinhal','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pinhal da Serra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pinhal Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pinheirinho do Vale','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pinheiro Machado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pirapó','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Piratini','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Planalto','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Poço das Antas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pontão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ponte Preta','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Portão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Porto Alegre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Porto Lucena','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Porto Mauá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Porto Vera Cruz','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Porto Xavier','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Pouso Novo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Presidente Lucena','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Progresso','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Protásio Alves','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Putinga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Quaraí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Quatro Irmãos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Quevedos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Quinze de Novembro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Redentora','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Relvado','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Restinga Seca','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rio dos Índios','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rio Grande','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rio Pardo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Riozinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Roca Sales','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rodeio Bonito','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rolador','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rolante','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ronda Alta','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rondinha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Roque Gonzales','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Rosário do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sagrada Família','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Saldanha Marinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Salto do Jacuí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Salvador das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Salvador do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sananduva','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Bárbara do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Cecília do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Clara do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Cruz do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Margarida do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Maria','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Maria do Herval','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Rosa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Tereza','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santa Vitória do Palmar','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santana da Boa Vista','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santana do Livramento','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santiago','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Ângelo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Antônio da Patrulha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Antônio das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Antônio do Palma','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Antônio do Planalto','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Augusto','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Cristo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Santo Expedito do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Borja','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Domingos do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Francisco de Assis','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Francisco de Paula','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Gabriel','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Jerônimo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São João da Urtiga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São João do Polêsine','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Jorge','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Herval','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Hortêncio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Inhacorá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Norte','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Ouro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São José dos Ausentes','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Leopoldo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Lourenço do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Luiz Gonzaga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Marcos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Martinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Martinho da Serra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Miguel das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Nicolau','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Paulo das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Pedro da Serra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Pedro das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Pedro do Butiá','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Pedro do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Sebastião do Caí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Sepé','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Valentim','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Valentim do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Valério do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Vendelino','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('São Vicente do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sapiranga','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sapucaia do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sarandi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Seberi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sede Nova','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Segredo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Selbach','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Senador Salgado Filho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sentinela do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Serafina Corrêa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sério','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sertão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sertão Santana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sete de Setembro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Severiano de Almeida','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Silveira Martins','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sinimbu','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Sobradinho','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Soledade','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tabaí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tapejara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tapera','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tapes','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Taquara','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Taquari','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Taquaruçu do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tavares','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tenente Portela','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Terra de Areia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Teutônia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tio Hugo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tiradentes do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Toropi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Torres','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tramandaí','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Travesseiro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Arroios','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Cachoeiras','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Coroas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três de Maio','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Forquilhas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Palmeiras','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Três Passos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Trindade do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Triunfo','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tucunduva','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tunas','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tupanci do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tupanciretã','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tupandi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Tuparendi','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Turuçu','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Ubiretama','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('União da Serra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Unistalda','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Uruguaiana','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vacaria','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vale do Sol','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vale Real','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vale Verde','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vanini','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Venâncio Aires','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vera Cruz','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Veranópolis','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vespasiano Correa','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Viadutos','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Viamão','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vicente Dutra','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Victor Graeff','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vila Flores','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vila Lângaro','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vila Maria','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vila Nova do Sul','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vista Alegre','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vista Alegre do Prata','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vista Gaúcha','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Vitória das Missões','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Westfalia','RS','Rio Grande do Sul');
INSERT INTO public.cidade (descricao, uf, estado) VALUES ('Xangri-lá','RS','Rio Grande do Sul');
*/

-- INSERTs TABELA pessoa
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (1, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);
INSERT INTO public.pessoa (id_tipo_pessoa, status, liberar_adocao) VALUES (4, 1, 1);

-- INSERTs TABELA pessoa_fisica
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (1, 'Camila', '21354561335');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (2, 'Douglas', '03265132151');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (3, 'Jórdan Finatto', '02526410002');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (4, 'Christian', '02341265854');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (5, 'Abílio Carballo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (6, 'Adolfo Ruela', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (7, 'Adélia Anhaia', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (8, 'Afonso Cayado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (9, 'Agostinho Sesimbra', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (10, 'Ajuricaba Fitas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (11, 'Alberta Moreira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (12, 'Alberto Franca', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (13, 'Alda Campelo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (14, 'Aldonça Pacheco', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (15, 'Alexandre Paes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (16, 'Almor Velasco', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (17, 'Aluísio Boga', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (18, 'Amanda Baldaia', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (19, 'Amanda Paiva', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (20, 'Amélia Azevedo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (21, 'Amílcar Lencastre', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (22, 'Anabela Morais', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (23, 'Andreoleto Boaventura', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (24, 'Anhangüera Hollanda', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (25, 'Antero Cyrne', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (26, 'Antónia Furquim', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (27, 'Antónia Matos', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (28, 'Artur Castilho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (29, 'Aurélio Carrasqueira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (30, 'Baltasar Granjeia', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (31, 'Belmiro Albernaz', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (32, 'Berengária Toledo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (33, 'Carminda Marques', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (34, 'Cauê Sequeira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (35, 'Caím Acevedo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (36, 'Celeste Conceição', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (37, 'Claudemira Hierro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (38, 'Claudemira Téllez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (39, 'Clementina Lira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (40, 'Cláudia Botelho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (41, 'Crispim Malta', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (42, 'Danilo Regalado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (43, 'Davide Quintero', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (44, 'Deise Toledo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (45, 'Derli Inês', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (46, 'Dirceu Rebelo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (47, 'Dorindo Lessa', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (48, 'Dália Goes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (49, 'Débora Anlicoara', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (50, 'Eduardo Freitas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (51, 'Egas Belchiorinho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (52, 'Eliseu Melgaço', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (53, 'Eládio Murtinho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (54, 'Epaminondas Torcato', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (55, 'Esperança Novalles', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (56, 'Estêvão Caldas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (57, 'Eudes Junqueira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (58, 'Fabiano Alvim', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (59, 'Fabíola Freitas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (60, 'Fernanda Rios', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (61, 'Filipe Gil', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (62, 'Filipe Sobral', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (63, 'Filomena Varella', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (64, 'Firmina Tévez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (65, 'Flor Lagoa', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (66, 'Floriano Silvera', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (67, 'Florêncio Baptista', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (68, 'Francisco Assumpção', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (69, 'Frutuoso Azambuja', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (70, 'Fábia Bulhões', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (71, 'Fábia Leão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (72, 'Gerson Olivares', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (73, 'Gertrudes Jaguariúna', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (74, 'Gerusa Magallanes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (75, 'Gláuber Vilanova', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (76, 'Gláucia Lira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (77, 'Gláucio Cayado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (78, 'Gláucio Sotomayor', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (79, 'Godinho ou Godim Jesús', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (80, 'Gomes Garcez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (81, 'Graciano Pérez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (82, 'Greice Bugallo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (83, 'Gualdim Foquiço', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (84, 'Guida Cardin', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (85, 'Gávio Fialho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (86, 'Henriqueta Varejão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (87, 'Honório Prado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (88, 'Horácio Moniz', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (89, 'Iara Furquim', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (90, 'Iara Quirós', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (91, 'Ifigénia Vellozo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (92, 'Ilduara Neiva', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (93, 'Ilídio Bicalho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (94, 'Ilídio Meirelles', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (95, 'Ingrit Varejão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (96, 'Iolanda Maciel', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (97, 'Iracema Galindo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (98, 'Iraci Grangeia', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (99, 'Isabel Carromeu', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (100, 'Isadora Vargas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (101, 'Itiberê Froes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (102, 'Jacinta Arruda', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (103, 'Jacir Graça', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (104, 'Jacira Cisneros', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (105, 'Jadir Porciúncula', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (106, 'Jerónimo, Jerônimo Caires', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (107, 'Jordana Gameiro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (108, 'Jordão Rego', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (109, 'Jorgina Bulhão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (110, 'Judá Gabeira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (111, 'Juçara Becerra', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (112, 'Juçara Junquera', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (113, 'Jéssica Trinidad', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (114, 'Júlia Barreiro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (115, 'Júlio Valério', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (116, 'Júlio Vilanova', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (117, 'Lavínia Morera', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (118, 'Leandro Barroso', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (119, 'Letícia Amado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (120, 'Lina Quintal', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (121, 'Lopo Pena', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (122, 'Lourenço Carmona', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (123, 'Lua Campelo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (124, 'Lúcia Rolim', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (125, 'Mariano Dorneles', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (126, 'Marisa Vale', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (127, 'Mateus Vieyra', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (128, 'Matias Aveiro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (129, 'Mário Padilla', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (130, 'Máxima Bautista', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (131, 'Máximo Meira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (132, 'Napoleão Pegado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (133, 'Nestor Barrios', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (134, 'Neusa Regueira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (135, 'Nicolau Carvajal', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (136, 'Nivaldo Francia', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (137, 'Nivaldo Soto Mayor', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (138, 'Noel Affonso', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (139, 'Nuno Granja', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (140, 'Nídia Mascareñas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (141, 'Olavo Casalinho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (142, 'Olavo Vieira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (143, 'Ondina Rosário', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (144, 'Orestes Lacerda', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (145, 'Osvaldo Hidalgo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (146, 'Otília Jiménez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (147, 'Patrícia Sardinha', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (148, 'Patrício Ramalho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (149, 'Polibe Borba', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (150, 'Polibe Carrasqueira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (151, 'Priscila Sousa de Arronches', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (152, 'Quirina Granja', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (153, 'Quitério Lameirinhas', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (154, 'Ramiro Valentín', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (155, 'Raquel Galvão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (156, 'Raul Freyre', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (157, 'Raul Nazário', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (158, 'Renata Peixoto', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (159, 'Rosalina Furtado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (160, 'Rosalinda Gómez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (161, 'Rosaura Marrero', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (162, 'Rosaura Prates', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (163, 'Roseli Amorín', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (164, 'Rubim Rebelo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (165, 'Rufus Menezes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (166, 'Rufus Severiano', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (167, 'Rute Azambuja', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (168, 'Sabrina Moniz', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (169, 'Sabrina Páez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (170, 'Selma Felgueiras', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (171, 'Silvana Hilário', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (172, 'Simeão Belo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (173, 'Socorro Beiriz', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (174, 'Sílvio Queiroz', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (175, 'Tabalipa Morales', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (176, 'Talita Ferrão', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (177, 'Teodoro Pérez', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (178, 'Teresa Lousado', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (179, 'Tobias Lameira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (180, 'Tobias Quaresma', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (181, 'Ubiratã Norões', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (182, 'Uriel Rosa', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (183, 'Valentina Nolasco', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (184, 'Valentina Silveira dos Açores', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (185, 'Valmor Corrêa', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (186, 'Valéria Vila-Chã', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (187, 'Vanderlei Bulhosa', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (188, 'Vanessa Carlos', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (189, 'Veridiana Osorio', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (190, 'Violeta Quintais', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (191, 'Viridiana Eiró', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (192, 'Viridiano Herrera', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (193, 'Viridiano Valentín', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (194, 'Viviana Ramos', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (195, 'Vlademiro Rebelo', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (196, 'Vânia Abelho', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (197, 'Xavier Sacramento', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (198, 'Zoraide Peralta', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (199, 'Zuleica Carneiro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (200, 'Zuriel Figueira', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (201, 'Zózimo Magallanes', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (202, 'Átila Carneiro', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (203, 'Ângela Quadros', '42645314201');
INSERT INTO public.pessoa_fisica (id_pessoa, nome, cpf) VALUES (204, 'Úrsula Domingos', '42645314201');



-- INSERTs TABELA usuario
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('camila', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 1, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('douglas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('jordan', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('christian', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Abílio Carballo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Adolfo Ruela', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Adélia Anhaia', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Afonso Cayado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Agostinho Sesimbra', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ajuricaba Fitas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Alberta Moreira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Alberto Franca', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Alda Campelo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Aldonça Pacheco', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Alexandre Paes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Almor Velasco', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Aluísio Boga', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Amanda Baldaia', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Amanda Paiva', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Amélia Azevedo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Amílcar Lencastre', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Anabela Morais', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Andreoleto Boaventura', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Anhangüera Hollanda', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Antero Cyrne', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Antónia Furquim', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Antónia Matos', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Artur Castilho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Aurélio Carrasqueira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Baltasar Granjeia', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Belmiro Albernaz', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Berengária Toledo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Carminda Marques', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Cauê Sequeira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Caím Acevedo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Celeste Conceição', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Claudemira Hierro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Claudemira Téllez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Clementina Lira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Cláudia Botelho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Crispim Malta', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Danilo Regalado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Davide Quintero', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Deise Toledo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Derli Inês', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Dirceu Rebelo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Dorindo Lessa', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Dália Goes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Débora Anlicoara', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Eduardo Freitas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Egas Belchiorinho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Eliseu Melgaço', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Eládio Murtinho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Epaminondas Torcato', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Esperança Novalles', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Estêvão Caldas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Eudes Junqueira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Fabiano Alvim', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Fabíola Freitas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Fernanda Rios', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Filipe Gil', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Filipe Sobral', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Filomena Varella', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Firmina Tévez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Flor Lagoa', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Floriano Silvera', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Florêncio Baptista', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Francisco Assumpção', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Frutuoso Azambuja', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Fábia Bulhões', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Fábia Leão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gerson Olivares', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gertrudes Jaguariúna', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gerusa Magallanes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gláuber Vilanova', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gláucia Lira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gláucio Cayado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gláucio Sotomayor', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Godinho ou Godim Jesús', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gomes Garcez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Graciano Pérez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Greice Bugallo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gualdim Foquiço', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Guida Cardin', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Gávio Fialho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Henriqueta Varejão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Honório Prado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Horácio Moniz', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Iara Furquim', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Iara Quirós', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ifigénia Vellozo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ilduara Neiva', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ilídio Bicalho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ilídio Meirelles', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ingrit Varejão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Iolanda Maciel', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Iracema Galindo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Iraci Grangeia', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Isabel Carromeu', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Isadora Vargas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Itiberê Froes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jacinta Arruda', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jacir Graça', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jacira Cisneros', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jadir Porciúncula', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jerónimo, Jerônimo Caires', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jordana Gameiro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jordão Rego', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jorgina Bulhão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Judá Gabeira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Juçara Becerra', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Juçara Junquera', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Jéssica Trinidad', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Júlia Barreiro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Júlio Valério', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Júlio Vilanova', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lavínia Morera', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Leandro Barroso', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Letícia Amado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lina Quintal', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lopo Pena', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lourenço Carmona', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lua Campelo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Lúcia Rolim', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Mariano Dorneles', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Marisa Vale', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Mateus Vieyra', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Matias Aveiro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Mário Padilla', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Máxima Bautista', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Máximo Meira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Napoleão Pegado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nestor Barrios', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Neusa Regueira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nicolau Carvajal', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nivaldo Francia', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nivaldo Soto Mayor', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Noel Affonso', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nuno Granja', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Nídia Mascareñas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Olavo Casalinho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Olavo Vieira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ondina Rosário', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Orestes Lacerda', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Osvaldo Hidalgo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Otília Jiménez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Patrícia Sardinha', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Patrício Ramalho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Polibe Borba', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Polibe Carrasqueira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Priscila Sousa de Arronches', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Quirina Granja', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Quitério Lameirinhas', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ramiro Valentín', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Raquel Galvão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Raul Freyre', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Raul Nazário', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Renata Peixoto', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rosalina Furtado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rosalinda Gómez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rosaura Marrero', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rosaura Prates', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Roseli Amorín', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rubim Rebelo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rufus Menezes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rufus Severiano', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Rute Azambuja', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Sabrina Moniz', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Sabrina Páez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Selma Felgueiras', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Silvana Hilário', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Simeão Belo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Socorro Beiriz', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Sílvio Queiroz', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Tabalipa Morales', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Talita Ferrão', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Teodoro Pérez', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Teresa Lousado', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Tobias Lameira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Tobias Quaresma', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ubiratã Norões', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Uriel Rosa', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Valentina Nolasco', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Valentina Silveira dos Açores', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Valmor Corrêa', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Valéria Vila-Chã', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Vanderlei Bulhosa', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Vanessa Carlos', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Veridiana Osorio', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Violeta Quintais', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Viridiana Eiró', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Viridiano Herrera', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Viridiano Valentín', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Viviana Ramos', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Vlademiro Rebelo', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Vânia Abelho', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Xavier Sacramento', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Zoraide Peralta', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Zuleica Carneiro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Zuriel Figueira', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Zózimo Magallanes', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Átila Carneiro', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Ângela Quadros', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);
INSERT INTO public.usuario (login, senha, id_tipo_usuario, status) VALUES ('Úrsula Domingos', '40BD001563085FC35165329EA1FF5C5ECBDBBEEF', 2, 1);

-- INSERTs TABELA endereco
INSERT INTO public.endereco (id_cidade, logradouro, numero, complemento, bairro, cep) VALUES (1, 'Av. Qualquer Uma', 123, 'Apto. 345', 'São Cristóvão', 95913160);
INSERT INTO public.endereco (id_cidade, logradouro, numero, complemento, bairro, cep) VALUES (2, 'Av. Qualquer Uma', 231, 'Apto. 453', 'São Cristóvão', 95913160);
INSERT INTO public.endereco (id_cidade, logradouro, numero, complemento, bairro, cep) VALUES (3, 'Av. Qualquer Uma', 132, 'Apto. 543', 'São Cristóvão', 95913160);
INSERT INTO public.endereco (id_cidade, logradouro, numero, complemento, bairro, cep) VALUES (4, 'Av. Qualquer Uma', 321, 'Apto. 534', 'São Cristóvão', 95913160);

-- INSERTs TABELA pessoa_endereco
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (1, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (2, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (3, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (4, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (5, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (6, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (7, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (8, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (9, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (10, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (11, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (12, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (13, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (14, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (15, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (16, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (17, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (18, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (19, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (20, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (21, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (22, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (23, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (24, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (25, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (26, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (27, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (28, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (29, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (30, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (31, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (32, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (33, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (34, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (35, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (36, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (37, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (38, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (39, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (40, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (41, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (42, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (43, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (44, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (45, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (46, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (47, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (48, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (49, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (50, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (51, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (52, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (53, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (54, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (55, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (56, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (57, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (58, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (59, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (60, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (61, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (62, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (63, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (64, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (65, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (66, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (67, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (68, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (69, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (70, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (71, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (72, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (73, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (74, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (75, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (76, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (77, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (78, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (79, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (80, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (81, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (82, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (83, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (84, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (85, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (86, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (87, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (88, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (89, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (90, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (91, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (92, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (93, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (94, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (95, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (96, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (97, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (98, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (99, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (100, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (101, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (102, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (103, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (104, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (105, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (106, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (107, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (108, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (109, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (110, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (111, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (112, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (113, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (114, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (115, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (116, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (117, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (118, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (119, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (120, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (121, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (122, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (123, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (124, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (125, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (126, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (127, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (128, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (129, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (130, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (131, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (132, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (133, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (134, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (135, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (136, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (137, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (138, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (139, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (140, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (141, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (142, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (143, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (144, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (145, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (146, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (147, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (148, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (149, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (150, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (151, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (152, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (153, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (154, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (155, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (156, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (157, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (158, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (159, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (160, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (161, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (162, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (163, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (164, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (165, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (166, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (167, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (168, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (169, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (170, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (171, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (172, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (173, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (174, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (175, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (176, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (177, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (178, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (179, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (180, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (181, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (182, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (183, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (184, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (185, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (186, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (187, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (188, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (189, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (190, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (191, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (192, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (193, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (194, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (195, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (196, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (197, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (198, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (199, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (200, 4, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (201, 1, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (202, 2, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (203, 3, NOW());
INSERT INTO public.pessoa_endereco (id_pessoa, id_endereco, data_ativacao) VALUES (204, 4, NOW());

-- INSERTs TABELA tipo_usuario_permissao
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (1, 1);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (1, 2);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (1, 3);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (1, 4);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (2, 1);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (2, 2);
INSERT INTO public.tipo_usuario_permissao (id_tipo_usuario, id_tipo_permissao) VALUES (2, 3);

-- INSERTs TABELA tipo_contato
INSERT INTO public.tipo_contato (descricao, status) VALUES ('Telefone', 1);
INSERT INTO public.tipo_contato (descricao, status) VALUES ('Celular', 1);
INSERT INTO public.tipo_contato (descricao, status) VALUES ('E-mail', 1);

-- INSERTs TABELA animal
INSERT INTO public.animal (id_animal_porte, id_animal_status, data_resgate, nome) VALUES (1, 1, '2019-10-28', 'Lili');
INSERT INTO public.animal (id_animal_porte, id_animal_status, data_resgate, nome) VALUES (2, 1, '2019-10-28', 'Thor');
INSERT INTO public.animal (id_animal_porte, id_animal_status, data_resgate, nome) VALUES (2, 2, '2019-10-28', 'Fera');
INSERT INTO public.animal (id_animal_porte, id_animal_status, data_resgate, nome) VALUES (3, 3, '2019-10-28', 'Rex');
INSERT INTO public.animal (id_animal_porte, id_animal_status, data_resgate, nome) VALUES (3, 3, '2019-10-28', 'Lobo');

-- INSERTs TABELA tipo_tratamento
INSERT INTO public.tipo_tratamento (descricao, status) VALUES ('Vacina', 1);
INSERT INTO public.tipo_tratamento (descricao, status) VALUES ('Castração', 1);

-- INSERTs TABELA animal_tratamento
INSERT INTO public.animal_tratamento (id_tipo_tratamento, id_animal, observacao, data_tratamento, proxima_data_tratamento) VALUES (1, 2, 'Castrar somente após 30 dias', '2019-10-28', '2019-11-27');
INSERT INTO public.animal_tratamento (id_tipo_tratamento, id_animal, observacao, data_tratamento, proxima_data_tratamento) VALUES (2, 3, 'Voltar após 45 dias', '2019-10-28', '2019-12-13');
INSERT INTO public.animal_tratamento (id_tipo_tratamento, id_animal, observacao, data_tratamento, proxima_data_tratamento) VALUES (2, 4, 'Voltar após 30 dias', '2019-10-28', '2019-10-27');

-- INSERTs TABELA adocao
INSERT INTO public.adocao (id_animal, id_pessoa_tutor, id_pessoa_usuario, id_adocao_status, data_registro, observacao) VALUES (4, 3, 3, 2, '2019-10-29', 'Aguardando a revisão do cadastro.');
INSERT INTO public.adocao (id_animal, id_pessoa_tutor, id_pessoa_usuario, id_adocao_status, data_registro, observacao) VALUES (5, 3, 3, 3, '2019-10-29', 'Falta documentos e preenchimento do questionário.');

-- INSERTs TABELA contato

-- Contatos Camila
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (1, 1, '5137141111', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (1, 2, '51999991111', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (1, 3, 'camila@camila.com.br', 'Contato de teste', 1);
-- Contatos Douglas
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (2, 1, '5137142222', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (2, 2, '51999992222', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (2, 3, 'douglas@douglas.com.br', 'Contato de teste', 1);
-- Contatos Jordan
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (3, 1, '5137144444', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (3, 2, '51999994444', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (3, 3, 'jordan@jordan.com.br', 'Contato de teste', 1);
-- Contatos Christian
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (4, 1, '5137143333', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (4, 2, '51999993333', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (4, 3, 'christian@christian.com.br', 'Contato de teste', 1);


INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (5, 3, 'bordeaux55@optonline.net:j121903j', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (6, 3, 'krsscheer@centurytel.net:terry2', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (7, 3, 'mhelies@yahoo.com:skipternet1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (8, 3, 'marceldiaz.cdic@gmail.com:andrew00', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (9, 3, 'ting__1973@hotmail.com:joel2004', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (10, 3, 'nunemach@mho.com:7chelios', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (11, 3, 'bryanjiles@gmail.com:jile8948', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (12, 3, 'jonylc@gmail.com:karlmarx', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (13, 3, 'sculzboy@gmail.com:dagger1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (14, 3, 'reed327@msn.com:dime4600', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (15, 3, 'wolf2emt@optonline.net:kevince7', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (16, 3, 'cgingrich@gmail.com:littleal', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (17, 3, 'sdaggie11@tx.rr.com:avalon30', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (18, 3, 'knrod69@gmail.com:soggy1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (19, 3, 'green37@AOL.com:scout709', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (20, 3, 'tonyag354@yahoo.com:liam0605', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (21, 3, 'namezoo@gmail.com:tech5547', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (22, 3, 'michaelabreu8@gmail.com:mets66', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (23, 3, 'coletteschlichter@gmail.com:devon0831', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (24, 3, 'drsharonleon@optonline.net:za1210ck', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (25, 3, 'jimsjunkemail@ca.rr.com:quadquad', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (26, 3, 'khonzik@mac.com:doggie94', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (27, 3, 'misschrissy32@yahoo.com:willjosmom', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (28, 3, 'nhodgetts@gmail.com:1176ho', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (29, 3, 'massaqr@hotmail.com:metallica666', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (30, 3, 'yobanihidalgojr@gmail.com:Lol47114!', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (31, 3, 'ajmc9880@gmail.com:archie2001', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (32, 3, 'rusty2226@gmail.com:dragon66', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (33, 3, 'curranamy@hotmail.com:sean30', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (34, 3, 'nicholaslong217@gmail.com:pitcher24', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (35, 3, 'bridgett_barry@hotmail.com:fatbob1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (36, 3, 'adutcher133@gmail.com:wooting133', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (37, 3, 'achrist8@nycap.rr.com:freddy123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (38, 3, 'julian.kais98@googlemail.com:yannis7', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (39, 3, 'sally@jgslc.com:carter3897', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (40, 3, 'moore151@bellsouth.net:6800noah', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (41, 3, 'nacualyle3@gmail.com:ball/python123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (42, 3, 'dewitt.russ@gmail.com:zigzag99', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (43, 3, 'vareynolds@aol.com:gus331', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (44, 3, '34harv@cox.net:william07', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (45, 3, 'julie@gizmofish.com:lincoln25', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (46, 3, 'jonasgutten100@gmail.com:jonas12345678910', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (47, 3, 'brianorme@gmail.com:c1t1z3ns', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (48, 3, 'lbell124@gmail.com:Lostabc8', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (49, 3, 'costrander44@gmail.com:driver12', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (50, 3, 'schoeve@gmail.com:Jello666', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (51, 3, 'ebbe.finding@gmail.com:bs14dkmod', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (52, 3, 'korin@korinbarr.com:bitofjo', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (53, 3, 'jandbmack@comcast.net:Torikitty1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (54, 3, 'mmatkins1@gmail.com:hgq186', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (55, 3, 'mihael98@live.se:00800653f', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (56, 3, 'pavlifam@gmail.com:eatd09haamn', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (57, 3, 'maryowen@windstream.net:Emma2006', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (58, 3, 'magic.cherry@freenet.de:mittwoch#7', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (59, 3, 'dave505@gmail.com:z9a724ccb', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (60, 3, 'melissaagraff@gmail.com:stephen05', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (61, 3, 'mimipham25@yahoo.com:tommydevan83', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (62, 3, 'mrbillclark@gmail.com:smurfs01', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (63, 3, 'chipmunks94@hotmail.com:ian2007', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (64, 3, 'j9saks@aol.com:steve627', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (65, 3, 'shadow991965@hotmail.com:shadow99', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (66, 3, 'karlene.szekely@gmail.com:wickett5w', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (67, 3, 'kipfilipe@hotmail.com:paulo000', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (68, 3, 'Thedeboards@gmail.com:Herman12', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (69, 3, 'hnklose@gmail.com:ethan51410', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (70, 3, 'outerplains@hotmail.com:calvin77', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (71, 3, 'jkguy@woh.rr.com:j13k13g13', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (72, 3, 'mja219@hotmail.com:verve4', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (73, 3, 'ddsara@yahoo.com:avery6max3', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (74, 3, 'alex@bordenlawoffice.com:borden25', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (75, 3, 'chellmarie_h@yahoo.com:chellhill', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (76, 3, 'alex@shar.ca:123bob123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (77, 3, 'supremeostrich@gmail.com:ostrichk4w', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (78, 3, 'numenlumen@att.net:mugshot1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (79, 3, 'tsmealhernandez@att.net:mazdarx7', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (80, 3, 'advertjunkie@hotmail.com:Etf325385', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (81, 3, 'chuckkeith1@mac.com:banzli07', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (82, 3, 'chrystalbeth@msn.com:dashM107', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (83, 3, 'icongamersproductions@gmail.com:Gabriel1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (84, 3, 'jamietaylor1994@gmail.com:yellow123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (85, 3, 'thiagobin1999@hotmail.com:tiaguinho1999', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (86, 3, 'mjdaly22@gmail.com:22roma22', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (87, 3, 'mjvailco@aol.com:Battle03', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (88, 3, 'graciecups@gmail.com:crcnod03', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (89, 3, 'andibusz@gmx.ch:andi03', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (90, 3, 'elisa.c.souza@hotmail.com:elisasouza100', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (91, 3, 'Prjensen1@hotmail.com:jensen1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (92, 3, 'apstevens1@yahoo.com:1234qwerty', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (93, 3, 'senoperalta@gmail.com:per23518', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (94, 3, 'mulhollandgaz@aol.com:rubymulholland', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (95, 3, 'bruno.sader@hotmail.com:FGVMinhoca1@@', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (96, 3, 'cinnimonbear@hotmail.com:coconut333', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (97, 3, 'nilsdoehring@gmail.com:Vincent2004', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (98, 3, 'theo.mignotte@free.fr:quicou83', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (99, 3, 'charliecrittenden@gmail.com:orangecat27', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (100, 3, 'cleitch91@gmail.com:drake1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (101, 3, 'NitrofireCP@gmail.com:ash42516', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (102, 3, 'outofspace123123@gmail.com:123123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (103, 3, 'zearo298@gmail.com:shadow', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (104, 3, 'casperkrave@live.se:wille13', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (105, 3, 'benji.olsen6@gmail.com:Orangedoesntrhyme0', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (106, 3, 'dirk.kooij.dk@gmail.com:kim1dirk', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (107, 3, 'colleencolleenr@yahoo.com:alan1969', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (108, 3, 'sanna.paivanurmi@luukku.com:etarvaa12', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (109, 3, 'kathleen.brennan@insightbb.com:kathy55', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (110, 3, 'carlycoollol@gmail.com:10greenbottles', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (111, 3, 'prodigy27gaming@gmail.com:thall1127101324', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (112, 3, 'njamspike@gmail.com:1coffeelover', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (113, 3, 'obiwouane@hotmail.com:sou27comhe', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (114, 3, 'haooad12345@gmail.com:Mohammed3321095', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (115, 3, 'ArmstrongFam4@optonline.net:Bermuda1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (116, 3, 'allyg553@gmail.com:mkisawesome14', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (117, 3, 'giacodrudi@hotmail.it:LuNaWiNnI', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (118, 3, 'halfdust@att.net:judah09', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (119, 3, 'jj21685@gmail.com:thunder1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (120, 3, 'ignas2092@gmail.com:sngsng2092', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (121, 3, 'spoiledrottenfox@gmail.com:steph1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (122, 3, 'noahbosterman@gmail.com:dunkin99', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (123, 3, 'exporgames@outlook.com:123258456a', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (124, 3, 'mike.n.mr@gmail.com:nederland', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (125, 3, 'harrison@kloppers.com:harrison', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (126, 3, 'bartonjkroeger@yahoo.com:Husker69', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (127, 3, 'calebostrich@gmail.com:caleb11', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (128, 3, 'jaxonjaxoff12@yahoo.com:Fuckthis123', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (129, 3, 'feorave@hotmail.com:alibaba9', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (130, 3, 'limjay2000@gmail.com:daye0818', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (131, 3, 'domic3250@gmail.com:little1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (132, 3, 'nat.fache@wanadoo.fr:FAche59310', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (133, 3, 'bfcroke@gmail.com:Brendan00', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (134, 3, 'julian.fearne@gmail.com:arcticfox01', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (135, 3, 'fredsandbobs@hotmail.com:poopman147', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (136, 3, 'taiel_03@hotmail.com.ar:velazco10', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (137, 3, 'dlp416@hotmail.com:robloxrox101', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (138, 3, 'lwiseme@outlook.com:IGRADin20/20', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (139, 3, 'lukehavers17@gmail.com:EnderBoys', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (140, 3, 'hbelz@comcast.net:bunhead33', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (141, 3, 'craigkocur@gmail.com:prus92no', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (142, 3, 'littledafishq@gmail.com:ksh22365', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (143, 3, 'loganc01@yahoo.com:starwars', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (144, 3, 'mrallmighty@hotmail.nl:OGstuff', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (145, 3, 'mmark1993@gmail.com:aTARgatis123#$%', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (146, 3, 'cari_we@hotmail.com:majmaj', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (147, 3, 'kinnarirpatel@comcast.net:Shiv$1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (148, 3, 'sluchie@gmail.com:snakeyes', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (149, 3, 'Tina198080@gmx.de:ronny25', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (150, 3, 'hadleydolan@gmail.com:leigh2023', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (151, 3, 'shining.star.12321@gmail.com:daddy99', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (152, 3, 'pryme8@gmail.com:spqe61626', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (153, 3, 'dustin.kikuchi@gmail.com:dkkbingo23', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (154, 3, 'toyimp@gmail.com:kroe82', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (155, 3, 'vespertIne.mc@gmail.com:vespercore32', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (156, 3, 'mertdostol@hotmail.com:kabus22', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (157, 3, 'ketolabrad160@gmail.com:bjk9210051', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (158, 3, 'strom.kim@gmail.com:OGstuff', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (159, 3, 'tymekplays@gmail.com:BRIGHTON12', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (160, 3, 'xxfangxx1t@gmail.com:gabe12', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (161, 3, 'zannoni.daniela.fe@gmail.com:minecraft2003', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (162, 3, 'jbbiz95@gmail.com:greenday', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (163, 3, 'caian.lima@hotmail.com:1qw23er4', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (164, 3, 'matt.tesseract@gmail.com:r2d2c3po', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (165, 3, 'mischka66@gmail.com:prince66', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (166, 3, 'adnanhmd77@gmail.com:agent129', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (167, 3, 'verticallychallangedcutie@gmail.com:lwhite77', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (168, 3, 'brendonauger@gmail.com:tucker69', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (169, 3, 'aidan.townshend@gmail.com:dweezil', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (170, 3, 'krj1072@hotmail.com:jacob04', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (171, 3, 'lil_rocknroller@hotmail.com:dragonforce27', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (172, 3, '12fosse@gmail.com:aa2h7kgf9', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (173, 3, 'jujufernandes3003@gmail.com:juliafernandesdowse', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (174, 3, 'john.chibirka@verizon.net:alfisol1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (175, 3, 'brianmolenaar@live.nl:OGnamem8', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (176, 3, 'lynx757@gmail.com:psycho757', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (177, 3, 'simonp.mbox@gmail.com:dynasty73', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (178, 3, 'erewuoysa@gmail.com:april2590', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (179, 3, 'drizzt416@gmail.com:vader1337', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (180, 3, 'aragonderrick@gmail.com:dragonclaw99', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (181, 3, 'hobbes99@gmail.com:shavano', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (182, 3, 'pkchris02@yahoo.com:budder501', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (183, 3, 'mauriciodel30@yahoo.com:jjx30m', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (184, 3, 'jamesandrewcurtis@btinternet.com:cattymat', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (185, 3, 'jackson9w9@gmail.com:spencer', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (186, 3, 'lbtmitchell@hotmail.com:wildcat', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (187, 3, 'bl1nky_8_u@hotmail.com:jackson12398', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (188, 3, 'jmk4u14@gmail.com:idklol14', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (189, 3, 'm8forever@hotmail.de:admin89', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (190, 3, 'xandraj09@gmail.com:ajk314775', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (191, 3, 'adi99812@gmail.com:adik9988', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (192, 3, 'berkemnohutcu@windowslive.com:erdem337', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (193, 3, 'missyann2207@gmail.com:ipod23', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (194, 3, 'andrewkinthiseng@yahoo.com:andrew1017', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (195, 3, 'zachhunt@live.com:noodle6', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (196, 3, 'bradleyhornsey@gmail.com:brad1102', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (197, 3, 'lacront@yahoo.de:Rotauge10', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (198, 3, 'bardewitz@xplornet.com:bester1', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (199, 3, 'grant.bailey@verizon.net:polpol9', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (200, 3, 'kmfdmgodlike108@gmail.com:henrybuck108', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (201, 3, 'timmy.voet@gmail.com:mydogrigby', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (202, 3, 'nolifegam3r@gmail.com:runescape7', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (203, 3, 'viktor.gojkovic890@gmail.com:viktor063226228', 'Contato de teste', 1);
INSERT INTO public.contato (id_pessoa, id_tipo_contato, contato, observacao, status) VALUES (204, 3, 'charls.lopez@outlook.es:Bbanonymous19', 'Contato de teste', 1);
