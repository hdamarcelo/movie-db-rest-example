package com.hdamarcelo.application.controller;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdamarcelo.application.model.common.RestResponse;
import com.hdamarcelo.application.model.movies.AddMovieInboundDTO;
import com.hdamarcelo.application.model.movies.MovieOutboundDTO;
import com.hdamarcelo.application.model.movies.UpdateMovieInboundDTO;
import com.hdamarcelo.application.service.MovieService;
import com.hdamarcelo.application.service.MovieServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Defines REST services to manipulate movie data
 */
@Path(MovieController.PATH)
@Api(tags = MovieController.PATH)
public class MovieController extends AbstractRestController {

    static final String PATH = "movies";

    Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Inject
    private MovieService movieService;

    @GET
    @ApiOperation(response = MovieOutboundDTO.class, responseContainer = "List", value = "Lists all registered movies")
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, response = RestResponse.class, message = "Indicates that query was executed successfully"),
            @ApiResponse(code = STATUS_NOT_FOUND, response = RestResponse.class, message = "Indicates that there are no registered movies") })

    public Response getAllmovies() {
        List<MovieOutboundDTO> movies = movieService.findAllMovies();
        if (movies.isEmpty()) {
            return Response.status(STATUS_NOT_FOUND)
                    .entity(new RestResponse("Resource not found", STATUS_NOT_FOUND)).build();
        }
        return Response.ok(movieService.findAllMovies()).build();
    }

    @GET
    @Path("{id}")
    @ApiOperation(response = MovieOutboundDTO.class, value = "Lists a single movie by id")
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, response = RestResponse.class, message = "Indicates that query was executed successfully"),
            @ApiResponse(code = STATUS_NOT_FOUND, response = RestResponse.class, message = "Indicates that a movie with the specified id was not found") })
    public Response getMovieById(@PathParam("id") @ApiParam(value = "Id of the movie do search", required = true) String id) {
        MovieOutboundDTO movie = movieService.findMovieById(id);

        if (movie == null) {
            return Response.status(STATUS_NOT_FOUND)
                    .entity(new RestResponse("Resource not found", STATUS_NOT_FOUND)).build();
        }

        return Response.ok(movie).build();
    }

    @POST
    @ApiOperation(value = "Registers a new movie")
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_CREATED, response = RestResponse.class, message = "Indicates that the specified movie was created successfully"),
            @ApiResponse(code = STATUS_CONFLICT, response = RestResponse.class, message = "Indicates that a movie with the same specified id already exists"),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, response = RestResponse.class, message = "Indicates that an unexpected error occurred") })
    public Response addMovie(@ApiParam(value = "New Movie to insert", required = true) AddMovieInboundDTO addMovieDTO) {
        String newMovieId;
        Response response;
        try {
            newMovieId = movieService.addMovie(addMovieDTO);
            response = Response.created(new URI("./" + MovieController.PATH + "/" + newMovieId))
                    .entity(new RestResponse("Resource created", STATUS_CREATED)).build();
        } catch (MovieServiceException e) {
            logger.error("", e);
            return Response.status(STATUS_CONFLICT)
                    .entity(new RestResponse("Resource already exists", STATUS_CONFLICT)).build();

        } catch (Exception e) {
            logger.error("", e);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR)
                    .entity(new RestResponse("An unexpected error occurred", STATUS_INTERNAL_SERVER_ERROR)).build();
        }
        return response;
    }

    @PUT
    @Path("{id}")
    @ApiOperation(value = "Registers a specific movie")
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_CREATED, response = RestResponse.class, message = "Indicates that the specified movie was created/updated"),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, response = RestResponse.class, message = "Indicates that an unexpected error occurred") })
    public Response updateMovie(
            @PathParam("id") @ApiParam(value = "Id of the movie to register", required = true) String id,
            @ApiParam(value = "Movie content to register", required = true) UpdateMovieInboundDTO updateMovieDTO) {
        String newMovieId;
        Response response;
        try {
            newMovieId = movieService.updateMovie(id, updateMovieDTO);
            response = Response.created(new URI("./" + MovieController.PATH + "/" + newMovieId))
                    .entity(new RestResponse("Resource created/updated", STATUS_CREATED)).build();
        } catch (Exception e) {
            logger.error("", e);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR)
                    .entity(new RestResponse("An unexpected error occurred", STATUS_INTERNAL_SERVER_ERROR)).build();
        }

        return response;
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(value = "Deletes a specific movie")
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_NO_CONTENT, message = "Indicates that the specified movie was deleted successfully"),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, response = RestResponse.class, message = "Indicates that an unexpected error occurred") })
    public Response deleteMovie(
            @PathParam("id") @ApiParam(value = "Movie to delete", required = true) String id) {

        try {
            movieService.deleteMovie(id);
        } catch (Exception e) {
            logger.error("", e);
            return Response.status(STATUS_INTERNAL_SERVER_ERROR)
                    .entity(new RestResponse("An unexpected error occurred", STATUS_INTERNAL_SERVER_ERROR)).build();
        }
        return Response.noContent().build();
    }
}
