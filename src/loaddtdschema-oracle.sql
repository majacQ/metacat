DELETE FROM xml_catalog 
      WHERE entry_type LIKE 'DTD'
        AND public_id LIKE '%@eml-version@%';
DELETE FROM xml_catalog 
      WHERE entry_type LIKE 'DTD'
        AND public_id LIKE '%@eml-beta4-version@%';
DELETE FROM xml_catalog 
      WHERE entry_type LIKE 'Schema'
        AND system_id LIKE '%eml%';
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-access-@eml-version@//EN',
         '/dtd/eml-access-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-attribute-@eml-version@//EN',
          '/dtd/eml-attribute-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-constraint-@eml-version@//EN',
          '/dtd/eml-constraint-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-coverage-@eml-version@//EN',
          '/dtd/eml-coverage-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-dataset-@eml-version@//EN',
          '/dtd/eml-dataset-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-entity-@eml-version@//EN',
          '/dtd/eml-entity-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-literature-@eml-version@//EN',
          '/dtd/eml-literature-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-physical-@eml-version@//EN',
          '/dtd/eml-physical-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-project-@eml-version@//EN',
          '/dtd/eml-project-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-protocol-@eml-version@//EN',
          '/dtd/eml-protocol-@eml-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-software-@eml-version@//EN',
          '/dtd/eml-software-@eml-version@.dtd');
-- include 2.0.0beta4
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-access-@eml-beta4-version@//EN',
         '/dtd/eml-access-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-attribute-@eml-beta4-version@//EN',
          '/dtd/eml-attribute-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-constraint-@eml-beta4-version@//EN',
          '/dtd/eml-constraint-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-coverage-@eml-beta4-version@//EN',
          '/dtd/eml-coverage-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-dataset-@eml-beta4-version@//EN',
          '/dtd/eml-dataset-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-entity-@eml-beta4-version@//EN',
          '/dtd/eml-entity-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-literature-@eml-beta4-version@//EN',
          '/dtd/eml-literature-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-physical-@eml-beta4-version@//EN',
          '/dtd/eml-physical-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-project-@eml-beta4-version@//EN',
          '/dtd/eml-project-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-protocol-@eml-beta4-version@//EN',
          '/dtd/eml-protocol-@eml-beta4-version@.dtd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('DTD', '-//ecoinformatics.org//eml-software-@eml-beta4-version@//EN',
          '/dtd/eml-software-@eml-beta4-version@.dtd');
--include schema
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@eml2_0_1namespace@', '/schema/eml-2.0.1/eml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@eml2_0_0namespace@', '/schema/eml-2.0.0/eml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@stmmlnamespace@', '/schema/eml-2.0.1/stmml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@stmml11namespace@', '/schema/eml-2.1.0/stmml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', 'metadata', '/schema/fgdc-std-001/fgdc-std-001-1998.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@eml2_1_0namespace@', '/schema/eml-2.1.0/eml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '@eml2_1_1namespace@', '/schema/eml-2.1.1/eml.xsd');
INSERT INTO xml_catalog (entry_type, public_id, system_id)
  VALUES ('Schema', '/schema/RegistryService/RegistryEntryType.xsd', '/schema/RegistryService/RegistryEntryType.xsd'); 
INSERT INTO db_version (version, status, date_created) 
  VALUES ('2.1.1',1,CURRENT_DATE);
