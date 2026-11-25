package org.modelio.module.intocps.model.fmi.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.api.INTOCPSTagTypes;
import org.modelio.module.intocps.model.enumeration.ModelKind;
import org.modelio.module.intocps.model.enumeration.VariableKind;
import org.modelio.module.intocps.model.fmi.Fmi2Causality;
import org.modelio.module.intocps.model.fmi.Fmi2ScalarVariable;
import org.modelio.module.intocps.model.fmi.Fmi2SimpleType;
import org.modelio.module.intocps.model.fmi.Fmi2Unit;
import org.modelio.module.intocps.model.fmi.Fmi2Variability;
import org.modelio.module.intocps.model.fmi.Fmi2VariableDependency;
import org.modelio.module.intocps.model.fmi.Fmi2VariableDependency.Unknown;
import org.modelio.module.intocps.model.fmi.FmiModelDescription;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.CoSimulation;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.ModelStructure;
import org.modelio.module.intocps.model.fmi.FmiModelDescription.ModelVariables;
import org.modelio.module.intocps.utils.ModelsUtils;
import org.modelio.module.sysml.utils.ModelUtils;

public class FMIFactory {


    private final String toolName = "Modelio";

    private final String fmiVersion = "2.0";

    private final String descNoteType = "description";

    private final String modelerModuleName = "ModelerModule";

    private Fmi2Variability modelKind = Fmi2Variability.discrete;

    private Map<ModelElement, List<Fmi2ScalarVariable>> scalarVariableMap = new HashMap<>();

    private List<Port> outputs = new ArrayList<>();

    private Map<ModelElement, Fmi2Unit> unitMap = new HashMap<>();

    private Map<ModelElement, Fmi2SimpleType> simpleTypeMap = new HashMap<>();

    private int valueRef = 0;


    public FMIFactory(){

    }


    public FmiModelDescription createFMIModelDescription(Class block){

        String modelKindCurrent = ModelUtils.getTaggedValue(INTOCPSTagTypes.ECOMPONENT_MODELTY, block);
        if (modelKindCurrent.equals(ModelKind.Continuous)){
            this.modelKind = Fmi2Variability.continuous;
        }

        FmiModelDescription md = new FmiModelDescription();
        md.setGenerationTool(this.toolName);
        md.setFmiVersion(this.fmiVersion);
        md.setModelName(block.getName());
        md.setGuid(block.getTagValue(IINTOCPSPeerModule.MODULE_NAME, INTOCPSTagTypes.ECOMPONENT_GUID));

        Note note = block.getNote(this.modelerModuleName, this.descNoteType);
        if (note != null)
            md.setDescription(note.getContent());

        createCoSim(block, md);

        createModelVariable(md);

        return md;
    }



    private ModelVariables createModelVariable(FmiModelDescription md) {
        //Create Model variables
        ModelVariables mv = new ModelVariables();
        md.setModelVariables(mv);
        return mv;
    }


    private void createCoSim(Class block, FmiModelDescription md) {
        //Create CoSimulation
        CoSimulation cs = new CoSimulation();
        md.getModelExchangeAndCoSimulation().add(cs);
        cs.setModelIdentifier(block.getName());
    }

    public List<Fmi2ScalarVariable> exportBindableInstance(BindableInstance bi) {
    	List<Fmi2ScalarVariable> result = exportBindableInstance(bi, "");
    	this.scalarVariableMap.put(bi, result);
        return result;
    }

