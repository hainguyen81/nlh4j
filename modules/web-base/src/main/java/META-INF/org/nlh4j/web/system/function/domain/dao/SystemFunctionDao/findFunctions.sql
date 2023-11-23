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
/*%if conditions == null*/
AND
		false
/*%end*/
/*%if conditions != null*/
AND		(
				-- system administrator
				fn_iv_is_system_admin( /* conditions.getUsername() */'abc' )
				-- specified user has been assigned into company
			OR	( COALESCE( (
						SELECT
								COUNT( 1 )
						FROM
								"user" u
						WHERE
								u.user_name = /* conditions.getUsername() */'abc'
						AND
								u.enabled = true
						AND		(
										u.expired_at IS NULL
									OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
						)
				), 0 ) > 0 )
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isPrefix()*/
AND		(
				LOWER( f.code ) LIKE LOWER( /* @prefix(conditions.keyword) */'abc' ) escape '$'
			AND	LOWER( f.name ) LIKE LOWER( /* @prefix(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isSuffix()*/
AND		(
				LOWER( f.code ) LIKE LOWER( /* @suffix(conditions.keyword) */'abc' ) escape '$'
			OR	LOWER( f.name ) LIKE LOWER( /* @suffix(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isContain()*/
AND		(
				LOWER( f.code ) LIKE LOWER( /* @infix(conditions.keyword) */'abc' ) escape '$'
			OR	LOWER( f.name ) LIKE LOWER( /* @infix(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && conditions.enabled != null */
AND		f.enabled = /* conditions.enabled */true
/*%end*/

/*# orderBy */

