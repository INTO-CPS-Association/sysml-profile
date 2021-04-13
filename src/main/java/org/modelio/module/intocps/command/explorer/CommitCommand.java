/**
 * Java Class : AddBlockDiagramExplorerCommand.java
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
 * @category   Command explorer
 * @package    com.modeliosoft.modelio.sysml.gui.explorer
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.intocps.command.explorer;

import java.util.List;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.vcore.smkernel.mapi.MObject;


/**
 * This class handles the creation of SysML block diagram
 * @author ebrosse
 *
 */
public class CommitCommand extends DefaultModuleCommandHandler {

	@Override
	public void actionPerformed(List<MObject> selectedElements, IModule module) {

//		IModuleUserConfiguration config = module.getModuleContext().getConfiguration();
//
//		String hostUrl = "http://" + config.getParameterValue(INTOCPSParameters.IPADRESSE)
//				+ ":" + config.getParameterValue(INTOCPSParameters.PORT);
//
//		UrlScheme.SchemeType schemeType = UrlScheme.SchemeType.github;
//
//		String agentName = config.getParameterValue(INTOCPSParameters.NAME);
//
//		String agentEmail = config.getParameterValue(INTOCPSParameters.EMAIL);
//
//		TraceDriver driver = new TraceDriver(hostUrl, schemeType, agentName, agentEmail);
//
//		ElementStore eltStore = ElementStore.getInstance();
//
//		if (!eltStore.isEmpty()){
//
//			try {
//				//commit
//				GitCmd gitcmd = new GitCmd(module.getConfiguration().getProjectSpacePath().toFile() );
//				RevCommit revCommit = gitcmd.commit("User commit");
//
////				driver.syncModelio(schemeType, gitcmd.getGitRepo(), revCommit.getId());
//			} catch (IOException | GitAPIException | JSONException | ParseException
//					| InterruptedException e) {
//				INTOCPSModule.logService.error(e);
//			}
//			eltStore.clearSet();
//		}

	}




	@Override
	public boolean accept(List<MObject> selectedElements, IModule module) {
		return true;
	}

}
