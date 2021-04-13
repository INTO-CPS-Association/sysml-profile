package org.modelio.module.intocps.model.configuration;

public class CoeAlgorithm {

	private String type = "";

	private Float size = Float.valueOf("0.0001");

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getSize() {
		return this.size;
	}

	public void setSize(Float step) {
		this.size = step;
	}


}
