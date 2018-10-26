SELECT
		u.*

FROM
		"user" u

LEFT JOIN
		user_online uo
ON
		uo.uid = u.id

WHERE
		uo.login_at is not null
AND
		uo.logout_at is null
AND
		uo.uid is not null
