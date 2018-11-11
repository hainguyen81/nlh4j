package org.nlh4j.web.system.user.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2018-10-30T07:32:17.837+0700")
public class SystemUserDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.system.user.domain.dao.SystemUserDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public SystemUserDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.system.user.domain.entity.UserEx> findUsers(org.nlh4j.web.system.user.dto.UserSearchConditions conditions, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUsers", conditions, options, orderBy);
        try {
            if (conditions == null) {
                throw new org.seasar.doma.DomaNullPointerException("conditions");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/user/domain/dao/SystemUserDao/findUsers.sql");
            __query.setOptions(options);
            __query.addParameter("conditions", org.nlh4j.web.system.user.dto.UserSearchConditions.class, conditions);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl");
            __query.setCallerMethodName("findUsers");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.system.user.domain.entity.UserEx>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.system.user.domain.entity.UserEx>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.system.user.domain.entity.UserEx>(org.nlh4j.web.system.user.domain.entity._UserEx.getSingletonInternal()));
            java.util.List<org.nlh4j.web.system.user.domain.entity.UserEx> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUsers", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUsers", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.system.user.dto.UserDto findUser(org.nlh4j.web.base.changepwd.dto.UserUniqueDto unique) {
        entering("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUser", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/user/domain/dao/SystemUserDao/findUser.sql");
            __query.addParameter("unique", org.nlh4j.web.base.changepwd.dto.UserUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl");
            __query.setCallerMethodName("findUser");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.system.user.dto.UserDto> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.system.user.dto.UserDto>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.system.user.dto.UserDto>(org.nlh4j.web.system.user.dto._UserDto.getSingletonInternal()));
            org.nlh4j.web.system.user.dto.UserDto __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUser", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "findUser", __e);
            throw __e;
        }
    }

    @Override
    public boolean isUniqueConstraint(org.nlh4j.web.base.changepwd.dto.UserUniqueDto unique) {
        entering("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "isUniqueConstraint", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/user/domain/dao/SystemUserDao/isUniqueConstraint.sql");
            __query.addParameter("unique", org.nlh4j.web.base.changepwd.dto.UserUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl");
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
            exiting("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "isUniqueConstraint", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "isUniqueConstraint", __e);
            throw __e;
        }
    }

    @Override
    public int deleteUser(java.lang.Long uid) {
        entering("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "deleteUser", uid);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/user/domain/dao/SystemUserDao/deleteUser.sql");
            __query.addParameter("uid", java.lang.Long.class, uid);
            __query.setCallerClassName("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl");
            __query.setCallerMethodName("deleteUser");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Integer> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.lang.Integer>(__query, new org.seasar.doma.internal.jdbc.command.BasicSingleResultHandler<java.lang.Integer>(new org.seasar.doma.wrapper.IntegerWrapper(), true));
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "deleteUser", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.user.domain.dao.SystemUserDaoImpl", "deleteUser", __e);
            throw __e;
        }
    }

}
