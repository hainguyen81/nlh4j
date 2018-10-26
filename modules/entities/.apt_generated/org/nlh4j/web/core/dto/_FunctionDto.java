package org.nlh4j.web.core.dto;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-21T18:30:44.031+0700")
public final class _FunctionDto extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.dto.FunctionDto> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _FunctionDto __singleton = new _FunctionDto();

    /** the id */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "ID", true, true);

    /** the code */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object> $code = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "code", "CODE", true, true);

    /** the name */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object> $name = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "name", "NAME", true, true);

    /** the langKey */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object> $langKey = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "langKey", "LANG_KEY", true, true);

    /** the funcOrder */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Integer, java.lang.Object> $funcOrder = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Integer, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.Integer.class, org.seasar.doma.wrapper.IntegerWrapper.class, null, null, "funcOrder", "FUNC_ORDER", true, true);

    /** the enabled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Boolean, java.lang.Object> $enabled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.FunctionDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "enabled", "ENABLED", true, true);

    private final org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.FunctionDto> __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __entityPropertyTypeMap;

    private _FunctionDto() {
        __listener = new org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.FunctionDto>();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.SNAKE_UPPER_CASE;
        __immutable = false;
        __name = "FunctionDto";
        __catalogName = "";
        __schemaName = "";
        __tableName = "FUNCTION_DTO";
        __qualifiedTableName = "FUNCTION_DTO";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>>(6);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>>(6);
        __list.add($id);
        __map.put("id", $id);
        __list.add($code);
        __map.put("code", $code);
        __list.add($name);
        __map.put("name", $name);
        __list.add($langKey);
        __map.put("langKey", $langKey);
        __list.add($funcOrder);
        __map.put("funcOrder", $funcOrder);
        __list.add($enabled);
        __map.put("enabled", $enabled);
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
    public void preInsert(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.dto.FunctionDto entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.dto.FunctionDto> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.FunctionDto, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.dto.FunctionDto, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.dto.FunctionDto newEntity() {
        return new org.nlh4j.web.core.dto.FunctionDto();
    }

    @Override
    public org.nlh4j.web.core.dto.FunctionDto newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.dto.FunctionDto();
    }

    @Override
    public Class<org.nlh4j.web.core.dto.FunctionDto> getEntityClass() {
        return org.nlh4j.web.core.dto.FunctionDto.class;
    }

    @Override
    public org.nlh4j.web.core.dto.FunctionDto getOriginalStates(org.nlh4j.web.core.dto.FunctionDto __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.dto.FunctionDto __entity) {
    }

    /**
     * @return the singleton
     */
    public static _FunctionDto getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _FunctionDto newInstance() {
        return new _FunctionDto();
    }

}
