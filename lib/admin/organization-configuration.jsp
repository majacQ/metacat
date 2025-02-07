<%@ page language="java"%>
<%@ page import="java.util.Set,java.util.Map,java.util.Vector,edu.ucsb.nceas.utilities.*" %>
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

<%
	PropertiesMetaData metadata = (PropertiesMetaData)request.getAttribute("metadata");
 	Vector<String> ldapOrganizations = 
 		(Vector<String>)request.getAttribute("orgList"); 
%>

<html>
<head>

<title>Organization Configuration</title>
<link rel="stylesheet" type="text/css" 
        href="<%= request.getContextPath() %>/admin/admin.css"></link>
<script language="JavaScript" type="text/JavaScript" src="<%= request.getContextPath() %>/admin/admin.js"></script>

</head>
<body>
<img src="<%= request.getContextPath() %>/metacat-logo.png" width="100px" align="right"/> 
<h2>Organization Configuration</h2>
Enter organization specific properties here. 
<br class="ldap-header">

<%@ include file="./page-message-section.jsp"%>


<form method="POST" name="configuration_form" action="<%= request.getContextPath() %>/admin" 
                                        onsubmit="return submitForm(this);">
<%
	if (metadata != null) {
		// each group describes a section of properties
		Map<Integer, MetaDataGroup> groupMap = metadata.getGroups();
		Set<Integer> groupIdSet = groupMap.keySet();
		for (Integer groupId : groupIdSet) {
			// for this group, display the header (group name)
			MetaDataGroup metaDataGroup = (MetaDataGroup)groupMap.get(groupId);
%>
			<h3><%= metaDataGroup.getName()  %></h3>
			<hr class="config-line">
<%
 			if (metaDataGroup.getComment() != null) {
%>
  				<div class="heading-comment"><%= metaDataGroup.getComment() %></div>
<%
 			}
%>
			<br>
<%
			for (String orgName : ldapOrganizations) {
%>
			<table class="config-section">
				<tr>
				<td class="config-checkbox">
	  				<input class="org" type="checkbox" name="<%= orgName %>.cb" onClick="toggleHiddenTable(this, 'hiding-section-<%= orgName %>')"/>
	  			</td>
	  			<td class="config-checkbox-label">	
					<label for="<%= orgName %>.cb"><%=orgName%></label>
				</td> 
				</tr>
			</table>
			<table class="config-section-hiding" id="hiding-section-<%= orgName %>">  
<%
				// get all the properties in this group
				Map<Integer, MetaDataProperty> propertyMap = 
					metadata.getPropertiesInGroup(metaDataGroup.getIndex());
				Set<Integer> orgIndexes = propertyMap.keySet();
	  			for (Integer orgIndex : orgIndexes) {
	  				MetaDataProperty orgProperty = propertyMap.get(orgIndex);
	  				String orgKeyName = orgProperty.getKey() + "." + orgName;
%>		
				<tr>
				<td class="config-property-label" >	
	    			<label for="<%= orgKeyName %>" title="<%= orgProperty.getDescription() %>"><%= orgProperty.getLabel() %></label>
     			</td>	
     			<td class="config-property-input" >
					<input name="<%= orgKeyName %>" 
						value="<%= request.getAttribute(orgKeyName) %>"	  
<% 
					if (orgProperty.getFieldType().equals("password")) { 
%>           		    	    	           		    	             			
	           			type="password"       
<%
	  				}
%>    			
	           			alt="List of administrators for this installation in LDAP DN syntax (colon separated)"/>	           		    
				</td>
				<td class="config-question-mark">
					<img src="style/images/help.png" 
						 alt="<%= orgProperty.getDescription() %>" 
						 onClick="helpWindow('<%= request.getContextPath() %>', '<%= orgProperty.getHelpFile() %>')"/>
				</td>
				</tr>	  
<%
				if (orgProperty.getDescription() != null) {
%>
	    	        <tr>
	    	        <td></td>
	    	        <td class="config-property-description" colspan="2" >
						<%= orgProperty.getDescription() %>
	    	        </td>
<%
	    			}
	  			}
%>
      		</table>
<%
			}
		}
	}
%>

  <input type="hidden" name="configureType" value="organization"/>
  <input type="hidden" name="processForm" value="true"/>
  <input class="left-button" type="submit" value="Save"/>
  <input class="button" type="button" value="Cancel" onClick="forward('./admin')"> 

</form>

</body>
</html>
