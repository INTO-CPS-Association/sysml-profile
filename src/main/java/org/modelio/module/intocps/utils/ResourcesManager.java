/**
 * Java Class : ResourcesManager.java
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
 * @category   Util
 * @package    com.modeliosoft.modelio.sysml.utils
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.utils;

import java.io.File;
import org.modelio.api.module.IModule;


/**
 * This class handles the SysML resources i.e. images, styles, property names, etc.
 * @author ebrosse
 *
 */
public class ResourcesManager {


	private static final String configFolderName = "configs";

	private static final String mdFolderName = "modeldescriptions";

	private static final String pngFolderName = "pngs";

	private static final String fmuFolderName = "fmus";

	private static final String jsonSchemaName = "INTO-CPS-Traceability-Schema-V1.4.json";

	private static ResourcesManager instance = null;

	private IModule _mdac;

	/**
	 * Method ResourcesManager
	 * @author ebrosse
	 */

	private ResourcesManager() {
	}

	/**
	 * Method getInstance
	 * @author ebrosse
	 * @return the SysMLResourcesManager instance
	 */

	public static ResourcesManager getInstance() {
		if(instance == null){
			instance =  new ResourcesManager();
		}
		return instance;
	}

	/**
	 * This method sets the current module
	 * @param module : the current module
	 */
	public void setJMDAC(IModule module) {
		this._mdac = module;
	}

	/**
	 * Method getImage
	 * @author ebrosse
	 * @param imageName : the name of the image file
	 * @return the complete path of the image file
	 */
	public String getImage(String imageName) {
		return this._mdac.getModuleContext().getConfiguration().getModuleResourcesPath() + File.separator + "res" + File.separator + "icons" + File.separator + imageName;
	}


	public String getJSONSchema() {
		return this._mdac.getModuleContext().getConfiguration().getModuleResourcesPath() + File.separator + "res" + File.separator + "json" + File.separator + jsonSchemaName;
	}

	public String getPlugin(String pluginName) {
		return this._mdac.getModuleContext().getConfiguration().getModuleResourcesPath() + File.separator + "res" + File.separator + "plugin" + File.separator + pluginName;
	}

	public String getProjectSimulationPath(){
		return this._mdac.getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString() + File.separator + "Simulation";
	}

	public String getFMUFolderPath(){
		return this.getProjectSimulationPath() + File.separator + fmuFolderName;
	}

	public String getConfigPath(){
		return this._mdac.getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString() + File.separator + configFolderName;
	}

	public String getMDPath(){
		return this._mdac.getModuleContext().getProjectStructure().getPath().toAbsolutePath().toString() + File.separator + mdFolderName;
	}

	public String getPNGPath(){
		return this.getConfigPath() + File.separator + pngFolderName;
	}


}
