CREATE TABLE temp_rates_data
(
    id              int4         NOT NULL,
    create_date     varchar(255) NULL,
    is_approved     bool         NULL,
    modified_date   varchar(255) NULL,
    rates_data      text         NULL,
    vendor_agent_id varchar(255) NULL,
    CONSTRAINT temp_rates_data_pkey PRIMARY KEY (id)
);
