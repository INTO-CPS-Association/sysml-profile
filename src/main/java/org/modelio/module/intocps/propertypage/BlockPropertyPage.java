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
import org.modelio.module.intocps.model.enumeration.BlockKind;
import org.modelio.module.intocps.model.enumeration.CComponentKind;
import org.modelio.module.intocps.model.enumeration.EComponentKind;
import org.modelio.module.intocps.model.enumeration.POComponentKind;
import org.modelio.module.sysml.api.SysMLStereotypes;
import org.modelio.module.sysml.impl.SysMLPeerModule;
import org.modelio.module.sysml.utils.ModelUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class BlockPropertyPage implements IPropertyContent{

	/**
	 * Default constructor
	 */
	public BlockPropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		BlockKind[] bkind = BlockKind.values();
		String[] names = new String[bkind.length];

		for (int i = 0; i < bkind.length; i++) {
			names[i] = bkind[i].name();
		}

		String value_kind ="";

		if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SYSTEM))
			value_kind = "System";
		else if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CCOMPONENT))
			value_kind = "CComponent";
		else if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT))
			value_kind = "EComponent";
		else if (element.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.POCOMPONENT))
			value_kind = "POComponent";

		table.addProperty("BlockKind", value_kind, names);
	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){

			element.removeStereotypes(SysMLPeerModule.MODULE_NAME, SysMLStereotypes.BLOCK);
			element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SYSTEM);
			element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT);
			element.removeStereotypes(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.POCOMPONENT);

			try {

				String compKind = ModelUtils.getTaggedValue(INTOCPSTagTypes.COMPONENT_KIND, element);

				switch (BlockKind.valueOf(value)) {
				case System:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SYSTEM);
					ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, " ", element);
					break;

				case CComponent:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CCOMPONENT);
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPOSITION);
					ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, CComponentKind.Composition.name(), element);
					break;

				case EComponent:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT);
					if (!((compKind.equals(EComponentKind.Cyber.name())
							|| (compKind.equals(EComponentKind.Environment.name()))
							|| (compKind.equals(EComponentKind.SubSystem.name()))
							|| (compKind.equals(EComponentKind.Visualisation.name()))
							|| (compKind.equals(EComponentKind.Physical.name())))))
						ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, EComponentKind.Cyber.name(), element);
					break;

				case POComponent:
					element.addStereotype(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.POCOMPONENT);
					if (!((compKind.equals(POComponentKind.Cyber.name()) || (compKind.equals(POComponentKind.Physical.name())))))
						ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, POComponentKind.Cyber.name(), element);
					break;

				default :
					element.addStereotype(SysMLPeerModule.MODULE_NAME, SysMLStereotypes.BLOCK);
					ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.COMPONENT_KIND, " ", element);
					break;
				}

			} catch (ExtensionNotFoundException e) {
				INTOCPSModule.logService.error(e);
			}
		}
	}

	@Override
    public int getRow(ModelElement element) {
		return 1;
	}


}
