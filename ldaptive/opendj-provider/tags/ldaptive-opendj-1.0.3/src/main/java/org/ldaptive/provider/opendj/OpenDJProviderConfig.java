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
package org.ldaptive.provider.opendj;

import java.util.Arrays;
import org.forgerock.opendj.ldap.LDAPOptions;
import org.forgerock.opendj.ldap.controls.Control;
import org.ldaptive.ResultCode;
import org.ldaptive.provider.ControlProcessor;
import org.ldaptive.provider.ProviderConfig;

/**
 * Contains configuration data for the OpenDJ provider.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class OpenDJProviderConfig extends ProviderConfig
{

  /** Connection options. */
  private LDAPOptions options;

  /** Search result codes to ignore. */
  private ResultCode[] searchIgnoreResultCodes;

  /** OpenDJ specific control processor. */
  private ControlProcessor<Control> controlProcessor;


  /** Default constructor. */
  public OpenDJProviderConfig()
  {
    setOperationExceptionResultCodes(ResultCode.SERVER_DOWN);
    searchIgnoreResultCodes = new ResultCode[] {
      ResultCode.TIME_LIMIT_EXCEEDED,
      ResultCode.SIZE_LIMIT_EXCEEDED,
    };
    controlProcessor = new ControlProcessor<Control>(
      new OpenDJControlHandler());
  }


  /**
   * Returns the connection options.
   *
   * @return  ldap options
   */
  public LDAPOptions getOptions()
  {
    return options;
  }


  /**
   * Sets the connection options.
   *
   * @param  o  ldap options
   */
  public void setOptions(final LDAPOptions o)
  {
    options = o;
  }


  /**
   * Returns the search ignore result codes.
   *
   * @return  result codes to ignore
   */
  public ResultCode[] getSearchIgnoreResultCodes()
  {
    return searchIgnoreResultCodes;
  }


  /**
   * Sets the search ignore result codes.
   *
   * @param  codes  to ignore
   */
  public void setSearchIgnoreResultCodes(final ResultCode[] codes)
  {
    checkImmutable();
    logger.trace("setting searchIgnoreResultCodes: {}", Arrays.toString(codes));
    searchIgnoreResultCodes = codes;
  }


  /**
   * Returns the control processor.
   *
   * @return  control processor
   */
  public ControlProcessor<Control> getControlProcessor()
  {
    return controlProcessor;
  }


  /**
   * Sets the control processor.
   *
   * @param  processor  control processor
   */
  public void setControlProcessor(final ControlProcessor<Control> processor)
  {
    checkImmutable();
    logger.trace("setting controlProcessor: {}", processor);
    controlProcessor = processor;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::operationExceptionResultCodes=%s, properties=%s, " +
        "connectionStrategy=%s, options=%s, searchIgnoreResultCodes=%s, " +
        "controlProcessor=%s]",
        getClass().getName(),
        hashCode(),
        Arrays.toString(getOperationExceptionResultCodes()),
        getProperties(),
        getConnectionStrategy(),
        options,
        Arrays.toString(searchIgnoreResultCodes),
        controlProcessor);
  }
}
