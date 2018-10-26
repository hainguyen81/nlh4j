package org.nlh4j.web.system.module.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-21T18:32:38.109+0700")
public class SystemModuleDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.system.module.domain.dao.SystemModuleDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method5 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.module.domain.dao.SystemModuleDao.class, "removeOutOf", java.lang.String.class);

    private static final java.lang.reflect.Method __method6 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.module.domain.dao.SystemModuleDao.class, "deleteModuleBy", java.lang.String.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public SystemModuleDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.ModuleDto> findModules(org.nlh4j.web.system.module.dto.ModuleSearchConditions conditions, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModules", conditions, options, orderBy);
        try {
            if (conditions == null) {
                throw new org.seasar.doma.DomaNullPointerException("conditions");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/findModules.sql");
            __query.setOptions(options);
            __query.addParameter("conditions", org.nlh4j.web.system.module.dto.ModuleSearchConditions.class, conditions);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
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
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModules", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModules", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.dto.ModuleDto findModule(org.nlh4j.web.system.module.dto.ModuleUniqueDto unique) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModule", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/findModule.sql");
            __query.addParameter("unique", org.nlh4j.web.system.module.dto.ModuleUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("findModule");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.ModuleDto> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.ModuleDto>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.dto.ModuleDto>(org.nlh4j.web.core.dto._ModuleDto.getSingletonInternal()));
            org.nlh4j.web.core.dto.ModuleDto __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModule", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModule", __e);
            throw __e;
        }
    }

    @Override
    public boolean isUniqueConstraint(org.nlh4j.web.system.module.dto.ModuleUniqueDto unique) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUniqueConstraint", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/isUniqueConstraint.sql");
            __query.addParameter("unique", org.nlh4j.web.system.module.dto.ModuleUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("isUniqueConstraint");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUniqueConstraint", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUniqueConstraint", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.ModuleDto> findModulesExcluded(java.util.List<java.lang.String> codes) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModulesExcluded", codes);
        try {
            if (codes == null) {
                throw new org.seasar.doma.DomaNullPointerException("codes");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/findModulesExcluded.sql");
            __query.addParameter("codes", java.util.List.class, codes);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("findModulesExcluded");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.ModuleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.ModuleDto>(org.nlh4j.web.core.dto._ModuleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.ModuleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModulesExcluded", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "findModulesExcluded", __e);
            throw __e;
        }
    }

    @Override
    public boolean isUrlUniqueConstraint(java.lang.Long id, java.lang.String code, java.lang.String url) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUrlUniqueConstraint", id, code, url);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/isUrlUniqueConstraint.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.addParameter("code", java.lang.String.class, code);
            __query.addParameter("url", java.lang.String.class, url);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("isUrlUniqueConstraint");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Boolean>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Boolean>(new org.seasar.doma.wrapper.BooleanWrapper(), true));
            boolean __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUrlUniqueConstraint", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "isUrlUniqueConstraint", __e);
            throw __e;
        }
    }

    @Override
    public int removeOutOf(java.lang.String code) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "removeOutOf", code);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery();
            __query.setMethod(__method5);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/removeOutOf.sql");
            __query.addParameter("code", java.lang.String.class, code);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("removeOutOf");
            __query.setQueryTimeout(-1);
            __query.setVersionIncluded(false);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.UpdateCommand __command = new org.seasar.doma.internal.jdbc.command.UpdateCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "removeOutOf", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "removeOutOf", __e);
            throw __e;
        }
    }

    @Override
    public int deleteModuleBy(java.lang.String code) {
        entering("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "deleteModuleBy", code);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery();
            __query.setMethod(__method6);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/module/domain/dao/SystemModuleDao/deleteModuleBy.sql");
            __query.addParameter("code", java.lang.String.class, code);
            __query.setCallerClassName("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl");
            __query.setCallerMethodName("deleteModuleBy");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "deleteModuleBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.module.domain.dao.SystemModuleDaoImpl", "deleteModuleBy", __e);
            throw __e;
        }
    }

}
