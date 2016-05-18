package rest.vertx;

import rest.vertx.Annotations.GET;
import rest.vertx.Annotations.Path;
import rest.vertx.Annotations.Produces;
import rest.vertx.models.RestResponse;


public class AvalibleService {
	
	@GET
	@Produces("json")
	@Path("avalible")
	public RestResponse getLogin() {

		return new RestResponse("response:OK", 200, "");

	}
	
}
