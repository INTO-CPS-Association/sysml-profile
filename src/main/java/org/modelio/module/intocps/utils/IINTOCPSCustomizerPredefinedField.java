package org.modelio.module.intocps.utils;

import org.modelio.module.intocps.api.INTOCPSStereotypes;


/**
 * This class lists all predefined value of UML command name.
 * @author ebrosse
 *
 */
public interface IINTOCPSCustomizerPredefinedField {

	static final String Prefix = "INTOCPS";

	public static final String  DType =  Prefix + INTOCPSStereotypes.DTYPE;

	public static final String  StrtType =  Prefix + INTOCPSStereotypes.STRTTYPE;

	public static final String  UMLComposition =  Prefix + "UMLComposition";

	public static final String  UnitType =  Prefix + INTOCPSStereotypes.UNITTYPE;

	public static final String  Variable =  Prefix + INTOCPSStereotypes.VARIABLE;

	public static final  String BlockInstance = Prefix + INTOCPSStereotypes.BLOCKINSTANCE;

	public static final String FlowPort =  Prefix + INTOCPSStereotypes.FLOWPORT;

	public static final String Port =  Prefix + "Port";

	public static final String UMLConnector = Prefix + "UMLConnector";

	public static final String  ExternalScript =  Prefix + INTOCPSStereotypes.EXTERNALSCRIPT;

	public static final String  Parameter =  Prefix + INTOCPSStereotypes.PARAMETER;

	public static final String  DSEParameter =  Prefix + INTOCPSStereotypes.DSEPARAMETER;

	public static final String  DSEAnalysis =  Prefix + INTOCPSStereotypes.DSEANALYSIS;

	public static final String  Reference =  Prefix + INTOCPSStereotypes.REFERENCE;

	public static final String  CComponent =  Prefix + INTOCPSStereotypes.CCOMPONENT;

	public static final String  POComponent =  Prefix + INTOCPSStereotypes.POCOMPONENT;

	public static final String  EComponent =  Prefix + INTOCPSStereotypes.ECOMPONENT;

	public static final String  System =  Prefix + INTOCPSStereotypes.SYSTEM;

}
