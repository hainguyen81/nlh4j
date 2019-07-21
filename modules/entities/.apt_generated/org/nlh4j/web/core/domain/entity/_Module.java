package org.nlh4j.web.core.domain.entity;

/** */
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T20:06:59.409+0700")
public final class _Module extends org.seasar.doma.jdbc.entity.AbstractEntityType<org.nlh4j.web.core.domain.entity.Module> {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final _Module __singleton = new _Module();

    private final org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator __idGenerator = new org.seasar.doma.jdbc.id.BuiltinIdentityIdGenerator();

    /** the id */
    public final org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Long, java.lang.Object> $id = new org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "id", "id", __idGenerator);

    /** the pid */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Long, java.lang.Object> $pid = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Long, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Long.class, org.seasar.doma.wrapper.LongWrapper.class, null, null, "pid", "parent_id", true, true);

    /** the code */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $code = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "code", "code", true, true);

    /** the name */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $name = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "name", "name", true, true);

    /** the langKey */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $langKey = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "langKey", "lang_key", true, true);

    /** the modOrder */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Integer, java.lang.Object> $modOrder = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Integer, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Integer.class, org.seasar.doma.wrapper.IntegerWrapper.class, null, null, "modOrder", "mod_order", true, true);

    /** the enabled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object> $enabled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "enabled", "enabled", true, true);

    /** the visibled */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object> $visibled = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "visibled", "visibled", true, true);

    /** the service */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object> $service = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "service", "service", true, true);

    /** the common */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object> $common = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.Boolean, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.Boolean.class, org.seasar.doma.wrapper.BooleanWrapper.class, null, null, "common", "common", true, true);

    /** the urlRegex */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $urlRegex = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "urlRegex", "link_regex", true, true);

    /** the mainUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $mainUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "mainUrl", "main_link", true, true);

    /** the css */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $css = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "css", "css", true, true);

    /** the description */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $description = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "description", "description", true, true);

    /** the library */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $library = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "library", "library", true, true);

    /** the forwardUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $forwardUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "forwardUrl", "forward_url", true, true);

    /** the templateUrl */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $templateUrl = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "templateUrl", "template_url", true, true);

    /** the ngcontroller */
    public final org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object> $ngcontroller = new org.seasar.doma.jdbc.entity.BasicPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, java.lang.String, java.lang.Object>(org.nlh4j.web.core.domain.entity.Module.class, java.lang.String.class, org.seasar.doma.wrapper.StringWrapper.class, null, null, "ngcontroller", "ngcontroller", true, true);

    private final org.nlh4j.web.core.domain.entity.ModuleListener __listener;

    private final org.seasar.doma.jdbc.entity.NamingType __namingType;

    private final boolean __immutable;

    private final String __catalogName;

    private final String __schemaName;

    private final String __tableName;

    private final String __qualifiedTableName;

    private final String __name;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __idPropertyTypes;

    private final java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __entityPropertyTypes;

    private final java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __entityPropertyTypeMap;

    private _Module() {
        __listener = new org.nlh4j.web.core.domain.entity.ModuleListener();
        __namingType = org.seasar.doma.jdbc.entity.NamingType.NONE;
        __immutable = false;
        __name = "Module";
        __catalogName = "";
        __schemaName = "";
        __tableName = "module";
        __qualifiedTableName = "module";
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __idList = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>>();
        java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __list = new java.util.ArrayList<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>>(18);
        java.util.Map<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> __map = new java.util.HashMap<String, org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>>(18);
        __idList.add($id);
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
    public void preInsert(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PreInsertContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.preInsert(entity, context);
    }

    @Override
    public void preUpdate(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PreUpdateContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.preUpdate(entity, context);
    }

    @Override
    public void preDelete(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PreDeleteContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.preDelete(entity, context);
    }

    @Override
    public void postInsert(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PostInsertContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.postInsert(entity, context);
    }

    @Override
    public void postUpdate(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PostUpdateContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.postUpdate(entity, context);
    }

    @Override
    public void postDelete(org.nlh4j.web.core.domain.entity.Module entity, org.seasar.doma.jdbc.entity.PostDeleteContext<org.nlh4j.web.core.domain.entity.Module> context) {
        __listener.postDelete(entity, context);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> getEntityPropertyTypes() {
        return __entityPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?> getEntityPropertyType(String __name) {
        return __entityPropertyTypeMap.get(__name);
    }

    @Override
    public java.util.List<org.seasar.doma.jdbc.entity.EntityPropertyType<org.nlh4j.web.core.domain.entity.Module, ?>> getIdPropertyTypes() {
        return __idPropertyTypes;
    }

    @Override
    public org.seasar.doma.jdbc.entity.GeneratedIdPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, ?, ?> getGeneratedIdPropertyType() {
        return $id;
    }

    @Override
    public org.seasar.doma.jdbc.entity.VersionPropertyType<java.lang.Object, org.nlh4j.web.core.domain.entity.Module, ?, ?> getVersionPropertyType() {
        return null;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Module newEntity() {
        return new org.nlh4j.web.core.domain.entity.Module();
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Module newEntity(java.util.Map<String, Object> __args) {
        return new org.nlh4j.web.core.domain.entity.Module();
    }

    @Override
    public Class<org.nlh4j.web.core.domain.entity.Module> getEntityClass() {
        return org.nlh4j.web.core.domain.entity.Module.class;
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Module getOriginalStates(org.nlh4j.web.core.domain.entity.Module __entity) {
        return null;
    }

    @Override
    public void saveCurrentStates(org.nlh4j.web.core.domain.entity.Module __entity) {
    }

    /**
     * @return the singleton
     */
    public static _Module getSingletonInternal() {
        return __singleton;
    }

    /**
     * @return the new instance
     */
    public static _Module newInstance() {
        return new _Module();
    }

}
