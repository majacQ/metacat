<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0//EN">
<!--
  *  '$RCSfile$'
  *      Authors: Chad Berkley
  *    Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *  For Details: http://www.nceas.ucsb.edu/
  *
  *   '$Author$'
  *     '$Date$'
  * '$Revision$'
  * 
  * This is an HTML document for loading an xml document into Oracle
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
-->
<html>
<head>

<title>Replication Configuration</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/admin/admin.css"></link>
<script language="JavaScript" type="text/JavaScript"
	src="<%=request.getContextPath()%>/admin/admin.js"></script>

</head>
<body>
<%@ include file="./header-section.jsp"%>

<img src="<%=request.getContextPath()%>/metacat-logo.png"
	width="100px" align="right" />
<h2>Replication Configuration</h2>

<p>Manage Metacat Replication</p>

<h3>Timed Replication</h3>
<hr class="config-line">

<form action="<%=request.getContextPath()%>/admin" method="POST" target="bottom">

	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="rate" title="Rate">Delta T (milliseconds)</label>
		</div>
		<input class="textinput" 
			id="rate" 
			name="rate" 	             		    	    	           		    	             			
			value="172800000"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="firsttime" title="First Time (The format should be 10:00 AM)">First Time</label>
		</div>
		<input class="textinput" 
			id="firsttime" 
			name="firsttime" 	             		    	    	           		    	             			
			value="10:00 PM"/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="action" title="Action">Timer</label>
		</div>
		<input 
			type= radio 
			class="textinput" 
			id="action" 
			name="action" 	             		    	    	           		    	             			
			value="start"/>Start
		<input 
			type= radio 
			class="textinput" 
			id="action" 
			name="action" 	             		    	    	           		    	             			
			value="stop"/>Stop
	</div>

	<input type="hidden" name="configureType" value="replication">
	<input type="submit" value="Submit" target="bottom">
	
</form>

<h3>Replicate Now</h3>
<hr class="config-line">

<table border="0" cellpadding="4" cellspacing="0" width="100%" class="tablepanel">
	<tr>
		<td>
		<form action="<%=request.getContextPath()%>/admin" method="POST" target="bottom">
			<input type="hidden" name="action" value="getall"> 
			<input type="hidden" name="configureType" value="replication">
			<input type="submit" value="Get All" target="bottom">
			bring all updated documents from remote hosts to this server
		</form>
		</td>
	</tr>
</table>

<h3>Servers</h3>
<hr class="config-line">

<form action="<%=request.getContextPath()%>/admin" method="POST" target="bottom">
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="subaction" title="Action">&nbsp;</label>
		</div>
		<input 
			type= radio 
			class="textinput" 
			id="subaction" 
			name="subaction" 	             		    	    	           		    	             			
			value="add"/>Add
		<input 
			type= radio 
			class="textinput" 
			id="subaction" 
			name="subaction" 	             		    	    	           		    	             			
			value="delete"/>Remove
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="server" title="Server">Server</label>
		</div>
		<input class="textinput" 
			id="server" 
			name="server" 	             		    	    	           		    	             			
			value=""/> 
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="replicate" title="Replicate">Replicate metadata?</label>
		</div>
		<input 
			type= radio 
			class="textinput" 
			id="replicate" 
			name="replicate" 	             		    	    	           		    	             			
			value="1"/>Yes
		<input 
			type= radio 
			class="textinput" 
			id="replicate" 
			name="replicate" 	             		    	    	           		    	             			
			value="0"/>No
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="datareplicate" title="Replicate data">Replicate data?</label>
		</div>
		<input 
			type= radio 
			class="textinput" 
			id="datareplicate" 
			name="datareplicate" 	             		    	    	           		    	             			
			value="1"/>Yes
		<input 
			type= radio 
			class="textinput" 
			id="datareplicate" 
			name="datareplicate" 	             		    	    	           		    	             			
			value="0"/>No 
	</div>
	<div class="form-row">
		<img class="question-mark" src="<%=request.getContextPath()%>/style/images/help.png" 
			onClick="helpWindow('<%= request.getContextPath() %>','docs/replication.html')"/>
		<div class="textinput-label">
			<label for="hub" title="Hub">Localhost is a hub?</label>
		</div>
		<input 
			type= radio 
			class="textinput" 
			id="hub" 
			name="hub" 	             		    	    	           		    	             			
			value="1"/>Yes
		<input 
			type= radio 
			class="textinput" 
			id="hub" 
			name="hub" 	             		    	    	           		    	             			
			value="0"/>No
	</div>

	<input type="hidden" name="configureType" value="replication">
	<input type="hidden" name="action" value="servercontrol">
	<input type="submit" value="Submit" target="bottom">

</form>

<h3>Hazelcast Synchronization</h3>
<hr class="config-line">

<table border="0" cellpadding="4" cellspacing="0" width="100%" class="tablepanel">
	<tr>
		<td>
		<form action="<%=request.getContextPath()%>/admin" method="POST" target="bottom">
			<input type="hidden" name="action" value="resynchSystemMetadata"> 
			<input type="hidden" name="configureType" value="replication">
			<input type="submit" value="Resynch" target="bottom">
			Bring all missing System Metadata from remote hosts to this server
		</form>
		</td>
	</tr>
</table>

<br/>

<a
	href="<%=request.getContextPath()%>/admin?configureType=replication&action=servercontrol&subaction=list"
	target="bottom">Refresh Server List</a>

<br/>

<a href="<%=request.getContextPath()%>/admin" target="_top">Return to main configuration</a>

<%@ include file="./footer-section.jsp"%>

</body>
</html>
