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

/**
 * Provides post processing of authentication responses.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public interface AuthenticationResponseHandler
{


  /**
   * Process the response from an ldap authentication.
   *
   * @param  response  produced from an authentication
   */
  void process(AuthenticationResponse response);
}
