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
package org.ldaptive.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import org.ldaptive.LdapUtil;

/**
 * Reads private key credentials from classpath, filepath, or stream resource.
 * Supported private key formats include: PKCS8.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class PrivateKeyCredentialReader
  extends AbstractCredentialReader<PrivateKey>
{


  /**
   * Reads a private key from an input stream.
   *
   * @param  is  Input stream from which to read private key.
   * @param  params  A single optional parameter, algorithm, may be specified.
   * The default is RSA.
   *
   * @return  Private key read from data in stream.
   *
   * @throws  IOException  On IO errors.
   * @throws  GeneralSecurityException  On errors with the credential data.
   */
  public PrivateKey read(final InputStream is, final String... params)
    throws IOException, GeneralSecurityException
  {
    String algorithm = "RSA";
    if (params.length > 0 && params[0] != null) {
      algorithm = params[0];
    }

    final KeyFactory kf = KeyFactory.getInstance(algorithm);
    final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(
      LdapUtil.readInputStream(getBufferedInputStream(is)));
    return kf.generatePrivate(spec);
  }
}
