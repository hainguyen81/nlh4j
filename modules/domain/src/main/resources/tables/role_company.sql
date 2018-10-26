-- Table: role_company

DROP TABLE IF EXISTS role_company CASCADE;
CREATE TABLE role_company(
	id bigserial NOT NULL, -- ROLES GROUP COMPANY identity
	gid bigint NOT NULL, -- ROLES GROUP identity
	cid bigint NOT NULL, -- COMPANY identity
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	created_user bigint NOT NULL,
	updated_at timestamp without time zone,
	updated_user bigint,
	CONSTRAINT pk_role_company PRIMARY KEY (id),
	CONSTRAINT fk_role_company_company FOREIGN KEY (cid)
		REFERENCES company (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- ROLE COMPANY foreign key
	CONSTRAINT fk_role_company_role FOREIGN KEY (gid)
		REFERENCES role_group (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- ROLE COMPANY ROLE foreign key
	CONSTRAINT fk_role_company_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_role_company_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT uq_role_company UNIQUE (gid, cid) -- UNIQUE ROLE COMPANY
)
WITH (
	OIDS=FALSE
);
ALTER TABLE role_company
	OWNER TO postgres;
COMMENT ON TABLE role_company
	IS 'ROLES GROUP COMPANY';
COMMENT ON COLUMN role_company.id IS 'ROLES GROUP COMPANY identity';
COMMENT ON COLUMN role_company.gid IS 'ROLES GROUP identity';
COMMENT ON COLUMN role_company.cid IS 'COMPANY identity';
COMMENT ON CONSTRAINT fk_role_company_company ON role_company IS 'ROLE COMPANY foreign key';
COMMENT ON CONSTRAINT fk_role_company_role ON role_company IS 'ROLE COMPANY ROLE foreign key';
COMMENT ON CONSTRAINT uq_role_company ON role_company IS 'UNIQUE ROLE COMPANY';


-- Index: fki_role_company_company

DROP INDEX IF EXISTS fki_role_company_company CASCADE;
CREATE INDEX fki_role_company_company
	ON role_company
	USING btree
	(cid);

-- Index: fki_role_company_created_user

DROP INDEX IF EXISTS fki_role_company_created_user CASCADE;
CREATE INDEX fki_role_company_created_user
	ON role_company
	USING btree
	(created_user);

-- Index: fki_role_company_updated_user

DROP INDEX IF EXISTS fki_role_company_updated_user CASCADE;
CREATE INDEX fki_role_company_updated_user
	ON role_company
	USING btree
	(updated_user);

