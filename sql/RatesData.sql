CREATE TABLE rates_data
(
    id            int4         NOT NULL,
    approved_by   varchar(255) NULL,
    create_date   varchar(255) NULL,
    rates_details text         NULL,
    CONSTRAINT rates_data_pkey PRIMARY KEY (id)
);