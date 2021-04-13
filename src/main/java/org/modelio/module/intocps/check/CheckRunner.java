package org.modelio.module.intocps.check;

import java.util.Collection;

import org.modelio.module.intocps.report.IReportWriter;
import org.modelio.vcore.smkernel.mapi.MObject;

public class CheckRunner {

	private Collection<? extends MObject> toBeChecked = null;

	private IRulePlan plan = null;

	private IReportWriter reportWriter = null;

	public CheckRunner(IRulePlan plan, Collection<? extends MObject> objects, IReportWriter reportWriter){
		this.toBeChecked = objects;
		this.plan = plan;
		this.reportWriter = reportWriter;
	}

	public void run(){
		for (MObject current : this.toBeChecked){
			for (IRule rule : this.plan.getRules()){
				if (rule.isValid(current))
					rule.check(current, this.reportWriter);
			}
		}
	}

}
