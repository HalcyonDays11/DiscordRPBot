package com.dreaminsteam.rpbot.utilities;

import java.io.File;

public class PathUtil {

	public static final String configFolder = "DiscordRPBot";
	
	public static File getConfigFile(String relativePath){
		File configFolder = getConfigFolder();
		if(!configFolder.exists()){
			configFolder.mkdirs();
		}
		return new File(configFolder, relativePath);
	}
	
	public static File getConfigFolder(){
		String userHome = System.getProperty("user.home");
		return new File(userHome, configFolder);
	}
}
