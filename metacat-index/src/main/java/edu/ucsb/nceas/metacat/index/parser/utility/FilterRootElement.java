package edu.ucsb.nceas.metacat.index.parser.utility;

/**
 *  Copyright: 2019 Regents of the University of California and the
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.cn.indexer.parser.utility.LeafElement;

/**
 *
 * Assembled a query string based on a set of filters in a DataONE collection document.
 * <p>
 * Used by FilterCommonRootSolrField.
 * Based on CommonRootSolrField by sroseboo
 * </p>
 * @author slaughter
 *
 */
public class FilterRootElement {

    private String name;
    private String xPath;
    private XPathExpression xPathExpression = null;
    private String delimiter = " ";
    private List<LeafElement> leafs = new ArrayList<LeafElement>();
    private List<FilterRootElement> subRoots = new ArrayList<FilterRootElement>();
    private List<FilterProcessor> filters = null;
    private FilterGroupProcessor filterGroup = null;
    private String prefixMatch = null;
    private String fixedTerm = null;
    private String postfixMatch = null;
    private Log log = LogFactory.getLog(FilterRootElement.class);

    public FilterRootElement() {
    }

    /**
     * Get the document processor values for the document or node
     *
     * @param docOrNode - An XML document root or sub-node of a DataONE collection document
     * @return the value of the query derived from the document
     * @throws XPathExpressionException
     *
     * <p>
     * This method parses a DataONE collection document and builds a Solr query from the filters
     * defined in the document. The filters define the set of DataONE pids that are of interest
     * to the outhor of the collection (portal). The Solr query provides a way for client
     * programs to fetch the same set of pids that are defined by the filters.
     * </p>
     * <p>
     * One example client use case for this Solr 'collectionQuery' field is to retrive all
     * the pids for a collection and run metadata quality scores on them, to allow the portal
     * users to determine the quality of the metadata for their collection.
     * </p>
     */
    public String getRootValues(Object docOrNode)
            throws XPathExpressionException {

        String prefilterValue = null;
        String postfilterValue = null;
        String filterValue = null;
        filters = getFilters();
        filterGroup = getFilterGroup();
        prefixMatch = getPrefixMatch();
        fixedTerm = getFixedTerm();
        postfixMatch = getPostfixMatch();

        // A typical query prefilter: (isPartOf:urn\:uuid\:349aa330-4645-4dab-a02d-3bf950cf708 OR seriesId:urn:uuid:8c63bc73-c60e-4082-8dc2-8e3ea20bd6e5)
        // A main filter: ((text:soil) AND (-(keywords:*soil layer*) AND -(attribute:*soil layer*)) AND ((dateUploaded:[1800-01-01T00:00:00Z TO 2009-12-31T23:59:59Z])
        // A 'fixed' filter: AND (-obsoletedBy:* AND formatType:METADATA))
        // A 'poastfix' filter: OR (id:urn:uuid:298073d0-dc2b-4f59-bf35-f7a8e60efa0e OR id:urn:uuid:6c6040ef-1393-47f9-a725-645f125f61ef)
        // These are concatenated together to arrive at the full query:
        //     (((<prefix filter) OR (<main filter>)) AND (<fixed filter>)) OR (<postfix filter)

        String mainFilterValue = null;
        String completeFilterValue = null;
        String operator = "AND";
        Boolean prefilter;
        Boolean postfilter;
        int nFilters = filters.size();
        int iFilter;

        FilterGroupProcessor fgp = new FilterGroupProcessor();
        // This call processess all '<filterGroup> elements, in addition to all root level
        // filter definitions.
        log.trace("getRootValues, xpath: " + getxPath());
        completeFilterValue = fgp.getFilterGroupValue(docOrNode, filters, filterGroup, prefixMatch, postfixMatch, xPath);

        // Don't include the 'fixed' filter if there are no pre or main filters. The fixed filter
        // is usually something like '(-obsoletedBy:* AND formatType:METADATA)', which will return ALL
        // unobsoleted metadata pids if there is no pre or main filters to constrain it.
        if (fixedTerm != null) {
            if (completeFilterValue != null) {
                completeFilterValue = "(" + completeFilterValue + " AND " + fixedTerm + ")";
            } else {
                completeFilterValue = "(" + fixedTerm + ")";
            }
        }

        // This cause shouldn't happen (no terms found or specified), but check anyway
        if(completeFilterValue == null) {
            completeFilterValue = "(id:*)";
        }

        return completeFilterValue;
    }

