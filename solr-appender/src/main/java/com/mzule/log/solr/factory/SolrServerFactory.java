package com.mzule.log.solr.factory;

import org.apache.solr.client.solrj.SolrServer;

/**
 * <p>
 * Interface for all SolrServer providers.
 * </p>
 * 
 * @author mzule
 * 
 */
public interface SolrServerFactory {

	/**
	 * <p>
	 * Method to construct and return SolrServer.
	 * </p>
	 * 
	 * @return
	 */
	SolrServer getSolrServer();

}
