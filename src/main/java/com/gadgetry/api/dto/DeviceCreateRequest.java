package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new device")
public record DeviceCreateRequest(
        @Schema(description = "Device display name", example = "iPhone 15 Pro")
                @NotBlank(message = "Device name is required")
                @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
                String displayName,
        @Schema(description = "Device brand", example = "Apple")
                @NotBlank(message = "Brand is required")
                @Size(min = 1, max = 100, message = "Brand must be between 1 and 100 characters")
                String displayBrand,
        @Schema(description = "Device state", example = "AVAILABLE") DeviceState state) {}
