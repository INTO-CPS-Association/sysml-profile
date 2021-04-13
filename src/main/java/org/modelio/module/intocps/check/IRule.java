
package org.modelio.module.intocps.check;

import org.modelio.module.intocps.report.IReportWriter;
import org.modelio.vcore.smkernel.mapi.MObject;


public interface IRule {

    void check(MObject object, final IReportWriter report);

    boolean isValid(MObject object);
}
