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
package edu.vt.middleware.ldap;

import java.util.Arrays;
import javax.security.auth.login.LoginContext;
import edu.vt.middleware.ldap.auth.AuthenticationRequest;
import edu.vt.middleware.ldap.auth.Authenticator;
import edu.vt.middleware.ldap.auth.SearchDnResolver;
import edu.vt.middleware.ldap.handler.DnAttributeResultHandler;
import edu.vt.middleware.ldap.handler.LdapResultHandler;
import edu.vt.middleware.ldap.handler.MergeResultHandler;
import edu.vt.middleware.ldap.handler.RecursiveResultHandler;
import edu.vt.middleware.ldap.jaas.RoleResolver;
import edu.vt.middleware.ldap.jaas.TestCallbackHandler;
import edu.vt.middleware.ldap.props.AuthenticatorPropertySource;
import edu.vt.middleware.ldap.props.ConnectionConfigPropertySource;
import edu.vt.middleware.ldap.props.SearchRequestPropertySource;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link SearchConfigProperties}.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class PropertiesTest
{


  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"propstest"})
  public void init()
    throws Exception
  {
    System.setProperty(
      "java.security.auth.login.config",
      "target/test-classes/ldap_jaas.config");
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"propstest"})
  public void nullProperties()
    throws Exception
  {
    final ConnectionConfig cc = new ConnectionConfig();
    final ConnectionConfigPropertySource ccSource =
      new ConnectionConfigPropertySource(
        cc, PropertiesTest.class.getResourceAsStream("/ldap.null.properties"));
    ccSource.initialize();

    AssertJUnit.assertNull(cc.getSslSocketFactory());
    AssertJUnit.assertNull(cc.getHostnameVerifier());

    final SearchRequest sr = new SearchRequest();
    final SearchRequestPropertySource srSource =
      new SearchRequestPropertySource(
        sr, PropertiesTest.class.getResourceAsStream("/ldap.null.properties"));
    srSource.initialize();

    AssertJUnit.assertNull(sr.getLdapResultHandlers());
    AssertJUnit.assertNull(sr.getSearchIgnoreResultCodes());
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"propstest"})
  public void parserProperties()
    throws Exception
  {
    final ConnectionConfig cc = new ConnectionConfig();
    final ConnectionConfigPropertySource ccSource =
      new ConnectionConfigPropertySource(
        cc,
        PropertiesTest.class.getResourceAsStream("/ldap.parser.properties"));
    ccSource.initialize();

    AssertJUnit.assertEquals(
      "ldap://ed-dev.middleware.vt.edu:14389", cc.getLdapUrl());
    AssertJUnit.assertEquals("uid=1,ou=test,dc=vt,dc=edu", cc.getBindDn());
    AssertJUnit.assertEquals(8000, cc.getTimeout());
    AssertJUnit.assertFalse(cc.isTlsEnabled());
    AssertJUnit.assertEquals(1, cc.getProvider().getProperties().size());
    AssertJUnit.assertEquals(
      "true",
      cc.getProvider().getProperties().get("java.naming.authoritative"));
    AssertJUnit.assertEquals(7, cc.getOperationRetry());
    AssertJUnit.assertEquals(2000, cc.getOperationRetryWait());
    AssertJUnit.assertEquals(3, cc.getOperationRetryBackoff());

    final SearchRequest sr = new SearchRequest();
    final SearchRequestPropertySource srSource =
      new SearchRequestPropertySource(
        sr,
        PropertiesTest.class.getResourceAsStream("/ldap.parser.properties"));
    srSource.initialize();

    AssertJUnit.assertEquals("ou=test,dc=vt,dc=edu", sr.getBaseDn());
    AssertJUnit.assertEquals(SearchScope.OBJECT, sr.getSearchScope());
    AssertJUnit.assertEquals(5000, sr.getTimeLimit());
    AssertJUnit.assertEquals("jpegPhoto", sr.getBinaryAttributes()[0]);
    AssertJUnit.assertEquals(5, sr.getPagedResultsControl().getSize());

    for (LdapResultHandler rh : sr.getLdapResultHandlers()) {
      if (RecursiveResultHandler.class.isInstance(rh)) {
        final RecursiveResultHandler h = (RecursiveResultHandler) rh;
        AssertJUnit.assertEquals("member", h.getSearchAttribute());
        AssertJUnit.assertEquals(
          Arrays.asList(new String[] {"mail", "department"}),
          Arrays.asList(h.getMergeAttributes()));
      } else if (MergeResultHandler.class.isInstance(rh)) {
        final MergeResultHandler h = (MergeResultHandler) rh;
        AssertJUnit.assertNotNull(h);
      } else if (DnAttributeResultHandler.class.isInstance(rh)) {
        final DnAttributeResultHandler h = (DnAttributeResultHandler) rh;
        AssertJUnit.assertEquals("myDN", h.getDnAttributeName());
      } else {
        throw new Exception("Unknown search result handler type " + rh);
      }
    }

    AssertJUnit.assertEquals(2, sr.getSearchIgnoreResultCodes().length);
    AssertJUnit.assertEquals(
      ResultCode.SIZE_LIMIT_EXCEEDED, sr.getSearchIgnoreResultCodes()[0]);
    AssertJUnit.assertEquals(
      ResultCode.PARTIAL_RESULTS, sr.getSearchIgnoreResultCodes()[1]);

    final Authenticator auth = new Authenticator();
    final AuthenticatorPropertySource aSource =
      new AuthenticatorPropertySource(
        auth,
        PropertiesTest.class.getResourceAsStream("/ldap.parser.properties"));
    aSource.initialize();

    final ConnectionConfig authCc =
      ((SearchDnResolver) auth.getDnResolver()).getConnectionConfig();
    AssertJUnit.assertEquals(
      "ldap://ed-auth.middleware.vt.edu:14389", authCc.getLdapUrl());
    AssertJUnit.assertEquals("uid=1,ou=test,dc=vt,dc=edu", authCc.getBindDn());
    AssertJUnit.assertEquals(8000, authCc.getTimeout());
    AssertJUnit.assertTrue(authCc.isTlsEnabled());
    AssertJUnit.assertEquals(1, authCc.getProvider().getProperties().size());
    AssertJUnit.assertEquals(
      "true",
      authCc.getProvider().getProperties().get("java.naming.authoritative"));
  }


  /** @throws  Exception  On test failure. */
  @Test(groups = {"propstest"})
  public void jaasProperties()
    throws Exception
  {
    final LoginContext lc = new LoginContext(
      "vt-ldap-props", new TestCallbackHandler());
    lc.login();

    Authenticator auth = null;
    AuthenticationRequest authRequest = null;
    RoleResolver roleResolver = null;
    SearchRequest searchRequest = null;
    for (Object o : lc.getSubject().getPublicCredentials()) {
      if (o instanceof Authenticator) {
        auth = (Authenticator) o;
      } else if (o instanceof AuthenticationRequest) {
        authRequest = (AuthenticationRequest) o;
      } else if (o instanceof RoleResolver) {
        roleResolver = (RoleResolver) o;
      } else if (o instanceof SearchRequest) {
        searchRequest = (SearchRequest) o;
      } else {
        throw new Exception("Unknown public credential found: " + o);
      }
    }

    final ConnectionConfig cc =
      auth.getAuthenticationHandler().getConnectionConfig();

    AssertJUnit.assertNotNull(cc.getProvider().getClass());
    AssertJUnit.assertEquals(
      "ldap://ed-dev.middleware.vt.edu:14389", cc.getLdapUrl());
    AssertJUnit.assertEquals("uid=1,ou=test,dc=vt,dc=edu", cc.getBindDn());
    AssertJUnit.assertEquals(8000, cc.getTimeout());
    AssertJUnit.assertTrue(cc.isTlsEnabled());
    AssertJUnit.assertEquals(1, cc.getProvider().getProperties().size());
    AssertJUnit.assertEquals(
      "true",
      cc.getProvider().getProperties().get("java.naming.authoritative"));
    AssertJUnit.assertEquals(7, cc.getOperationRetry());
    AssertJUnit.assertEquals(2000, cc.getOperationRetryWait());
    AssertJUnit.assertEquals(3, cc.getOperationRetryBackoff());

    AssertJUnit.assertEquals("ou=test,dc=vt,dc=edu", searchRequest.getBaseDn());
    AssertJUnit.assertEquals(
      SearchScope.OBJECT, searchRequest.getSearchScope());
    AssertJUnit.assertEquals(5000, searchRequest.getTimeLimit());
    AssertJUnit.assertEquals(
      "jpegPhoto", searchRequest.getBinaryAttributes()[0]);

    for (LdapResultHandler srh : searchRequest.getLdapResultHandlers()) {
      if (RecursiveResultHandler.class.isInstance(srh)) {
        final RecursiveResultHandler h = (RecursiveResultHandler)
          srh;
        AssertJUnit.assertEquals("member", h.getSearchAttribute());
        AssertJUnit.assertEquals(
          Arrays.asList(new String[] {"mail", "department"}),
          Arrays.asList(h.getMergeAttributes()));
      } else if (MergeResultHandler.class.isInstance(srh)) {
        final MergeResultHandler h = (MergeResultHandler) srh;
        AssertJUnit.assertNotNull(h);
      } else if (DnAttributeResultHandler.class.isInstance(srh)) {
        final DnAttributeResultHandler h = (DnAttributeResultHandler) srh;
        AssertJUnit.assertEquals("myDN", h.getDnAttributeName());
      } else {
        throw new Exception("Unknown search result handler type " + srh);
      }
    }

    AssertJUnit.assertEquals(
      2, searchRequest.getSearchIgnoreResultCodes().length);
    AssertJUnit.assertEquals(
      ResultCode.SIZE_LIMIT_EXCEEDED,
      searchRequest.getSearchIgnoreResultCodes()[0]);
    AssertJUnit.assertEquals(
      ResultCode.PARTIAL_RESULTS,
      searchRequest.getSearchIgnoreResultCodes()[1]);

    final ConnectionConfig authLcc =
      ((SearchDnResolver) auth.getDnResolver()).getConnectionConfig();
    AssertJUnit.assertEquals(
      "ldap://ed-dev.middleware.vt.edu:14389", authLcc.getLdapUrl());
    AssertJUnit.assertEquals("uid=1,ou=test,dc=vt,dc=edu", authLcc.getBindDn());
    AssertJUnit.assertEquals(8000, authLcc.getTimeout());
    AssertJUnit.assertTrue(authLcc.isTlsEnabled());
    AssertJUnit.assertEquals(1, authLcc.getProvider().getProperties().size());
    AssertJUnit.assertEquals(
      "true",
      authLcc.getProvider().getProperties().get("java.naming.authoritative"));

    AssertJUnit.assertEquals(
      edu.vt.middleware.ldap.auth.handler.CompareAuthenticationHandler.class,
      auth.getAuthenticationHandler().getClass());
    AssertJUnit.assertEquals(
      edu.vt.middleware.ldap.auth.PersistentSearchDnResolver.class,
      auth.getDnResolver().getClass());
  }
}
