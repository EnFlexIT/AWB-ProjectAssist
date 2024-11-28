package [SymBundleName];

import jade.core.Agent;

/**
 * The Class MyAgent.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MyAgent extends Agent {


	private static final long serialVersionUID = 7263335870426015831L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {

		// --- Start here, to add individual agent behaviors --------
		System.out.println("Hello, my agent name is '" + this.getLocalName() + "'");
		
		
	}
	
}
