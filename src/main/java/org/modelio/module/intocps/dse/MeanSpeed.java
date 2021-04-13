
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
	"scriptFile",
	"scriptParameters"
})
public class MeanSpeed {

	@JsonProperty("scriptFile")
	private String scriptFile;
	@JsonProperty("scriptParameters")
	private Map<String, String> scriptParameters = new HashMap<>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return
	 *     The scriptFile
	 */
	@JsonProperty("scriptFile")
	public String getScriptFile() {
		return this.scriptFile;
	}

	/**
	 *
	 * @param scriptFile
	 *     The scriptFile
	 */
	@JsonProperty("scriptFile")
	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	public MeanSpeed withScriptFile(String scriptfile) {
		this.scriptFile = scriptfile;
		return this;
	}

	/**
	 *
	 * @return
	 *     The scriptParameters
	 */
	@JsonProperty("scriptParameters")
	public Map<String, String> getScriptParameters() {
		return this.scriptParameters;
	}

	/**
	 *
	 * @param scriptParameters
	 *     The scriptParameters
	 */
	@JsonProperty("scriptParameters")
	public void setScriptParameters( Map<String, String> scriptParameters) {
		this.scriptParameters = scriptParameters;
	}

	public void addScriptParameter(String key, String path) {
		this.scriptParameters.put(key, path);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public MeanSpeed withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
