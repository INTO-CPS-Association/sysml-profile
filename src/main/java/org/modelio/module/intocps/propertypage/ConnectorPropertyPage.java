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
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.module.intocps.utils.ModelsUtils;

/**
 * This class handles the properties associated to the Allocated stereotype
 * @author ebrosse
 *
 */
public class ConnectorPropertyPage implements IPropertyContent{


    ArrayList<GeneralClass> flowTypes = ModelsUtils.getValueTypes();

    /**
     * Default constructor
     */
    public ConnectorPropertyPage(){}


    @Override
    public void update(ModelElement element, IModulePropertyTable table) {

        Link connector =  ((ConnectorEnd) element).getLink();
        String value_kind ="";

        if (connector != null){
            for (LinkEnd end : connector.getLinkEnd()){
                if (end.getRealizedInformationFlow().size() > 0){
                    InformationFlow informationFlow = end.getRealizedInformationFlow().get(0);
                    if (informationFlow.getConveyed().size() > 0){
                        value_kind =  ModelsUtils.getQualifiedName(informationFlow.getConveyed().get(0));
                    }
                }
            }
        }


        String[] possibleType = ModelsUtils.createListString(this.flowTypes);
        table.addProperty("FlowType", value_kind, possibleType);
    }

    @Override
    public void changeProperty(ModelElement element, int row, String value) {

        if(row == 1){
            Link connector =  ((ConnectorEnd) element).getLink();

            for (LinkEnd end : connector.getLinkEnd()){
                if (end.getRealizedInformationFlow().size() > 0){
                    InformationFlow informationFlow = end.getRealizedInformationFlow().get(0);
                    informationFlow.getConveyed().clear();

                    for (Classifier temp : this.flowTypes){
                        if (ModelsUtils.getQualifiedName(temp).equals(value)){
                            informationFlow.getConveyed().add(temp);
                        }
                    }
                }
            }


        }

    }

    @Override
    public int getRow(ModelElement element) {
        return 1;
    }


}
