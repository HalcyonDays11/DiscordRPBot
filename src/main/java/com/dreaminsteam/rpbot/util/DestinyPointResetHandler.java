package com.dreaminsteam.rpbot.util;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class DestinyPointResetHandler {

	public static void resetAllDestinyPoints(){
		Dao<Player, String> dao = DatabaseUtil.getPlayerDao();
		
		// there might be a better way to do this that doesn't involve raw db commands.
		// the problem is that I don't think SQlite supports dropping a single column like this at all, 
		// and ormlite is probably trying to cater to the lowest common denominator.
		// (though maybe it's hidden away somewhere in some h2 specific subclass and I just couldn't find it??)
		
		// I suppose I could just iterate through each Player, but that sounds horribly inefficient.
		try {
			dao.executeRawNoArgs("ALTER TABLE players DROP COLUMN usedDestiny");
			dao.executeRawNoArgs("ALTER TABLE players ADD usedDestiny INT");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
