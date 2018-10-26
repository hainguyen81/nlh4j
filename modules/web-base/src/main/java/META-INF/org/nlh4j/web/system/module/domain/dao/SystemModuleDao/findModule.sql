SELECT
		m.id
		, m.pid
		, m.code
		, m.name
		, m.lang_key
		, m.mod_order
		, m.enabled
		, m.visibled
		, m.service
		, m.common
		, m.url_regex
		, m.main_url
		, m.css
		, m.description
		, m.library
		, m.forward_url
		, m.template_url
		, m.ngcontroller
		, m.childs
		, m.depth
		, m.leaf
		, false viewable
		, false insertable
		, false updatable
		, false deletable

FROM
		fn_iv_modules( null ) m
WHERE
		true
/*%if unique != null && unique.id != null && unique.id > 0L*/
AND
		m.id = /* unique.id */123
/*%end*/
/*%if unique != null && (unique.id == null || unique.id <= 0L) && @isNotEmpty(unique.code)*/
AND
		m.code = COALESCE( /* unique.code */'abc', '' )
/*%end*/
/*%if unique == null || ((unique.id == null || unique.id <= 0L) && @isEmpty(unique.code))*/
AND
		false
/*%end*/

OFFSET 0 LIMIT 1
