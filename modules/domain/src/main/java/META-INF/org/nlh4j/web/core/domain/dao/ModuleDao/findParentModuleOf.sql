SELECT
		m.*

FROM
		fn_iv_modules_by_user( null, /* userName */'abc' ) m

WHERE
		true
/*%if visibled != null */
AND
		m.visibled = /* visibled */false
/*%end*/
/*%if ui != null */
AND
		m.service = ( CASE WHEN /* ui */false THEN false ELSE true END )
/*%end*/
AND
		m.id = (
			SELECT
					mp.pid
			FROM
					fn_iv_modules( null ) mp
			WHERE
					-- check by regular expression
					/* requestURI */'abc' ~ mp.url_regex
			OFFSET 0 LIMIT 1
		)