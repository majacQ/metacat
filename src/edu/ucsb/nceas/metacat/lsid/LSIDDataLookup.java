/**
 *  '$RCSfile$'
 *  Copyright: 2000-2005 Regents of the University of California and the
 *             National Center for Ecological Analysis and Synthesis
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

package edu.ucsb.nceas.metacat.lsid;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.lsid.LSID;
import com.ibm.lsid.server.LSIDServerException;

public class LSIDDataLookup
{
    public static final int UNKNOWN = 1;
    public static final int ABSTRACT = 2;
    public static final int CONCRETE = 4;

    private static Log logger = LogFactory
                    .getLog("edu.ucsb.nceas.metacat.lsid");

    public LSIDDataLookup() throws LSIDServerException
    {
        logger.info("Creating LSIDDataLookup (Metacat).");
    }

    public String getDocType(LSID lsid) {
      // example
      //http://metacat.nceas.ucsb.edu/knb/metacat?action=getrevisionanddoctype&docid=knb-lter-gce.109.6
      // returns rev;doctype

      String _docType = null;
      String ns = lsid.getNamespace();
      String id = lsid.getObject();
      String ver = lsid.getRevision();
      InputStream docStream = null;

      ResourceBundle rb = ResourceBundle.getBundle("metacat-lsid");
      String theServer = rb.getString("metacatserver");
      logger.debug("the server is " + theServer);

      String url = theServer + "?action=getrevisionanddoctype&docid=";
      url = url + ns + "." + id + "." + ver;
      try {
        URL theDoc = new URL(url);
        docStream = theDoc.openStream();

        StringTokenizer _st = new StringTokenizer(LSIDAuthorityMetaData.
                                                  getStringFromInputStream(
            docStream), ";");
        _st.nextToken();
        _docType = _st.nextToken();
        docStream.close();
      }
      catch (MalformedURLException mue) {
        logger.error("MalformedURLException in LSIDDataLookup: " + mue);
        mue.printStackTrace();
      }
      catch (IOException ioe) {
        logger.error("IOException in LSIDDataLookup: " + ioe);
        ioe.printStackTrace();
      }
      return _docType;
    }

    public int lsidType(LSID lsid) throws LSIDServerException
    {

        int result = CONCRETE;
        return result;
    }

    public InputStream lsidData(LSID lsid) throws LSIDServerException
    {
        String ns = lsid.getNamespace();
        String id = lsid.getObject();
        String ver = lsid.getRevision();
        InputStream docStream = null;

        // example metacat query
        // http://metacat.nceas.ucsb.edu/knb/metacat?action=read&qformat=xml&docid=knb-lter-gce.109.6
        //

        ResourceBundle rb = ResourceBundle.getBundle("metacat-lsid");
        String theServer = rb.getString("metacatserver");
        logger.debug("the server is " + theServer);

        String url = theServer + "?action=read&qformat=xml&docid=";
        url = url + ns + "." + id + "." + ver;
        try {
            URL theDoc = new URL(url);
            docStream = theDoc.openStream();
        } catch (MalformedURLException mue) {
            logger.error("MalformedURLException in LSIDDataLookup: " + mue);
            mue.printStackTrace();
        } catch (IOException ioe) {
            logger.error("IOException in LSIDDataLookup: " + ioe);
            ioe.printStackTrace();
        }
        return docStream;
    }
}
