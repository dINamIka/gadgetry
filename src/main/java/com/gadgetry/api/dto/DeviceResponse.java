package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Device response")
public record DeviceResponse(
        @Schema(
                        description = "Device unique identifier",
                        example = "123e4567-e89b-12d3-a456-426614174000")
                UUID id,
        @Schema(description = "Device display name", example = "iPhone 15 Pro") String displayName,
        @Schema(description = "Device brand", example = "Apple") String displayBrand,
        @Schema(description = "Device state", example = "AVAILABLE") DeviceState state,
        @Schema(description = "Creation timestamp", example = "2025-11-03330:15:30Z")
                Instant createdAt,
        @Schema(description = "Last update timestamp", example = "2025-11-03T10:15:30Z")
                Instant updatedAt,
        @Schema(description = "Resource version for optimistic locking", example = "1")
                Long version) {}
