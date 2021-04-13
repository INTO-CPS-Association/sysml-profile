/**
 * Java Class : BlockInstanceDiagramCommand.java
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
 * @category   Command Diagram
 * @package    com.modeliosoft.modelio.intocps.gui.diagram
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.command.diagram;

import java.util.List;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.tools.DefaultBoxTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.enumeration.POComponentKind;
import org.modelio.module.sysml.utils.ModelUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The diagram command which handles the creation of a blockinstance
 * @author ebrosse
 *
 */
public class POComponentDiagramCommand extends DefaultBoxTool{

    @Override

    public boolean acceptElement(IDiagramHandle representation, IDiagramGraphic target) {
        MObject element = target.getElement();
        if (element instanceof AbstractDiagram) {
            element = ((AbstractDiagram) element).getOrigin();
        }

        return  ((element != null)
                && (element.getStatus().isModifiable()
                        && (element instanceof org.modelio.metamodel.uml.statik.Package)));
    }


    @Override

    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic target, Rectangle rect) {
        IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();

        Stereotype sterPOComponent = session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.POCOMPONENT, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(org.modelio.metamodel.uml.statik.Class.class));

        Stereotype sterPhysical = session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.PHYSICAL, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(org.modelio.metamodel.uml.statik.Class.class));


        String name = "POComponent";

        org.modelio.metamodel.uml.statik.Class system = null;

        try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","POComponent"))){
            MObject element = target.getElement();

            if (element instanceof AbstractDiagram) {
                element = ((AbstractDiagram) element).getOrigin();
            }


            if (element instanceof org.modelio.metamodel.uml.statik.Package){
                system = session.getModel().createClass();
                system.setOwner((org.modelio.metamodel.uml.statik.Package) element);
            }

            if (system != null){

                session.getModel().getDefaultNameService().setDefaultName(system, name);
                system.getExtension().add(sterPOComponent);
                system.getExtension().add(sterPhysical);

                ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, POComponentKind.Physical.name(), system);

                List<IDiagramGraphic> graph = representation.unmask(system, rect.x, rect.y);

                if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode))
                    ((IDiagramNode)graph.get(0)).setBounds(rect);

                representation.save();
                representation.close();
                transaction.commit ();
            }
        }
    }

}
