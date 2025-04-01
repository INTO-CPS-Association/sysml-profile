/**
 * Java Class : AddBlockDiagramExplorerCommand.java
 *
 * Description :
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 *
 * @category   Command explorer
 * @package    com.modeliosoft.modelio.sysml.gui.explorer
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.diagram.wizard;

import org.modelio.api.modelio.IModelioServices;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.contributor.AbstractWizardContributor;
import org.modelio.api.module.contributor.ElementDescriptor;
import org.modelio.api.module.contributor.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MMetamodel;


/**
 * This class handles the creation of SysML block diagram
 * @author ebrosse
 *
 */
public class ArchitectureDiagramWizard extends AbstractWizardContributor implements IDiagramWizardContributor {


    @Override
    public AbstractDiagram actionPerformed(ModelElement element, String diagramName,
            String description) {

        IModuleContext context = INTOCPSModule.getInstance().getModuleContext();
        IModelingSession session = context.getModelingSession();
        AbstractDiagram diagram = null;
        
        try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","Architecture Diagram"))){

            IModelioServices modelioServices = context.getModelioServices();

            diagram = session.getModel().createClassDiagram("architecture diagram",
                    element,
                    session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ARCHITECTUREDIAGRAM, modelioServices.getMetamodelService().getMetamodel().getMClass(ClassDiagram.class)));

            if (diagram != null) {
                IDiagramService ds = modelioServices.getDiagramService();
                try(IDiagramHandle handler = ds.getDiagramHandle(diagram)){
                    IDiagramDG dg = handler.getDiagramNode();

                    for (IStyleHandle style : ds.listStyles()){
                        if (style.getName().equals("intocps")){
                            dg.setStyle(style);
                            break;
                        }
                    }

                    handler.save();
                    handler.close();
                }

                modelioServices.getEditionService().openEditor(diagram);
            }

            transaction.commit ();
        }

        return diagram;
    }

    @Override
    public ElementDescriptor getCreatedElementType() {
    	IModuleContext moduleContext = getModule().getModuleContext();
        MMetamodel metamodel = moduleContext.getModelioServices().getMetamodelService().getMetamodel();
        MClass mClass = metamodel.getMClass(ClassDiagram.class);
        IMetamodelExtensions extensions = moduleContext.getModelingSession().getMetamodelExtensions();
        Stereotype stereotype = extensions.getStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ARCHITECTUREDIAGRAM, mClass);
        return stereotype != null ? new ElementDescriptor(mClass, stereotype) : null;
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean checkCanCreateIn(ModelElement selectedElt) {
		 return ((selectedElt != null)
	                && (selectedElt instanceof Package)
	                && !(selectedElt instanceof Profile));
	}



}
