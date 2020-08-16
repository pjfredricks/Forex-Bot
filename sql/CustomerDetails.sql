CREATE TABLE customer_details (
	id int4 NOT NULL,
	create_date varchar(255) NULL,
	customer_id uuid NULL,
	email_id varchar(255) NULL,
	hex_data varchar(255) NULL,
	is_email_verified bool NULL DEFAULT false,
	is_mobile_verified bool NULL DEFAULT false,
	mobile_num varchar(255) NULL,
	modified_by varchar(255) NULL,
	modified_date varchar(255) NULL,
	"name" varchar(255) NULL,
	"password" varchar(255) NULL,
	is_active bool NULL DEFAULT true,
	deleted_by varchar(255) NULL DEFAULT NULL::character varying,
	CONSTRAINT customer_details_pkey PRIMARY KEY (id),
	CONSTRAINT uk_customer_id UNIQUE (customer_id)
);