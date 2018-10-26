UPDATE
		"user"
SET
		language = /* language */'abc'
		, updated_at = CURRENT_TIMESTAMP
		, updated_user = /* updater */123
WHERE
		id = /* uid */123