package [SymBundleName].impl;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import [SymBundleName].RestApiConfiguration;
import [SymBundleName].gen.NotFoundException;
import [SymBundleName].gen.StateApi;
import [SymBundleName].gen.StateApiService;
import [SymBundleName].gen.model.ExecutionState;

/**
 * The individual implementation class for the {@link StateApi}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class StateApiImpl extends StateApiService {

	@Override
	public Response stateGet(SecurityContext securityContext) throws NotFoundException {
		
		ExecutionState execState = new ExecutionState();
		
		
		// TODO
		
		return Response.ok().variant(RestApiConfiguration.getResponseVariant()).entity(execState).build();
	}

}
