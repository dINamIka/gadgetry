package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;
import java.time.Instant;
import java.util.UUID;

public record DeviceResponse(
        UUID id,
        String displayName,
        String displayBrand,
        DeviceState state,
        Instant createdAt,
        Instant updatedAt,
        Long version) {}
