/*
 * @(#)UserEx.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.entity;

import java.sql.Timestamp;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.util.StringUtils;

/**
 * User DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
//@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class UserEx extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** USER identity */
    @Getter
    @Setter
    private Long id;
    /** USER ROLES GROUP identity */
    @Getter
    @Setter
    private Long gid;
    /** USER ROLES GROUP code */
    @Getter
    @Setter
    private String groupCode;
    /** USER EMPLOYEE identity */
    @Getter
    @Setter
    private Long eid;
    /** USER EMPLOYEE code */
    @Getter
    @Setter
    private String employeeCode;
    /** USER EMPLOYEE call_name */
    @Getter
    @Setter
    private String employeeCallName;
    /** USER EMPLOYEE first_name */
    @Getter
    @Setter
    private String employeeFirstName;
    /** USER EMPLOYEE last_name */
    @Getter
    @Setter
    private String employeeLastName;
    /** USER EMPLOYEE name */
    @Getter
    @Setter
    private String employeeName;
    /** USER DEPARTMENT code */
    @Getter
    @Setter
    private String departmentCode;
    /** USER DEPARTMENT name */
    @Getter
    @Setter
    private String departmentName;
    /** USER EMPLOYEE name */
    @Getter
    @Setter
    private String employeeTel;
    /** Login user name */
    @Setter
    private String userName;
    public String getUserName() {
    	return (!StringUtils.hasText(this.userName)
    			? this.username : this.userName);
    }
    /** Login user MD5 password */
    @Getter
    @Setter
    private String password;
    /** Login user email */
    @Getter
    @Setter
    private String email;
    /** Specify this user whether can login */
    @Getter
    @Setter
    private boolean enabled;
    /** Specify this user whether is system administrator */
    @Getter
    @Setter
    private boolean sysadmin;
    /** USER language settings */
    @Getter
    @Setter
    private String language;
    /** USER description */
    @Getter
    @Setter
    private String description;
    /** USER login date */
    @Getter
    @Setter
    private Timestamp loginAt;
    /** USER login date */
    @Getter
    @Setter
    private Timestamp expiredAt;
    /** */
    @Getter
    @Setter
    private Timestamp createdAt;
    /** */
    @Getter
    @Setter
    private Long createdUser;
    /** */
    @Setter
    private String username;
    /**
     * Get the user name (an alias of {@link #getUserName()} to use for {@link UserDetails} interface)
     * @return the user name
     */
    public String getUsername() {
    	return (!StringUtils.hasText(this.userName)
    			? this.username : this.userName);
    }

    /* (Non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    /* (Non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserEx other = (UserEx) obj;
        if (password == null) {
            if (other.password != null) return false;
        }
        else if (!password.equals(other.password)) return false;
        if (userName == null) {
            if (other.userName != null) return false;
        }
        else if (!userName.equals(other.userName)) return false;
        return true;
    }
}
