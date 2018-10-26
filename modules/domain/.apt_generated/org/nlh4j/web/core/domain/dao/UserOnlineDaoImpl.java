package org.nlh4j.web.core.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-21T18:31:17.454+0700")
public class UserOnlineDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.core.domain.dao.UserOnlineDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method2 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.UserOnlineDao.class, "insert", org.nlh4j.web.core.domain.entity.UserOnline.class);

    private static final java.lang.reflect.Method __method3 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.UserOnlineDao.class, "update", org.nlh4j.web.core.domain.entity.UserOnline.class);

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.UserOnlineDao.class, "delete", org.nlh4j.web.core.domain.entity.UserOnline.class);

    private static final java.lang.reflect.Method __method7 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.UserOnlineDao.class, "logout", java.util.List.class);

    private static final java.lang.reflect.Method __method8 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.core.domain.dao.UserOnlineDao.class, "logoutByUserNames", java.util.List.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public UserOnlineDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.domain.entity.UserOnline> selectAll() {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectAll");
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/selectAll.sql");
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("selectAll");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.UserOnline>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.domain.entity.UserOnline>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.domain.entity.UserOnline> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectAll", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectAll", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline selectById(java.lang.Long id) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectById", id);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/selectById.sql");
            __query.addParameter("id", java.lang.Long.class, id);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("selectById");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.UserOnline __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectById", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "selectById", __e);
            throw __e;
        }
    }

    @Override
    public int insert(org.nlh4j.web.core.domain.entity.UserOnline entity) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "insert", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.UserOnline> __query = new org.seasar.doma.internal.jdbc.query.AutoInsertQuery<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal());
            __query.setMethod(__method2);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("insert");
            __query.setQueryTimeout(-1);
            __query.setNullExcluded(false);
            __query.setIncludedPropertyNames();
            __query.setExcludedPropertyNames();
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.InsertCommand __command = new org.seasar.doma.internal.jdbc.command.InsertCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "insert", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "insert", __e);
            throw __e;
        }
    }

    @Override
    public int update(org.nlh4j.web.core.domain.entity.UserOnline entity) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "update", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.UserOnline> __query = new org.seasar.doma.internal.jdbc.query.AutoUpdateQuery<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal());
            __query.setMethod(__method3);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
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
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "update", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "update", __e);
            throw __e;
        }
    }

    @Override
    public int delete(org.nlh4j.web.core.domain.entity.UserOnline entity) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "delete", entity);
        try {
            if (entity == null) {
                throw new org.seasar.doma.DomaNullPointerException("entity");
            }
            org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.UserOnline> __query = new org.seasar.doma.internal.jdbc.query.AutoDeleteQuery<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal());
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setEntity(entity);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("delete");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "delete", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "delete", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline findOnlineUser(java.lang.Long uid) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUser", uid);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/findOnlineUser.sql");
            __query.addParameter("uid", java.lang.Long.class, uid);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("findOnlineUser");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.UserOnline __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUser", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUser", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.domain.entity.UserOnline findOnlineUserByUserName(java.lang.String userName) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUserByUserName", userName);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/findOnlineUserByUserName.sql");
            __query.addParameter("userName", java.lang.String.class, userName);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("findOnlineUserByUserName");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.domain.entity.UserOnline>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.domain.entity.UserOnline>(org.nlh4j.web.core.domain.entity._UserOnline.getSingletonInternal()));
            org.nlh4j.web.core.domain.entity.UserOnline __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUserByUserName", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "findOnlineUserByUserName", __e);
            throw __e;
        }
    }

    @Override
    public int logout(java.util.List<java.lang.Long> uids) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logout", uids);
        try {
            if (uids == null) {
                throw new org.seasar.doma.DomaNullPointerException("uids");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery();
            __query.setMethod(__method7);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/logout.sql");
            __query.addParameter("uids", java.util.List.class, uids);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("logout");
            __query.setQueryTimeout(-1);
            __query.setVersionIncluded(false);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.UpdateCommand __command = new org.seasar.doma.internal.jdbc.command.UpdateCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logout", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logout", __e);
            throw __e;
        }
    }

    @Override
    public int logoutByUserNames(java.util.List<java.lang.String> userNames) {
        entering("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logoutByUserNames", userNames);
        try {
            if (userNames == null) {
                throw new org.seasar.doma.DomaNullPointerException("userNames");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileUpdateQuery();
            __query.setMethod(__method8);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/core/domain/dao/UserOnlineDao/logoutByUserNames.sql");
            __query.addParameter("userNames", java.util.List.class, userNames);
            __query.setCallerClassName("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl");
            __query.setCallerMethodName("logoutByUserNames");
            __query.setQueryTimeout(-1);
            __query.setVersionIncluded(false);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.UpdateCommand __command = new org.seasar.doma.internal.jdbc.command.UpdateCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logoutByUserNames", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.core.domain.dao.UserOnlineDaoImpl", "logoutByUserNames", __e);
            throw __e;
        }
    }

}
