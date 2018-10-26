SELECT	( COALESCE( (
				SELECT
						COUNT( 1 )
				FROM
						module m
				WHERE
						m.id != COALESCE( /* unique.id */123, 0 )
				AND
						m.code = COALESCE( /* unique.code */'abc', '' )
		), 0 ) >= 1 )

