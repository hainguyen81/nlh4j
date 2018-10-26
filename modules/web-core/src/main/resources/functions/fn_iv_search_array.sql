-- Function: fn_iv_search_array(ANYARRAY, ANYELEMENT)

DROP FUNCTION IF EXISTS fn_iv_search_array(ANYARRAY, ANYELEMENT) CASCADE;
CREATE OR REPLACE FUNCTION fn_iv_search_array(ANYARRAY, ANYELEMENT)
	RETURNS BIGINT
AS $BODY$
BEGIN

	RETURN COALESCE( (
				SELECT
						i
				FROM
						generate_subscripts($1, 0) AS i
				WHERE
						$2[i] = $1
				ORDER BY
						i
			), -1 );

END;
$BODY$
	LANGUAGE plpgsql VOLATILE
	COST 100;
ALTER FUNCTION fn_iv_search_array(ANYARRAY, ANYELEMENT)
	OWNER TO postgres;
	COMMENT ON FUNCTION fn_iv_search_array(ANYARRAY, ANYELEMENT) IS 'search element from array';