    /**
     * Initialize the XPath object for XML node which includes all filters for
     * this collection document.
     * @param xPathObject the XPath object which includes all filters to process
     */
    public void initXPathExpressions(XPath xPathObject) {
        try {
            if (xPathExpression == null) {
                xPathExpression = xPathObject.compile(xPath);
            }
            for (LeafElement leaf : leafs) {
                leaf.initXPathExpression(xPathObject);
            }
            for (FilterRootElement subRoot : subRoots) {
                subRoot.initXPathExpressions(xPathObject);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the name of this processor
     * @return the name of this processor
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of this processor
     * @param name the name of this processor
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the XPath of the filter node this processor operates on
     * @return the XPath of the filter node this processor operates on
     */
    public String getxPath() {
        return xPath;
    }

    /**
     * Return the XPath for filter nodes this processor operates on
     * @param xPath  the XPath for filter nodes this processor operates on
     */
    public void setxPath(String xPath) {
        this.xPath = xPath;
    }

    /**
     * Return the xPath expression applied to the filter node that this processor operates on
     * @return the xPath expression applied to the filter node that this processor operates on
     */
    public XPathExpression getxPathExpression() {
        return xPathExpression;
    }

    /**
     * Set the xpath expression applied to the filter node that this processor operates on
     * @param xPathExpression the xpath expression applied to the filter node that this processor operates on
     */
    public void setxPathExpression(XPathExpression xPathExpression) {
        this.xPathExpression = xPathExpression;
    }

    /**
     * Get the delimeter used to separate string tokens
     * @return the delimeter used to separate string tokens
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Set the delimeter used to separate string tokens
     * @param delimiter the delimeter used to separate string tokens
     * @see "application-context-collection.xml"
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Get the terms used to identify a 'prefix' filter
     * @return the terms used to identify a 'prefix' filter
     * @see "application-context-collection.xml"
     */
    public String getPrefixMatch() {
        return prefixMatch;
    }

    /**
     * Get the terms used to identify a 'prefix' filter
     * @param prefixMatch the terms used to identify a 'prefix' filter
     */
    public void setPrefixMatch(String prefixMatch) {
        this.prefixMatch = prefixMatch;
    }

    /**
     * Get the 'fixed' portion of a query filter
     * @return the 'fixed' portion of a query filter
     */
    public String getFixedTerm() {
        return fixedTerm;
    }

    /**
     * Set the 'fixed' portion of a query filter
     * @param fixedTerm the 'fixed' portion of a query filter
     */
    public void setFixedTerm(String fixedTerm) {
        this.fixedTerm = fixedTerm;
    }

    /**
     * Get the terms used to identify a 'postfix' filter
     * @return the terms used to identify a 'postfix' filter
     */
    public String getPostfixMatch() {
        return postfixMatch;
    }

    /**
     * Set the terms used to identify a 'postfix' filter
     * @param postfixMatch the terms used to identify a 'postfix' filter
     */
    public void setPostfixMatch(String postfixMatch) {
        this.postfixMatch = postfixMatch;
    }

    /**
     * Get the 'leaf' elements defined for a filter
     * @return the leaf elements
     * @see "application-context-collection.xml"
     */
    public List<LeafElement> getLeafs() {
        return leafs;
    }

    /**
     * Get the 'leaf' elements defined for a filter
     * @param leafs the 'leaf' elements defined for a filter
     * @see "application-context-collection.xml"
     */
    public void setLeafs(List<LeafElement> leafs) {
        this.leafs = leafs;
    }

    /**
     * Get the children filter nodes
     * @return the children filter nodes
     */
    public List<FilterRootElement> getSubRoots() {
        return subRoots;
    }

    /**
     * Set the children filter nodes
     * @param subRoots the children filter nodes
     */
    public void setSubRoots(List<FilterRootElement> subRoots) {
        this.subRoots = subRoots;
    }

    /**
     * Get all defined filter processors
     * @return all defined filter processors
     * @see "application-context-collection.xml"
     */
    public List<FilterProcessor> getFilters() {
        return filters;
    }

    /**
     * Get all defined filter processors
     * @param filters all defined filter processors
     * @see "application-context-collection.xml"
     */
    public void setFilters(List<FilterProcessor> filters) {
        this.filters = filters;
    }

    /**
     * Get defined filter group processor
     * @return defined filter group processor
     * @see "application-context-collection.xml"
     */
    public FilterGroupProcessor getFilterGroup() {
        return filterGroup;
    }

    /**
     * Get defined filter group processor
     * @param filterGroup defined filter group processor
     * @see "application-context-collection.xml"
     */
    public void setFilterGroup(FilterGroupProcessor filterGroup) {
        this.filterGroup = filterGroup;
    }
}

