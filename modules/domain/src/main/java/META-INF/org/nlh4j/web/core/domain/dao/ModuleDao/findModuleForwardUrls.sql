SELECT
		m.forward_url

FROM
		fn_iv_modules_by_user( null, /* userName */'abc' ) m
WHERE
		true
AND
		CHAR_LENGTH( COALESCE( m.forward_url, '' ) ) > 0
/*%if moduleCds != null && moduleCds.size() > 0 */
AND
		m.code IN /* moduleCds */()
/*%end*/
