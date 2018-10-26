SELECT
		r.id
		, COALESCE( r.viewable, false ) viewable
		, COALESCE( r.insertable, false ) insertable
		, COALESCE( r.updatable, false ) updatable
		, COALESCE( r.updatable, false ) deletable
		, m.id mid
		, m.code module_cd
		, m.name module_name
		, m.lang_key module_lang_key
		, m.main_url module_link
		, m.description module_description
		, rg.id gid
		, rg.code group_cd
		, rg.name group_name

FROM
		role r

INNER JOIN
		role_group rg
ON
		rg.id = r.gid

INNER JOIN
		fn_iv_modules( null ) m
ON
		m.id = r.mid

WHERE
		-- only selecting leaf nodes
		COALESCE( m.leaf, false ) = true
AND
		-- only selecting not common modules
		COALESCE( m.common, false ) = false
/*%if (enabled != null) */
AND
		COALESCE( m.enabled, false ) = /* enabled */false
/*%end*/
/*%if unique != null && unique.id != null && unique.id > 0L*/
AND
		rg.id = /* unique.id */123
/*%end*/
/*%if unique != null && (unique.id == null || unique.id <= 0L) && @isNotEmpty(unique.code)*/
AND
		COALESCE( rg.code, '' ) = COALESCE( /* unique.code */'abc', '' )
/*%end*/
/*%if unique == null || ((unique.id == null || unique.id <= 0L) && @isEmpty(unique.code))*/
AND
		false
/*%end*/

/*# orderBy */

