-- Function: fn_iv_modules(text)

DROP FUNCTION IF EXISTS fn_iv_modules(text) CASCADE;
CREATE OR REPLACE FUNCTION fn_iv_modules(IN codes text)
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
	, childs_path bigint[]
	, path bigint[]
	, depth bigint
	, leaf boolean
) AS
$BODY$

	WITH RECURSIVE nodes(
		id
		, pid
		, code
		, name
		, lang_key
		, mod_order
		, enabled
		, visibled
		, service
		, common
		, url_regex
		, main_url
		, css
		, description
		, library
		, forward_url
		, template_url
		, ngcontroller
		, childs
		, path
		, depth
		, orders
	) AS (

		-- root nodes info
		SELECT
				m.id
				, COALESCE( m.pid, 0 ) pid
				, m.code
				, m.name
				, m.lang_key
				, m.mod_order
				, COALESCE( m.enabled, false ) enabled
				, COALESCE( m.visibled, false ) visibled
				-- show on UI or background service
				, COALESCE( m.service, false ) service
				, COALESCE( m.common, false ) common
				, m.url_regex
				, m.main_url
				, m.css
				, m.description
				, m.library
				, m.forward_url
				, m.template_url
				, m.ngcontroller
				-- recursive tree info
				, COALESCE( (
					SELECT
							COUNT( 1 )
					FROM
							module m1
					WHERE
							COALESCE( m1.pid, 0 ) = m.id
				), 0 ) childs
				, ARRAY[ COALESCE( m.id, 0 ) ] path
				, 1 depth
				, ARRAY[ COALESCE( m.mod_order, COALESCE( m.id, 0 ) ) ] orders

		FROM
				module AS m

		WHERE	(
						CHAR_LENGTH( COALESCE( $1, '' ) ) > 0
					AND	CHAR_LENGTH( COALESCE( m.code, '' ) ) > 0
					AND	COALESCE( m.code, '' ) = ANY( STRING_TO_ARRAY( COALESCE( $1, '0' ), ',' ) )
		)
		OR		(
						CHAR_LENGTH( COALESCE( $1, '' ) ) <= 0
					AND	COALESCE( m.pid, 0 ) = 0
		)

	UNION ALL

		-- child nodes info
		SELECT
				m.id
				, COALESCE( m.pid, 0 ) pid
				, m.code
				, m.name
				, m.lang_key
				, m.mod_order
				-- if parent's enabled; then enabling by child
				-- else if parent's disabled then disabling child
				, ( CASE	WHEN nd.enabled
							THEN COALESCE( m.enabled, false )
							ELSE nd.enabled
				END ) enabled
				-- if parent's visibled; then making visible by child
				-- else if parent's in-visibled then making child in-visible
				, ( CASE	WHEN nd.visibled
							THEN COALESCE( m.visibled, false )
							ELSE nd.visibled
				END ) visibled
				-- show on UI or background service
				, COALESCE( m.service, false ) service
				, COALESCE( m.common, false ) common
				, m.url_regex
				, m.main_url
				, m.css
				, m.description
				, m.library
				, m.forward_url
				, m.template_url
				, m.ngcontroller
				-- recursive tree info
				, COALESCE( (
					SELECT
							COUNT( 1 )
					FROM
							module m1
					WHERE
							COALESCE( m1.pid, 0 ) = m.id
				), 0 ) childs
				, ( nd.path || ARRAY[ COALESCE( m.id, 0 ) ] ) path
				, ( nd.depth + 1 ) depth
				, ( nd.orders || ARRAY[ COALESCE( m.mod_order, COALESCE( m.id, 0 ) ) ] ) orders

		FROM
				module AS m, nodes AS nd

		WHERE
				COALESCE( m.pid, 0 ) = nd.id

	)
	SELECT
			m.id
			, COALESCE( m.pid, 0 ) pid
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
			, m.childs
			, (
				SELECT
						m.id || array_agg( m1.id )
				FROM
						nodes m1
				WHERE
						m.id = ANY( m1.path )
				AND
						m.depth < m1.depth
			) childs_path
			, m.path
			, CAST( m.depth AS bigint ) depth
			, ( m.childs <= 0 ) leaf

	FROM
			nodes m

	ORDER BY
			m.orders;

$BODY$
	LANGUAGE sql VOLATILE
	COST 100
	ROWS 1000;
ALTER FUNCTION fn_iv_modules(text)
	OWNER TO postgres;
	COMMENT ON FUNCTION fn_iv_modules(text) IS 'select modules under tree format';

