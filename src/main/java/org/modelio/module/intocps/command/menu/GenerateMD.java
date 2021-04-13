package org.modelio.module.intocps.command.menu;

import java.util.List;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.ui.FMIWizardExport;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of the IModuleContextualCommand interface.
 * <br>The module contextual commands are displayed in the contextual menu and in the specific toolbar of each module property page.
 * <br>The developer may inherit the DefaultModuleContextualCommand class which contains a default standard contextual command implementation.
 *
 */
public class GenerateMD extends DefaultModuleCommandHandler {


	/**
	 * Constructor.
	 */
	public GenerateMD() {
		super();
	}

	/**
	 * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#accept(java.util.List,
	 *      org.modelio.api.module.IModule)
	 */
	@Override
	public boolean accept(List<MObject> selectedElements, IModule module) {
		// Check that there is only one selected element
		return ((selectedElements.size() == 1)
				&& (selectedElements.get(0) instanceof Class)
				&& ((Class) selectedElements.get(0)).isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT));
	}

	/**
	 * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
	 *      org.modelio.api.module.IModule)
	 */
	@Override
	public void actionPerformed(List<MObject> selectedElements, IModule module) {

		FMIWizardExport export = new FMIWizardExport(Display.getCurrent().getActiveShell(), module);
		export.setSelectedElt((ModelElement)selectedElements.get(0));
		export.open();

	}


}
