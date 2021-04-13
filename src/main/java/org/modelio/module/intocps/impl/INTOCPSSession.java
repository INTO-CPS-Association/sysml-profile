package org.modelio.module.intocps.impl;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.modelio.api.modelio.mc.IModelComponentDescriptor;
import org.modelio.api.modelio.mc.IModelComponentService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.module.context.configuration.IModuleUserConfiguration;
import org.modelio.api.module.lifecycle.DefaultModuleLifeCycleHandler;
import org.modelio.api.module.lifecycle.ModuleException;
import org.modelio.module.intocps.api.INTOCPSParameters;
import org.modelio.module.intocps.traceability.GitRepository;
import org.modelio.vbasic.version.Version;

/**
 * Implementation of the IModuleSession interface.
 * <br>This default implementation may be inherited by the module developers in order to simplify the code writing of the module session.
 */
public class INTOCPSSession extends DefaultModuleLifeCycleHandler{

	private INTOCPSModelChangeHandler modelChangeHandler = null;

	/**
	 * Constructor.
	 * @param module the Module this session is instanciated for.
	 */
	public INTOCPSSession(INTOCPSModule module) {
		super(module);
	}

	/**
	 * @see org.modelio.api.module.DefaultModuleSession#start()
	 */
	@Override
	public boolean start() throws ModuleException {
		// get the version of the module
		Version moduleVersion = this.module.getVersion();

		// get the Modelio log service
		INTOCPSModule.logService.info("Start of " + this.module.getName() + " " + moduleVersion);
		
		IModelingSession session = this.module.getModuleContext().getModelingSession();
		this.modelChangeHandler = new INTOCPSModelChangeHandler();
		session.addModelHandler(this.modelChangeHandler);

		installStyles();
		installRamc();
		initParameter();
		
		INTOCPSModule.getInstance().setAnalystDeployed();

		super.start();

		return true;

	}

	private void initParameter(){

		IModuleUserConfiguration configuration = this.module.getModuleContext().getConfiguration();
		if (configuration.getParameterValue (INTOCPSParameters.IPADRESSE).isEmpty ()) {
			configuration.setParameterValue (INTOCPSParameters.IPADRESSE, "127.0.0.1");
		}

		if (configuration.getParameterValue (INTOCPSParameters.PORT).isEmpty ()) {
			configuration.setParameterValue (INTOCPSParameters.PORT, "8083");
		}

	}


	/**
	 * @see org.modelio.api.module.DefaultModuleSession#stop()
	 */
	@Override
	public void stop() throws ModuleException {

		this.module.getModuleContext().getModelingSession().removeModelHandler(this.modelChangeHandler);
		
		this.modelChangeHandler = null;

		//traceability
		GitRepository gitRepository = new GitRepository(this.module.getModuleContext());
		gitRepository.commit();

		super.stop();
	}

	/**
	 * @see org.modelio.api.module.DefaultModuleSession#select()
	 */
	@Override
	public boolean select() throws ModuleException {
		IModuleUserConfiguration configuration = this.module.getModuleContext().getConfiguration();
		configuration.setParameterValue (INTOCPSParameters.IPADRESSE, "127.0.0.1");
		configuration.setParameterValue (INTOCPSParameters.PORT, "8083");
		configuration.setParameterValue (INTOCPSParameters.NAME, "");
		configuration.setParameterValue (INTOCPSParameters.EMAIL, "");
		configuration.setParameterValue (INTOCPSParameters.GITREPOSITORYPATH, this.module.getModuleContext().getProjectStructure().getPath().getParent().getParent().toAbsolutePath().toString());
		return super.select();
	}

	/**
	 * @see org.modelio.api.module.DefaultModuleSession#unselect()
	 */
	@Override
	public void unselect() throws ModuleException {
		super.unselect();
	}

	/**
	 * @see org.modelio.api.module.DefaultModuleSession#upgrade(org.modelio.api.modelio.Version, java.util.Map)
	 */
	@Override
	public void upgrade(Version oldVersion, Map<String, String> oldParameters) throws ModuleException {
		super.upgrade(oldVersion, oldParameters);
	}

	private void installStyles() {

		Path mdaplugsPath = this.module.getModuleContext().getConfiguration().getModuleResourcesPath();

		INTOCPSModule.getInstance().getModuleContext().getModelioServices().getDiagramService().registerStyle("intocps", "default", new File(mdaplugsPath.resolve("res" + File.separator + "style" + File.separator + "intocps.style").toString()));
	
	}

	private void installRamc(){
	    
		Path mdaplugsPath = this.module.getModuleContext().getConfiguration().getModuleResourcesPath();

		final IModelComponentService modelComponentService = this.module.getModuleContext().getModelioServices().getModelComponentService();
		for (IModelComponentDescriptor mc :  modelComponentService.getModelComponents()) {
			if (mc.getName().equals("PType")) {
				if (new Version(mc.getVersion()).isOlderThan(new Version("1.2.00"))) {
					modelComponentService.deployModelComponent(
							new File("res" + File.separator + "ramc" + File.separator + "PType_1.2.0.ramc"),
							new NullProgressMonitor());
				} else {
					// Ramc already deployed...
					return;
				}
			}
		}

		//      No ramc found, deploy it
		modelComponentService.deployModelComponent(
				new File(mdaplugsPath.resolve("res" + File.separator + "ramc" + File.separator + "PType_1.2.0.ramc").toString()),
				new NullProgressMonitor());

	}

}
