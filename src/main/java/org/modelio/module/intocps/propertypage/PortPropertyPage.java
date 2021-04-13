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
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.module.intocps.utils.ModelsUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class PortPropertyPage implements IPropertyContent{



	/**
	 * Default constructor
	 */
	public PortPropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		//Types
		Port port = (Port) element;

		ArrayList<Port> portTypes = new ArrayList<>();

		Instance cluster = port.getCluster();

		if (cluster != null){

			NameSpace base = cluster.getBase();

			if ((base != null) && (base instanceof Classifier)){
				for(BindableInstance bi : ((Classifier)base).getInternalStructure()){
					if (bi instanceof Port){
						portTypes.add((Port)bi);
					}
				}
			}

			String[] possibleType = ModelsUtils.createListString(portTypes);

			String value_kind = "";
			if (port.getRepresentedFeature() != null){
				value_kind = ModelsUtils.getQualifiedName(port.getRepresentedFeature());
			}

			table.addProperty("Type", value_kind, possibleType);
		}

		//Direction
		table.addProperty("Direction", port.getDirection().toString());



	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){

			//Types

			Port port = (Port) element;
			Instance cluster = port.getCluster();

			if (cluster != null){
				NameSpace base = cluster.getBase();

				if ((base != null) && (base instanceof Classifier)){
					for(BindableInstance bi : ((Classifier)base).getInternalStructure()){
						if ((bi instanceof Port)
								&& (value.equals(ModelsUtils.getQualifiedName(bi)))){
							port.setRepresentedFeature(bi);
							port.setDirection(((Port)bi).getDirection());
						}
					}
				}
			}

		}
	}

	@Override
	public int getRow(ModelElement element) {
		Instance cluster = ((Port) element).getCluster();

		if (cluster != null){
			return 1;
		}else{
			return 0;
		}
	}


}
