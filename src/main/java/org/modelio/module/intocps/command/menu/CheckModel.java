package org.modelio.module.intocps.command.menu;

import java.util.Collection;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.check.CheckRunner;
import org.modelio.module.intocps.check.GeneralRulePlan;
import org.modelio.module.intocps.check.IRulePlan;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.report.ReportManager;
import org.modelio.module.intocps.report.ReportModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of the IModuleContextualCommand interface.
 * <br>The module contextual commands are displayed in the contextual menu and in the specific toolbar of each module property page.
 * <br>The developer may inherit the DefaultModuleContextualCommand class which contains a default standard contextual command implementation.
 *
 */
public class CheckModel extends DefaultModuleCommandHandler {


	/**
	 * Constructor.
	 */
	public CheckModel() {
		super();
	}

	/**
	 * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
	 *      org.modelio.api.module.IModule)
	 */
	@Override
	public boolean accept(List<MObject> selectedElements, IModule module) {
		return true;
	}

	/**
	 * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
	 *      org.modelio.api.module.IModule)
	 */
	@Override
	public void actionPerformed(List<MObject> selectedElements, IModule module) {

		IRulePlan plan = new GeneralRulePlan();
		Collection<ModelElement> listObject = INTOCPSModule.getInstance().getModuleContext().getModelingSession().findByClass(ModelElement.class);

		ReportModel reportModel = ReportManager.getNewReport();

		CheckRunner check = new CheckRunner(plan, listObject, reportModel);
		check.run();

		if (!(reportModel.isEmpty()))
			ReportManager.showGenerationReport(Display.getCurrent().getActiveShell(), reportModel);

	}


}
