package com.dreaminsteam.rpbot.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.pmw.tinylog.Logger;

import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.utilities.GoogleDriveIntegration;
import com.j256.ormlite.table.TableUtils;

public class SpellParser {

	public static void parseSpells() throws Exception {
		
		ByteArrayOutputStream outputStream;
		try {
			outputStream = GoogleDriveIntegration.getSpellListAsOutputStream();
			Logger.info("successfully connected to Google Drive for the spell list");
		} catch (Exception e){
			Logger.error(e, "There was an error connecting with Google Drive.  The bot will start, using the most recent cached list of spells (which may be no spells)"
					+ "Check your internet connection, and make sure you have provided the client_secrets.json file."
					+ "You may also try removing your cached credentials, which are stored in ~/.credentials/discord-rp-bot/StoredCredential on OSX/linux,"
					+ "C:\\Users\\<username>.credentials\\discord-rp-bot\\StoredCredential on Windows.");
			return;
		}
		
		if (DatabaseUtil.getSpellDao().isTableExists()){
			TableUtils.dropTable(DatabaseUtil.getSpellDao(), true);
		} 
		
		TableUtils.createTable(DatabaseUtil.getSpellDao());
		
		
		Set<String> incantations = new HashSet<>();
		
		try (CSVParser parser = new CSVParser(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8"), CSVFormat.EXCEL)){
			Iterator<CSVRecord> lineIterator = parser.iterator();
			
			CSVRecord firstLine = lineIterator.next();
			
			Map<Integer, Field> fieldMap = new HashMap<>();
			
			for (int i=0; i< firstLine.size(); i++){
				String fieldname = firstLine.get(i).toLowerCase();
				try {
					Field field = Spell.class.getDeclaredField(fieldname);
					fieldMap.put(i, field);
				} catch (Exception e){}
			}
			
			while (lineIterator.hasNext()){
				CSVRecord line = lineIterator.next();
				Spell spell = new Spell();
				for (Entry<Integer, Field> entry : fieldMap.entrySet()) {
					String text = line.get(entry.getKey());
					Field field = entry.getValue();
					if (field == null || text == null || "".equals(text)){
						continue;
					}
					field.setAccessible(true);
					if (String.class.equals(field.getType())){
						if (Spell.class.getDeclaredField("incantation").equals(field)){
							spell.setPrettyIncantation(text);
							text = text.trim().toLowerCase().replace(" ", "_");
						}
						field.set(spell, text);
					} else if (field.getType().equals(int.class)){
						try {
							int value = Integer.parseInt(text);
							field.set(spell, value);
						} catch (Exception e){}
					}
				}
				if ("".equals(spell.getIncantation()) || "unknown".equals(spell.getIncantation())){
					continue;
				}
				boolean add = incantations.add(spell.getIncantation());
				if (!add){
					System.err.println("duplicate spell - " + spell.getIncantation());
					continue;
				}
				DatabaseUtil.getSpellDao().create(spell);
			}
		}
		
		
		
		
		
		
	}
}
