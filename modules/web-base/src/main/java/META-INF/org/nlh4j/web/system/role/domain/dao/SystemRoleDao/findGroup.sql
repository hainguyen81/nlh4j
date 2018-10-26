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

WHERE
		true
/*%if unique != null && unique.id != null && unique.id > 0L*/
AND
		rg.id = /* unique.id */123
/*%end*/
/*%if unique != null && @isNotEmpty(unique.code)*/
AND
		rg.code = COALESCE( /* unique.code */'abc', '' )
/*%end*/
/*%if unique == null || ((unique.id == null || unique.id <= 0L) && @isEmpty(unique.code))*/
AND
		false
/*%end*/

OFFSET 0 LIMIT 1
