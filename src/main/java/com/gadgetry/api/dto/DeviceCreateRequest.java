package com.gadgetry.api.dto;

import com.gadgetry.domain.model.DeviceState;

public record DeviceCreateRequest(String displayName, String displayBrand, DeviceState state) {}
