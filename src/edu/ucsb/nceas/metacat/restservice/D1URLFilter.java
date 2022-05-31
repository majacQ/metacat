/**
 *  '$RCSfile$'
 *  Copyright: 2010 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *
 *   '$Author: berkley $'
 *     '$Date: 2009-06-13 13:28:21 +0300  $'
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

package edu.ucsb.nceas.metacat.restservice;

import javax.servlet.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.*;

/**
 * @author berkley
 *
 */
public class D1URLFilter implements Filter
{
    ServletContext context;
    private static Log logMetacat = LogFactory.getLog(D1URLFilter.class);
    
    public void init(FilterConfig filterConfig) 
    {
        logMetacat.debug("D1URLFilter.init - init.");
        this.context = filterConfig.getServletContext();
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException 
    {
        logMetacat.debug("D1URLFilter.doFilter - do filtering");
        D1HttpRequest d1h = new D1HttpRequest(request);
        chain.doFilter(d1h, response);
    }
    
    public void destroy() 
    {
        logMetacat.debug("D1URLFilter.destory - destroy filter");
    }
}
