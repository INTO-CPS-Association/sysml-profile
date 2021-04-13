package org.modelio.module.intocps.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

public class ModelsUtils {
          
	/**
	 * Method setTaggedValue
	 * @author ebrosse
	 * @param name
	 * @param elt
	 * @param value @return
	 */
	@objid ("00edfeee-e32b-46dc-8a3b-f789f9577b9c")
	public static void setTaggedValue(String name, ModelElement elt, String value) {
		List<TaggedValue> tagElements = elt.getTag();
		IUmlModel model = INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel();

		if (!tagElements.isEmpty()) {

			for (TaggedValue tag : tagElements) {
				String tagname = tag.getDefinition().getName();
				if (tagname.equals(name)) {

					TagParameter firstElt = null;
					List<TagParameter> actuals = tag.getActual();
					if ((actuals != null) && (actuals.size() > 0)) {
						firstElt = actuals.get(0);
					} else {
						firstElt = model.createTagParameter();
						tag.getActual().add(firstElt);
					}

					if (((value.equals("false")) && (tag.getDefinition().getParamNumber().equals("0")))
							|| ((value.equals("")) && (tag.getDefinition().getParamNumber().equals("1")))) {
						tag.delete();
					} else {
						firstElt.setValue(value);
					}
				}
			}
		}
	}

	/**
	 * Method addValue
	 * @author ebrosse
	 * @param name
	 * @param values
	 * @param MObject
	 * @return
	 */
	@objid ("96317883-3ae2-4c9a-85f2-5468f41a9106")
	public static void addValue(String modulename, String name, String values, ModelElement element) {
		// DON'T place Transition HERE
		boolean exist = false;
		List<TaggedValue> tagElements = element.getTag();
		TaggedValue tvFound = null;

		// existing verification
		if (!tagElements.isEmpty()) {
			for (TaggedValue tag : tagElements) {

				TagType type = tag.getDefinition();
				String tagname = type.getName();

				if (tagname.equals(name)) {
					exist = true;
					// Modelio.out.println("tvFound FOUND");
					tvFound = tag;
				}
			}
		}

		// if the tagged value doesn't exist yet, we create this
		if (!exist) {
			try {
				// Modelio.out.println("tvFound does not exist");
				TaggedValue v = INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel().createTaggedValue(modulename, name, element);
				element.getTag().add(v);
				if (!v.getDefinition().getParamNumber().equals("0")) {
					setTaggedValue(name, element, values);
				}
			} catch (Exception e) {
				INTOCPSModule.logService.error(e);
			}
		}
		// if the tagged value already exists
		else {
			if ((tvFound != null ) && (tvFound.getDefinition().getParamNumber().equals("0"))) {
				// Modelio.out.println("tvFound.getDefinition().getParamNumber().equals(0), the tv is deleted");
				tvFound.delete();
			} else {
				setTaggedValue(name, element, values);
			}
		}
	}


    /**
     * Allows the element searching by extended class and stereotype
     * @param extendedClass
     * @param stereotype
     * @return a ArrayList<MObject> of ModelElement with the found elements
     */

    public static <T extends ModelElement> ArrayList<T> searchElementStereotyped(final Class<T> extendedClass, final String moduleName, final String stereotype) {
        //initialize the result
        ArrayList<T> result = new ArrayList<>();

        // dynamic elements List<MObject> creating
        Collection<T> listElements = INTOCPSModule.getInstance().getModuleContext().getModelingSession().findByClass(extendedClass);

        // vector initialization
        for (T elt : listElements) {

            if (elt.isStereotyped(moduleName, stereotype)) {
                result.add(elt);
            }

        }
        return result;
    }

    /**
     * @param element
     * @return
     */
    public static List<Stereotype> computePropertyList(final ModelElement element, final String moduleName) {
        List<Stereotype> result = new ArrayList<>();
        int i = 0;

        for (Stereotype ster : element.getExtension()) {
            if (ster.getOwner().getOwnerModule().getName().equals(moduleName)) {
                if (!(result.contains(ster))) {
                    result.add(ster);

                    Stereotype parent = ster.getParent();
                    while ((parent != null) && (!(result.contains(parent)))) {
                        result.add(i, parent);
                        ster = parent;
                        parent = ster.getParent();
                    }
                    i = result.size();
                }

            }
        }
        return result;
    }


   public static NameSpace getNameSpaceOwner(Port source){

	   MObject owner = source.getCluster().getCompositionOwner();
	   while (!(owner instanceof NameSpace)){
		   owner = owner.getCompositionOwner();
	   }

	   return (NameSpace) owner;
   }


    public static ArrayList<DataType> getPType() {
        //initialize the result
        ArrayList<DataType> result = new ArrayList<>();

        // dynamic elements List<MObject> creating
        org.modelio.metamodel.uml.statik.Package pTypeLibraryRoot = INTOCPSModule.getInstance().getModuleContext().getModelingSession().findElementById(org.modelio.metamodel.uml.statik.Package.class, "7f5fdc1e-52f7-49ce-be8f-4bf7d851e69d");

        // vector initialization
        for (ModelElement elt : pTypeLibraryRoot.getOwnedElement()) {

            if (elt instanceof DataType){
                result.add((DataType)elt);
            }

        }
        return result;
    }

    public static ArrayList<GeneralClass> getValueTypes() {
        //initialize the result
        ArrayList<GeneralClass> result = new ArrayList<>();

        result.addAll(getPType());
        result.addAll(searchElementStereotyped(Interface.class, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.STRTTYPE));
        result.addAll(searchElementStereotyped(DataType.class, IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.DTYPE));
        result.addAll(INTOCPSModule.getInstance().getModuleContext().getModelingSession().findByClass(Enumeration.class));

        return result;
    }

    public static DataType getReal(){
    	return INTOCPSModule.getInstance().getModuleContext().getModelingSession().findElementById(DataType.class,"cf54ada1-c5bf-4eaf-9ef5-a2fc8c7edcad");
    }

    public static DataType getBoolean(){
    	return INTOCPSModule.getInstance().getModuleContext().getModelingSession().findElementById(DataType.class, "adb8de67-7232-462f-b36c-c45585a37a74");
    }

    public static DataType getString(){
    	return INTOCPSModule.getInstance().getModuleContext().getModelingSession().findElementById(DataType.class,"6ba90c81-39b9-4015-8c47-1ab27cf9d19d");
    }


    public static DataType getInt(){
    	return INTOCPSModule.getInstance().getModuleContext().getModelingSession().findElementById(DataType.class,"723773db-22ed-4325-bf11-83e380290b30");
    }

    /**
     * Returns the "Qualified" name
     * @param elt : the element
     * @return String : the marte name of the this.element.
     */

    public static String getQualifiedName(final ModelElement elt) {
        String result = elt.getName();
        MObject owner = elt.getCompositionOwner();
        if (owner instanceof ModelElement){
            result = ((ModelElement) owner).getName() + "::" + result;
        }

        return result;
    }

    /**
     * Allows a string tab creating. The string tab element has this form : parentName::elementName
     * @param listElement
     * @return a string tab
     */

    public static String[] createListString(final List<? extends ModelElement> listElement) {
        List<String> listEltName = new ArrayList<>();
        listEltName.add("");

        for (ModelElement elt: listElement) {
            listEltName.add(getQualifiedName(elt));
        }

        Collections.sort(listEltName);
        String[] result = listEltName.toArray(new String[listEltName.size()]);
        return result;
    }

}
