package org.modelio.module.intocps.traceability;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.traceability.type.Message;
import org.modelio.module.intocps.traceability.type.ProvActivity;
import org.modelio.module.intocps.traceability.type.ProvAgent;
import org.modelio.module.intocps.traceability.type.ProvArtefact;
import org.modelio.module.intocps.traceability.type.ProvEntity;
import org.modelio.module.intocps.traceability.type.ProvTool;
import org.modelio.module.intocps.traceability.type.ProvUsed;
import org.modelio.module.intocps.traceability.type.ProvWasAssociatedWith;
import org.modelio.module.intocps.traceability.type.RdfRDF;
import org.modelio.module.intocps.utils.ResourcesManager;

/**
 * Created by kel on 03/11/16.
 */
public class TraceDriver
{

    List<String> excludePrefix = null;

    String hostUrl = "";

    private static final String toolName = "Modelio";

    String agentName = "";

    String agentEmail = "";



    public TraceDriver(String hostUrl, String agentName, String agentEmail)
    {
        this.hostUrl = hostUrl;
        this.agentName = agentName;
        this.agentEmail = agentEmail;
    }

    public void syncModelio(File gitRepo, String lastCommitId, Set<ModelElement> elements)
            throws IOException
    {
        internalSyncModelio(gitRepo, lastCommitId, elements);
    }

    private void internalSyncModelio(File gitRepo, String lastCommitId, Set<ModelElement> elements)
            throws IOException
    {
        Message message = initMessage();

        EntityMapper entMapper = EntityMapper.getInstance();
        Set<ProvEntity> entities = message.getRdfRDF().getProvEntity();

        IDiagramService ds = INTOCPSModule.getInstance().getModuleContext().getModelioServices().getDiagramService();

        for (ModelElement element : elements){

            ProvArtefact mappedArtefact = entMapper.createArtefact(element, gitRepo, lastCommitId);
            entities.add(mappedArtefact);
        }

        for (ModelElement element : elements){

            ProvArtefact mappedArtefact = entMapper.getMappedArtefact(element, gitRepo);

            for (Dependency dep : element.getDependsOnDependency()){
                if (dep.isStereotyped("ModelerModule", "verify")){

                    ProvArtefact verifiedElement = entMapper.getMappedArtefact(dep.getDependsOn(), gitRepo);
                    mappedArtefact.addOslcVerifies(verifiedElement);


                }else if (dep.isStereotyped("ModelerModule", "satisfy")){

                    ProvArtefact satisfiedElement = entMapper.getMappedArtefact(dep.getDependsOn(), gitRepo);
                    mappedArtefact.addOslcSatisfies(satisfiedElement);

                }
            }

            if (element instanceof AbstractDiagram){

                try(IDiagramHandle handler = ds.getDiagramHandle((AbstractDiagram) element)){
                    IDiagramDG dg = handler.getDiagramNode();
                    List<IDiagramNode> nodes = dg.getNodes();
                    List<IDiagramNode> subNodes = new ArrayList<>();

                    while (!nodes.isEmpty()){

                        subNodes.clear();

                        //add Node
                        for (IDiagramNode node: nodes){
                            ProvArtefact nodeArt = entMapper.getMappedArtefact(node.getElement(), gitRepo);
                            if (nodeArt != null){
                                mappedArtefact.addProvHadMember(nodeArt);
                            }
                            subNodes.addAll(node.getNodes());
                        }

                        nodes = subNodes;
                    }
                }
            }
        }

        //ProvActivity
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss'Z'");

        String messageDate = ft.format(date);
        ProvActivity activity = new ProvActivity(ProvActivity.Type.ARCHITECTURE_MODELLING, messageDate);
        Set<ProvActivity> activities = new HashSet<>();
        activities.add(activity);
        message.getRdfRDF().setProvActivity(activities);

        //setAgent
        ProvWasAssociatedWith provWasAssociatedWith = new ProvWasAssociatedWith();
        provWasAssociatedWith.setProvAgent((ProvAgent)message.getRdfRDF().getProvAgent().toArray()[0]);
        activity.setProvWasAssociatedWith(provWasAssociatedWith);

        //setEntity
        ProvUsed provUsed = new ProvUsed();
        provUsed.setProvEntity(message.getRdfRDF().getProvEntity());
        activity.setProvUsed(provUsed);

        sendMessage(message);
    }

    private Message initMessage(){

        Message message = new Message();

        RdfRDF rdf = new RdfRDF();
        message.setRdfRDF(rdf);

        ProvAgent agent = new ProvAgent(this.agentEmail, this.agentName);
        Set<ProvAgent> agents = new HashSet<>();
        agents.add(agent);
        rdf.setProvAgent(agents);

        ProvTool tool = new ProvTool(toolName, IINTOCPSPeerModule.MODULE_VERSION, ProvTool.Type.ARCHITECTURE_TOOL);

        Set<ProvEntity> entyties = new HashSet<>();
        entyties.add(tool);
        rdf.setProvEntity(entyties);

        return message;
    }

