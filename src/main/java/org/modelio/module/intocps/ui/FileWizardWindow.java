/*
 * Copyright 2013 Modeliosoft
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


package org.modelio.module.intocps.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.ui.composite.NameComposite;
import org.modelio.module.intocps.ui.composite.ValidationBoutonComposite;
import org.modelio.vcore.smkernel.mapi.MObject;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author ebrosse
 */
@objid ("b72a3346-d88d-4771-aa39-4fdd4e429e9e")
public abstract class FileWizardWindow extends Dialog {
    private String title = "";

    @objid ("d5a9b8b0-56e8-4a58-935d-df5e33b03939")
    private String description = "";

    @objid ("6289e8a3-a442-44f0-a004-14f17f8da131")
    private String frametitle = "";

    @objid ("9ec1295c-6e82-4e8b-b489-c886671ce664")
    private String cancelButton = "";

    @objid ("48886e6d-b0c4-477c-b909-07da0b0ac676")
    private String validateButton = "";


    @objid ("f90eb057-f764-43cf-be9a-969b040c3ed4")
    private static boolean cancelation = false;

    @objid ("b0e9b55d-9ff1-4053-91c0-8a9f9c0854b5")
    protected boolean exportWindows = false;

    @objid ("a24a7ce1-79fb-4b9f-84a0-b1746a17e04a")
    protected boolean error = false;

    @objid ("e0ec614a-0d71-430c-aae6-7ce7191b437d")
    protected String path = "";

    @objid ("1774a60a-11b2-4185-a68e-b2864da06768")
    protected ModelElement selectedElt = null;

    @objid ("de78733e-a6be-4963-8536-c094ec211ac6")
    protected Shell shell = null;

    protected NameComposite nameComposite = null;

    @objid ("e3e59f25-e517-452e-9456-14d68ebb39b3")
    private ValidationBoutonComposite validateComposite = null;


    /**
     * @return nothing
     */
    @objid ("f2fcf340-15f9-4a76-af7a-f14e34c1ce52")
    public Object open() {
        setCancellation(false);
        createContents();
        Display display = getParent().getDisplay();
        centerOnPrimaryScreen(display);
        this.shell.open();
        this.shell.layout();

        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return null;
    }

