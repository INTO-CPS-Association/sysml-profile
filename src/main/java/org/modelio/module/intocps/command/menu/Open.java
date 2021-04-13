package org.modelio.module.intocps.command.menu;

import java.io.IOException;
import java.util.List;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of the IModuleContextualCommand interface.
 * <br>The module contextual commands are displayed in the contextual menu and in the specific toolbar of each module property page.
 * <br>The developer may inherit the DefaultModuleContextualCommand class which contains a default standard contextual command implementation.
 *
 */
public class Open extends DefaultModuleCommandHandler {


    /**
     * Constructor.
     */
    public Open() {
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
                && (selectedElements.get(0) instanceof BindableInstance));
    }

    /**
     * @see org.modelio.api.module.commands.DefaultModuleContextualCommand#actionPerformed(java.util.List,
     *      org.modelio.api.module.IModule)
     */
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(new String[] { "D:\\Logs\\OpenModelica1.9.2Beta1\\bin\\OMEdit.exe" } );
        } catch (IOException e) {
        	INTOCPSModule.logService.error(e);
        }

    }

}
