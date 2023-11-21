-- Table: company

DROP TABLE IF EXISTS company CASCADE;
CREATE TABLE company(
	id bigserial NOT NULL,
	code character varying(50) NOT NULL, -- COMPANY code
	name character varying(255) NOT NULL, -- COMPANY name
	address1 text, -- COMPANY address 1
	address2 text, -- COMPANY address 2
	post_code character varying(10), -- COMPANY post code
	tel1 character varying(15), -- COMPANY tel 1
	tel2 character varying(15), -- COMPANY tel 2
	fax1 character varying(15), -- COMPANY fax 1
	fax2 character varying(15), -- COMPANY fax 2
	url character varying(255), -- COMPANY site URL
	tax character varying(15), -- COMPANY tax number
	pos_x1 double precision DEFAULT (-1), -- COMPANY map position latitude
	pos_y1 double precision DEFAULT (-1), -- COMPANY map position longitude
	pos_x2 double precision DEFAULT (-1), -- COMPANY map position latitude
	pos_y2 double precision DEFAULT (-1), -- COMPANY map position longitude
	vat double precision DEFAULT 10, -- COMPANY VAT percent
	description text, -- COMPANY description
	enabled boolean DEFAULT true, -- Specify this company whether is enabled
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	updated_at timestamp without time zone,
	created_user bigserial NOT NULL,
	updated_user bigint,
	CONSTRAINT pk_company PRIMARY KEY (id), -- COMPANY primary key
	CONSTRAINT fk_company_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_company_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT uq_company_code UNIQUE (code) -- UNIQUE COMPANY code
)
WITH (
  OIDS=FALSE
);
ALTER TABLE company
	OWNER TO postgres;
COMMENT ON TABLE company
	IS 'Contain COMPANY information';
COMMENT ON COLUMN company.code IS 'COMPANY code';
COMMENT ON COLUMN company.name IS 'COMPANY name';
COMMENT ON COLUMN company.address1 IS 'COMPANY address 1';
COMMENT ON COLUMN company.address2 IS 'COMPANY address 2';
COMMENT ON COLUMN company.tel1 IS 'COMPANY tel 1';
COMMENT ON COLUMN company.fax1 IS 'COMPANY fax 1';
COMMENT ON COLUMN company.url IS 'COMPANY site URL';
COMMENT ON COLUMN company.tax IS 'COMPANY tax number';
COMMENT ON COLUMN company.pos_x1 IS 'COMPANY map position latitude';
COMMENT ON COLUMN company.pos_y1 IS 'COMPANY map position longitude';
COMMENT ON COLUMN company.pos_x2 IS 'COMPANY map position latitude';
COMMENT ON COLUMN company.pos_y2 IS 'COMPANY map position longitude';
COMMENT ON COLUMN company.vat IS 'COMPANY VAT percent';
COMMENT ON COLUMN company.enabled IS 'Specify this company whether is enabled';
COMMENT ON COLUMN company.description IS 'COMPANY description';
COMMENT ON COLUMN company.tel2 IS 'COMPANY tel 2';
COMMENT ON COLUMN company.fax2 IS 'COMPANY fax 2';
COMMENT ON COLUMN company.post_code IS 'COMPANY post code';
COMMENT ON CONSTRAINT pk_company ON company IS 'COMPANY primary key';
COMMENT ON CONSTRAINT uq_company_code ON company IS 'UNIQUE COMPANY code';


-- Index: fki_company_created_user

DROP INDEX IF EXISTS fki_company_created_user CASCADE;
CREATE INDEX fki_company_created_user
	ON company
	USING btree
	(created_user);

-- Index: fki_company_updated_user

DROP INDEX IF EXISTS fki_company_updated_user CASCADE;
CREATE INDEX fki_company_updated_user
	ON company
	USING btree
	(updated_user);

-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: postgres
INSERT INTO company(
	id, code, name,
	address1,
	enabled,
	created_at, created_user
) VALUES (
	1, 'NLH4J', 'NLH4J',
	'364 Cộng Hòa, P.13, Q.TB, TP.HCM',
	TRUE,
	1, CURRENT_TIMESTAMP
);

