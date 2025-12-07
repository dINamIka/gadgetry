package com.gadgetry.api;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.domain.service.DeviceService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }
}
