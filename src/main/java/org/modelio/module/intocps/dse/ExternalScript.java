
package org.modelio.module.intocps.dse;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")

public class ExternalScript {

    @JsonProperty("scriptFile")
    private String scriptFile;

    @JsonProperty("scriptParameters")
    private Map<String, String> scriptParameters = new HashMap<>();



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
   public void addScriptParameters(String key, String scriptParameter) {
       this.scriptParameters.put(key, scriptParameter);
   }

}
