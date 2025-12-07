package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeviceCreateRequest(
        @NotBlank(message = "Device name is required")
                @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
                String displayName,
        @NotBlank(message = "Brand is required")
                @Size(min = 1, max = 100, message = "Brand must be between 1 and 100 characters")
                String displayBrand,
        DeviceState state) {}
