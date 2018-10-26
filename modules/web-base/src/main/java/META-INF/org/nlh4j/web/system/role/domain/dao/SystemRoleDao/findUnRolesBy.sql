SELECT
		null id
		, false viewable
		, false insertable
		, false updatable
		, false deletable
		, m.id mid
		, m.code module_cd
		, m.name module_name
		, m.lang_key module_lang_key
		, m.main_url module_link
		, m.description module_description
		, null gid
		, null group_cd
		, null group_name

FROM
		fn_iv_modules( null ) m

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
		( COALESCE( (
			SELECT
					COUNT( 1 )
			FROM
					role r
			LEFT JOIN
					role_group rg
			ON
					rg.id = r.gid
			WHERE
					rg.id = /* unique.id */123
			AND
					r.mid = m.id
		), 0 ) <= 0 )
/*%end*/
/*%if unique != null && (unique.id == null || unique.id <= 0L) && @isNotEmpty(unique.code)*/
AND
		( COALESCE( (
			SELECT
					COUNT( 1 )
			FROM
					role r
			LEFT JOIN
					role_group rg
			ON
					rg.id = r.gid
			WHERE
					rg.code = COALESCE( /* unique.code */'abc', '' )
			AND
					r.mid = m.id
		), 0 ) <= 0 )
/*%end*/

/*# orderBy */

