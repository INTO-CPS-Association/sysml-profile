package org.modelio.module.intocps.ui;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.modelio.module.intocps.utils.SWTResourceManager;

public class CoSimulationExecution extends Dialog {

	protected Object result;
	protected Shell shlCosimulation;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CoSimulationExecution(Shell parent, int style) {
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
	    this.shlCosimulation.setSize(670, 517);
	    this.shlCosimulation.setText("CoSimulation");

		ToolBar toolBar = new ToolBar(this.shlCosimulation, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(0, 0, 654, 32);

		ToolItem tltmSaveas = new ToolItem(toolBar, SWT.NONE);
		tltmSaveas.setText("SaveAs");

		ToolItem tltmCompare = new ToolItem(toolBar, SWT.NONE);
		tltmCompare.setImage(null);
		tltmCompare.setText("Compare");

		ToolItem tltmZoom = new ToolItem(toolBar, SWT.NONE);
		tltmZoom.setText("Zoom +");

		ToolItem tltmZoom_1 = new ToolItem(toolBar, SWT.NONE);
		tltmZoom_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		tltmZoom_1.setText("Zoom -");

		ToolItem tltmLecture = new ToolItem(toolBar, SWT.NONE);
		tltmLecture.setText("Lecture");

		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.setText("Pause");

		ToolItem tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setText("Stop");

		Canvas canvas = new Canvas(this.shlCosimulation, SWT.NONE);
		canvas.setBounds(152, 43, 480, 328);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Group grpComponent = new Group(this.shlCosimulation, SWT.NONE);
		grpComponent.setBounds(10, 43, 136, 328);
		grpComponent.setText("Variable");

		CheckboxTreeViewer checkboxTreeViewer = new CheckboxTreeViewer(grpComponent, SWT.BORDER);
		Tree tree_1 = checkboxTreeViewer.getTree();
		tree_1.setBounds(10, 24, 116, 294);

		Group grpConsole = new Group(this.shlCosimulation, SWT.NONE);
		grpConsole.setText("Console");
		grpConsole.setBounds(10, 378, 622, 87);

		StyledText styledText = new StyledText(grpConsole, SWT.BORDER);
		styledText.setBounds(10, 21, 602, 56);

	}
}
