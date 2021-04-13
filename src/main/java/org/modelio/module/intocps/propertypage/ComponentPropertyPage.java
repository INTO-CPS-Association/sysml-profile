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

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.impl.INTOCPSPeerModule;
import org.modelio.module.intocps.model.enumeration.CComponentKind;
import org.modelio.module.intocps.model.enumeration.EComponentKind;
import org.modelio.module.intocps.model.enumeration.POComponentKind;
import org.modelio.module.sysml.utils.ModelUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class ComponentPropertyPage implements IPropertyContent{

	/**
	 * Default constructor
	 */
	public ComponentPropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		String[] names = null;

		if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT)){

			EComponentKind[] ckind = EComponentKind.values();
			names = new String[ckind.length];
			for (int i = 0; i < ckind.length; i++) {
				names[i] = ckind[i].name();
			}

		}else if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.POCOMPONENT)){

			POComponentKind[] ckind = POComponentKind.values();
			names = new String[ckind.length];
			for (int i = 0; i < ckind.length; i++) {
				names[i] = ckind[i].name();
			}

		}else if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CCOMPONENT)){

			CComponentKind[] ckind = CComponentKind.values();
			names = new String[ckind.length];
			for (int i = 0; i < ckind.length; i++) {
				names[i] = ckind[i].name();
			}
		}

		String value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.COMPONENT_KIND, element);
		table.addProperty("ComponentKind", value_kind, names);


	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){

			try {
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CYBER);
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PHYSICAL);
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ENVIRONMENT);
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VISUALISATION);
				element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);

				switch (EComponentKind.valueOf(value)) {

				case Environment:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ENVIRONMENT);
					break;

				case Visualisation:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VISUALISATION);
					break;

				case SubSystem:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
					break;

				case Cyber:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CYBER);
					break;

				case Physical:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.PHYSICAL);
					break;

				default :
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);
					break;
				}

			} catch (ExtensionNotFoundException e) {
				INTOCPSModule.logService.error(e);
			}

			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, value, element);
		}

	}


	@Override
    public int getRow(ModelElement element) {
		return 1;
	}


}
