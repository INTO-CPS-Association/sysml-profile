package org.modelio.module.intocps.utils;

import org.modelio.module.intocps.model.fmi.Fmi2ScalarVariable;
import org.modelio.module.intocps.model.fmi.FmiModelDescription;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.ModelVariables;
import org.modelio.module.intocps.report.ReportModel;

public class FMIUtils {

	public static boolean isSubset(FmiModelDescription completeFMI, FmiModelDescription subFMI, ReportModel report){
		return isSubset(completeFMI.getModelVariables(), subFMI.getModelVariables(), report);
	}

	private static boolean isSubset(ModelVariables completeMV, ModelVariables subMV, ReportModel report){

		for (Fmi2ScalarVariable subVar : subMV.getScalarVariable()){

			boolean match = false;

			for (Fmi2ScalarVariable completeVar : completeMV.getScalarVariable()){
				if (equals(subVar, completeVar))
					match = true;
			}

			if (!(match)){
				report.addError("No match found for " + subVar.getName() + " variable", null);
			}

		}

		return !(report.isEmpty());

	}

	private static boolean equals(Fmi2ScalarVariable var1, Fmi2ScalarVariable var2){

		// test name attribut
		if (!var1.getName().equals(var2.getName())){
			return false;
		}

		// test causality
		if (!var1.getCausality().equals(var2.getCausality())){
			return false;
		}

		//test Integer type
		if (!((var1.getInteger() != null)
				&& ((var2.getInteger() != null)))){
			return false;
		}

		//test Real type
		if (!((var1.getReal() != null)
				&& ((var2.getReal() != null)))){
			return false;
		}

		//test String Type
		if (!((var1.getString() != null)
				&& ((var2.getString() != null)))){
			return false;
		}

		//test Enumeration type
		if (!((var1.getEnumeration() != null)
				&& ((var2.getEnumeration() != null)))){
			return false;
		}

		//test Boolean type
		if (!((var1.getBoolean() != null)
				&& ((var2.getBoolean() != null)))){
			return false;
		}

		return true;

	}
}
