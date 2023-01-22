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
