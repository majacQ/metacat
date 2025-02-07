/**
 *  '$RCSfile$'
 *    Purpose: A Class that tracks sessions for MetaCatServlet users.
 *  Copyright: 2000 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
 *    Authors: Matt Jones
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

package edu.ucsb.nceas.metacat;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.ucsb.nceas.metacat.properties.PropertyService;
import edu.ucsb.nceas.metacat.shared.MetacatUtilException;
import edu.ucsb.nceas.metacat.util.AuthUtil;
import edu.ucsb.nceas.utilities.PropertyNotFoundException;

/**
 * A Class that implements session tracking for MetaCatServlet users.
 * User's login data are stored in the session object.
 * User authentication is done through a dynamically determined AuthInterface.
 */
public class AuthSession {

	private String authClass = null;
	private HttpSession session = null;
	private AuthInterface authService = null;
	private String statusMessage = null;
	private static Logger logMetacat = Logger.getLogger(AuthSession.class);

	/**
	 * Construct an AuthSession
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public AuthSession() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Determine our session authentication method and
		// create an instance of the auth class
		try {
            this.authClass = PropertyService.getProperty("auth.class");
        } catch (PropertyNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		this.authService = (AuthInterface) createObject(authClass);
	}

	/**
	 * Get the new session
	 */
	public HttpSession getSessions() {
		return this.session;
	}

	/**
	 * determine if the credentials for this session are valid by
	 * authenticating them using the authService configured for this session.
	 *
	 * @param request the request made from the client
	 * @param username the username entered when login
	 * @param password the password entered when login
	 */
	public boolean authenticate(HttpServletRequest request, String username,
			String password) {
		String message = null;
		try {
			if (authService.authenticate(username, password)) {

				// getGroups returns groupname along with their description.
				// hence groups[] is generated from groupsWithDescription[][]
				String[][] groupsWithDescription = authService.getGroups(username,
						password, username);
				String groups[] = new String[groupsWithDescription.length];

				for (int i = 0; i < groupsWithDescription.length; i++) {
					groups[i] = groupsWithDescription[i][0];
				}

				if (groups == null) {
					groups = new String[0];
				}

				String[] userInfo = authService.getUserInfo(username, password);

				this.session = createSession(request, username, password, groups,
						userInfo);
				String sessionId = session.getId();
				message = "Authentication successful for user: " + username;
				this.statusMessage = formatOutput("login", message, sessionId, username,
						groups, userInfo);
				return true;
			} else {
				message = "Authentication failed for user: " + username;
				this.statusMessage = formatOutput("unauth_login", message);
				return false;
			}
		} catch (ConnectException ce) {
			message = "Connection to the authentication service failed in "
					+ "AuthSession.authenticate: " + ce.getMessage();
		} catch (IllegalStateException ise) {
			message = ise.getMessage();
		}

		this.statusMessage = formatOutput("error_login", message);
		return false;
	}

	/** Get new HttpSession and store username & password in it */
	private HttpSession createSession(HttpServletRequest request, String username,
			String password, String[] groups, String[] userInfo)
			throws IllegalStateException {

		// get the current session object, create one if necessary
		HttpSession session = request.getSession(true);

		// if it is still in use invalidate and get a new one
		if (!session.isNew()) {
			logMetacat.info("in session is not new");
			logMetacat.info("the old session id is : " + session.getId());
			logMetacat.info("the old session username : "
					+ session.getAttribute("username"));
			session.invalidate();
			logMetacat.info("in session is not new");
			session = request.getSession(true);
		}
		// store the username, password, and groupname (the first only)
		// in the session obj for use on subsequent calls to Metacat servlet
		session.setMaxInactiveInterval(-1);
		session.setAttribute("username", username);
		session.setAttribute("password", password);

		if (userInfo != null & userInfo.length == 3) {
			session.setAttribute("name", userInfo[0]);
			session.setAttribute("organization", userInfo[1]);
			session.setAttribute("email", userInfo[2]);
		}

		if (groups.length > 0) {
			session.setAttribute("groupnames", groups);
		}
		logMetacat.info("the new session id is : " + session.getId());
		logMetacat.info("the new session username : " + session.getAttribute("username"));
		return session;
	}

