package org.modelio.module.intocps.utils;

import org.modelio.archimate.metamodel.layers.motivation.Requirement;
import org.modelio.module.intocps.traceability.ElementStore;
import org.modelio.vcore.smkernel.mapi.MObject;

public class RequirementUtils {

    
    
    public static void addRequirement(MObject updatedElement){
        
        if (updatedElement instanceof Requirement){
            ElementStore eltStore = ElementStore.getInstance();
            eltStore.addElement((Requirement) updatedElement);
        }

    }
    
    public static boolean isRequirement(MObject updatedElement){
        return (updatedElement instanceof Requirement);
    }
    
}
