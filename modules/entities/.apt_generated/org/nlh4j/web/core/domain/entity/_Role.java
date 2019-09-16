package org.nlh4j.web.core.domain.entity;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T22:30:18.610+0700")
public final class _Role extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.domain.entity.Role> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _Role __singleton = new _Role();

    private final org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator __idGenerator = new org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator();

    /** the id */
    public final org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "id", __idGenerator);

    /** the mid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object> $mid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "mid", "module_id", true, true);

    /** the gid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object> $gid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "gid", "role_id", true, true);

    /** the functions */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.String, java.lang.Object> $functions = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "functions", "functions", true, true);

    /** the createdAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.sql.Timestamp, java.lang.Object> $createdAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "createdAt", "created_at", true, true);

    /** the updatedAt */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.sql.Timestamp, java.lang.Object> $updatedAt = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.sql.Timestamp, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.sql.Timestamp.class, org.seasar.doma.wrapper.TimestampWrapper.class, null, null, "updatedAt", "updated_at", true, true);

    /** the createdUser */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object> $createdUser = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "createdUser", "created_user", true, true);

    /** the updatedUser */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object> $updatedUser = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Role.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "updatedUser", "updated_user", true, true);

    private final org.nlh4j.web.core.domain.entity.RoleListener __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __entityPropertyTypeMap;

    private _Role() {
        __listener = new org.nlh4j.web.core.domain.entity.RoleListener();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.NONE;
        __immutable = false;
        __name = "Role";
        __catalogName = "";
        __schemaName = "";
        __tableName = "\"right\"";
        __qualifiedTableName = "\"right\"";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>>(8);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>>(8);
        __idList.add($id);
        __list.add($id);
        __map.put("id", $id);
        __list.add($mid);
        __map.put("mid", $mid);
        __list.add($gid);
        __map.put("gid", $gid);
        __list.add($functions);
        __map.put("functions", $functions);
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
    public void preInsert(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.domain.entity.Role entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.domain.entity.Role> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Role, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, ?, ?> getGeneratedIdPropertyType() {
        return $id;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Role, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Role newEntity() {
        return new org.nlh4j.web.core.domain.entity.Role();
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Role newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.domain.entity.Role();
    }

    @Override
    public Class<org.nlh4j.web.core.domain.entity.Role> getEntityClass() {
        return org.nlh4j.web.core.domain.entity.Role.class;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Role getOriginalStates(org.nlh4j.web.core.domain.entity.Role __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.domain.entity.Role __entity) {
    }

    /**
     * @return the singleton
     */
    public static _Role getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _Role newInstance() {
        return new _Role();
    }

}