	/**
	 * Get the message associated with authenticating this session. The
	 * message is formatted in XML.
	 */
	public String getMessage() {
		return this.statusMessage;
	}

	/**
	 * Get all groups and users from authentication scheme.
	 * The output is formatted in XML.
	 * @param user the user which requests the information
	 * @param password the user's password
	 */
	public String getPrincipals(String user, String password) throws ConnectException {
		return authService.getPrincipals(user, password);
	}
	
	/**
	 * Get attributes describing a user or group
	 * 
	 * @param foruser
	 *            the user for which the attribute list is requested
	 * @returns HashMap a map of attribute name to a Vector of values
	 */
	public HashMap<String, Vector<String>> getAttributes(String foruser)
			throws ConnectException {
		return authService.getAttributes(foruser);
	}

	/*
	 * format the output in xml for processing from client applications
	 * 
	 * @param tag the root element tag for the message (error or success) @param
	 * message the message content of the root element
	 */
	private String formatOutput(String tag, String message) {
		return formatOutput(tag, message, null, null, null, null);
	}

	/*
	 * format the output in xml for processing from client applications
	 *
	 * @param tag the root element tag for the message (error or success)
	 * @param message the message content of the root element
	 * @param sessionId the session identifier for a successful login
	 */
	private String formatOutput(String tag, String message, String sessionId,
			String username, String[] groups, String userInfo[]) {
		StringBuffer out = new StringBuffer();

		out.append("<?xml version=\"1.0\"?>\n");
		out.append("<" + tag + ">");
		out.append("\n  <message>" + message + "</message>\n");
		if (sessionId != null) {
			out.append("\n  <sessionId>" + sessionId + "</sessionId>\n");

			if (userInfo != null && userInfo[0] != null) {
				out.append("\n<name>\n");
				out.append(userInfo[0]);
				out.append("\n</name>\n");
			}
      
			if(userInfo != null && userInfo[1]!=null){
				out.append("\n<organization>\n");
				out.append(userInfo[1]);
				out.append("\n</organization>\n");
			}
      
			if(userInfo != null && userInfo[2]!=null){
				out.append("\n<email>\n");
				out.append(userInfo[2]);
				out.append("\n</email>\n");
			}

			try {
				// insert <isAdministrator> tag if the user is an administrator
				if (AuthUtil.isAdministrator(username, groups)) {
					out.append("\n  <isAdministrator></isAdministrator>\n");
				}
			} catch (MetacatUtilException ue) {
				logMetacat.error("Could not determine if user is administrator. "
						+ "Omitting from xml output: " + ue.getMessage());
			}
			
			try {
				// insert <isModerator> tag if the user is a Moderator
				if (AuthUtil.isModerator(username, groups)) {
					out.append("\n  <isModerator></isModerator>\n");
				}
			} catch (MetacatUtilException ue) {
				logMetacat.error("Could not determine if user is moderator. "
						+ "Omitting from xml output: " + ue.getMessage());
			}
		}
		out.append("</" + tag + ">");

		return out.toString();
	}

	/**
	 * Instantiate a class using the name of the class at runtime
	 *
	 * @param className the fully qualified name of the class to instantiate
	 */
	private static Object createObject(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		Object object = null;
//		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
//		} catch (InstantiationException e) {
//			throw e;
//		} catch (IllegalAccessException e) {
//			throw e;
//		} catch (ClassNotFoundException e) {
//			throw e;
//		}
		return object;
	}
	
	/**
	 * Instantiate a class using the name of the class at runtime
	 *
	 * @param className the fully qualified name of the class to instantiate
	 */
	private static Object createObject(String className, String orgName) throws Exception {

		Object object = null;
		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}
		return object;
	}
}
