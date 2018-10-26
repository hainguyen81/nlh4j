SELECT COALESCE( (
			SELECT
					true
			FROM
					module m
			WHERE
					m.enabled = true

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

			OFFSET 0 LIMIT 1
), false )