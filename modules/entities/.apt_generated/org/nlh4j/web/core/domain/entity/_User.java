package org.nlh4j.web.core.domain.entity;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T20:06:59.535+0700")
public final class _User extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.domain.entity.User> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _User __singleton = new _User();

    private final org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator __idGenerator = new org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator();

    /** the id */
    public final org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "id", __idGenerator);

    /** the gid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object> $gid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "gid", "role_group_id", true, true);

    /** the eid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object> $eid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "eid", "employee_id", true, true);

    /** the userName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object> $userName = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "userName", "user_name", true, true);

    /** the password */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object> $password = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "password", "password", true, true);

    /** the email */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object> $email = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "email", "email", true, true);

    /** the enabled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Boolean, java.lang.Object> $enabled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "enabled", "enabled", true, true);

    /** the sysadmin */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Boolean, java.lang.Object> $sysadmin = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "sysadmin", "sysadmin", true, true);

    /** the createdAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.sql.Timestamp, java.lang.Object> $createdAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "createdAt", "created_at", true, true);

    /** the updatedAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.sql.Timestamp, java.lang.Object> $updatedAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "updatedAt", "updated_at", true, true);

    /** the createdUser */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object> $createdUser = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "createdUser", "created_user", true, true);

    /** the updatedUser */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object> $updatedUser = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.User.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "updatedUser", "updated_user", true, true);

    private final org.nlh4j.web.core.domain.entity.UserListener __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __entityPropertyTypeMap;

    private _User() {
        __listener = new org.nlh4j.web.core.domain.entity.UserListener();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.NONE;
        __immutable = false;
        __name = "User";
        __catalogName = "";
        __schemaName = "";
        __tableName = "\"user\"";
        __qualifiedTableName = "\"user\"";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>>(12);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>>(12);
        __idList.add($id);
        __list.add($id);
        __map.put("id", $id);
        __list.add($gid);
        __map.put("gid", $gid);
        __list.add($eid);
        __map.put("eid", $eid);
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
        __list.add($createdAt);
        __map.put("createdAt", $createdAt);
        __list.add($updatedAt);
        __map.put("updatedAt", $updatedAt);
        __list.add($createdUser);
        __map.put("createdUser", $createdUser);
        __list.add($updatedUser);
        __map.put("updatedUser", $updatedUser);
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
    public void preInsert(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.domain.entity.User entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.domain.entity.User> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.User, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, ?, ?> getGeneratedIdPropertyType() {
        return $id;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.User, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.User newEntity() {
        return new org.nlh4j.web.core.domain.entity.User();
    }

    @Override
    public org.nlh4j.web.core.domain.entity.User newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.domain.entity.User();
    }

    @Override
    public Class<org.nlh4j.web.core.domain.entity.User> getEntityClass() {
        return org.nlh4j.web.core.domain.entity.User.class;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.User getOriginalStates(org.nlh4j.web.core.domain.entity.User __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.domain.entity.User __entity) {
    }

    /**
     * @return the singleton
     */
    public static _User getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _User newInstance() {
        return new _User();
    }

}
