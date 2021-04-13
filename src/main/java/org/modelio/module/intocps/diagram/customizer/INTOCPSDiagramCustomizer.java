package org.modelio.module.intocps.diagram.customizer;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.sysml.utils.IDiagramCustomizerPredefinedField;


/**
 * This class handles the common palette configuration of all I diagrams
 * @author ebrosse
 *
 */
public class INTOCPSDiagramCustomizer  {

	protected PaletteEntry createDefaultFreeDrawingGroup(final IDiagramService toolRegistry) {
		final PaletteDrawer group = new PaletteDrawer(I18nMessageService.getString("SysMLPaletteGroup.Freedrawing"), null);

		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingRectangle));
		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingEllipse));
		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingText));
		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingLine));

		group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
		return group;
	}

	protected PaletteEntry createDefaultNotesGroup(final IDiagramService toolRegistry) {
		final PaletteDrawer group = new PaletteDrawer(I18nMessageService.getString("SysMLPaletteGroup.NotesAndConstraints"), null);

		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Note));
		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Constraint));
		group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.ExternDocument));
		group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
		return group;
	}


}
