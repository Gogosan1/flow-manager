CREATE TABLE IF NOT EXISTS flow_manager.file_metadata (
    id UUID PRIMARY KEY,
    original_file_path VARCHAR(100) NOT NULL,
    converted_file_path VARCHAR(100),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    processed_at TIMESTAMPTZ
);