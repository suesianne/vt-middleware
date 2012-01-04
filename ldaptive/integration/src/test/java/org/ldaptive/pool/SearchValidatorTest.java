/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.pool;

import org.ldaptive.AbstractTest;
import org.ldaptive.Connection;
import org.ldaptive.SearchFilter;
import org.ldaptive.SearchRequest;
import org.ldaptive.TestUtil;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Test for {@link SearchValidator}.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class SearchValidatorTest extends AbstractTest
{


  /**
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"validator"})
  public void defaultSettings()
    throws Exception
  {
    final Connection c = TestUtil.createConnection();
    final SearchValidator sv = new SearchValidator();
    try {
      c.open();
      AssertJUnit.assertTrue(sv.validate(c));
    } finally {
      c.close();
    }
    AssertJUnit.assertFalse(sv.validate(c));
  }


  /**
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"validator"})
  public void customSettings()
    throws Exception
  {
    final Connection c = TestUtil.createConnection();
    final SearchValidator sv = new SearchValidator(
      new SearchRequest("ou=test,dc=vt,dc=edu", new SearchFilter("uid=*")));
    try {
      c.open();
      AssertJUnit.assertTrue(sv.validate(c));
      sv.getSearchRequest().setSearchFilter(new SearchFilter("dne=*"));
      AssertJUnit.assertFalse(sv.validate(c));
    } finally {
      c.close();
    }
    AssertJUnit.assertFalse(sv.validate(c));
  }
}
