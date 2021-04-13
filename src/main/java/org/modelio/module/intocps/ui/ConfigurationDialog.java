package org.modelio.module.intocps.ui;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ConfigurationDialog extends Dialog {

	protected Object result;
	protected Shell shlConfiguration;
	private Table init;
	private Table table;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConfigurationDialog(Shell parent, int style) {
		super(parent, style);
		setText("Configuration Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		this.shlConfiguration.open();
		this.shlConfiguration.layout();
		Display display = getParent().getDisplay();
		while (!this.shlConfiguration.isDisposed()) {
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
	    this.shlConfiguration = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
	    this.shlConfiguration.setSize(689, 526);
	    this.shlConfiguration.setText("Configuration");

		Group grpParameters = new Group(this.shlConfiguration, SWT.NONE);
		grpParameters.setText("Parameters");
		grpParameters.setBounds(10, 29, 663, 176);

		Composite composite = new Composite(grpParameters, SWT.NONE);
		composite.setBounds(10, 25, 604, 141);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);

		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnNewColumn, new ColumnPixelData(150, true, true));
		tblclmnNewColumn.setText("Property");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValue_1 = tableViewerColumn_1.getColumn();
		tcl_composite.setColumnData(tblclmnValue_1, new ColumnPixelData(150, true, true));
		tblclmnValue_1.setText("Value");

		Button btnLaunch = new Button(this.shlConfiguration, SWT.NONE);
		btnLaunch.setText("Launch");
		btnLaunch.setBounds(598, 463, 75, 25);

		Group grpInitialValues = new Group(this.shlConfiguration, SWT.NONE);
		grpInitialValues.setText("Initial values");
		grpInitialValues.setBounds(10, 235, 663, 201);

		TableViewer tableViewer_1 = new TableViewer(grpInitialValues, SWT.BORDER | SWT.FULL_SELECTION);
		this.init = tableViewer_1.getTable();
		this.init.setBounds(10, 35, 616, 156);
		this.init.setHeaderVisible(true);
		this.init.setLinesVisible(true);

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn_3.setWidth(96);
		tblclmnNewColumn_3.setText("Variable");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnNewColumn_4 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn_4.setWidth(141);
		tblclmnNewColumn_4.setText("Type");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_5.getColumn();
		tblclmnValue.setWidth(350);
		tblclmnValue.setText("Value");

		ToolBar toolBar = new ToolBar(this.shlConfiguration, SWT.FLAT | SWT.RIGHT);
		toolBar.setBounds(0, 0, 673, 23);

		ToolItem tltmNew = new ToolItem(toolBar, SWT.NONE);
		tltmNew.setText("New");

		ToolItem tltmLoad = new ToolItem(toolBar, SWT.NONE);
		tltmLoad.setText("Load");

		ToolItem tltmSaveas = new ToolItem(toolBar, SWT.NONE);
		tltmSaveas.setText("Save");

		ToolItem tltmSaveas_1 = new ToolItem(toolBar, SWT.NONE);
		tltmSaveas_1.setText("SaveAs");

	}
}
