package website;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/user")
public class UserEndpoint {

    @POST
    @Path("/logout")
    @Produces("application/text")
    @Consumes("application/x-www-form-urlencoded")
    public Response logout(@FormParam("username") String username,
                           @FormParam("token") String token) {
        System.out.println("logout " + username);

        AuthTokens.getInstance().removeToken(username);

        return Response.ok("").build();
    }
}
