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
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.module.IModule;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.configuration.ConfigurationExporter;
import org.modelio.module.intocps.traceability.GitRepository;
import org.modelio.module.intocps.utils.FileUtils;
import org.modelio.module.intocps.utils.ResourcesManager;

/**
 * This class provides the XMI export dialog
 * @author ebrosse
 */
@objid ("f91e2337-fef4-4e6e-b382-cb9fb518f9b2")
public class ConfigurationWizardExport extends FileWizardWindow {

	private IModule module = null;

	public ConfigurationWizardExport(Shell parent, IModule module) {
		super(parent);
		this.module = module;
	}

	@objid ("4e2d6edc-3ccc-4cba-ad1f-15d93e376374")
	@Override
	public void setLabels() {
		setTitle("Title");
		setDescription("Description");
		setFrametitle("Configuration generation");
		setCancelButton("Cancel");
		setValidateButton("Generate");
	}

	@objid ("9029119f-2c5b-4472-b2ad-9ecfc3a56cbc")
	@Override
	public void validationAction() {

		File theFile = new File(ResourcesManager.getInstance().getConfigPath() + File.separator + this.nameComposite.getText() + ".sysml.json");

		if (!theFile.getParentFile().exists()){
			theFile.getParentFile().mkdirs();
		}

//		try {
//			GProject.getProject(this.selectedElt).save(null);
//		} catch (IOException e) {
//			INTOCPSModule.logService.error(e);
//		}

		try {
			ConfigurationExporter exporter = new ConfigurationExporter();
			exporter.exportConfiguration(this.selectedElt, theFile);

			//traceability
			GitRepository gitRepository = new GitRepository(this.module.getModuleContext());
			gitRepository.configurationCoSimulation(theFile.getAbsolutePath(), this.selectedElt);

			completeBox();

		} catch (Exception e) {
			INTOCPSModule.logService.error(e);
		}

		String imagePath = ResourcesManager.getInstance().getConfigPath() + File.separator + this.nameComposite.getText() + ".sysml.png";
		FileUtils.saveRelatedDiag(this.selectedElt, imagePath, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CONNECTIONDIAGRAM);

	}


	@objid ("fdbbd9fa-c792-4c27-b1e3-f95ee6a8cd9c")
	@Override
	public void setPath() {
		if (this.path.equals(""))
			this.path = INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString();

		this.nameComposite.setText(this.selectedElt.getName());
	}


	@objid ("063e9bd5-0823-4284-be39-aca4a7fb2a8c")
	@Override
	public void setDefaultDialog() {
		setPath();
	}


}
