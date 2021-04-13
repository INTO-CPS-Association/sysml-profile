/**
 * Java Class : UMLCompositionDiagramCommand.java
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
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.InvalidDestinationPointException;
import org.modelio.api.modelio.diagram.InvalidPointsPathException;
import org.modelio.api.modelio.diagram.InvalidSourcePointException;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The diagram command which handles the creation of an UML/SysML Composition
 * @author ebrosse
 */

public class ReferenceDiagramCommand extends DefaultLinkTool {

	@Override
	public boolean acceptFirstElement(IDiagramHandle arg0, IDiagramGraphic arg1) {
		if ((arg1 != null) && (arg1.getElement() != null)){
			MObject element = arg1.getElement();
			return ((element).getStatus().isModifiable ()
					&& (element instanceof ModelElement));

		}
		return false;
	}



	@Override
	public boolean acceptSecondElement(IDiagramHandle arg0, IDiagramGraphic arg1, IDiagramGraphic arg2) {
		if ((arg1 != null) && (arg1.getElement() != null)){
			MObject element = arg1.getElement();
			return ((element).getStatus().isModifiable ()
					&& (element instanceof ModelElement));

		}
		return false;
	}

	@Override
	public void actionPerformed(IDiagramHandle representation, IDiagramGraphic arg1, IDiagramGraphic arg2, LinkRouterKind kind, ILinkPath path) {
		IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
		IUmlModel model = session.getModel();

		Stereotype sterRef = session.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
				INTOCPSStereotypes.REFERENCE, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(Dependency.class));

		try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","Reference"))){

			ModelElement source = (ModelElement) arg1.getElement();
			ModelElement target = (ModelElement) arg2.getElement();

			Dependency connector = model.createDependency(source, target, sterRef);

			List<IDiagramGraphic> graphics = representation.unmask(connector, 0 , 0);
			for (IDiagramGraphic graphic : graphics){
				if (graphic instanceof IDiagramLink){
					IDiagramLink link = (IDiagramLink) graphic;
					link.setRouterKind(kind);
					link.setPath(path);
				}
			}

			representation.save();
			representation.close();
			transaction.commit();
		} catch (InvalidSourcePointException e) {
			INTOCPSModule.logService.error(e);
		} catch (InvalidPointsPathException e) {
			INTOCPSModule.logService.error(e);
		} catch (InvalidDestinationPointException e) {
			INTOCPSModule.logService.error(e);
		}
	}

}
