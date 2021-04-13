/*
 * Copyright 2013-2015 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


package org.modelio.module.intocps.report;

import java.util.Set;
import java.util.TreeSet;

import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This class represents the report model of INTOCPS Checker
 * @author ebrosse
 */
@objid ("ad47b435-0cf4-44f1-b559-1cedf8396a26")
public class ReportModel implements IReportWriter {

    private Set<ElementMessage> errors;

    private Set<ElementMessage> warnings;


    private Set<ElementMessage> infos;

    /**
     * This default constructor initializes the lists of message (errors, warning and infos)
     */

    public ReportModel() {
        this.errors = new TreeSet <> ();
        this.warnings = new TreeSet <> ();
        this.infos = new TreeSet <> ();
    }


    @Override
    public void addWarning(final String initialMessage, MObject element, String description) {
        String message = initialMessage;
        if (message == null) {
            message = "";
        }

        this.warnings.add (new ElementMessage (message, element, description));
    }


    @Override
    public void addError(final String initialMessage, MObject element, String description) {
        String message = initialMessage;
        if (message == null) {
            message = "";
        }

        this.errors.add (new ElementMessage (message, element, description));
    }

    /**
     * This method returns the list of error message
     * @return set of error message
     */

    public Set<ElementMessage> getErrors() {
        return this.errors;
    }

    /**
     * This method returns the list of warning message
     * @return set of warning message
     */

    public Set<ElementMessage> getWarnings() {
        return this.warnings;
    }


    @Override
    public boolean isEmpty() {
        return !(hasErrors () || hasWarnings () || hasInfos ());
    }

    /**
     * Adds a warning message at the existing list
     * @param message : the text of the message
     * @param element : the Element
     */

    public void addWarning(final String message, final Element element) {
        this.warnings.add (new ElementMessage (message, element, ""));
    }

    /**
     * Adds an error message at the existing list
     * @param message : the text of the message
     * @param element : the Element
     */

    public void addError(String message, Element element) {
        this.errors.add (new ElementMessage (message, element, ""));
    }


    @Override
    public void addInfo(String message, MObject element, String description) {
        this.infos.add(new ElementMessage (message, element, description));
    }

    /**
     * Adds an info message at the existing list
     * @param message : the text of the message
     * @param element : the Element
     */
    public void addInfo(String message, Element element) {
        this.infos.add (new ElementMessage (message, element, ""));
    }

    /**
     * This method returns the list of info message
     * @return set of info message
     */

    public Set<ElementMessage> getInfos() {
        return this.infos;
    }


    @Override
    public boolean hasErrors() {
        return !this.errors.isEmpty ();
    }


    @Override
    public boolean hasInfos() {
        return !this.infos.isEmpty ();
    }


    @Override
    public boolean hasWarnings() {
        return !this.warnings.isEmpty ();
    }


    class ElementMessage implements Comparable<ElementMessage> {

        public String message;


        public String description;


        public MObject element;


        ElementMessage(final String message, final MObject element, final String description) {
            this.message = message;
            this.element = element;
            this.description = description;
        }


        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ElementMessage other = (ElementMessage) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (this.description == null) {
                if (other.description != null)
                    return false;
            } else if (!this.description.equals(other.description))
                return false;
            if (this.element == null) {
                if (other.element != null)
                    return false;
            } else if (!this.element.equals(other.element))
                return false;
            if (this.message == null) {
                if (other.message != null)
                    return false;
            } else if (!this.message.equals(other.message))
                return false;
            return true;
        }


        @Override
        public int compareTo(final ElementMessage anotherMessage) {
            if (this.element.equals(anotherMessage.element))
                return this.message.compareTo(anotherMessage.message) ;
            else
                return 1;
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
            result = prime * result + ((this.element == null) ? 0 : this.element.hashCode());
            result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
            return result;
        }


        private ReportModel getOuterType() {
            return ReportModel.this;
        }

    }

}
