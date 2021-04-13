


package org.modelio.module.intocps.report;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.modelio.navigation.INavigationService;
import org.modelio.api.ui.ModelioDialog;
import org.modelio.module.intocps.report.ReportModel.ElementMessage;
import org.modelio.ui.UIColor;

/**
 * Dialog box used for the generation report.
 */
class ReportDialog extends ModelioDialog {

	private ReportModel model;


	INavigationService navigationService;


	private Image warningImage;


	private Image errorImage;


	Table table;


	private Image infoImage;


	Text descriptionText;


	public ReportDialog(Shell parentShell, final INavigationService navigationService) {
		super (parentShell);
		setShellStyle (SWT.DIALOG_TRIM | getDefaultOrientation ());
		this.navigationService = navigationService;
	}


	@Override
	public void addButtonsInButtonBar(Composite parent) {
		createButton (parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}


	@Override
	public Control createContentArea(Composite parent) {
		this.warningImage = JFaceResources.getImage ("dialog_messasge_warning_image"); //$NON-NLS-1$
		this.errorImage = JFaceResources.getImage ("dialog_message_error_image"); //$NON-NLS-1$
		this.infoImage = JFaceResources.getImage ("dialog_messasge_info_image"); //$NON-NLS-1$

		Point s = getInitialSize ();


		this.table = new Table (parent, SWT.MULTI | SWT.BORDER |
				SWT.FULL_SELECTION);
		GridData data = new GridData (SWT.FILL, SWT.FILL, true, true);
		this.table.setLayoutData (data);
		this.table.setLinesVisible (true);

		TableColumn column = new TableColumn (this.table, SWT.NONE);
		column.setText ("Message");

		updateViewFromModel ();

		column.setWidth (s.x);

		this.table.addMouseListener (new MouseListener () {
			@Override
			public void mouseDoubleClick (MouseEvent e) {
				TableItem item = ReportDialog.this.table.getItem (new Point (e.x, e.y));
				if (item != null) {
					if (item.getData () instanceof ElementMessage) {
						ElementMessage theElement = (ElementMessage) item.getData ();

						String desc = theElement.description;
						ReportDialog.this.descriptionText.setText (desc);

						// On a double clic, select the element
						if (theElement.element != null && theElement.element.isValid ()) {
							ReportDialog.this.navigationService.fireNavigate (theElement.element);
						}
					}
				}
			}

			@Override
			public void mouseDown (MouseEvent e) {
				// Nothing to do
			}

			@Override
			public void mouseUp (MouseEvent e) {
				TableItem item = ReportDialog.this.table.getItem (new Point (e.x, e.y));
				if (item != null) {
					if (item.getData () instanceof ElementMessage) {
						ElementMessage theElement = (ElementMessage) item.getData ();

						String desc = theElement.description;
						ReportDialog.this.descriptionText.setText (desc);

						// On a CTRL + clic, select the element
						if ((e.stateMask & SWT.CTRL) != 0) {
							if (theElement.element != null && theElement.element.isValid ()) {
								ReportDialog.this.navigationService.fireNavigate (theElement.element);
							}
						}
					}
				}
			}
		});

		this.descriptionText = new Text (parent, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		GridData data_description = new GridData (SWT.FILL, SWT.FILL, true, false);
		data_description.heightHint = 50;
		this.descriptionText.setLayoutData (data_description);
		this.descriptionText.setBackground (UIColor.TEXT_READONLY_BG);
		return parent;
	}

	@objid ("3a854dbd-3471-451f-a466-f9348b96599e")
	private void updateViewFromModel() {
		if (this.table != null) {
			this.table.removeAll ();

			if (this.model != null) {
				for (ElementMessage error : this.model.getErrors ()) {
					TableItem item = new TableItem (this.table, SWT.NONE);
					item.setImage (0, this.errorImage);
					item.setText (0, error.message);
					item.setData (error);
				}

				for (ElementMessage warning : this.model.getWarnings ()) {
					TableItem item = new TableItem (this.table, SWT.NONE);
					item.setImage (0, this.warningImage);
					item.setText (0, warning.message);
					item.setData (warning);
				}

				for (ElementMessage info : this.model.getInfos ()) {
					TableItem item = new TableItem (this.table, SWT.NONE);
					item.setImage (0, this.infoImage);
					item.setText (0, info.message);
					item.setData (info);
				}
			}

			this.table.getColumn (0).pack ();
		}
	}


	public void setModel(ReportModel model) {
		this.model = model;
		updateViewFromModel ();
	}


	public boolean isDisposed() {
		Shell s = getShell ();
		return s == null || s.isDisposed ();
	}


	@Override
	public void init() {
		Shell shell = getShell ();

		// Put the messages in the banner area
		setLogoImage (null);
		shell.setText ("ReportDialogTitle");
		setTitle ("Gui.Export.ReportDialogTitle");
		setMessage ("Gui.Export.ReportDialogMessage");
	}


	@Override
	protected Point getInitialSize() {
		Point p = super.getInitialSize();
		p.x = (int) Math.floor(p.x*1.35);
		return p;
	}

}
