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
package org.modelio.module.intocps.command.explorer;

import java.util.List;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;


/**
 * This class handles the creation of SysML block diagram
 * @author ebrosse
 *
 */
public class DSEParameterConnectionDiagram extends DefaultModuleCommandHandler {


    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {

        IModuleContext moduleContext = INTOCPSModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        StaticDiagram diagram = null;
        String name = "DSE Parameter Connection Diagram";
        Stereotype sterArchi = session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
        		INTOCPSStereotypes.DSEPARAMETERCONNECTIONDIAGRAM, moduleContext.getModelioServices().getMetamodelService().getMetamodel().getMClass(ClassDiagram.class));

        try( ITransaction transaction = session.createTransaction(I18nMessageService.getString ("Info.Session.Create","DSE Parameter Connection Daigram"))){

            diagram = session.getModel().createClassDiagram(name, (ModelElement) selectedElements.get(0), sterArchi);
            session.getModel().getDefaultNameService().setDefaultName(diagram, name);

            if (diagram != null) {
                IDiagramService ds = moduleContext.getModelioServices().getDiagramService();
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

                moduleContext.getModelioServices().getEditionService().openEditor(diagram);
            }

            transaction.commit ();
        } catch (Exception e) {
        	INTOCPSModule.logService.error(e);
        }


    }




    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {

        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElt = selectedElements.get(0);
            return ((selectedElt != null)
            		&& (selectedElt instanceof Package)
                    		&& !(selectedElt instanceof Profile));
        }
        return false;
    }

}
