package edu.ucsb.nceas.metacat.annotation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Resource;

import edu.ucsb.nceas.metacat.replication.ReplicationService;
import edu.ucsb.nceas.utilities.XMLUtilities;

public class BioPortalService {
	
	private static Logger logMetacat = Logger.getLogger(BioPortalService.class);
	
    // for looking up concepts in BioPortal
    private static final String REST_URL = "http://data.bioontology.org";
    private static final String API_KEY = "24e4775e-54e0-11e0-9d7b-005056aa3316";

    /**
	 * Look up possible concept from BioPortal annotation service.
	 * @see "http://data.bioontology.org/documentation"
	 * @param superClass
	 * @param text
	 * @return
	 */
	public static Resource lookupAnnotationClass(OntClass superClass, String text, String ontologies) {
		
		try {
			
			String urlParameters = "apikey=" + API_KEY;
			urlParameters += "&format=xml";
			if (ontologies != null) {
				urlParameters += "&ontologies=" + ontologies;
			}
			urlParameters += "&text=" + URLEncoder.encode(text, "UTF-8");
			
			String url = REST_URL + "/annotator?" + urlParameters ;
			URL restURL = new URL(url);
			InputStream is = ReplicationService.getURLStream(restURL);
			Document doc = XMLUtilities.getXMLReaderAsDOMDocument(new InputStreamReader(is, "UTF-8"));
			NodeList classNodeList = XMLUtilities.getNodeListWithXPath(doc, "//annotation/annotatedClass/id");
			if (classNodeList != null && classNodeList.getLength() > 0) {
				String classURI = classNodeList.item(0).getFirstChild().getNodeValue();
				logMetacat.info("annotator suggested: " + classURI);
				Resource subclass = superClass.getModel().getResource(classURI);
				// check that it is a subclass of superClass
				if (superClass.hasSubClass(subclass)) {
					return subclass;
				}
			}
		} catch (Exception e) {
			logMetacat.error("Could not lookup BioPortal annotation for text= " + text, e);
		}
		
		return null;
	}
}
