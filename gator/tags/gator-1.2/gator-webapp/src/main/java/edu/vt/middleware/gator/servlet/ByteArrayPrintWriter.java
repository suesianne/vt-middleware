/*
  $Id$

  Copyright (C) 2008 Virginia Tech, Middleware.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.gator.servlet;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Memory-backed {@link PrintWriter}.
 *
 * @author Middleware
 * @version $Revision$
 *
 */
public class ByteArrayPrintWriter extends PrintWriter
{
  /**
   * Creates a new instance with the default backing stream.
   */
  public ByteArrayPrintWriter()
  {
    this(new ByteArrayOutputStream());
  }


  /**
   * Creates a new instance with the given backing stream.
   * @param os Backing stream.
   */
  public ByteArrayPrintWriter(final ByteArrayOutputStream os)
  {
    super(os);
  }
}
