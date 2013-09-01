package com.mzule.log.solr.appender;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.mzule.log.solr.util.SolrServerBuilder;

/**
 * <p>
 * Appender to write logs to solr server.
 * </p>
 * 
 * @author mzule
 * 
 */
public class SolrAppender extends AppenderSkeleton {

	protected SolrServer solr;

	protected String host;
	protected String zkHost;
	protected String serverFactory;
	protected String tag;
	protected Field field;

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField(field.level, event.getLevel());
			doc.setField(field.message, event.getMessage());
			doc.setField(field.stacktrace, formatBody(event));
			doc.setField(field.tag, tag);
			doc.setField(field.thread, event.getThreadName());
			doc.setField(field.timestamp, new Date(event.getTimeStamp()));
			getSolr().add(doc);
			getSolr().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String formatBody(LoggingEvent event) {
		StringBuilder sb = new StringBuilder();
		String[] s = event.getThrowableStrRep();
		if (s != null) {
			for (int j = 0; j < s.length; j++) {
				sb.append(s[j]);
				sb.append(Layout.LINE_SEP);
			}
		}
		return sb.toString();
	}

	public SolrServer getSolr() {
		if (solr == null) {
			this.solr = SolrServerBuilder.build(this);
		}
		return solr;
	}

	public String getHost() {
		return host;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
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

	public void setSolr(SolrServer solr) {
		this.solr = solr;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
