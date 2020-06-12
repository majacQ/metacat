/**
 *    Purpose: Implements a service for managing a Hazelcast cluster member
 *  Copyright: 2020 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
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
package edu.ucsb.nceas.metacat.dataone.quota;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.message.BasicHeader;
import org.apache.wicket.util.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.Usage;
import org.dataone.configuration.Settings;
import org.dataone.service.exceptions.InvalidRequest;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.Subject;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * A client class to access the BookKeeper service
 * @author tao
 *
 */
public class BookKeeperClient {
    private static final String QUOTAS = "quotas";
    private static final String QUOTATYPE = "quotaType";
    private static final String QUOTAID = "quotaId";
    private static final String SUBSCRIBER = "subscriber";
    private static final String REQUESTOR = "requestor";
    private static final String USAGES = "usages";
    private static final String INSTNACEID = "instanceId";
    private static Log logMetacat  = LogFactory.getLog(BookKeeperClient.class);
    private static BookKeeperClient bookKeeperClient = null;
    
    private String bookKeeperURL = null;
    private CloseableHttpClient httpClient = null;
    private ObjectMapper mapper = new ObjectMapper();
    private BasicHeader header = null;
    
    
    /**
     * A private constructor
     * @throws IOException 
     * @throws ServiceFailure 
     */
    protected BookKeeperClient() throws IOException, ServiceFailure {
        if (bookKeeperURL == null) {
            bookKeeperURL = Settings.getConfiguration().getString("dataone.quotas.bookkeeper.serviceUrl");
            logMetacat.debug("BookKeeperClient.BookKeeperClient - the bookkeeper service url from the metacat.properties file is " + bookKeeperURL);
            if (bookKeeperURL == null || bookKeeperURL.trim().equals("")) {
                throw new ServiceFailure("0000", "The quota service url can't be null or blank. Please ask the Metacat admin to check the property \"dataone.quotas.bookkeeper.serviceUrl\" in its metacat.properties file.");
            }
            if (!bookKeeperURL.endsWith("/")) {
                bookKeeperURL = bookKeeperURL + "/";
            }
            logMetacat.debug("BookKeeperClient.BookKeeperClient - the final bookkeeper service url is " + bookKeeperURL);
        }
        if (header == null) {
            String tokenFilePath = Settings.getConfiguration().getString("dataone.bearToken.file");
            File tokenFile = new File(tokenFilePath);
            String token = FileUtils.readFileToString(tokenFile, "UTF-8");
            if (token == null || token.trim().equals("")) {
                throw new ServiceFailure("0000", "The member node token can't be null or blank when it access the remote quota service. Please ask the Metacat admin to check the content of the token file with the path " + tokenFilePath + 
                        ". If the token file path is null or blank, please ask the Metacat admin to set the proper token file path at the property \"dataone.bearToken.file\" in its metacat.properties file.");
            }
            header = new BasicHeader("Authorization", "Beaer " + token);
        }
        httpClient = HttpClientBuilder.create().build();
    }
    
    /**
     * Get the singleton instance of the BookKeeplerClient class
     * @return  the instance of the class
     * @throws IOException 
     * @throws ServiceFailure 
     */
    public static BookKeeperClient getInstance() throws IOException, ServiceFailure {
        if (bookKeeperClient == null) {
            synchronized (BookKeeperClient.class) {
              if (bookKeeperClient == null) {
                  bookKeeperClient = new BookKeeperClient();
              }
           }
         }
         return bookKeeperClient;
    }
    
    
    /**
     * List the quotas associated with the given subject
     * @param subscriber  the subject who owns the quotas
     * @param requestor  the subject of user who will request a usage 
     * @param quotaType  the type of the quotas (storage or portal)
     * @return  the list of quotas associated with the subject.
     * @throws IOException 
     * @throws ClientProtocolException 
     * @throws NotFound 
     * @throws ServiceFailure 
     * @throws InvalidRequest 
     */
    public List<Quota> listQuotas(String subscriber, String requestor, String quotaType) throws ClientProtocolException, IOException, NotFound, ServiceFailure, InvalidRequest {
        List<Quota> result = null;
        if (subscriber != null && !subscriber.trim().equals("") && quotaType != null && !quotaType.trim().equals("") && requestor != null && !requestor.trim().equals("")) {
            String restStr = bookKeeperURL + QUOTAS + "?"+ SUBSCRIBER + "=" + subscriber + "&" + QUOTATYPE + "=" + quotaType + "&" + REQUESTOR + requestor;
            logMetacat.debug("BookKeeperClient.listQuotas - the rest request to list the quotas is " + restStr);
            HttpGet get = new HttpGet(restStr);
            get.addHeader(header);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(get);
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    result = mapper.readValue(response.getEntity().getContent(), List.class);
                    if (result != null) {
                        logMetacat.debug("BookKeeperClient.listQuotas - the bookkeeper service return a list of quotas with the size " + result.size());
                    } else {
                        logMetacat.debug("BookKeeperClient.listQuotas - the bookkeeper service return null");
                    }
                } else if (status == 404) {
                    throw new NotFound("0000", "The quota with the subscription subject " + subscriber + " is not found");
                } else {
                    String error = IOUtils.toString(response.getEntity().getContent());
                    throw new ServiceFailure("0000", "Quota service can't fulfill to list quotas since " + error);
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } else {
            throw new InvalidRequest("0000", "The quota subscriber, requestor and quota type can't be null or blank");
        }
        return result;
    }
    
