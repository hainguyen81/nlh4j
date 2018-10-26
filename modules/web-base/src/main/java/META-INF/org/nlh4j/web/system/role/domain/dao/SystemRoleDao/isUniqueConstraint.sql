SELECT	( COALESCE( (
				SELECT
						COUNT( 1 )

				FROM
						role_group rg

				WHERE
						rg.id != COALESCE( /* unique.id */123, 0 )
				AND
						rg.code = COALESCE( /* unique.code */'abc', '' )
		), 0 ) >= 1 )
