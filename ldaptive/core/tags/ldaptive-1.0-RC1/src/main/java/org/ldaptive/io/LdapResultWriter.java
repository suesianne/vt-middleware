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
package org.ldaptive.io;

import java.io.IOException;
import org.ldaptive.LdapResult;

/**
 * Interface for writing ldap results.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public interface LdapResultWriter
{


  /**
   * Writes the supplied ldap result.
   *
   * @param  result  ldap result to write
   *
   * @throws  IOException  if an error occurs using the writer
   */
  void write(final LdapResult result)
    throws IOException;
}
