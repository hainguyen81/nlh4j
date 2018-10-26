SELECT
		m.*

FROM
		fn_iv_modules_by_user( null, /* userName */'abc' ) m
WHERE
		true
/*%if pid != null && pid <= 0L */
AND
		COALESCE( m.pid, 0 ) = 0
/*%end*/
/*%if pid != null && pid > 0L */
AND
		COALESCE( m.pid, 0 ) = /* pid */123
/*%end*/
/*%if visibled != null */
AND
		COALESCE( m.visibled, false ) = /* visibled */false
/*%end*/
/*%if enabled != null */
AND
		COALESCE( m.enabled, false ) = /* enabled */false
/*%end*/
/*%if leaf != null */
AND
		COALESCE( m.leaf, false ) = /* leaf */false
/*%end*/
/*%if ui != null */
AND
		COALESCE( m.service, false ) = ( CASE WHEN /* ui */false THEN false ELSE true END )
/*%end*/

