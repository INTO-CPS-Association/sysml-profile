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
import org.modelio.module.sysml.utils.ISysMLCustomizerPredefinedField;

/**
 * This class handles the palette configuration of Into CPS architecture diagram
 * @author ebrosse
 *
 */
public class ArchitectureDiagramCustomizer extends INTOCPSDiagramCustomizer implements IDiagramCustomizer {

    @Override
    public void fillPalette(PaletteRoot paletteRoot) {
        IDiagramService toolRegistry = INTOCPSModule.getInstance().getModuleContext().getModelioServices().getDiagramService();

        final PaletteDrawer commonGroup = new PaletteDrawer("Default", null);
        commonGroup.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        commonGroup.add(new SelectionToolEntry());
        commonGroup.add(new MarqueeToolEntry());
        paletteRoot.add(commonGroup);

        paletteRoot.add(this.createClassGroup(toolRegistry));
        paletteRoot.add(this.createTypeGroup(toolRegistry));
        paletteRoot.add(this.createDefaultNotesGroup(toolRegistry));
        paletteRoot.add(this.createDefaultFreeDrawingGroup(toolRegistry));

    }


    private PaletteEntry createClassGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer("Architecture", null);
        group.add(toolRegistry.getRegisteredTool(ISysMLCustomizerPredefinedField.Block));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.System));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.CComponent));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.EComponent));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.POComponent));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.FlowPort));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.Variable));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.UMLComposition));


        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }


    private PaletteEntry createTypeGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer("Type", null);
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.DType));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.UnitType));
        group.add(toolRegistry.getRegisteredTool(IINTOCPSCustomizerPredefinedField.StrtType));

        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Enumeration));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.EnumerationLiteral));

        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }


    @Override
    public boolean keepBasePalette() {
        return false;
    }

    @Override
    public Map<String, String> getParameters() {
        return null;
    }

    @Override
    public void initialize(IModule module, List<org.modelio.api.modelio.diagram.tools.PaletteEntry> tools, Map<String, String> hParameters, boolean keepBasePalette) {
 
    }


}
