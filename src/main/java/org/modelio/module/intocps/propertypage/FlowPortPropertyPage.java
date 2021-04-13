/**
 * Java Class : CommonPropertyPage.java
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
 * @category   PropertyDefinition page
 * @package    com.modeliosoft.modelio.sysml.gui.propertypage
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.propertypage;

import java.util.ArrayList;
import java.util.List;
import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.enumeration.Direction;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.modelermodule.api.IModelerModuleNoteTypes;
import org.modelio.module.modelermodule.api.IModelerModulePeerModule;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class FlowPortPropertyPage implements IPropertyContent{


	List<GeneralClass> types =  ModelsUtils.getValueTypes();


	/**
	 * Default constructor
	 */
	public FlowPortPropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		//Types
		Port port = (Port) element;

		NameSpace base = port.getBase();
		String value_kind = "";

		if (base != null){
			value_kind = ModelsUtils.getQualifiedName(base);
		}

		String[] possibleType = ModelsUtils.createListString(this.types);
		table.addProperty("Type", value_kind, possibleType);

		//Init
		value_kind = port.getValue();
		table.addProperty("Init", value_kind);

		//Description
		IMetamodelExtensions me = INTOCPSModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions();
		Note descriptionNote = port.getNote(me.getNoteType(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION, port.getMClass()));
		if (descriptionNote == null){
			IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
			try( ITransaction transaction = session.createTransaction(I18nMessageService.getString ("Info.Session.Create",IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION))){
				descriptionNote = session.getModel().createNote(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION, port, "");
				transaction.commit();
			} catch (ExtensionNotFoundException e) {
				INTOCPSModule.logService.error(e);
			}
		}
		table.addProperty("Description", element.getNoteContent(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION));

		//Direction
		Direction[] portDirections = Direction.values();
		String[] portDirectionNames = new String[portDirections.length];
		for (int i = 0; i < portDirections.length; i++) {
			portDirectionNames[i] = portDirections[i].name();
		}

		table.addProperty("Direction", ((Port) element).getDirection().toString(), portDirectionNames);

		//Depends
		if (port.getDirection().equals(PortOrientation.OUT)){

			//List of Input Port
			List<Port> possibleDependingPorts = new ArrayList<>();

			NameSpace ns = port.getInternalOwner();

			if ((ns != null) && (ns instanceof Classifier)){
				for( Port pi : ((Classifier) ns).getInternalStructure(Port.class)){
					if (pi.getDirection().equals(PortOrientation.IN)){
						possibleDependingPorts.add(pi);
					}
				}
			}


			//List of depending Port
			List<Port> dependingPorts = new ArrayList<>();
			for(Dependency depends : port.getDependsOnDependency()){
				if (depends.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DEPENDS)){
					ModelElement target = depends.getDependsOn();
					if ((target instanceof Port)){
						if (!dependingPorts.contains(target))
							dependingPorts.add((Port) target);

						if (possibleDependingPorts.contains(target))
							possibleDependingPorts.remove(target);
					}
				}
			}


			List<String> values = new ArrayList<>();
			for (Port possibleDependingPort : possibleDependingPorts){
				values.add("Add " + ModelsUtils.getQualifiedName(possibleDependingPort));
			}

			for (Port dependingPort : dependingPorts){
				values.add("Remove " + ModelsUtils.getQualifiedName(dependingPort));
			}

			String[] valueString = new String[values.size()];

			String value = "";
			if (!dependingPorts.isEmpty()){

				for (Port dependingPort : dependingPorts){
					value += ModelsUtils.getQualifiedName(dependingPort) + ", ";
				}
				value = value.substring(0, value.length() - 2);
			}

			table.addProperty("Depends", value, values.toArray(valueString));

		}

	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		Port port = (Port) element;

		if(row == 1){

			for(Classifier type : this.types){
				if (value.equals(ModelsUtils.getQualifiedName(type))){
					port.setBase(type);
				}
			}

		}else if(row == 2){

			port.setValue(value);

		}else if(row == 3){

			Note descriptionNote = port.getNote(IModelerModulePeerModule.MODULE_NAME, "description");
			descriptionNote.setContent(value);

		}else if(row == 4){

			switch (value) {
			case "out":
				port.setDirection(PortOrientation.OUT);
				break;

			case "in":
				port.setDirection(PortOrientation.IN);
				break;

			default:
				port.setDirection(PortOrientation.IN);
				break;
			}

		}else if((row == 5) && (port.getDirection().equals(PortOrientation.OUT))){

			if (value.startsWith("Add ")){

				NameSpace ns = port.getInternalOwner();

				if ((ns != null) && (ns instanceof Classifier)){
					for( Port pi : ((Classifier) ns).getInternalStructure(Port.class)){
						if (pi.getDirection().equals(PortOrientation.IN)
								&& (value.equals("Add " + ModelsUtils.getQualifiedName(pi)))){
							try {
								INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel().createDependency(port, pi, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DEPENDS);
							} catch (ExtensionNotFoundException e) {
								INTOCPSModule.logService.error(e);
							}
						}
					}
				}


			}else if (value.startsWith("Remove ")){

				Dependency toDelete = null;

				//List of depending Port
				for(Dependency depends : port.getDependsOnDependency()){
					if (depends.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DEPENDS)){
						if (value.equals("Remove " + ModelsUtils.getQualifiedName(depends.getDependsOn()))){
							toDelete = depends;
							break;
						};
					}
				}

				if (toDelete != null){
					toDelete.delete();
				}

			}
		}
	}

	@Override
	public int getRow(ModelElement element) {
		if (((Port)element).getDirection().equals(PortOrientation.OUT)){
			return 5;
		}else{
			return 4;
		}
	}


}
