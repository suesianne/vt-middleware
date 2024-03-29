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
package edu.vt.middleware.ldap.jaas;

import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import com.sun.security.auth.callback.TextCallbackHandler;
import edu.vt.middleware.ldap.Ldap;
import edu.vt.middleware.ldap.SearchFilter;

/**
 * <code>LdapRoleAuthorizationModule</code> provides a JAAS authentication hook
 * into LDAP roles. No authentication is performed in this module. Role data is
 * set for the login name in the shared state or for the name returned by the
 * CallbackHandler.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class LdapRoleAuthorizationModule extends AbstractLoginModule
  implements LoginModule
{

  /** Ldap filter for role searches. */
  private String roleFilter;

  /** Role attribute to add to role data. */
  private String[] roleAttribute = new String[0];

  /** Whether failing to find any roles should raise an exception. */
  private boolean noResultsIsError;

  /** Ldap to use for searching roles against the LDAP. */
  private Ldap ldap;


  /** {@inheritDoc} */
  public void initialize(
    final Subject subject,
    final CallbackHandler callbackHandler,
    final Map<String, ?> sharedState,
    final Map<String, ?> options)
  {
    super.initialize(subject, callbackHandler, sharedState, options);

    final Iterator<String> i = options.keySet().iterator();
    while (i.hasNext()) {
      final String key = i.next();
      final String value = (String) options.get(key);
      if (key.equalsIgnoreCase("roleFilter")) {
        this.roleFilter = value;
      } else if (key.equalsIgnoreCase("roleAttribute")) {
        if ("*".equals(value)) {
          this.roleAttribute = null;
        } else {
          this.roleAttribute = value.split(",");
        }
      } else if (key.equalsIgnoreCase("noResultsIsError")) {
        this.noResultsIsError = Boolean.valueOf(value);
      }
    }

    if (this.logger.isDebugEnabled()) {
      this.logger.debug("roleFilter = " + this.roleFilter);
      this.logger.debug(
        "roleAttribute = " + Arrays.toString(this.roleAttribute));
      this.logger.debug("noResultsIsError = " + this.noResultsIsError);
    }

    this.ldap = createLdap(options);
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Created ldap: " + this.ldap.getLdapConfig());
    }
  }


  /** {@inheritDoc} */
  public boolean login()
    throws LoginException
  {
    try {
      final NameCallback nameCb = new NameCallback("Enter user: ");
      final PasswordCallback passCb = new PasswordCallback(
        "Enter user password: ",
        false);
      this.getCredentials(nameCb, passCb, false);

      if (nameCb.getName() == null && this.tryFirstPass) {
        this.getCredentials(nameCb, passCb, true);
      }

      final String loginName = nameCb.getName();
      if (loginName != null && this.setLdapPrincipal) {
        this.principals.add(new LdapPrincipal(loginName));
        this.success = true;
      }

      final String loginDn = (String) this.sharedState.get(LOGIN_DN);
      if (loginDn != null && this.setLdapDnPrincipal) {
        this.principals.add(new LdapDnPrincipal(loginDn));
        this.success = true;
      }

      if (this.roleFilter != null) {
        final Object[] filterArgs = new Object[] {loginDn, loginName, };
        final Iterator<SearchResult> results = this.ldap.search(
          new SearchFilter(this.roleFilter, filterArgs),
          this.roleAttribute);
        if (!results.hasNext() && this.noResultsIsError) {
          this.success = false;
          throw new LoginException(
            "Could not find roles using " + this.roleFilter);
        }
        while (results.hasNext()) {
          final SearchResult sr = results.next();
          this.roles.addAll(this.attributesToRoles(sr.getAttributes()));
        }
      }
      if (this.defaultRole != null && !this.defaultRole.isEmpty()) {
        this.roles.addAll(this.defaultRole);
      }
      if (!this.roles.isEmpty()) {
        this.success = true;
      }
      this.storeCredentials(nameCb, passCb, null);
    } catch (NamingException e) {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Error occured attempting role lookup", e);
      }
      this.success = false;
      throw new LoginException(e.getMessage());
    } finally {
      this.ldap.close();
    }
    return true;
  }


  /**
   * This provides command line access to a <code>LdapRoleLoginModule</code>.
   *
   * @param  args  <code>String[]</code>
   *
   * @throws  Exception  if an error occurs
   */
  public static void main(final String[] args)
    throws Exception
  {
    String name = "vt-ldap-role";
    if (args.length > 0) {
      name = args[0];
    }

    final LoginContext lc = new LoginContext(name, new TextCallbackHandler());
    lc.login();
    System.out.println("Authorization succeeded");

    final Set<Principal> principals = lc.getSubject().getPrincipals();
    System.out.println("Subject Principal(s): ");

    final Iterator<Principal> i = principals.iterator();
    while (i.hasNext()) {
      final Principal p = i.next();
      System.out.println("  " + p.getName());
    }
    lc.logout();
  }
}
