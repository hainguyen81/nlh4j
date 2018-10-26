-- Table: role

DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role(
	id bigserial NOT NULL, -- Role identity
	mid bigint NOT NULL, -- MODULE identity
	gid bigint NOT NULL, -- ROLES GROUP identity
	functions text, -- Functions permission
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	updated_at timestamp without time zone,
	created_user bigint NOT NULL,
	updated_user bigint,
	CONSTRAINT pk_role PRIMARY KEY (id), -- MODULE ROLE primary key
	CONSTRAINT fk_role_module FOREIGN KEY (mid)
		REFERENCES module (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- MODULE ROLE foreign key
	CONSTRAINT fk_role_role_group FOREIGN KEY (gid)
		REFERENCES role_group (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- MODULE ROLE GROUP foreign key
	CONSTRAINT fk_role_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_role_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE role
	OWNER TO postgres;
COMMENT ON TABLE role
	IS 'Contain MODULE ROLE PERMISSIONS information';
COMMENT ON COLUMN role.id IS 'Role identity';
COMMENT ON COLUMN role.mid IS 'MODULE identity';
COMMENT ON COLUMN role.gid IS 'ROLES GROUP identity';
COMMENT ON COLUMN role.functions IS 'Functions permission';
COMMENT ON CONSTRAINT pk_role ON role IS 'MODULE ROLE primary key';
COMMENT ON CONSTRAINT fk_role_module ON role IS 'MODULE ROLE foreign key';
COMMENT ON CONSTRAINT fk_role_role_group ON role IS 'MODULE ROLE GROUP foreign key';


-- Index: fki_role_created_user

DROP INDEX IF EXISTS fki_role_created_user CASCADE;
CREATE INDEX fki_role_created_user
	ON role
	USING btree
	(created_user);

-- Index: fki_role_module

DROP INDEX IF EXISTS fki_role_module CASCADE;
CREATE INDEX fki_role_module
	ON role
	USING btree
	(mid);

-- Index: fki_role_role_group

DROP INDEX IF EXISTS fki_role_role_group CASCADE;
CREATE INDEX fki_role_role_group
	ON role
	USING btree
	(gid);

-- Index: fki_role_updated_user

DROP INDEX IF EXISTS fki_role_updated_user CASCADE;
CREATE INDEX fki_role_updated_user
	ON role
	USING btree
	(updated_user);

