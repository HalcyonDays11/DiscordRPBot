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

	@Command(aliases = {"!playerInfo"}, description = "List current player information. **Admin only!**", usage="!playerInfo", async=true)
	public String getPlayerInfo(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		boolean hasAdminRole = CommandUtils.hasAdminRole(user, channel);
		if(!hasAdminRole){
			return user.mention() + "  You are likely to be eaten by a grue. If this predicament seems particularly cruel, consider whose fault it might be: not a torch nor a match in your inventory.";
		}
		IPrivateChannel pmChannel = user.getOrCreatePMChannel();

		StringBuilder sb = new StringBuilder();
		sb.append("Current players: \n");
		int count = 0;
		for (Player player : DatabaseUtil.getPlayerDao().queryForAll()) {
			if(player != null){
				String snowflakeId = player.getSnowflakeId();
				String playerName = player.getName();
				String currentYear = player.getCurrentYear() != null ? player.getCurrentYear().getPrettyName() : " - MISSING YEAR!!!! ---";
				sb.append("**" + snowflakeId + "**: " + playerName + ", " + currentYear + "\n");
				count++;
				if(count == 30){
					count = 0;
					pmChannel.sendMessage(sb.toString());
					sb = new StringBuilder();
				}
			}
			
		}
		if(count > 0){
			pmChannel.sendMessage(sb.toString());
		}
		return user.mention() + "  Information DM'd to you.";
	}
}