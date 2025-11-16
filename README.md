API E-Commerce "CataSucata"
Esta é uma API RESTful simples para um sistema de e-commerce (baseado no projeto "Cata Sucata"), desenvolvida em Java com Spring Boot.

O projeto implementa as funcionalidades essenciais de CRUD para Produtos (Itens), Clientes e Endereços. A principal funcionalidade de negócios é a Finalização de Compra, que cria um Pedido, calcula o valor total e atualiza o estoque do produto.

🛠️ Tecnologias Utilizadas
Java 17

Spring Boot 3 (com Spring Web e Spring Data JPA)

Hibernate (Como implementação JPA)

MySQL (Banco de dados)

Maven (Gerenciador de dependências)

🚀 Como Rodar o Projeto (Passo a Passo)
Para executar esta API na sua máquina local, siga estes 5 passos.

1. Pré-requisitos
Java 17 (JDK) instalado.

Maven (geralmente já vem com o VS Code ou IntelliJ).

XAMPP (ou qualquer servidor MySQL) rodando na porta 3306.

MySQL Workbench (ou DBeaver/phpMyAdmin) para gerenciar o banco.

Insomnia (ou Postman) para testar os endpoints.

2. Clonar o Repositório
Bash

git clone [URL_DO_SEU_REPOSITORIO_AQUI]
cd catasucata
3. Configurar o Banco de Dados (MySQL)
A aplicação não cria o banco de dados automaticamente, apenas as tabelas. Você precisa criar o database catasucata primeiro.

Abra o XAMPP e dê "Start" no MySQL.

Abra o MySQL Workbench.

Execute o script SQL abaixo para criar o banco e todas as tabelas:

<details> <summary><b>Clique para ver o Script SQL (Dump)</b></summary>

SQL

-- Criação do Banco de Dados
CREATE DATABASE IF NOT EXISTS catasucata;
USE catasucata;

-- 1. Tabela Produto (Item)
CREATE TABLE IF NOT EXISTS item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    preco DOUBLE PRECISION NOT NULL,
    estoque INT, 
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- 2. Tabela Cliente
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME(6),
    PRIMARY KEY (id_cliente)
) ENGINE=InnoDB;

-- 3. Tabela Endereco
CREATE TABLE IF NOT EXISTS endereco (
    id_endereco BIGINT NOT NULL AUTO_INCREMENT,
    rua VARCHAR(255),
    cidade VARCHAR(255),
    cep VARCHAR(255), -- Aumentado de 10 para 255 (corrigido do log)
    cliente_id BIGINT,
    PRIMARY KEY (id_endereco),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id_cliente)
) ENGINE=InnoDB;

-- 4. Tabela Pedido
CREATE TABLE IF NOT EXISTS pedido (
    id_pedido BIGINT NOT NULL AUTO_INCREMENT,
    data_pedido DATETIME(6),
    status VARCHAR(255) NOT NULL, -- Aumentado de 50 para 255 (corrigido do log)
    valor_total DOUBLE PRECISION,
    cliente_id BIGINT, 
    PRIMARY KEY (id_pedido),
    FOREIGN KEY (cliente_id) REFERENCES cliente (id_cliente)
) ENGINE=InnoDB;

-- 5. Tabela de Relacionamento (ItemPedido)
CREATE TABLE IF NOT EXISTS item_pedido (
    pedido_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DOUBLE PRECISION,
    PRIMARY KEY (pedido_id, item_id),
    FOREIGN KEY (pedido_id) REFERENCES pedido (id_pedido),
    FOREIGN KEY (item_id) REFERENCES item (id)
) ENGINE=InnoDB;
</details>

4. Configurar o application.properties
Verifique se o arquivo src/main/resources/application.properties está com as credenciais corretas para o seu XAMPP (usuário root e senha vazia).

Properties

