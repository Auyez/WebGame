package website;

import com.google.gson.Gson;
import game.Constants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Path("/user")
public class UserEndpoint {
    @GET
    @Path("/user_id")
    public Response getUserId(@QueryParam("username") String username,
                              @CookieParam("authToken") String authToken) {
        if (AuthTokens.getInstance().isValid(username, authToken)) {
            try {
                int userId = Database.getInstance().getUserId(username);
                return Response.ok(userId).build();
            } catch (SQLException ex) {
                return Response.serverError().build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/statistics")
    public Response getStatistics(@QueryParam("username") String username) {
        try {
            List<Database.UserSkillUsage> statistics = Database.getInstance().getUserStatistics(username);

            Gson gson = new Gson();
            return Response.ok(gson.toJson(statistics)).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/skill_limit")
    public Response getSkillLimit(@QueryParam("username") String username) {
        return Response.ok(Constants.PLAYER_SKILL_LIMIT).build();
    }

    @GET
    @Path("/skill_ids")
    public Response getUserSkills(@QueryParam("username") String username) {
        try {
            List<Integer> ids = Database.getInstance().getUserSkillIds(username);
            Gson gson = new Gson();
            return Response.ok(gson.toJson(ids)).build();
        } catch (SQLException ex) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/skill_ids_names")
    public Response getSkillIdsNames() {
        try {
            Map<Integer, String> idsNames = Database.getInstance().getSkills();
            Gson gson = new Gson();
            return Response.ok(gson.toJson(idsNames)).build();
        } catch (SQLException ex) {
            return Response.serverError().build();
        }
    }


    private class UsernameSkillIds {
        String username;
        List<Integer> skillIds;
    }
    @POST
    @Path("/set_skills")
    @Produces("application/text")
    @Consumes("application/json")
    public Response setUserSkills(String usernameSkillIdsJson,
                                  @CookieParam("authToken") String authToken) {
        Gson gson = new Gson();
        UsernameSkillIds usernameSkillIds = gson.fromJson(usernameSkillIdsJson, UsernameSkillIds.class);
        String username = usernameSkillIds.username;
        List<Integer> skillIds = usernameSkillIds.skillIds;

        if (AuthTokens.getInstance().isValid(username, authToken)) {
            if (skillIds.size() <= Constants.PLAYER_SKILL_LIMIT) {
                try {
                    Database.getInstance().updateSkills(username, skillIds);
                } catch (SQLException ex) {
                    return Response.serverError().build();
                }

                return Response.ok("").build();
            }
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/logout")
    @Produces("application/text")
    @Consumes("application/x-www-form-urlencoded")
    public Response logout(@FormParam("username") String username,
                           @CookieParam("authToken") String authToken) {
        System.out.println("logout " + username + " " + authToken);

        if (AuthTokens.getInstance().isValid(username, authToken)) {
            AuthTokens.getInstance().removeToken(username);
            return Response.ok("").build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
