package com.dreaminsteam.rpbot.web.util;

public class Path {

	public static class Web {
		public static final String INDEX = "/index";
		public static final String LOGIN = "/login";
		public static final String LOGOUT = "/logout";
		
		public static String getIndex() {
			return INDEX;
		}
		public static String getLogin() {
			return LOGIN;
		}
		public static String getLogout() {
			return LOGOUT;
		}	
	}
	
	public static class Template {
		public static final String INDEX = "/velocity/index.vm";
	}
	
	
}
