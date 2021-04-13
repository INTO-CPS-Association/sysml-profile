
package org.modelio.module.intocps.traceability.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "prov:Agent"
})
public class ProvWasAssociatedWith {

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    private ProvAgent provAgent;

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    public ProvAgent getProvAgent() {
        return this.provAgent;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:Agent")
    public void setProvAgent(ProvAgent provAgent) {
    	ProvAgent temp = new ProvAgent();
    	temp.setRdfAbout(provAgent.getRdfAbout());
        this.provAgent = temp;
    }

}
