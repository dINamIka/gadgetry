package com.gadgetry.api;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.api.dto.DeviceUpdateRequest;
import com.gadgetry.domain.model.Device;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.domain.service.DeviceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Validated
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;

    @PostMapping
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceCreateRequest request) {
        var device = deviceMapper.toEntity(request);
        var created = deviceService.create(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getById(@PathVariable UUID id) {
        var device = deviceService.findById(id);
        return ResponseEntity.ok(deviceMapper.toResponse(device));
    }

    @GetMapping
    public ResponseEntity<Page<DeviceResponse>> geAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            DeviceState state,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        var sortParams = sort.split(",");
        var sortField = sortParams[0];
        var direction =
                sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        var pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        var devicePage =
                (name != null || brand != null || state != null)
                        ? deviceService.search(name, brand, state, pageable)
                        : deviceService.findAll(pageable);

        Page<DeviceResponse> responsePage = devicePage.map(deviceMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DeviceResponse> update(
            @PathVariable UUID id, @Valid @RequestBody DeviceUpdateRequest request) {
        Device updateData = deviceMapper.toEntity(request);
        Device updated = deviceService.update(id, updateData);
        return ResponseEntity.ok(deviceMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
