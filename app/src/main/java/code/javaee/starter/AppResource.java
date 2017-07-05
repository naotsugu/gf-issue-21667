package code.javaee.starter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/")
public class AppResource {
    @EJB
    private Service service;

    @GET
    public Response hello() throws Exception {
        return Response.ok("hello").build();
    }

    @POST
    public Response login(@Context HttpServletRequest request) throws Exception {
        request.getSession(true);
        request.login("user1", "user1");
        service.exec();
        return Response.ok("ok").build();
    }

}
