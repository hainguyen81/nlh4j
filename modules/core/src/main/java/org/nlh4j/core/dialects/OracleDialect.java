/*
 * @(#)OracleDialect.java 1.0 Aug 28, 2015
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
 * Custom {@link org.seasar.doma.jdbc.dialect.OracleDialect}
 * (for table name with special characters)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class OracleDialect extends org.seasar.doma.jdbc.dialect.OracleDialect implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public OracleDialect() { super(); }
    public OracleDialect(ExpressionFunctions expressionFunctions) {
        super(expressionFunctions);
    }
    public OracleDialect(
            JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor,
            ExpressionFunctions expressionFunctions) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    }
    public OracleDialect(
            JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor);
    }
    public OracleDialect(JdbcMappingVisitor jdbcMappingVisitor) {
        super(jdbcMappingVisitor);
    }
    public OracleDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(sqlLogFormattingVisitor);
    }

    /* (Non-Javadoc)
     * @see org.seasar.doma.jdbc.dialect.OracleDialect#getSequenceNextValSql(java.lang.String, long)
     */
    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
        // detect special characters
        qualifiedSequenceName = StringUtils.refixSqlObjectName(qualifiedSequenceName);
        return super.getSequenceNextValSql(qualifiedSequenceName, allocationSize);
    }
}
