<#-- このテンプレートに対応するデータモデルのクラスは org.seasar.doma.extension.gen.SqlDesc です -->
SELECT
	*
FROM
	${entityDesc.tableName}
<#if entityDesc.tenantIdEntityPropertyDesc??>
WHERE
	COMPANY_CD = /* companyCd */'9999'
AND
	VALID_FLAG = true
 </#if>
