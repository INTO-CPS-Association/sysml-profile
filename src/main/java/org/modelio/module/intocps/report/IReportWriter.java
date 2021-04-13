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

import org.modelio.vcore.smkernel.mapi.MObject;


public interface IReportWriter {

    void addWarning(String Id, MObject element, String description);


    void addError(String Id, MObject element, String description);


    void addInfo(String Id, MObject element, String description);


    boolean isEmpty();


    boolean hasErrors();


    boolean hasWarnings();


    boolean hasInfos();

}
