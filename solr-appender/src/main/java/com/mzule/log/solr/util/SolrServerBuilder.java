package com.mzule.log.solr.util;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.mzule.log.solr.appender.SolrAppender;
import com.mzule.log.solr.factory.SolrServerFactory;

public class SolrServerBuilder {

	private String host;
	private String zkHost;
	private String serverFactory;

	public SolrServerBuilder(SolrAppender appender) {
		this.host = appender.getHost();
		this.zkHost = appender.getZkHost();
		this.serverFactory = appender.getServerFactory();
	}

	public SolrServer build() {
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
		return null;
	}

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
		return null;
	}

}
