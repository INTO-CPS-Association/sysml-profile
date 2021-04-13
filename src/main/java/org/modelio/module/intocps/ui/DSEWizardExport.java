/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package org.modelio.module.intocps.ui;

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.intocps.dse.DSEExporter;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.traceability.GitRepository;
import org.modelio.module.intocps.ui.composite.FileChooserComposite;
import org.modelio.module.intocps.ui.composite.ValidationBoutonComposite;
import org.modelio.module.intocps.utils.ResourcesManager;

/**
 * This class provides the XMI export dialog
 * @author ebrosse
 */
@objid ("f91e2337-fef4-4e6e-b382-cb9fb518f9b2")
public class DSEWizardExport extends AbstractSwtWizardWindow {

	private IModule module = null;

	@Override
    public Object open() {
		createContents();
		return super.open();
	}

	private void createContents() {
		setLabels();

		this.shell = new Shell(getParent(), 67696 | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.TITLE);
		this.shell.setLayout( new FormLayout());
		this.shell.setText(this.frametitle);

		// File chooser composite
		this.fileChooserComposite = new FileChooserComposite(this.shell, SWT.NONE, SWT.OPEN);


		// Validation Composite
		this.validateComposite = new ValidationBoutonComposite(this.shell, SWT.NONE, this.cancelButton, this.validateButton);

		this.validateComposite.getValidationButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (getFileChooserComposite().getCurrentFile() != null) {
					validationAction();
				} else {
					selectAFile();
				}
			}
		});

		this.validateComposite.getCancelButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setCancellation(true);
				cancelAction();
			}
		});

		final FormData fd_fileChooserComposite = new FormData();
		fd_fileChooserComposite.right = new FormAttachment(100, 0);
		fd_fileChooserComposite.bottom = new FormAttachment(0, 50);
		fd_fileChooserComposite.top = new FormAttachment(0, 0);
		fd_fileChooserComposite.left = new FormAttachment(0, 0);
		this.fileChooserComposite.setLayoutData(fd_fileChooserComposite);

		final FormData fd_validateComposite = new FormData();
		fd_validateComposite.top = new FormAttachment(this.fileChooserComposite, 5);
		fd_validateComposite.bottom = new FormAttachment(100, -5);
		fd_validateComposite.left = new FormAttachment(this.fileChooserComposite, 0, SWT.LEFT);
		fd_validateComposite.right = new FormAttachment(this.fileChooserComposite, 0, SWT.RIGHT);
		this.validateComposite.setLayoutData(fd_validateComposite);

		this.fileChooserComposite.getSearch().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DSEWizardExport.this.fileChooserComposite.searchFile();
			}
		});

		setDefaultDialog();
		this.shell.pack();
		this.shell.setMinimumSize(new Point(this.shell.getBounds().width, this.shell.getBounds().height));

		this.validateComposite.getValidationButton().setFocus();
	}


	public DSEWizardExport(Shell parent, IModule module) {
		super(parent);
		this.module = module;
	}

	@objid ("4e2d6edc-3ccc-4cba-ad1f-15d93e376374")
	@Override
	public void setLabels() {
		setTitle("Title");
		setDescription("Description");
		setFrametitle("DSE Export");
		setCancelButton("Cancel");
		setValidateButton("Export");
	}

	@objid ("9029119f-2c5b-4472-b2ad-9ecfc3a56cbc")
	@Override
	public void validationAction() {
		File generatedDSE = new File(ResourcesManager.getInstance().getConfigPath() + File.separator + this.fileChooserComposite.getText() + ".sysml-dse.json");

		if (!generatedDSE.getParentFile().exists()){
			generatedDSE.getParentFile().mkdirs();
		}

//		try {
//			GProject.getProject(this.selectedElt).save(null);
//		} catch (IOException e) {
//			INTOCPSModule.logService.error(e);
//		}

		try {

			DSEExporter exporter = new DSEExporter();
			exporter.exportDSE((Class) this.selectedElt, generatedDSE);

			//traceability
			GitRepository gitRepository = new GitRepository(this.module.getModuleContext());
			gitRepository.generateDSE(generatedDSE.getAbsolutePath(), this.selectedElt);

			completeBox();

		} catch (Exception e) {
			INTOCPSModule.logService.error(e);
		}
	}



	@objid ("fdbbd9fa-c792-4c27-b1e3-f95ee6a8cd9c")
	@Override
	public void setPath() {

		if (this.path.equals(""))
			this.path = INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString();

		this.fileChooserComposite.setText(this.selectedElt.getName());
	}


	@objid ("063e9bd5-0823-4284-be39-aca4a7fb2a8c")
	@Override
	public void setDefaultDialog() {
		this.fileChooserComposite.getDialog().setFilterNames(new String[] { "DSE JSON Files" });
		this.fileChooserComposite.getDialog().setFilterExtensions(new String[] { "*.sysml-dse.json" });
		setPath();
	}


}
