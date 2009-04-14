/*
  $Id$

  Copyright (C) 2008-2009 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.gator.web;

import edu.vt.middleware.gator.Config;
import edu.vt.middleware.gator.ProjectConfig;

/**
 * Represents a request to delete a configuration object.
 *
 * @author Middleware
 * @version $Revision$
 *
 */
public class DeleteSpec
{
  private ProjectConfig project;
  
  private Config configToBeDeleted;

  private boolean confirmationFlag;
  
  private String typeName;
  

  /**
   * @return the project
   */
  public ProjectConfig getProject()
  {
    return project;
  }

  /**
   * @param p the project to set
   */
  public void setProject(final ProjectConfig p)
  {
    project = p;
  }

  /**
   * @return the config
   */
  public Config getConfigToBeDeleted()
  {
    return configToBeDeleted;
  }

  /**
   * @param config the config to set
   */
  public void setConfigToBeDeleted(final Config config)
  {
    configToBeDeleted = config;
  }

  /**
   * @return the confirmationFlag
   */
  public boolean getConfirmationFlag()
  {
    return confirmationFlag;
  }

  /**
   * @param confirm the confirmationFlag to set
   */
  public void setConfirmationFlag(final boolean confirm)
  {
    this.confirmationFlag = confirm;
  }


  /**
   * @param typeName the typeName to set
   */
  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }

  /**
   * @return the typeName
   */
  public String getTypeName()
  {
    return typeName;
  }


}
