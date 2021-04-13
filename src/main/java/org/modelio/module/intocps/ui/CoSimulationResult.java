package org.modelio.module.intocps.ui;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.modelio.module.intocps.utils.SWTResourceManager;

public class CoSimulationResult extends Dialog {

	protected Object result;
	protected Shell shlCosimulation;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CoSimulationResult(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		this.shlCosimulation.open();
		this.shlCosimulation.layout();
		Display display = getParent().getDisplay();
		while (!this.shlCosimulation.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
	    this.shlCosimulation = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
	    this.shlCosimulation.setSize(716, 396);
	    this.shlCosimulation.setText("CoSimulationResult");

		Canvas canvas = new Canvas(this.shlCosimulation, SWT.NONE);
		canvas.setBounds(152, 38, 548, 313);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Group grpComponent = new Group(this.shlCosimulation, SWT.NONE);
		grpComponent.setBounds(10, 32, 136, 319);
		grpComponent.setText("Variables");

		CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(grpComponent, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		tree.setBounds(10, 24, 116, 285);

		ToolBar toolBar = new ToolBar(this.shlCosimulation, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(0, 0, 700, 23);

		ToolItem tltmSaveas = new ToolItem(toolBar, SWT.NONE);
		tltmSaveas.setText("SaveAs");

		ToolItem tltmAddremove = new ToolItem(toolBar, SWT.NONE);
		tltmAddremove.setText("Compare");

		ToolItem tltmZoom = new ToolItem(toolBar, SWT.NONE);
		tltmZoom.setText("Zoom+");

		ToolItem tltmZoom_1 = new ToolItem(toolBar, SWT.NONE);
		tltmZoom_1.setText("Zoom-");

	}
}
