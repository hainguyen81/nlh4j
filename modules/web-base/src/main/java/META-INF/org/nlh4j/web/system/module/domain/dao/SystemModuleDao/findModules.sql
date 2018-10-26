SELECT
		m.id
		, m.pid
		, m.code
		, m.name
		, m.lang_key
		, m.mod_order
		, m.enabled
		, m.visibled
		, m.service
		, m.common
		, m.url_regex
		, m.main_url
		, m.css
		, m.description
		, m.library
		, m.forward_url
		, m.template_url
		, m.ngcontroller
		, m.childs
		, m.depth
		, m.leaf
		, false viewable
		, false insertable
		, false updatable
		, false deletable

FROM
		fn_iv_modules( null ) m
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
				LOWER( m.code ) LIKE LOWER( /* @prefix(conditions.keyword) */'abc' ) escape '$'
			AND	LOWER( m.name ) LIKE LOWER( /* @prefix(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isSuffix()*/
AND		(
				LOWER( m.code ) LIKE LOWER( /* @suffix(conditions.keyword) */'abc' ) escape '$'
			OR	LOWER( m.name ) LIKE LOWER( /* @suffix(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isContain()*/
AND		(
				LOWER( m.code ) LIKE LOWER( /* @contain(conditions.keyword) */'abc' ) escape '$'
			OR	LOWER( m.name ) LIKE LOWER( /* @contain(conditions.keyword) */'abc' ) escape '$'
)
/*%end*/
/*%if conditions != null && conditions.enabled != null */
AND		m.enabled = /* conditions.enabled */true
/*%end*/
/*%if conditions != null && conditions.common != null */
AND		m.common = /* conditions.common */true
/*%end*/
/*%if conditions != null && conditions.service != null */
AND		m.service = /* conditions.service */true
/*%end*/
/*%if conditions != null && conditions.visibled != null */
AND		m.visibled = /* conditions.visibled */true
/*%end*/

/*# orderBy */

