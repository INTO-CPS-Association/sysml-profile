/**
 * Java Class : ResourcesManager.java
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
 * @category   Util
 * @package    com.modeliosoft.modelio.sysml.utils
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.dse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.impl.INTOCPSPeerModule;
import org.modelio.module.sysml.utils.ModelUtils;
import org.modelio.vcore.smkernel.mapi.MObject;



/**
 * This class handles the ModelDescription export
 * @author ebrosse
 *
 */
public class DSEExporter {


    /**
     * Method ResourcesManager
     * @author ebrosse
     */

    public DSEExporter() {
    }



    public void exportDSE(Class block, File file){

        DSEConfiguration dse = exportAnalysis(block);
        try {

            if (!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, dse);

        } catch (JsonGenerationException e) {
            INTOCPSModule.logService.error(e);
        } catch (JsonMappingException e) {
            INTOCPSModule.logService.error(e);
        } catch (IOException e) {
            INTOCPSModule.logService.error(e);
        }

    }

    private String getQualifiedName(ModelElement elt){
        String result = "test";
        MObject owner = elt.getCompositionOwner();
        if (owner instanceof BindableInstance){

            BindableInstance biOwner = (BindableInstance) owner;
            if (elt instanceof AttributeLink){
                AttributeLink attLink = (AttributeLink) elt;
                result = "{" + biOwner.getBase().getName() + "}." + biOwner.getName() + "." +  attLink.getBase().getName();
            }else{
                result = "{" + biOwner.getBase().getName() + "}." + biOwner.getName() + "." +  elt.getName();
            }
        }
        return result;
    }

    public DSEConfiguration exportAnalysis(Class block) {

        DSEConfiguration config = new DSEConfiguration();

        Algorithm algorithm = new Algorithm();
        config.setAlgorithm(algorithm);

        ObjectiveDefinition objDef = new ObjectiveDefinition();
        config.setObjectiveDefinitions(objDef);

        for (BindableInstance bi : block.getInternalStructure()){
            NameSpace type = bi.getBase();

            if ((type != null) && (type.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.EXTERNALSCRIPT))){
                ExternalScript extScript = new ExternalScript();
                String extFile = ModelUtils.getTaggedValue(INTOCPSTagTypes.EXTERNALSCRIPT_FILE, type);
                extScript.setScriptFile(extFile);

                for (BindableInstance biType : bi.getPart()){
                    String value = ModelUtils.getTaggedValue(INTOCPSTagTypes.SCRIPTPARAMETER_PORT_VALUE, biType);
                    if ((value == null) || (value.equals(""))){
                        for(Dependency dep : biType.getDependsOnDependency()){
                            extScript.addScriptParameters(biType.getName(), getQualifiedName(dep.getDependsOn()));
                        }
                    }else{
                        extScript.addScriptParameters(biType.getName(), value);
                    }

                }
                objDef.addExternalScripts(type.getName(), extScript);
            }
        }

        InternalFunction intFunct = new InternalFunction();
        objDef.setInternalFunctions(intFunct);

        for (BindableInstance att: block.getInternalStructure()){
            for(Dependency dep : att.getDependsOnDependency()){
                NameSpace type = att.getBase();

                if ((type != null)
                        && (type.isStereotyped(INTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DSEPARAMETER))){

                    TagType tagType = INTOCPSModule.getInstance().getModuleContext().getModelingSession().getMetamodelExtensions().getTagType(INTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.PARAMETER_VALUES, type.getMClass());
                    List<String> values = type.getTagValues(tagType);
                    
                    if (values != null){
                        List<Double> paramValues = new ArrayList<>();
                        for (String value : values){
                            paramValues.add(Double.valueOf(value));
                        }
                        config.addParameters(getQualifiedName(dep.getDependsOn()), paramValues);
                    }
                }
            }
        }

        Ranking rank = new Ranking();
        config.setRanking(rank);
        for (BindableInstance bi : block.getInternalStructure()){
            NameSpace type = bi.getBase();
            if (type != null){
                String extFile = ModelUtils.getTaggedValue(INTOCPSTagTypes.EXTERNALSCRIPT_WEIGHT, type);
                if ((extFile != null ) && (!extFile.equals("")))
                    rank.putPareto(type.getName(), "-");
            }

        }

        config.addScenario(ModelUtils.getTaggedValue(INTOCPSTagTypes.DSEANALYSIS_SCENARIOS, block));

        return config;
    }



}
