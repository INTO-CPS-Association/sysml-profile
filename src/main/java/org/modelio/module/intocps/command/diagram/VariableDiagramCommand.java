/**
 * Java Class : UMLAggregationDiagramCommand.java
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
 * @package    com.modeliosoft.modelio.sysml.gui.diagram
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
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The diagram command which handles the creation of an SysML/UML Attribute
 * @author ebrosse
 *
 */
public class VariableDiagramCommand extends DefaultBoxTool{

    @Override
    public boolean acceptElement(IDiagramHandle arg0, IDiagramGraphic arg1) {
        if ((arg1 != null) && (arg1.getElement() != null)){
            MObject element = arg1.getElement();
            return(element.getStatus().isModifiable()
                    &&  (element instanceof Classifier));
        }
        return false;
    }




    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic arg1, Rectangle rect) {
        IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
        IUmlModel model = session.getModel();
        try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","Variable"))){

        	Classifier owner = (Classifier) arg1.getElement();

            Attribute attribute = model.createAttribute();
            attribute.setOwner(owner);

            session.getModel().getDefaultNameService().setDefaultName(attribute, "variable");

            Stereotype sterVariable = session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
            		INTOCPSStereotypes.VARIABLE, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Attribute.class));

            attribute.getExtension().add(sterVariable);

            List<IDiagramGraphic> graph = representation.unmask(attribute, rect.x , rect.y);
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode))
            	((IDiagramNode)graph.get(0)).setBounds(rect);

            representation.save();
            representation.close();
            transaction.commit ();
        }
    }

}
