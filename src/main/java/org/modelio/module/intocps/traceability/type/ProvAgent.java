
package org.modelio.module.intocps.traceability.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rdf:about",
    "name",
    "email"
})
public class ProvAgent {

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("rdf:about")
    private String rdfAbout;

    @JsonProperty("name")
    private String name;

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("email")
    private String email;

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

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("email")
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public ProvAgent(){

    }

    public ProvAgent(String email, String name){
    	this.rdfAbout = "Agent:" + email;
    	this.email = email;
    	this.name = name;

    }

}
