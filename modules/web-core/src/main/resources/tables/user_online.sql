-- Table: user_online

DROP TABLE IF EXISTS user_online CASCADE;
CREATE TABLE user_online(
	id bigserial NOT NULL, -- USER ONLINE identity
	uid bigint NOT NULL, -- USER identity
	login_at timestamp without time zone NOT NULL DEFAULT now(), -- USER login date
	logout_at timestamp without time zone, -- USER logout date
	CONSTRAINT pk_user_online PRIMARY KEY (id), -- USER ONLINE primary key
	CONSTRAINT fk_user_online_user FOREIGN KEY (uid)
		REFERENCES "user" (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- USER ONLINE USER foreign key
	CONSTRAINT uq_user_online UNIQUE (uid, login_at) -- UNIQUE USER ONLINE login date
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_online
	OWNER TO postgres;
COMMENT ON TABLE user_online
	IS 'Contain USER ONLINE information';
COMMENT ON COLUMN user_online.id IS 'USER ONLINE identity';
COMMENT ON COLUMN user_online.login_at IS 'USER login date';
COMMENT ON COLUMN user_online.logout_at IS 'USER logout date';
COMMENT ON COLUMN user_online.uid IS 'USER identity';
COMMENT ON CONSTRAINT pk_user_online ON user_online IS 'USER ONLINE primary key';
COMMENT ON CONSTRAINT fk_user_online_user ON user_online IS 'USER ONLINE USER foreign key';
COMMENT ON CONSTRAINT uq_user_online ON user_online IS 'UNIQUE USER ONLINE login date';


-- Index: fki_user_created_user

DROP INDEX IF EXISTS fki_user_online_user CASCADE;
CREATE INDEX fki_user_online_user
	ON user_online
	USING btree
	(uid);

