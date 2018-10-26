SELECT	( COALESCE( (
				SELECT
						COUNT( 1 )
				FROM
						module m
				WHERE
						m.id != COALESCE( /* id */123, 0 )
				AND		(
							m.code = COALESCE( /* code */'abc', '' )
						OR	m.main_url = COALESCE( /* url */'abc', '' )
				)
		), 0 ) >= 1 )
