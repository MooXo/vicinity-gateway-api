package eu.bavenir.ogwapi.restapi.services;

import java.util.logging.Logger;

import org.apache.commons.configuration2.XMLConfiguration;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import eu.bavenir.ogwapi.commons.CommunicationManager;
import eu.bavenir.ogwapi.restapi.Api;

/**
 * This class implements a {@link org.restlet.resource.ServerResource ServerResource} interface for following
 * Gateway API calls:
 * 
 *   URL: 				[server]:[port]/api/agents/{agid}/objects/update
 *   METHODS: 			PUT
 *   SPECIFICATION:		@see <a href="https://vicinityh2020.github.io/vicinity-gateway-api/#/">Gateway API</a>
 *   ATTRIBUTES:		agid - VICINITY Identifier of the Agent, that is in control of the Adapters 
 *   					(e.g. 1dae4326-44ae-4b98-bb75-15aa82516cc3).
 *   
 * @author sulfo
 *
 */
public class AgentsAgidObjectsUpdate extends ServerResource {

	/**
	 * Name of the Agent ID attribute.
	 */
	private static final String ATTR_AGID = "agid";

	
	
	
	@Put("json")
	public Representation store(Representation entity) {
		
		String attrAgid = getAttribute(ATTR_AGID);
		
		Logger logger = (Logger) getContext().getAttributes().get(Api.CONTEXT_LOGGER);
		XMLConfiguration config = (XMLConfiguration) getContext().getAttributes().get(Api.CONTEXT_CONFIG);
		
		if (attrAgid == null){
			logger.info("AGID: " + attrAgid + " Invalid Agent ID.");
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, 
					"Invalid Agent ID.");
		}
		
		if (!entity.getMediaType().equals(MediaType.APPLICATION_JSON)){
			logger.info("AGID: " + attrAgid + " Invalid object descriptions.");
			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, 
					"Invalid object descriptions");
		}
		
		return lightweightUpdate(entity, logger, config);
	}
	
	
	private Representation lightweightUpdate(Representation json, Logger logger, XMLConfiguration config){
		
		CommunicationManager communicationManager 
			= (CommunicationManager) getContext().getAttributes().get(Api.CONTEXT_COMMMANAGER);

		return communicationManager.lightweightUpdate(json);
	}
}
