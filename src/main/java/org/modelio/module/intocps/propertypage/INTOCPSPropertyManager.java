package org.modelio.module.intocps.propertypage;

import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.sysml.api.ISysMLPeerModule;
import org.modelio.module.sysml.api.SysMLStereotypes;

/**
 * @author ebrosse
 *
 */
public class INTOCPSPropertyManager {

    /**
     * @param MObject
     *            : the selected MObject
     * @param row
     *            : the row of the property
     * @param value
     *            : the new value of the prpoperty
     * @return the new value of the row
     */
    public int changeProperty(ModelElement element, int row, String value) {

        IPropertyContent propertypage = null;
        int currentRow = row;

        // ModelElement property page
        propertypage = new NamedElementPropertyPage();
        if (element instanceof AssociationEnd){
            propertypage.changeProperty(((AssociationEnd)element).getAssociation(), currentRow, value);
        }else{
            propertypage.changeProperty(element, currentRow, value);
        }

        currentRow = currentRow - propertypage.getRow(element);


        //BlockInstance property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.BLOCKINSTANCE)) {

            propertypage = new BlockInstancePropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        //Block property page
        if (element.isStereotyped(ISysMLPeerModule.MODULE_NAME,	SysMLStereotypes.BLOCK)
                || element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.COMPONENT)
                || element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.SYSTEM)) {

            propertypage = new BlockPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        //Component property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.COMPONENT)) {
            propertypage = new ComponentPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        //EComponent property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.ECOMPONENT)) {
            propertypage = new EComponentPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        //DType property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DTYPE)) {
            propertypage = new DTypePropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        // FlowPort property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.FLOWPORT)) {
            propertypage = new FlowPortPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        //		// ScriptParameter property page
        //		if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
        //				INTOCPSStereotypes.SCRIPTPARAMETER_PORT)) {
        //			propertypage = new ScriptParameterPropertyPage();
        //			propertypage.changeProperty(element, currentRow, value);
        //			currentRow = currentRow - propertypage.getRow(element);
        //		}

        // Parameter property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.PARAMETER)) {
            propertypage = new ParameterPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // DSEAnalysis property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DSEANALYSIS)) {
            propertypage = new DSEAnalysisPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // UnitType property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.UNITTYPE)) {
            propertypage = new UnitTypePropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        // Variable property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.VARIABLE)) {
            propertypage = new VariablePropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // External Script property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.EXTERNALSCRIPT)) {
            propertypage = new DSEExternalScriptPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // Literal Value property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.LITERALVALUE)) {
            propertypage = new LiteralValuePropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // DSE Parameter property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DSEPARAMETER)) {
            propertypage = new DSEParameterPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // Composition property page
        if (element instanceof AssociationEnd) {
            propertypage = new CompositionPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        // Connector property page
        if (element instanceof ConnectorEnd) {
            propertypage = new ConnectorPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }


        // Port property page
        if ((element instanceof Port)
                && !(element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT))){
            propertypage = new PortPropertyPage();
            propertypage.changeProperty(element, currentRow, value);
            currentRow = currentRow - propertypage.getRow(element);
        }

        return currentRow;

    }

    /**
     * build the property table of the selected Elements
     *
     * @param MObject
     *            : the selected element
     * @param table
     *            : the property table
     */
    public void update(ModelElement element, IModulePropertyTable table) {
        
        IPropertyContent propertypage = null;

        // ModelElement property page
        propertypage = new NamedElementPropertyPage();
        if (element instanceof AssociationEnd){
            propertypage.update(((AssociationEnd)element).getAssociation(), table);
        }else{
            propertypage.update(element, table);
        }


        //BlockInstance property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.BLOCKINSTANCE)) {
            propertypage = new BlockInstancePropertyPage();
            propertypage.update(element, table);
        }

        //Block property page
        if (element.isStereotyped(ISysMLPeerModule.MODULE_NAME, SysMLStereotypes.BLOCK)
                || element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.COMPONENT)
                || element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,INTOCPSStereotypes.SYSTEM)) {
            propertypage = new BlockPropertyPage();
            propertypage.update(element, table);
        }

        //Component property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.COMPONENT)) {
            propertypage = new ComponentPropertyPage();
            propertypage.update(element, table);
        }


        //EComponent property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.ECOMPONENT)) {
            propertypage = new EComponentPropertyPage();
            propertypage.update(element, table);
        }

        //DSEANALYSIS property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DSEANALYSIS)) {
            propertypage = new DSEAnalysisPropertyPage();
            propertypage.update(element, table);
        }

        //DSEExternalScript property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.EXTERNALSCRIPT)) {
            propertypage = new DSEExternalScriptPropertyPage();
            propertypage.update(element, table);
        }

        //LiteralValue property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.LITERALVALUE)) {
            propertypage = new LiteralValuePropertyPage();
            propertypage.update(element, table);
        }

        //DType property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DTYPE)) {
            propertypage = new DTypePropertyPage();
            propertypage.update(element, table);
        }


        // FlowPort property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.FLOWPORT)) {
            propertypage = new FlowPortPropertyPage();
            propertypage.update(element, table);
        }

        //		// ScriptParameter property page
        //		if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
        //				INTOCPSStereotypes.SCRIPTPARAMETER_PORT)) {
        //			propertypage = new ScriptParameterPropertyPage();
        //			propertypage.update(element, table);
        //		}

        // Parameter property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.PARAMETER)) {
            propertypage = new ParameterPropertyPage();
            propertypage.update(element, table);
        }

        // UnitType property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.UNITTYPE)) {
            propertypage = new UnitTypePropertyPage();
            propertypage.update(element, table);
        }


        // Variable property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.VARIABLE)) {
            propertypage = new VariablePropertyPage();
            propertypage.update(element, table);
        }

        // DSE Parameter property page
        if (element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME,
                INTOCPSStereotypes.DSEPARAMETER)) {
            propertypage = new DSEParameterPropertyPage();
            propertypage.update(element, table);
        }

        // Composition property page
        if (element instanceof AssociationEnd) {
            propertypage = new CompositionPropertyPage();
            propertypage.update(element, table);
        }

        // Connector property page
        if (element instanceof ConnectorEnd) {
            propertypage = new ConnectorPropertyPage();
            propertypage.update(element, table);
        }

        //Port Property page
        if ((element instanceof Port)
                && !(element.isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.FLOWPORT))){
            propertypage = new PortPropertyPage();
            propertypage.update(element, table);
        }


    }

}
