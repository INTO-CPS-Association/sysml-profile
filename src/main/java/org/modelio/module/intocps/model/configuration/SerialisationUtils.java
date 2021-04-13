package org.modelio.module.intocps.model.configuration;

import java.io.File;
import java.io.IOException;

import org.modelio.module.intocps.impl.INTOCPSModule;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerialisationUtils {

	public boolean ConfigExport(CoeConfiguration config, File file){

		//Object to JSON in file
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(file, config);

		} catch (JsonGenerationException e) {
			INTOCPSModule.logService.error(e);
			return false;
		} catch (JsonMappingException e) {
			INTOCPSModule.logService.error(e);
			return false;
		} catch (IOException e) {
			INTOCPSModule.logService.error(e);
			return false;
		}

		return true;
	}


	public boolean ConfigExport(CoeConfiguration config, String path){

		//Object to JSON in file
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(new File(path), config);

		} catch (JsonGenerationException e) {
			INTOCPSModule.logService.error(e);
			return false;
		} catch (JsonMappingException e) {
			INTOCPSModule.logService.error(e);
			return false;
		} catch (IOException e) {
			INTOCPSModule.logService.error(e);
			return false;
		}

		return true;
	}




}
