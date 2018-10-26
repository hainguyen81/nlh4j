/*
 * @(#)DashboardController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.dashboard.controller;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.controller.MasterPageType;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractSearchController;
import org.nlh4j.web.base.dashboard.dto.ModuleSearchConditions;
import org.nlh4j.web.base.dashboard.dto.ModuleSearchParamControllerDto;
import org.nlh4j.web.base.dashboard.dto.SidebarDto;
import org.nlh4j.web.base.dashboard.service.DashboardService;
import org.nlh4j.web.base.util.SidebarUtils;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * Dashboard controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping(value = { "", "/index", "/home", "/dashboard" })
@ExecutePermission(value = {
		ModuleConst.CMN_DASHBOARD,
		ModuleConst.CMN_DASHBOARD_MASTER,
		ModuleConst.CMN_DASHBOARD_SYSTEM,
		ModuleConst.CMN_HOME
})
public class DashboardController
    extends AbstractSearchController
    <ModuleDto, ModuleSearchConditions, ModuleSearchParamControllerDto, DashboardService, AbstractDto> {

	/** */
	private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link DashboardController}
     */
    public DashboardController() {
        super(DashboardService.class, Module.CMN_DASHBOARD);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#attachModelAndView(org.springframework.web.servlet.ModelAndView, org.nlh4j.core.controller.AbstractMasterController.PAGE)
     */
    @Override
    protected ModelAndView attachModelAndView(ModelAndView mav, MasterPageType mode) {
        if (MasterPageType.HOME.equals(mode)) {
            // check request module for showing dasboard home or dashboard module home
            // if not current controller URI
            ModuleDto module = null;
            String refererURI = RequestUtils.getRefererURI(super.getRequest());
            boolean home = RequestUtils.getBooleanParameter(super.getRequest(), "h", 1);
            if (!home && StringUtils.hasText(refererURI) && !refererURI.startsWith("/dashboard")) {
                module = super.getMasterService().selectParentModuleOf(refererURI, Boolean.TRUE, Boolean.TRUE);
            }
            mav.addObject("menuModule", (module == null || module.getId() == null ? 0 : module.getId()));
        }
        return super.attachModelAndView(mav, mode);
    }

	/**
	 * Search all modules list
	 *
	 * @return the modules list
	 */
	@RequestMapping(value = { "/sidebar" }, method = { RequestMethod.POST, RequestMethod.GET })
	public List<SidebarDto> searchAllModules() {
		return SidebarUtils.parseSidebarData(super.getMasterService().searchUIModulesList(null));
	}
}