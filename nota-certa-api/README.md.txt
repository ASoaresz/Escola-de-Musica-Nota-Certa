# Nota Certa API

API REST desenvolvida como solução para o desafio técnico Escola de Música Nota Certa.

O sistema permite o gerenciamento de alunos, cursos e aulas de uma escola de música, incluindo cadastro, consulta, atualização de informações, agendamento de aulas, controle de status e aplicação de regras de negócio para garantir a integridade dos dados.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.2
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- PostgreSQL 15
- Maven
- Docker e Docker Compose
- JUnit e Mockito


## Funcionalidades

### Alunos
- Cadastro de alunos.
- Listagem de alunos com paginação.
- Consulta por ID.
- Atualização dos dados.
- Filtros por nome e cidade.
- Validação de e-mail único.

### Cursos
- Cadastro de cursos.
- Listagem de cursos com paginação.
- Consulta por ID.
- Atualização dos dados.
- Filtros por nome e instrumento.
- Validação de código único.

### Aulas
- Agendamento de aulas.
- Listagem de aulas com paginação.
- Consulta por ID.
- Atualização do status da aula.
- Filtros por status, modalidade, aluno, curso e período.
- Validação de disponibilidade de alunos e cursos.
- Prevenção de conflitos de horários.

## Estrutura da API

A aplicação disponibiliza os seguintes endpoints:

| Método | Endpoint | Descrição |
|---|---|---|
| POST | /alunos | Cadastrar aluno |
| GET | /alunos | Listar alunos |
| GET | /alunos/{id} | Consultar aluno |
| PUT | /alunos/{id} | Atualizar aluno |
| POST | /cursos | Cadastrar curso |
| GET | /cursos | Listar cursos |
| GET | /cursos/{id} | Consultar curso |
| PUT | /cursos/{id} | Atualizar curso |
| POST | /aulas | Agendar aula |
| GET | /aulas | Listar aulas |
| GET | /aulas/{id} | Consultar aula |
| PATCH | /aulas/{id}/status | Atualizar status da aula |

## Regras de negócio

A API implementa validações para garantir a integridade dos dados:

- Não permite e-mails duplicados entre alunos.
- Não permite códigos duplicados entre cursos.
- Não permite agendar aulas para alunos ou cursos inativos.
- Não permite duas aulas agendadas para o mesmo aluno no mesmo horário.
- Valores de aulas e preços dos cursos devem ser maiores que zero.
- O status de uma aula pode mudar de AGENDADA para REALIZADA ou CANCELADA.
- Aulas realizadas ou canceladas não podem ter seu status alterado novamente.
- Retorna os códigos HTTP adequados para situações de sucesso, validação, conflito e registros inexistentes.

## Configuração do ambiente

### Pré-requisitos

Para executar a aplicação, é necessário ter instalado:

- Java 21.
- Docker Desktop.
- Docker Compose.

### Banco de dados

A aplicação utiliza PostgreSQL com as seguintes configurações:

| Configuração | Valor |
|---|---|
| Banco de dados | escola-musica |
| Usuário | postgres |
| Senha | postgres |
| Porta externa | 5433 |
| Porta interna | 5432 |

A conexão entre a API e o PostgreSQL é realizada internamente pelo Docker Compose.

### Executando com Docker

1. Clone o repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Entre na pasta do projeto:

```bash
cd nota-certa-api
```

3. Inicie os containers:

```bash
docker compose up -d --build
```

Esse comando inicia a API Spring Boot e o banco PostgreSQL.

4. Verifique os containers:

```bash
docker compose ps
```

5. Para consultar os logs da API:

```bash
docker compose logs -f api
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

## Execução dos testes

Os testes unitários dos Services foram implementados utilizando JUnit e Mockito.

Para executar os testes pelo Maven:

```bash
mvn test
```

## Persistência dos dados

Os dados são armazenados no PostgreSQL e persistidos por meio do Spring Data JPA e Hibernate.

O Docker Compose utiliza um volume para preservar os dados do banco, inclusive após a reinicialização dos containers.

## Tratamento de erros

A API utiliza códigos HTTP apropriados para representar os resultados das operações:

- 200 – Operação realizada com sucesso.
- 201 – Registro criado com sucesso.
- 400 – Dados inválidos.
- 404 – Registro não encontrado.
- 409 – Conflito com as regras de negócio.

## Autor

Desenvolvido como parte do Processo Seletivo de Estágio – Projeto Imersão Técnica.

**Projeto:** Nota Certa – Escola de Música.