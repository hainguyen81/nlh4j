package org.nlh4j.web.core.dto;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T20:06:59.387+0700")
public final class _RoleDto extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.dto.RoleDto> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _RoleDto __singleton = new _RoleDto();

    /** the id */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "ID", true, true);

    /** the functions */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $functions = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "functions", "FUNCTIONS", true, true);

    /** the mid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object> $mid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "mid", "MID", true, true);

    /** the moduleCd */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $moduleCd = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "moduleCd", "MODULE_CD", true, true);

    /** the moduleName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $moduleName = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "moduleName", "MODULE_NAME", true, true);

    /** the moduleLangKey */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $moduleLangKey = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "moduleLangKey", "MODULE_LANG_KEY", true, true);

    /** the moduleLink */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $moduleLink = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "moduleLink", "MODULE_LINK", true, true);

    /** the moduleDescription */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $moduleDescription = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "moduleDescription", "MODULE_DESCRIPTION", true, true);

    /** the gid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object> $gid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "gid", "GID", true, true);

    /** the groupCd */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $groupCd = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "groupCd", "GROUP_CD", true, true);

    /** the groupName */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object> $groupName = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.RoleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "groupName", "GROUP_NAME", true, true);

    private final org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.RoleDto> __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __entityPropertyTypeMap;

    private _RoleDto() {
        __listener = new org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.RoleDto>();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.SNAKE_UPPER_CASE;
        __immutable = false;
        __name = "RoleDto";
        __catalogName = "";
        __schemaName = "";
        __tableName = "ROLE_DTO";
        __qualifiedTableName = "ROLE_DTO";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>>(11);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>>(11);
        __list.add($id);
        __map.put("id", $id);
        __list.add($functions);
        __map.put("functions", $functions);
        __list.add($mid);
        __map.put("mid", $mid);
        __list.add($moduleCd);
        __map.put("moduleCd", $moduleCd);
        __list.add($moduleName);
        __map.put("moduleName", $moduleName);
        __list.add($moduleLangKey);
        __map.put("moduleLangKey", $moduleLangKey);
        __list.add($moduleLink);
        __map.put("moduleLink", $moduleLink);
        __list.add($moduleDescription);
        __map.put("moduleDescription", $moduleDescription);
        __list.add($gid);
        __map.put("gid", $gid);
        __list.add($groupCd);
        __map.put("groupCd", $groupCd);
        __list.add($groupName);
        __map.put("groupName", $groupName);
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
    public void preInsert(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.dto.RoleDto entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.dto.RoleDto> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.RoleDto, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.dto.RoleDto, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.dto.RoleDto newEntity() {
        return new org.nlh4j.web.core.dto.RoleDto();
    }

    @Override
    public org.nlh4j.web.core.dto.RoleDto newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.dto.RoleDto();
    }

    @Override
    public Class<org.nlh4j.web.core.dto.RoleDto> getEntityClass() {
        return org.nlh4j.web.core.dto.RoleDto.class;
    }

    @Override
    public org.nlh4j.web.core.dto.RoleDto getOriginalStates(org.nlh4j.web.core.dto.RoleDto __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.dto.RoleDto __entity) {
    }

    /**
     * @return the singleton
     */
    public static _RoleDto getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _RoleDto newInstance() {
        return new _RoleDto();
    }

}
