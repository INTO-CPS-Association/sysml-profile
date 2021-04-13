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

import java.util.List;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.sysml.api.ISysMLPeerModule;
import org.modelio.module.sysml.api.SysMLStereotypes;

/**
 * This class handles the properties associated to the BlockInstance stereotype
 * @author ebrosse
 *
 */
public class BlockInstancePropertyPage implements IPropertyContent{


	private List<org.modelio.metamodel.uml.statik.Class> classes =  null;


	/**
	 * Default constructor
	 */
	public BlockInstancePropertyPage(){

		this.classes = ModelsUtils.searchElementStereotyped(org.modelio.metamodel.uml.statik.Class.class, ISysMLPeerModule.MODULE_NAME, SysMLStereotypes.BLOCK);
		this.classes.addAll(ModelsUtils.searchElementStereotyped(org.modelio.metamodel.uml.statik.Class.class, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.COMPONENT));
		this.classes.addAll(ModelsUtils.searchElementStereotyped(org.modelio.metamodel.uml.statik.Class.class, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SYSTEM));

	}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		//Type property
		NameSpace base = ((Instance) element).getBase();
		String value_kind = "";

		if (base != null){
			value_kind = ModelsUtils.getQualifiedName(base);
		}

		String[] possibleType = ModelsUtils.createListString(this.classes);

		table.addProperty("Type", value_kind, possibleType);

	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){

			for (ModelElement temp : this.classes){
				if (ModelsUtils.getQualifiedName(temp).equals(value) && (temp instanceof NameSpace)){
					((Instance) element).setBase((NameSpace) temp);
				}
			}

		}

	}





	@Override
	public int getRow(ModelElement element) {
		return 1;
	}



}
