package org.nlh4j.web.core.dto;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T20:06:59.434+0700")
public final class _ModuleDto extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.dto.ModuleDto> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _ModuleDto __singleton = new _ModuleDto();

    /** the id */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "ID", true, true);

    /** the pid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object> $pid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "pid", "PID", true, true);

    /** the code */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $code = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "code", "CODE", true, true);

    /** the name */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $name = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "name", "NAME", true, true);

    /** the langKey */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $langKey = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "langKey", "LANG_KEY", true, true);

    /** the modOrder */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Integer, java.lang.Object> $modOrder = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Integer, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Integer.class, org.seasar.doma.wrapper.IntegerWrapper.class, null, null, "modOrder", "MOD_ORDER", true, true);

    /** the enabled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object> $enabled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "enabled", "ENABLED", true, true);

    /** the visibled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object> $visibled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "visibled", "VISIBLED", true, true);

    /** the service */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object> $service = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "service", "SERVICE", true, true);

    /** the common */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object> $common = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "common", "COMMON", true, true);

    /** the urlRegex */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $urlRegex = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "urlRegex", "URL_REGEX", true, true);

    /** the mainUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $mainUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "mainUrl", "MAIN_URL", true, true);

    /** the css */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $css = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "css", "CSS", true, true);

    /** the description */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $description = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "description", "DESCRIPTION", true, true);

    /** the library */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $library = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "library", "LIBRARY", true, true);

    /** the childs */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object> $childs = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "childs", "CHILDS", true, true);

    /** the depth */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object> $depth = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "depth", "DEPTH", true, true);

    /** the leaf */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object> $leaf = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "leaf", "LEAF", true, true);

    /** the functions */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $functions = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "functions", "FUNCTIONS", true, true);

    /** the forwardUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $forwardUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "forwardUrl", "FORWARD_URL", true, true);

    /** the templateUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $templateUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "templateUrl", "TEMPLATE_URL", true, true);

    /** the ngcontroller */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object> $ngcontroller = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, java.lang.String, java.lang.Object>(org.nlh4j.web.core.dto.ModuleDto.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "ngcontroller", "NGCONTROLLER", true, true);

    private final org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.ModuleDto> __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __entityPropertyTypeMap;

    private _ModuleDto() {
        __listener = new org.seasar.doma.jdbc.entity.NullEntityListener<org.nlh4j.web.core.dto.ModuleDto>();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.SNAKE_UPPER_CASE;
        __immutable = false;
        __name = "ModuleDto";
        __catalogName = "";
        __schemaName = "";
        __tableName = "MODULE_DTO";
        __qualifiedTableName = "MODULE_DTO";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>>(22);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>>(22);
        __list.add($id);
        __map.put("id", $id);
        __list.add($pid);
        __map.put("pid", $pid);
        __list.add($code);
        __map.put("code", $code);
        __list.add($name);
        __map.put("name", $name);
        __list.add($langKey);
        __map.put("langKey", $langKey);
        __list.add($modOrder);
        __map.put("modOrder", $modOrder);
        __list.add($enabled);
        __map.put("enabled", $enabled);
        __list.add($visibled);
        __map.put("visibled", $visibled);
        __list.add($service);
        __map.put("service", $service);
        __list.add($common);
        __map.put("common", $common);
        __list.add($urlRegex);
        __map.put("urlRegex", $urlRegex);
        __list.add($mainUrl);
        __map.put("mainUrl", $mainUrl);
        __list.add($css);
        __map.put("css", $css);
        __list.add($description);
        __map.put("description", $description);
        __list.add($library);
        __map.put("library", $library);
        __list.add($childs);
        __map.put("childs", $childs);
        __list.add($depth);
        __map.put("depth", $depth);
        __list.add($leaf);
        __map.put("leaf", $leaf);
        __list.add($functions);
        __map.put("functions", $functions);
        __list.add($forwardUrl);
        __map.put("forwardUrl", $forwardUrl);
        __list.add($templateUrl);
        __map.put("templateUrl", $templateUrl);
        __list.add($ngcontroller);
        __map.put("ngcontroller", $ngcontroller);
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
    public void preInsert(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.dto.ModuleDto entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.dto.ModuleDto> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.dto.ModuleDto, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, ?, ?> getGeneratedIdPropertyType() {
        return null;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.dto.ModuleDto, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.dto.ModuleDto newEntity() {
        return new org.nlh4j.web.core.dto.ModuleDto();
    }

    @Override
    public org.nlh4j.web.core.dto.ModuleDto newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.dto.ModuleDto();
    }

    @Override
    public Class<org.nlh4j.web.core.dto.ModuleDto> getEntityClass() {
        return org.nlh4j.web.core.dto.ModuleDto.class;
    }

    @Override
    public org.nlh4j.web.core.dto.ModuleDto getOriginalStates(org.nlh4j.web.core.dto.ModuleDto __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.dto.ModuleDto __entity) {
    }

    /**
     * @return the singleton
     */
    public static _ModuleDto getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _ModuleDto newInstance() {
        return new _ModuleDto();
    }

}
