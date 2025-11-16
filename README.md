# API E-Commerce "CataSucata" 🛒

Esta é uma API RESTful simples para um sistema de e-commerce (baseado no projeto "Cata Sucata"), desenvolvida em Java com Spring Boot.

O projeto implementa as funcionalidades essenciais de CRUD para Produtos (Itens), Clientes e Endereços. A principal funcionalidade de negócios é a **Finalização de Compra**, que cria um Pedido, calcula o valor total e atualiza o estoque do produto.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3** (com Spring Web e Spring Data JPA)
- **Hibernate**
- **MySQL**
- **Maven**

---

## 🚀 Como Rodar o Projeto (Passo a Passo)

### 1. Pré-requisitos

- Java 17 (JDK)
- Maven
- XAMPP/MySQL rodando na porta 3306
- MySQL Workbench, DBeaver ou phpMyAdmin
- Insomnia ou Postman

---

### 2. Clonar o Repositório

```bash
git clone https://github.com/AsaphGabriel/trabalho-api.git
cd trabalho-api
```

---

### 3. Configurar Banco de Dados

Crie o database:

```sql
CREATE DATABASE IF NOT EXISTS catasucata;
USE catasucata;
```

E execute o dump completo:

```sql
-- Tabela Produto (Item)
CREATE TABLE IF NOT EXISTS item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    preco DOUBLE PRECISION NOT NULL,
    estoque INT,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Tabela Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME(6),
    PRIMARY KEY (id_cliente)
) ENGINE=InnoDB;

-- Tabela Endereco
CREATE TABLE IF NOT EXISTS endereco (
    id_endereco BIGINT NOT NULL AUTO_INCREMENT,
    rua VARCHAR(255),
    cidade VARCHAR(255),
    cep VARCHAR(255),
    cliente_id BIGINT,
    PRIMARY KEY (id_endereco),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id_cliente)
) ENGINE=InnoDB;

-- Tabela Pedido
CREATE TABLE IF NOT EXISTS pedido (
    id_pedido BIGINT NOT NULL AUTO_INCREMENT,
    data_pedido DATETIME(6),
    status VARCHAR(255) NOT NULL,
    valor_total DOUBLE PRECISION,
    cliente_id BIGINT,
    PRIMARY KEY (id_pedido),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id_cliente)
) ENGINE=InnoDB;

-- Tabela ItemPedido
CREATE TABLE IF NOT EXISTS item_pedido (
    pedido_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DOUBLE PRECISION,
    PRIMARY KEY (pedido_id, item_id),
    FOREIGN KEY (pedido_id) REFERENCES pedido (id_pedido),
    FOREIGN KEY (item_id) REFERENCES item (id)
) ENGINE=InnoDB;
```

---

### 4. Configurar o `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/catasucata
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 5. Rodar a Aplicação

```bash
.\mvnw spring-boot:run
```

---

# 📖 Guia de Endpoints

## 1. Itens (Produtos)

### Criar Item
- **POST** `http://localhost:8080/itens`
```json
{
    "nome": "Cadeira de Praia",
    "descricao": "Antiga, mas funcional.",
    "preco": 100.00,
    "estoque": 10
}
```

### Listar Itens
- **GET** `http://localhost:8080/itens`

### Buscar Item por ID
- **GET** `http://localhost:8080/itens/1`

### Atualizar Item
- **PUT** `http://localhost:8080/itens/1`
```json
{
    "nome": "Cadeira de Praia (Reformada)",
    "preco": 120.00,
    "estoque": 15
}
```

### Deletar Item
- **DELETE** `http://localhost:8080/itens/1`

---

## 2. Clientes

### Criar Cliente
- **POST** `http://localhost:8080/clientes`
```json
{
    "nome": "Asaph Felix",
    "email": "teste@exemplo.com",
    "senha": "123"
}
```

### Listar Clientes
- **GET** `http://localhost:8080/clientes`

---

## 3. Endereços

### Criar Endereço
- **POST** `http://localhost:8080/enderecos/1`
```json
{
    "rua": "Rua Principal",
    "cidade": "Cidade Teste",
    "cep": "00000000"
}
```

---

## 4. Finalização de Compra (Funcionalidade Principal)

### Fluxo Obrigatório Antes do Teste

1. Criar Cliente  
2. Criar Item (com estoque)  
3. Criar Endereço para o Cliente  

### Finalizar Compra
- **POST** `http://localhost:8080/pedidos/1`
```json
[
  {
    "item": { "id": 1 },
    "quantidade": 3
  }
]
```

Resposta esperada: **201 Created**, com `valorTotal = 300.00`.

---

## Verificar Estoque
- **GET** `http://localhost:8080/itens/1`  
Resultado esperado: `"estoque": 7`

---

## Teste de Estoque Insuficiente
- **POST** `http://localhost:8080/pedidos/1`
```json
[
  {
    "item": { "id": 1 },
    "quantidade": 10
  }
]
```

Resposta: **400 Bad Request** — "Estoque insuficiente".

