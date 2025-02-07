<%@ page language="java" %>
<%@ page import="java.util.Set,java.util.Map,java.util.Vector,edu.ucsb.nceas.utilities.PropertiesMetaData" %>
<%@ page import="edu.ucsb.nceas.utilities.MetaDataGroup,edu.ucsb.nceas.utilities.MetaDataProperty" %>
<% 
/**
 *  '$RCSfile$'
 *    Copyright: 2008 Regents of the University of California and the
 *               National Center for Ecological Analysis and Synthesis
 *  For Details: http://www.nceas.ucsb.edu/
 *
 *   '$Author$'
 *     '$Date$'
 * '$Revision$'
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
%>

<html>
<head>

<title>DataONE Configuration</title>
<link rel="stylesheet" type="text/css" 
        href="<%= request.getContextPath() %>/admin/admin.css"></link>
<script language="JavaScript" type="text/JavaScript" src="<%= request.getContextPath() %>/admin/admin.js"></script>

</head>
<body>
<%@ include file="./header-section.jsp"%>

<img src="<%= request.getContextPath() %>/metacat-logo.png" width="100px" align="right"/> 
<h2>DataONE Configuration</h2>

<p> 
    DataONE configuration is optional; choose 'Skip' below to proceed to the next configuration section,
    unless you want to join the DataONE federation.
</p>
<p>
	<a href="http://www.dataone.org" target="_D1">DataONE</a> is a federation of data repositories (called Member Nodes)
    that work together to create a seamless, interoperable network to store, preserve, discover, and publish scientific data.
	Becoming a DataONE Member Node is a means of distributing data and metadata using a common set of standards, making it
	easy to build software systems that can interact with any of the federated repository nodes.  Metacat can be used
	to join the DataONE federation by configuring Metacat to act as a Member Node and applying with DataONE to become a
	Member Node.  Once a node's application is approved and Metacat is configured, metadata will be synchronized in the 
	network for easy discovery, and replicas of data and metadata can be housed on other Member Nodes for 
	archival safe-keeping. In addition, a Metacat Member Node can optionally provide space to store replicas of data from 
	other Member Nodes that wish to preserve their data in the federation.
<p/>
<p> To configure Metacat as a Member Node, you must first obtain a Member Node X.509 certificate that will
    be used to authenticate this node, and you must have a registered and verified account with DataONE that can
    be used as the point of contact for the candidate Member Node.  Once you have this information, fill out
    the form below with the proper configuration information, and then click 'Register', which will send the
    registration data to DataONE. A unique Member Node ID will be assigned and should only be used for this 
    node and never shared or altered.
<p/>
<br clear="right"/>

<%@ include file="page-message-section.jsp"%>

<form method="POST" name="configuration_form" action="<%= request.getContextPath() %>/admin" 
                                        onsubmit="return submitForm(this);">

	<h3>Member Node Services</h3>
	Enable or disable DataONE Member Node Services for this deployment
	
	<hr class="config-line">
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label"><label for="dataone.mn.services.enabled" title="Enable DataONE Member Node Services">Enable DataONE Services</label></div>

		<%
		
		boolean enableServices = false;
		String enableServicesString = (String) request.getAttribute("dataone.mn.services.enabled");
		if (enableServicesString != null) {
			enableServices = Boolean.parseBoolean(enableServicesString);
		}
		
		if (enableServices) { 
		%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.mn.services.enabled" 
				name="dataone.mn.services.enabled" 	             		    	    	           		    	             			
				value="true"
				checked="checked"/>
		<% } else {%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.mn.services.enabled" 
				name="dataone.mn.services.enabled" 	             		    	    	           		    	             			
				value="true"/>
		<% } %>

	</div>

	<h3>Member Node Configuration</h3>
	General information identifiying this node, its owner, and contents.  You will need
	   a certificate to identify the node, and a DataONE account to act as a node contact.
	
	<hr class="config-line">
	
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeName" title="A short, human-readable name for this node">Node Name</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeName" 
			name="dataone.nodeName" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeName") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeDescription" title="A brief description of the node and its holdings">Node Description</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeDescription" 
			name="dataone.nodeDescription" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeDescription") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeId" title="The DataONE-assigned unique identifier for this node">Node Identifier</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeId" 
			name="dataone.nodeId" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeId") %>"/>
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.subject" title="The account in Distinguished Name (DN) format that represents this node in all service interactions">Node Subject</label>
		</div>
		<input class="textinput" 
			id="dataone.subject" 
			name="dataone.subject" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.subject") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.contactSubject" title="A verified account in DN format to be used as the primary node contact for this node">Contact Subject</label>
		</div>
		<input class="textinput" 
			id="dataone.contactSubject" 
			name="dataone.contactSubject" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.contactSubject") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="D1Client.certificate.file" title="The absolute path to the X.509 certificate used to authenticate this node">Node Certificate Path</label>
		</div>
		<input class="textinput" 
			id="D1Client.certificate.file" 
			name="D1Client.certificate.file" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("D1Client.certificate.file") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label"><label for="dataone.nodeSynchronize" title="Enable DataONE metadata synchronization">Enable Metadata Synchronization</label></div>

		<%
		
		boolean synchronize = false;
		String nodeSynchronize = (String) request.getAttribute("dataone.nodeSynchronize");
		if (nodeSynchronize != null) {
			synchronize = Boolean.parseBoolean(nodeSynchronize);
		}
		
		if (synchronize) { 
		%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.nodeSynchronize" 
				name="dataone.nodeSynchronize" 	             		    	    	           		    	             			
				value="true"
				checked="checked"/>
		<% } else {%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.nodeSynchronize" 
				name="dataone.nodeSynchronize" 	             		    	    	           		    	             			
				value="true"/>
		<% } %>

	</div>
    <p>&nbsp;</p>
    
	<h3>Synchronization Schedule</h3>
	The schedule on which metadata should be synchronized with DataONE, expressed as a crontab entry.
	<hr class="config-line">
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.year" title="Year">Year</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.year" 
			name="dataone.nodeSynchronization.schedule.year" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.year") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.mon" title="Month">Month</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.mon" 
			name="dataone.nodeSynchronization.schedule.mon" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.mon") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.mday" title="Day of Month">Day of Month</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.mday" 
			name="dataone.nodeSynchronization.schedule.mday" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.mday") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.wday" title="Day of Week">Day of Week</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.wday" 
			name="dataone.nodeSynchronization.schedule.wday" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.wday") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.hour" title="Hours">Hours</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.hour" 
			name="dataone.nodeSynchronization.schedule.hour" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.hour") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.min" title="Minutes">Minutes</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.min" 
			name="dataone.nodeSynchronization.schedule.min" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.min") %>"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.nodeSynchronization.schedule.sec" title="Seconds">Seconds</label>
		</div>
		<input class="textinput" 
			id="dataone.nodeSynchronization.schedule.sec" 
			name="dataone.nodeSynchronization.schedule.sec" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.nodeSynchronization.schedule.sec") %>"/> 
	</div>

	<h3>Replication</h3>
	Configuration for replication, including whether this node can be used to house replicas of
	objects from other nodes, as well as default replication policies for the objects originating
	on this Member Node.
	<hr class="config-line">
	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label"><label for="dataone.nodeReplicate" title="Store replicas from other Member Nodes">Accept and Store Replicas</label></div>

		<%
		boolean replicate = false;
		String nodeReplicate = (String) request.getAttribute("dataone.nodeReplicate");
		if (nodeReplicate != null) {
			replicate = Boolean.parseBoolean(nodeReplicate);
		}
		if (replicate) { 
		%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.nodeReplicate" 
				name="dataone.nodeReplicate" 	             		    	    	           		    	             			
				value="true"
				checked="checked"/>
		<% } else {%>
		<input type="checkbox" 
				class="textinput" 
				id="dataone.nodeReplicate" 
				name="dataone.nodeReplicate" 	             		    	    	           		    	             			
				value="true"/>
		<% } %>
	</div>
	
	<hr class="config-line">

 	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.replicationpolicy.default.numreplicas" title="Default Number of Replicas to be created for local objects">Default Number of Replicas</label>
		</div>
		<input class="textinput" 
			id="dataone.replicationpolicy.default.numreplicas" 
			name="dataone.replicationpolicy.default.numreplicas" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.replicationpolicy.default.numreplicas") %>"/> 
	</div>
  	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.replicationpolicy.default.preferredNodeList" title="A comma-separated list of preferred nodes to house replicas">Default Preferred Nodes</label>
		</div>
		<input class="textinput" 
			id="dataone.replicationpolicy.default.preferredNodeList" 
			name="dataone.replicationpolicy.default.preferredNodeList" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.replicationpolicy.default.preferredNodeList") %>"/> 
	</div>
   	<div class="form-row">
		<img class="question-mark" src="style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/dataone.html#configuring-metacat-as-a-member-node')"/>
		<div class="textinput-label">
			<label for="dataone.replicationpolicy.default.blockedNodeList" title="A comma-separated list of blocked nodes never to house replicas">Default Blocked Nodes</label>
		</div>
		<input class="textinput" 
			id="dataone.replicationpolicy.default.blockedNodeList" 
			name="dataone.replicationpolicy.default.blockedNodeList" 	             		    	    	           		    	             			
			value="<%= request.getAttribute("dataone.replicationpolicy.default.blockedNodeList") %>"/> 
	</div>
 
	<input type="hidden" name="configureType" value="dataone"/>
	<input type="hidden" name="processForm" value="true"/>

	<%
		// do we know if it is an update or a new registration?
		boolean isUpdate = false;
		String isUpdateString = (String) request.getAttribute("dataone.isUpdate");
		if (isUpdateString != null) {
			isUpdate = Boolean.parseBoolean(isUpdateString);
		}
		// do we know if it is pending approval?
		boolean isSubmitted = false;
		String isSubmittedString = (String) request.getAttribute("dataone.mn.registration.submitted");
		if (isSubmittedString != null) {
			isSubmitted = Boolean.parseBoolean(isSubmittedString);
		}
	%>
	<%if (isUpdate) { %>
		<input class=button type="submit" value="Update"/>
	<%} else if (isSubmitted) { %>
		<input class=button type="submit" value="Update" disabled="disabled"/>
	<%} else { %>
		<input class=button type="submit" value="Register"/>
	<%} %>
	<%
		// if have we already configured this section, then we cannot skip it
		boolean previouslyConfigured = false;
		String previouslyConfiguredString = (String) request.getAttribute("configutil.dataoneConfigured");
		if (previouslyConfiguredString != null) {
			previouslyConfigured = Boolean.parseBoolean(previouslyConfiguredString);
		}
	%>
	<%if (!previouslyConfigured) { %>
		<input class=button type="button" value="Skip" onClick="forward('./admin?configureType=dataone&bypass=true&processForm=true')">
	<%} %>
	<input class=button type="button" value="Cancel" onClick="forward('./admin')"> 

</form>

<%@ include file="./footer-section.jsp"%>

</body>
</html>
