# Nota Certa API

API REST desenvolvida como solução para o desafio técnico **Escola de
Música Nota Certa**.

O sistema permite o gerenciamento de alunos, cursos e aulas de uma
escola de música, incluindo cadastro, consulta, atualização de
informações, agendamento de aulas, controle de status e aplicação de
regras de negócio para garantir a integridade dos dados.

## TECNOLOGIAS UTILIZADAS

-   Java 21
-   Spring Boot 4.1.2
-   Spring WebMVC
-   Spring Data JPA
-   Hibernate
-   Bean Validation
-   PostgreSQL 15
-   Maven
-   Docker e Docker Compose
-   JUnit 5
-   Mockito

## ARQUITETURA DO PROJETO

O projeto utiliza arquitetura em camadas, separando as responsabilidades
para facilitar a manutenção, organização e evolução da aplicação.

``` 
src/main/java
└── com.escola_musica.nota_certa
    ├── controller
    ├── dto
    │   ├── filter
    │   ├── request
    │   └── response
    ├── exception
    ├── validation
    ├── config
    ├── entity
    ├── enums
    ├── repository
    │   └── specification
    └── service
```

### Responsabilidades

-   **Controller:** exposição dos endpoints REST.
-   **Service:** implementação das regras de negócio.
-   **Repository:** acesso e persistência dos dados.
-   **Specification:** construção de filtros dinâmicos.
-   **DTO:** transferência de dados entre API e cliente.
-   **Entity:** representação das tabelas do banco de dados.
-   **Enum:** definição de valores controlados do domínio.
-   **Exception:** tratamento centralizado de erros.
-   **Validation:** validações específicas da aplicação.
-   **Config:** configurações da aplicação.

## FUNCIONALIDADES

### ALUNOS

-   Cadastro de alunos.
-   Consulta por ID.
-   Atualização de dados.
-   Listagem paginada.
-   Filtros por nome e cidade.
-   Validação de e-mail único.

### CURSOS

-   Cadastro de cursos.
-   Consulta por ID.
-   Atualização de dados.
-   Listagem paginada.
-   Filtros por nome e instrumento.
-   Validação de código único.

### AULAS

-   Agendamento de aulas.
-   Consulta por ID.
-   Atualização de status.
-   Listagem paginada.
-   Filtros por:
    -   Status.
    -   Modalidade.
    -   Aluno.
    -   Curso.
    -   Período.

## MODELO DE DADOS

O relacionamento entre as entidades é estruturado da seguinte forma:

``` 
Aluno (1) -------- (N) Aula (N) -------- (1) Curso
```

Cada aula pertence a um aluno e a um curso.

Um aluno pode possuir várias aulas, assim como um curso pode estar
associado a várias aulas.

## REGRAS DE NEGÓCIO IMPLEMENTADAS

O sistema implementa regras para garantir a consistência e integridade
dos dados:

-   Não permite alunos com e-mail duplicado.
-   Não permite cursos com código duplicado.
-   Não permite aulas para alunos inativos.
-   Não permite aulas para cursos inativos.
-   Não permite duas aulas agendadas para o mesmo aluno no mesmo
    horário.
-   O valor da aula deve ser maior que zero.
-   O preço padrão do curso deve ser maior que zero.

### TRANSIÇÕES DE STATUS

São permitidas apenas as seguintes transições:

``` 
AGENDADA → REALIZADA
AGENDADA → CANCELADA
```

Aulas com status REALIZADA ou CANCELADA não podem ser alteradas
posteriormente.

### TRATAMENTO DE ERROS

-   Entidades inexistentes retornam HTTP 404.
-   Violações de regras de negócio retornam HTTP 409.

## ENDPOINTS

A API disponibiliza os seguintes endpoints:

### ALUNOS

  Método   Endpoint         Descrição
  -------- ---------------- ------------------------
  POST     `/alunos`        Cadastrar aluno
  GET      `/alunos`        Listar alunos
  GET      `/alunos/{id}`   Consultar aluno por ID
  PUT      `/alunos/{id}`   Atualizar aluno

### CURSOS

  Método   Endpoint         Descrição
  -------- ---------------- ------------------------
  POST     `/cursos`        Cadastrar curso
  GET      `/cursos`        Listar cursos
  GET      `/cursos/{id}`   Consultar curso por ID
  PUT      `/cursos/{id}`   Atualizar curso

### AULAS

  Método   Endpoint               Descrição
  -------- ---------------------- --------------------------
  POST     `/aulas`               Agendar aula
  GET      `/aulas`               Listar aulas
  GET      `/aulas/{id}`          Consultar aula por ID
  PATCH    `/aulas/{id}/status`   Atualizar status da aula

## PAGINAÇÃO

Os endpoints de listagem suportam paginação através dos parâmetros
`page` e `size`.

Exemplos:

``` 
GET /alunos?page=0&size=20
GET /cursos?page=0&size=20
GET /aulas?page=0&size=20
```

### Parâmetros

-   `page`: número da página, iniciando em zero.
-   `size`: quantidade de registros por página.

O tamanho máximo permitido é de 100 registros por página.

## FILTROS

As consultas permitem a utilização de filtros dinâmicos.

### ALUNOS

``` 
GET /alunos?nome=julia
GET /alunos?cidade=São Paulo
```

### CURSOS

``` 
GET /cursos?nome=piano
GET /cursos?instrumento=PIANO
```

### AULAS

``` 
GET /aulas?status=AGENDADA
GET /aulas?modalidade=ONLINE
GET /aulas?alunoId=1
GET /aulas?cursoId=2
GET /aulas?dataInicio=2026-01-01T00:00:00
GET /aulas?dataFim=2026-01-31T23:59:59
```

