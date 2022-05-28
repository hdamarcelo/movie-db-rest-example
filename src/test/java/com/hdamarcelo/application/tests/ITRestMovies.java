package com.hdamarcelo.application.tests;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.nio.file.Paths;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import com.hdamarcelo.application.model.movies.AddMovieInboundDTO;
import com.hdamarcelo.application.model.movies.UpdateMovieInboundDTO;

public class ITRestMovies {
    private static final String BASE_PATH = "/movie-db-rest-example/api";
    private static final String MOVIES_ENDPOINT = "/movies";
    private String address = "";

    @ClassRule
    public static GenericContainer restServiceContainer = new GenericContainer<>(
            new ImageFromDockerfile().withFileFromPath("Dockerfile",
                    Paths.get("Dockerfile")).withFileFromPath(
                            "target/movie-db-rest-example.war",
                            Paths.get("target/movie-db-rest-example.war")))
                                    .withExposedPorts(8080);

    @Before
    public void beforeClass() {
        address = "http://" + restServiceContainer.getContainerIpAddress() + ":"
                + restServiceContainer.getMappedPort(8080);
    }

    @Test
    public void testShouldReturnAtLeastOneMovie() {

        given().when().get(address + BASE_PATH + MOVIES_ENDPOINT).then().assertThat()
                .statusCode(Status.OK.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
                .body("[0]", is(not(empty())))
                .body("[0]", allOf(hasKey("id"), hasKey("title"), hasKey("director"), hasKey("genre"),
                        hasKey("language"), hasKey("rating")));
    }

    @Test
    public void testShouldReturnASpecificMovie() {

        given().pathParam("id", "1").when().get(address + BASE_PATH + MOVIES_ENDPOINT + "/{id}").then()
                .assertThat().statusCode(Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON).body("id", equalTo("1"));
    }

    @Test
    public void testShouldAddANewMovie() {
        AddMovieInboundDTO movieDTO = new AddMovieInboundDTO("5", "The Fellowship of the Ring", "Peter Jackson",
                "Action/Adventure", "English", 100);
        given().body(movieDTO).contentType(MediaType.APPLICATION_JSON).when()
                .post(address + BASE_PATH + MOVIES_ENDPOINT).then().assertThat()
                .statusCode(Status.CREATED.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
                .body("message", equalTo("Resource created")).body("code", equalTo(201));

    }

    @Test
    public void testShouldUpdateAMovie() {

        UpdateMovieInboundDTO movieDTO = new UpdateMovieInboundDTO("The Two Towers", "Peter Jackson",
                "Action/Adventure", "English", 100);
        given().pathParam("id", "6").body(movieDTO).contentType(MediaType.APPLICATION_JSON)
                .when()
                .put(address + BASE_PATH + MOVIES_ENDPOINT + "/{id}")
                .then().assertThat()
                .statusCode(Status.CREATED.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
                .body("message", equalTo("Resource created/updated")).body("code", equalTo(201));
    }

    @Test
    public void testShouldDeleteAMovie() {

        enableLoggingOfRequestAndResponseIfValidationFails();

        given().pathParam("id", "2").contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(address + BASE_PATH + MOVIES_ENDPOINT + "/{id}")
                .then().assertThat()
                .statusCode(Status.NO_CONTENT.getStatusCode());

    }

}
