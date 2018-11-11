package org.nlh4j.web.core.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-30T07:30:42.223+0700")
public class FunctionDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.core.domain.dao.FunctionDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method2 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.FunctionDao.class, "insert", org.nlh4j.web.core.domain.entity.Function.class);

    private static final java.lang.reflect.Method __method3 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.FunctionDao.class, "update", org.nlh4j.web.core.domain.entity.Function.class);

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.FunctionDao.class, "delete", org.nlh4j.web.core.domain.entity.Function.class);

    private static final java.lang.reflect.Method __method5 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.FunctionDao.class, "batchInsert", java.util.List.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public FunctionDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.domain.entity.Function> selectAll() {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectAll");
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/FunctionDao/selectAll.sql");
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
            __query.setCallerMethodName("selectAll");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Function>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.Function>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.domain.entity.Function> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectAll", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectAll", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.Function selectById(java.lang.Long id) {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectById", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/FunctionDao/selectById.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
            __query.setCallerMethodName("selectById");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Function> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.Function>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.Function __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectById", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "selectById", __e);
            throw __e;
        }
    }

    @Override
    public int insert(org.nlh4j.web.core.domain.entity.Function entity) {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "insert", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Function> __query = new org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal());
            __query.setMethod(__method2);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
            __query.setCallerMethodName("insert");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.InsertCommand __command = new org.seasar.doma.internal.jdbc.command.InsertCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "insert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "insert", __e);
            throw __e;
        }
    }

    @Override
    public int update(org.nlh4j.web.core.domain.entity.Function entity) {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "update", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Function> __query = new org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal());
            __query.setMethod(__method3);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
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
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "update", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "update", __e);
            throw __e;
        }
    }

    @Override
    public int delete(org.nlh4j.web.core.domain.entity.Function entity) {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "delete", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Function> __query = new org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal());
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
            __query.setCallerMethodName("delete");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "delete", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "delete", __e);
            throw __e;
        }
    }

    @Override
    public int[] batchInsert(java.util.List<org.nlh4j.web.core.domain.entity.Function> roles) {
        entering("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "batchInsert", roles);
        try {
            if (roles == null) {
                throw new org.seasar.doma.DomaNullPointerException("roles");
            }
            org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery<org.nlh4j.web.core.domain.entity.Function> __query = new org.seasar.doma.internal.jdbc.query.AutoBatchInsertQuery<org.nlh4j.web.core.domain.entity.Function>(org.nlh4j.web.core.domain.entity._Function.getSingletonInternal());
            __query.setMethod(__method5);
            __query.setConfig(config);
            __query.setEntities(roles);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.FunctionDaoImpl");
            __query.setCallerMethodName("batchInsert");
            __query.setQueryTimeout(-1);
            __query.setBatchSize(-1);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.BatchInsertCommand __command = new org.seasar.doma.internal.jdbc.command.BatchInsertCommand(__query);
            int[] __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "batchInsert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.FunctionDaoImpl", "batchInsert", __e);
            throw __e;
        }
    }

}
