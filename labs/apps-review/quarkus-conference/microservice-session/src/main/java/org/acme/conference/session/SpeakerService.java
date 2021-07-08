package org.acme.conference.session;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/speaker")
@RegisterRestClient
public interface SpeakerService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<SpeakerFromService> listAll();

}
