package com.lavesh.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lavesh.dto.Account;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;

public abstract class AccountConfig {

	private Map<String, Account> map;

	public AccountConfig() {

		map = new HashMap<String, Account>();
	}

	@Consumes({ MediaType.APPLICATION_JSON })
	@POST
	public String save(InputStream inputStream) {

		Account acc = null;

		acc = extract(inputStream);
		acc.setAccountNo(UUID.randomUUID().toString());

		map.put(acc.getAccountNo(), acc);

		return acc.getAccountNo();
	}

	@Consumes({ MediaType.APPLICATION_JSON })
	@GET
	public String get(InputStream inputStream) {

		String key = null;
		key = getkey(inputStream);

		if (map.containsKey(key) == false) {
			throw new RuntimeException("key not found");
		}

		return map.get(key).getAccountNo() + " " + map.get(key).getFirstname() + " " + map.get(key).getLastname() + " "
				+ map.get(key).getAge();
	}

	protected abstract String getkey(InputStream inputStream);

	protected abstract Account extract(InputStream inputStream);

}