Os filtros podem ser utilizados individualmente ou combinados conforme a
necessidade da consulta.

## EXEMPLO DE USO

### Criar Aluno

**Requisição:**

``` 
POST /alunos
Content-Type: application/json
```

**Corpo da requisição:**

``` 
{
  "nome": "Amanda Silva",
  "email": "amanda@email.com",
  "telefone": "11999999999",
  "cidade": "São Paulo"
}
```

**Resposta:**

``` 
{
  "id": 1,
  "nome": "Amanda Silva",
  "email": "amanda@email.com",
  "telefone": "11999999999",
  "cidade": "São Paulo",
  "ativo": true
}
```

## CONFIGURAÇÃO DO AMBIENTE

### Pré-requisitos

Para executar o projeto, são necessários:

-   Java 21.
-   Maven.
-   Docker Desktop.
-   Docker Compose.

## BANCO DE DADOS

A aplicação utiliza PostgreSQL 15 como banco de dados relacional.

### Configurações

  Configuração               Valor
  -------------------------- -----------------
  Banco de dados             `escola-musica`
  Usuário                    `postgres`
  Senha                      `postgres`
  Porta externa PostgreSQL   `5433`
  Porta interna PostgreSQL   `5432`
  Porta da API               `8080`

As configurações são definidas no arquivo `docker-compose.yml` e no
`application.properties`.

A API utiliza a porta interna 5432 para comunicação com o PostgreSQL
dentro da rede Docker, enquanto o acesso externo ao banco é realizado
pela porta 5433.

## EXECUTANDO COM DOCKER

Os comandos abaixo devem ser executados na pasta `nota-certa-api`, que
fica dentro da pasta clonada do repositório.

### 1. Clonar o repositório

``` 
git clone https://github.com/ASoaresz/Escola-de-Musica-Nota-Certa.git
```

### 2. Entrar na pasta do repositório

``` 
cd Escola-de-Musica-Nota-Certa
```

### 3. Entrar na pasta da API

``` 
cd nota-certa-api
```

### 4. Criar o volume persistente

O projeto utiliza um volume Docker externo chamado
`recursos_postgres_data`.

Criar o volume antes de iniciar os containers:

``` 
docker volume create recursos_postgres_data
```

### 5. Subir os containers

``` 
docker compose up -d --build
```

### 6. Verificar os containers

``` 
docker compose ps
```

### 7. Consultar os logs da API

``` 
docker compose logs -f api
```

Após a inicialização, a API estará disponível em:

``` 
http://localhost:8080
```

A aplicação e o PostgreSQL serão iniciados automaticamente através do
Docker Compose.

## EXECUTANDO LOCALMENTE

Para executar a aplicação fora do Docker, é necessário possuir uma
instância PostgreSQL disponível na porta 5433, com as credenciais
configuradas no projeto.

### 1. Criar o banco de dados

``` 
CREATE DATABASE "escola-musica";
```

### 2. Configurar as credenciais

No arquivo `application.properties`:

``` 
spring.datasource.url=jdbc:postgresql://localhost:5433/escola-musica
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Executar a aplicação

Na raiz da pasta `nota-certa-api`, executar:

``` 
mvn spring-boot:run
```

A API estará disponível em:

``` 
http://localhost:8080
```

## CARGA INICIAL DE DADOS

O projeto inclui o arquivo `data.sql`, localizado em
`src/main/resources/data.sql`, responsável pela carga inicial de dados
de alunos, cursos e aulas.

O script contém registros de exemplo com diferentes situações,
incluindo:

-   Alunos ativos e inativos.
-   Cursos ativos e inativos.
-   Aulas realizadas.
-   Aulas canceladas.
-   Aulas agendadas.

A inicialização dos scripts SQL é habilitada através da configuração:

``` 
spring.sql.init.mode=always
```

O script utiliza verificações para evitar a inserção de registros
duplicados.

## TESTES

Foram implementados testes unitários utilizando:

-   JUnit 5.
-   Mockito.

### Serviços testados

-   AlunoService.
-   CursoService.
-   AulaService.

### Cenários cobertos

-   Criação de registros.
-   Atualizações.
-   Consultas.
-   Regras de negócio.
-   Validações.
-   Conflitos de agendamento.
-   Alteração de status.
-   Entidades inexistentes.

### Executar os testes

Na raiz da pasta `nota-certa-api`, executar:

``` 
mvn test
```

## DECISÕES TÉCNICAS

As principais decisões técnicas adotadas no desenvolvimento foram:

-   Utilização de Spring Data JPA para persistência de dados.
-   Hibernate como implementação de ORM.
-   Filtros dinâmicos implementados com Specifications.
-   DTOs para desacoplamento entre entidades e camada de apresentação.
-   Bean Validation para validação dos dados recebidos.
-   Tratamento centralizado de exceções.
-   Docker Compose para orquestração da API e do banco de dados.
-   PostgreSQL como banco de dados relacional.
-   JUnit e Mockito para testes unitários.

## PERSISTÊNCIA DOS DADOS

Os dados são persistidos em PostgreSQL através do Spring Data JPA e
Hibernate.

O Docker Compose utiliza o volume externo:

``` text
recursos_postgres_data
```

Esse volume garante que os dados permaneçam armazenados mesmo após a
reinicialização ou recriação dos containers, desde que o volume não seja
removido.

## CÓDIGOS HTTP

  Código   Descrição
  -------- --------------------------------
  200      Operação realizada com sucesso
  201      Recurso criado com sucesso
  400      Dados inválidos
  404      Recurso não encontrado
  409      Conflito com regra de negócio

## AUTOR

**Amanda Soares**

Projeto desenvolvido como solução para o desafio técnico **Escola de
Música Nota Certa**.
