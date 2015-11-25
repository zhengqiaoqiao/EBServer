package com.qiao.EBServer;

import java.io.IOException;
import java.net.URI;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	
	public static final String BASE_URI = "http://localhost:9999/ebserver/";
	

	public static HttpServer startServer() {
		final ResourceConfig rc = new ResourceConfig().packages("com.qiao.EBServer.resource");
		rc.register(JacksonJsonProvider.class);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(Main.BASE_URI), rc);
	}

	public static void main(String[] args) throws IOException {
		final HttpServer server = Main.startServer();
		LOGGER.info(String.format("EBServer started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...", Main.BASE_URI));
		System.in.read();
		server.shutdownNow();
	}
}