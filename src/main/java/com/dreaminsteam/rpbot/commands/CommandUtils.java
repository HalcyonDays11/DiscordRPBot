package com.dreaminsteam.rpbot.commands;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class CommandUtils {

	public static final String[] adminRoles = {"admin", "moderator"};
	
	public static boolean hasAdminRole(IUser user, IChannel channel){
		List<IRole> rolesForGuild = user.getRolesForGuild(channel.getGuild());
		boolean anyMatch = rolesForGuild.stream().anyMatch((role) -> {
			for(String adminRole : adminRoles){
				if(role.getName().equalsIgnoreCase(adminRole)){
					return true;
				}
			}
			return false;
		});
		return anyMatch;
	}
}
