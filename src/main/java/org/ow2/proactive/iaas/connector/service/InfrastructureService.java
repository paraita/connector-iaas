package org.ow2.proactive.iaas.connector.service;

import java.util.Map;

import org.ow2.proactive.iaas.connector.cache.ComputeServiceCache;
import org.ow2.proactive.iaas.connector.cache.InfrastructureCache;
import org.ow2.proactive.iaas.connector.model.Infrastructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfrastructureService {

	@Autowired
	private InfrastructureCache infrastructureCache;

	@Autowired
	private ComputeServiceCache computeServiceCache;

	public Map<String, Infrastructure> getAllSupportedInfrastructure() {
		return infrastructureCache.getSupportedInfrastructures();
	}

	public void registerInfrastructure(Infrastructure infrastructure) {
		infrastructureCache.registerInfrastructure(infrastructure);
	}

	public void deleteInfrastructure(String infrastructureName) {
		infrastructureCache.deleteInfrastructure(infrastructureName);
		computeServiceCache.removeComputeService(getInfrastructurebyName(infrastructureName));
	}

	public Infrastructure getInfrastructurebyName(String infrastructureName) {
		return infrastructureCache.getSupportedInfrastructures().get(infrastructureName);
	}

	public void updateInfrastructure(Infrastructure infrastructure) {
		infrastructureCache.updateInfrastructure(infrastructure);
	}

}
