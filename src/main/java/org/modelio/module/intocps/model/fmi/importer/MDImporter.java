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
package org.modelio.module.intocps.model.fmi.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.IModelioServices;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.diagram.layouter.DiagramLayouter;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.enumeration.ImportType;
import org.modelio.module.intocps.model.fmi.Fmi2Causality;
import org.modelio.module.intocps.model.fmi.Fmi2ScalarVariable;
import org.modelio.module.intocps.model.fmi.Fmi2VariableDependency;
import org.modelio.module.intocps.model.fmi.Fmi2VariableDependency.Unknown;
import org.modelio.module.intocps.model.fmi.FmiModelDescription;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.ModelStructure;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.ModelVariables;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.modelermodule.api.IModelerModuleNoteTypes;
import org.modelio.module.modelermodule.api.IModelerModulePeerModule;
import org.modelio.module.modelermodule.api.IModelerModuleStereotypes;
import org.modelio.module.sysml.api.ISysMLPeerModule;
import org.modelio.module.sysml.api.SysMLStereotypes;
import org.modelio.module.sysml.utils.ModelUtils;


/**
 * This class handles ModelDescription import
 * @author ebrosse
 *
 */
public class MDImporter {

    private IModelingSession modelingSession = INTOCPSModule.getInstance().getModuleContext().getModelingSession();

    private IUmlModel factory = this.modelingSession.getModel();

    private List<Fmi2ScalarVariable> outputs = new ArrayList<>();

    private Map<Fmi2ScalarVariable, ModelElement> svs = new HashMap<>();

    private Class fmi = null;

    private Interface inputInt = null;

    private Interface outputInt = null;


    /**
     * Method ModelDescription
     * @author ebrosse
     */

    public MDImporter() {
    }


    public void importMD(Package owner, File file, ImportType typeImport){

        FmiModelDescription md = null;

        try {
            md = loadModelDescription(file);

            try{
                if (typeImport.equals(ImportType.Structured)){
                    importStructuredModelDescription(owner, md);
                }else{
                    importFlattedModelDescription(owner, md);
                }
            } catch (ExtensionNotFoundException e) {
                INTOCPSModule.logService.error(e);
            }

            ModelStructure ms = md.getModelStructure();
            if (ms != null){
                Fmi2VariableDependency vd = ms.getOutputs();
                List<Unknown> unknows = vd.getUnknown();
                for (Unknown unknow : unknows){
                    List<Fmi2ScalarVariable> scvs = md.getModelVariables().getScalarVariable();
                    try{
                        ModelElement destination = this.svs.get(scvs.get(((int) unknow.getIndex()) - 1 ));
                        IUmlModel model = INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel();

                        for (long input : unknow.getDependencies() ){
                            ModelElement source = this.svs.get(scvs.get(((int)input) - 1 ));
                            try {
                                model.createDependency(source, destination, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DEPENDS);
                            } catch (ExtensionNotFoundException e) {
                                INTOCPSModule.logService.error(e);
                            }
                        }
                    }catch (IndexOutOfBoundsException e){
                        INTOCPSModule.logService.error(e);
                    }
                }
            }
        } catch (JAXBException e) {
            INTOCPSModule.logService.error(e);
        }

    }

    private FmiModelDescription loadModelDescription(File file)
            throws JAXBException {
        FmiModelDescription md;
        JAXBContext jaxbContext = JAXBContext.newInstance(FmiModelDescription.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        md = (FmiModelDescription) jaxbUnmarshaller.unmarshal(file);
        return md;
    }

    private void importStructuredModelDescription(Package owner, FmiModelDescription md)
            throws ExtensionNotFoundException {

        createStructuredBlock(owner, md);

        Stereotype sterArchi = this.modelingSession.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.ARCHITECTUREDIAGRAM, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getMetamodelService().getMetamodel().getMClass(ClassDiagram.class));

        String name = md.getModelName() +  " architecture diagram";

        try( ITransaction transaction = this.modelingSession.createTransaction(I18nMessageService.getString ("Info.Session.Create","Architecture Diagram"))){

            ClassDiagram diagram = this.factory.createClassDiagram(name, owner ,sterArchi);

            this.factory.createDependency(this.fmi, diagram, IModelerModulePeerModule.MODULE_NAME, IModelerModuleStereotypes.RELATED_DIAGRAM);
            this.modelingSession.getModel().getDefaultNameService().setDefaultName(diagram, name);

            DiagramLayouter diagLayout = new DiagramLayouter(diagram, this.inputInt, this.outputInt, this.fmi);
            diagLayout.layouting();

            transaction.commit();

        } catch (Exception e) {
            INTOCPSModule.logService.error(e);
        }

    }

