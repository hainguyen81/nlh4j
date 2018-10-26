-- Table: "user"

DROP TABLE IF EXISTS "user" CASCADE;
CREATE TABLE "user"(
	id bigserial NOT NULL, -- USER identity
	cid bigint, -- USER COMPANY identity
	gid bigint, -- USER ROLES GROUP identity
	user_name character varying(50), -- Login user name
	password character varying NOT NULL, -- Login user MD5 password
	email character varying(255), -- Login user email
	enabled boolean, -- Specify this user whether can login
	sysadmin boolean, -- Specify this user whether is system administrator
	language character varying(10), -- USER language settings
	description text, -- USER description
	expired_at timestamp without time zone, -- USER expired date
	created_at timestamp without time zone NOT NULL DEFAULT now(),
	updated_at timestamp without time zone,
	created_user bigint NOT NULL,
	updated_user bigint,
	CONSTRAINT pk_user PRIMARY KEY (id), -- USER primary key
	CONSTRAINT fk_user_role_group FOREIGN KEY (gid) -- USER ROLES GROUP foreign key
		REFERENCES role_group (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_user_created_user FOREIGN KEY (created_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT fk_user_updated_user FOREIGN KEY (updated_user)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT uq_user_name UNIQUE (cid, user_name) -- UNIQUE USER NAME
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "user"
	OWNER TO postgres;
COMMENT ON TABLE "user"
	IS 'Contain system user information';
COMMENT ON COLUMN "user".id IS 'USER identity';
COMMENT ON COLUMN "user".user_name IS 'Login user name';
COMMENT ON COLUMN "user".password IS 'Login user MD5 password';
COMMENT ON COLUMN "user".email IS 'Login user email';
COMMENT ON COLUMN "user".enabled IS 'Specify this user whether can login';
COMMENT ON COLUMN "user".sysadmin IS 'Specify this user whether is system administrator';
COMMENT ON COLUMN "user".com_admin IS 'Specify this user whether is company administrator';
COMMENT ON COLUMN "user".description IS 'USER description';
COMMENT ON COLUMN "user".cid IS 'USER COMPANY identity';
COMMENT ON COLUMN "user".gid IS 'USER ROLES GROUP identity';
COMMENT ON COLUMN "user".language IS 'USER language settings';
COMMENT ON COLUMN "user".expired_at IS 'USER expired date';
COMMENT ON CONSTRAINT pk_user ON "user" IS 'USER primary key';
COMMENT ON CONSTRAINT fk_user_role_group ON "user" IS 'USER ROLES GROUP foreign key';
COMMENT ON CONSTRAINT uq_user_name ON "user" IS 'UNIQUE USER NAME';


-- Index: fki_user_created_user

DROP INDEX IF EXISTS fki_user_role_group CASCADE;
CREATE INDEX fki_user_role_group
	ON "user"
	USING btree
	(gid);

-- Index: fki_user_created_user

DROP INDEX IF EXISTS fki_user_created_user CASCADE;
CREATE INDEX fki_user_created_user
	ON "user"
	USING btree
	(created_user);

-- Index: fki_user_updated_user

DROP INDEX IF EXISTS fki_user_updated_user CASCADE;
CREATE INDEX fki_user_updated_user
	ON "user"
	USING btree
	(updated_user);

-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
-- User/Password: admin/admin
INSERT INTO "user"(
	id, user_name, password,
	email, enabled, sysadmin,
	language, description, created_user
) VALUES (
	1, 'admin', '21232f297a57a5a743894a0e4a801fc3',
	'admin@admin.com', true, true,
	'vi_VN', 'System Administrator', 1
);

