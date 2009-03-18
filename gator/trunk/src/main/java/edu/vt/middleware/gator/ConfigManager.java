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
package edu.vt.middleware.gator;

import java.util.List;

import org.springframework.security.annotation.Secured;

/**
 * Manages the lifecycle (CRUD) operations on config objects.
 *
 * @author Middleware
 * @version $Revision$
 *
 */
public interface ConfigManager
{

  /**
   * Sets the listeners that will receive project configuration change messages.
   * @param listeners Registered listeners.
   */
  void setConfigChangeListeners(List<ConfigChangeListener> listeners);

  /**
   * Fetches all of the configuration objects of the given type in the
   * underlying data store.
   * @param type Type of configuration object.
   * @return List of all existing configuration objects of given type in
   * underlying data store.
   */
  <T extends Config> List<T> findAll(Class<T> type);

  /**
   * Fetches a configuration object from persistent storage by its ID.
   * @param type Type of configuration object.
   * @param id ID of object to load.
   * @return Config object or null if none exists for given type/ID.
   */
  @Secured("ACL_PROJECT_READ")
  <T extends Config> T find(Class<T> type, int id);

  /**
   * Fetches a project configuration object from persistent storage by name
   * (case-insensitive).
   * @param name Project name.
   * @return Project configuration object or null if none exists for given name.
   */
  @Secured("ACL_PROJECT_READ")
  ProjectConfig findProject(String name);

  /**
   * Fetches all projects that contain the given client name (case-insensitive).
   * @param name Client name.
   * @return List of projects that contain the given client name.
   */
  List<ProjectConfig> findProjectsByClientName(String name);
  
  /**
   * Gets the project to which the configuration object belongs.
   * @param config Configuration object.
   * @return Project configuration containing given configuration item or
   * null if no project is found. 
   */
  ProjectConfig getProject(Config config);

  /**
   * Determines whether a config object exists in persistent storage.
   * @param config Config object to test for existence.
   * @return True if given object exists in persistent storage, false otherwise.
   */
  boolean exists(Config config);

  /**
   * Saves changes to the given object or creates it if it does not exist.
   * @param config Object to save.
   */
  @Secured("ACL_PROJECT_EDIT")
  void save(Config config);
  
  /**
   * Deletes the given configuration object(s) from persistent storage.
   * @param objects One or more configuration objects.
   */
  @Secured("ACL_PROJECT_DELETE")
  void delete(Config ... objects);
}
