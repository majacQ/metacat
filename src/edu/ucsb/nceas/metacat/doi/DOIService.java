/**
 *  Copyright: 2021 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
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
package edu.ucsb.nceas.metacat.doi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.service.exceptions.IdentifierNotUnique;
import org.dataone.service.exceptions.InsufficientResources;
import org.dataone.service.exceptions.InvalidRequest;
import org.dataone.service.exceptions.InvalidSystemMetadata;
import org.dataone.service.exceptions.InvalidToken;
import org.dataone.service.exceptions.NotAuthorized;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.exceptions.UnsupportedType;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.Session;
import org.dataone.service.types.v2.SystemMetadata;

import edu.ucsb.nceas.metacat.properties.PropertyService;
import edu.ucsb.nceas.metacat.util.SystemUtil;
import edu.ucsb.nceas.utilities.PropertyNotFoundException;

/**
 * An abstract class for the DOI service
 * @author tao
 */
public abstract class DOIService {
    private static Log logMetacat = LogFactory.getLog(DOIService.class);
    protected static boolean doiEnabled = false;
    protected static String serviceBaseUrl = null;
    protected static String username = null;
    protected static String password = null;
    protected static String uriTemplate = null;
    protected static boolean autoPublishDOI = true;
    
    /**
     * Constructor
     */
    public DOIService() {
        try {
            doiEnabled = new Boolean(PropertyService.getProperty("guid.doi.enabled")).booleanValue();
            serviceBaseUrl = PropertyService.getProperty("guid.doi.baseurl");
            username = PropertyService.getProperty("guid.doi.username");
            password = PropertyService.getProperty("guid.doi.password");
            autoPublishDOI = (new Boolean(PropertyService.getProperty("guid.doi.autoPublish"))).booleanValue();
            uriTemplate = PropertyService.getProperty("guid.doi.uritemplate.metadata");
          
        } catch (PropertyNotFoundException e) {
            logMetacat.error("DOIService.constructor - we can't get the value of the property:", e);
        }
    }
    
    /**
     * Refresh the status (enable or disable) of the DOI service from property file
     * @throws PropertyNotFoundException 
     */
    public void refreshStatus() throws PropertyNotFoundException {
        doiEnabled = new Boolean(PropertyService.getProperty("guid.doi.enabled")).booleanValue();
        autoPublishDOI = (new Boolean(PropertyService.getProperty("guid.doi.autoPublish"))).booleanValue();
    }
    
    /**
     * Get the landing page url string for the given identifier
     * @param identifier  the identifier which associates the landing page
     * @return the url of the landing page
     */
    protected String getLandingPage(Identifier identifier) {
        String siteUrl = null;
        try {
            if (uriTemplate != null) {
                siteUrl =  SystemUtil.getSecureServerURL() + uriTemplate.replaceAll("<IDENTIFIER>", identifier.getValue());
            } else {
                siteUrl =  SystemUtil.getContextURL() + "/d1/mn/v2/object/" + identifier.getValue();
            }
        } catch (PropertyNotFoundException e) {
            logMetacat.warn("DOIService.getLandingPage - No target URI template found in the configuration for: " + e.getMessage());
        }
        logMetacat.warn("DOIService.getLandingPage - the landing page url is: " + siteUrl);
        return siteUrl;
    }
    
    /**
     * Submits DOI metadata information about the object to DOI services
     * @param sysMeta
     * @return true if succeeded; false otherwise.
     * @throws InvalidRequest
     * @throws DOIException
     * @throws NotImplemented
     * @throws ServiceFailure
     * @throws InterruptedException
     * @throws InvalidToken
     * @throws NotAuthorized
     * @throws NotFound
     */
    public abstract boolean registerDOI(SystemMetadata sysMeta) throws InvalidRequest, DOIException, NotImplemented, 
                                                                ServiceFailure, InterruptedException, InvalidToken, NotAuthorized, NotFound;

    /**
     * Generate a DOI using the DOI service as configured
     * @return  the identifier which was minted by the DOI service
     * @throws DOIException
     * @throws InvalidRequest
     */
    public abstract Identifier generateDOI() throws DOIException, InvalidRequest;
    
    /**
     * Make the status of the identifier to be public 
     * @param session  the subjects call the method
     * @param identifer  the identifier of the object which will be published.
     * @param session
     * @param identifier
     * @throws InvalidToken
     * @throws ServiceFailure
     * @throws NotAuthorized
     * @throws NotImplemented
     * @throws InvalidRequest
     * @throws NotFound
     * @throws IdentifierNotUnique
     * @throws UnsupportedType
     * @throws InsufficientResources
     * @throws InvalidSystemMetadata
     * @throws DOIException
     */
    public abstract void publishIdentifier(Session session, Identifier identifier) throws InvalidToken, 
    ServiceFailure, NotAuthorized, NotImplemented, InvalidRequest, NotFound, IdentifierNotUnique, 
    UnsupportedType, InsufficientResources, InvalidSystemMetadata, DOIException;
    
}
