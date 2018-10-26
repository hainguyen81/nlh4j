-- Function: fn_iv_distinct_array(ANYARRAY)

DROP FUNCTION IF EXISTS fn_iv_distinct_array(ANYARRAY) CASCADE;
CREATE OR REPLACE FUNCTION fn_iv_distinct_array(ANYARRAY)
	RETURNS ANYARRAY
AS $BODY$
DECLARE
	n integer;
BEGIN

	-- number of elements in the array
	n = REPLACE( SPLIT_PART( ARRAY_DIMS( $1 ), ':', 2 ), ']', '' )::int;
	IF n > 1 THEN
		-- test if the last item belongs to the rest of the array
		IF ( ($1)[1:n-1] @> ($1)[n:n] ) THEN
			-- returns the result of the same function on the rest of the array
			RETURN fn_iv_distinct_array( $1[1:n-1] );
		ELSE
			-- returns the result of the same function on the rest of the array plus the last element
			RETURN fn_iv_distinct_array($1[1:n-1]) || $1[n:n];
		END IF;
	ELSE
		-- if array has only one item, returns the array
		RETURN $1;
	END IF;

END;
$BODY$
	LANGUAGE plpgsql VOLATILE
	COST 100;
ALTER FUNCTION fn_iv_distinct_array(ANYARRAY)
	OWNER TO postgres;
	COMMENT ON FUNCTION fn_iv_distinct_array(ANYARRAY) IS 'select distinct array (not sorted after distinct)';