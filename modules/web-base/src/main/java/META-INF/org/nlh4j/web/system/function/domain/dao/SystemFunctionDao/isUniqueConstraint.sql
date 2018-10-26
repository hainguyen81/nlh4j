SELECT	( COALESCE( (
				SELECT
						COUNT( 1 )
				FROM
						"function" f
				WHERE
						f.id != COALESCE( /* unique.id */123, 0 )
				AND
						f.code = COALESCE( /* unique.code */'abc', '' )
		), 0 ) >= 1 )

