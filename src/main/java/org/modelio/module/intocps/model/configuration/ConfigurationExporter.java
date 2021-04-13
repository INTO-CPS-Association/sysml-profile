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
package org.modelio.module.intocps.model.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.modelermodule.api.IModelerModuleNoteTypes;
import org.modelio.module.modelermodule.api.IModelerModulePeerModule;



/**
 * This class handles the ModelDescription export
 * @author ebrosse
 *
 */
public class ConfigurationExporter {

    /**
     * Method ConfigurationExporter constructor
     * @author ebrosse
     */

    public ConfigurationExporter() {
    }


    public void exportConfiguration(ModelElement topElt, File file){

        //Mapping
        CoeConfiguration config = null;
        if (topElt instanceof Instance){
            config = exportConfiguration((Instance) topElt);
        }else if (topElt instanceof Package){
            config = exportConfiguration((org.modelio.metamodel.uml.statik.Package) topElt);
        }


        if (config != null){
            //Object to JSON in file
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(file, config);
            } catch (JsonGenerationException e) {
                INTOCPSModule.logService.error(e);
            } catch (JsonMappingException e) {
                INTOCPSModule.logService.error(e);
            } catch (IOException e) {
                INTOCPSModule.logService.error(e);
            }
        }
    }


    private CoeConfiguration exportConfiguration(Instance topSystem) {

        //Create resulting configuration
        CoeConfiguration result = new CoeConfiguration();

        for (BindableInstance binst : topSystem.getPart()){
            configurationExport(result, binst);
        }

        return result;
    }

    private CoeConfiguration exportConfiguration(org.modelio.metamodel.uml.statik.Package topPackage) {

        //Create resulting configuration
        CoeConfiguration result = new CoeConfiguration();

        for (Instance inst : topPackage.getDeclared()){
            configurationExport(result, inst);
        }

        return result;
    }



    private void configurationExport(CoeConfiguration result,
            Instance bi) {
        List<Link> links = new ArrayList<>();

        //fmu description
        String description = "";
        String noteContent = bi.getNoteContent(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION);

        if (noteContent != null)
            description += noteContent;

        String name = bi.getName();

        result.getFmus().put("{" + name + "}", description);

        for (BindableInstance port : bi.getPart()){
            if (port instanceof Port){
                for (LinkEnd linkEnd : port.getOwnedEnd()){

                    boolean isConnection = true;
                    Instance bindInstance = ((Port)linkEnd.getOpposite().getOwner()).getCluster();
                    if (bindInstance instanceof BindableInstance){
                        isConnection = !(((BindableInstance) bindInstance).getCluster() instanceof BindableInstance);
                    }

                    if (isConnection)
                        links.add(linkEnd.getLink());
                }
            }
        }

        NameSpace base = bi.getBase();

        //Add variables
        if (base instanceof Classifier){
            for (Attribute att : ((Classifier) base).getOwnedAttribute()){
                Float value = null;
                try{
                    value = Float.valueOf(att.getValue());
                }catch (@SuppressWarnings ("unused") Exception e){
                    value = Float.valueOf("0.0");
                }
                result.getParameters().put("{" + name + "}" + "." + bi.getName() + "." + att.getName(), value);
            }
        }


        //Add connections
        for (Link link : links){
            Instance port1 = link.getLinkEnd().get(0).getSource();
            Instance port2 = link.getLinkEnd().get(0).getTarget();

            if ((port1 == null) && (port2 == null)){
                port1 = link.getLinkEnd().get(1).getSource();
                port2 = link.getLinkEnd().get(1).getTarget();
            }
            
            // find source and target
            Port source = null;
            Port target = null;

            if ((port1 instanceof Port) && (port2 instanceof Port)){
                if (((Port) port1).getDirection().equals(PortOrientation.OUT)
                        || ((Port) port2).getDirection().equals(PortOrientation.IN)){
                    source = (Port) port1;
                    target = (Port) port2;
                }else{
                    source = (Port) port2;
                    target = (Port) port1;
                }
            }

            if ((source != null) 
                    && (source.getCluster() != null)
                    && (target != null) 
                    && (target.getCluster() != null)){
                Instance sourceInstance = source.getCluster();
                String sourceConnection = "{" + sourceInstance.getName() + "}" + "." + sourceInstance.getName() + "." + source.getName();

                Instance targetInstance = target.getCluster();
                String targetConnection = "{" + targetInstance.getName() + "}" + "." + targetInstance.getName() + "." + target.getName();

                result.addConnection(sourceConnection, targetConnection);
            }

        }
    }




}
