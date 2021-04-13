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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.module.intocps.impl.INTOCPSModule;

/**
 * This class is the controller of the XMI report windows.
 *
 * It provides all needed services and manages the relations between the XMI report model and the XMI report dialog.
 * @author ebrosse
 */

public class ReportManager {

    private static ReportDialog dialog;

    /**
     * This method opens the XMI report dialog
     * @param report : the report model exposed in report dialog
     */

    public static void showGenerationReport(final Shell shell, final ReportModel report) {
        if (report == null || report.isEmpty ()) {
            if (ReportManager.dialog != null &&
                    !ReportManager.dialog.isDisposed ()) {
                ReportManager.dialog.close ();
            }
        } else {

            // Get the current display
            Display display = Display.getCurrent();

            if (display == null) {
                display = Display.getDefault();
            }

            if (ReportManager.dialog == null ||
                    ReportManager.dialog.isDisposed ()) {
                ReportManager.dialog = new ReportDialog (shell, INTOCPSModule.getInstance().getModuleContext().getModelioServices().getNavigationService());
            }

            ReportManager.dialog.setModel(report);

            if (ReportManager.dialog.open () == SWT.OK) {
                shell.dispose();
            }
        }
    }

    /**
     * This method creates a new Report Model
     * @return the created Report Model
     */

    public static ReportModel getNewReport() {
        return new ReportModel ();
    }

}