    private void importFlattedModelDescription(Package owner, FmiModelDescription md)
            throws ExtensionNotFoundException {

        Class FMI = createFlattedBlock(owner, md);

        IModelioServices modelioServices = INTOCPSModule.getInstance().getModuleContext().getModelioServices();
        Stereotype sterArchi = this.modelingSession.getMetamodelExtensions().getStereotype(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.ARCHITECTUREDIAGRAM, modelioServices.getMetamodelService().getMetamodel().getMClass(ClassDiagram.class));

        String name = md.getModelName() + " architecture diagram";
        try( ITransaction transaction = this.modelingSession.createTransaction(I18nMessageService.getString ("Info.Session.Create","Architecture Diagram"))){

            ClassDiagram diagram = this.modelingSession.getModel().createClassDiagram(name, owner, sterArchi);
            this.modelingSession.getModel().getDefaultNameService().setDefaultName(diagram, name);

            if (diagram != null) {
                IDiagramService ds = modelioServices.getDiagramService();
                try(IDiagramHandle handler = ds.getDiagramHandle(diagram)){
                    IDiagramDG dg = handler.getDiagramNode();
                    IDiagramNode dn = null;

                    for (IStyleHandle style : ds.listStyles()){
                        if (style.getName().equals("intocps")){
                            dg.setStyle(style);
                            break;
                        }
                    }

                    //FMI
                    List<IDiagramGraphic> dgs = handler.unmask(FMI, 0, 0);
                    for (IDiagramGraphic dg2 : dgs) {
                        if (dg2 instanceof IDiagramNode){
                            dn = ((IDiagramNode) dg2);
                            dn.setBounds(new Rectangle(100, 100, 300, 250));
                        }
                    }

                    for(Attribute att : FMI.getOwnedAttribute()){
                        handler.unmask(att, 0, 0);
                    }

                    int heightIn = 150;
                    int heightOut = 150;

                    for(BindableInstance port : FMI.getInternalStructure()){
                        if (port instanceof Port){
                            Port flowPort = (Port) port;
                            if (flowPort.getDirection().equals(PortOrientation.IN)){
                                handler.unmask(flowPort, 400, heightIn);
                                heightIn += 20;
                            }else{
                                handler.unmask(flowPort, 100, heightOut);
                                heightOut += 20;
                            }
                        }

                    }

                    if (dn != null)
                        dn.fitToContent();

                    handler.save();
                    handler.close();
                }

                modelioServices.getEditionService().openEditor(diagram);
            }

            transaction.commit ();
        } catch (Exception e) {
            INTOCPSModule.logService.error(e);
        }

    }


    private void createVariable(Classifier owner, Fmi2ScalarVariable var)
            throws ExtensionNotFoundException {
        Attribute att = this.factory.createAttribute();
        att.setName(var.getName());

        //Initial
        String initial = var.getInitial();
        if (initial != null){
            att.setValue(initial);
        }

        att.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.VARIABLE);

        if (var.getInteger() != null){
            att.setType(ModelsUtils.getInt());
        }else if (var.getReal() != null){
            att.setType(ModelsUtils.getReal());
        }else if (var.getBoolean() != null){
            att.setType(ModelsUtils.getBoolean());
        }else if (var.getString() != null){
            att.setType(ModelsUtils.getString());
        }
        att.setOwner(owner);

