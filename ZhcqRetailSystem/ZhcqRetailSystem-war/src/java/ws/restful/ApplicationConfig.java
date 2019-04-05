package ws.restful;

import java.util.Set;
import javax.ws.rs.core.Application;


//starting point of the restful resource service
@javax.ws.rs.ApplicationPath("Resources")

public class ApplicationConfig extends Application
{
    @Override
    public Set<Class<?>> getClasses() 
    {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    
    
    private void addRestResourceClasses(Set<Class<?>> resources)
    {
//        resources.add(ws.restful.BookResource.class);
    }    
}