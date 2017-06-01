package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class ListPlayersCommand implements CommandExecutor{

	@Command(aliases = {"!playerInfo"}, description = "Admin only! List current player information", usage="!playerInfo", async=true)
	public void getPlayerInfo(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		boolean hasAdminRole = CommandUtils.hasAdminRole(user, channel);
		if(!hasAdminRole){
			return;
		}
		IPrivateChannel pmChannel = user.getOrCreatePMChannel();

		StringBuilder sb = new StringBuilder();
		sb.append("Current players: \n");
		int count = 0;
		for (Player player : DatabaseUtil.getPlayerDao().queryForAll()) {
			sb.append("**" + player.getSnowflakeId() + "**: " + player.getName() + ", " + player.getCurrentYear().getPrettyName() + "\n");
			count++;
			if(count == 50){
				count = 0;
				pmChannel.sendMessage(sb.toString());
				sb = new StringBuilder();
			}
		}
		if(count > 0){
			pmChannel.sendMessage(sb.toString());
		}
	}
}