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
package org.ldaptive.asn1;

import java.math.BigInteger;

/**
 * Converts context types to their DER encoded format.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ContextType extends AbstractDERType implements DEREncoder
{

  /** ASN tag. */
  private final int derTag;

  /** Data to encode. */
  private final byte[] derItem;


  /**
   * Creates a new context type.
   *
   * @param  index  of this item in the context
   * @param  item  to encode
   */
  public ContextType(final int index, final byte[] item)
  {
    derTag = ContextDERTag.TAG_CLASS | index;
    derItem = item;
  }


  /**
   * Creates a new context type.
   *
   * @param  index  of this item in the context
   * @param  item  to encode
   */
  public ContextType(final int index, final String item)
  {
    this(index, OctetStringType.toBytes(item));
  }


  /**
   * Creates a new context type.
   *
   * @param  index  of this item in the context
   * @param  item  to encode
   */
  public ContextType(final int index, final boolean item)
  {
    this(index, BooleanType.toBytes(item));
  }


  /**
   * Creates a new context type.
   *
   * @param  index  of this item in the context
   * @param  item  to encode
   */
  public ContextType(final int index, final BigInteger item)
  {
    this(index, IntegerType.toBytes(item));
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    return encode(derTag, derItem);
  }
}
