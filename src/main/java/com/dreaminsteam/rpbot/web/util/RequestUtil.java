package com.dreaminsteam.rpbot.web.util;

import spark.Request;
import spark.Response;

public class RequestUtil {

	public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
	}
	
	public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    }
 
}
