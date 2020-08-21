CREATE TABLE address
(
	id              int4 NOT NULL,
	address_id      varchar(255) NULL,
	address_line    varchar(255) NULL,
	city            varchar(255) NULL,
	pincode         varchar(255) NULL,
	state           varchar(255) NULL,
	CONSTRAINT address_pkey PRIMARY KEY (id)
);