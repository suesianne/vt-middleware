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
package org.ldaptive.provider;

import org.ldaptive.LdapException;
import org.ldaptive.ResultCode;
import org.ldaptive.control.ResponseControl;

/**
 * Exception thrown when a connection attempt fails.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class ConnectionException extends LdapException
{

  /** serialVersionUID. */
  private static final long serialVersionUID = -1902801031167384619L;


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   */
  public ConnectionException(final String msg)
  {
    super(msg);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   * @param  code  result code
   */
  public ConnectionException(final String msg, final ResultCode code)
  {
    super(msg, code);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   * @param  code  result code
   * @param  c  response controls
   */
  public ConnectionException(
    final String msg,
    final ResultCode code,
    final ResponseControl[] c)
  {
    super(msg, code, c);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  e  provider specific exception
   */
  public ConnectionException(final Exception e)
  {
    super(e);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  e  provider specific exception
   * @param  code  result code
   */
  public ConnectionException(final Exception e, final ResultCode code)
  {
    super(e, code);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  e  provider specific exception
   * @param  code  result code
   * @param  c  response controls
   */
  public ConnectionException(
    final Exception e,
    final ResultCode code,
    final ResponseControl[] c)
  {
    super(e, code, c);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   * @param  e  provider specific exception
   */
  public ConnectionException(final String msg, final Exception e)
  {
    super(msg, e);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   * @param  e  provider specific exception
   * @param  code  result code
   */
  public ConnectionException(
    final String msg,
    final Exception e,
    final ResultCode code)
  {
    super(msg, e, code);
  }


  /**
   * Creates a new connection exception.
   *
   * @param  msg  describing this exception
   * @param  e  provider specific exception
   * @param  code  result code
   * @param  c  response controls
   */
  public ConnectionException(
    final String msg,
    final Exception e,
    final ResultCode code,
    final ResponseControl[] c)
  {
    super(msg, e, code, c);
  }
}