    public void generateFMI(ProvArtefact mappedBlock, String relativeFilePath, String hash) throws IOException
    {

        Message message = initMessage();

        EntityMapper entMapper = EntityMapper.getInstance();
        Set<ProvEntity> entities = message.getRdfRDF().getProvEntity();

        ProvArtefact mappedFMI = entMapper.getGeneratedFMI(relativeFilePath, hash);
        mappedFMI.addProvWasDerivedFrom(mappedBlock);
        entities.add(mappedFMI);


        //ProvActivity
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss'Z'");

        String messageDate = ft.format(date);
        ProvActivity activity = new ProvActivity(ProvActivity.Type.MODEL_DESCRIPTION_EXPORT, messageDate);
        Set<ProvActivity> activities = new HashSet<>();
        activities.add(activity);
        message.getRdfRDF().setProvActivity(activities);

        //setAgent
        ProvWasAssociatedWith provWasAssociatedWith = new ProvWasAssociatedWith();
        provWasAssociatedWith.setProvAgent((ProvAgent)message.getRdfRDF().getProvAgent().toArray()[0]);
        activity.setProvWasAssociatedWith(provWasAssociatedWith);

        //setEntity
        ProvUsed provUsed = new ProvUsed();
        provUsed.setProvEntity(message.getRdfRDF().getProvEntity());
        activity.setProvUsed(provUsed);

        sendMessage(message);

    }

    private void sendMessage(Message message) throws IOException {

        final JsonNode traceabilitySchema = JsonLoader.fromPath(ResourcesManager.getInstance().getJSONSchema());
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        try {

            JsonSchema schema = factory.getJsonSchema(traceabilitySchema);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(message);
            JsonNode jsonNode = mapper.readTree(json);
            INTOCPSModule.logService.error(json);

            ProcessingReport report = schema.validate(jsonNode);

            if (report.isSuccess()){
                WebClient.post(this.hostUrl + "/traces/push/json", json);
            }
        } catch (Exception e) {
            INTOCPSModule.logService.error(e);
        }
    }

    public void generateCoSimulation(ProvArtefact mappedBlock,String coSimRelatedPath, String hash) throws IOException {

        Message message = initMessage();

        EntityMapper entMapper = EntityMapper.getInstance();
        Set<ProvEntity> entities = message.getRdfRDF().getProvEntity();


        ProvArtefact mappedConfig = entMapper.getGeneratedConfiguration(coSimRelatedPath, hash);
        mappedConfig.addProvWasDerivedFrom(mappedBlock);
        entities.add(mappedConfig);


        //ProvActivity
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss'Z'");

        String messageDate = ft.format(date);
        ProvActivity activity = new ProvActivity(ProvActivity.Type.CONFIGURATION_CREATION, messageDate);
        Set<ProvActivity> activities = new HashSet<>();
        activities.add(activity);
        message.getRdfRDF().setProvActivity(activities);

        //setAgent
        ProvWasAssociatedWith provWasAssociatedWith = new ProvWasAssociatedWith();
        provWasAssociatedWith.setProvAgent((ProvAgent)message.getRdfRDF().getProvAgent().toArray()[0]);
        activity.setProvWasAssociatedWith(provWasAssociatedWith);

        //setEntity
        ProvUsed provUsed = new ProvUsed();
        provUsed.setProvEntity(message.getRdfRDF().getProvEntity());
        activity.setProvUsed(provUsed);

        sendMessage(message);

    }

    public void generateDSE(ProvArtefact mappedBlock, String generatedFileRelatedPath, String hash) throws IOException {

        Message message = initMessage();

        EntityMapper entMapper = EntityMapper.getInstance();
        Set<ProvEntity> entities = message.getRdfRDF().getProvEntity();


        ProvArtefact mappedConfig = entMapper.getGeneratedDSE(generatedFileRelatedPath, hash);
        mappedConfig.addProvWasDerivedFrom(mappedBlock);
        entities.add(mappedConfig);


        //ProvActivity
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss'Z'");

        String messageDate = ft.format(date);
        ProvActivity activity = new ProvActivity(ProvActivity.Type.DSE_CONFIGURATION_CREATION, messageDate);
        Set<ProvActivity> activities = new HashSet<>();
        activities.add(activity);
        message.getRdfRDF().setProvActivity(activities);

        //setAgent
        ProvWasAssociatedWith provWasAssociatedWith = new ProvWasAssociatedWith();
        provWasAssociatedWith.setProvAgent((ProvAgent)message.getRdfRDF().getProvAgent().toArray()[0]);
        activity.setProvWasAssociatedWith(provWasAssociatedWith);

        //setEntity
        ProvUsed provUsed = new ProvUsed();
        provUsed.setProvEntity(message.getRdfRDF().getProvEntity());
        activity.setProvUsed(provUsed);

        sendMessage(message);

    }
}
