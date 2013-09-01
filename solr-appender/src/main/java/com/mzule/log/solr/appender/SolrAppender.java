package com.mzule.log.solr.appender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.mzule.log.solr.factory.SolrServerFactory;

public class SolrAppender extends AppenderSkeleton implements Appender {

	protected SolrServer solr;

	protected String host;
	protected String zkHost;
	protected String serverFactory;

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", UUID.randomUUID());
			doc.setField("type", event.getLevel());
			doc.setField("name", formatBody(event));
			getSolr().add(doc);
			getSolr().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String formatBody(LoggingEvent event) {
		StringBuffer sbuf = new StringBuffer();
		String[] s = event.getThrowableStrRep();
		if (s != null) {
			for (int j = 0; j < s.length; j++) {
				sbuf.append(s[j]);
				sbuf.append(Layout.LINE_SEP);
			}
		}
		return sbuf.toString();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getServerFactory() {
		return serverFactory;
	}

	public void setServerFactory(String serverFactory) {
		this.serverFactory = serverFactory;
	}

	public String getZkHost() {
		return zkHost;
	}

	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	public SolrServer getSolr() {
		if (solr == null) {
			if (host != null) {
				// case HttpSolrServer
				solr = new HttpSolrServer(host);
			} else if (zkHost != null) {
				// case CloudSolrServer
				try {
					solr = new CloudSolrServer(zkHost);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} else if (serverFactory != null) {
				// case customer serverFactory
				try {
					SolrServerFactory factory = (SolrServerFactory) Class
							.forName(serverFactory).newInstance();
					solr = factory.getSolrServer();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return solr;
	}
}
