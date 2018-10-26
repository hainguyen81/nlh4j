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

			/*%if @isNotEmpty(requestURI) */
			AND
					-- check by regular expression
					/* requestURI */'abc' ~ m.url_regex
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

			OFFSET 0 LIMIT 1
), false )
