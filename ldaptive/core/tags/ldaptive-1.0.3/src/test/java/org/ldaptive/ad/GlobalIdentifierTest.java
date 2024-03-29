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
package org.ldaptive.ad;

import org.ldaptive.LdapUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link GlobalIdentifier}.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class GlobalIdentifierTest
{


  /**
   * ObjectGuid test data.
   *
   * @return  test data
   *
   * @throws  Exception  if test data cannot be generated
   */
  @DataProvider(name = "guids")
  public Object[][] createGuids()
    throws Exception
  {
    return new Object[][] {
      new Object[] {
        "{B1DB3CCA-72BD-4F31-9EBF-C70CD44BDA32}",
        LdapUtils.base64Decode("yjzbsb1yMU+ev8cM1EvaMg=="),
      },
      new Object[] {
        "{0F0BF778-1C43-4D0C-82E6-BAD22D6AB646}",
        LdapUtils.base64Decode("ePcLD0McDE2C5rrSLWq2Rg=="),
      },
      new Object[] {
        "{36B403E2-BA7F-4A83-8049-B3CD202C7032}",
        LdapUtils.base64Decode("4gO0Nn+6g0qASbPNICxwMg=="),
      },
    };
  }


  /**
   * @param  guidString  objectGuid string form
   * @param  guid  global identifier
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"ad"}, dataProvider = "guids")
  public void testToString(final String guidString, final byte[] guid)
    throws Exception
  {
    Assert.assertEquals(GlobalIdentifier.toString(guid), guidString);
  }


  /**
   * @param  guidString  objectGuid string form
   * @param  guid  global identifier
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"ad"}, dataProvider = "guids")
  public void testToBytes(final String guidString, final byte[] guid)
    throws Exception
  {
    Assert.assertEquals(guid, GlobalIdentifier.toBytes(guidString));
  }
}
