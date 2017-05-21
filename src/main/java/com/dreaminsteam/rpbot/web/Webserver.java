package com.dreaminsteam.rpbot.web;

import com.dreaminsteam.rpbot.web.controllers.IndexController;
import com.dreaminsteam.rpbot.web.util.Filters;
import com.dreaminsteam.rpbot.web.util.Path;

import spark.Spark;

public class Webserver {

	private int port;
	public Webserver(int port){
		this.port = port;
	}
	
	public void initializeWebServer(){
		Spark.port(port);
		
		Spark.staticFiles.location("/public");
		Spark.staticFiles.expireTime(600L);
				
		Spark.get(Path.Web.INDEX, IndexController.serveIndexPage);
	}
	
	public void teardownWebServer(){
		Spark.stop();
	}
}
