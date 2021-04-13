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
package org.modelio.module.intocps.model.fmi.exporter;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.module.intocps.impl.INTOCPSModule;
import org.modelio.module.intocps.model.fmi.FmiModelDescription;



/**
 * This class handles the ModelDescription export
 * @author ebrosse
 *
 */
public class MDExporter {




	/**
	 * Method ResourcesManager
	 * @author ebrosse
	 */

	public MDExporter() {
	}



	public void exportFMI(Class block, File file){

		FmiModelDescription md = exportClass(block);

		//Serialization
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(FmiModelDescription.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			if (!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			jaxbMarshaller.marshal(md, file);

		} catch (JAXBException e) {
			INTOCPSModule.logService.error(e);
		} catch (IOException e) {
			INTOCPSModule.logService.error(e);
		}
	}

	public FmiModelDescription exportClass(Class block) {

		FMIFactory fmiFactory = new FMIFactory();

		FmiModelDescription md = fmiFactory.createFMIModelDescription(block);

		//attributes
		for (Attribute att : block.getOwnedAttribute()){
			fmiFactory.exportAttribut(att);
		}

		//Bindable Instance and Port
		for (BindableInstance bi: block.getInternalStructure()){
			if (bi instanceof Port){
				fmiFactory.exportPort((Port) bi);
			}else{
				fmiFactory.exportBindableInstance(bi);
			}
		}

		fmiFactory.exportDependencies(md);

		return md;
	}





}
