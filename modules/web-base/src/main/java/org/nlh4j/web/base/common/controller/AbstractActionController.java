/*
 * @(#)AbstractActionController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.controller.MasterActionController;
import org.nlh4j.core.controller.MasterPageType;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseEntityParamControllerDto;
import org.nlh4j.core.service.MasterService;
import org.nlh4j.util.StringUtils;
import org.nlh4j.web.base.common.Module;

/**
 * Abstract controller for inserting/updating/deleting on master screens
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <T> the main entity type
 * @param <U> the bound class of entity primary/unique key
 * @param <PK> the entity primary/unique key class
 * @param <Srv> the {@link MasterService} class
 * @param <M> the attached upload data type
 */
@RestController
public abstract class AbstractActionController
    <T extends AbstractDto, PK extends AbstractDto,
    U extends BaseEntityParamControllerDto<PK>,
    Srv extends MasterService<T, AbstractDto, PK>,
    M extends AbstractDto>
    extends MasterActionController<T, PK, U, Srv, M> {

    /** */
    private static final long serialVersionUID = 1L;
    private Module module;

    /**
     * Initialize a new instance of {@link AbstractActionController}
     *
     * @param <M> the {@link Module} type
     * @param masterServiceClass master service class
     * @param module system module
     */
    protected <Mod extends Module> AbstractActionController(Class<Srv> masterServiceClass, Mod module) {
        super(masterServiceClass);
        this.module = module;
    }

    /**
     * Get the controller module
     *
     * @return the controller module
     */
    protected final Module getModule() {
        return this.module;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#getDetailPage()
     */
    @Override
    protected String getDetailPage() {
        return this.getModule().getDetailPage();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#attachModelAndView(org.springframework.web.servlet.ModelAndView, org.nlh4j.core.controller.MasterPageType)
     */
    @Override
    protected ModelAndView attachModelAndView(ModelAndView mav, MasterPageType mode) {
        // attach module, page code
        if (mav != null) {
            mav.addObject("module", this.getModule().getCode());
            mav.addObject("pageModule", this.getModule().getFixedCode());
            // attach page module
            if (mode != null && !MasterPageType.HOME.equals(mode)) {
                mav.addObject("pageName", this.getModule().getFixedPageCode(2));
            } else {
             // parse page name from view name
                String viewName = mav.getViewName();
                if (StringUtils.hasText(viewName)) {
                    // split by /
                    int slashIdx = viewName.lastIndexOf("/");
                    if (slashIdx >= 0) {
                        viewName = viewName.substring(slashIdx + 1, viewName.length());
                    }
                    mav.addObject("pageName", viewName);

                    // using view name with enumeration ordinal
                } else {
                    mav.addObject("pageName", this.getModule().getFixedPageCode(mode.ordinal() + 1));
                }
            }
        }
        // call super class
        return super.attachModelAndView(mav, mode);
    }
}
