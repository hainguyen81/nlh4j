-- Table: functions

DROP TABLE IF EXISTS functions CASCADE;
CREATE TABLE functions(
	id bigserial NOT NULL, -- Function identity
	code character varying(50) NOT NULL, -- Function code
	name character varying(255) NOT NULL, -- Function name
	lang_key character varying(255), -- Function language key
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	updated_at timestamp without time zone,
	created_user bigint NOT NULL,
	updated_user bigint,
	CONSTRAINT pk_functions PRIMARY KEY (id), -- FUNCTION primary key
	CONSTRAINT fk_functions_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_functions_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE functions
	OWNER TO postgres;
COMMENT ON TABLE functions
	IS 'Contain FUNCTIONS PERMISSIONS information';
COMMENT ON COLUMN functions.id IS 'Function identity';
COMMENT ON COLUMN functions.code IS 'Function code';
COMMENT ON COLUMN functions.name IS 'Function name';
COMMENT ON COLUMN functions.lang_key IS 'Function language key';
COMMENT ON CONSTRAINT pk_functions ON functions IS 'FUNCTION primary key';


-- Index: fki_functions_created_user

DROP INDEX IF EXISTS fki_functions_created_user CASCADE;
CREATE INDEX fki_functions_created_user
	ON functions
	USING btree
	(created_user);

-- Index: fki_functions_updated_user

DROP INDEX IF EXISTS fki_functions_updated_user CASCADE;
CREATE INDEX fki_functions_updated_user
	ON functions
	USING btree
	(updated_user);

