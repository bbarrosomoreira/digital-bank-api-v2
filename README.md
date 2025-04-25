# 💳 Banco Digital / Digital Bank

🇧🇷 Versão em português abaixo | 🇺🇸 English version below

---

## 🇺🇸 Digital Bank Project

This repository contains the second version of a **Digital Bank** project developed in Java:

### 📌 First Version: Basic Java 
👉 Check it out here: [digital-bank-java](https://github.com/bbarrosomoreira/digital-bank-java)
A simple and functional version, built only with Java (no frameworks), including:

- Account management
- Deposit, withdrawal, and transfer features
- Card and insurance structure
- Object-oriented approach

### 🚀 Second Version: Java + Spring Boot + JWT

An enhanced version using modern Java backend tools and best practices:

- Built with **Spring Boot**
- API with RESTful endpoints
- Security with **JWT Authentication**
- In-memory **H2 Database**
- JPA for ORM and entity persistence
- Role-based access (ADMIN / USER)
- Exception handling and custom error responses

### 🛠️ Technologies

- Java 17+
- Spring Boot
- Spring Security
- H2 Database
- JPA (Hibernate)
- JWT
- Maven

### ▶️ How to Run Locally

#### ✅ Requirements

- [Java 17 or higher](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [Eclipse IDE](https://www.eclipse.org/downloads/) or other IDE
- [Git](https://git-scm.com/) (optional)

#### 👣 Steps

1. **Clone the repository**

```bash
git clone https://github.com/bbarrosomoreira/digital-bank-api.git
```

2. **Import project in Eclipse**

- Go to `File > Import`
- Select `Existing Maven Projects`
- Choose the cloned folder
- Finish the import

3. **Configure environment variables**

Create a `.env` file at the root of the project with the following content:

```env
API_KEY=j6k9BY4OO4oXOOhLiUbdjPzfsKKcxoIZ
```

> ⚠️ Make sure `.env` is listed in `.gitignore` to protect your secret keys.

> ℹ️ **Note:** 
This `API_KEY` is used to access an **external currency conversion API**.  
In a real-world project, this key should be kept private and **never exposed publicly**, for security reasons.

However, **for educational purposes**, the key has been included here so that anyone can test and explore **all features** of the system without restrictions.

You can also generate your own key on the API provider's website and replace it in the `.env` file.

4. **Run the application**

- Locate the main class with `@SpringBootApplication` (BancodigitaljpaApplication)
- Right-click and select `Run As > Java Application`

### 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 🇧🇷 Projeto Banco Digital

Este repositório contém duas versões de um **Banco Digital** desenvolvido em Java:

### 📌 Primeira Versão: Java Básico
👉 Acesse aqui: [digital-bank-java](https://github.com/bbarrosomoreira/digital-bank-java)
Versão simples e funcional, construída apenas com Java (sem frameworks), incluindo:

- Gerenciamento de contas
- Funcionalidades de depósito, saque e transferência
- Estrutura de cartões e seguros
- Abordagem orientada a objetos

### 🚀 Segunda Versão: Java + Spring Boot + JWT

Uma versão aprimorada com ferramentas modernas do backend Java e boas práticas:

- Desenvolvido com **Spring Boot**
- API com endpoints RESTful
- Segurança com **autenticação JWT**
- Banco de dados em memória (**H2**)
- JPA para persistência das entidades
- Controle de acesso por perfil (ADMIN / USER)
- Tratamento de exceções e respostas personalizadas

### 🛠️ Tecnologias

- Java 17+
- Spring Boot
- Spring Security
- H2 Database
- JPA (Hibernate)
- JWT
- Maven

### ▶️ Como rodar localmente

#### ✅ Requisitos

- [Java 17 ou superior](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [IDE Eclipse](https://www.eclipse.org/downloads/)
- [Git](https://git-scm.com/) (opcional)

#### 👣 Passo a passo

1. **Clone o repositório**

```bash
git clone https://github.com/bbarrosomoreira/digital-bank-api.git
```

2. **Importe o projeto no Eclipse**

- Vá em `File > Import`
- Escolha `Existing Maven Projects`
- Navegue até a pasta clonada
- Finalize a importação

3. **Configure as variáveis de ambiente**

Crie um arquivo `.env` na raiz do projeto com o seguinte conteúdo:

```env
API_KEY=j6k9BY4OO4oXOOhLiUbdjPzfsKKcxoIZ
```

> ⚠️ Certifique-se de que o `.env` está listado no `.gitignore` para proteger suas chaves secretas.

> ℹ️ **Observação:** 
Esta `API_KEY` é utilizada para acessar uma **API externa de conversão de moedas**.  
Em um projeto real, essa chave deveria ser mantida em sigilo e **nunca ser exposta publicamente**, por questões de segurança.

No entanto, **para fins didáticos**, a chave foi incluída aqui para que qualquer pessoa consiga testar e explorar **todas as funcionalidades** do sistema sem restrições.

Você também pode gerar sua própria chave diretamente no site da API e substituí-la no arquivo `.env`.

4. **Execute o projeto**

- Localize a classe principal com `@SpringBootApplication` (BancodigitaljpaApplication)
- Clique com o botão direito e selecione `Run As > Java Application`

### 📄 Licença

Este projeto está licenciado sob a licença [MIT](LICENSE).

---