        if (var.getDescription() != null)
            this.factory.createNote(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION, att, var.getDescription());

        this.svs.put(var, att);
    }


    private Class createFlattedBlock(Package owner, FmiModelDescription md) throws ExtensionNotFoundException{
        Class FMI = this.factory.createClass(md.getModelName(), owner, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.SUBSYSTEM);
        FMI.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ECOMPONENT);

        ModelVariables mv = md.getModelVariables();

        ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.ECOMPONENT_GUID, md.getGuid(), FMI);

        for (Fmi2ScalarVariable var : mv.getScalarVariable()){

            if (var.getCausality().equals(Fmi2Causality.input.toString())){

                createPort(FMI, var, PortOrientation.IN);

            }else if (var.getCausality().equals(Fmi2Causality.output.toString())){

                createPort(FMI, var, PortOrientation.OUT);

            }else {
                createVariable(FMI, var);
            }
        }

        return FMI;
    }

    private void createInInterface(Package owner) throws ExtensionNotFoundException{

        Port inputPort = this.factory.createPort();
        inputPort.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
        inputPort.setDirection(PortOrientation.IN);
        inputPort.setInternalOwner(this.fmi);
        inputPort.setName("input");

        this.inputInt = this.factory.createInterface();
        inputPort.setBase(this.inputInt);
        this.inputInt.setOwner(owner);
        this.inputInt.setName("Input");
        inputPort.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
        this.inputInt.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.STRTTYPE);
    }

    private void createOutInterface(Package owner) throws ExtensionNotFoundException{

        Port outputPort = this.factory.createPort();
        outputPort.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
        outputPort.setDirection(PortOrientation.OUT);
        outputPort.setInternalOwner(this.fmi);

        this.outputInt = this.factory.createInterface();
        this.outputInt.setOwner(owner);
        this.outputInt.setName("Output");
        outputPort.setBase(this.outputInt);
        outputPort.setName("output");
        outputPort.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
        this.outputInt.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.STRTTYPE);
    }


    private void createVariables(FmiModelDescription md) throws ExtensionNotFoundException {

        ModelVariables mv = md.getModelVariables();

        for (Fmi2ScalarVariable var : mv.getScalarVariable()){

            if (var.getCausality().equals(Fmi2Causality.input.toString())){

                createVariable(this.inputInt, var);
            }else if (var.getCausality().equals(Fmi2Causality.output.toString())){
                createVariable(this.outputInt, var);
            }else {
                createVariable(this.fmi, var);
            }
        }
    }

    private void createStructuredBlock(Package owner, FmiModelDescription md) throws ExtensionNotFoundException{

        this.fmi = this.factory.createClass(md.getModelName(), owner, ISysMLPeerModule.MODULE_NAME, SysMLStereotypes.BLOCK);
        ModelUtils.addValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.ECOMPONENT_GUID, md.getGuid(), this.fmi);

        createInInterface(owner);
        createOutInterface(owner);
        createVariables(md);

    }

    private void createPort(Class FMI, Fmi2ScalarVariable var, PortOrientation direction)
            throws ExtensionNotFoundException {

        Port port = this.factory.createPort(var.getName(), FMI);
        port.addStereotype(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT);
        //Initial
        String initial = var.getInitial();
        if (initial != null){
            port.setValue(initial);
        }

        if (var.getInteger() != null){
            port.setBase(ModelsUtils.getInt());
        }else if (var.getReal() != null){
            port.setBase(ModelsUtils.getReal());
        }else if (var.getBoolean() != null){
            port.setBase(ModelsUtils.getBoolean());
        }else if (var.getString() != null){
            port.setBase(ModelsUtils.getString());
        }

        port.setDirection(direction);

        if (direction.equals(PortOrientation.OUT)){
            this.outputs.add(var);
        }

        if (var.getDescription() != null)
            this.factory.createNote(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION, port, var.getDescription());

        this.svs.put(var, port);

    }

}
