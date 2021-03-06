CREATE TABLE back_office_user_data
(
    id              int4         NOT NULL,
    address_id      varchar(255) NULL,
    create_date     varchar(255) NULL,
    created_by      varchar(255) NULL,
    email_id        varchar(255) NULL,
    hex_data        varchar(255) NULL,
    is_active       bool         NULL,
    mobile_num      varchar(255) NULL,
    modified_by     varchar(255) NULL,
    modified_date   varchar(255) NULL,
    "password"      varchar(255) NULL,
    user_category   varchar(255) NULL,
    user_id         varchar(255) NULL,
    user_name       varchar(255) NULL,
    vendor_agent_id varchar(255) NULL,
    deleted_by      varchar(255) NULL DEFAULT NULL::character varying,
    CONSTRAINT back_office_user_data_pkey PRIMARY KEY (id),
    CONSTRAINT uk_address_id UNIQUE (address_id),
    CONSTRAINT uk_user_id UNIQUE (user_id),
    CONSTRAINT uk_vendor_agent_id UNIQUE (vendor_agent_id)
);