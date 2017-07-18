package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.utilities.PlayerResetHandler;
import com.dreaminsteam.rpbot.utilities.Year;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class DestinyRestoreCommand implements CommandExecutor{
		
		@Command(aliases = {"!restoreDestiny"}, description="Restore Destiny Points for an individual, or the entire server. **Admin Only!**", usage = "!restoreDestiny <playerId> <number of points to restore>", async = true)
		public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception {
			
			if(!CommandUtils.hasAdminRole(user, channel)){
				return user.mention() + "  Sorry, you are not yet a master of your own destiny.";
			}
			
			if (args == null || args.length == 0){
				PlayerResetHandler.resetAllDestinyPoints();
				
				return user.mention() + "  Success! All destiny points have been reset for all students!";
			}
			
			String playerId = args[0];
			Player player = DatabaseUtil.getPlayerDao().queryForId(playerId);
			
			if(player == null){
				return " I can't find a player with that ID... try using **!playerInfo** to get the list of players.";
			}
			
			if (args.length > 1){
				try {
					int pointsToRestore = Integer.parseInt(args[1]);
					player.restoreDestinyPoints(pointsToRestore);
				} catch (Exception e){}
			} else {
				player.restoreAllDestinyPoints();
			}
			
			DatabaseUtil.getPlayerDao().update(player);
			
			return user.mention() + "  Success! " + player.getName() + " now has **" + player.getAvailableDestinyPoints() + "** destiny points for today.";
			
		}
}
