package org.modelio.module.intocps.diagram.layouter;

import java.util.List;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;

public class ClassifierLayouter implements IElementLayouter{

    private final int topMarging = 100;

    private final int portDiff = 50;

    private Classifier classifier = null;

    private IDiagramHandle handler = null;

    private int xmin = 0;

    private int ymin = 0;


    ClassifierLayouter (Classifier classifier, IDiagramHandle handler){
        this.classifier = classifier;
        this.handler = handler;
    }

    ClassifierLayouter (Classifier classifier, IDiagramHandle handler, int xmin, int ymin){
        this.classifier = classifier;
        this.handler = handler;
        this.xmin = xmin;
        this.ymin = ymin;
    }

    public int getXmin() {
        return this.xmin;
    }

    public void setXmin(int xmin) {
        this.xmin = xmin;
    }

    public int getYmin() {
        return this.ymin;
    }

    public void setYmin(int ymin) {
        this.ymin = ymin;
    }

    @Override
    public IDiagramGraphic unmaskElement(){
        IDiagramNode dn = null;

        //Unmask the classifier
        List<IDiagramGraphic> dgs = this.handler.unmask(this.classifier, this.xmin, this.ymin);
        for (IDiagramGraphic dg2 : dgs) {
            if (dg2 instanceof IDiagramNode){
                dn = ((IDiagramNode) dg2);

            }
        }

        //Unmask attribut
        for(Attribute att : this.classifier.getOwnedAttribute()){
            this.handler.unmask(att, 0, 0);
        }

        if (dn != null){
            //set fit to content
            dn.fitToContent();


            Rectangle bound = dn.getBounds();
            int xmax = bound.getTopRight().x;
            this.ymin += this.topMarging;
            int ymax = this.ymin;

            for(BindableInstance bi : this.classifier.getInternalStructure()){
                if (bi instanceof Port){
                    Port port = (Port) bi;
                    if (port.getDirection().equals(PortOrientation.IN)){
                        this.ymin += this.portDiff;
                        List<IDiagramGraphic> graphics = this.handler.unmask(port, this.xmin,  this.ymin);
                        for(IDiagramGraphic graphic : graphics){
                            if ((graphic.getElement() == null) && (graphic instanceof IDiagramNode)){
                                IDiagramNode node = (IDiagramNode) graphic;
                                node.setBounds(node.getBounds());
                            }
                        }

                    }else {
                        ymax += this.portDiff;
                        List<IDiagramGraphic> graphics = this.handler.unmask(port, xmax + 200, ymax);
                        for(IDiagramGraphic graphic : graphics){
                            if ((graphic.getElement() == null) && (graphic instanceof IDiagramNode)){
                                IDiagramNode node = (IDiagramNode) graphic;
                                node.setBounds(node.getBounds());
                            }
                        }
                    }
                }
            }

            dn.setSize(dn.getBounds().width(), dn.getBounds().height());
        }
        return dn;
    }


}
