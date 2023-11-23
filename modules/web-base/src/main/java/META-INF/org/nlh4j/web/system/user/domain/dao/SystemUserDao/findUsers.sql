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
		, null employee_call_name
		, null employee_first_name
		, null employee_last_name
		, null employee_name
		, null employee_tel

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
		true
/*%if conditions == null*/
AND
		false
/*%end*/
/*%if conditions != null*/
		-- if selected company (sys-admin or company of current logged-in user)
AND		(
				fn_iv_is_system_admin( /* conditions.getUsername() */'abc' )
			OR	( COALESCE( (
					SELECT
							COUNT( 1 )
					FROM
							"user" u1
					WHERE
							u1.enabled = true
					AND
							u1.user_name = /* conditions.getUsername() */'abc'
					AND		(
									u1.expired_at IS NULL
								OR	DATE_TRUNC( 'second', u1.expired_at ) <= DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
					)
			), 0 ) > 0 )
)
/*%end*/
/*%if conditions != null && conditions.sysAdmin != null*/
AND		COALESCE( u.sysadmin, false ) = /* conditions.sysAdmin */false
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isPrefix()*/
AND
		LOWER( u.user_name ) LIKE LOWER( /* @prefix(conditions.keyword) */'abc' ) escape '$'
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isSuffix()*/
AND
		LOWER( u.user_name ) LIKE LOWER( /* @suffix(conditions.keyword) */'abc' ) escape '$'
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isContain()*/
AND
		LOWER( u.user_name ) LIKE LOWER( /* @infix(conditions.keyword) */'abc' ) escape '$'
/*%end*/
/*%if conditions != null && conditions.enabled != null*/
AND
		u.enabled = /* conditions.enabled */true
/*%end*/
/*%if conditions != null && conditions.expired != null*/
AND		( CASE	WHEN ( /* conditions.expired */true = true )
				THEN (		u.expired_at IS NOT NULL
						AND	DATE_TRUNC( 'second', u.expired_at ) <= DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
				)
				ELSE (		u.expired_at IS NULL
						OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
				)
		END )
/*%end*/

/*# orderBy */

