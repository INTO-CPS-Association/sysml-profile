package org.modelio.module.intocps.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FMUExport extends Dialog {

	protected Object result;
	protected Shell shlFmuExport;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FMUExport(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		this.shlFmuExport.open();
		this.shlFmuExport.layout();
		Display display = getParent().getDisplay();
		while (!this.shlFmuExport.isDisposed()) {
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
	    this.shlFmuExport = new Shell(getParent(), getStyle());
	    this.shlFmuExport.setSize(450, 77);
	    this.shlFmuExport.setText("FMU Export");
		
		Button btnNewButton = new Button(this.shlFmuExport, SWT.NONE);
		btnNewButton.setBounds(359, 10, 75, 25);
		btnNewButton.setText("Export");
		
		this.text = new Text(this.shlFmuExport, SWT.BORDER);
		this.text.setBounds(10, 14, 327, 21);

	}
}
