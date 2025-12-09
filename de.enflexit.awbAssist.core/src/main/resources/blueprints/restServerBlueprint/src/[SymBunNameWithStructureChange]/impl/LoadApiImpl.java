package [SymBundleName].impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import [SymBundleName].RestApiConfiguration;
import [SymBundleName].gen.LoadApi;
import [SymBundleName].gen.LoadApiService;
import [SymBundleName].gen.NotFoundException;
import [SymBundleName].gen.model.SystemLoad;
import de.enflexit.awb.simulation.load.LoadMeasureThread;

/**
 * The individual implementation class for the {@link LoadApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LoadApiImpl extends LoadApiService {

	@Override
	public Response loadGet(SecurityContext securityContext) throws NotFoundException {
				
		// --- Create return value -----------------------
		SystemLoad sysLoad = new SystemLoad();
		sysLoad.setCpuUsage(LoadMeasureThread.getLoadCPU());
		sysLoad.setMemUsage(LoadMeasureThread.getLoadRAM());
		sysLoad.setHeapUsage(LoadMeasureThread.getLoadMemoryJVM());

		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(sysLoad).build();
	}

}
