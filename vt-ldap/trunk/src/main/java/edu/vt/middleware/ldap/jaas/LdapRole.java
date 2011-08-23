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

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import edu.vt.middleware.ldap.LdapAttribute;
import edu.vt.middleware.ldap.LdapEntry;
import edu.vt.middleware.ldap.LdapResult;

/**
 * Provides a custom implementation for adding LDAP principals to a subject
 * that represent roles.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class LdapRole implements Principal, Serializable, Comparable<Principal>
{

  /** serial version uid. */
  private static final long serialVersionUID = 1427032827399935399L;

  /** LDAP role name. */
  private String name;


  /**
   * Creates a new ldap role with the supplied name.
   *
   * @param  s  name of this role
   */
  public LdapRole(final String s)
  {
    name = s;
  }


  /**
   * Returns the name for this ldap role.
   *
   * @return  role name
   */
  public String getName()
  {
    return name;
  }


  /**
   * Returns whether the supplied object is equal to this ldap role.
   *
   * @param  o  to compare
   *
   * @return  whether the supplied object is equal
   */
  public boolean equals(final Object o)
  {
    boolean b = false;
    if (o != null) {
      if (this != o) {
        if (o instanceof LdapRole) {
          if (((LdapRole) o).getName().equals(name)) {
            b = true;
          }
        }
      } else {
        b = true;
      }
    }
    return b;
  }


  /**
   * Returns the hash code for this ldap role.
   *
   * @return  hash code
   */
  public int hashCode()
  {
    return name.hashCode();
  }


  /**
   * Provides a descriptive string representation of this instance.
   *
   * @return  string representation
   */
  @Override
  public String toString()
  {
    return String.format(
      "[%s@%d::%s]",
      getClass().getName(),
      hashCode(),
      name);
  }


  /**
   * Principals are compared lexicographically by name. See
   * {@link Comparable#compareTo(Object)}.
   *
   * @param  p  principal to compare
   *
   * @return  a negative integer, zero, or a positive integer as this object is
   * less than, equal to, or greater than the specified object.
   */
  public int compareTo(final Principal p)
  {
    return name.compareTo(p.getName());
  }


  /**
   * Iterates over the supplied result and returns all attributes as a set of
   * ldap roles.
   *
   * @param  result  to read
   *
   * @return  ldap roles
   */
  public static Set<LdapRole> toRoles(final LdapResult result)
  {
    final Set<LdapRole> r = new HashSet<LdapRole>();
    for (LdapEntry le : result.getEntries()) {
      r.addAll(toRoles(le));
    }
    return r;
  }


  /**
   * Iterates over the supplied entry and returns all attributes as a set of
   * ldap roles.
   *
   * @param  entry  to read
   *
   * @return  ldap roles
   */
  public static Set<LdapRole> toRoles(final LdapEntry entry)
  {
    return toRoles(entry.getAttributes());
  }


  /**
   * Iterates over the supplied attributes and returns all values as a set of
   * ldap roles.
   *
   * @param  attributes  to read
   *
   * @return  ldap roles
   */
  public static Set<LdapRole> toRoles(
    final Collection<LdapAttribute> attributes)
  {
    final Set<LdapRole> r = new HashSet<LdapRole>();
    if (attributes != null) {
      for (LdapAttribute ldapAttr : attributes) {
        for (String attrValue : ldapAttr.getStringValues()) {
          r.add(new LdapRole(attrValue));
        }
      }
    }
    return r;
  }
}
