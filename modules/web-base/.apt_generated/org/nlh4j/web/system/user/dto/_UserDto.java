package org.nlh4j.web.system.user.dto;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T22:32:03.214+0700")
public final class _UserDto extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.system.user.dto.UserDto> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _UserDto __singleton = new _UserDto();

    /** the id */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$id, null, "id", "ID", true, true);

    /** the gid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object> $gid = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$gid, null, "gid", "GID", true, true);

    /** the groupCode */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $groupCode = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$groupCode, null, "groupCode", "GROUP_CODE", true, true);

    /** the eid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object> $eid = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$eid, null, "eid", "EID", true, true);

    /** the employeeCode */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeCode = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeCode, null, "employeeCode", "EMPLOYEE_CODE", true, true);

    /** the employeeCallName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeCallName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeCallName, null, "employeeCallName", "EMPLOYEE_CALL_NAME", true, true);

    /** the employeeFirstName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeFirstName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeFirstName, null, "employeeFirstName", "EMPLOYEE_FIRST_NAME", true, true);

    /** the employeeLastName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeLastName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeLastName, null, "employeeLastName", "EMPLOYEE_LAST_NAME", true, true);

    /** the employeeName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeName, null, "employeeName", "EMPLOYEE_NAME", true, true);

    /** the departmentCode */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $departmentCode = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$departmentCode, null, "departmentCode", "DEPARTMENT_CODE", true, true);

    /** the departmentName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $departmentName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$departmentName, null, "departmentName", "DEPARTMENT_NAME", true, true);

    /** the employeeTel */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $employeeTel = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$employeeTel, null, "employeeTel", "EMPLOYEE_TEL", true, true);

    /** the userName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $userName = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$userName, null, "userName", "USER_NAME", true, true);

    /** the password */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $password = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$password, null, "password", "PASSWORD", true, true);

    /** the email */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $email = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$email, null, "email", "EMAIL", true, true);

    /** the enabled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object> $enabled = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$enabled, null, "enabled", "ENABLED", true, true);

    /** the sysadmin */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object> $sysadmin = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$sysadmin, null, "sysadmin", "SYSADMIN", true, true);

    /** the language */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $language = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$language, null, "language", "LANGUAGE", true, true);

    /** the description */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $description = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$description, null, "description", "DESCRIPTION", true, true);

    /** the loginAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object> $loginAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$loginAt, null, "loginAt", "LOGIN_AT", true, true);

    /** the expiredAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object> $expiredAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$expiredAt, null, "expiredAt", "EXPIRED_AT", true, true);

    /** the createdAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object> $createdAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$createdAt, null, "createdAt", "CREATED_AT", true, true);

    /** the createdUser */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object> $createdUser = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$createdUser, null, "createdUser", "CREATED_USER", true, true);

    /** the username */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $username = new org.seasar.doma.jdbc.entity.BasicPropertyType<org.nlh4j.web.core.domain.entity.UserEx, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, org.nlh4j.web.core.domain.entity._UserEx.getSingletonInternal().$username, null, "username", "USERNAME", true, true);

    /** the groupName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object> $groupName = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, java.lang.String, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "groupName", "GROUP_NAME", true, true);

    /** the changepwd */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object> $changepwd = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.system.user.dto.UserDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "changepwd", "CHANGEPWD", true, true);

    private final org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.system.user.dto.UserDto> __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __entityPropertyTypeMap;

    private _UserDto() {
        __listener = new org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.system.user.dto.UserDto>();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.SNAKE_UPPER_CASE;
        __immutable = false;
        __name = "UserDto";
        __catalogName = "";
        __schemaName = "";
        __tableName = "USER_DTO";
        __qualifiedTableName = "USER_DTO";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>>(26);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>>(26);
        __list.add($id);
        __map.put("id", $id);
        __list.add($gid);
        __map.put("gid", $gid);
        __list.add($groupCode);
        __map.put("groupCode", $groupCode);
        __list.add($eid);
        __map.put("eid", $eid);
        __list.add($employeeCode);
        __map.put("employeeCode", $employeeCode);
        __list.add($employeeCallName);
        __map.put("employeeCallName", $employeeCallName);
        __list.add($employeeFirstName);
        __map.put("employeeFirstName", $employeeFirstName);
        __list.add($employeeLastName);
        __map.put("employeeLastName", $employeeLastName);
        __list.add($employeeName);
        __map.put("employeeName", $employeeName);
        __list.add($departmentCode);
        __map.put("departmentCode", $departmentCode);
        __list.add($departmentName);
        __map.put("departmentName", $departmentName);
        __list.add($employeeTel);
        __map.put("employeeTel", $employeeTel);
        __list.add($userName);
        __map.put("userName", $userName);
        __list.add($password);
        __map.put("password", $password);
        __list.add($email);
        __map.put("email", $email);
        __list.add($enabled);
        __map.put("enabled", $enabled);
        __list.add($sysadmin);
        __map.put("sysadmin", $sysadmin);
        __list.add($language);
        __map.put("language", $language);
        __list.add($description);
        __map.put("description", $description);
        __list.add($loginAt);
        __map.put("loginAt", $loginAt);
        __list.add($expiredAt);
        __map.put("expiredAt", $expiredAt);
        __list.add($createdAt);
        __map.put("createdAt", $createdAt);
        __list.add($createdUser);
        __map.put("createdUser", $createdUser);
        __list.add($username);
        __map.put("username", $username);
        __list.add($groupName);
        __map.put("groupName", $groupName);
        __list.add($changepwd);
        __map.put("changepwd", $changepwd);
        __idPropertyTypes = java.util.Collections.unmodifiableList(__idList);
        __entityPropertyTypes = java.util.Collections.unmodifiableList(__list);
        __entityPropertyTypeMap = java.util.Collections.unmodifiableMap(__map);
    }

    @Override
    public org.seasar.doma.jdbc.entity.NamingType getNamingType() {
        return __namingType;
    }

    @Override
    public boolean isImmutable() {
        return __immutable;
    }

    @Override
    public String getName() {
        return __name;
    }

    @Override
    public String getCatalogName() {
        return __catalogName;
    }

    @Override
    public String getSchemaName() {
        return __schemaName;
    }

    @Override
    public String getTableName() {
        return __tableName;
    }

    @Override
    public String getQualifiedTableName() {
        return __qualifiedTableName;
    }

    @Override
    public void preInsert(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.system.user.dto.UserDto entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.system.user.dto.UserDto> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.system.user.dto.UserDto, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.system.user.dto.UserDto, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.system.user.dto.UserDto newEntity() {
        return new org.nlh4j.web.system.user.dto.UserDto();
    }

    @Override
    public org.nlh4j.web.system.user.dto.UserDto newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.system.user.dto.UserDto();
    }

    @Override
    public Class<org.nlh4j.web.system.user.dto.UserDto> getEntityClass() {
        return org.nlh4j.web.system.user.dto.UserDto.class;
    }

    @Override
    public org.nlh4j.web.system.user.dto.UserDto getOriginalStates(org.nlh4j.web.system.user.dto.UserDto __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.system.user.dto.UserDto __entity) {
    }

    /**
     * @return the singleton
     */
    public static _UserDto getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _UserDto newInstance() {
        return new _UserDto();
    }

}
