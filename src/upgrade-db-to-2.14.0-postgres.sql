/*
 * Create the quota_usage_events table
 */
CREATE SEQUENCE quota_usage_events_usage_local_id_seq;
CREATE TABLE quota_usage_events (
	usage_local_id INT8 default nextval('quota_usage_events_usage_local_id_seq'),  -- the unique usage local id (pk)
  object text NOT NULL,  -- it should always be usage
  quota_id INT,  -- the identifier of the quota
  instance_id TEXT NOT NULL,  -- storage - pid of object; portal - sid of portal document
  quantity FLOAT8 NOT NULL, -- the amount of the usage
  date_reported TIMESTAMP,  -- the time stamp that the quota usage was reported to the quota service
  status text, -- the status of the usage 
  usage_remote_id INT8, -- the usage id in the remote book keeper server
  node_id text, -- the id of the node which host the usage
  quota_subject text, -- the subject of the quota
  quota_type text, -- the type of the quota
  requestor text, -- the requestor of the qutao usage
   CONSTRAINT quota_usage_events_pk PRIMARY KEY (usage_local_id),
   CONSTRAINT quota_usage_events_uk1 UNIQUE (quota_id, instance_id, status),
   CONSTRAINT quota_usage_events_uk2 UNIQUE (quota_subject, quota_type, instance_id, status)
);
CREATE INDEX quota_usage_events_idx1 ON quota_usage_events (date_reported);
CREATE INDEX quota_usage_events_idx2 ON quota_usage_events (quota_id);
CREATE INDEX quota_usage_events_idx3 ON quota_usage_events (instance_id);
CREATE INDEX quota_usage_events_idx4 ON quota_usage_events (status);
CREATE INDEX quota_usage_events_idx5 ON quota_usage_events (usage_remote_id);
CREATE INDEX quota_usage_events_idx6 ON quota_usage_events (quota_subject);
CREATE INDEX quota_usage_events_idx7 ON quota_usage_events (requestor);
CREATE INDEX quota_usage_events_idx8 ON quota_usage_events (quota_type);

/*
 * Ensure xml_catalog sequence is at table max
 */
SELECT setval('xml_catalog_id_seq', (SELECT max(catalog_id) from xml_catalog));

/*
 * update the database version
 */
UPDATE db_version SET status=0;

INSERT INTO db_version (version, status, date_created)
  VALUES ('2.14.0', 1, CURRENT_DATE);
