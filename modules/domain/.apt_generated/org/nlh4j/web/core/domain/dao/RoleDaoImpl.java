package org.nlh4j.web.core.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-30T07:30:42.336+0700")
public class RoleDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.core.domain.dao.RoleDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method2 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleDao.class, "insert", org.nlh4j.web.core.domain.entity.Role.class);

    private static final java.lang.reflect.Method __method3 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleDao.class, "update", org.nlh4j.web.core.domain.entity.Role.class);

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleDao.class, "delete", org.nlh4j.web.core.domain.entity.Role.class);

    private static final java.lang.reflect.Method __method6 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleDao.class, "batchInsert", java.util.List.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public RoleDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.domain.entity.Role> selectAll() {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectAll");
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/RoleDao/selectAll.sql");
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("selectAll");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Role>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Role>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.domain.entity.Role> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectAll", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectAll", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Role selectById(java.lang.Long id) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectById", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/RoleDao/selectById.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("selectById");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Role> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Role>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.Role __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectById", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "selectById", __e);
            throw __e;
        }
    }

    @Override
    public int insert(org.nlh4j.web.core.domain.entity.Role entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "insert", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Role> __query = new org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal());
            __query.setMethod(__method2);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("insert");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.InsertCommand __command = new org.seasar.doma.internal.jdbc.command.InsertCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "insert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "insert", __e);
            throw __e;
        }
    }

    @Override
    public int update(org.nlh4j.web.core.domain.entity.Role entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "update", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Role> __query = new org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal());
            __query.setMethod(__method3);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
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
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "update", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "update", __e);
            throw __e;
        }
    }

    @Override
    public int delete(org.nlh4j.web.core.domain.entity.Role entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "delete", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Role> __query = new org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal());
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("delete");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "delete", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "delete", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.RoleDto> findRoles(java.lang.String userName) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "findRoles", userName);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/RoleDao/findRoles.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("findRoles");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.RoleDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.RoleDto>(org.nlh4j.web.core.dto._RoleDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.RoleDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "findRoles", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "findRoles", __e);
            throw __e;
        }
    }

    @Override
    public int[] batchInsert(java.util.List<org.nlh4j.web.core.domain.entity.Role> roles) {
        entering("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "batchInsert", roles);
        try {
            if (roles == null) {
                throw new org.seasar.doma.DomaNullPointerException("roles");
            }
            org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery<org.nlh4j.web.core.domain.entity.Role> __query = new org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery<org.nlh4j.web.core.domain.entity.Role>(org.nlh4j.web.core.domain.entity._Role.getSingletonInternal());
            __query.setMethod(__method6);
            __query.setConfig(config);
            __query.setEntities(roles);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleDaoImpl");
            __query.setCallerMethodName("batchInsert");
            __query.setQueryTimeout(-1);
            __query.setBatchSize(-1);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.BatchInsertCommand __command = new org.seasar.doma.internal.jdbc.command.BatchInsertCommand(__query);
            int[] __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "batchInsert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleDaoImpl", "batchInsert", __e);
            throw __e;
        }
    }

}
