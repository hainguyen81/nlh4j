-- Function: fn_iv_is_valid_user(text)

DROP FUNCTION IF EXISTS fn_iv_is_valid_user(text) CASCADE;
CREATE OR REPLACE FUNCTION fn_iv_is_valid_user(IN ucd text)
RETURNS boolean AS
$BODY$

	SELECT ( COALESCE( (
					SELECT
							COUNT( 1 )
					FROM
							"user" u
					WHERE
							u.user_name = $1
					AND
							u.enabled = true
					AND		(
									u.expired_at IS NULL
								OR	DATE_TRUNC( 'second', u.expired_at ) > DATE_TRUNC( 'second', CURRENT_TIMESTAMP )
					)
			), 0 ) > 0 );

$BODY$
	LANGUAGE sql VOLATILE;
ALTER FUNCTION fn_iv_is_valid_user(text)
	OWNER TO postgres;
	COMMENT ON FUNCTION fn_iv_is_valid_user(text) IS 'detect the specified user name whether is valid user';

