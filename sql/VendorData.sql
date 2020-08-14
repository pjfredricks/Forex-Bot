CREATE TABLE vendor_data (
	id int4 NOT NULL,
	gst_number varchar(255) NULL,
	pan_number varchar(255) NULL,
	rbiagent_id varchar(255) NULL,
	vendor_agent_id varchar(255) NULL,
	CONSTRAINT uk_a8t51d1u7gfp8cuq157og0rxr UNIQUE (vendor_agent_id),
	CONSTRAINT uk_bblvw0359875nohmwbbswfbqc UNIQUE (rbiagent_id),
	CONSTRAINT uk_jp8sr3dndf9rsm5knklhcoxhb UNIQUE (gst_number),
	CONSTRAINT uk_rketfegiwcptsmgorpcr36hna UNIQUE (pan_number),
	CONSTRAINT vendor_data_pkey PRIMARY KEY (id)
);