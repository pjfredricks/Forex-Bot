CREATE TABLE otp_data
(
    id                  int4         NOT NULL,
    create_date         varchar(255) NULL,
    mobile_num          varchar(255) NULL,
    modified_date       varchar(255) NULL,
    otp                 varchar(255) NULL,
    otp_type            varchar(255) NULL,
    otp_verified        bool         NULL DEFAULT false,
    retry_count         int4         NULL,
    text_local_response text         NULL DEFAULT 'empty'::text,
    CONSTRAINT otp_data_pkey PRIMARY KEY (id),
    CONSTRAINT uk_mobile_num UNIQUE (mobile_num)
);