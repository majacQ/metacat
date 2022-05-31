package edu.ucsb.nceas.metacat.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.ucsb.nceas.metacat.dataone.MNodeService;
import edu.ucsb.nceas.utilities.XMLUtilities;

public class OrcidService {
	
	private static Log logMetacat = LogFactory.getLog(OrcidService.class);
	
    //private static final String REST_URL = "http://pub.sandbox.orcid.org/v1.1/search/orcid-bio";
    private static final String REST_URL = "https://pub.orcid.org/v2.0/search";
    
    private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000).build();
    private static CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

    
    /**
	 * Look up possible ORCID from orcid service.
	 * @see "http://support.orcid.org/knowledgebase/articles/132354-searching-with-the-public-api"
	 * @param surName
	 * @param givenNames
	 * @param otherNames
	 * @return
	 */
	public static String lookupOrcid(String text, String surName, List<String> givenNames, List<String> otherNames) {
		
		String url = null;
		CloseableHttpResponse response = null;
		InputStream is = null;

		try {
			
			String urlParameters = "";
			
			if (text != null) {
				urlParameters += "\"" + text + "\""; 
			} else {
				if (surName != null) {
//					surName = surName.replaceAll(" ", "%20");
					urlParameters += "+family-name:\"" + surName + "\"";
				}
				if (otherNames != null) {
					for (String otherName: otherNames) {
//						otherName = otherName.replaceAll(" ", "%20");
						urlParameters += "+other-names:\"" + otherName + "\""; 
					}
				}
				if (givenNames != null) {
					for (String givenName: givenNames) {
//						givenName = givenName.replaceAll(" ", "%20");
						urlParameters += "+given-names:\"" + givenName + "\""; 
					}
				}
			}
			
			urlParameters = URLEncoder.encode(urlParameters, "UTF-8");
			
			url = REST_URL + "?q=" + urlParameters + "&rows=1";
			HttpGet method = new HttpGet(url);
			method.addHeader("Accept", "application/orcid+xml");
			response = client.execute(method);
			is = response.getEntity().getContent();
			//InputStream is = restURL.openStream();
			
			String results = IOUtils.toString(is, "UTF-8");
			logMetacat.debug("RESULTS: " + results);
			Node doc = XMLUtilities.getXMLReaderAsDOMTreeRootNode(new StringReader(results));
			Node orcidUriNodeList = XMLUtilities.getNodeWithXPath(doc, "//*[local-name()=\"orcid-identifier\"]/*[local-name()=\"uri\"]");
			if (orcidUriNodeList != null) {
				String orcidUri = orcidUriNodeList.getFirstChild().getNodeValue();
				logMetacat.info("Found ORCID URI: " + orcidUri);
				return orcidUri;
			}
		} catch (Exception e) {
			logMetacat.error("Could not lookup ORCID using: " + url, e);
		} finally {
		    if (response != null) {
		        try {
		            response.close();
		        } catch (IOException ee) {
		            logMetacat.warn("OrcidServic.lookupOrcid - could not close the http response from the ORCID service since " + ee.getMessage());
		        }
		        
		    }
		    if (is != null) {
		        try {
                    is.close();
                } catch (IOException ee) {
                    logMetacat.warn("OrcidServic.lookupOrcid - could not close the input stream object from the http response " + ee.getMessage());
                }
		    }
		}
		
		return null;
	}
	
	/**
	 * Look up indexed creators
	 */
	public static List<String> lookupCreators(boolean includeObsolete) {
		// Search for the creators if we can find them
		List<String> retList = null;
		try {
			String query = "q=-obsoletedBy:[* TO *]&rows=0&facet=true&facet.limit=-1&facet.field=origin";
			if (includeObsolete) {
				query = "q=*:*&rows=0&facet=true&facet.limit=-1&facet.field=origin";
			}
			
			MockHttpServletRequest request = new MockHttpServletRequest(null, null, null);
			InputStream results = MNodeService.getInstance(request ).query(null, "solr", query);
			Node rootNode = XMLUtilities.getXMLReaderAsDOMTreeRootNode(new InputStreamReader(results, "UTF-8"));
			//String resultString = XMLUtilities.getDOMTreeAsString(rootNode);
			NodeList nodeList = XMLUtilities.getNodeListWithXPath(rootNode, "//lst[@name=\"origin\"]/int/@name");
			if (nodeList != null && nodeList.getLength() > 0) {
				retList = new ArrayList<String>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					String found = nodeList.item(i).getFirstChild().getNodeValue();
					retList.add(found);
				}
			}
		} catch (Exception e) {
			logMetacat.error("Error checking for creators[s]: " + e.getMessage(), e);
		}
		
		return retList;
	}
}
