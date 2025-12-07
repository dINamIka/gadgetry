CREATE TABLE devices (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    display_name        VARCHAR(255) NOT NULL,
    display_brand       VARCHAR(100) NOT NULL,
    name                VARCHAR(255) NOT NULL,
    brand               VARCHAR(100) NOT NULL,
    state               VARCHAR(20) NOT NULL,
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    deleted_at          TIMESTAMP,
    version             BIGINT NOT NULL DEFAULT 0,
    
    CONSTRAINT chk_state CHECK (state IN ('AVAILABLE', 'IN_USE', 'INACTIVE'))
);

CREATE INDEX idx_devices_name ON devices(name);
CREATE INDEX idx_devices_brand ON devices(brand);
CREATE INDEX idx_devices_state ON devices(state);
CREATE INDEX idx_devices_created_at ON devices(created_at DESC);
CREATE INDEX idx_devices_deleted_at ON devices(deleted_at) WHERE deleted_at IS NULL;
