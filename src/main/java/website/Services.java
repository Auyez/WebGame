package website;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class Services extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public Services() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
        //singletons.add(new LobbyListService());
        singletons.add(new AuthEndpoint());
        singletons.add(new UserEndpoint());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
