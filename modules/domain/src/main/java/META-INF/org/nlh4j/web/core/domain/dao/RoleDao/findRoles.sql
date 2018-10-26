SELECT
		r.id
		, r.functions
		, m.id mid
		, m.code module_cd
		, m.name module_name
		, m.lang_key module_lang_key
		, m.main_url module_link
		, r.gid
		, rg.code group_cd
		, rg.name group_name

FROM
		fn_iv_modules_by_user( null, /* userName */'abc' ) m

LEFT JOIN
		role r
ON
		r.mid = m.id

LEFT JOIN
		role_group rg
ON
		rg.id = r.gid

LEFT JOIN
		"user" u
ON
		u.gid = rg.id

WHERE
		r.id is not null
AND
		rg.id is not null
AND
		COALESCE( u.enabled, false ) = true
-- AND		(
-- 				u.expired_at IS NULL
-- 			OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
-- )
AND
		u.user_name = /* userName */'abc'
-- AND		(
-- 				COALESCE( r.readable, false ) = true
-- 			OR	COALESCE( r.writable, false ) = true
-- )
