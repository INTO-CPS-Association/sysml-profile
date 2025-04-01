/**
 * Java Class : CompositionDiagramCommand.java
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

import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkRoute;
import org.modelio.api.modelio.diagram.InvalidDestinationPointException;
import org.modelio.api.modelio.diagram.InvalidPointsPathException;
import org.modelio.api.modelio.diagram.InvalidSourcePointException;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * The diagram command which handles the creation of an UML/SysML Composition
 * @author ebrosse
 */
@objid ("6631048c-d39f-465a-8a54-34f944a4f27e")
public class CompositionDiagramCommand extends DefaultLinkTool {
    @objid ("86de30b6-346d-42a3-8795-4becec2eeb83")
    @Override
    public boolean acceptFirstElement(IDiagramHandle arg0, IDiagramGraphic arg1) {
        if ((arg1 != null) && (arg1.getElement() != null)){
            MObject element = arg1.getElement();
            return ((element).getStatus().isModifiable () && (element instanceof Classifier));
        }
        return false;
    }

    @objid ("a63a0b57-27c7-4546-b39c-e42679ca4dcb")
    @Override
    public boolean acceptSecondElement(IDiagramHandle arg0, IDiagramGraphic arg1, IDiagramGraphic arg2) {
        return ((arg2 != null) && (arg2.getElement() != null) && (arg2.getElement() instanceof Classifier));
    }

    @objid ("7aa66109-5bc9-4517-80a9-881a5a1d131f")
    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic arg1, IDiagramGraphic arg2, LinkRouterKind kind, ILinkRoute path) {
        IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
        IUmlModel model = session.getModel();

        try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","Composition"))){

        	Classifier source = (Classifier) arg1.getElement();
            Classifier destination = (Classifier) arg2.getElement();

            AssociationEnd sourceRole = model.createAssociationEnd();
            sourceRole.setSource(source);
            sourceRole.setTarget(destination);
            sourceRole.setAggregation(AggregationKind.KINDISCOMPOSITION);
            sourceRole.setName(destination.getName().toLowerCase());
            sourceRole.setMultiplicityMin("1");
            sourceRole.setMultiplicityMax("1");

            AssociationEnd destinationRole = model.createAssociationEnd();
            destinationRole.setMultiplicityMin("1");
            destinationRole.setMultiplicityMax("1");

            // Opposite relation must be set for both ends
            destinationRole.setOpposite(sourceRole);
            sourceRole.setOpposite(destinationRole);

            // Create the association itself
            Association association = model.createAssociation();
            destinationRole.setAssociation(association);
            sourceRole.setAssociation(association);

            List<IDiagramGraphic> graphics = representation.unmask(association, 0 , 0);
            for (IDiagramGraphic graphic : graphics){
                if (graphic instanceof IDiagramLink){
                    IDiagramLink link = (IDiagramLink) graphic;
                    link.setRouterKind(kind);
                    link.setRoute(path);
                }
            }

            representation.save();
            representation.close();
            transaction.commit ();
        } catch (InvalidSourcePointException e) {
        	INTOCPSModule.logService.error(e);
        } catch (InvalidPointsPathException e) {
        	INTOCPSModule.logService.error(e);
        } catch (InvalidDestinationPointException e) {
        	INTOCPSModule.logService.error(e);
        }
    }


}
