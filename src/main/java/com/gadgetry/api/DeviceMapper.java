package com.gadgetry.api;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.api.dto.DeviceUpdateRequest;
import com.gadgetry.domain.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Device toEntity(DeviceCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Device toEntity(DeviceUpdateRequest request);

    DeviceResponse toResponse(Device device);
}
