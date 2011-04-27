/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.ldap;

/**
 * Executes a paged ldap search operation.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public class PagedSearchOperation
  extends AbstractSearchOperation<PagedSearchRequest>
{


  /**
   * Creates a new paged search operation.
   *
   * @param  lc  ldap connection
   */
  public PagedSearchOperation(final LdapConnection lc)
  {
    this.ldapConnection = lc;
    this.initialize(lc.getLdapConnectionConfig());
  }


  /** {@inheritDoc} */
  protected LdapResult executeSearch(final PagedSearchRequest request)
    throws LdapException
  {
    final LdapResult lr =
      this.ldapConnection.getProviderConnection().pagedSearch(request);
    this.executeLdapResultHandlers(request, lr);
    return lr;
  }


  /** {@inheritDoc} */
  protected void initializeRequest(
    final PagedSearchRequest request, final LdapConnectionConfig config)
  {
    super.initializeRequest(request, config);
  }
}
