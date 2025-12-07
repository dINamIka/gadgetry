package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing device (PATCH - all fields optional)")
public record DeviceUpdateRequest(
        @Schema(description = "Device display name", example = "iPhone 15 Pro Max")
                @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
                String displayName,
        @Schema(description = "Device brand", example = "Apple")
                @Size(min = 1, max = 100, message = "Brand must be between 1 and 100 characters")
                String displayBrand,
        @Schema(description = "Device state", example = "IN_USE") DeviceState state) {}
