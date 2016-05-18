package examples.simpleRestService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import rest.vertx.RestVertx;


public class ExampleServer extends AbstractVerticle {

	static Router router = null;

	public static void main(String[] args) {

		final Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new ExampleServer());
		
	}

	@Override
	public void start() throws Exception {
		
		/* Certificado para servidor de seguridad, proocolo JWT */
		JWTAuth jwt = JWTAuth.create(vertx, new JsonObject()
		        .put("keyStore", new JsonObject()
		            .put("type", "jceks")
		            .put("path", "D:/VertX/Fuentes/RestMicroServiciosVertx/src/main/java/co/com/quipux/viaticos/webapp/examples/keystore.jceks")
		           .put("password", "secret")));
		
		/* Certificado para HTTPS */
		HttpServerOptions options = new HttpServerOptions().setSsl(true).setKeyStoreOptions(
			    new JksOptions().
			        setPath("C:/opt/vigia/SSO/wso2is-5.0.0/repository/resources/security/server.jks").
			        setPassword("password")
			);
		
		/* EN CASO DE USAR SSL */
		/* Se crea una instancia del servidor web con el certificaso SSL para desplegar HTTPS */
		// HttpServer server = vertx.createHttpServer(options);
		
		/* Se crea una instancia del servidor web sin certificado SSl */
		HttpServer server = vertx.createHttpServer();
		
		/* Se crea instancia de Router para el manejo de peticiones */
		router = Router.router(vertx);
		
		String PACKAGE = "examples.simpleRestService.restServices";
		RestVertx.initScan(vertx, router, jwt, PACKAGE);
		
		/* Se inicializa el servicio de autenticación */
		/* ServerAuth serverAuth = new ServerAuth(jwt); */
		/*RestVertx.register(vertx, router, serverAuth);*/
		
		/* En caso de querer publicar una carpeta estatica */
		//router.route().handler(StaticHandler.create("D:/VertX/Fuentes/RestMicroServiciosVertx/src/main/java/co/com/quipux/viaticos/webapp/webroot"));

		server.requestHandler(router::accept).listen(8081);

	}

}

