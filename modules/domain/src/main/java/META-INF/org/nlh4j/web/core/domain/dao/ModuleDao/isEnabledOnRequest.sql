SELECT COALESCE( (
			SELECT
					true
			FROM
					fn_iv_modules( null ) m
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
			/*%if @isNotEmpty(requestURI) */
			AND
					-- check by regular expression
					/* requestURI */'abc' ~ m.url_regex
			/*%end*/

			OFFSET 0 LIMIT 1
), false )