<#-- このテンプレートに対応するデータモデルのクラスは org.seasar.doma.extension.gen.SqlDesc です -->
SELECT
	*
FROM
	${entityDesc.tableName}
WHERE
<#list entityDesc.idEntityPropertyDescs as property>
	${property.columnName} = /* ${property.name} */<#if property.number>1<#elseif property.time>${toTime("12:34:56")}<#elseif property.date>${toDate("2010-01-23")}<#elseif property.timestamp>${toTimestamp("2010-01-23 12:34:56")}<#else>'9999'</#if>
AND
</#list>
<#if entityDesc.idEntityPropertyDescs?size gt 0 && entityDesc.versionEntityPropertyDesc??>
	${entityDesc.versionEntityPropertyDesc.columnName} = /* ${entityDesc.versionEntityPropertyDesc.name} */1
</#if>
<#if entityDesc.logicalDeleteEntityPropertyDesc??>
AND
	VALID_FLAG = true
</#if>