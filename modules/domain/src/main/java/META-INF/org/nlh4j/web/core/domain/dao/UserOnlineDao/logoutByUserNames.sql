UPDATE
	user_online uo
SET
	logout_at = CURRENT_TIMESTAMP

FROM
	"user" u
WHERE
	u.id = uo.uid
AND
	u.user_name in (''
/*%if userNames != null */
		/*%for userName: userNames */
			/*# "," */
			/* userName */'abc'
		/*%end */
/*%end*/
	)
AND
	login_at is not null
AND
	logout_at is null