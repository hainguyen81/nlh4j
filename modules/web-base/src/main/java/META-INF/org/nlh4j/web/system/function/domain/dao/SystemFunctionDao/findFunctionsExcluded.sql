SELECT
		f.id
		, f.code
		, f.name
		, f.lang_key

FROM
		"function" f
WHERE
		true
/*%if codes != null && codes.size() > 0*/
AND
	   	f.code NOT IN (''
	  		/*%for code: codes */
				/*# "," */
	    		/* code */'abc'
	  		/*%end */
		)
/*%end*/

