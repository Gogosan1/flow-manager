--liquibase formatted sql

--changeset flow-manager:003-debezium-connect-grant splitStatements:false
DO $$
BEGIN
    EXECUTE format(
        'GRANT CONNECT ON DATABASE %I TO debezium',
        current_database()
    );
END
$$;

--changeset flow-manager:004-debezium-outbox-grants
GRANT USAGE ON SCHEMA flow_manager TO debezium;
GRANT SELECT ON TABLE flow_manager.outbox TO debezium;

--changeset flow-manager:005-debezium-publication splitStatements:false
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_publication
        WHERE pubname = 'flow_manager_publication'
    ) THEN
        EXECUTE
            'CREATE PUBLICATION flow_manager_publication
             FOR TABLE flow_manager.outbox';

    ELSIF NOT EXISTS (
        SELECT 1
        FROM pg_publication_tables
        WHERE pubname = 'flow_manager_publication'
          AND schemaname = 'flow_manager'
          AND tablename = 'outbox'
    ) THEN
        EXECUTE
            'ALTER PUBLICATION flow_manager_publication
             ADD TABLE flow_manager.outbox';
    END IF;
END
$$;
