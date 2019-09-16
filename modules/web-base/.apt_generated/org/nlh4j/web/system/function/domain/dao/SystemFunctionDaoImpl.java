package org.nlh4j.web.system.function.domain.dao;

/** */
@org.springframework.stereotype.Repository()
@javax.annotation.Generated(value = { "Doma", "1.38.0" }, date = "2019-07-21T22:32:03.469+0700")
public class SystemFunctionDaoImpl extends org.seasar.doma.internal.jdbc.dao.AbstractDao implements org.nlh4j.web.system.function.domain.dao.SystemFunctionDao {

    static {
        org.seasar.doma.internal.Artifact.validateVersion("1.38.0");
    }

    private static final java.lang.reflect.Method __method4 = org.seasar.doma.internal.jdbc.dao.AbstractDao.__getDeclaredMethod(org.nlh4j.web.system.function.domain.dao.SystemFunctionDao.class, "deleteFunctionBy", java.lang.String.class);

    /**
     * @param config the config
     */
    @org.springframework.beans.factory.annotation.Autowired()
    public SystemFunctionDaoImpl(org.seasar.doma.jdbc.Config config) {
        super(config);
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.FunctionDto> findFunctions(org.nlh4j.web.system.function.dto.FunctionSearchConditions conditions, org.seasar.doma.jdbc.SelectOptions options, java.lang.String orderBy) {
        entering("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctions", conditions, options, orderBy);
        try {
            if (conditions == null) {
                throw new org.seasar.doma.DomaNullPointerException("conditions");
            }
            if (options == null) {
                throw new org.seasar.doma.DomaNullPointerException("options");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/function/domain/dao/SystemFunctionDao/findFunctions.sql");
            __query.setOptions(options);
            __query.addParameter("conditions", org.nlh4j.web.system.function.dto.FunctionSearchConditions.class, conditions);
            __query.addParameter("orderBy", java.lang.String.class, orderBy);
            __query.setCallerClassName("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl");
            __query.setCallerMethodName("findFunctions");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.FunctionDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.FunctionDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.FunctionDto>(org.nlh4j.web.core.dto._FunctionDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.FunctionDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctions", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctions", __e);
            throw __e;
        }
    }

    @Override
    public org.nlh4j.web.core.dto.FunctionDto findFunction(org.nlh4j.web.system.function.dto.FunctionUniqueDto unique) {
        entering("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunction", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/function/domain/dao/SystemFunctionDao/findFunction.sql");
            __query.addParameter("unique", org.nlh4j.web.system.function.dto.FunctionUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl");
            __query.setCallerMethodName("findFunction");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.FunctionDto> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<org.nlh4j.web.core.dto.FunctionDto>(__query, new org.seasar.doma.internal.jdbc.command.EntitySingleResultHandler<org.nlh4j.web.core.dto.FunctionDto>(org.nlh4j.web.core.dto._FunctionDto.getSingletonInternal()));
            org.nlh4j.web.core.dto.FunctionDto __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunction", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunction", __e);
            throw __e;
        }
    }

    @Override
    public boolean isUniqueConstraint(org.nlh4j.web.system.function.dto.FunctionUniqueDto unique) {
        entering("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "isUniqueConstraint", unique);
        try {
            if (unique == null) {
                throw new org.seasar.doma.DomaNullPointerException("unique");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/function/domain/dao/SystemFunctionDao/isUniqueConstraint.sql");
            __query.addParameter("unique", org.nlh4j.web.system.function.dto.FunctionUniqueDto.class, unique);
            __query.setCallerClassName("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl");
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
            exiting("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "isUniqueConstraint", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "isUniqueConstraint", __e);
            throw __e;
        }
    }

    @Override
    public java.util.List<org.nlh4j.web.core.dto.FunctionDto> findFunctionsExcluded(java.util.List<java.lang.String> codes) {
        entering("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctionsExcluded", codes);
        try {
            if (codes == null) {
                throw new org.seasar.doma.DomaNullPointerException("codes");
            }
            org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileSelectQuery();
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/function/domain/dao/SystemFunctionDao/findFunctionsExcluded.sql");
            __query.addParameter("codes", java.util.List.class, codes);
            __query.setCallerClassName("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl");
            __query.setCallerMethodName("findFunctionsExcluded");
            __query.setResultEnsured(false);
            __query.setResultMappingEnsured(false);
            __query.setQueryTimeout(-1);
            __query.setMaxRows(-1);
            __query.setFetchSize(-1);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.FunctionDto>> __command = new org.seasar.doma.internal.jdbc.command.SelectCommand<java.util.List<org.nlh4j.web.core.dto.FunctionDto>>(__query, new org.seasar.doma.internal.jdbc.command.EntityResultListHandler<org.nlh4j.web.core.dto.FunctionDto>(org.nlh4j.web.core.dto._FunctionDto.getSingletonInternal()));
            java.util.List<org.nlh4j.web.core.dto.FunctionDto> __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctionsExcluded", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "findFunctionsExcluded", __e);
            throw __e;
        }
    }

    @Override
    public int deleteFunctionBy(java.lang.String code) {
        entering("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "deleteFunctionBy", code);
        try {
            org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery __query = new org.seasar.doma.internal.jdbc.query.SqlFileDeleteQuery();
            __query.setMethod(__method4);
            __query.setConfig(config);
            __query.setSqlFilePath("META-INF/org/nlh4j/web/system/function/domain/dao/SystemFunctionDao/deleteFunctionBy.sql");
            __query.addParameter("code", java.lang.String.class, code);
            __query.setCallerClassName("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl");
            __query.setCallerMethodName("deleteFunctionBy");
            __query.setQueryTimeout(-1);
            __query.setVersionIgnored(false);
            __query.setOptimisticLockExceptionSuppressed(false);
            __query.prepare();
            org.seasar.doma.internal.jdbc.command.DeleteCommand __command = new org.seasar.doma.internal.jdbc.command.DeleteCommand(__query);
            int __result = __command.execute();
            __query.complete();
            exiting("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "deleteFunctionBy", __result);
            return __result;
        } catch (java.lang.RuntimeException __e) {
            throwing("org.nlh4j.web.system.function.domain.dao.SystemFunctionDaoImpl", "deleteFunctionBy", __e);
            throw __e;
        }
    }

}