    @objid ("b6131dac-a270-49fb-8205-2584bc34b535")
    protected void createContents() {
        setLabels();

        this.shell = new Shell(getParent(), 67696 | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.TITLE);
        this.shell.setLayout( new FormLayout());
        this.shell.setText(this.frametitle);

        // File chooser composite
        this.nameComposite = new NameComposite(this.shell, SWT.NONE, SWT.OPEN);


        // Validation Composite
        this. validateComposite = new ValidationBoutonComposite(this.shell, SWT.NONE, this.cancelButton, this.validateButton);

        this.validateComposite.getValidationButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	validationAction();
            }
        });


        this.validateComposite.getCancelButton().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setCancellation(true);
                cancelAction();
            }
        });

        final FormData fd_fileChooserComposite = new FormData();
        fd_fileChooserComposite.right = new FormAttachment(100, 0);
        fd_fileChooserComposite.bottom = new FormAttachment(0, 50);
        fd_fileChooserComposite.top = new FormAttachment(0, 0);
        fd_fileChooserComposite.left = new FormAttachment(0, 0);
        this.nameComposite.setLayoutData(fd_fileChooserComposite);

        final FormData fd_validateComposite = new FormData();
        fd_validateComposite.top = new FormAttachment(this.nameComposite, 5);
        fd_validateComposite.bottom = new FormAttachment(100, -5);
        fd_validateComposite.left = new FormAttachment(this.nameComposite, 0, SWT.LEFT);
        fd_validateComposite.right = new FormAttachment(this.nameComposite, 0, SWT.RIGHT);
        this.validateComposite.setLayoutData(fd_validateComposite);

        setDefaultDialog();
        this.shell.pack();
        this.shell.setMinimumSize(new Point(this.shell.getBounds().width, this.shell.getBounds().height));

        this.validateComposite.getValidationButton().setFocus();
    }

    /**
     * set default configuration of the dialog box
     */
    @objid ("f91bd53b-fb9f-442d-a4ee-f110a5f345f4")
    public abstract void setDefaultDialog();

    /**
     * @return the selected element i.e. Package or IModule
     */
    @objid ("ab7abd89-8279-474d-888e-e2ca3f5b84cc")
    public MObject getSelectedElt() {
        return this.selectedElt;
    }

    @objid ("46fe668f-cc7b-43be-8972-983b52e36a5e")
    void cancelAction() {

        if ((this.shell != null) && (!this.shell.isDisposed())){
            this.shell.dispose();
        }
    }


    /**
     * @param cancelButton : the text of the cancel button
     */
    @objid ("43b4eaf1-377f-4d85-a207-b56eed68d8d8")
    public void setCancelButton(final String cancelButton) {
        this.cancelButton = cancelButton;
    }

    /**
     * @param description : the description of the windows
     */
    @objid ("ac22039a-5bd2-4d2d-8781-98ee83febdfa")
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @param frametitle : the title of the windows frame
     */
    @objid ("ae17c648-dd4c-4256-af0a-8a98e5edda04")
    public void setFrametitle(final String frametitle) {
        this.frametitle = frametitle;
    }

    /**
     * @param title : the title of the windows
     */
    @objid ("abff41e0-d9a6-437d-8f47-240b9018a602")
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @param validateButton : the button of validation
     */
    @objid ("f21fbab8-57bb-42ef-8636-ff7785e55831")
    public void setValidateButton(final String validateButton) {
        this.validateButton = validateButton;
    }


    /**
     * Warning user that he does not select a file
     */
    @objid ("e76e8285-f3ca-4f40-ab9b-49334c2bdefd")
    public void selectAFile() {
        MessageBox messageBox = new MessageBox(this.shell, SWT.ICON_WARNING);
        messageBox.setMessage(this.description);
        messageBox.open();
    }

    /**
     * @return true if the process is cancelled by the user
     */
    @objid ("e5238760-0b56-4c38-9ce1-7bff302ecc70")
    public static boolean isCancelation() {
        return FileWizardWindow.cancelation;
    }

    /**
     * @param cancelation : set the cancellation of the process
     */
    @objid ("e0479287-d76d-4290-bfcf-0a11f7e5aa29")
    public static void setCancellation(final boolean cancelation) {
        FileWizardWindow.cancelation = cancelation;
    }

    /**
     * the action i.e. import or export
     */
    @objid ("79b6009f-1feb-49db-be47-510f8237760d")
    public abstract void validationAction();

    /**
     * set button labels
     */
    @objid ("82b11c44-0921-44d6-b8df-74db93e5c7c1")
    public abstract void setLabels();

    /**
     * initialize file path
     */
    @objid ("bedb1c71-f7b7-4e44-869e-c4b16b874eca")
    public abstract void setPath();

    /**
     * launch a dialog box for wrong path
     */
    @objid ("f321402b-4356-4feb-8ca5-d5ccfff8370c")
    public void fileDontExist() {
        MessageBox messageBox = new MessageBox(this.shell, SWT.ICON_WARNING);
        messageBox.setMessage("Specified File do not exist");
        messageBox.open();
    }


    /**
     * @param parent : the shell parent
     * @param style : the swt style
     */
    @objid ("164d1dac-c354-4fe3-815c-089c11e9ab38")
    public FileWizardWindow(final Shell parent, final int style) {
        super(parent != null ? parent : new Shell(Display.getDefault()), style);
    }

    /**
     * @param parent : the shell parent
     */
    @objid ("bd78756e-a049-4d5e-959f-1c5aa3d2512b")
    public FileWizardWindow(final Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
     * @param selectedElt : set the selected element
     */
    @objid ("405af65a-445c-44f6-8e42-56f4efc0d4b6")
    public void setSelectedElt(final ModelElement selectedElt) {
        this.selectedElt = selectedElt;
    }

    @objid ("2cfa948f-4c41-4910-b2c0-4c8c66056485")
    private void centerOnPrimaryScreen(Display display) {
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = this.shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        this.shell.setLocation(x, y);
        this.shell.open();
    }


    /**
     * @param inpath : the initial path
     * @return the same path in a correct form
     */
    @objid ("704c60b8-8f89-44e9-83d9-8e0601e36418")
    public static String checkAndReplaceEndPath(final String inpath) {
        if (inpath.endsWith("\\")) {
            return inpath.substring(0, inpath.lastIndexOf("\\"));
        } else if (inpath.endsWith("/")) {
            return inpath.substring(0, inpath.lastIndexOf("/"));
        }
        return inpath;
    }

    @objid ("bca27331-7279-463e-997c-4a390ad546d9")
    protected void completeBox() {
        this.title = "Complete";

        this.description = "Complete";


        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                customMessageBox(SWT.ICON_INFORMATION);
                FileWizardWindow.this.shell.dispose();
            }
        });
    }

    @objid ("8c469992-8416-428d-849f-0ea9d7787942")
    void customMessageBox(int icon) {
        MessageBox messageBox = new MessageBox(this.shell, icon);
        messageBox.setMessage(this.description);
        messageBox.setText(this.title);
        messageBox.open();
    }



}
