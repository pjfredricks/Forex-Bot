CREATE TABLE order_details (
	id int4 NOT NULL,
	country_code varchar(255) NULL,
	coupon_code varchar(255) NULL,
	create_date varchar(255) NULL,
	customer_id uuid NULL,
	discount_amount float8 NULL,
	forex_amount int4 NULL,
	forex_total float8 NULL,
	gst_amount float8 NULL,
	order_rate float8 NULL,
	order_type varchar(255) NULL,
	sales_total int4 NULL,
	status int4 NULL,
	tracking_id varchar(255) NULL,
	CONSTRAINT order_details_pkey PRIMARY KEY (id),
	CONSTRAINT uk_f82l3ksc1xirqn96hc6o0ixbx UNIQUE (tracking_id)
);