# CONFIGURAÇÃO DO MYSQL
spring.datasource.url=jdbc:mysql://localhost:3306/catasucata
spring.datasource.username=root
spring.datasource.password= 
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# CONFIGURAÇÃO DO HIBERNATE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
5. Rodar a Aplicação
Abra um terminal na raiz do projeto e execute o wrapper do Maven:

Bash

.\mvnw spring-boot:run
Aguarde até o terminal mostrar Started CatasucataApplication....

📖 Guia de Endpoints (Como Usar TUDO)
Aqui está o passo a passo para testar cada recurso que você criou, usando o Insomnia.

1. Módulo de Itens (Produtos)
Gerencia os produtos do e-commerce.

📦 Criar Item (com Estoque)
Método: POST

URL: http://localhost:8080/itens

Body (JSON):

JSON

{
    "nome": "Cadeira de Praia",
    "descricao": "Antiga, mas funcional.",
    "preco": 100.00,
    "estoque": 10 
}
Resposta: 201 Created

📦 Listar Itens
Método: GET

URL: http://localhost:8080/itens

📦 Buscar Item por ID
Método: GET

URL: http://localhost:8080/itens/1

📦 Atualizar Item
Método: PUT

URL: http://localhost:8080/itens/1

Body (JSON):

JSON

{
    "nome": "Cadeira de Praia (Reformada)",
    "preco": 120.00,
    "estoque": 15
}
📦 Deletar Item
Método: DELETE

URL: http://localhost:8080/itens/1

2. Módulo de Clientes
Gerencia o cadastro de usuários.

👤 Criar Cliente
Método: POST

URL: http://localhost:8080/clientes

Body (JSON):

JSON

{
    "nome": "Asaph Felix",
    "email": "teste@exemplo.com",
    "senha": "123"
}
Resposta: 201 Created

👤 Listar Clientes
Método: GET

URL: http://localhost:8080/clientes

3. Módulo de Endereços
Gerencia os endereços (ligados a um Cliente).

🏠 Cadastrar Endereço
Método: POST

URL: http://localhost:8080/enderecos/1 (O 1 é o clienteId)

Body (JSON):

JSON

{
    "rua": "Rua Principal",
    "cidade": "Cidade Teste",
    "cep": "00000000"
}
Resposta: 201 Created

🛒 4. Testando a Finalização da Compra (O GRANDE TESTE)
Esta é a funcionalidade principal. Ela requer que um Cliente e um Item já existam no banco.

Cenário: O Cliente (ID 1) quer comprar 3 unidades do Item (ID 1).

Sequência Completa (Obrigatória)
Siga estes 3 passos no Insomnia para garantir que os dados existam ANTES de tentar a compra:

POST http://localhost:8080/clientes (JSON do Cliente) -> Salva o Cliente ID 1.

POST http://localhost:8080/itens (JSON do Item com estoque: 10) -> Salva o Item ID 1.

POST http://localhost:8080/enderecos/1 (JSON do Endereço) -> Salva o Endereço do Cliente 1.

O Teste de Compra
Agora, execute a finalização:

Método: POST

URL: http://localhost:8080/pedidos/1 (O 1 é o clienteId)

Body (JSON):

JSON

[
  {
    "item": {
      "id": 1
    },
    "quantidade": 3
  }
]
Resposta Esperada: 201 Created com o JSON do Pedido (valorTotal = 300.00).

Verificação do Estoque
Para provar que a lógica funcionou:

Método: GET

URL: http://localhost:8080/itens/1

Resultado: O JSON do Item 1 deve retornar com "estoque": 7 (pois 10 - 3 = 7).

Teste de Falha (Estoque Insuficiente)
Tente comprar mais do que o estoque restante (mais de 7):

Método: POST

URL: http://localhost:8080/pedidos/1

Body (JSON):

JSON

[
  {
    "item": {
      "id": 1
    },
    "quantidade": 10
  }
]
Resposta Esperada: 400 Bad Request (com a mensagem de erro "Estoque insuficiente...").
