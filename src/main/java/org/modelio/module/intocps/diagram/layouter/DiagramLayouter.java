package org.modelio.module.intocps.diagram.layouter;

import org.modelio.api.modelio.IModelioServices;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.module.intocps.impl.INTOCPSModule;

public class DiagramLayouter {

    private int xmin = 100;

    private final int ymin = 100;

    private final int spaceX = 100;

    private final String styleName = "intocps";

    private Interface inInt = null;

    private Interface outInt = null;

    private Class block = null;

    private StaticDiagram diagram = null;

    public DiagramLayouter(StaticDiagram diagram, Interface inInt, Interface outInt, Class block){
        this.diagram = diagram;
        this.inInt = inInt;
        this.outInt = outInt;
        this.block = block;
    }


    public void layouting(){

        if  (this.diagram != null){ 
            
            IModelioServices modelioServices = INTOCPSModule.getInstance().getModuleContext().getModelioServices();
            IDiagramService ds = modelioServices.getDiagramService();
            
            try(IDiagramHandle handler = ds.getDiagramHandle(this.diagram)){
                    IDiagramDG dg = handler.getDiagramNode();
            IDiagramNode dn = null;

            for (IStyleHandle style : ds.listStyles()){
                if (style.getName().equals(this.styleName)){
                    dg.setStyle(style);
                    break;
                }
            }

            if (this.inInt != null){
                ClassifierLayouter inIntLayout = new ClassifierLayouter(this.inInt, handler, this.xmin, this.ymin);
                dn = (IDiagramNode) inIntLayout.unmaskElement();
                this.xmin = dn.getBounds().getBottomRight().x();
            }

            if (this.block != null){
                ClassifierLayouter blockLayout = new ClassifierLayouter(this.block, handler, this.xmin + this.spaceX, this.ymin);
                dn = (IDiagramNode) blockLayout.unmaskElement();
                this.xmin = dn.getBounds().getBottomRight().x();
            }


            if (this.outInt != null){
                ClassifierLayouter outIntLayout = new ClassifierLayouter(this.outInt, handler, this.xmin + this.spaceX, this.ymin);
                dn = (IDiagramNode) outIntLayout.unmaskElement();
            }

            handler.save();
            handler.close();
            }

            modelioServices.getEditionService().openEditor(this.diagram);
        }

    }



    public Interface getInInt() {
        return this.inInt;
    }


    public void setInInt(Interface inInt) {
        this.inInt = inInt;
    }


    public Interface getOutInt() {
        return this.outInt;
    }


    public void setOutInt(Interface outInt) {
        this.outInt = outInt;
    }


    public Class getBlock() {
        return this.block;
    }


    public void setBlock(Class block) {
        this.block = block;
    }


    public StaticDiagram getDiag() {
        return this.diagram;
    }


    public void setDiag(StaticDiagram diagram) {
        this.diagram = diagram;
    }

}
