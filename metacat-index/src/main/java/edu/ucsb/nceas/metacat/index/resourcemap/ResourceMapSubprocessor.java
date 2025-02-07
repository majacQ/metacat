/**
 *  '$RCSfile$'
 *    Purpose: A class that gets Accession Number, check for uniqueness
 *             and register it into db
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Jivka Bojilova, Matt Jones
 *
 *   '$Author: leinfelder $'
 *     '$Date: 2011-11-02 20:40:12 -0700 (Wed, 02 Nov 2011) $'
 * '$Revision: 6595 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package edu.ucsb.nceas.metacat.index.resourcemap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.LocalSolrQueryRequest;
import org.apache.solr.response.QueryResponseWriter;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.DateField;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.servlet.SolrRequestParsers;
import org.dataone.cn.indexer.parser.AbstractDocumentSubprocessor;
import org.dataone.cn.indexer.parser.IDocumentSubprocessor;
import org.dataone.cn.indexer.resourcemap.ResourceMap;
import org.dataone.cn.indexer.solrhttp.SolrDoc;
import org.dataone.cn.indexer.solrhttp.SolrElementField;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.UnsupportedType;
import org.dataone.service.types.v1.Subject;
import org.dspace.foresite.OREParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.ucsb.nceas.metacat.common.query.SolrQueryResponseTransformer;
import edu.ucsb.nceas.metacat.common.query.SolrQueryResponseWriterFactory;
import edu.ucsb.nceas.metacat.common.query.SolrQueryServiceController;
import edu.ucsb.nceas.metacat.common.SolrServerFactory;
import edu.ucsb.nceas.metacat.index.SolrIndex;


/**
 * A solr index parser for the ResourceMap file.
 * The solr doc of the ResourceMap self only has the system metadata information.
 * The solr docs of the science metadata doc and data file have the resource map package information.
 */
public class ResourceMapSubprocessor extends AbstractDocumentSubprocessor implements IDocumentSubprocessor {

    private static final String QUERY ="q=id:";
    private static Log log = LogFactory.getLog(SolrIndex.class);
    private static SolrServer solrServer =  null;
    private static SolrCore solrCore = null;
    private static CoreContainer solrCoreContainer = null;
    static {
        try {
            solrServer = SolrServerFactory.createSolrServer();
            CoreContainer solrCoreContainer = SolrServerFactory.getCoreContainer();
            String coreName = SolrServerFactory.getCollectionName();
            solrCore = solrCoreContainer.getCore(coreName);
        } catch (Exception e) {
            log.error("ResourceMapSubprocessor - can't generate the SolrServer since - "+e.getMessage());
        }
    }
          
    @Override
    public Map<String, SolrDoc> processDocument(String identifier, Map<String, SolrDoc> docs,
    Document doc) throws IOException, EncoderException, SAXException,
    XPathExpressionException, ParserConfigurationException, SolrServerException, NotImplemented, NotFound, UnsupportedType, OREParserException {
        SolrDoc resourceMapDoc = docs.get(identifier);
        List<SolrDoc> processedDocs = processResourceMap(resourceMapDoc, doc);
        Map<String, SolrDoc> processedDocsMap = new HashMap<String, SolrDoc>();
        for (SolrDoc processedDoc : processedDocs) {
            processedDocsMap.put(processedDoc.getIdentifier(), processedDoc);
        }
        return processedDocsMap;
    }

    private List<SolrDoc> processResourceMap(SolrDoc indexDocument, Document resourceMapDocument)
                    throws XPathExpressionException, IOException, SAXException, ParserConfigurationException, EncoderException, SolrServerException, NotImplemented, NotFound, UnsupportedType, OREParserException{
        //ResourceMap resourceMap = new ResourceMap(resourceMapDocument);
        ResourceMap resourceMap = new ResourceMap(resourceMapDocument);
        List<String> documentIds = resourceMap.getAllDocumentIDs();//this list includes the resourceMap id itself.
        //List<SolrDoc> updateDocuments = getHttpService().getDocuments(getSolrQueryUri(), documentIds);
        List<SolrDoc> updateDocuments = getSolrDocs(resourceMap.getIdentifier(), documentIds);
        List<SolrDoc> mergedDocuments = resourceMap.mergeIndexedDocuments(updateDocuments);
        /*if(mergedDocuments != null) {
            for(SolrDoc doc : mergedDocuments) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                doc.serialize(out, "UTF-8");
                String result = new String(out.toByteArray(), "UTF-8");
                System.out.println("after updated document===========================");
                System.out.println(result);
            }
        }*/
        mergedDocuments.add(indexDocument);
        return mergedDocuments;
    }
    
