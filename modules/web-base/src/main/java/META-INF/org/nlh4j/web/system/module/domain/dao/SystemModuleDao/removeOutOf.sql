UPDATE
		module
SET
		pid = NULL
WHERE
		pid = (
				SELECT
						id
				FROM
						module
				WHERE
						code = /* code */'abc'
		)