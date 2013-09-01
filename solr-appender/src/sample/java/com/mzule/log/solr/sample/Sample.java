package com.mzule.log.solr.sample;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class Sample {

	private static final Logger log = Logger.getLogger(Logger.class);

	public static void main(String[] args) {
		log.info("this is a INFO level log");
		try {
			HttpSolrServer server = new HttpSolrServer("cds");
			server.add((SolrInputDocument) null);
			server.commit();
		} catch (Throwable e) {
			log.error("Never Error is expected.", e);
		}
	}
}
