package org.nlh4j.web.core.domain.entity;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-21T18:30:43.838+0700")
public final class _UserOnline extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.domain.entity.UserOnline> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _UserOnline __singleton = new _UserOnline();

    private final org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator __idGenerator = new org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator();

    /** the id */
    public final org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.UserOnline.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "id", __idGenerator);

    /** the uid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.lang.Long, java.lang.Object> $uid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.UserOnline.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "uid", "uid", true, true);

    /** the loginAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.sql.Timestamp, java.lang.Object> $loginAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.UserOnline.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "loginAt", "login_at", true, true);

    /** the logoutAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.sql.Timestamp, java.lang.Object> $logoutAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.UserOnline.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "logoutAt", "logout_at", true, true);

    private final org.nlh4j.web.core.domain.entity.UserOnlineListener __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __entityPropertyTypeMap;

    private _UserOnline() {
        __listener = new org.nlh4j.web.core.domain.entity.UserOnlineListener();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.NONE;
        __immutable = false;
        __name = "UserOnline";
        __catalogName = "";
        __schemaName = "";
        __tableName = "user_online";
        __qualifiedTableName = "user_online";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>>(4);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>>(4);
        __idList.add($id);
        __list.add($id);
        __map.put("id", $id);
        __list.add($uid);
        __map.put("uid", $uid);
        __list.add($loginAt);
        __map.put("loginAt", $loginAt);
        __list.add($logoutAt);
        __map.put("logoutAt", $logoutAt);
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
    public void preInsert(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.domain.entity.UserOnline entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.domain.entity.UserOnline> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.UserOnline, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, ?, ?> getGeneratedIdPropertyType() {
        return $id;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.UserOnline, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline newEntity() {
        return new org.nlh4j.web.core.domain.entity.UserOnline();
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.domain.entity.UserOnline();
    }

    @Override
    public Class<org.nlh4j.web.core.domain.entity.UserOnline> getEntityClass() {
        return org.nlh4j.web.core.domain.entity.UserOnline.class;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline getOriginalStates(org.nlh4j.web.core.domain.entity.UserOnline __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.domain.entity.UserOnline __entity) {
    }

    /**
     * @return the singleton
     */
    public static _UserOnline getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _UserOnline newInstance() {
        return new _UserOnline();
    }

}