    /**
     * Create a usage record for a given quota identifier in the book keeper service. If it fails, an exception will be thrown
     * @param usage  the object of the usage will be created
     * @throws ClientProtocolException
     * @throws IOException
     * @throws ServiceFailure
     */
    public void createUsage(Usage usage) throws ClientProtocolException, IOException, ServiceFailure {
        String restStr = bookKeeperURL + USAGES;
        logMetacat.debug("BookKeeperClient.updateUsage - the rest request to create the usuage is " + restStr);
        String jsonStr = mapper.writeValueAsString(usage); 
        logMetacat.debug("BookKeeperClient.updateUsage - the json string will be sent is " + jsonStr);
        StringEntity reqEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        reqEntity.setChunked(true);
        HttpPost post = new HttpPost(restStr);
        post.setEntity(reqEntity);
        post.addHeader(header);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                String error = IOUtils.toString(response.getEntity().getContent());
                throw new ServiceFailure("0000", "Quota service can't create the usage since " + error);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    /**
     * Delete the usage with the given quota type and instance id in the remote book keeper service
     * @param quotaType  the quota type associated with the usage
     * @param instanceId  the instance id associated with the usage
     * @throws InvalidRequest
     * @throws ClientProtocolException
     * @throws ServiceFailure
     * @throws IOException
     */
    public void deleteUsage(int quotaId, String instanceId) throws InvalidRequest, ClientProtocolException, ServiceFailure, IOException {
        if (instanceId != null && !instanceId.trim().equals("")) {
            List<Usage> usages = null;
            try {
                usages = listUsages(quotaId, instanceId);
                if (usages == null || usages.size() == 0) {
                    logMetacat.warn("BookKeeperClient.deleteUsage - the book keeper service don't find any usages matching the quota id " + quotaId + " and instance id " + instanceId + ". So we don't need to delete anything.");
                } else if (usages.size() == 1) {
                    Usage usage = usages.get(0);
                    int id = usage.getId();
                    logMetacat.debug("BookKeeperClient.deleteUsage - the book keeper service find the usage with id " + id + " matching the quota id " + quotaId + " and instance id " + instanceId);
                    String restStr = bookKeeperURL + USAGES + "/" + id;
                    logMetacat.debug("BookKeeperClient.deleteUsage - the delete rest command is " + restStr);
                    CloseableHttpResponse response = null;
                    try {
                        HttpDelete httpdelete = new HttpDelete(restStr);
                        response = httpClient.execute(httpdelete);
                        int status = response.getStatusLine().getStatusCode();
                        if (status == 200) {
                            boolean success = mapper.readValue(response.getEntity().getContent(), Boolean.class);
                            if (success) {
                                logMetacat.info("BookKeeperClient.deleteUsage - successfully delete the usage with id " + id);
                            } else {
                                throw new ServiceFailure("0000", "BookKeeperClient.deleteUsage - can't delete the usage with the id " + id);
                            }
                        } else {
                            String error = IOUtils.toString(response.getEntity().getContent());
                            throw new ServiceFailure("0000", "BookKeeperClient.deleteUsage - can't delete the usage with the id " + id + " since " + error);
                        }
                    } finally {
                        if (response != null) {
                            response.close();
                        }
                    }
                } else {
                    throw new ServiceFailure("0000", "BookKeeperClient.deleteUsage - the bookkeeper service should only send back one record with the given quota id "+ quotaId + " and instance id " + instanceId 
                            + ". However, it sent back more than one. Something is wrong in the bookkeeper service.");
                }
            } catch (NotFound e) {
                logMetacat.warn("BookKeeperClient.deleteUsage - the book keeper service don't find any usages " + e.getMessage());
            }
        } else {
            throw new InvalidRequest("0000", "The quota type and instance id can't be null or blank when you try to delete a usage.");
        }
    }
    
    
    /**
     * Get the list of usage from the book keeper service with the given quota id and instance id
     * @param quotaId  the quota id associated with the usage
     * @param instanceId  the instance id associated with the usage
     * @return the list of usages matching the query 
     * @throws ClientProtocolException
     * @throws IOException
     * @throws NotFound
     * @throws ServiceFailure
     */
    private List<Usage> listUsages(int quotaId, String instanceId) throws ClientProtocolException, IOException, NotFound, ServiceFailure {
        String restStr = bookKeeperURL + USAGES + "/?" + QUOTAID + "=" + quotaId + "&" + INSTNACEID + "=" + instanceId;
        logMetacat.debug("BookKeeperClient.getUsageId - the rest request to get the usuage id is " + restStr);
        HttpGet get = new HttpGet(restStr);
        get.addHeader(header);
        CloseableHttpResponse response = null;
        List<Usage> result = null;
        try {
            response = httpClient.execute(get);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                result = mapper.readValue(response.getEntity().getContent(), List.class);
            } else if (status == 404) {
                throw new NotFound("0000", "BookKeeperClient.getUsageId - the usage with the quota id " + quotaId + " and instance id " + instanceId + "is not found");
            } else {
                String error = IOUtils.toString(response.getEntity().getContent());
                throw new ServiceFailure("0000", "BookKeeperClient.getUsageId - quota service can't fulfill to list usages since " + error);
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return result;
    }
}
