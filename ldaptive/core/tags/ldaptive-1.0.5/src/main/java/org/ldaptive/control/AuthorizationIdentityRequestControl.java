/*
  $Id$

  Copyright (C) 2003-2014 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.control;

import org.ldaptive.LdapUtils;

/**
 * Request control for authorization identify. See RFC 3829.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class AuthorizationIdentityRequestControl extends AbstractControl
  implements RequestControl
{

  /** OID of this control. */
  public static final String OID = "2.16.840.1.113730.3.4.16";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 7013;


  /** Default constructor. */
  public AuthorizationIdentityRequestControl()
  {
    super(OID);
  }


  /**
   * Creates a new ManageDsaIT control.
   *
   * @param  critical  whether this control is critical
   */
  public AuthorizationIdentityRequestControl(final boolean critical)
  {
    super(OID, critical);
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(HASH_CODE_SEED, getOID(), getCriticality());
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::criticality=%s]",
        getClass().getName(),
        hashCode(),
        getCriticality());
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    return null;
  }
}
