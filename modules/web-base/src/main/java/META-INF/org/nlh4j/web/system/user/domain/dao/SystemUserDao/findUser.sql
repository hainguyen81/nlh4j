SELECT
		u.id
		, u.gid
		, u.user_name
		, u.email
		, u.enabled
		, u.sysadmin
		, u.language
		, u.description
		, uo.login_at
		, u.expired_at
		, rg.code group_code
		, rg.name group_name
		, null cid
		, null company_code
		, null company_name
		, null eid
		, null employee_code
		, null employee_name
		, null employee_tel

FROM
		"user" u

LEFT JOIN
		user_online uo
ON
		uo.uid = u.id
AND
		uo.logout_at IS NULL

LEFT JOIN
		role_group rg
ON
		rg.id = u.gid

WHERE
/*%if unique != null && unique.id != null && unique.id > 0L*/
AND
		u.id = /* unique.id */123
/*%end*/
/*%if unique != null && (unique.id == null || unique.id <= 0L) && @isNotEmpty(unique.username)*/
AND
		u.user_name = COALESCE( /* unique.username */'abc', '' )
/*%end*/
/*%if unique == null || ((unique.id == null || unique.id <= 0L) && @isEmpty(unique.username))*/
AND
		false
/*%end*/

OFFSET 0 LIMIT 1
