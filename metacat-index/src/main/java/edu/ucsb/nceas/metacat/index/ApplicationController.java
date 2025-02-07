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
package edu.ucsb.nceas.metacat.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.dataone.configuration.Settings;
import org.dataone.service.exceptions.ServiceFailure;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ibm.icu.util.Calendar;

import edu.ucsb.nceas.metacat.common.SolrServerFactory;
import edu.ucsb.nceas.metacat.common.query.EnabledQueryEngines;

/**
 * The start class of the index.
 * @author tao
 *
 */
public class ApplicationController implements Runnable {
    
    private static long DEFAULTINTERVAL = 7200000;
    private static String SOLRINDEXES = "solrIndexes";
    private static short FIRST = 0;

    private List<SolrIndex> solrIndexes = null;
    private List<SystemMetadataEventListener> sysmetaListeners = new ArrayList<SystemMetadataEventListener>();
    private static ApplicationContext context = null;
    private String springConfigFile = "src/main/resources/index-processor-context.xml";
    private String metacatPropertiesFile = null;
    private static int waitingTime = IndexGenerator.WAITTIME;
    private static int maxAttempts = IndexGenerator.MAXWAITNUMBER;
    private static long period = DEFAULTINTERVAL;
    Log log = LogFactory.getLog(ApplicationController.class);
    
    
    /**
     * Constructor
     */
    /*public ApplicationController () throws Exception {
        init();
    }*/
    
    /**
     * Set the Spring configuration file and metacat.properties file
     * @param springConfigFile  the path of the Spring configuration file
     * @param metacatPropertyFile  the path of the metacat.properties file
     */
    public ApplicationController(String springConfigFile, String metacatPropertiesFile) throws Exception {
        this.springConfigFile = springConfigFile;
        this.metacatPropertiesFile = metacatPropertiesFile;
        //init();
    }
    
    /**
     * Loads the metacat.prioerties into D1 Settings utility
     * this gives us access to all metacat properties as well as 
     * overriding any properties as needed. 
     * Note: in the junit test, we are using the test.properties rather than this properties file.
     * 
     * Makes sure shared Hazelcast configuration file location is set
     */
    private void initializeSharedConfiguration() {
        int times = 0;
        boolean foundProperty = false;
        while(true) {
            File metacatProperties = new File(metacatPropertiesFile);
            if(metacatProperties.exists()) {
                foundProperty = true;
                break;
            } else {
                try {
                        Thread.sleep(waitingTime);
                } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                        e.printStackTrace();
                }
            }
            times++;
            if(times >= maxAttempts) {
                log.error("ApplicationController.initialzeSharedConfiguration - MetacatIndex wait a while and still can't find the metacat.properties file.");
                break;//we still break the while loop and continue. But the properties in the metacat.properties can't be read.
            }
                
        }
        
        try {
            Settings.getConfiguration();
            Settings.augmentConfiguration(metacatPropertiesFile);
        } catch (ConfigurationException e) {
            log.error("Could not initialize shared Metacat properties. " + e.getMessage(), e);
        }
        
