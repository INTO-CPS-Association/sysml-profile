package org.modelio.module.intocps.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConnectionExport extends Dialog {

	protected Object result;
	protected Shell shlConnectionExport;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConnectionExport(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		this.shlConnectionExport.open();
		this.shlConnectionExport.layout();
		Display display = getParent().getDisplay();
		while (!this.shlConnectionExport.isDisposed()) {
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
	    this.shlConnectionExport = new Shell(getParent(), getStyle());
	    this.shlConnectionExport.setSize(450, 73);
	    this.shlConnectionExport.setText("Connection Export");
		
		Button btnNewButton = new Button(this.shlConnectionExport, SWT.NONE);
		btnNewButton.setBounds(359, 10, 75, 25);
		btnNewButton.setText("Export");
		
		this.text = new Text(this.shlConnectionExport, SWT.BORDER);
		this.text.setBounds(10, 14, 331, 21);

	}

}
