package org.modelio.module.intocps.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FMUConfig extends Dialog {

	protected Object result;
	protected Shell shlFmulist;
	private Table table;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FMUConfig(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		this.shlFmulist.open();
		this.shlFmulist.layout();
		Display display = getParent().getDisplay();
		while (!this.shlFmulist.isDisposed()) {
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
	    this.shlFmulist = new Shell(getParent(), getStyle());
	    this.shlFmulist.setSize(450, 300);
	    this.shlFmulist.setText("FMUList");
		
		Button btnNewButton = new Button(this.shlFmulist, SWT.NONE);
		btnNewButton.setBounds(359, 51, 75, 25);
		btnNewButton.setText("Add");
		
		Button btnNewButton_1 = new Button(this.shlFmulist, SWT.NONE);
		btnNewButton_1.setBounds(359, 92, 75, 25);
		btnNewButton_1.setText("Supress");
		
		TableViewer tableViewer = new TableViewer(this.shlFmulist, SWT.BORDER | SWT.FULL_SELECTION);
		this.table = tableViewer.getTable();
		this.table.setLinesVisible(true);
		this.table.setHeaderVisible(true);
		this.table.setBounds(10, 51, 334, 189);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnFmu = tableViewerColumn.getColumn();
		tblclmnFmu.setWidth(132);
		tblclmnFmu.setText("FMU");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTool = tableViewerColumn_1.getColumn();
		tblclmnTool.setWidth(175);
		tblclmnTool.setText("Tool");

	}
}
