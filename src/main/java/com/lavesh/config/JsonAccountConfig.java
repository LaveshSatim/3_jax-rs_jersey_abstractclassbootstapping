package com.lavesh.config;

import java.io.InputStream;

import com.lavesh.dto.Account;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.Path;

@Path("/json/config")
public class JsonAccountConfig extends AccountConfig {

	@Override
	protected Account extract(InputStream inputStream) {
		Account account = null;
		JsonParser jsonParser = Json.createParser(inputStream);

		if (jsonParser.hasNext()) {

			jsonParser.next();

			JsonObject object = jsonParser.getObject();
			account = new Account();
			account.setAge(object.getInt("age", 10));
			account.setFirstname(object.getString("firstname", "none"));
			account.setLastname(object.getString("lastname", "none"));
		}

		return account;
	}

	@Override
	protected String getkey(InputStream inputStream) {
		String key = null;
		JsonParser jsonParser = Json.createParser(inputStream);
		if (jsonParser.hasNext()) {
			jsonParser.next();

			JsonObject object = jsonParser.getObject();
			key = object.getString("key");
		}
		return key;
	}

}
