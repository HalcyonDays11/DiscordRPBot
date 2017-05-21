package com.dreaminsteam.rpbot.web.controllers;

import java.util.HashMap;
import java.util.Map;

import com.dreaminsteam.rpbot.web.util.Path;
import com.dreaminsteam.rpbot.web.util.ViewUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public class IndexController {

	public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Path.Template.INDEX);
	};
	
}
