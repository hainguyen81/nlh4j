SELECT
		f.id
		, f.code
		, f.name
		, f.lang_key
		, f.func_order
		, f.enabled

FROM
		"function" f
WHERE
		true
/*%if unique != null && unique.id != null && unique.id > 0L*/
AND
		f.id = /* unique.id */123
/*%end*/
/*%if unique != null && (unique.id == null || unique.id <= 0L) && @isNotEmpty(unique.code)*/
AND
		f.code = COALESCE( /* unique.code */'abc', '' )
/*%end*/
/*%if unique == null || ((unique.id == null || unique.id <= 0L) && @isEmpty(unique.code))*/
AND
		false
/*%end*/

OFFSET 0 LIMIT 1
