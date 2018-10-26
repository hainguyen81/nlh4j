/*
 * @(#)PostgresDialect.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dialects;

import java.io.Serializable;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;

import org.nlh4j.util.StringUtils;

/**
 * Custom {@link org.seasar.doma.jdbc.dialect.PostgresDialect}
 * (for table name with special characters)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class PostgresDialect
    extends org.seasar.doma.jdbc.dialect.PostgresDialect
    implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public PostgresDialect() { super(); }
    public PostgresDialect(ExpressionFunctions expressionFunctions) {
        super(expressionFunctions);
    }
    public PostgresDialect(
            JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    }
    public PostgresDialect(
            JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor);
    }
    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        super(jdbcMappingVisitor);
    }
    public PostgresDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(sqlLogFormattingVisitor);
    }

    /* (Non-Javadoc)
     * @see org.seasar.doma.jdbc.dialect.PostgresDialect#getIdentitySelectSql(java.lang.String, java.lang.String)
     */
    @Override
    public PreparedSql getIdentitySelectSql(String qualifiedTableName, String columnName) {
        // detect special characters
        qualifiedTableName = StringUtils.refixSqlObjectName(qualifiedTableName);
        return super.getIdentitySelectSql(qualifiedTableName, columnName);
    }

    /* (Non-Javadoc)
     * @see org.seasar.doma.jdbc.dialect.PostgresDialect#getSequenceNextValSql(java.lang.String, long)
     */
    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
        // detect special characters
        qualifiedSequenceName = StringUtils.refixSqlObjectName(qualifiedSequenceName);
        return super.getSequenceNextValSql(qualifiedSequenceName, allocationSize);
    }
}
