package org.modelio.module.intocps.impl;

import java.util.List;
import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.context.configuration.IModuleUserConfiguration;
import org.modelio.api.module.parameter.IParameterEditionModel;
import org.modelio.api.module.parameter.impl.DirectoryParameterModel;
import org.modelio.api.module.parameter.impl.ParameterGroupModel;
import org.modelio.api.module.parameter.impl.ParameterModel;
import org.modelio.api.module.parameter.impl.ParametersEditionModel;
import org.modelio.api.module.parameter.impl.StringParameterModel;
import org.modelio.module.intocps.api.INTOCPSParameters;
import org.modelio.module.intocps.i18n.I18nMessageService;
import org.modelio.module.intocps.utils.ResourcesManager;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of the IModule interface.
 * <br>All Modelio java modules should inherit from this class.
 *
 */
public class INTOCPSModule extends AbstractJavaModule {

    private final String analystName = "Analyst"; 

    private INTOCPSPeerModule peerModule = null;

    private INTOCPSSession session = null;

    public static INTOCPSLogService logService;

    private static INTOCPSModule instance;

    private static boolean isAnalystDeployed = false; 


    @Override
    public INTOCPSPeerModule getPeerModule() {
        return this.peerModule;
    }

    /**
     * Method automatically called just after the creation of the module.
     * <p>
     * <p>
     * The module is automatically instanciated at the beginning of the MDA
     * lifecycle and constructor implementation is not accessible to the module
     * developer.
     * <p>
     * <p>
     * The <code>init</code> method allows the developer to execute the desired initialization code at this step. For
     * example, this is the perfect place to register any IViewpoint this module provides.
     *
     *
     * @see org.modelio.api.module.AbstractJavaModule#init()
     */
    @Override
    public void init() {
        // Add the module initialization code
        ResourcesManager.getInstance().setJMDAC(this);
        super.init();
    }

    /**
     * Method automatically called just before the disposal of the module.
     * <p>
     * <p>
     *
     *
     * The <code>uninit</code> method allows the developer to execute the desired un-initialization code at this step.
     * For example, if IViewpoints have been registered in the {@link #init()} method, this method is the perfect place
     * to remove them.
     * <p>
     * <p>
     *
     * This method should never be called by the developer because it is already invoked by the tool.
     *
     * @see org.modelio.api.module.AbstractJavaModule#uninit()
     */
    @Override
    public void uninit() {
        // Add the module un-initialization code
        super.uninit();
    }

    /**
     * Builds a new module.
     * <p>
     * <p>
     * This constructor must not be called by the user. It is automatically
     * invoked by Modelio when the module is installed, selected or started.
     * @param modelingSession the modeling session this module is deployed into.
     * @param model the model part of this module.
     * @param moduleConfiguration the module configuration, to get and set parameter values from the module itself.
     * @param peerConfiguration the peer module configuration, to get and set parameter values from another module.
     */
    public INTOCPSModule(IModuleContext moduleContext) {
        super(moduleContext);
        this.session = new INTOCPSSession(this);
        this.peerModule = new INTOCPSPeerModule(this, moduleContext.getPeerConfiguration());
        this.peerModule.init();
        INTOCPSModule.logService = new INTOCPSLogService(this.getModuleContext().getLogService(), this);
        instance = this;
    }

    /**
     * @see org.modelio.api.module.AbstractJavaModule#getParametersEditionModel()
     */
    @Override
    public IParameterEditionModel getParametersEditionModel() {
        if (this.parameterEditionModel == null) {

            ParametersEditionModel parameters = new ParametersEditionModel(this);
            this.parameterEditionModel = parameters;
            ParameterModel parameter;

            IModuleUserConfiguration configuration = this.getModuleContext().getConfiguration();

            ParameterGroupModel Group1 = new ParameterGroupModel("Group1","Daemon");
            parameters.addGroup(Group1);
            parameter = new StringParameterModel(configuration, INTOCPSParameters.IPADRESSE, I18nMessageService.getString ("Ui.Parameter.IPAdresse.Label"), I18nMessageService.getString ("Ui.Parameter.IPAdresse.Description"),"");
            Group1.addParameter(parameter);
            parameter = new StringParameterModel(configuration, INTOCPSParameters.PORT, I18nMessageService.getString ("Ui.Parameter.Port.Label"), I18nMessageService.getString ("Ui.Parameter.Port.Description"),"");
            Group1.addParameter(parameter);


            ParameterGroupModel Group2 = new ParameterGroupModel("Group2", "Author");
            parameters.addGroup(Group2);
            parameter = new StringParameterModel(configuration, INTOCPSParameters.NAME, I18nMessageService.getString ("Ui.Parameter.Name.Label"), I18nMessageService.getString ("Ui.Parameter.Name.Description"),"");
            Group2.addParameter(parameter);
            parameter = new StringParameterModel(configuration, INTOCPSParameters.EMAIL, I18nMessageService.getString ("Ui.Parameter.Email.Label"), I18nMessageService.getString ("Ui.Parameter.Email.Description"),"");
            Group2.addParameter(parameter);

            ParameterGroupModel Group3 = new ParameterGroupModel("Group3", "Git");
            parameters.addGroup(Group3);
            parameter = new DirectoryParameterModel(configuration, INTOCPSParameters.GITREPOSITORYPATH, I18nMessageService.getString ("Ui.Parameter.GitRepositoryPath.Label"), I18nMessageService.getString ("Ui.Parameter.GitRepositoryPath.Description"),"");
            Group3.addParameter(parameter);

        }
        return this.parameterEditionModel;
    }

    @Override
    public String getModuleImagePath() {
        return "/res/icons/intocps-icon.png";
    }

    @Override
    public INTOCPSSession getLifeCycleHandler() {
        return this.session;
    }

    public static INTOCPSModule getInstance() {
        return instance;
    }

    public static boolean isAnalystDeployed(){
        return isAnalystDeployed;
    }


    public void setAnalystDeployed(){
        List<MObject> roots = this.getModuleContext().getModelingSession().getModel().getModelRoots();
        for (MObject root : roots){
            if (root.getName().equals(this.analystName))
                isAnalystDeployed = true;
        }
    }
}
