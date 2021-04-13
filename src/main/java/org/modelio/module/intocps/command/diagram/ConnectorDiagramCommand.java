/**
 * Java Class : UMLCompositionDiagramCommand.java
 *
 * Description :
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 *
 * @category   Command Diagram
 * @package    com.modeliosoft.modelio.sysml.gui.diagram
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.command.diagram;

import java.util.List;
import org.eclipse.draw2d.geometry.Point;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.InvalidDestinationPointException;
import org.modelio.api.modelio.diagram.InvalidPointsPathException;
import org.modelio.api.modelio.diagram.InvalidSourcePointException;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The diagram command which handles the creation of an UML/SysML Composition
 * @author ebrosse
 */

public class ConnectorDiagramCommand extends DefaultLinkTool {

    private Port firstPort = null;


    @Override
    public boolean acceptFirstElement(IDiagramHandle arg0, IDiagramGraphic arg1) {
        if ((arg1 != null) && (arg1.getElement() != null)){
            MObject element = arg1.getElement();
            if ((element).getStatus().isModifiable ()
                    && (element instanceof Port)){
                this.firstPort = (Port) element;
                return ((this.firstPort.getRepresentedFeature() != null)
                        && (this.firstPort.getRepresentedFeature() instanceof Port)
                        && (!((Port) this.firstPort.getRepresentedFeature()).getDirection().equals(PortOrientation.IN)));

            }
        }
        return false;
    }

    private boolean testPorts(Port secondPort){


        if ((this.firstPort.getDirection().equals(PortOrientation.OUT)
                || this.firstPort.getDirection().equals(PortOrientation.INOUT))
                && (secondPort.getDirection().equals(PortOrientation.IN))){
            return true;
        }

        if ((this.firstPort.getDirection().equals(PortOrientation.INOUT))
                && ((secondPort.getDirection().equals(PortOrientation.INOUT))
                        || (secondPort.getDirection().equals(PortOrientation.IN)))){
            return true;
        }


        if ((this.firstPort.getDirection().equals(PortOrientation.OUT))
                && (secondPort.getDirection().equals(PortOrientation.OUT))
                && this.firstPort.getCompositionOwner().getCompositionOwner().equals(secondPort.getCompositionOwner())){
            return true;
        }

        if ((this.firstPort.getDirection().equals(PortOrientation.IN))
                && (secondPort.getDirection().equals(PortOrientation.IN))
                && this.firstPort.getCompositionOwner().equals(secondPort.getCompositionOwner().getCompositionOwner())){
            return true;
        }

        return false;
    }

    @Override
    public boolean acceptSecondElement(IDiagramHandle arg0, IDiagramGraphic arg1, IDiagramGraphic arg2) {
        if ((arg2 != null)
                && (arg2.getElement() != null)
                && (arg2.getElement() instanceof Port)){
            return testPorts((Port) arg2.getElement());
        }
        return false;
    }

    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic arg1, IDiagramGraphic arg2, LinkRouterKind kind, ILinkPath path) {
        IModelingSession session = INTOCPSModule.getInstance().getModuleContext().getModelingSession();
        IUmlModel model = session.getModel();

        try( ITransaction transaction = session.createTransaction (I18nMessageService.getString ("Info.Session.Create","UMLConnector"))){

            Port source = (Port) arg1.getElement();
            Port target = (Port) arg2.getElement();

            Connector connector = model.createConnector(source, target, "");

            List<IDiagramGraphic> graphics = representation.unmask(connector.getLinkEnd().get(0), 0 , 0);

            // remove first and last point 
            List<Point> points = path.getPoints();
            if ((points.size() > 1)
                && (points.get(0).equals(points.get(1)))
                && (points.get(path.getPoints().size() - 2).equals(points.get(path.getPoints().size() -1)))){
                
                path.removePoint(0);
                path.removePoint(path.getPoints().size() -1 );
            }

            graphics = representation.unmask(connector, 0 , 0);
            for (IDiagramGraphic graphic : graphics){
                if (graphic instanceof IDiagramLink){
                    IDiagramLink link = (IDiagramLink) graphic;
                    link.setRouterKind(kind);
                    link.setPath(path);
                }
            }

            representation.save();
            representation.close();
            transaction.commit();
        } catch (InvalidSourcePointException e) {
            INTOCPSModule.logService.error(e);
        } catch (InvalidPointsPathException e) {
            INTOCPSModule.logService.error(e);
        } catch (InvalidDestinationPointException e) {
            INTOCPSModule.logService.error(e);
        }
    }

}
