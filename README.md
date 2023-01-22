## Conclusion
> code abstraction , learn about inputstream to json obj creation using apis , without web.xml config

## dependencies

```xml
	<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.11</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jersey.inject</groupId>
			<artifactId>jersey-hk2</artifactId>
			<version>3.0.8</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jersey.containers</groupId>
			<artifactId>jersey-container-servlet</artifactId>
			<version>3.0.8</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jersey.core</groupId>
			<artifactId>jersey-server</artifactId>
			<version>3.0.8</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish.jersey.core</groupId>
			<artifactId>jersey-common</artifactId>
			<version>3.0.8</version>
		</dependency>
		<dependency>
			<groupId>org.glassfish</groupId>
			<artifactId>jakarta.json</artifactId>
			<version>2.0.1</version>
		</dependency>
```


## boot class with out web.xml i.e annotation config

```java

package com.lavesh.boot;

import java.util.HashSet;
import java.util.Set;

import com.lavesh.config.JsonAccountConfig;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class Init extends Application {

	private Set<Object> objs;

	public Init() {
		objs = new HashSet<Object>();
		objs.add(new JsonAccountConfig());
	}

	@Override
	public Set<Object> getSingletons() {
		return objs;
	}

}

```


## Abstract class

```java
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


```

## impl class 

```java
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

```


## demo dto

```java
package com.lavesh.dto;

public class Account {

	private String accountNo;
	private String firstname;
	private String lastname;
	private int age;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Account [accountNo=" + accountNo + ", firstname=" + firstname + ", lastname=" + lastname + ", age="
				+ age + "]";
	}

}

```