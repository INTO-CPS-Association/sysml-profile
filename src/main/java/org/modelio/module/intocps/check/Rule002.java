


package org.modelio.module.intocps.check;

import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.module.intocps.report.IReportWriter;
import org.modelio.vcore.smkernel.mapi.MObject;


public class Rule002 implements IRule {

    private static final String ERRORID = "E002";

    private static final String message = "Connection must be compatible";


    @Override
    public void check(final MObject object, final IReportWriter report) {
    	Connector connector = (Connector) object;

    	boolean error = false;

    	if (connector.getLinkEnd().size() == 2){

    	}



        if (error){
        	report.addError(ERRORID, object, message);
        }
    }


	@Override
	public boolean isValid(MObject object) {
		return (object instanceof Connector);
	}


}
