
package org.modelio.module.intocps.traceability.type;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rdf:about",
    "type",
    "time",
    "prov:wasAssociatedWith",
    "prov:used"
})
public class ProvActivity {

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
    private ProvActivity.Type type;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("time")
    private String time;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:wasAssociatedWith")
    private ProvWasAssociatedWith provWasAssociatedWith;
    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:used")
    private ProvUsed provUsed;

    public ProvActivity(ProvActivity.Type type, String time){
    	this.type = type;
    	this.time = time;
    	this.rdfAbout = "Activity." + type.toString() + ":" + time + "#" + UUID.randomUUID().toString();
    }

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

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public ProvActivity.Type getType() {
        return this.type;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("type")
    public void setType(ProvActivity.Type type) {
        this.type = type;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("time")
    public String getTime() {
        return this.time;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:wasAssociatedWith")
    public ProvWasAssociatedWith getProvWasAssociatedWith() {
        return this.provWasAssociatedWith;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:wasAssociatedWith")
    public void setProvWasAssociatedWith(ProvWasAssociatedWith provWasAssociatedWith) {
        this.provWasAssociatedWith = provWasAssociatedWith;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:used")
    public ProvUsed getProvUsed() {
        return this.provUsed;
    }

    /**
     *
     * (Required)
     *
     */
    @JsonProperty("prov:used")
    public void setProvUsed(ProvUsed provUsed) {
        this.provUsed = provUsed;
    }

    public enum Type {

        ARCHITECTURE_CONFIGURATION_CREATION("architectureConfigurationCreation"),

        ARCHITECTURE_MODELLING("architectureModelling"),

        CODE_GENERATION("codeGeneration"),

        CONFIGURATION_CREATION("configurationCreation"),

        DEFINE_CTABSTRACTION("defineCTAbstraction"),

        DEFINE_MCMODEL("defineMCModel"),

        DEFINE_MCQUERY("defineMCQuery"),

        DEFINE_TEST_MODEL("defineTestModel"),

        DEFINE_TEST_OBJECTIVE("defineTestObjectives"),

        DESIGN_NOTE_CREATION("designNoteCreation"),

        DSE("dse"),

        DSE_ANALYSIS_CREATION("dseAnalysisCreation"),

        DSE_CONFIGURATION_CREATION("dseConfigurationCreation"),

        FMU_EXPORT("fmuExport"),

        FMU_EXPORT_FOR_HIL("fmuExportForHiL"),

        MODEL_DESCRIPTION_EXPORT("modelDescriptionExport"),

        MODEL_CHECKING("modelChecking"),

        MODEL_DESCRIPTION_IMPORT("modelDescriptionImport"),

        REQUIREMENTS_MANAGEMENT("requirementsManagement"),

        RUNMCQUERY("runMCQuery"),

        RUNTEST("runTest"),

        SIMULATION("simulation"),

        SIMULATION_CONFIGURATION_CREATION("simulationConfigurationCreation"),

        SIMULATION_MODELLING("simulationModelling"),

        TEST_CREATION("testCreation");

        private final String value;

        private final static Map<String, ProvActivity.Type> CONSTANTS = new HashMap<>();

        static {
            for (ProvActivity.Type c: values()) {
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
        public static ProvActivity.Type fromValue(String value) {
            ProvActivity.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
