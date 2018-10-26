select
	m.css
from
	module m
where
	m.enabled = true

/*%if (moduleCds != null && moduleCds.size() > 0) */
and
	m.code in (''
		/*%for moduleCd: moduleCds */
				/*# "," */
				/* moduleCd */'abc'
		/*%end */
	)
/*%end*/
/*%if (moduleCds == null || moduleCds.size() <= 0) */
and
	m.code = ''
/*%end*/
