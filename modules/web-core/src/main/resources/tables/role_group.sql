-- Table: role_group

DROP TABLE IF EXISTS role_group CASCADE;
CREATE TABLE role_group(
	id bigserial NOT NULL, -- Roles group identity
	code character varying(50) NOT NULL, -- Roles group code
	name character varying(255) NOT NULL, -- Roles group name
	description text, -- Roles group description
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	updated_at timestamp without time zone,
	created_user bigint NOT NULL,
	updated_user bigint,
	CONSTRAINT pk_role_group PRIMARY KEY (id), -- Roles group primary key
	CONSTRAINT fk_role_group_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_role_group_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT uq_role_group_code UNIQUE (code) -- UNIQUE roles group code
)
WITH (
  OIDS=FALSE
);
ALTER TABLE role_group
	OWNER TO postgres;
COMMENT ON TABLE role_group
	IS 'Contain MODULE ROLES GROUP information';
COMMENT ON COLUMN role_group.id IS 'Roles group identity';
COMMENT ON COLUMN role_group.code IS 'Roles group code';
COMMENT ON COLUMN role_group.name IS 'Roles group name';
COMMENT ON COLUMN role_group.description IS 'Roles group description';
COMMENT ON CONSTRAINT pk_role_group ON role_group IS 'Roles group primary key';
COMMENT ON CONSTRAINT uq_role_group_code ON role_group IS 'UNIQUE roles group code';


-- Index: fki_role_group_created_user

DROP INDEX IF EXISTS fki_role_group_created_user CASCADE;
CREATE INDEX fki_role_group_created_user
	ON role_group
	USING btree
	(created_user);

-- Index: fki_role_group_updated_user

DROP INDEX IF EXISTS fki_role_group_updated_user CASCADE;
CREATE INDEX fki_role_group_updated_user
	ON role_group
	USING btree
	(updated_user);

