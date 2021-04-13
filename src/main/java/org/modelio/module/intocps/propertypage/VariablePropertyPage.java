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
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.enumeration.VariableKind;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.sysml.utils.ModelUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class VariablePropertyPage implements IPropertyContent{

	ArrayList<GeneralClass> types =  ModelsUtils.getValueTypes();


	/**
	 * Default constructor
	 */
	public VariablePropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		//Types

		Attribute att = (Attribute) element;

		NameSpace base = att.getType();
		String value_kind = "";

		if (base != null){
			value_kind = ModelsUtils.getQualifiedName(base);
		}

		String[] possibleType = ModelsUtils.createListString(this.types);

		table.addProperty("Type", value_kind, possibleType);


		//Init
		value_kind = att.getValue();
		table.addProperty("Init", value_kind);

		//VariableKind
		VariableKind[] variableKind = VariableKind.values();
		String[] names = new String[variableKind.length];

		for (int i = 0; i < variableKind.length; i++) {
			names[i] = variableKind[i].name();
		}

		value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.VARIABLE_VARIABLEKIND, element);

		table.addProperty("VariableKind", value_kind, names);

		//local or parameter
		table.addProperty("IsLocal", ((Attribute) element).isIsDerived());

		//description
		Note descriptionNote = att.getNote("ModelerModule", "description");
		if (descriptionNote == null){
			IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
			try( ITransaction transaction = session.createTransaction(I18nMessageService.getString ("Info.Session.Create","description"))){
				descriptionNote = session.getModel().createNote("ModelerModule", "description", att, "");
				transaction.commit();
			} catch (ExtensionNotFoundException e) {
				INTOCPSModule.logService.error(e);
			}
		}
		table.addProperty("Description", element.getNoteContent("ModelerModule", "description"));

		//Minimum
		value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.VARIABLE_MINIMUMVALUE, element);
		table.addProperty("Minimum", value_kind);

		//Maximum
		value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.VARIABLE_MAXIMUMVALUE, element);
		table.addProperty("Maximum", value_kind);
	}



	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		Attribute att = (Attribute) element;

		if(row == 1){

			for(GeneralClass type : this.types){
				if (value.equals(ModelsUtils.getQualifiedName(type))){
					att.setType(type);
				}
			}

		}else if(row == 2){

			att.setValue(value);

		}else if(row == 3){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.VARIABLE_VARIABLEKIND, value, element);
			element.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VARIABLE);
			element.removeStereotypes(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PARAMETER);

			try {
				if (value.equals(VariableKind.parameter.toString())){
					element.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PARAMETER);
				}else{
					element.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VARIABLE);
				}

			} catch (ExtensionNotFoundException e) {
				INTOCPSModule.logService.error(e);
			}

		}else if(row == 4){
			att.setIsDerived(Boolean.valueOf(value));

		}else if(row == 5){
			Note descriptionNote = att.getNote("ModelerModule", "description");
			descriptionNote.setContent(value);

		}else if(row == 6){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.VARIABLE_MINIMUMVALUE, value, element);

		}else if(row == 7){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.VARIABLE_MAXIMUMVALUE, value, element);
		}
	}

	@Override
	public int getRow(ModelElement element) {
		return 7;
	}


}
