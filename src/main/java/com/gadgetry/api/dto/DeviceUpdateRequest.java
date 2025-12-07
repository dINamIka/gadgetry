package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;

public record DeviceUpdateRequest(String displayName, String displayBrand, DeviceState state) {}
