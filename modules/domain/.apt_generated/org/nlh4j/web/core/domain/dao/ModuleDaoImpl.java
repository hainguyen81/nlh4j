package org.nlh4j.web.core.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-30T07:30:42.311+0700")
public class ModuleDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.core.domain.dao.ModuleDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method2 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.ModuleDao.class, "insert", org.nlh4j.web.core.domain.entity.Module.class);

    private static final java.lang.reflect.Method __method3 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.ModuleDao.class, "update", org.nlh4j.web.core.domain.entity.Module.class);

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.ModuleDao.class, "delete", org.nlh4j.web.core.domain.entity.Module.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public ModuleDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.domain.entity.Module> selectAll() {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectAll");
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/selectAll.sql");
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("selectAll");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Module>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Module>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.domain.entity.Module>(org.nlh4j.web.core.domain.entity._Module.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.domain.entity.Module> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectAll", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectAll", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Module selectById(java.lang.Long id) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectById", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/selectById.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("selectById");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Module> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Module>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.Module>(org.nlh4j.web.core.domain.entity._Module.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.Module __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectById", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "selectById", __e);
            throw __e;
        }
    }

    @Override
    public int insert(org.nlh4j.web.core.domain.entity.Module entity) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "insert", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Module> __query = new org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Module>(org.nlh4j.web.core.domain.entity._Module.getSingletonInternal());
            __query.setMethod(__method2);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("insert");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.InsertCommand __command = new org.seasar.doma.internal.jdbc.command.InsertCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "insert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "insert", __e);
            throw __e;
        }
    }

    @Override
    public int update(org.nlh4j.web.core.domain.entity.Module entity) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "update", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Module> __query = new org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Module>(org.nlh4j.web.core.domain.entity._Module.getSingletonInternal());
            __query.setMethod(__method3);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("update");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setVersionIncluded(false);
            __query.setVersionIgnored(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.setUnchangedPropertyIncluded(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.UpdateCommand __command = new org.seasar.doma.internal.jdbc.command.UpdateCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "update", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "update", __e);
            throw __e;
        }
    }

    @Override
    public int delete(org.nlh4j.web.core.domain.entity.Module entity) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "delete", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Module> __query = new org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Module>(org.nlh4j.web.core.domain.entity._Module.getSingletonInternal());
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("delete");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "delete", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "delete", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.ModuleDto> findModules(java.lang.String userName, java.lang.Long pid, java.lang.Boolean enabled, java.lang.Boolean visibled, java.lang.Boolean ui, java.lang.Boolean leaf) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModules", userName, pid, enabled, visibled, ui, leaf);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/findModules.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("pid", java.lang.Long.class, pid);
            __query.addParameter("enabled", java.lang.Boolean.class, enabled);
            __query.addParameter("visibled", java.lang.Boolean.class, visibled);
            __query.addParameter("ui", java.lang.Boolean.class, ui);
            __query.addParameter("leaf", java.lang.Boolean.class, leaf);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("findModules");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.ModuleDto>(org.nlh4j.web.core.dto._ModuleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.ModuleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModules", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModules", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.ModuleDto> findModulesByCode(java.lang.String userName, java.util.List<java.lang.String> moduleCds, java.lang.Boolean enabled, java.lang.Boolean visibled, java.lang.Boolean ui, java.lang.Boolean leaf) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModulesByCode", userName, moduleCds, enabled, visibled, ui, leaf);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/findModulesByCode.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.addParameter("enabled", java.lang.Boolean.class, enabled);
            __query.addParameter("visibled", java.lang.Boolean.class, visibled);
            __query.addParameter("ui", java.lang.Boolean.class, ui);
            __query.addParameter("leaf", java.lang.Boolean.class, leaf);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("findModulesByCode");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.ModuleDto>(org.nlh4j.web.core.dto._ModuleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.ModuleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModulesByCode", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModulesByCode", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<java.lang.String> findModuleForwardUrls(java.lang.String userName, java.util.List<java.lang.String> moduleCds) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleForwardUrls", userName, moduleCds);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/findModuleForwardUrls.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("findModuleForwardUrls");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<java.lang.String>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<java.lang.String>>(__query, new org.seasar.doma.internal.jdbc.command.BasicResultListHandler<java.lang.String>(new org.seasar.doma.wrapper.StringWrapper()));
            java.util.List<java.lang.String> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleForwardUrls", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleForwardUrls", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<java.lang.String> findModuleStylesheets(java.util.List<java.lang.String> moduleCds) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleStylesheets", moduleCds);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/findModuleStylesheets.sql");
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("findModuleStylesheets");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<java.lang.String>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<java.lang.String>>(__query, new org.seasar.doma.internal.jdbc.command.BasicResultListHandler<java.lang.String>(new org.seasar.doma.wrapper.StringWrapper()));
            java.util.List<java.lang.String> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleStylesheets", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findModuleStylesheets", __e);
            throw __e;
        }
    }

    @Override
    public boolean hasPermission(java.lang.String userName, java.util.List<java.lang.String> moduleCds, java.util.List<java.lang.String> functions) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermission", userName, moduleCds, functions);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            if (functions == null) {
                throw new org.seasar.doma.DomaNullPointerException("functions");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/hasPermission.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.addParameter("functions", java.util.List.class, functions);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("hasPermission");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermission", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermission", __e);
            throw __e;
        }
    }

    @Override
    public boolean hasPermissionOnRequest(java.lang.String userName, java.util.List<java.lang.String> moduleCds, java.lang.String requestURI, java.util.List<java.lang.String> functions) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermissionOnRequest", userName, moduleCds, requestURI, functions);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            if (functions == null) {
                throw new org.seasar.doma.DomaNullPointerException("functions");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/hasPermissionOnRequest.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.addParameter("requestURI", java.lang.String.class, requestURI);
            __query.addParameter("functions", java.util.List.class, functions);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("hasPermissionOnRequest");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermissionOnRequest", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "hasPermissionOnRequest", __e);
            throw __e;
        }
    }

    @Override
    public boolean isEnabled(java.util.List<java.lang.String> moduleCds) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabled", moduleCds);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/isEnabled.sql");
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("isEnabled");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabled", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabled", __e);
            throw __e;
        }
    }

    @Override
    public boolean isEnabledOnRequest(java.util.List<java.lang.String> moduleCds, java.lang.String requestURI) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabledOnRequest", moduleCds, requestURI);
        try {
            if (moduleCds == null) {
                throw new org.seasar.doma.DomaNullPointerException("moduleCds");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/isEnabledOnRequest.sql");
            __query.addParameter("moduleCds", java.util.List.class, moduleCds);
            __query.addParameter("requestURI", java.lang.String.class, requestURI);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("isEnabledOnRequest");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabledOnRequest", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "isEnabledOnRequest", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.dto.ModuleDto findParentModuleOf(java.lang.String userName, java.lang.String requestURI, java.lang.Boolean visibled, java.lang.Boolean ui) {
        entering("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findParentModuleOf", userName, requestURI, visibled, ui);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/ModuleDao/findParentModuleOf.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.addParameter("requestURI", java.lang.String.class, requestURI);
            __query.addParameter("visibled", java.lang.Boolean.class, visibled);
            __query.addParameter("ui", java.lang.Boolean.class, ui);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.ModuleDaoImpl");
            __query.setCallerMethodName("findParentModuleOf");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.ModuleDto> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.ModuleDto>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.dto.ModuleDto>(org.nlh4j.web.core.dto._ModuleDto.getSingletonInternal()));
            org.nlh4j.web.core.dto.ModuleDto __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findParentModuleOf", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.ModuleDaoImpl", "findParentModuleOf", __e);
            throw __e;
        }
    }

}
