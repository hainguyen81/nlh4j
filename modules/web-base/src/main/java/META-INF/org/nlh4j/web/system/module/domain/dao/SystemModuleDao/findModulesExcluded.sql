SELECT
		m.id
		, m.pid
		, m.code
		, m.name
		, m.lang_key
		, m.depth

FROM
		fn_iv_modules( null ) m
WHERE
		true
/*%if codes != null && codes.size() > 0*/
/*%for code: codes */
/*# "AND " */
		NOT( (
				SELECT
						mod.id
				FROM
						module mod
				WHERE
						mod.code = /* code */'abc'
		) = ANY( m.path ) )
/*%end */
/*%end*/

