// nome do pacote
package apiTest;

// Bibliotecas
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


// Classe

public class TesteUser {    // inicio da classe
    // Atributos
    static String ct = "application/json"; // content type
    static String uriUser = "https://petstore.swagger.io/v2/user/";

    // Funções e Métodos
    // Funções de Apoio
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // Funções de Testes
    @Test
    public void testarIncluirUser() throws IOException {
        // carregar os dados do nosso json
        String jsonBody = lerArquivoJson("src/test/resources/json/user1.json");

        String userId = "1373259334";

        // realizar o teste
        given()                                         // Dado que
                .contentType(ct)                        // o tipo do conteúdo
                .log().all()                            // mostre tudo
                .body(jsonBody)                         // corpo da requisição
                .when()                                         // Quando
                .post(uriUser) // Endpoint / Onde
                .then()                                         // Então
                .log().all()                            // mostre tudo na volta
                .statusCode(200)                      // comunic. ida e volta ok
                .body("code", is(200))          // tag code é 200
                .body("type", is("unknown"))    // tag type é "unknown"
                .body("message", is(userId))         // message é o userId
        ;
    } // fim do post

    @Test
    public void testarConsultarUser(){
        String username = "charlieB";

        // resultados esperados
        int userId = 1373259334;   // código do usuário
        String email = "charlie@teste.com";
        String senha = "123456";
        String telefone = "11999998888";

        given()
                .contentType(ct)
                .log().all()
                .when()
                .get(uriUser + username)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(userId))
                .body("email", is(email))
                .body("password", is(senha))
                .body("phone", is(telefone))
        ;
    } // fim do Get User
    @Test
    public void testarAlterarUser() throws IOException { // inicio do Put User
        String jsonBody = lerArquivoJson("src/test/resources/json/user2.json");

        String userId = "1373259334";
        String username = "charlieB";

        given()
                .contentType(ct)
                .log().all()
                .body(jsonBody)
                .when()
                .put(uriUser + username)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(userId))
        ;
    } // fim do Put User

    @Test
    public void testarExcluirUser(){ // inicio do Delete User
        String username = "charlieB";

        given()
                .contentType(ct)
                .log().all()
                .when()
                .delete(uriUser + username)
                .then()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(username))
        ;
    } // fim do Delete User
    @Test
    public void testarLogin() { // inicio do login
        String username = "charlieB";
        String password = "123456";

        Response resp = (Response) given()
                .contentType(ct)
                .log().all()
                .when()
                .get(uriUser + "login?username=" + username + "&password=" + password)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", containsString("logged in user session:"))
                .body("message", hasLength(36))
                .extract();

        // Extração do token da resposta
        String token = resp.jsonPath().getString("message").substring(23);
        System.out.println("Conteudo do Token: " + token);
    } // fim do login

    @ParameterizedTest
    @CsvFileSource(resources = "src/test/resources/csv/massaUser.csv", numLinesToSkip = 1, delimiter = ',')
    public void testarIncluiruserCSV(
            String id,
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String userStatus){
        // carregar os dados do nosso json
        StringBuilder jsonBody = new StringBuilder("{");
        jsonBody.append("'id:'" + id + ","),
        jsonBody.append("'username':" + username + ","),
        jsonBody.append("'firstName':" + firstName + ","),
        jsonBody.append("'lastName':" lastName + ","),
        jsonBody.append("'email': " + email + ",");
        jsonBody.append("'password': " + password+ ",");
        jsonBody.append("'phone': " + phone + ",");
        jsonBody.append("'userStatus': " + userStatus);
        jsonBody.append("}"),

        // realizar o teste
        given()                                         // Dado que
                .contentType(ct)                        // o tipo do conteúdo
                .log().all()                            // mostre tudo
                .body(jsonBody)                         // corpo da requisição
                .when()                                         // Quando
                .post(uriUser) // Endpoint / Onde
                .then()                                         // Então
                .log().all()                            // mostre tudo na volta
                .statusCode(200)                      // comunic. ida e volta ok
                .body("code", is(200))          // tag code é 200
                .body("type", is("unknown"))    // tag type é "unknown"
                .body("message", is(uriUserid))         // message é o userId
        ;
    }
} // fim da classe