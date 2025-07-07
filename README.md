# 💳 Banco Digital / Digital Bank

🇧🇷 Versão em português abaixo | 🇺🇸 English version below

## Project Updates 🚀

The project is currently undergoing improvements to its architecture and technology stack.

## 🚧 Upcoming Changes

- [x] **Removal of JPA + Hibernate**: To simplify database communication.
- [x] **Migration to PostgreSQL**: For better integration, scalability, and performance.
- [x] **Hexagonal Architecture**: Refactoring the project to adopt a clean, layered structure.
- [ ] **MapStruct**: Implementation for simplified and efficient object mapping.
- [ ] **Testing**: Adding unit and integration tests to ensure application stability.

> 🔄 All updates are being developed in the `dev` branch.

---

## Atualizações em Andamento 🚀

O projeto está se modernizando.

## 🚧 Próximas Mudanças

- [x] **Remoção de JPA + Hibernate**: Para simplificar a comunicação com o banco de dados.
- [x] **Migração para PostgreSQL**: Para melhor integração, escalabilidade e desempenho.
- [x] **Arquitetura Hexagonal**: Refatoração do projeto para adotar uma estrutura limpa e em camadas.
- [ ] **MapStruct**: Implementação para mapeamento de objetos de forma eficiente e simplificada.
- [ ] **Testes**: Adição de testes unitários e de integração para garantir a estabilidade da aplicação.

> 🔄 Todas as atualizações estão sendo desenvolvidas na branch `dev`.

---

## About Original Project | Sobre o Projeto Original

## 🇺🇸 Digital Bank Project

- Built with **Spring Boot**
- API with RESTful endpoints
- Security with **JWT Authentication**
- In-memory **H2 Database**
- JPA for ORM and entity persistence
- Role-based access (ADMIN / USER)
- Exception handling and custom error responses
- Integration with external APIs (postal code search and currency conversion)

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
cd digital-bank-springboot
```

2. **Import project in Eclipse**

- Go to `File > Import`
- Select `Existing Maven Projects`
- Choose the cloned folder
- Finish the import

3. **Configure environment variables**

> ℹ️ **Note:** 
An `API_KEY` is used to access an **external currency conversion API**.  
In a real-world project, this key should be kept private and **never exposed publicly**, for security reasons.
However, **for educational purposes**, the key has been included here so that anyone can test and explore **all features** of the system without restrictions.
You can also generate your own key on the API provider's website /and replace it in the `application.properties` file.

### 🔁 Receita Federal API Simulation

This project also consumes a **simulated Receita Federal API**, used to validate CPF (Brazilian ID).

You can choose between two ways to run the system:

- `dev` (default): skips CPF validation and always considers CPF as **valid and active** (mock behavior to simplify local testing)
  
```properties
spring.profiles.active=dev
```

- `prod`: connects to a **mock Receita Federal API** for CPF status simulation.  
  To use it, you must run the companion repository: [validadorCPF](https://github.com/bbarrosomoreira/validadorCPF)

```properties
spring.profiles.active=prod
```

This simulated API works with the following logic:

| CPF Starts With | Response                                  |
|-----------------|-------------------------------------------|
| `0` to `5`      | `status: ATIVO` (Active)                  |
| `6` or `7`      | `status: INATIVO` (Inactive)              |
| `8`             | Simulates an API error (throws exception) |
| Other values    | Default handling                          |

> ⚠️ This API is **not affiliated with the real Receita Federal**. It was created solely for **testing and demonstration purposes**.

4. **Run the application**

- Locate the main class with `@SpringBootApplication` (BancodigitaljpaApplication)
- Right-click and select `Run As > Java Application`

---

### 🧩 Possible issue after cloning the project

After cloning, Spring Boot might not run immediately. To fix that:

1. Right-click the project in your IDE → `Configure` → `Convert to Maven Project`  
2. Right-click again → `Maven` → `Update Project` → check "Force update"  
3. Run the application from the main class (`@SpringBootApplication`)  
4. Ensure the `.env` file exists at the project root  

If issues persist, open an issue in this repository.

### 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 🇧🇷 Projeto Banco Digital

- Desenvolvido com **Spring Boot**
- API com endpoints RESTful
- Segurança com **autenticação JWT**
- Banco de dados em memória (**H2**)
- JPA para persistência das entidades
- Controle de acesso por perfil (ADMIN / USER)
- Tratamento de exceções e respostas personalizadas
- Integração com APIs externas (busca de CEP, conversão de moedas e mock da Receita Federal)

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
- [IDE Eclipse](https://www.eclipse.org/downloads/) ou outra IDE
- [Git](https://git-scm.com/) (opcional)

#### 👣 Passo a passo

1. **Clone o repositório**

```bash
git clone https://github.com/bbarrosomoreira/digital-bank-api.git
cd digital-bank-springboot
```

2. **Importe o projeto no Eclipse**

- Vá em `File > Import`
- Escolha `Existing Maven Projects`
- Navegue até a pasta clonada
- Finalize a importação

3. **Configure as variáveis de ambiente**

> ℹ️ **Observação:** 
Uma `API_KEY` é utilizada para acessar uma **API externa de conversão de moedas**.  
Em um projeto real, essa chave deveria ser mantida em sigilo e **nunca ser exposta publicamente**, por questões de segurança.
No entanto, **para fins didáticos**, a chave foi incluída aqui para que qualquer pessoa consiga testar e explorar **todas as funcionalidades** do sistema sem restrições.
Você também pode gerar a sua própria chave diretamente no site da API e substituí-la no arquivo `application.properties`.

### 🔁 Simulação da API da Receita Federal

Este projeto também consome uma **API simulada da Receita Federal**, usada para validar CPF.

Você pode escolher entre duas formas de rodar o sistema:

- `dev` (padrão): ignora a validação e considera todo CPF como **válido e ativo** (comportamento mockado para facilitar testes locais)
  
```properties
spring.profiles.active=dev
```

- `prod`: utiliza uma **API simulada da Receita Federal** para validação de CPF.  
  Para usar, é necessário rodar o repositório auxiliar: [validadorCPF](https://github.com/bbarrosomoreira/validadorCPF)

```properties
spring.profiles.active=prod
```

A resposta da API varia conforme o dígito inicial do CPF informado:

| CPF começa com | Resposta                           |
|----------------|------------------------------------|
| `0` a `5`      | `status: ATIVO`                    |
| `6` ou `7`     | `status: INATIVO`                  |
| `8`            | Simula erro da API (lança exceção) |
| Outros valores | Tratamento padrão                  |

> ⚠️ Esta é uma **simulação** e **não realiza validações reais**. Não tem relação com a Receita Federal oficial.

4. **Execute o projeto**

- Localize a classe principal com `@SpringBootApplication` (BancodigitaljpaApplication)
- Clique com o botão direito e selecione `Run As > Java Application`

---

### 🧩 Possíveis problemas ao clonar e rodar o projeto

Após clonar o repositório, o Spring Boot pode não rodar imediatamente. Para resolver:

1. Clique com o botão direito no projeto → `Configure` → `Convert to Maven Project`  
2. Depois → `Maven` → `Update Project` e marque "Force update"  
3. Rode a aplicação pela classe principal (`@SpringBootApplication`)  
4. Verifique se o arquivo `.env` está presente na raiz do projeto  

Se o problema persistir, abra uma issue neste repositório.

### 📄 Licença

Este projeto está licenciado sob a licença [MIT](LICENSE).
