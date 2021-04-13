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
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class CompositionPropertyPage implements IPropertyContent{

	/**
	 * Default constructor
	 */
	public CompositionPropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		Association association = ((AssociationEnd) element).getAssociation();
		for (AssociationEnd end : association.getEnd()){
			if ((end.getAggregation().equals(AggregationKind.KINDISCOMPOSITION))){
				table.addProperty("Min", end.getMultiplicityMin());
				table.addProperty("Max", end.getMultiplicityMax());
			}
		}

	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){
			Association association = ((AssociationEnd) element).getAssociation();
			for (AssociationEnd end : association.getEnd()){
				if ((end.getAggregation().equals(AggregationKind.KINDISCOMPOSITION))){
					end.setMultiplicityMin(value);
				}
			}
		}else if(row == 2){
			Association association = ((AssociationEnd) element).getAssociation();
			for (AssociationEnd end : association.getEnd()){
				if ((end.getAggregation().equals(AggregationKind.KINDISCOMPOSITION))){
					end.setMultiplicityMax(value);
				}
			}
		}

	}

	@Override
	public int getRow(ModelElement element) {
		return 1;
	}


}
