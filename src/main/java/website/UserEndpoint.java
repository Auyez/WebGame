package website;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/user")
public class UserEndpoint {

    @GET
    @Path("/user_id")
    public Response getUserId(@QueryParam("username") String username,
                              @CookieParam("authToken") String authToken) {
        if (AuthTokens.getInstance().isValid(username, authToken)) {
            try {
                int userId = Database.getUserId(username);
                return Response.ok(userId).build();
            } catch (SQLException ex) {
                return Response.serverError().build();
            }
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
