-- Table: module

DROP TABLE IF EXISTS module CASCADE;
CREATE TABLE module(
	id bigserial NOT NULL, -- MODULE identity
	pid bigint, -- PARENT MODULE identity
	code character varying(50) NOT NULL, -- MODULE code
	name character varying(255) NOT NULL, -- MODULE name
	lang_key character varying(255), -- MODULE name key from language resource file
	mod_order integer, -- MODULE display order
	enabled boolean DEFAULT false, -- Specify this module whether is enabled
	visibled boolean DEFAULT false, -- Specify this module whether is visibled, only when this is not a serviced module
	service boolean DEFAULT false, -- Specify this module whether is a serviced module
	common boolean DEFAULT false, -- Specify this module whether is common module. This's mean force permissions for all users
	url_regex character varying(255), -- MODULE URL regular expression
	main_url character varying(255), -- MODULE URL
	css character varying(50), -- MODULE stylesheet
	description text, -- MODULE description
	library character varying(50), -- MODULE JAR library if necessary
	forward_url character varying(255), -- URL to forward
	template_url character varying(255), -- Client HTML template file
	ngcontroller character varying(50), -- Angular client controller name
	CONSTRAINT pk_module PRIMARY KEY (id), -- MODULE primary key
	CONSTRAINT fk_module_parent FOREIGN KEY (pid)
		REFERENCES module (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION, -- PARENT MODULE foreign key
	CONSTRAINT uq_module_code UNIQUE (code), -- UNIQUE module code
	CONSTRAINT uq_module_url UNIQUE (main_url) -- UNIQUE module URL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE module
	OWNER TO postgres;
COMMENT ON TABLE module
	IS 'Contain MODULE information';
COMMENT ON COLUMN module.id IS 'MODULE identity';
COMMENT ON COLUMN module.pid IS 'PARENT MODULE identity';
COMMENT ON COLUMN module.name IS 'MODULE name';
COMMENT ON COLUMN module.lang_key IS 'MODULE name key from language resource file';
COMMENT ON COLUMN module.mod_order IS 'MODULE display order';
COMMENT ON COLUMN module.enabled IS 'Specify this module whether is enabled';
COMMENT ON COLUMN module.visibled IS 'Specify this module whether is visibled, only when this is not a serviced module';
COMMENT ON COLUMN module.code IS 'MODULE code';
COMMENT ON COLUMN module.service IS 'Specify this module whether is a serviced module';
COMMENT ON COLUMN module.common IS 'Specify this module whether is common module. This''s mean force permissions for all users';
COMMENT ON COLUMN module.url_regex IS 'MODULE URL regular expression';
COMMENT ON COLUMN module.main_url IS 'MODULE URL';
COMMENT ON COLUMN module.css IS 'MODULE stylesheet';
COMMENT ON COLUMN module.description IS 'MODULE description';
COMMENT ON COLUMN module.library IS 'MODULE JAR library if necessary';
COMMENT ON COLUMN module.forward_url IS 'URL to forward';
COMMENT ON COLUMN module.template_url IS 'Client HTML template file';
COMMENT ON COLUMN module.ngcontroller IS 'Angular client controller name';
COMMENT ON CONSTRAINT pk_module ON module IS 'MODULE primary key';
COMMENT ON CONSTRAINT fk_module_parent ON module IS 'PARENT MODULE foreign key';
COMMENT ON CONSTRAINT uq_module_code ON module IS 'UNIQUE module code';
COMMENT ON CONSTRAINT uq_module_url ON module IS 'UNIQUE module URL';


-- Index: fki_module_parent

DROP INDEX IF EXISTS fki_module_parent CASCADE;
CREATE INDEX fki_module_parent
	ON module
	USING btree
	(pid);

-- Data for Name: module; Type: TABLE DATA; Schema: public; Owner: postgres
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	1, null, 'C00', 'Login', -1,
	true, false, true, true,
	'^(/login)', '/login', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	2, null, 'C01', 'Spring Security Check', -1,
	true, false, true, true,
	'^(/j_spring_security_check)', '/j_spring_security_check', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	3, null, 'C02', 'Spring Socket Endpoint', -1,
	true, false, true, true,
	'^(/socket)', '/socket', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	4, null, 'C03', 'Spring Socket Endpoint', -1,
	true, false, true, true,
	'^(/ws)', '/ws', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	5, null, 'C04', 'Spring Socket Endpoint - Chat', -1,
	true, false, true, true,
	'^(/ws/chat)', '/ws/chat', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	6, null, 'C05', 'Spring Socket Endpoint - Onlined Users', -1,
	true, false, true, true,
	'^(/ws/online)', '/ws/online', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	7, null, 'C06', 'Token Generation', -1,
	true, false, true, true,
	'^(/csrftoken)', '/csrftoken', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	8, null, 'C07', 'User Name Requirement', -1,
	true, false, true, true,
	'^(/username)', '/username', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	9, null, 'C08', 'Personal Home', -1,
	true, true, true, true,
	'^(/index|/home)', '/home', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	11, null, 'C10', 'Next Personal Tasks', -1,
	true, true, true, true,
	'^(/task/next)', '/task/next', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	10, null, 'C09', 'Today Personal Tasks', -1,
	true, true, true, true,
	'^(/task/today)', '/task/today', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	12, null, 'C11', 'Download Service', -1,
	true, false, true, true,
	'^(/download)', '/download', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	13, null, 'C12', 'Change Personal Password', -1,
	true, true, true, true,
	'^(/chgpwd)', '/chgpwd', 'fa fa-pencil-square-o'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	14, null, 'C13', 'Logout', -1,
	true, false, true, true,
	'^(/logout)', '/logout', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	20, null, 'C20', 'Dashboard', -1,
	true, true, true, true,
	'^(/dashboard)', '/dashboard', null
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	23, null, 'C21', 'Master', 1,
	true, true, false, false,
	'^(/dashboard/master|/master)', '/master', 'fa fa-hashtag'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	26, null, 'C22', 'System', 0,
	true, true, false, false,
	'^(/dashboard/system|/system)', '/system', 'fa fa-gears'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	40, 26, 'S01', 'System Role', 0,
	true, true, false, false,
	'^(/system/role|/role)', '/system/role', 'fa fa-universal-access'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	41, 26, 'S02', 'System User', 1,
	true, true, false, false,
	'^(/system/user|/user)', '/system/user', 'fa fa-users'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	42, 26, 'S03', 'System Company', 2,
	true, true, false, false,
	'^(/system/company|/company)', '/system/company', 'fa fa-building-o'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	43, 26, 'S04', 'System Module', 3,
	true, true, false, false,
	'^(/system/module|/module)', '/system/module', 'fa fa-braille'
);
INSERT INTO module(
	id, pid, code, name, mod_order,
	enabled, visibled, service, common,
	url_regex, main_url, css
) VALUES (
	60, 23, 'M01', 'Master Company', 0,
	true, true, false, false,
	'^(/master/company)', '/master/company', 'fa fa-building-o'
);

