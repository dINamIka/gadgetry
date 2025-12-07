package com.gadgetry.api;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@Validated
public class DeviceController {

    @PostMapping
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceCreateRequest request) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }
}
