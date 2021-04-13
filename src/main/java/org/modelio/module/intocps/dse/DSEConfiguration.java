package org.modelio.module.intocps.dse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DSEConfiguration {

	private Algorithm algorithm = new Algorithm();

	private List<ObjectiveConstraint> objectiveConstraints = new ArrayList<>();

	private ObjectiveDefinition objectiveDefinitions = new ObjectiveDefinition();

	private List<ParameterConstraint> parameterConstraints = new ArrayList<>();

	private Map<String, List<Double>> parameters = new HashMap<>();

	private Ranking ranking = new Ranking();

	private List<String> scenarios = new ArrayList<>();


	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public List<ObjectiveConstraint> getObjectiveConstraints() {
		return this.objectiveConstraints;
	}

	public void addObjectiveConstraints(ObjectiveConstraint objectiveConstraint) {
		this.objectiveConstraints.add(objectiveConstraint);
	}

	public ObjectiveDefinition getObjectiveDefinitions() {
		return this.objectiveDefinitions;
	}

	public void setObjectiveDefinitions(ObjectiveDefinition objectiveDefinitions) {
		this.objectiveDefinitions = objectiveDefinitions;
	}

	public List<ParameterConstraint> getParameterConstraints() {
		return this.parameterConstraints;
	}

	public void addParameterConstraints(ParameterConstraint parameterConstraint) {
		this.parameterConstraints.add(parameterConstraint);
	}

	public Map<String, List<Double>> getParameters() {
		return this.parameters;
	}

	public void addParameters(String key, List<Double> value) {
		this.parameters.put(key, value);
	}

	public Ranking getRanking() {
		return this.ranking;
	}

	public void setRanking(Ranking ranking) {
		this.ranking = ranking;
	}

	public List<String> getScenarios() {
		return this.scenarios;
	}

	public void addScenario(String scenario) {
		this.scenarios.add(scenario);
	}


}
