/*
  $Id$

  Copyright (C) 2008 Virginia Tech, Marvin S. Addison.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Marvin S. Addison
  Email:   serac@vt.edu
  Version: $Revision$
  Updated: $Date$
 */
package edu.vt.middleware.gator;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Abstract configuration class from which all concrete configuration classes
 * derive.
 *
 * @author Marvin S. Addison
 *
 */
@MappedSuperclass
public abstract class Config
{
  protected int id;

  protected String name;
  
  
  /**
   * @return the id
   */
  @Transient
  public abstract int getId();

  /**
   * @param id the id to set
   */
  protected void setId(final int id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  @Column(name = "name", nullable = false)
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return String.format("%s::%s", getClass().getSimpleName(), getName());
  }
  
  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    if (name != null) {
      return 31 * getHashCodeSeed() + name.hashCode();
    } else {
      return getHashCodeSeed();
    }
  }
 
  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o)
  {
    if (o == null) {
      return false;
    } else if (o == this) {
      return true;
    } else {
      return getClass() == o.getClass() && hashCode() == o.hashCode();
    }
  }
 
  /**
   * Gets the hash code seed for this class.
   * @return Hash code seed for this class;
   */
  @Transient
  protected abstract int getHashCodeSeed();
}
