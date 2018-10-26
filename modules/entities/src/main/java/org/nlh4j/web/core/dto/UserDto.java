/*
 * @(#)UserDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.dto;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.util.LocaleUtils;
import org.nlh4j.util.StringUtils;
import org.nlh4j.web.core.domain.entity.UserEx;

/**
 * User DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
public class UserDto extends UserEx implements UserDetails, AttributesMapper<UserDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /** Login user MD5 password */
    private String password;
    @JsonProperty()
    public void setPassword(String password) {
        this.password = password;
    }
    @JsonIgnore()
    public String getPassword() {
        return this.password;
    }
    /** USER roles */
    @Setter
    private Set<RoleDto> roles;
    @JsonIgnore()
    public Set<RoleDto> getRoles() {
        return this.roles;
    }
    /** USER LDAP attributes */
    @Setter
    private Map<String, Attribute> attributes;
    @JsonIgnore()
    public Map<String, Attribute> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap<String, Attribute>();
        }
        return this.attributes;
    }

    /* (Non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    @Override
    @JsonIgnore()
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<RoleDto> roles = this.getRoles();
        roles = (CollectionUtils.isEmpty(roles) ? Collections.emptySet() : roles);
        return Collections.unmodifiableCollection(roles);
    }

    /* (Non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return (this.isAccountNonLocked() && this.isCredentialsNonExpired());
    }

    /* (Non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.isEnabled();
    }

    /* (Non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return (this.getExpiredAt() == null
                || (DateUtils.compareTo(
                        this.getExpiredAt(),
                        DateUtils.currentTimestamp(),
                        Calendar.SECOND) < 0));
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.UserDetails#isSystemAdmin()
     */
    @Override
    public boolean isSystemAdmin() {
        return super.isSysadmin();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.UserDetails#getLocale()
     */
    @Override
    @JsonIgnore
    public Locale getLocale() {
        Locale locale = null;
        try { locale = LocaleUtils.toLocale(this.getLanguage()); }
        catch(Exception e) { locale = null; }
        return locale;
    }
    public String getDisplayLanguage() {
        Locale locale = this.getLocale();
        return (locale == null ? "" : locale.getDisplayLanguage());
    }

    /* (Non-Javadoc)
     * @see org.springframework.ldap.core.AttributesMapper#mapFromAttributes(javax.naming.directory.Attributes)
     */
    @Override
    public UserDto mapFromAttributes(Attributes attributes) throws NamingException {
        if (this.attributes == null) {
            this.attributes = new LinkedHashMap<String, Attribute>();
        }
        this.attributes.clear();
        if (attributes != null) {
            NamingEnumeration<? extends Attribute> ne = attributes.getAll();
            while(ne.hasMore()) {
                Attribute attribute = ne.next();
                this.attributes.put(attribute.getID(), attribute);
            }
        }
        return this;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.UserDetails#getRememberTokenSignatures()
     */
    @Override
    @JsonIgnore()
    public String[] getRememberTokenSignatures() {
        return new String[] { (!StringUtils.hasText(this.getEmail()) ? "" : this.getEmail()) };
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.UserDetails#getCredentials()
     */
    @Override
    @JsonIgnore()
    public Object getCredentials() {
        return new String[] { this.getPassword() };
    }
}
