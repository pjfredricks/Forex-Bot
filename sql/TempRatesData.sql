CREATE TABLE temp_rates_data (
	id int4 NOT NULL,
	approved_by varchar(255) NULL,
	buy_rate float8 NULL,
	country_code varchar(255) NULL,
	country_name varchar(255) NULL,
	create_date varchar(255) NULL,
	currency_name varchar(255) NULL,
	sell_rate float8 NULL,
	CONSTRAINT temp_rates_data_pkey PRIMARY KEY (id)
);
