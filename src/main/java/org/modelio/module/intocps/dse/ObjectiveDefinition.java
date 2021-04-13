
package org.modelio.module.intocps.dse;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "externalScripts",
    "internalFunctions"
})
public class ObjectiveDefinition {

    @JsonProperty("externalScripts")
    private Map<String, ExternalScript> externalScripts = new HashMap<>();

    @JsonProperty("internalFunctions")
    private InternalFunction internalFunctions;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     *
     * @return
     *     The externalScripts
     */
    @JsonProperty("externalScripts")
    public Map<String, ExternalScript> getExternalScripts() {
        return this.externalScripts;
    }

    /**
     *
     * @param externalScripts
     *     The externalScripts
     */
    @JsonProperty("externalScripts")
    public void addExternalScripts(String key, ExternalScript externalScript) {
        this.externalScripts.put(key, externalScript);
    }


    /**
     *
     * @return
     *     The internalFunctions
     */
    @JsonProperty("internalFunctions")
    public InternalFunction getInternalFunctions() {
        return this.internalFunctions;
    }

    /**
     *
     * @param internalFunctions
     *     The internalFunctions
     */
    @JsonProperty("internalFunctions")
    public void setInternalFunctions(InternalFunction internalFunctions) {
        this.internalFunctions = internalFunctions;
    }

    public ObjectiveDefinition withInternalFunctions(InternalFunction internalFunction) {
        this.internalFunctions = internalFunction;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ObjectiveDefinition withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
