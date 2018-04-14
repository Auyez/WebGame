package website;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


@Path("/auth")
public class AuthEndpoint {

    @POST
    @Path("/login")
    @Produces("application/text")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
        System.out.println(username + " " + password);
        if (!AuthTokens.getInstance().isValid(username, password)) {
            System.out.println(username + " " + password + " is not valid");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = AuthTokens.getInstance().newToken(username);

        System.out.println(username + " " + password + " logged");

        return Response.ok(token).build();
    }

    @POST
    @Path("/register")
    @Produces("application/text")
    @Consumes("application/x-www-form-urlencoded")
    public Response registerUser(@FormParam("username") String username,
                                 @FormParam("password") String password) {
        System.out.println("register " + username + " " + password);

        if (Database.usernameTaken(username)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        try {
            Database.insertUser(username, AuthTokens.getInstance().hash(password));
        } catch (SQLException ex) {
            return Response.serverError().build();
        }

        return Response.ok("").build();
    }
}
