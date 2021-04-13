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
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.enumeration.PType;
import org.modelio.module.sysml.utils.ModelUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class DTypePropertyPage implements IPropertyContent{

	/**
	 * Default constructor
	 */
	public DTypePropertyPage(){}


	@Override
	public void update(ModelElement element, IModulePropertyTable table) {

		String superName = "";

		DataType dataType = ((DataType) element);
		if (dataType.getParent().size() > 0)
			superName = dataType.getParent().get(0).getSuperType().getName();

		PType[] ptypes = PType.values();
		String[] names = new String[ptypes.length];

		for (int i = 0; i < ptypes.length; i++) {
			names[i] = ptypes[i].name();
		}

		table.addProperty("Super", superName, names);
	}

	@Override
	public void changeProperty(ModelElement element, int row, String value) {

		if(row == 1){
			DataType dataType = ((DataType) element);

			List<Generalization> superTypes = dataType.getParent();

			for(int i = 0; i<superTypes.size(); i++){
				 superTypes.get(i).delete();
			}

			IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
			IUmlModel model = session.getModel();
			Generalization gen = model.createGeneralization();
			gen.setSubType(dataType);
			ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.DTYPE_SUPER, value, element);

			switch (PType.valueOf(value)) {
			case String:
				gen.setSuperType(session.findElementById(DataType.class, "6ba90c81-39b9-4015-8c47-1ab27cf9d19d"));
				break;

			case Int:
				gen.setSuperType(session.findElementById(DataType.class, "723773db-22ed-4325-bf11-83e380290b30"));
				break;

			case Real:
				gen.setSuperType(session.findElementById(DataType.class, "cf54ada1-c5bf-4eaf-9ef5-a2fc8c7edcad"));
				break;

			case Bool:
				gen.setSuperType(session.findElementById(DataType.class, "adb8de67-7232-462f-b36c-c45585a37a74"));
				break;

			case Nat:
				gen.setSuperType(session.findElementById(DataType.class, "7a569746-3ff6-4bff-898a-d3c9dd2eddbb"));
				break;

			case Interval:
				gen.setSuperType(session.findElementById(DataType.class, "63a107d1-cba2-493b-8c18-86cbd8c41a7b"));
				break;

			default:
				break;
			}

		}

	}

	@Override
	public int getRow(ModelElement element) {
		return 1;
	}


}
