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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.dataone.bookkeeper.api.Usage;

import edu.ucsb.nceas.MCTestCase;
import edu.ucsb.nceas.metacat.database.DBConnection;
import edu.ucsb.nceas.metacat.database.DBConnectionPool;
import junit.framework.Test;
import junit.framework.TestSuite;

public class QuotaDBManagerTest  extends MCTestCase {
    
    /**
     * Constructor
     * @param name  name of method will be tested
     */
    public QuotaDBManagerTest(String name) {
        super(name);
    }
    
    /**
     * Create a suite of tests to be run together
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new QuotaDBManagerTest("testCreateUsage"));
        suite.addTest(new QuotaDBManagerTest("testCreateUsage2"));
        suite.addTest(new QuotaDBManagerTest("testGetUnReportedUsagesAndSetReportDate"));
        suite.addTest(new QuotaDBManagerTest("testLookupRemoteUsageId"));
        return suite;
    }
    
    /**
     * Test the createUsage method
     */
    public void testCreateUsage() throws Exception {
        //create a usage with the report date
        int quotaId =  (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue() +  (new Double (Math.random() * 100000000)).intValue();
        String instanceId = "testcreateusage" + System.currentTimeMillis() + Math.random() * 10000;
        double quantity = 1;
        Usage usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        Date now = new Date();
        QuotaDBManager.createUsage(usage, now);
        ResultSet rs = getResultSet(quotaId, instanceId);
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) > 0);
        assertTrue(rs.getInt(2) == quotaId);
        assertTrue(rs.getString(3).equals(instanceId));
        assertTrue(rs.getDouble(4) == quantity);
        assertTrue(rs.getTimestamp(5).compareTo((new Timestamp(now.getTime()))) == 0);
        assertTrue(rs.getString(6).equals(QuotaServiceManager.ACTIVE));
        assertTrue(rs.getString(8).equals(QuotaService.nodeId));
        assertTrue(rs.getString(9) == null);
        assertTrue(rs.getString(10) == null);
        assertTrue(rs.getString(11) == null);
        rs.close();
        
        
        //create a usage without the report date
        quotaId =  (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue();
        instanceId = "testcreateusage" + System.currentTimeMillis() + Math.random() *10;
        quantity = 100.11;
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        rs = getResultSet(quotaId, instanceId);
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) > 0);
        assertTrue(rs.getInt(2) == quotaId);
        assertTrue(rs.getString(3).equals(instanceId));
        assertTrue(rs.getDouble(4) == quantity);
        assertTrue(rs.getTimestamp(5) == null);
        assertTrue(rs.getString(6).equals(QuotaServiceManager.ACTIVE));
        assertTrue(rs.getString(8).equals(QuotaService.nodeId));
        assertTrue(rs.getString(9) == null);
        assertTrue(rs.getString(10) == null);
        assertTrue(rs.getString(11) == null);
        rs.close();
        
        //create another unreported event
        quotaId =  (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue();
        instanceId = "testcreateusage" + System.currentTimeMillis() + Math.random() *10;
        quantity = 100.11;
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        

    }
    
    /**
     * Test the getUnReportedUsages and setReportDate method.
     * @throws Exception
     */
    public void testGetUnReportedUsagesAndSetReportDate() throws Exception {
        ResultSet rs = QuotaDBManager.getUnReportedUsages();
        LocalUsage usage = new LocalUsage();
        int index = 0;
        int previousUsageId = -1;
        while (rs.next() && index < 500) {
            int usageId = rs.getInt(1);
            System.out.println("the usage id is " + usageId);
            assertTrue(usageId > previousUsageId); //make sure the result set is ordered by the column usage_id  acs
            usage.setLocalId(usageId);
            usage.setQuotaId(rs.getInt(2));
            usage.setInstanceId(rs.getString(3));
            usage.setQuantity(rs.getDouble(4));
            previousUsageId = usageId;
            index++;
        }
        assertTrue(index > 0);
        rs.close();
        
        int usageId = usage.getLocalId();
        rs = getResultSet(usageId);
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) == usageId);
        assertTrue(rs.getTimestamp(5) == null);
        rs.close();
        
        Date now = new Date();
        int remoteId = (new Double (Math.random() * 100000000)).intValue();
        QuotaDBManager.setReportedDateAndRemoteId(usageId, now, remoteId);
        rs = getResultSet(usageId);
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) == usageId);
        assertTrue(rs.getTimestamp(5).compareTo((new Timestamp(now.getTime()))) == 0);
        assertTrue(rs.getInt(7) == remoteId);
        assertTrue(rs.getString(8).equals(QuotaService.nodeId));
        rs.close();
    }
    
    /**
     * Test the lookupRemoteUsageId method.
     * @throws Exception
     */
    public void testLookupRemoteUsageId() throws Exception {
        //create a usage with the report date
        int quotaId =  (new Double (Math.random() * 100000000)).intValue() + (new Double (Math.random() * 100000000)).intValue() +  (new Double (Math.random() * 100000000)).intValue();
        String instanceId = "testcreateusage" + System.currentTimeMillis() + Math.random() * 10000;
        int remoteId = (new Double (Math.random() * 100000000)).intValue();
        double quantity = 1;
        Usage usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        usage.setId(remoteId);
        Date now = new Date();
        QuotaDBManager.createUsage(usage, now);
        
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.INACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        usage.setId(remoteId);
        now = new Date();
        QuotaDBManager.createUsage(usage, now);
        
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.DELETED);
        usage.setNodeId(QuotaService.nodeId);
        usage.setId(remoteId);
        now = new Date();
        QuotaDBManager.createUsage(usage, now);
        
        int remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(quotaId, instanceId);
        assertTrue(remotedIdFromDB == remoteId);
        
        //test the case - there is no remote id saved locally
        String instanceId2 = "testcreateusage2" + System.currentTimeMillis() + Math.random() * 10000;
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId2);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId2);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.INACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId2);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.DELETED);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(quotaId, instanceId2);
        assertTrue(remotedIdFromDB == BookKeeperClient.DEFAULT_REMOTE_USAGE_ID);
        
        // test the case - mixture of the records with/without remote id
        String instanceId3 = "testcreateusage2" + System.currentTimeMillis() + Math.random() * 10000;
        remoteId = (new Double (Math.random() * 100000000)).intValue();
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.INACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        usage.setId(remoteId);
        QuotaDBManager.createUsage(usage, now);
        usage = new Usage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setQuotaId(quotaId);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.DELETED);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(quotaId, instanceId3);
        assertTrue(remotedIdFromDB == remoteId);
    }
    
    /**
     * Test the createUsage method for cases which don't quota id, instead, they have subscriber and quota type
     * @throws Exception
     */
    public void testCreateUsage2() throws Exception {
        String subscriber = "subscriber-" + (new Double (Math.random() * 100000000)).intValue();
        String requestor = "requestor-" + (new Double (Math.random() * 100000000)).intValue();
        String instanceId3 = "testcreateusage3" + System.currentTimeMillis() + Math.random() * 10000;
        double quantity = 1;
        LocalUsage usage = new LocalUsage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setSubscriber(subscriber);
        usage.setRequestor(requestor);
        usage.setQuotaType(QuotaTypeDeterminer.PORTAL);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.ACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        int remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(QuotaService.DEFAULT_QUOTA_ID, instanceId3);
        assertTrue(remotedIdFromDB == BookKeeperClient.DEFAULT_REMOTE_USAGE_ID);
        ResultSet rs = getResultSet(subscriber, QuotaTypeDeterminer.PORTAL, instanceId3);
        assertTrue(rs.next());
        assertTrue(rs.getInt(1) > 0);
        assertTrue(rs.getInt(2) == 0);
        assertTrue(rs.getString(3).equals(instanceId3));
        assertTrue(rs.getDouble(4) == quantity);
        assertTrue(rs.getTimestamp(5) == null);
        assertTrue(rs.getString(6).equals(QuotaServiceManager.ACTIVE));
        assertTrue(rs.getString(8).equals(QuotaService.nodeId));
        assertTrue(rs.getString(9).equals(subscriber));
        assertTrue(rs.getString(10).equals(QuotaTypeDeterminer.PORTAL));
        assertTrue(rs.getString(11).equals(requestor));
        rs.close();
        
        usage = new LocalUsage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setSubscriber(subscriber);
        usage.setRequestor(requestor);
        usage.setQuotaType(QuotaTypeDeterminer.PORTAL);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.INACTIVE);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(QuotaService.DEFAULT_QUOTA_ID, instanceId3);
        assertTrue(remotedIdFromDB == BookKeeperClient.DEFAULT_REMOTE_USAGE_ID);
        
        usage = new LocalUsage();
        usage.setObject(QuotaServiceManager.USAGE);
        usage.setSubscriber(subscriber);
        usage.setRequestor(requestor);
        usage.setQuotaType(QuotaTypeDeterminer.PORTAL);
        usage.setInstanceId(instanceId3);
        usage.setQuantity(quantity);
        usage.setStatus(QuotaServiceManager.DELETED);
        usage.setNodeId(QuotaService.nodeId);
        QuotaDBManager.createUsage(usage, null);
        remotedIdFromDB = QuotaDBManager.lookupRemoteUsageId(QuotaService.DEFAULT_QUOTA_ID, instanceId3);
        assertTrue(remotedIdFromDB == BookKeeperClient.DEFAULT_REMOTE_USAGE_ID);
    }
    
    /**
     * Get the result set from a query matching the given quota id and instance id.
     * @param quotaId  the quota id in the query
     * @param instanceId  the instance id in the query
     * @return the result set after executing the query
     * @throws Exception
     */
     static ResultSet getResultSet(int quotaId, String instanceId) throws Exception {
        DBConnection dbConn = null;
        int serialNumber = -1;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            dbConn = DBConnectionPool.getDBConnection("QuotaDBManager.getUnReportedUsages");
            serialNumber = dbConn.getCheckOutSerialNumber();
            String query = "select " + QuotaDBManager.USAGELOCALID + ", " + QuotaDBManager.QUOTAID + "," + QuotaDBManager.INSTANCEID + ", " + QuotaDBManager.QUANTITY + "," + QuotaDBManager.DATEREPORTED + "," 
                                     + QuotaDBManager.STATUS + "," + QuotaDBManager.USAGEREMOTEID + "," + QuotaDBManager.NODEID + "," + QuotaDBManager.QUOTASUBJECT + "," + QuotaDBManager.QUOTATYPE + "," + QuotaDBManager.REQUESTOR
                                     + " from " + QuotaDBManager.TABLE + " where " + QuotaDBManager.QUOTAID + "=? AND " + QuotaDBManager.INSTANCEID  + "=?" ;
            stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, quotaId);
            stmt.setString(2, instanceId);
            rs = stmt.executeQuery();
        } finally {
            DBConnectionPool.returnDBConnection(dbConn, serialNumber);
        }
        return rs;
    }
     
     /**
      * Get the result set from a query matching the given quota id and instance id.
      * @param subscriber  the subscriber of the quota
      * @param quotaType  the type of the quota
      * @param instanceId  the instance id in the query
      * @return the result set after executing the query
      * @throws Exception
      */
      static ResultSet getResultSet(String subscriber, String quotaType, String instanceId) throws Exception {
         DBConnection dbConn = null;
         int serialNumber = -1;
         PreparedStatement stmt = null;
         ResultSet rs = null;
         try {
             dbConn = DBConnectionPool.getDBConnection("QuotaDBManager.getUnReportedUsages");
             serialNumber = dbConn.getCheckOutSerialNumber();
             String query = "select " + QuotaDBManager.USAGELOCALID + ", " + QuotaDBManager.QUOTAID + "," + QuotaDBManager.INSTANCEID + ", " + QuotaDBManager.QUANTITY + "," + QuotaDBManager.DATEREPORTED + "," 
                                      + QuotaDBManager.STATUS + "," + QuotaDBManager.USAGEREMOTEID + "," + QuotaDBManager.NODEID + "," + QuotaDBManager.QUOTASUBJECT + "," + QuotaDBManager.QUOTATYPE + "," + QuotaDBManager.REQUESTOR
                                      + " from " + QuotaDBManager.TABLE + " where " + QuotaDBManager.QUOTASUBJECT + "=? AND " + QuotaDBManager.INSTANCEID  + "=? AND " + QuotaDBManager.QUOTATYPE + "=?" ;
             stmt = dbConn.prepareStatement(query);
             stmt.setString(1, subscriber);
             stmt.setString(2, instanceId);
             stmt.setString(3, quotaType);
             rs = stmt.executeQuery();
         } finally {
             DBConnectionPool.returnDBConnection(dbConn, serialNumber);
         }
         return rs;
     }
    
    /**
     * Get the result set from a query matching the given usage id
     * @param usageId  the id of usage needs to be matched
     * @return the result set after executing the query
     * @throws Exception
     */
    private ResultSet getResultSet(int usageId) throws Exception {
        DBConnection dbConn = null;
        int serialNumber = -1;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            dbConn = DBConnectionPool.getDBConnection("QuotaDBManager.getUnReportedUsages");
            serialNumber = dbConn.getCheckOutSerialNumber();
            String query = "select " + QuotaDBManager.USAGELOCALID + ", " + QuotaDBManager.QUOTAID + "," + QuotaDBManager.INSTANCEID + ", " + QuotaDBManager.QUANTITY + "," + QuotaDBManager.DATEREPORTED + "," + QuotaDBManager.STATUS + "," + QuotaDBManager.USAGEREMOTEID + "," + QuotaDBManager.NODEID + " from " + QuotaDBManager.TABLE + " where " + 
                                            QuotaDBManager.USAGELOCALID + "=?";
            stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, usageId);
            rs = stmt.executeQuery();
        } finally {
            DBConnectionPool.returnDBConnection(dbConn, serialNumber);
        }
        return rs;
    }

}
