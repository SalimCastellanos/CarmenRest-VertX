package examples.simpleRestService.restServices;

import rest.vertx.AvalibleService;
import rest.vertx.Annotations.Base;
import rest.vertx.Annotations.GET;
import rest.vertx.Annotations.POST;
import rest.vertx.Annotations.Path;
import rest.vertx.Annotations.Produces;
import rest.vertx.Annotations.RolesAllowed;
import rest.vertx.models.RestResponse;

@Base("product")
public class productService extends AvalibleService{

	@GET
	@RolesAllowed({"defcon1","defcon2"})
	@Produces("json")
	@Path("getProduct")
	public RestResponse getProducto() {
	
		return new RestResponse("response:OK", 200, "");
	
	}
	
}
