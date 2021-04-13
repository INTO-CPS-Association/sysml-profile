package org.modelio.module.intocps.diagram.customizer;

import java.util.List;
import java.util.Map;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.modelio.api.modelio.diagram.IDiagramCustomizer;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.IModule;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.utils.IINTOCPSCustomizerPredefinedField;
import org.modelio.module.sysml.utils.IDiagramCustomizerPredefinedField;

/**
 * This class handles the palette configuration of Into CPS Connection diagram
 * @author ebrosse
 *
 */
public class ConnectionDiagramCustomizer extends INTOCPSDiagramCustomizer implements IDiagramCustomizer {

    @Override
    public void fillPalette(PaletteRoot paletteRoot) {
        IDiagramService toolRegistry = INTOCPSModule.getInstance().getModuleContext().getModelioServices().getDiagramService();

        final PaletteDrawer commonGroup = new PaletteDrawer("Default", null);
        commonGroup.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        commonGroup.add(new SelectionToolEntry());
        commonGroup.add(new MarqueeToolEntry());
        paletteRoot.add(commonGroup);

        paletteRoot.add(this.createInstanceGroup(toolRegistry));

        paletteRoot.add(this.createDefaultNotesGroup(toolRegistry));
        paletteRoot.add(this.createDefaultFreeDrawingGroup(toolRegistry));
    }


    private PaletteEntry createInstanceGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer("Connection", null);

        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.BlockInstance));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.Port));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.UMLConnector));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.InformationFlowRealized));

        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @Override
    public boolean keepBasePalette() {
        return false;
    }
    
    @Override
    public void initialize(IModule module, List<org.modelio.api.modelio.diagram.tools.PaletteEntry> tools, Map<String, String> hParameters, boolean keepBasePalette) {
 
    }

    @Override
    public Map<String, String> getParameters() {
        return null;
    }



}
