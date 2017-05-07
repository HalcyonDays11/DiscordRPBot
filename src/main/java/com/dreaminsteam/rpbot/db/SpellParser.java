package com.dreaminsteam.rpbot.db;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.dreaminsteam.rpbot.db.models.Spell;
import com.j256.ormlite.table.TableUtils;

public class SpellParser {

	public static void parseSpells() throws Exception {
		if (DatabaseUtil.getSpellDao().isTableExists()){
			TableUtils.dropTable(DatabaseUtil.getSpellDao(), true);
		} 
		
		TableUtils.createTable(DatabaseUtil.getSpellDao());
		
		try (CSVParser parser = new CSVParser(new FileReader("./src/main/resources/Spell List.csv"), CSVFormat.EXCEL)){
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
				DatabaseUtil.getSpellDao().create(spell);
			}
		}
		
		
		
		
		
		
	}
}
