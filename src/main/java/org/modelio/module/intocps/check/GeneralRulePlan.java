package org.modelio.module.intocps.check;

import java.util.ArrayList;
import java.util.List;

public class GeneralRulePlan implements IRulePlan {

	List<IRule> rules = new ArrayList<>();

	public GeneralRulePlan(){
		this.rules.add(new Rule001());
		this.rules.add(new Rule002());
	}

	@Override
    public List<IRule> getRules(){
		return this.rules;
	}

}
