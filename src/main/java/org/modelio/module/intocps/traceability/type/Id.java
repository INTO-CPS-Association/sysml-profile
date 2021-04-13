package org.modelio.module.intocps.traceability.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rdf:about"
})
public class Id {


	public Id (String rdfAbout){
		this.rdfAbout = rdfAbout;
	}


	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("rdf:about")
	private String rdfAbout;


	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("rdf:about")
	public String getRdfAbout() {
		return this.rdfAbout;
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("rdf:about")
	public void setRdfAbout(String rdfAbout) {
		this.rdfAbout = rdfAbout;
	}
}
