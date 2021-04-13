package org.modelio.module.intocps.traceability;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.ObjectDiagram;
import org.modelio.module.intocps.api.IINTOCPSPeerModule;
import org.modelio.module.intocps.api.INTOCPSStereotypes;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.traceability.type.ProvArtefact;
import org.modelio.module.intocps.utils.FileUtils;
import org.modelio.module.intocps.utils.RequirementUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

public class EntityMapper {

    private Map<MObject, ProvArtefact> map = new HashMap<>();

    private static EntityMapper instance = null;


    private EntityMapper(){}


    public static EntityMapper getInstance(){

        if (instance == null){
            instance = new EntityMapper();
        }

        return instance;
    }

    public void clearMap(){
        this.map.clear();
    }

    public ProvArtefact getMappedArtefact(MObject object, File gitRepo){

        if (this.map.get(object) == null ){

            //create artefact
            String filePath = FileUtils.findFilePath(INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString(), object);
            String relativeFilePath = filePath.replace(gitRepo.getParent() + File.separator, "");
            relativeFilePath = relativeFilePath.replace("\\", "/");
            return createArtefact(object, gitRepo, getHash(gitRepo, relativeFilePath));

        }

        return this.map.get(object);
    }

    private String getHash(File gitRepo, String relativeFilePath){

        String hash = "0";

        FileRepositoryBuilder builder = new FileRepositoryBuilder();

        try (Git git = Git.open(gitRepo);
                Repository repository = builder.setGitDir(gitRepo).readEnvironment().findGitDir().build();) {

            String treeName = "refs/heads/master"; // tag or branch
            try {
                for (RevCommit commit : git.log().add(repository.resolve(treeName)).call()) {

                    // and using commit's tree find the path
                    RevTree tree = commit.getTree();

                    // now try to find a specific file
                    try (TreeWalk treeWalk = new TreeWalk(repository)) {
                        treeWalk.addTree(tree);
                        treeWalk.setRecursive(true);
                        treeWalk.setFilter(PathFilter.create(relativeFilePath));
                        if (treeWalk.next()) {
                            hash = commit.toObjectId().getName();					
                            break;
                        }
                    }
                }


            } catch (RevisionSyntaxException | GitAPIException e) {
                INTOCPSModule.logService.error(e);
            }
        } catch (IOException e) {
            INTOCPSModule.logService.error(e);
        }

        return hash;
    }

    public ProvArtefact createArtefact(MObject object, File gitRepo, String hash){

        File projectRoot = gitRepo.getParentFile();
        String rootFilePath = INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString();

        MObject serializedObject = object;

        while (!FileUtils.fileExist(rootFilePath, serializedObject)){
            serializedObject = serializedObject.getCompositionOwner();
        }

        if (!this.map.containsKey(serializedObject)){
            ProvArtefact artefact = new ProvArtefact();

            artefact.setType(getType(serializedObject));
            artefact.setHash(hash);

            String absolutePath = FileUtils.findFilePath(rootFilePath, serializedObject);
            absolutePath = absolutePath.replace(projectRoot.getAbsolutePath(), "");
            absolutePath = absolutePath.replace("\\", "/");
            artefact.setPath(absolutePath);

            this.map.put(object, artefact);
            this.map.put(serializedObject, artefact);
        }

        return this.map.get(serializedObject);
    }


    public ProvArtefact getGeneratedFMI(String relativeFilePath, String hash){

        ProvArtefact artefact = new ProvArtefact();
        artefact.setType(ProvArtefact.Type.MODELDESCRIPTIONFILE);
        artefact.setHash(hash);

        artefact.setPath(relativeFilePath);

        return artefact;
    }

    public ProvArtefact getGeneratedConfiguration(String relatedPath, String hash){

        ProvArtefact artefact = new ProvArtefact();
        artefact.setType(ProvArtefact.Type.SIMULATIONCONFIGURATION);
        artefact.setHash(hash);

        artefact.setPath(relatedPath);

        return artefact;
    }

    public ProvArtefact getGeneratedDSE( String generatedFileRelatedPath, String hash){

        ProvArtefact artefact = new ProvArtefact();
        artefact.setType(ProvArtefact.Type.DSESEARCHCONFIGURATION);
        artefact.setHash(hash);

        artefact.setPath(generatedFileRelatedPath);

        return artefact;
    }


    private ProvArtefact.Type getType(MObject object){

        if ((object instanceof ClassDiagram)
                && (((ClassDiagram) object).isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.ARCHITECTUREDIAGRAM))){
            return ProvArtefact.Type.ARCHITECTURESTRUCTUREDIAGRAM;
        }else if ((object instanceof ObjectDiagram)
                && (((ObjectDiagram) object).isStereotyped(IINTOCPSPeerModule.MODULE_NAME, INTOCPSStereotypes.CONNECTIONDIAGRAM))){
            return ProvArtefact.Type.ARCHITECTURECONNECTIONDIAGRAM;
        }else if (INTOCPSModule.isAnalystDeployed() 
                && RequirementUtils.isRequirement(object)){
            return ProvArtefact.Type.REQUIREMENT;
        }else{
            return ProvArtefact.Type.ARCHITECTUREMODELFILE;
        }
    }
}
