# Projeto Task Manager - Backend com Spring Boot

Este é o repositório do backend para o Projeto Task Manager, desenvolvido utilizando o framework Spring Boot. Este backend oferece serviços e funcionalidades para a aplicação.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT Token
- MySQL
- Maven

## Instalação e Configuração

1. Clone este repositório:
```bash
git clone https://github.com/FelipeBert/Task-Manager.git
```

Edite o arquivo `application.properties` ou `application.yml` dentro do diretório `src/main/resources` com as informações do seu banco de dados.

# Script de Criação das Tabelas

A seguir estão os scripts SQL para criação das tabelas necessárias para o projeto:

### Tabela `users`

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL
);
```

## Tabela `tarefa`

```sql
CREATE TABLE tarefa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    local_date_time DATETIME,
    tarefa_status VARCHAR(50) NOT NULL
);
```

## Compile o projeto:

```
mvn clean install
```
O servidor estará em execução em `http://localhost:8080`.

- `src/main/java/com/fbert/TaskManager/`: Contém todo o código-fonte do backend.
- `entity/`: Entidades da Aplicação.
- `controllers/`: Controladores para manipulação de rotas e lógica de negócios.
- `dto/`: Data Transfer Objects (DTOs)
- `repositories/`: Repositórios para acesso aos dados.
- `services/`: Serviços para implementar a lógica de negócios.
- `infraestructure/`: Configurações de segurança e Exceções.
- `src/main/resources/`: Recursos estáticos e arquivos de configuração, como arquivos de propriedades e YAML.

## Licença

Este projeto está licenciado sob a [Licença MIT](https://opensource.org/licenses/MIT).