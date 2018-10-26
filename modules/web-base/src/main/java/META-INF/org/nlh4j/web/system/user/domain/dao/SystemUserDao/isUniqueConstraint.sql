SELECT	( COALESCE( (
				SELECT
						COUNT( 1 )
				FROM
						"user" u
				WHERE
						u.id != COALESCE( /* unique.id */123, 0 )
				AND
						u.user_name = COALESCE( /* unique.username */'abc', '' )
		), 0 ) >= 1 )

