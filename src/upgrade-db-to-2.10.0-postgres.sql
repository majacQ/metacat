/*
 * Ensure xml_catalog sequence is at table max
 */
SELECT setval('xml_catalog_id_seq', (SELECT max(catalog_id) from xml_catalog));

INSERT INTO xml_catalog (entry_type, public_id, system_id) SELECT 'Schema', 'http://www.openarchives.org/OAI/2.0/oai_dc/', '/schema/oai_dc/oai_dc.xsd'  WHERE NOT EXISTS (SELECT * FROM xml_catalog WHERE public_id='http://www.openarchives.org/OAI/2.0/oai_dc/');

CREATE INDEX systemMetadata_series_id on systemMetadata(series_id);

/*
 * update the database version
 */
UPDATE db_version SET status=0;

INSERT INTO db_version (version, status, date_created) 
  VALUES ('2.10.0', 1, CURRENT_DATE);
