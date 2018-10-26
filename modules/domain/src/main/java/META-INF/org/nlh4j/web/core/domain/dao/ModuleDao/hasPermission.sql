SELECT COALESCE( (
			SELECT
					true

			FROM
					fn_iv_modules_by_user( null, /* userName */'abc' ) m

			WHERE
					true

			/*%if (moduleCds != null && moduleCds.size() > 0) */
			AND
					m.code in (''
						/*%for moduleCd: moduleCds */
							/*# "," */
							/* moduleCd */'abc'
			  			/*%end */
					)
			/*%end*/
			/*%if (moduleCds == null || moduleCds.size() <= 0) */
			AND
					m.code = ''
			/*%end*/

			/*%if (functions != null && functions.size() > 0) */
			AND		(
						false
						/*%for function: functions */
							OR
								/* function */'abc' = ANY( string_to_array( m.functions, '|' ) )
						/*%end */
			)
			/*%end*/
			/*%if (functions == null || functions.size() <= 0) */
			-- FIXME if role has not specified function, it is default is viewable
			-- AND
			--		false
			/*%end*/

			OFFSET 0 LIMIT 1
), false )


