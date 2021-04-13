package org.modelio.module.intocps.traceability;

import java.util.HashSet;
import java.util.Set;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

public class ElementStore {

	private Set<ModelElement> createdElements = new HashSet<>();

	private Set<ModelElement> modifiedElements = new HashSet<>();

	private static ElementStore INSTANCE = null;

	private ElementStore(){
	}

	public static ElementStore getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ElementStore();
		return INSTANCE;
	}

	public Set<ModelElement> getModifiedElements(){
		return this.modifiedElements;
	}

	public Set<ModelElement> getCreatedElements(){
		return this.createdElements;
	}

	public void addElement(ModelElement e){
		this.createdElements.add(e);
	}

	public void updateElement(ModelElement e){
		this.modifiedElements.add(e);
	}

	public Set<ModelElement> getElements(){
		Set<ModelElement> elements = new HashSet<>();
		elements.addAll(this.createdElements);
		elements.addAll(this.modifiedElements);
		return elements;
	}

	public void clearSet(){
		this.createdElements.clear();
		this.modifiedElements.clear();
	}

	public void removeElement(ModelElement elt){
		this.createdElements.remove(elt);
	}

	public boolean isEmpty(){
		return (this.createdElements.isEmpty() && this.modifiedElements.isEmpty());
	}


}
