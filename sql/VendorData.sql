CREATE TABLE vendor_data (
	id int4 NOT NULL,
	gst_number varchar(255) NULL,
	pan_number varchar(255) NULL,
	rbiagent_id varchar(255) NULL,
	vendor_agent_id varchar(255) NULL,
	CONSTRAINT uk_vendor_agent_id UNIQUE (vendor_agent_id),
	CONSTRAINT uk_rbiagent_id UNIQUE (rbiagent_id),
	CONSTRAINT uk_gst_number UNIQUE (gst_number),
	CONSTRAINT uk_pan_number UNIQUE (pan_number),
	CONSTRAINT vendor_data_pkey PRIMARY KEY (id)
);