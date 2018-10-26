SELECT
		rg.id
		, rg.code
	    , rg.name
		, COALESCE( (
				SELECT
						COUNT( 1 )
				FROM
						role r
				WHERE
						r.gid = rg.id
		) ) roles_count
	    , rg.description

FROM
		role_group rg

LEFT JOIN
		role_company rc
ON
		rc.gid = rg.id

LEFT JOIN
		company c
ON
		rc.cid = c.id

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
							u1.cid = c.id
					AND
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
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isPrefix()*/
AND		(
				LOWER(rg.code) LIKE LOWER(/* @prefix(conditions.keyword) */'abc') escape '$'
			OR	LOWER(rg.name) LIKE LOWER(/* @prefix(conditions.keyword) */'abc') escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isSuffix()*/
AND		(
				LOWER(rg.code) LIKE LOWER(/* @suffix(conditions.keyword) */'abc') escape '$'
			OR	LOWER(rg.name) LIKE LOWER(/* @suffix(conditions.keyword) */'abc') escape '$'
)
/*%end*/
/*%if conditions != null && @isNotEmpty(conditions.keyword) && conditions.isContain()*/
AND		(
				LOWER(rg.code) LIKE LOWER(/* @contain(conditions.keyword) */'abc') escape '$'
			OR	LOWER(rg.name) LIKE LOWER(/* @contain(conditions.keyword) */'abc') escape '$'
)	
/*%end*/

/*# orderBy */

