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
	@RolesAllowed({"user1","defcon2"})
	@Produces("json")
	@Path("getProduct")
	public RestResponse getProducto() {
	
		/* example json param product:
		
			{
				name="Apple",
				price=25
			}
			
			example DTO:
			
			public class ProductDTO {
			
				private String name;
				private long price;
				
				public void setName(String name){
					this.name = name;
				}
				public void setPrice(long price){
					this.price = price;
				}
				public String getName(){
					return this.name;
				}
				public long getPrice(){
					return this.price;
				}
			
			}
		
		*/

		return new RestResponse("response:OK", 200, "");
	
	}
	
}
