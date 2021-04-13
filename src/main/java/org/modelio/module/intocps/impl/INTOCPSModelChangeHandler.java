/**
 * Java Class : SysMLModelChangeHandler.java
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
 * @category   Modelio Impl
 * @package    com.modeliosoft.modelio.sysml.impl
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.event.IElementDeletedEvent;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.traceability.ElementStore;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.intocps.utils.RequirementUtils;
import org.modelio.module.sysml.utils.ModelUtils;
import org.modelio.vcore.smkernel.mapi.MObject;



/**
 *  @author ebrosse
 */

public class INTOCPSModelChangeHandler implements IModelChangeHandler {

	@Override
	public void handleModelChange(
			IModelingSession session,
			IModelChangeEvent event) {

		ElementStore eltStore = ElementStore.getInstance();

		//Update events
		Set<MObject> updatedElements = event.getUpdateEvents();

		for (MObject updatedElement : updatedElements){

			if (updatedElement instanceof Port){
				updateFlowPort((Port) updatedElement);
				updatePort((Port) updatedElement);
			}else if (updatedElement instanceof org.modelio.metamodel.uml.statik.Class){
				updateClass((org.modelio.metamodel.uml.statik.Class) updatedElement);
			}
			
			
			if ((updatedElement instanceof StaticDiagram)
					|| (updatedElement instanceof Classifier)
					|| (updatedElement instanceof BindableInstance)){
				eltStore.addElement((ModelElement) updatedElement);
			}else if (INTOCPSModule.isAnalystDeployed()){
			    RequirementUtils.addRequirement(updatedElement);
			}

		}

		//Create events
		Set<MObject> createdElements = event.getCreationEvents();
		for (MObject createdElt : createdElements){
			checkUpdate(createdElt);
			if (createdElt instanceof ModelElement){
				eltStore.addElement((ModelElement)createdElt);
				if (createdElt instanceof Dependency){
					eltStore.addElement(((Dependency) createdElt).getImpacted());
				}
			}
		}


		//Deleted events
		List<IElementDeletedEvent> deletedEvents = event.getDeleteEvents();
		for (IElementDeletedEvent deletedEvent : deletedEvents){
			checkUpdate(deletedEvent.getOldParent());
			MObject deletedElt = deletedEvent.getDeletedElement();
			if (deletedElt instanceof ModelElement){
				eltStore.removeElement((ModelElement)deletedElt);
			}
		}
	}

	private void checkUpdate(final MObject createdElt) {

	    MObject currentElt = createdElt;
		while(currentElt != null ){
		    
			if ((currentElt instanceof org.modelio.metamodel.uml.statik.Class)
					&& ((org.modelio.metamodel.uml.statik.Class) currentElt).isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT)){
				updateGuid((org.modelio.metamodel.uml.statik.Class) currentElt);
				break;
			}else{
			    currentElt = currentElt.getCompositionOwner();
			}
		}

		if (createdElt instanceof Port){
			updatedPort((Port) createdElt);
		}

	}


	private void updatedPort(Port port) {
		MObject owner = port.getCompositionOwner();
		if (owner instanceof BindableInstance){
			port.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
		}
	}


	private void updateClass(org.modelio.metamodel.uml.statik.Class classe) {

		if (classe.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SYSTEM)){
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CYBER);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PHYSICAL);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ENVIRONMENT);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VISUALISATION);
		}else if (classe.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CCOMPONENT)){
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CYBER);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PHYSICAL);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ENVIRONMENT);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VISUALISATION);
		}else if (classe.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.POCOMPONENT)){
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);
		}else if (classe.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT)){
			classe.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);
			updateGuid(classe);
		}
	}

	private void updateGuid(org.modelio.metamodel.uml.statik.Class classe) {
		IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();

		//Get TagType definition
		TagType tt = session.getMetamodelExtensions().getTagType(
				session.getMetamodelExtensions().getStereotype(INTOCPSStereotypes.ECOMPONENT, classe.getMClass()),
				INTOCPSTagTypes.ECOMPONENT_GUID);

		//Find corresponding TaggedValue
		TaggedValue tv = null;

		for(TaggedValue tag : classe.getTag()){
			if (tag.getDefinition().equals(tt))
				tv = tag;
		}

		//or create it
		if (tv == null){
			tv = session.getModel().createTaggedValue();
			tv.setDefinition(tt);
			tv.setAnnoted(classe);
		}

		//Delete all Actual
		for (TagParameter actualParam : tv.getActual() ){
			actualParam.delete();
		}

		//set Value
		TagParameter param = tv.getQualifier();
		if (param != null){
			param.delete();
		}

		param = session.getModel().createTagParameter();
		tv.getActual().add(param);

		param.setValue(UUID.randomUUID().toString());


	}


	private void updatePort(Port port) {
		if (port.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.FLOWPORT)){
			PortOrientation dir = port.getDirection();
			for (BindableInstance instance : port.getRepresentingInstance()){
				if (instance instanceof Port){
					((Port) instance).setDirection(dir);
				}
			}
		}
	}


	private void updateFlowPort(Port port) {
		if (port.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.FLOWPORT)){

			PortOrientation dir = port.getDirection();

			switch (dir) {
			case IN:
				ModelsUtils.addValue(IINTOCPSPeerModule.MODULE_NAME,INTOCPSTagTypes.FLOWPORT_DIRECTION, "in", port);
				break;

			case OUT:
				ModelsUtils.addValue(IINTOCPSPeerModule.MODULE_NAME,INTOCPSTagTypes.FLOWPORT_DIRECTION, "out", port);
				break;

			case INOUT:
				ModelsUtils.addValue(IINTOCPSPeerModule.MODULE_NAME,INTOCPSTagTypes.FLOWPORT_DIRECTION, "inout", port);
				break;

			case NONE:
				ModelsUtils.addValue(IINTOCPSPeerModule.MODULE_NAME,INTOCPSTagTypes.FLOWPORT_DIRECTION, "inout", port);
				break;

			default:
				ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME,INTOCPSTagTypes.FLOWPORT_DIRECTION, "inout", port);
				break;
			}

		}
	}

}
