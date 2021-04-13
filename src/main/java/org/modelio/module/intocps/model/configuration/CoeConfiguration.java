package org.modelio.module.intocps.model.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoeConfiguration {

	private Map<String, String>  fmus = new HashMap<>();

	private Map<String, List<String>> connections = new HashMap<>();

	private Map<String, Float> parameters = new HashMap<>();


	public  Map<String, String> getFmus() {
		return this.fmus;
	}

	public void setFmus( Map<String, String> fmus) {
		this.fmus = fmus;
	}

	public void addFmu(String key, String path) {
		this.fmus.put(key, path);
	}


	public void addConnection(String key, String value) {
		List<String> temp = this.connections.get(key);
		if (temp == null){
			temp = new ArrayList<>();
		}
		temp.add(value);
		this.connections.put(key, temp);
	}

	public Map<String, List<String>> getConnections(){
		return this.connections;
	}

	public Map<String, Float> getParameters() {
		return this.parameters;
	}

	public void setParameters(Map<String, Float> parameters) {
		this.parameters = parameters;
	}


}
