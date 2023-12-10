/*
 * @(#)ChangePasswordController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.controller.MasterPageType;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.web.base.changepwd.dto.UserDto;
import org.nlh4j.web.base.changepwd.dto.UserEntityParamControllerDto;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.base.changepwd.service.ChangePasswordService;
import org.nlh4j.web.base.common.AppConst;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractActionController;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

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

    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#view(org.nlh4j.core.dto.AbstractDto, org.springframework.validation.BindingResult)
     */
    @Override
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView view(UserUniqueDto entitypk, BindingResult result) {
    	org.nlh4j.web.core.dto.UserDto sessUdto =
                AuthenticationUtils.getCurrentProfile(org.nlh4j.web.core.dto.UserDto.class);
    	return super.view(
    			Objects.requireNonNullElseGet(entitypk,
    					() -> Optional.ofNullable(sessUdto)
    					.map(u -> new UserUniqueDto(u.getId(), u.getUsername())).orElse(null)),
    			result);
    }
    
    @Override
    public ModelAndView edit(UserUniqueDto entitypk, BindingResult result) {
    	org.nlh4j.web.core.dto.UserDto sessUdto =
                AuthenticationUtils.getCurrentProfile(org.nlh4j.web.core.dto.UserDto.class);
    	return super.edit(
    			Objects.requireNonNullElseGet(entitypk,
    					() -> Optional.ofNullable(sessUdto)
    					.map(u -> new UserUniqueDto(u.getId(), u.getUsername())).orElse(null)),
    			result);
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#create()
     */
    @Deprecated
    @Override
    public ModelAndView create() {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#create(org.nlh4j.core.dto.AbstractDto, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> create(UserDto entity, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#createModel(org.nlh4j.core.dto.AbstractDto, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> createModel(UserDto entity, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#delete(org.nlh4j.core.dto.BaseEntityParamControllerDto, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> delete(UserEntityParamControllerDto entitypk, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#deleteModel(org.nlh4j.core.dto.BaseEntityParamControllerDto, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> deleteModel(UserEntityParamControllerDto entitypk, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#download(org.nlh4j.core.dto.AbstractDto, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> download(AbstractDto data, HttpServletResponse response, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#downloadBody(org.nlh4j.core.dto.AbstractDto, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> downloadBody(AbstractDto data, HttpServletResponse response, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#searchEntity(org.nlh4j.core.dto.BaseEntityParamControllerDto, org.springframework.validation.BindingResult)
     */
    @Deprecated
    @Override
    public UserDto searchEntity(UserEntityParamControllerDto entitypk, BindingResult result) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#upload(org.nlh4j.core.dto.AbstractDto, java.util.List, org.springframework.web.multipart.MultipartHttpServletRequest)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> upload(AbstractDto data, List<MultipartFile> files,
    		MultipartHttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#uploadBody(org.nlh4j.core.dto.AbstractDto, java.util.List, org.springframework.web.multipart.MultipartHttpServletRequest)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> uploadBody(AbstractDto data, List<MultipartFile> files,
    		MultipartHttpServletRequest request) {
    	throw new UnsupportedOperationException();
    }
    
    /*
     * (non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#uploadParam(org.nlh4j.core.dto.AbstractDto, java.util.List, org.springframework.web.multipart.MultipartHttpServletRequest)
     */
    @Deprecated
    @Override
    public ResponseEntity<String> uploadParam(AbstractDto data, List<MultipartFile> files,
    		MultipartHttpServletRequest request) {
    	throw new UnsupportedOperationException();
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
