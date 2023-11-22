/*
 * @(#)PostgresDialect.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dialects;

import java.io.Serializable;

import org.nlh4j.util.StringUtils;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;

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
    public PreparedSql getIdentitySelectSql(String catalogName, String schemaName, String tableName, String columnName,
    		boolean isQuoteRequired, boolean isIdColumnQuoteRequired) {
    	// detect special characters
        return super.getIdentitySelectSql(catalogName, schemaName,
        		StringUtils.refixSqlObjectName(tableName), columnName, isQuoteRequired, isIdColumnQuoteRequired);
    }

    /* (Non-Javadoc)
     * @see org.seasar.doma.jdbc.dialect.PostgresDialect#getSequenceNextValSql(java.lang.String, long)
     */
    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
        // detect special characters
        return super.getSequenceNextValSql(StringUtils.refixSqlObjectName(qualifiedSequenceName), allocationSize);
    }
}
