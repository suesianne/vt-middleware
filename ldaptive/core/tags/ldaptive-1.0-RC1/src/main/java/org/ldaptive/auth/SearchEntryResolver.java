/*
  $Id$

  Copyright (C) 2003-2012 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.auth;

import java.util.Arrays;
import org.ldaptive.Connection;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.LdapResult;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Looks up the LDAP entry associated with a user using an LDAP search.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class SearchEntryResolver implements EntryResolver
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** User attributes to return. */
  private String[] returnAttributes;


  /** Default constructor. */
  public SearchEntryResolver() {}


  /**
   * Creates a new search entry resolver.
   *
   * @param  attrs  to return
   */
  public SearchEntryResolver(final String... attrs)
  {
    setReturnAttributes(attrs);
  }


  /**
   * Returns the return attributes.
   *
   * @return  attributes to return
   */
  public String[] getReturnAttributes()
  {
    return returnAttributes;
  }


  /**
   * Sets the return attributes.
   *
   * @param  attrs  to return
   */
  public void setReturnAttributes(final String... attrs)
  {
    returnAttributes = attrs;
  }


  /** {@inheritDoc} */
  @Override
  public LdapEntry resolve(
    final Connection conn,
    final AuthenticationCriteria ac)
    throws LdapException
  {
    logger.debug(
      "resolve criteria={} with attributes={}",
      ac,
      returnAttributes == null ? "<all attributes>"
                               : Arrays.toString(returnAttributes));

    final SearchOperation search = new SearchOperation(conn);
    final LdapResult result = search.execute(
      SearchRequest.newObjectScopeSearchRequest(
        ac.getDn(), returnAttributes)).getResult();
    logger.debug(
      "resolve entry={} for criteria={} with attributes={}",
      new Object[] {
        result.getEntry(),
        ac,
        returnAttributes == null ? "<all attributes>"
                                 : Arrays.toString(returnAttributes),
      });
    return result.getEntry();
  }
}