        // make sure hazelcast configuration is defined so that
        String hzConfigFileName = Settings.getConfiguration().getString("dataone.hazelcast.configFilePath");
        if (hzConfigFileName == null) {
            // use default metacat hazelcast.xml file in metacat deployment
            hzConfigFileName = 
                    Settings.getConfiguration().getString("application.deployDir") +
                    "/" +
                    Settings.getConfiguration().getString("application.context") + 
                    "/WEB-INF/hazelcast.xml";
            // set it for other parts of the code
            Settings.getConfiguration().setProperty("dataone.hazelcast.configFilePath", hzConfigFileName);
            //set data.hazelcast.location.clientconfig. This property will be used in d1_cn_index_processor module.
            //if we don't set this property, d1_cn_index_processor will use the default location /etc/dataone/storage.
            Settings.getConfiguration().setProperty("dataone.hazelcast.location.clientconfig", hzConfigFileName);
        }
        if(foundProperty) {
            period = Settings.getConfiguration().getLong("index.regenerate.interval");
        }
        
    }
    
    /**
     * Initialize the list of the SolrIndex objects from the configuration file.
     * Set the SolrServer implementation using the factory.
     */
    public void initialize() throws Exception {
        context = getContext();
        solrIndexes = (List<SolrIndex>) context.getBean(SOLRINDEXES);
        
        // use factory to create the correct impl
    	SolrServer solrServer = null;
		try {
			solrServer = SolrServerFactory.createSolrServer();
		} catch (Exception e) {
			log.error("Could not create SolrServer form factory", e);
			throw e;
		}

        // start the SystemMetadata listener[s] (only expect there to be one)
        for (SolrIndex solrIndex: solrIndexes) {
        	// set the solr server to use
			solrIndex.setSolrServer(solrServer);
			
			// start listening for events
        	SystemMetadataEventListener smel = new SystemMetadataEventListener();
        	smel.setSolrIndex(solrIndex);
        	sysmetaListeners.add(smel);
        	//smel.start();
        }
        
    }
    
    /**
     * Get the ApplicaionContext of Spring.
     */
    private ApplicationContext getContext() {
        if (context == null) {
            context = new FileSystemXmlApplicationContext(springConfigFile);
        }
        return context;
    }

    /**
     * Get the path of the Spring configuration file.
     * @return the path of the Spring configuration file.
     */
    public String getSpringConfigFile() {
        return this.springConfigFile;
    }
    
    /**
     * Get the list of the solr index.
     * @return the list of the solr index.
     */
    public List<SolrIndex> getSolrIndexes() {
        return this.solrIndexes;
    }
    
    
    /**
     * Start to generate indexes for those haven't been indexed in another thread.
     * It will create a timer to run this task periodically. 
     * If the property of "index.regenerate.interval" is less than 0, the thread would NOT run.
     */
    private void startIndex() {
        if(period > 0) {
            SolrIndex index = solrIndexes.get(FIRST);
            //SystemMetadataEventListener listener = sysmetaListeners.get(FIRST);
            IndexGenerator generator = new IndexGenerator(index);
            //Thread indexThread = new Thread(generator);
            //indexThread.start();
            Timer indexTimer = new Timer();
            //indexTimer.scheduleAtFixedRate(generator, Calendar.getInstance().getTime(), period);
            indexTimer.schedule(generator, Calendar.getInstance().getTime(), period);
        }
        
    }
    
    /**
     * Start the system metadata listener. Prior to call this method, we should call
     * initialize method first.
     * @throws ServiceFailure 
     * @throws FileNotFoundException 
     */
    public void startSysmetaListener() throws FileNotFoundException, ServiceFailure {
        if(sysmetaListeners != null) {
            //only expects one listener.
            for(SystemMetadataEventListener listener : sysmetaListeners) {
                if(listener != null) {
                    listener.start();
                }
            }
        }
    }
    
    /**
     * It will initialize the shared metacat.properties and the SolrIndex, then start system metadata event listener and 
     *  generate indexes for those haven't been indexed in another thread. This method is for the MetacatIndexServlet.
     *  The reason we put those methods (init, startSysmetaListener, startIndex)in the run (another thread) is that the waiting readiness of the metacat wouldn't 
     *  block the start of the servlet container (e.g., tomcat).
     */
    public void run() {
        initializeSharedConfiguration();
        try {
            boolean isSolrEnabled = true;
            try {
               isSolrEnabled = EnabledQueryEngines.getInstance().isEnabled(EnabledQueryEngines.SOLRENGINE);
            } catch (Exception e) {
                log.error("ApplicationController.run - Metacat-index can't read the enabled query engine list from metacat.properties :"+e.getMessage());
            }
            //if the solr query engine is disabled, we stop here.
            if(!isSolrEnabled) {
                return;
            }
            initialize();
            startSysmetaListener();
            startIndex();//it will create another thread.
        } catch (Exception e) {
            log.error("Application.run "+e.getMessage());
        }
        
    }
}
