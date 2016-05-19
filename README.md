# CarmenRestVertx

CarmenRestVertx is written in Java and based on RestVertx 0.0.6 but with JAX-RS standart and security

view example in src/examples/

CarmenRest-Vertx is a mini-framework that makes it easier to build HTTP services with Vert.x and and Security with JWT Protocol, using the JAX-RS 2.0 standard

**** <b>Latest Version released 18/05/2016</b> ****

[Main Features](#Main-Features)<br/>
[Getting Started](#Getting-Started)<br/>
[Annotations](#Annotations)<br/>

<a name="Main-Features"/>
### Main Features

##### Feature #1: Easily create http endpoint handler methods in Java classes to be served by Vertx
Simply call CarmenRestVertx.initScan(vertx, router, jwt, PACKAGE); and annotate your methods.  Instantiate the handling class(es) in your verticle and you're ready to go!

Example constructor of handling class:

```java
@Base("product")
public class ShoppingList extends AvalibleService {

	@GET
	@Produces("json")
	@Path("getProduct")
	@RolesAllowed({"admin","user"})
	public RestResponse getProduct() {
	
		return new RestResponse("response:ok", 200, "");
	
	}
	
}
```

(Read Feature #2 below to see examples of handling methods)

##### Feature #2: Autobind JSON arguments to model parameters
Let's say you have a several variables, and/or nested objects you need to pass in to your endpoint as arguments.  You can create a model which contains your variables and/or nested objects and use it as the parameter in your handling method.  If you send a JSON object in your request (via path param or request body), it will automatically be deserialized into the model (using FasterJackson databind, core, and annotations - https://github.com/FasterXML/jackson-core).

Simply specify the model as the parameter in both the endpoint and handling method and send a valid JSON object in the request

Example handling method using URL encoded Json in the path param for Json object:

```java
@Base("product")
public class ShoppingList extends AvalibleService{

	@POST
	@Produces("json")
	@Path("saveProduct")
	public RestResponse saveProducto(ProductDTO poductDTO) {
	
		manager.saveProduct(poductDTO);
		return new RestResponse(Json.encode(new ProductDTO()), 200, "");
	
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
		
	
	}
	
}
	
```

Example handling method using Json in the request body (automatically detected and deserialized):

```java
	@POST
	@Produces("json")
	@Path("shoppingLists")
	public RestResponse getShoppingListPost(ShoppingListRequest request)
	{		
		return new RestResponse(manager.getShoppingList(request.getId()));
	}
```

Example data model that CarmenRest-Vertx deserializes into w/Jackson annotations (handling method argument):

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
	public class ShoppingListRequest {

		private String id;
		private String name;
		private GroceryStore store;

		public ShoppingListRequest() {
			...
		}

		// Getters/Setters
		...
	}
```

Example on the client side a request to be sent before stringifying:

``` javascript
var request = {
	id: "1",
	name: "Angela",
	store: {
		name: "fake store"
	}
}
```

##### Feature #3: Enable CORS
Great for cross-IDE development.  Are you transferring your web application code from one IDE to another because you prefer one for web development and the other for your Vertx endpoints?  Do you want to serve files with node in one IDE while running your Vertx service endpoints in Eclipse on a different port?

Simply call CORS.allowAll() in your App setup before the request handlers in your verticle, or else use the CORS annotation to open up a specific handling method

Example: <span style="color:red"> enable CORS for a specific method:</span>

```java

	@GET
	@CORS("http://localhost:3000")
	@Path("/")
	public RestResponse getItems()
	{
		...
	}
```
Example: <span style="color:red"> enable CORS at a higher level:</span>

```java

	@Override
	public void start() throws Exception {
	
		/* Certificado para servidor de seguridad, proocolo JWT */
		JWTAuth jwt = JWTAuth.create(vertx, new JsonObject()
		        .put("keyStore", new JsonObject()
		            .put("type", "jceks")
		            .put("path", "D:/VertX/Fuentes/RestMicroServiciosVertx/src/main/java/co/com/quipux/viaticos/webapp/examples/keystore.jceks")
		           .put("password", "secret")));
	

		HttpServer server = vertx.createHttpServer();

		server = vertx.createHttpServer();

		router = Router.router(vertx);
		
		String PACKAGE = "examples.simpleRestService.restServices";
		CarmenRestVertx.initScan(vertx, router, jwt, PACKAGE);

		server.requestHandler(router::accept).listen(8081);

		}
```

<a name=Getting-Started />
# Getting Started

## Prerequisites

- Vert.x version 3+
- Maven
- Your favorite java editor
- A cup of hot tea, coffee, or espresso drink :)

## Add to your project

#### In your Maven Project's POM file, do the following:

Add this to your repositories:

```xml
<repository>
    <id>git-SalimCastellanos</id>
    <name>CarmenRestVertx Git based repo</name>
    <url>https://github.com/SalimCastellanos/CarmenRest-VertX/releases</url>
</repository>
```

Add this in your dependencies:

```xml
<dependency>
	<groupId>co.com.quipux</groupId>
	<artifactId>carmenRest</artifactId>
	<version>1.0</version>
</dependency>
```

(You may need to do a maven force update)

## How to use RestVertx

1.Add your annotations to your handling class(es) - you must return a RestResponse object in your handling method

```java
// Vertx handling method in Java
	@POST
	@ResultType("json")
	@Path("shoppingLists/:request")
	public RestResponse getShoppingListPost(ShoppingListRequest request)
	{		
		if (request.hasSugar()) {
			// This will return a 404 status code with following status message
			return new RestResponse("", 404, "Sugar not found ;)");
		}

		// This will return a normal 200 status code
		return new RestResponse(manager.getShoppingList(request.getId()));
	}
```

2.Instantiate new instance of handling class in your app (don't forget to include the body handler!)

``` java
@Override
public void start() throws Exception {

		  /* for your secure request, JWT protocol
  JWTAuth jwt = JWTAuth.create(vertx, new JsonObject()
		        .put("keyStore", new JsonObject()
		            .put("type", "jceks")
		            .put("path", "D:/VertX/Fuentes/RestMicroServiciosVertx/src/main/java/co/com/quipux/viaticos/webapp/examples/keystore.jceks")
		           .put("password", "secret")));

		HttpServer server = vertx.createHttpServer();
		
		/* Se crea instancia de Router para el manejo de peticiones */
		router = Router.router(vertx);
		
		String PACKAGE = "examples.simpleRestService.restServices";
		
		/* scan the package search service class */
		CarmenRestVertx.initScan(vertx, router, jwt, PACKAGE);
		
}
```

3.Make sure you set compiler to remember parameter values for your project

That's it, you're done.  Just enough time left to make another espresso ;)

<a name=Annotations />
## Annotations

<span style="color:rgb(21, 186, 1)">@Path</span><br/>
Required<br/>
example: @Path("name/:id")

- If you don't set the path, then the method will skipped
- At a minimum, make sure to set the Path as "/"
- In the example path, :id is a path variable
- If you set your project to remember parameters in your build arguments, then path variable will be matched to parameters, no matter the order in the path

- If you don't set your project to remember params in your build arguments, then path variables will be processed as if they are in the same order as the parameters (even if they're not).  <span style="color:rgb(54, 108, 212)">It is highly recommended to set your compiler to remember params for your project to get the most benefit out of RestVertx</span>

<span style="color:rgb(21, 186, 1)">@Method</span><br/>
Optional<br/>
example: @GET

- If you don't set the method, we will attempt to determine if the name of the handling method is or contains an http method name and set the http method registered for the route to that name

<span style="color:rgb(21, 186, 1)">@Base </span> (<b>Class Annotation</b>)<br/>
Optional<br/>
example: @Base("api/monkeys")

- Set base path for all methods in the class

<span style="color:rgb(21, 186, 1)">@ResultType</span><br/>
Optional<br/>
example: @Produces("json")

- Sets the return type, which affects the header info as well
- <b>Don't specify a result type unless it's JSON or a file</b>

<span style="color:rgb(21, 186, 1)">@RestIgnore</span><br/>
Optional<br/>
example: @RestIgnore

- Ignores the method
- Note that any method not specifying a path is also ignored

<span style="color:rgb(21, 186, 1)">@CORS</span><br/>
Optional<br/>
example: @CORS and @CORS("http://localhost:3000")

- Enables CORS on a specific method(s) instead of across the board
- Can optionally specify the ip/port

<span style="color:rgb(21, 186, 1)">@Blocking</span><br/>
Optional<br/>
example: @Blocking(value = "true", serial = "false")

- Determines whether handling method is blocking or not
- First string argument is for blocking, second string argument is for serial
- Defaults to non-blocking


---------------------------------------------------------------------
CarmenRestVertx V 1.0


Please submit any issues you find

CarmenRestVertx is written in Java and based on RestVertx 0.0.6
