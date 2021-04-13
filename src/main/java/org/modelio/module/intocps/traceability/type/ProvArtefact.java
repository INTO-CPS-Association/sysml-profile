
package org.modelio.module.intocps.traceability.type;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"rdf:about",
	"path",
	"hash",
	"type",

})
public class ProvArtefact implements ProvEntity{

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
	@JsonProperty("path")
	private String path;
	/**
	 * (Required)
	 *
	 */
	@JsonProperty("hash")
	private String hash;
	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("type")
	private ProvArtefact.Type type;

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("oslc:satisfies")
	@JsonDeserialize(as = java.util.LinkedHashSet.class)
	private ProvUsed oslcSatisfies = null;

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("prov:hadMember")
	@JsonDeserialize(as = java.util.LinkedHashSet.class)
	private ProvUsed provHadMember = null;

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("prov:wasDerivedFrom")
	@JsonDeserialize(as = java.util.LinkedHashSet.class)
	private ProvUsed provWasDerivedFrom = null;

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("oslc:verifies")
	@JsonDeserialize(as = java.util.LinkedHashSet.class)
	private ProvUsed oslcVerifies = null;

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("prov:hadMember")
	private ProvWasAssociatedWith provWasAssociatedWith;



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
	public ProvArtefact.Type getType() {
		return this.type;
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("type")
	public void setType(ProvArtefact.Type type) {
		this.type = type;
		updateRDFAbout();
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("hash")
	public String getHash() {
		return this.hash;
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("hash")
	public void setHash(String hash) {
		this.hash = hash;
		updateRDFAbout();
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("path")
	public String getPath() {
		return this.path;
	}

	/**
	 *
	 * (Required)
	 *
	 */
	@JsonProperty("path")
	public void setPath(String path) {
		this.path = path;
		updateRDFAbout();
	}

	private void updateRDFAbout(){
		if (this.type != null){
			this.rdfAbout = "Entity." + this.type.toString() + ":" + this.path + "#" + this.hash;
		}
	}

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("oslc:verifies")
	public ProvUsed getOslcVerifies() {
		return this.oslcVerifies;
	}

	public void addOslcVerifies(ProvArtefact oslcVerifie) {
		if (this.oslcVerifies == null){
			this.oslcVerifies = new ProvUsed();
		}
		this.oslcVerifies.addProvEntity(oslcVerifie.getRdfAbout());
	}

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("prov:wasDerivedFrom")
	public ProvUsed getProvWasDerivedFrom() {
		return this.provWasDerivedFrom;
	}

	public void addProvWasDerivedFrom(ProvArtefact derivedFrom) {
		if (this.provWasDerivedFrom == null){
			this.provWasDerivedFrom = new ProvUsed();
		}
		this.provWasDerivedFrom.addProvEntity(derivedFrom.getRdfAbout());
	}

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("oslc:satisfies")
	public ProvUsed getOslcSatisfies() {
		return this.oslcSatisfies;
	}

	public void addOslcSatisfies(ProvArtefact _oslcSatisfies) {
		if (this.oslcSatisfies == null){
			this.oslcSatisfies = new ProvUsed();
		}
		this.oslcSatisfies.addProvEntity(_oslcSatisfies.getRdfAbout());
	}

	/**
	 *
	 * (Optional)
	 *
	 */
	@JsonProperty("prov:hadMember")
	public ProvUsed getProvHadMember() {
		return this.provHadMember;
	}

	public void addProvHadMember(ProvArtefact _provHadMember) {
		if (this.provHadMember == null){
			this.provHadMember = new ProvUsed();
		}
		this.provHadMember.addProvEntity(_provHadMember.getRdfAbout());
	}

	public enum Type {

		ARCHITECTURECONFIGURATION("architectureConfiguration"),

		ARCHITECTURECONNECTIONDIAGRAM("architectureConnectionDiagram"),

		ARCHITECTUREMODELFILE("architectureModelFile"),

		ARCHITECTURESTRUCTUREDIAGRAM("architectureStructureDiagram"),

		ARCHITECTURESUBSYSTEM("architectureSubSystem"),

		DSEALGORITHM("dseAlgorithm"),

		DSEANALYSISSCRIPT("dseAnalysisScript"),

		DSERANKINGCRIPT("dseRankingScript"),

		DSERANKINGVALUES("dseRankingValues"),

		DSERESULT("dseResult"),

		DSESEARCHCONFIGURATION("dseSearchConfiguration"),

		DSEANALYSISCONFIGURATION("dseAnalysisConfiguration"),

		DESIGNNOTE("designNote"),

		DESIGNNOTEFILE("designNoteFile"),

		FMU("fmu"),

		FILE("file"),

		HILASSET("hiLAsset"),

		MODELCHECKMODEL("modelCheckModel"),

		MODELCHECKRESULT("modelCheckResult"),

		MODELDESCRIPTIONFILE("modelDescriptionFile"),

		MULTIMODELONFIGURATION("multiModelConfiguration"),

		OBJECTIVESVALUES("objectivesValues"),

		REQUIREMENT("requirement"),

		REQUIREMENTSOURCE("requirementSource"),

		REQUIREMENTSOURCESUBPART("requirementSourceSubPart"),

		REQUIREMENTDOCUMENT("requirementsDocument"),

		SCENARIODATA("scenarioData"),

		SIMULATIONCONFIGURATION("simulationConfiguration"),

		SIMULATIONMODELCONTAINER("simulationModelContainer"),

		SIMULATIONRESULT("simulationResult"),

		SOFTWAREAGENT("softwareAgent"),

		TESTCASE("testCase");

		private final String value;

		private final static Map<String, ProvArtefact.Type> CONSTANTS = new HashMap<>();

		static {
			for (ProvArtefact.Type c: values()) {
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
		public static ProvArtefact.Type fromValue(String value) {
			ProvArtefact.Type constant = CONSTANTS.get(value);
			if (constant == null) {
				throw new IllegalArgumentException(value);
			} else {
				return constant;
			}
		}

	}

}
