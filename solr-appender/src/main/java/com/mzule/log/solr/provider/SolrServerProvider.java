package com.mzule.log.solr.provider;

import org.apache.solr.client.solrj.SolrServer;

/**
 * <p>
 * Interface for all SolrServer providers.
 * </p>
 * 
 * @author mzule
 * 
 */
public interface SolrServerProvider {

	/**
	 * <p>
	 * Method to construct and return SolrServer.
	 * </p>
	 * 
	 * @return
	 */
	SolrServer getSolrServer();

}
