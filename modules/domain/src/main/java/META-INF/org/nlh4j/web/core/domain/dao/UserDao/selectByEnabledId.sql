SELECT
		u.id
		, u.cid
		, u.gid
		, null eid
		, rg.code group_code
		, u.user_name username
		, u.password
		, u.email
		, COALESCE( u.enabled, false ) enabled
		, COALESCE( u.sysadmin, false ) sysadmin
		, u.language
		, u.description
		, uo.login_at
		, u.expired_at
		, u.created_at
		, u.created_user

FROM
		"user" u

LEFT JOIN
		role_group rg
ON
		rg.id = u.gid

LEFT JOIN
		user_online uo
ON
		uo.uid = u.id
AND
		uo.logout_at IS NULL

WHERE
		COALESCE( u.enabled, false ) = true
AND
		u.id = /* id */1
