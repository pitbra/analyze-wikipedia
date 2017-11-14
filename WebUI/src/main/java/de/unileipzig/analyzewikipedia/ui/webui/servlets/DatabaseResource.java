/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.servlets;

import com.google.gson.Gson;
import de.unileipzig.analyzewikipedia.ui.webui.contracts.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Pit.Braunsdorf
 */
@Path("/db")
public class DatabaseResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DatabaseResource
     */
    public DatabaseResource() {
    }

    /**
     * Retrieves representation of an instance of de.unileipzig.analyzewikipedia.ui.webui.servlets.DatabaseResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        List<Entity> entities = new ArrayList<>();
        
        Entity entity = new Entity();
        entity.setName("Artikel");
        entity.setType("Article");
        
        entities.add(entity);
        
        Gson gson = new Gson();
                
        return Response.status(200).entity(gson.toJson(entities)).build();
    }

    /**
     * PUT method for updating or creating an instance of DatabaseResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
