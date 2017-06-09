package de.prometheus.bildarchiv.model;

import com.google.gson.Gson;

public interface ToJson {
	
	public default String toJson() {
		return new Gson().toJson(this);
	}

}
