package com.dreaminsteam.rpbot.commands;

import java.util.List;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class CommandUtils {

	public static final String[] adminRoles = {"admin", "moderator"};
	public static final String[] editorRoles = {"Grimoire"};
	
	public static boolean hasAdminRole(IUser user, IChannel channel){
		return hasRole(user, channel, adminRoles);
	}
	
	public static boolean hasEditorRole(IUser user, IChannel channel) {
		return hasRole(user, channel, editorRoles);
	}
	
	private static boolean hasRole(IUser user, IChannel channel, String[] roleCheck) {
		List<IRole> rolesForGuild = user.getRolesForGuild(channel.getGuild());
		boolean anyMatch = rolesForGuild.stream().anyMatch((role) -> {
			for(String roleToCheck : roleCheck){
				if(role.getName().equalsIgnoreCase(roleToCheck)){
					return true;
				}
			}
			return false;
		});
		return anyMatch;
	}
}
