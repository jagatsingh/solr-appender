package com.mzule.log.solr.util;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.mzule.log.solr.appender.SolrAppender;
import com.mzule.log.solr.provider.SolrServerProvider;

/**
 * <p>
 * Builder utility class to build SolrServer.
 * </p>
 * 
 * @author mzule
 * 
 */
public class SolrServerBuilder {

	/**
	 * <p>
	 * Build SolrServer depending on configuration under log4j.properties
	 * </p>
	 * <ul>
	 * <li>Case host presented, build HttpSolrServer with host.</li>
	 * <li>Case zkHost presented, build CloudSolrServer with zkHost.</li>
	 * <li>Case serverFactory presented, build SolrServer by SolrServerFactory</li>
	 * <li>Otherwise exception is throwed.</li>
	 * </ul>
	 * 
	 * @param appender
	 * @return
	 */
	public static SolrClient build(SolrAppender appender) {
		String host = appender.getHost();
		String zkHost = appender.getZkHost();
		String serverFactory = appender.getServerFactory();
		if (host != null) {
			// case HttpSolrServer
			return new HttpSolrClient(host);
		} else if (zkHost != null) {
			// case CloudSolrServer
			try {
				return new CloudSolrClient(zkHost);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (serverFactory != null) {
			// case customer serverFactory
			try {
				SolrServerProvider factory = (SolrServerProvider) Class.forName(
						serverFactory).newInstance();
				return factory.getSolrServer();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		throw new IllegalStateException(
				"One of host, zkHost or serverFactory required.");
	}
}
