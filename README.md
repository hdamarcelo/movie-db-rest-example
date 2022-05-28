# API RESTfull de Filmes

## Estrutura do projeto

- `MovieController.java`: Define os serviços REST de filmes.
- `MovieService.java`: Define as regras de negócio dos serviços.
- `MovieRepository.java`: Responsável pelo acesso aos dados da aplicação (que estão "mockados"). Implementa a interface `IMovieRepository.java`.
- Modelos:
    - `Movie.java`: Representa as informações de um filme, utilizado pelo repositório.
    - `AddMovieInboundDTO.java`: DTO (Data Transfer Object) para a operação de incluir o cadastro de um filme.
    - `UpdateMovieInboundDTO.java`: DTO para a operação de atualização de um filme.
    - `MovieOutboundDTO.java`: DTO para a operação de consulta de um filme.
- Testes:
    - `TestMovies.java`: Testes Unitários da aplicação, testa os métodos negociais de maneira isolada utilizando o *Mockito*.
    - `ITRestMovies.java`: Testes Integrados dos serviços REST, rodam durante a fase *verify* do MAVEN através do *failsafe* após a geração do pacote implantável. Eles rodam contra a aplição implantada em um container com o *Wildfly* utilizando a biblioteca *testcontainers*.

## Compilação do projeto

O projeto está com o maven embarcado através do Maven Wrapper. Para rodar todos os testes é preciso ter o Docker na máquina.

- Compilação e geração do pacote implantável:
  ```
  ./mvnw clean package
  ```

- Execução dos testes Integrados:
  ```
  ./mvnw verify
  ```

- Execução da aplicação com o Docker
  ```
  docker build . --tag <tag>
  docker run --publish <porta>:8080 <tag>
  ```

- Execução da aplicação em um servidor de aplicação (wildfly/jboss)
implantar o pacote disponível ./target/movie-db-rest-example.war

## SwaggerUI

A API está documentada com o SwaggerUI que pode ser acessado em:
```
<host>:<porta>/movie-db-rest-example/swagger/

Ex.: http://localhost:8080/movie-db-rest-example/swagger/
```

### Próximos passos:
- Expandir a API com mais funcionalidades
- Incluir autenticação na API
- Incluir banco de dados possivelmente com modelo de dados evolutivo
- Criar YAML para deploy automático
- Criar um ExceptionMapper para facilitar o tratamento das exeções 