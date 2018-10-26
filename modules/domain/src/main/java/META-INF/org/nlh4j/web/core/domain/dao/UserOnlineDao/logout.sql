UPDATE
	user_online
SET
	logout_at = CURRENT_TIMESTAMP

WHERE
	uid in (0
/*%if uids != null */
		/*%for uid: uids */
			/*# "," */
			/* uid */123
		/*%end */
/*%end*/
	)
AND
	login_at is not null
AND
	logout_at is null