# NebuloHub
![Java](https://img.shields.io/badge/Java-17-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg)
![Heroku](https://img.shields.io/badge/Deploy-Heroku-purple.svg)

Um hub social para brainstorming, avaliação e análise de ideias de startups, desenvolvido como a Global Solution 2 de 2025 para a FIAP.

---

## Sobre o Projeto

NebuloHub é uma plataforma web que funciona como um "feed social" focado em empreendedorismo. A premissa é simples: permitir que usuários postem suas ideias de startups e recebam feedback valioso da comunidade e de uma inteligência artificial.

Os usuários podem se cadastrar, postar suas ideias, comentar, e avaliar as ideias de outros usuários com notas de 0 a 10. O projeto também integra o Google Gemini para fornecer uma análise automatizada (Prós e Contras) de cada ideia, atuando como um "Analista de IA".

## ☁️ Deploy no Heroku

### O projeto está deployado no Heroku:

https://nebulohub-167cb8a40423.herokuapp.com/


### Link do Repositório Github: 
https://github.com/NebuloHub/Java

### Link do Vídeo Explicativo:
Pendente

### Link do Pitch:
Pendente

---

## Funcionalidades Principais

* **Autenticação de Usuários:** Sistema completo de cadastro e login usando Spring Security (sessão web) e JWT (para a API REST).
* **CRUD de Posts:** Usuários autenticados podem criar, ler, editar e deletar posts de ideias.
* **Sistema de Avaliação (Rating):** Usuários podem dar notas de 0 a 10 para posts, impactando a "média de avaliação". (Um usuário não pode avaliar o próprio post).
* **Sistema de Comentários:** Usuários podem discutir as ideias através de comentários.
* **Perfis de Usuário:** Uma página de perfil pública que exibe os posts e a atividade de comentários de um usuário específico.
* **Análise por IA Generativa:** Um botão em cada post que chama a API do Google Gemini para gerar uma análise de "Prós e Contras" da ideia.
* **Internacionalização (i18n):** Suporte completo para Português (pt-BR) e Inglês (en).

---

## Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot 3.
* **Persistência de Dados:** Spring Data JPA, Hibernate, OracleDB (via FIAP).
* **Migrações de Banco:** Flyway.
* **Segurança:** Spring Security (Autenticação, Autorização por Roles, `BCrypt`).
* **Frontend (Server-Side):** Thymeleaf, Bootstrap 5.
* **Mensageria Assíncrona:** Spring AMQP, RabbitMQ (CloudAMQP no deploy).
* **IA Generativa:** Google Gemini (SDK Java `com.google.genai`).
* **Documentação da API:** SpringDoc (OpenAPI v3 / Swagger UI).
* **Build & Deploy:** Maven & Heroku.

---

## 🛠️ Configuração Local

Para rodar este projeto localmente, siga os passos:

1.  **Pré-requisitos:**
    * Git
    * Java 17
    * Maven 3.9+
    * Uma instância de Oracle DB acessível. (Opcional)
    * Uma instância de RabbitMQ acessível (ex: rodando em Docker).


2. **Configurar Variáveis de Ambiente:**
    O `application.properties` usa valores de *fallback* para o banco Oracle da FIAP e RabbitMQ local, mas é recomendado configurar as seguintes variáveis de ambiente no seu sistema:

    * `CLOUDAMQP_URL`: A URL do seu broker RabbitMQ (o padrão local é: `amqp://guest:guest@localhost:5672`).
    * `GEMINI_API_KEY`: Sua chave de API do Google AI Studio.


3.  **Rodar a Aplicação**

IMPORTANTE:
Se ao executar o projeto, o IntelliJ cuspir o erro "java: cannot find symbol", vá em File -> Settings (atalho CTRL + ALT + S) -> Build, Execution, Deployment -> Compiler -> Annotation Processors -> Selecione "nebulohub" - > Marque a opção "Obtain processors from project classpath" ao invés de "Processor path:"

4. **Acessar:**
    * **Aplicação Web:** `http://localhost:8080`
    * **Documentação da API:** `http://localhost:8080/swagger-ui.html`

---

## Integrantes:
2TDSPM:
- Vicenzo Massao - 554833
- Erick Alves - 556862

2TDSPX:
- Luiz Heimberg - 556864





