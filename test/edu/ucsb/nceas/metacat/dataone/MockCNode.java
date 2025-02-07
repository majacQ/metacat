/**
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright ${year}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.ucsb.nceas.metacat.dataone;

import org.dataone.client.CNode;
import org.dataone.service.exceptions.IdentifierNotUnique;
import org.dataone.service.exceptions.InvalidRequest;
import org.dataone.service.exceptions.InvalidToken;
import org.dataone.service.exceptions.NotAuthorized;
import org.dataone.service.exceptions.NotFound;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.Identifier;
import org.dataone.service.types.v1.Session;
import org.dataone.service.types.v1.Subject;
import org.dataone.service.types.v1.SystemMetadata;

/**
 * MockCNode mimics a DataONE Coordinating Node, and should be used only for testing
 * when there is a dependency on CN services
 */
public class MockCNode extends CNode {

    /**
     * See superclass for documentation
     */
    public MockCNode() {
    	super(null);
    }
    
    /**
     * No records exist in the Mock CNode - indicates such
     */
    @Override
    public SystemMetadata getSystemMetadata(Session session, Identifier pid)
        throws InvalidToken, NotImplemented, ServiceFailure, NotAuthorized, NotFound {
        throw new NotFound("0000", "MockCNode does not contain any records");
    }
    
    /**
     * Always return true that the reservation exists
     */
    @Override
    public boolean hasReservation(Session session, Subject subject, Identifier pid) 
    	throws InvalidToken, ServiceFailure, NotFound,
        NotAuthorized, IdentifierNotUnique, NotImplemented {
    	// always return true
        return true;
    }
    
    /**
     * we only want to test against ourselves
     */
    @Override
    public String lookupNodeBaseUrl(String nodeId) throws ServiceFailure, NotImplemented {

    	try {
			return MNodeService.getInstance(null).getCapabilities().getBaseURL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}
