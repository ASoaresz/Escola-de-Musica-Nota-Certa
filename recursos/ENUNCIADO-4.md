# Caso 4 — Escola de música Nota Certa

A escola Nota Certa controla alunos, cursos e aulas dadas. Você vai construir a API que cadastra esses dados e grava tudo no PostgreSQL.

Os registros serão analisados depois em um painel. Por isso, data, valor, instrumento e modalidade precisam ser colunas de verdade (não texto solto dentro de um campo único).

Prazo sugerido: 1 semana. O problema é de propósito pequeno.

## O que entregar

Uma API REST em JSON com:

- Java 21 (já instalado)
- Spring Boot 3
- Spring Web, Spring Data JPA e Bean Validation
- PostgreSQL

Também entregue:

- um `README` explicando como criar o banco, configurar usuário/senha e subir a aplicação
- um script `dados.sql` que recria a carga de exemplo descrita mais abaixo

URL e porta local: `http://localhost:8080`.

Datas e horas vão no formato ISO-8601, **sem fuso**, por exemplo `2026-03-10T19:00:00`. Valores em reais usam número com ponto decimal, por exemplo `90.00`.

## Modelo de dados

São três tabelas. A aula é o centro: ela aponta para um aluno e para um curso. Aluno e curso não apontam um para o outro.

Quem está com `ativo = false` continua no banco. Aula antiga não é apagada.

### Alunos

Pessoa matriculada que faz aulas avulsas. Neste teste não existe tabela de matrícula: cada aula é registrada direto.

| Coluna | Descrição | Obrigatório | Observação |
| --- | --- | --- | --- |
| `id` | Identificador inteiro gerado pelo banco | sim | chave primária |
| `nome` | Nome do aluno | sim | de 3 a 120 caracteres |
| `email` | E-mail de contato | sim | formato de e-mail, único na tabela |
| `telefone` | Telefone com DDD | sim | só dígitos, 10 ou 11 caracteres |
| `cidade` | Cidade onde mora | sim | de 2 a 80 caracteres |
| `data_cadastro` | Momento em que o registro foi criado | sim | preenchido pela API, o cliente não envia |
| `ativo` | Se o aluno pode ter aulas novas | sim | nasce `true` |

### Cursos

Oferta da escola, por exemplo "Violão iniciante". O preço do curso é o preço de tabela. O valor da aula pode ser outro (reposição, desconto, aula experimental).

| Coluna | Descrição | Obrigatório | Observação |
| --- | --- | --- | --- |
| `id` | Identificador inteiro gerado pelo banco | sim | chave primária |
| `codigo` | Código curto do curso | sim | texto de 2 a 20 caracteres, único |
| `nome` | Nome do curso | sim | de 3 a 120 caracteres |
| `instrumento` | Instrumento principal | sim | `VIOLAO`, `PIANO`, `CANTO` ou `BATERIA` |
| `preco_padrao` | Preço de tabela de uma aula, em reais | sim | decimal com 2 casas, maior que zero |
| `ativo` | Se o curso aceita aulas novas | sim | nasce `true` |

### Aulas

Uma aula de um aluno em um curso. É a tabela que o painel vai usar como base (uma linha = uma aula).

| Coluna | Descrição | Obrigatório | Observação |
| --- | --- | --- | --- |
| `id` | Identificador inteiro gerado pelo banco | sim | chave primária |
| `aluno_id` | Aluno da aula | sim | chave estrangeira para `alunos` |
| `curso_id` | Curso da aula | sim | chave estrangeira para `cursos` |
| `data_hora` | Quando a aula acontece | sim | data e hora |
| `modalidade` | Onde a aula acontece | sim | `PRESENCIAL` ou `ONLINE` |
| `valor` | Valor cobrado por esta aula, em reais | sim | decimal com 2 casas, maior que zero |
| `status` | Situação da aula | sim | `AGENDADA`, `REALIZADA` ou `CANCELADA` |

O `POST` não recebe `status`. Toda aula nasce `AGENDADA`.

## Regras de negócio

1. Não pode existir dois alunos com o mesmo e-mail.
2. Não pode existir dois cursos com o mesmo `codigo`.
3. Aluno ou curso com `ativo = false` não entra em aula nova. A API responde `409`.
4. O mesmo aluno não pode ter duas aulas `AGENDADA` no mesmo `data_hora`. Aula realizada ou cancelada não ocupa esse horário.
5. `valor` e `preco_padrao` têm que ser maiores que zero. Os dois números não precisam ser iguais.
6. Status só muda assim:
   - `AGENDADA` → `REALIZADA`
   - `AGENDADA` → `CANCELADA`
   - `REALIZADA` e `CANCELADA` não mudam mais
7. Qualquer outra troca de status responde `409`.
8. Se o aluno ou o curso informado não existir, responde `404`.

## Contrato dos endpoints

Pedidos e respostas usam JSON (`Content-Type: application/json`).

Nomes no JSON ficam em camelCase (`dataHora`, `alunoId`). Nomes no banco ficam em snake_case (`data_hora`, `aluno_id`).

