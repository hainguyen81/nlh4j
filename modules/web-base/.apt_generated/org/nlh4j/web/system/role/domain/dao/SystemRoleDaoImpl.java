package org.nlh4j.web.system.role.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T22:32:03.537+0700")
public class SystemRoleDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.system.role.domain.dao.SystemRoleDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method5 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.role.domain.dao.SystemRoleDao.class, "deleteRolesBy", java.lang.Long.class);

    private static final java.lang.reflect.Method __method6 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.role.domain.dao.SystemRoleDao.class, "deleteRoleGroupBy", java.lang.String.class);

    private static final java.lang.reflect.Method __method7 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.role.domain.dao.SystemRoleDao.class, "removeUserRoles", java.lang.Long.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public SystemRoleDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.system.role.domain.entity.RoleGroup> findGroups(org.nlh4j.web.system.role.dto.RoleGroupSearchConditions conditions, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroups", conditions, options, orderBy);
        try {
            if (conditions == null) {
                throw new org.seasar.doma.DomaNullPointerException("conditions");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/findGroups.sql");
            __query.setOptions(options);
            __query.addParameter("conditions", org.nlh4j.web.system.role.dto.RoleGroupSearchConditions.class, conditions);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("findGroups");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.system.role.domain.entity.RoleGroup>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.system.role.domain.entity.RoleGroup>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.system.role.domain.entity.RoleGroup>(org.nlh4j.web.system.role.domain.entity._RoleGroup.getSingletonInternal()));
            java.util.List<org.nlh4j.web.system.role.domain.entity.RoleGroup> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroups", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroups", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.system.role.domain.entity.RoleGroup findGroup(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto unique) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroup", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/findGroup.sql");
            __query.addParameter("unique", org.nlh4j.web.system.role.dto.RoleGroupUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("findGroup");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.system.role.domain.entity.RoleGroup> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.system.role.domain.entity.RoleGroup>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.system.role.domain.entity.RoleGroup>(org.nlh4j.web.system.role.domain.entity._RoleGroup.getSingletonInternal()));
            org.nlh4j.web.system.role.domain.entity.RoleGroup __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroup", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findGroup", __e);
            throw __e;
        }
    }

    @Override
    public boolean isUniqueConstraint(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto unique) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "isUniqueConstraint", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/isUniqueConstraint.sql");
            __query.addParameter("unique", org.nlh4j.web.system.role.dto.RoleGroupUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
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
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "isUniqueConstraint", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "isUniqueConstraint", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.RoleDto> findRolesBy(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto unique, java.lang.Boolean enabled, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findRolesBy", unique, enabled, options, orderBy);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/findRolesBy.sql");
            __query.setOptions(options);
            __query.addParameter("unique", org.nlh4j.web.system.role.dto.RoleGroupUniqueDto.class, unique);
            __query.addParameter("enabled", java.lang.Boolean.class, enabled);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("findRolesBy");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.RoleDto>(org.nlh4j.web.core.dto._RoleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.RoleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findRolesBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findRolesBy", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.RoleDto> findUnRolesBy(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto unique, java.lang.Boolean enabled, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findUnRolesBy", unique, enabled, options, orderBy);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/findUnRolesBy.sql");
            __query.setOptions(options);
            __query.addParameter("unique", org.nlh4j.web.system.role.dto.RoleGroupUniqueDto.class, unique);
            __query.addParameter("enabled", java.lang.Boolean.class, enabled);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("findUnRolesBy");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.RoleDto>(org.nlh4j.web.core.dto._RoleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.RoleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findUnRolesBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "findUnRolesBy", __e);
            throw __e;
        }
    }

    @Override
    public int deleteRolesBy(java.lang.Long gid) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRolesBy", gid);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery();
            __query.setMethod(__method5);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/deleteRolesBy.sql");
            __query.addParameter("gid", java.lang.Long.class, gid);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("deleteRolesBy");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRolesBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRolesBy", __e);
            throw __e;
        }
    }

    @Override
    public int deleteRoleGroupBy(java.lang.String code) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRoleGroupBy", code);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery();
            __query.setMethod(__method6);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/deleteRoleGroupBy.sql");
            __query.addParameter("code", java.lang.String.class, code);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("deleteRoleGroupBy");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRoleGroupBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "deleteRoleGroupBy", __e);
            throw __e;
        }
    }

    @Override
    public int removeUserRoles(java.lang.Long gid) {
        entering("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "removeUserRoles", gid);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery();
            __query.setMethod(__method7);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/role/domain/dao/SystemRoleDao/removeUserRoles.sql");
            __query.addParameter("gid", java.lang.Long.class, gid);
            __query.setCallerClassName("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl");
            __query.setCallerMethodName("removeUserRoles");
            __query.setQueryTimeout(-1);
            __query.setVersionIncluded(false);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.UpdateCommand __command = new org.seasar.doma.internal.jdbc.command.UpdateCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "removeUserRoles", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.role.domain.dao.SystemRoleDaoImpl", "removeUserRoles", __e);
            throw __e;
        }
    }

}
