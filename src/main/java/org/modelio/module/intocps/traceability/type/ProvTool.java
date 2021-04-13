
package org.modelio.module.intocps.traceability.type;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"version",
	"name",
    "type",
    "rdf:about"
})
public class ProvTool implements ProvEntity{

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
    @JsonProperty("type")
    private ProvTool.Type type;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("name")
    private String name;
    /**
    * (Required)
    *
    */
   @JsonProperty("version")
   private String version;

   public ProvTool(){

   }

   public ProvTool(String name, String version, ProvTool.Type type){
	   this.name = name;
	   this.version = version;
	   this.type = type;
	   this.rdfAbout = "Entity.softwareTool" + ":" + name + ":" + version;

   }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("rdf:about")
	@Override
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

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public ProvTool.Type getType() {
        return this.type;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public void setType(ProvTool.Type type) {
        this.type = type;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
    *
    * (Required)
    *
    */
   @JsonProperty("version")
   public String getVersion() {
       return this.version;
   }

   /**
    *
    * (Required)
    *
    */
   @JsonProperty("version")
   public void setVersion(String version) {
       this.version = version;
   }

    public enum Type {

    	ARCHITECTURE_TOOL("Architecture Tool"),

    	CO_SIMULATION_ENGINE("Co Simulation Engine"),

    	CO_SIMULATION_GUI("Co Simulation GUI"),

    	SOFTWARE_TOOL("Software Tool"),

    	SIMULATION_TOOL("Simulation Tool"),

    	MODEL_CHECKING_TOOL("Model Checking Tool"),

    	TEST_AUTOMATION_TOOL("Test Automation Tool");

        private final String value;

        private final static Map<String, ProvTool.Type> CONSTANTS = new HashMap<>();

        static {
            for (ProvTool.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static ProvTool.Type fromValue(String value) {
            ProvTool.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