Códigos usados em todos os recursos:

| Situação | Código |
| --- | --- |
| Criou | `201` e o corpo do registro criado |
| Leu ou atualizou | `200` |
| Corpo inválido (campo faltando, formato, tamanho, valor ≤ 0) | `400` |
| Id que não existe | `404` |
| E-mail ou código repetido, aluno ou curso inativo, horário ocupado ou status inválido | `409` |

Pode usar o JSON de erro padrão do Spring. A mensagem precisa deixar claro qual regra falhou.

Listagens aceitam `page` (padrão `0`) e `size` (padrão `20`, máximo `100`). Resposta no formato paginado do Spring (`content`, `totalElements`, `totalPages`, `number`, `size`) ou neste envelope equivalente:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0
}
```

### Alunos

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/alunos` | Cadastra um aluno |
| `GET` | `/alunos` | Lista. Filtros opcionais: `nome` (contém, sem diferenciar maiúsculas) e `cidade` (igual) |
| `GET` | `/alunos/{id}` | Busca um aluno |
| `PUT` | `/alunos/{id}` | Atualiza nome, e-mail, telefone, cidade e ativo |

`POST /alunos`

```json
{
  "nome": "Julia Nogueira",
  "email": "julia@email.com",
  "telefone": "41987654321",
  "cidade": "Curitiba"
}
```

Resposta `201`:

```json
{
  "id": 1,
  "nome": "Julia Nogueira",
  "email": "julia@email.com",
  "telefone": "41987654321",
  "cidade": "Curitiba",
  "dataCadastro": "2026-03-01T10:15:00",
  "ativo": true
}
```

`PUT` envia os mesmos campos do `POST` mais `ativo`. Não envia `id` nem `dataCadastro`.

### Cursos

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/cursos` | Cadastra um curso |
| `GET` | `/cursos` | Lista. Filtros opcionais: `nome` (contém) e `instrumento` (igual) |
| `GET` | `/cursos/{id}` | Busca um curso |
| `PUT` | `/cursos/{id}` | Atualiza código, nome, instrumento, preço padrão e ativo |

`POST /cursos`

```json
{
  "codigo": "VIO-01",
  "nome": "Violão iniciante",
  "instrumento": "VIOLAO",
  "precoPadrao": 100.00
}
```

Resposta `201`:

```json
{
  "id": 1,
  "codigo": "VIO-01",
  "nome": "Violão iniciante",
  "instrumento": "VIOLAO",
  "precoPadrao": 100.00,
  "ativo": true
}
```

### Aulas

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/aulas` | Agenda uma aula |
| `GET` | `/aulas` | Lista. Filtros opcionais: `status`, `modalidade`, `alunoId`, `cursoId`, `dataInicio`, `dataFim` |
| `GET` | `/aulas/{id}` | Busca uma aula |
| `PATCH` | `/aulas/{id}/status` | Muda o status |

`dataInicio` e `dataFim` são data e hora. Entram as aulas com `data_hora` dentro do intervalo, inclusive nos dois limites. Pode informar só um dos dois.

`POST /aulas`

```json
{
  "alunoId": 1,
  "cursoId": 1,
  "dataHora": "2026-03-10T19:00:00",
  "modalidade": "PRESENCIAL",
  "valor": 90.00
}
```

Resposta `201`:

```json
{
  "id": 1,
  "alunoId": 1,
  "alunoNome": "Julia Nogueira",
  "cursoId": 1,
  "cursoNome": "Violão iniciante",
  "dataHora": "2026-03-10T19:00:00",
  "modalidade": "PRESENCIAL",
  "valor": 90.00,
  "status": "AGENDADA"
}
```

`PATCH /aulas/{id}/status`

```json
{
  "status": "REALIZADA"
}
```

A resposta é a aula já com o status novo. Realizar ou cancelar não altera valor, data nem vínculos.

Exemplo de listagem: `GET /aulas?status=AGENDADA&modalidade=ONLINE&page=0&size=20`

## Dados de exemplo

O `dados.sql` precisa poder ser executado em um banco vazio (crie as tabelas pela aplicação antes, ou inclua a criação no próprio script — escolha um dos dois e documente).

Volume mínimo:

- 8 alunos, em pelo menos 3 cidades diferentes
- 6 cursos, cobrindo os 4 instrumentos, pelo menos 1 com `ativo = false`
- 30 aulas espalhadas por pelo menos 60 dias
- os três status aparecem
- as duas modalidades aparecem
- nenhum aluno com duas aulas `AGENDADA` no mesmo horário
- valores variados, sempre maiores que zero

Use nomes fictícios. Não use dados de pessoas reais.

## Critérios de aceite

- A aplicação conecta no PostgreSQL e sobrevive a um reinício (o dado continua lá).
- Os 12 endpoints existem e respondem os códigos desta página.
- As oito regras de negócio são respeitadas.
- A listagem de aulas filtra e pagina.
- O `README` permite que outra pessoa suba o projeto.
- O `dados.sql` carrega o volume mínimo.
