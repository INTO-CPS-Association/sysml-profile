package org.modelio.module.intocps.traceability;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.context.configuration.IModuleUserConfiguration;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.intocps.api.INTOCPSParameters;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.traceability.type.ProvArtefact;
import org.modelio.module.intocps.utils.FileUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

public class GitRepository {

	private TraceDriver driver = null;

	private File gitRepo = null;

	
	public GitRepository(IModuleContext context){
	    
	    IModuleUserConfiguration config = context.getConfiguration();

		String hostUrl = "http://" + config.getParameterValue(INTOCPSParameters.IPADRESSE)
				+ ":" + config.getParameterValue(INTOCPSParameters.PORT);

		String agentName = config.getParameterValue(INTOCPSParameters.NAME);

		String agentEmail = config.getParameterValue(INTOCPSParameters.EMAIL);

		this.driver = new TraceDriver(hostUrl, agentName, agentEmail);

		String folder = config.getParameterValue(INTOCPSParameters.GITREPOSITORYPATH);
		if (folder.equals("")){
			folder = context.getProjectStructure().getPath().getParent().getParent().toAbsolutePath().toString();
		}

		this.gitRepo =  new  File(folder +  File.separator + ".git");

	}


	private  boolean isGitRepository(){
		return (this.gitRepo.exists() && this.gitRepo.isDirectory());
	}

	public void configurationCoSimulation(String generatedCoSimulationConfigPath, MObject selectedElt) {

		if (isGitRepository()){
			//commit
			this.commit();

			//trace Model Description generation
			EntityMapper entMapper = EntityMapper.getInstance();
			ProvArtefact selectProvArtefact = entMapper.getMappedArtefact(selectedElt, this.gitRepo);
			this.configurationCoSimulation(generatedCoSimulationConfigPath, selectProvArtefact);
		}

	}

	private void configurationCoSimulation(String generatedCoSimulationConfigPath, ProvArtefact selectedElt) {
		//start by commiting all elements
		this.commit();

		try (Git git = Git.open(this.gitRepo);) {

		

			String relativeFilePath = generatedCoSimulationConfigPath.replace(this.gitRepo.getParent() + File.separator, "");
			relativeFilePath = relativeFilePath.replace("\\", "/");

			git.add().addFilepattern(relativeFilePath).call();

			RevCommit revCommit = git.commit().setMessage("Generate Co simulation configuration").call();

			if (revCommit != null){
			    this.driver.generateCoSimulation(selectedElt, "/" + relativeFilePath, revCommit.getId().getName());
			}
		} catch (IOException | GitAPIException e) {
			INTOCPSModule.logService.error(e);
		}

	}

	public void generateDSE(String generatedDSEPath, MObject selectedElt) {
		if (isGitRepository()){
			//commit
			this.commit();

			//trace Model Description generation
			EntityMapper entMapper = EntityMapper.getInstance();
			ProvArtefact selectProvArtefact = entMapper.getMappedArtefact(selectedElt, this.gitRepo);
			this.generateDSE(generatedDSEPath, selectProvArtefact);
		}
	}


	private void generateDSE(String dataFilePath, ProvArtefact selectedElt) {

		try (Git git = Git.open(this.gitRepo);) {

			String relativeFilePath = dataFilePath.replace(this.gitRepo.getParent() + File.separator, "");
			relativeFilePath = relativeFilePath.replace("\\", "/");

			git.add().addFilepattern(relativeFilePath).call();
			RevCommit revCommit = git.commit().setMessage("Generate DSE configuration").call();

			if (revCommit != null){
			    this.driver.generateDSE(selectedElt, "/" + relativeFilePath, revCommit.getId().getName());
			}
		} catch (IOException | GitAPIException e) {
			INTOCPSModule.logService.error(e);
		}

	}

	public void generateModelDescription(String generatedModelDescriptionPath, MObject selectedElt) {

		if (isGitRepository()){
			//commit all changed or updated elements
			this.commit();

			//trace Model Description generation
			EntityMapper entMapper = EntityMapper.getInstance();
			ProvArtefact selectProvArtefact = entMapper.getMappedArtefact(selectedElt, this.gitRepo);
			this.generateModelDescription(generatedModelDescriptionPath, selectProvArtefact);
		}

	}

	private void generateModelDescription(String fmiFilePath, ProvArtefact selectedElt) {
	    
		try (Git git = Git.open(this.gitRepo);){
			
			String relativeFilePath = fmiFilePath.replace(this.gitRepo.getParent() + File.separator, "");
			relativeFilePath = relativeFilePath.replace("\\", "/");

			git.add().addFilepattern(relativeFilePath).call();

			RevCommit revCommit = git.commit().setMessage("Generate model description").call();

			if (revCommit != null){
			    this.driver.generateFMI(selectedElt,  "/" + relativeFilePath, revCommit.getId().getName());
			}


		} catch (IOException | GitAPIException e) {
			INTOCPSModule.logService.error(e);
		}

	}


	public void commit() {

		if ((isGitRepository())
				&& (!ElementStore.getInstance().isEmpty())){

			try (Git git = Git.open(this.gitRepo);){
				
				//set Path
				File exmlFile = FileUtils.findFile(INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toFile(), INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel().getModelRoots().get(0).getUuid() + ".exml");
				String dataFilePath = exmlFile.getParentFile().getParent();
				String relativeFilePath = dataFilePath.replace(this.gitRepo.getParent() + File.separator, "");
				relativeFilePath = relativeFilePath.replace("\\", "/");

				git.add().addFilepattern(relativeFilePath).call();

				RevCommit revCommit = git.commit().setMessage("Modelio Automatic Commit").call();

				Set<ModelElement> elements = ElementStore.getInstance().getElements();

				if (revCommit != null){
				    this.driver.syncModelio(this.gitRepo , revCommit.getId().getName(), elements);
				}
			} catch (IOException | GitAPIException e) {
				INTOCPSModule.logService.error(e);
			}

			ElementStore.getInstance().clearSet();
		}
	}

	public void pushModel() {

		if (isGitRepository()){

			try (Git git = Git.open(this.gitRepo);){
				
				//set Path
				File exmlFile = FileUtils.findFile(INTOCPSModule.getInstance().getModuleContext().getProjectStructure().getPath().toAbsolutePath().toFile(), INTOCPSModule.getInstance().getModuleContext().getModelingSession().getModel().getModelRoots().get(0).getUuid() + ".exml");
				String dataFilePath = exmlFile.getParentFile().getParent();
				String relativeFilePath = dataFilePath.replace(this.gitRepo.getParent() + File.separator, "");
				relativeFilePath = relativeFilePath.replace("\\", "/");

				git.add().addFilepattern(relativeFilePath).call();

				RevCommit revCommit = git.commit().setMessage("Push all model").call();

				Set<ModelElement> elements = new HashSet<>(INTOCPSModule.getInstance().getModuleContext().getModelingSession().findByClass(ModelElement.class));

				if (revCommit != null){
					this.driver.syncModelio(this.gitRepo , revCommit.getId().getName(), elements);
				}
			} catch (IOException | GitAPIException e) {
				INTOCPSModule.logService.error(e);
			}

			ElementStore.getInstance().clearSet();
		}
	}

}




