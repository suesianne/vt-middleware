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
package org.ldaptive.handler;

import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;

/**
 * Provides post search processing of an ldap entry.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public interface LdapEntryHandler
{


  /**
   * Process an entry from an ldap search.
   *
   * @param  criteria  search criteria used to perform the search
   * @param  entry  search result
   *
   * @return  handler result
   *
   * @throws  LdapException  if the LDAP returns an error
   */
  HandlerResult process(SearchCriteria criteria, LdapEntry entry)
    throws LdapException;
}
