CREATE TABLE vendor_rates_data
(
    id              int4         NOT NULL,
    create_date     varchar(255) NULL,
    is_locked       bool         NULL,
    modified_date   varchar(255) NULL,
    rates_data      text         NULL,
    vendor_agent_id varchar(255) NULL,
    vendor_name     varchar(255) NULL,
    CONSTRAINT uk_vendor_agent_id UNIQUE (vendor_agent_id),
    CONSTRAINT vendor_rates_data_pkey PRIMARY KEY (id)
);