    private List<SolrDoc> getSolrDocs(String resourceMapId, List<String> ids) throws SolrServerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, NotImplemented, NotFound, UnsupportedType {
        List<SolrDoc> list = new ArrayList<SolrDoc>();
        if(ids != null) {
            for(String id : ids) {
                SolrDoc doc = getSolrDoc(id);
                if(doc != null) {
                    list.add(doc);
                } else if ( !id.equals(resourceMapId)) {
                    throw new SolrServerException("Solr index doesn't have the information about the id "+id+" which is a component in the resource map "+resourceMapId+". Metacat-Index can't process the resource map prior to its components.");
                }
            }
        }
        return list;
    } 
    
    /*
     * Get the SolrDoc list for the list of the ids.
     */
    public static List<SolrDoc> getSolrDocs(List<String> ids) throws SolrServerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, NotImplemented, NotFound, UnsupportedType {
        List<SolrDoc> list = new ArrayList<SolrDoc>();
        if(ids != null) {
            for(String id : ids) {
                SolrDoc doc = getSolrDoc(id);
                if(doc != null) {
                    list.add(doc);
                }
            }
        }
        return list;
    }
    
    /*
     * Get the SolrDoc for the specified id 
     */
    public static SolrDoc getSolrDoc(String id) throws SolrServerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, NotImplemented, NotFound, UnsupportedType {
        SolrDoc solrDoc = null;
        if(solrServer != null) {
           String query = QUERY+"\""+id+"\"";
           SolrParams solrParams = SolrRequestParsers.parseQueryString(query);
           Set<Subject>subjects = null;//when subjects are null, there will not be any access rules.
           InputStream response = SolrQueryServiceController.getInstance().query(solrParams, subjects);
           solrDoc = transformQueryResponseToSolrDoc(solrParams, response);
           
           /*if(solrDoc != null) {
               ByteArrayOutputStream out = new ByteArrayOutputStream();
               solrDoc.serialize(out, "UTF-8");
               String result = new String(out.toByteArray(), "UTF-8");
               System.out.println("need to be updated document ===========================");
               System.out.println(result);
           }*/
           
        }
        return solrDoc;
    }
    
    /*
     * Transform a Solr QueryReponse to a SolrDoc. The QueryReponse contains a list of
     * SolrDocuments. This method will transform the first SolrDocuments (in the Solr lib) to
     * the SolrDoc (in the d1_cn_index_processor lib).
     * @param reponse
     * @return
     */
    private static SolrDoc transformQueryResponseToSolrDoc(SolrParams solrParams, InputStream response) throws SolrServerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, UnsupportedType, NotFound {
        SolrDoc solrDoc = null;
        if(response != null) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(response);
            solrDoc = parseResults(doc);
        }
        return solrDoc;
    }
    
   
    
    /*
     * Parse the query result document. This method only choose the first one from a list.
     */
    private static SolrDoc parseResults(Document document) throws XPathExpressionException, MalformedURLException, UnsupportedType, NotFound, ParserConfigurationException, IOException, SAXException {
        SolrDoc solrDoc = null;
        NodeList nodeList = (NodeList) XPathFactory.newInstance().newXPath()
                .evaluate("/response/result/doc", document, XPathConstants.NODESET);
        if(nodeList != null && nodeList.getLength() >0) {
            Element docElement = (Element) nodeList.item(0);
            solrDoc = parseDoc(docElement);
        }
        return solrDoc;
    }

    
    /*
     * Parse an element
     */
    private static SolrDoc parseDoc(Element docElement) throws MalformedURLException, UnsupportedType, NotFound, ParserConfigurationException, IOException, SAXException {
        List<String> validSolrFieldNames = SolrQueryServiceController.getInstance().getValidSchemaFields();
        SolrDoc doc = new SolrDoc();
        doc.LoadFromElement(docElement, validSolrFieldNames);
        return doc;
    }
    
    
    /**
     * Get the valid schema fields from the solr server.
     * @return
     */
    /*private static List<String> getValidSchemaField() {
        List<String> validSolrFieldNames = new ArrayList<String>();
        IndexSchema schema = solrCore.getSchema();
        Map<String, SchemaField> fieldMap = schema.getFields();
        Set<String> fieldNames = fieldMap.keySet();
        for(String fieldName : fieldNames) {
            SchemaField field = fieldMap.get(fieldName);
            //remove the field which is the target field of a CopyField.
            if(field != null && !schema.isCopyFieldTarget(field)) {
                 validSolrFieldNames.add(fieldName);
            }
        }
        //System.out.println("the valid file name is\n"+validSolrFieldNames);
        return validSolrFieldNames;
    }*/

}
