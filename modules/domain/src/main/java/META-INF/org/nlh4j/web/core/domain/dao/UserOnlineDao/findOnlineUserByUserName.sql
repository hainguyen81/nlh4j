SELECT
		uo.*

FROM
		user_online uo

LEFT JOIN
		"user" u
ON
		u.id = uo.uid

WHERE
		u.user_name = /* userName */'abc' escape '$'
AND
		uo.login_at is not null
AND
		uo.logout_at is null

OFFSET 0 LIMIT 1