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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.sysml.utils.ModelUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class UnitTypePropertyPage implements IPropertyContent{

    /**
     * Default constructor
     */
	public UnitTypePropertyPage(){}


    @Override
    public void update(ModelElement element, IModulePropertyTable table) {

    	String value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.UNITTYPE_UNIT, element);
		table.addProperty("Unit", value_kind);

    	value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.UNITTYPE_FACTOR, element);
		table.addProperty("Factor", value_kind);

    	value_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.UNITTYPE_OFFSET, element);
		table.addProperty("Offset", value_kind);

    }

	@Override
	public void changeProperty(ModelElement element, int row, String value) {
		if(row == 1){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.UNITTYPE_UNIT, value, element);
		}else if(row == 2){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.UNITTYPE_FACTOR, value, element);
		}else if(row == 3){
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.UNITTYPE_OFFSET, value, element);
		}

	}

	@Override
	public int getRow(ModelElement element) {
		return 3;
	}


}