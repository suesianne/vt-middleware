/*
  $Id$

  Copyright (C) 2009-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.gator.web;

import javax.validation.Valid;
import edu.vt.middleware.gator.AppenderConfig;
import edu.vt.middleware.gator.AppenderParamConfig;
import edu.vt.middleware.gator.LayoutParamConfig;
import edu.vt.middleware.gator.ProjectConfig;
import edu.vt.middleware.gator.web.support.ParametersEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Handles appender configuration changes.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
@Controller
@RequestMapping("/secure")
@SessionAttributes("appender")
public class AppenderEditFormController extends AbstractFormController
{
  public static final String VIEW_NAME = "appenderEdit";


  @InitBinder
  public void initAppenderEditors(final WebDataBinder binder)
  {
    logger.trace("Registering custom data binders for appender edits.");
    // Set requiredType to null to avoid property value conversion done
    // by Spring that causes ClassCastException in this case
    binder.registerCustomEditor(
      null,
      "appenderParams",
      new ParametersEditor<AppenderParamConfig>(AppenderParamConfig.class));
    binder.registerCustomEditor(
      null,
      "layoutParams",
      new ParametersEditor<LayoutParamConfig>(LayoutParamConfig.class));
  }


  @RequestMapping(
    value = "/project/{projectName}/appender/add.html",
    method = RequestMethod.GET
  )
  public String getNewAppender(
    @PathVariable("projectName") final String projectName,
    final Model model)
  {
    final ProjectConfig project = getProject(projectName);
    // Touch appenders to force lazy load so they're accessible during
    // validation
    project.getAppenders();

    final AppenderConfig appender = new AppenderConfig();
    appender.setProject(project);
    model.addAttribute("appender", appender);
    return VIEW_NAME;
  }


  @RequestMapping(
    value = "/project/{projectName}/appender/{appenderId}/edit.html",
    method = RequestMethod.GET
  )
  public String getAppender(
    @PathVariable("projectName") final String projectName,
    @PathVariable("appenderId") final int appenderId,
    final Model model)
  {
    final AppenderConfig appender = getProject(projectName).getAppender(
      appenderId);
    if (appender == null) {
      throw new IllegalArgumentException(
        String.format(
          "Appender ID=%s not found in project '%s'.",
          appenderId,
          projectName));
    }
    model.addAttribute("appender", appender);
    return VIEW_NAME;
  }


  @RequestMapping(
    value = {
      "/project/{projectName}/appender/add.html",
      "/project/{projectName}/appender/{appenderId}/edit.html"
    },
    method = RequestMethod.POST
  )
  @Transactional(propagation = Propagation.REQUIRED)
  public String saveAppender(
    @Valid
    @ModelAttribute("appender")
    final AppenderConfig appender,
    final BindingResult result)
  {
    if (result.hasErrors()) {
      return VIEW_NAME;
    }
    if (!configManager.exists(appender)) {
      appender.getProject().addAppender(appender);
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Saving " + appender);
    }
    configManager.save(appender.getProject());
    return
      String.format(
        "redirect:/secure/project/%s/edit.html#appender",
        appender.getProject().getName());
  }
}
