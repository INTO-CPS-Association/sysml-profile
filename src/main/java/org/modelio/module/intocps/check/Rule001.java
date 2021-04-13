


package org.modelio.module.intocps.check;

import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.module.intocps.report.IReportWriter;
import org.modelio.vcore.smkernel.mapi.MObject;


public class Rule001 implements IRule {

    private static final String ERRORID = "E001";

    private static final String message = "Internal TRansition must have a target";


    @Override
    public void check(final MObject object, final IReportWriter report) {
        InternalTransition currentInternalTransition = (InternalTransition) object;
        if (currentInternalTransition.getTarget() != null) {
            report.addError(ERRORID, object, message);
        }
    }


	@Override
	public boolean isValid(MObject object) {
		return (object instanceof InternalTransition);
	}


}
