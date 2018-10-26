-- Table: module_company

DROP TABLE IF EXISTS module_company CASCADE;
CREATE TABLE module_company(
	id bigserial NOT NULL, -- MODULE COMPANY identity
	mid bigint, -- MODULE identity
	cid bigint, -- COMPANY identity
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	created_user bigint NOT NULL,
	updated_at timestamp without time zone,
	updated_user bigint,
	CONSTRAINT pk_module_company PRIMARY KEY (id), -- MODULE COMPANY primary key
	CONSTRAINT fk_module_company_company FOREIGN KEY (cid)
		REFERENCES company (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- COMPANY foreign key
	CONSTRAINT fk_module_company_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_module_company_module FOREIGN KEY (mid)
		REFERENCES module (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- MODULE foreign key
	CONSTRAINT fk_module_company_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT uq_module_company UNIQUE (mid, cid) -- UNIQUE MODULE COMPANY
)
WITH (
  OIDS=FALSE
);
ALTER TABLE module_company
	OWNER TO postgres;
COMMENT ON TABLE module_company
	IS 'MODULE COMPANY';
COMMENT ON COLUMN module_company.id IS 'MODULE COMPANY identity';
COMMENT ON COLUMN module_company.mid IS 'MODULE identity';
COMMENT ON COLUMN module_company.cid IS 'COMPANY identity';
COMMENT ON CONSTRAINT pk_module_company ON module_company IS 'MODULE COMPANY primary key';
COMMENT ON CONSTRAINT fk_module_company_company ON module_company IS 'COMPANY foreign key';
COMMENT ON CONSTRAINT fk_module_company_module ON module_company IS 'MODULE foreign key';
COMMENT ON CONSTRAINT uq_module_company ON module_company IS 'UNIQUE MODULE COMPANY';


-- Index: fki_module_company_company

DROP INDEX IF EXISTS fki_module_company_company CASCADE;
CREATE INDEX fki_module_company_company
	ON module_company
	USING btree
	(cid);

-- Index: fki_module_company_created_user

DROP INDEX IF EXISTS fki_module_company_created_user;
CREATE INDEX fki_module_company_created_user
	ON module_company
	USING btree
	(created_user);

-- Index: fki_module_company_updated_user

DROP INDEX IF EXISTS fki_module_company_updated_user;
CREATE INDEX fki_module_company_updated_user
	ON module_company
	USING btree
	(updated_user);

