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
        try {
            if (!isValid(username, password)) {
                System.out.println(username + " " + password + " is not valid");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (SQLException ex) {
            return Response.serverError().build();
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

        if (Database.getInstance().usernameTaken(username)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        try {
            String salt = generateSalt();
            Database.getInstance().insertUser(username, hash(password + salt), salt);
        } catch (SQLException ex) {
            return Response.serverError().build();
        }

        return Response.ok("").build();
    }

    private boolean isValid(String username, String password) throws SQLException {
        String salt = Database.getInstance().getPasswordSalt(username);
        String passwordHash = Database.getInstance().getPasswordHash(username);
        return hash(password + salt).equals(passwordHash);
    }

    public String hash(String password) {
        return password;
    }

    private String generateSalt() {
        return "";
    }
}
