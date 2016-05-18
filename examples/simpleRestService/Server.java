package examples.simpleRestService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import rest.vertx.RestVertx;


public class Server extends AbstractVerticle {

	static Router router = null;

	public Server() {

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
		
		/* Se crea una instancia del servidor web con el certificaso SSL para desplegar HTTPS */
		// HttpServer server = vertx.createHttpServer(options);
		
		/* Se crea una instancia del servidor web sin certificado SSl */
		HttpServer server = vertx.createHttpServer();
		
		/* Se crea instancia de Router para el manejo de peticiones */
		router = Router.router(vertx);
		
		ScanRestClass.initScan(vertx, router, context, jwt);
		
		/* Se inicializa el servicio de autenticación */
		ServerAuth serverAuth = new ServerAuth(jwt);
		RestVertx.register(vertx, router, serverAuth);
		
		 router.route().handler(StaticHandler.create("D:/VertX/Fuentes/RestMicroServiciosVertx/src/main/java/co/com/quipux/viaticos/webapp/webroot"));

		server.requestHandler(router::accept).listen(3030);

	}

}

