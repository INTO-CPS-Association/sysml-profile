package org.modelio.module.intocps.api;

import org.modelio.api.module.IPeerModule;

/**
 * @see com.modeliosoft.modelio.api.module.IPeerModule
 */
public interface IINTOCPSPeerModule extends IPeerModule {

	public static final String MODULE_NAME = "INTOCPS";

	public static final String MODULE_VERSION= "1.3.16";

	public boolean pushModel();

}
