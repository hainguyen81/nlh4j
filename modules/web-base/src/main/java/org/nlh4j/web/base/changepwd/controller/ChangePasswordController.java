/*
 * @(#)ChangePasswordController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.controller.MasterPageType;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.web.base.changepwd.dto.UserDto;
import org.nlh4j.web.base.changepwd.dto.UserEntityParamControllerDto;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.base.changepwd.service.ChangePasswordService;
import org.nlh4j.web.base.common.AppConst;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractActionController;

/**
 * Change current USER password controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping(value = { "/chgpwd" })
@ExecutePermission(value = { ModuleConst.CMN_CHANGE_PASSWORD })
public class ChangePasswordController
    extends AbstractActionController
    <UserDto, UserUniqueDto, UserEntityParamControllerDto, ChangePasswordService, AbstractDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ChangePasswordController}
     */
    public ChangePasswordController() {
        super(ChangePasswordService.class, Module.CMN_CHANGE_PASSWORD);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#attachModelAndView(org.springframework.web.servlet.ModelAndView, org.nlh4j.core.controller.AbstractMasterController.PAGE)
     */
    @Override
    protected ModelAndView attachModelAndView(ModelAndView mav, MasterPageType mode) {
        if (MasterPageType.UPDATE.equals(mode)) {
            mav.addObject("minPassLength", AppConst.findMinPasswordLength());
        }
        return super.attachModelAndView(mav, mode);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.MasterActionController#getEntityPk(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    protected UserUniqueDto getEntityPk(UserDto entity) {
        UserUniqueDto unique = null;
        if (entity != null) {
            unique = new UserUniqueDto();
            unique.setId(entity.getId());
            unique.setUsername(entity.getUsername());
        }
        return unique;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#validateEntity(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    protected boolean validateEntity(UserDto entity, boolean create) {
        UserDto sessudto = this.findEntityBy(this.getEntityPk(entity));
        if (sessudto != null) entity.setPassword(sessudto.getPassword());
        return (!create
                && StringUtils.hasText(entity.getCurrentPassword())
                && EncryptUtils.md5(entity.getCurrentPassword()).equals(entity.getPassword())
                && StringUtils.hasText(entity.getNewPassword())
                && entity.getNewPassword().length() >= AppConst.findMinPasswordLength()
                && entity.getNewPassword().equals(entity.getRetypePassword()));
    }
}
