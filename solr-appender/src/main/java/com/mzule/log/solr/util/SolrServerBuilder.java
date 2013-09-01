package com.mzule.log.solr.util;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.mzule.log.solr.appender.SolrAppender;
import com.mzule.log.solr.factory.SolrServerFactory;

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
	public static SolrServer build(SolrAppender appender) {
		String host = appender.getHost();
		String zkHost = appender.getZkHost();
		String serverFactory = appender.getServerFactory();
		if (host != null) {
			// case HttpSolrServer
			return new HttpSolrServer(host);
		} else if (zkHost != null) {
			// case CloudSolrServer
			try {
				return new CloudSolrServer(zkHost);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (serverFactory != null) {
			// case customer serverFactory
			try {
				SolrServerFactory factory = (SolrServerFactory) Class.forName(
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
