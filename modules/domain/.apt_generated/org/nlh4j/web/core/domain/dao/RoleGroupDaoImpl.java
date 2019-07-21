package org.nlh4j.web.core.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T20:07:47.560+0700")
public class RoleGroupDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.core.domain.dao.RoleGroupDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method2 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleGroupDao.class, "insert", org.nlh4j.web.core.domain.entity.RoleGroup.class);

    private static final java.lang.reflect.Method __method3 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleGroupDao.class, "update", org.nlh4j.web.core.domain.entity.RoleGroup.class);

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.RoleGroupDao.class, "delete", org.nlh4j.web.core.domain.entity.RoleGroup.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public RoleGroupDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.domain.entity.RoleGroup> selectAll() {
        entering("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectAll");
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/RoleGroupDao/selectAll.sql");
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl");
            __query.setCallerMethodName("selectAll");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.RoleGroup>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.RoleGroup>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.domain.entity.RoleGroup>(org.nlh4j.web.core.domain.entity._RoleGroup.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.domain.entity.RoleGroup> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectAll", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectAll", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.RoleGroup selectById(java.lang.Long id) {
        entering("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectById", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/RoleGroupDao/selectById.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl");
            __query.setCallerMethodName("selectById");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.RoleGroup> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.RoleGroup>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.RoleGroup>(org.nlh4j.web.core.domain.entity._RoleGroup.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.RoleGroup __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectById", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "selectById", __e);
            throw __e;
        }
    }

    @Override
    public int insert(org.nlh4j.web.core.domain.entity.RoleGroup entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "insert", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.RoleGroup> __query = new org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.RoleGroup>(org.nlh4j.web.core.domain.entity._RoleGroup.getSingletonInternal());
            __query.setMethod(__method2);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl");
            __query.setCallerMethodName("insert");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.InsertCommand __command = new org.seasar.doma.internal.jdbc.command.InsertCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "insert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "insert", __e);
            throw __e;
        }
    }

    @Override
    public int update(org.nlh4j.web.core.domain.entity.RoleGroup entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "update", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.RoleGroup> __query = new org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.RoleGroup>(org.nlh4j.web.core.domain.entity._RoleGroup.getSingletonInternal());
            __query.setMethod(__method3);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl");
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
            exiting("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "update", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "update", __e);
            throw __e;
        }
    }

    @Override
    public int delete(org.nlh4j.web.core.domain.entity.RoleGroup entity) {
        entering("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "delete", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.RoleGroup> __query = new org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.RoleGroup>(org.nlh4j.web.core.domain.entity._RoleGroup.getSingletonInternal());
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl");
            __query.setCallerMethodName("delete");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "delete", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.RoleGroupDaoImpl", "delete", __e);
            throw __e;
        }
    }

}