    private List<Fmi2ScalarVariable> exportBindableInstance(BindableInstance bi, String preFix) {

        NameSpace type = bi.getBase();
        if (type instanceof Interface){
            return exportInt((Interface) type, bi.getName(), Fmi2Causality.local);
        }else if (type.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DTYPE)){
            return exportDType(bi, type, preFix, Fmi2Causality.local, bi.getValue());
        }else {
            return exportBase(bi, preFix, type, Fmi2Causality.local, bi.getValue());
        }

    }

    public List<Fmi2ScalarVariable> exportAttribut(Attribute att) {
        return exportAttribut(att, "");
    }


    private List<Fmi2ScalarVariable> exportAttribut(Attribute att, String preFix) {

        String variable_kind = ModelUtils.getTaggedValue(INTOCPSTagTypes.VARIABLE_VARIABLEKIND, att);
        Fmi2Causality causality = Fmi2Causality.local;

        if (variable_kind.equals(VariableKind.parameter.toString())){
            causality = Fmi2Causality.parameter;
        }

        GeneralClass type = att.getType();
        if (type instanceof Interface){
            return exportInt((Interface) type, att.getName(), causality);
        }else if (type.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DTYPE)){
            return exportDType(att, type, preFix, causality, att.getValue());
        }else {
            return exportBase(att, preFix, type, causality, att.getValue());
        }

    }

    /**
     * Sets the FMI type element of a variable and, when {@code withStart} is true, its start value.
     * An empty value gives the type's default start (0, false, empty string).
     */
    private void setPrimitiveType(Fmi2ScalarVariable var,
            NameSpace type, String value, boolean withStart) {

        boolean hasValue = value != null && !value.equals("");

        if (type.equals(ModelsUtils.getReal())){
            Fmi2ScalarVariable.Real real =  new Fmi2ScalarVariable.Real();
            var.setReal(real);
            if (withStart) {
                real.setStart(hasValue ? Double.valueOf(value) : Double.valueOf(0));
            }
        }else if (type.equals(ModelsUtils.getBoolean())){
            Fmi2ScalarVariable.Boolean bool =  new Fmi2ScalarVariable.Boolean();
            var.setBoolean(bool);
            if (withStart) {
                bool.setStart(hasValue ? Boolean.valueOf(value) : Boolean.FALSE);
            }
        }else if (type.equals(ModelsUtils.getString())){
            Fmi2ScalarVariable.String str =  new Fmi2ScalarVariable.String();
            var.setString(str);
            if (withStart) {
                str.setStart(hasValue ? value : "");
            }
        }else if (type.equals(ModelsUtils.getInt())){
            Fmi2ScalarVariable.Integer integer =  new Fmi2ScalarVariable.Integer();
            var.setInteger(integer);
            if (withStart) {
                integer.setStart(hasValue ? Integer.valueOf(value) : Integer.valueOf(0));
            }
        }
    }

    private void setType(Fmi2ScalarVariable var,
            NameSpace type, String value, boolean withStart) {

        if (type instanceof DataType){
            setPrimitiveType(var, type, value, withStart);
        }
    }
    

    public List<Fmi2ScalarVariable> exportPort(Port port) {
    	List<Fmi2ScalarVariable> result = exportPort(port, "");
    	this.scalarVariableMap.put(port, result);
        return result;
    }

    public List<Fmi2ScalarVariable> exportPort(Port port, String preFix) {

        NameSpace type = port.getBase();
        Fmi2Causality causality = Fmi2Causality.input;

        if (port.getDirection().equals(PortOrientation.OUT)){
            causality = Fmi2Causality.output;
            this.outputs.add(port);
        }

        if (type instanceof Interface){
            return exportInt((Interface) type, port.getName(), causality);
        }else if ((type instanceof DataType) && (type.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DTYPE))){
            return exportDType(port, type, preFix, causality, port.getValue());
        }else {
            return exportBase(port, preFix, type, causality, port.getValue());
        }
        
    }

    private List<Fmi2ScalarVariable> exportDType(ModelElement elt, NameSpace type, String preFix, Fmi2Causality causality, String value) {

        EList<Generalization> parents = type.getParent();

        if (parents.size() > 0){
            NameSpace superType = parents.get(0).getSuperType();
            if (superType.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DTYPE)){
               return exportDType(elt, superType, preFix, causality, value);
            }else{
                return exportBase(elt, preFix, superType, causality, value);
            }
        }
        
        return new ArrayList<>();
    }

    private List<Fmi2ScalarVariable> exportInt(Interface inter, String preFix, Fmi2Causality causality) {

        List<Fmi2ScalarVariable> result = new ArrayList<>();

        for (Attribute att : inter.getOwnedAttribute()){

            Fmi2ScalarVariable var = new Fmi2ScalarVariable();
            var.setVariability(this.modelKind.toString());

            var.setName(preFix + "." + att.getName());

            exportDescription(att, var);
            var.setValueReference(this.valueRef);
            this.valueRef++;

            var.setCausality(causality.toString());
        // FMI 2.0: a parameter is fixed or tunable, never continuous or discrete
        if (causality == Fmi2Causality.parameter) {
            var.setVariability("fixed");
        }
            exportDescription(att, var);

            result.add(var);
        }

        EList<Generalization> parents = inter.getParent();

        if (parents.size() > 0){
            NameSpace superType = parents.get(0).getSuperType();
            if (superType instanceof Interface){
                result.addAll(exportInt((Interface) superType, preFix, causality));
            }
        }

        return result;

    }


    private List<Fmi2ScalarVariable> exportBase(ModelElement elt, String preFix, NameSpace base, Fmi2Causality causality, String value) {

        List<Fmi2ScalarVariable> result = new ArrayList<>();

        Fmi2ScalarVariable var = new Fmi2ScalarVariable();
        var.setVariability(this.modelKind.toString());

        String name = elt.getName();

        if (!(name.equals(""))){
            if (preFix.equals("")){
                var.setName(name);
            }else{
                var.setName(preFix + "." + name);
            }
        }

        var.setCausality(causality.toString());
        // FMI 2.0: a parameter is fixed or tunable, never continuous or discrete
        if (causality == Fmi2Causality.parameter) {
            var.setVariability("fixed");
        }

        // FMI 2.0: every variable has its own value reference
        var.setValueReference(this.valueRef);
        this.valueRef++;

        exportDescription(elt, var);

        // FMI 2.0: the start value goes in the type element (Real, Integer, Boolean, String);
        // parameters and inputs must have one, other variables only when a value is given.
        // "initial" is exact, approx or calculated, never the value; it is not allowed for inputs.
        boolean hasValue = value != null && !value.equals("");
        boolean withStart = hasValue || causality == Fmi2Causality.parameter || causality == Fmi2Causality.input;
        if (base != null){
            setType(var, base, value, withStart);
        }
        if (withStart && causality != Fmi2Causality.input) {
            var.setInitial("exact");
        }

        result.add(var);
        return result;

    }

    public void exportDescription(ModelElement md, Fmi2ScalarVariable var) {
        Note note = md.getNote(this.modelerModuleName, this.descNoteType);
        if ((note != null) && (!note.getContent().equals("")))
            var.setDescription(note.getContent());
    }

    public void exportDependencies(FmiModelDescription md) {

        //Dependencies
        ModelStructure ms = new ModelStructure();
        md.setModelStructure(ms);

        Fmi2VariableDependency varDepend = new Fmi2VariableDependency();
        ms.setOutputs(varDepend);
        List<Unknown> unknows = varDepend.getUnknown();

        ModelVariables mv = md.getModelVariables();
        List<Fmi2ScalarVariable> svs = mv.getScalarVariable();

        for (Port output : this.outputs){

            List<Fmi2ScalarVariable> fmiOutputs = this.scalarVariableMap.get(output);

            for (Fmi2ScalarVariable fmiOutput : fmiOutputs){
                Unknown unknow = new Unknown();
                unknow.setIndex(svs.indexOf(fmiOutput) + 1);

                for (Dependency dependency : output.getDependsOnDependency()){
                    if (dependency.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DEPENDS)){
                        ModelElement input = dependency.getDependsOn();
                        List<Fmi2ScalarVariable> fmiInputs = this.scalarVariableMap.get(input);
                        for (Fmi2ScalarVariable fmiInput : fmiInputs){
                            unknow.getDependencies().add(Long.valueOf(svs.indexOf(fmiInput) + 1 ));
                        }
                    }
                }

                unknows.add(unknow);
            }
        }

        // FMI 2.0: <Outputs> must not be empty; leave it out when there are no outputs
        if (unknows.isEmpty()) {
            ms.setOutputs(null);
        }
    }

}
