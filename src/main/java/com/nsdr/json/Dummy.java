package com.nsdr.json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Dummy {

	JsonObject schema;
	JsonObject root;

	public Dummy(Path pathToSchema) {
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(pathToSchema.toFile());
			JsonReader rdr = Json.createReader(fileStream);
			schema = rdr.readObject();
			root = schema.getJsonObject("root");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Dummy.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fileStream.close();
			} catch (IOException ex) {
				Logger.getLogger(Dummy.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	
}
