-- Function: fn_iv_modules_by_user(text, text, text)

DROP FUNCTION IF EXISTS fn_iv_modules_by_user(text, text, text) CASCADE;
CREATE OR REPLACE FUNCTION fn_iv_modules_by_user(IN codes text, IN ccd text, IN ucd text)
RETURNS TABLE(
	id bigint
	, pid bigint
	, code character varying
	, name character varying
	, lang_key character varying
	, mod_order integer
	, enabled boolean
	, visibled boolean
	, service boolean
	, common boolean
	, url_regex character varying
	, main_url character varying
	, css character varying
	, description text
	, library character varying
	, forward_url character varying
	, template_url character varying
	, ngcontroller character varying
	, childs bigint
	, depth bigint
	, leaf boolean
	, functions text[]
) AS
$BODY$

	-- select accessed modules
	WITH accessed_modules(
			pid
			, childs
	) AS (
			SELECT
					m.pid
					, COUNT( 1 ) childs

			FROM
					fn_iv_modules( $1 ) m

			WHERE
					-- this module is enabled
					m.enabled = true
			AND		(
							-- this module is a common module (so all users has readable permission)
							m.common = true

							-- system administrator
						OR	fn_iv_is_system_admin( $3 )

							-- this parent module has been assigned to user
						OR	( COALESCE( (
									SELECT
											COUNT( 1 )
									FROM
											module mp
									LEFT JOIN
											role rl
									ON
											rl.mid = mp.id
									LEFT JOIN
											role_group rg
									ON
											rg.id = rl.gid
									LEFT JOIN
											"user" u
									ON
											u.gid = rg.id
									LEFT JOIN
											company c
									ON
											c.id = u.cid
									LEFT JOIN
											module_company mc
									ON
											mc.mid = mp.id
									AND
											mc.cid = c.id
									WHERE
											mc.id IS NOT NULL
									AND
											COALESCE( mp.pid, 0 ) = m.id
									AND
											u.user_name = $3
									AND
											u.enabled = true
									AND		(
													u.expired_at IS NULL
												OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
									)
									AND		(
													CHAR_LENGTH( TRIM( COALESCE( $2, '' ) ) ) <= 0
												OR	c.code = TRIM( COALESCE( $2, '' ) )
									)
							), 0 ) > 0 )

							-- this module has been assigned to user
						OR	( COALESCE( (
									SELECT
											COUNT( 1 )
									FROM
											module mp
									LEFT JOIN
											role rl
									ON
											rl.mid = mp.id
									LEFT JOIN
											role_group rg
									ON
											rg.id = rl.gid
									LEFT JOIN
											"user" u
									ON
											u.gid = rg.id
									LEFT JOIN
											company c
									ON
											c.id = u.cid
									LEFT JOIN
											module_company mc
									ON
											mc.mid = mp.id
									AND
											mc.cid = c.id
									WHERE
											mc.mid = m.id
									AND
											u.user_name = $3
									AND
											u.enabled = true
									AND		(
													u.expired_at IS NULL
												OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
									)
									AND		(
													CHAR_LENGTH( TRIM( COALESCE( $2, '' ) ) ) <= 0
												OR	c.code = TRIM( COALESCE( $2, '' ) )
									)
							), 0 ) > 0 )
			)

			GROUP BY
					m.pid
	)
	SELECT
			m.id
			, m.pid
			, m.code
			, m.name
			, m.lang_key
			, m.mod_order
			, m.enabled
			, m.visibled
			-- show on UI or background service
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
			, ( CASE	WHEN m.leaf
						THEN m.childs
						ELSE COALESCE( am.childs, 0 )
			END ) childs
			, m.depth
			, m.leaf
			, fn_iv_distinct_array(
					string_to_array( ( CASE	WHEN	( m.common OR fn_iv_is_system_admin( $3 ) )
								THEN	null
								ELSE	( '|' || COALESCE( (
												SELECT
														string_agg( fn.code, '|' )
												FROM
														functions fn
												WHERE	(
															COALESCE( (
																	SELECT
																			COUNT( 1 )
																	FROM
																			role rl
																	LEFT JOIN
																			role_group rg
																	ON
																			rg.id = rl.gid
																	LEFT JOIN
																			"user" u
																	ON
																			u.gid = rg.id
																	LEFT JOIN
																			company c
																	ON
																			c.id = u.cid
																	LEFT JOIN
																			module_company mc
																	ON
																			mc.cid = c.id
																	AND
																			mc.mid = rl.mid
																	WHERE
																			mc.mid = m.id
																	AND
																			u.user_name = $3
																	AND
																			u.enabled = true
																	AND		(
																					u.expired_at IS NULL
																				OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
																	)
																	AND		(
																					CHAR_LENGTH( TRIM( COALESCE( $2, '' ) ) ) <= 0
																				OR	c.code = TRIM( COALESCE( $2, '' ) )
																	)
																	AND		(
																				( '|' || lower( fn.code ) || '|' ) LIKE ('%|' || rl.functions || '|%' )
																	)
															), 0 ) > 0
												)
										), '' ) || '|' )
					END ), '|' )
			) "functions"
	FROM
			fn_iv_modules( $1 ) m

	LEFT JOIN
			accessed_modules am
	ON
			am.pid = m.id

	WHERE
			-- this module is enabled
			m.enabled = true
	AND		(
					-- this module is a common module (so all users has readable permission)
					m.common = true

					-- system administrator
				OR	fn_iv_is_system_admin( $3 )

					-- this parent module has been assigned to user
				OR	( COALESCE( (
							SELECT
									COUNT( 1 )
							FROM
									module mp
							LEFT JOIN
									role rl
							ON
									rl.mid = mp.id
							LEFT JOIN
									role_group rg
							ON
									rg.id = rl.gid
							LEFT JOIN
									"user" u
							ON
									u.gid = rg.id
							LEFT JOIN
									company c
							ON
									c.id = u.cid
							LEFT JOIN
									module_company mc
							ON
									mc.mid = mp.id
							AND
									mc.cid = c.id
							WHERE
									mc.id IS NOT NULL
							AND
									COALESCE( mp.pid, 0 ) = m.id
							AND
									u.user_name = $3
							AND
									u.enabled = true
							AND		(
											u.expired_at IS NULL
										OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
							)
							AND		(
											CHAR_LENGTH( TRIM( COALESCE( $2, '' ) ) ) <= 0
										OR	c.code = TRIM( COALESCE( $2, '' ) )
							)
					), 0 ) > 0 )

					-- this module has been assigned to user
				OR	( COALESCE( (
							SELECT
									COUNT( 1 )
							FROM
									module mp
							LEFT JOIN
									role rl
							ON
									rl.mid = mp.id
							LEFT JOIN
									role_group rg
							ON
									rg.id = rl.gid
							LEFT JOIN
									"user" u
							ON
									u.gid = rg.id
							LEFT JOIN
									company c
							ON
									c.id = u.cid
							LEFT JOIN
									module_company mc
							ON
									mc.mid = mp.id
							AND
									mc.cid = c.id
							WHERE
									mc.mid = m.id
							AND
									u.user_name = $3
							AND
									u.enabled = true
							AND		(
											u.expired_at IS NULL
										OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
							)
							AND		(
											CHAR_LENGTH( TRIM( COALESCE( $2, '' ) ) ) <= 0
										OR	c.code = TRIM( COALESCE( $2, '' ) )
							)
					), 0 ) > 0 )
	)

$BODY$
	LANGUAGE sql VOLATILE
	COST 100
	ROWS 1000;
ALTER FUNCTION fn_iv_modules_by_user(text, text, text)
	OWNER TO postgres;
	COMMENT ON FUNCTION fn_iv_modules_by_user(text, text, text) IS 'select modules that the specified user have permission';

