package com.gadgetry.api;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.api.dto.DeviceUpdateRequest;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.domain.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Validated
@Tag(name = "Devices", description = "Device management API")
public class DeviceController {

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("createdAt", "updatedAt", "name", "brand", "state");

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;

    @PostMapping
    @Operation(
            summary = "Create a new device",
            description = "Creates a new device with the provided details")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Device created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data"),
                @ApiResponse(responseCode = "422", description = "Business validation failed")
            })
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceCreateRequest request) {
        var createdDevice = deviceService.create(deviceMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deviceMapper.toResponse(createdDevice));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get device by ID",
            description = "Retrieves a device by its unique identifier")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Device found"),
                @ApiResponse(responseCode = "404", description = "Device not found")
            })
    public ResponseEntity<DeviceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(deviceMapper.toResponse(deviceService.findById(id)));
    }

    @GetMapping
    @Operation(
            summary = "Get all devices with pagination and optional filtering",
            description =
                    "Retrieves devices with pagination. Optionally filter by name, brand, and/or state")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Devices retrieved successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
            })
    public ResponseEntity<Page<DeviceResponse>> getAll(
            @Parameter(description = "Filter by device name (case-insensitive partial match)")
                    @RequestParam(required = false)
                    String name,
            @Parameter(description = "Filter by device brand (case-insensitive partial match)")
                    @RequestParam(required = false)
                    String brand,
            @Parameter(description = "Filter by device state") @RequestParam(required = false)
                    DeviceState state,
            @Parameter(description = "Page number (0-indexed)")
                    @RequestParam(defaultValue = "0")
                    @Min(0)
                    int page,
            @Parameter(description = "Page size (max 50)")
                    @RequestParam(defaultValue = "20")
                    @Min(1)
                    @Max(50)
                    int size,
            @Parameter(description = "Sort field and direction (e.g., 'createdAt,desc')")
                    @RequestParam(defaultValue = "createdAt,desc")
                    String sort) {
        var sortParams = sort.split(",");
        var sortField = sortParams[0];
        var pageable = buildPageReq(page, size, sortField, sortParams);
        var devicePage = deviceService.findDevices(name, brand, state, pageable);
        var responsePage = devicePage.map(deviceMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    private static PageRequest buildPageReq(
            int page, int size, String sortField, String[] sortParams) {
        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid sort field: %s. Allowed fields: %s",
                            sortField, ALLOWED_SORT_FIELDS));
        }

        var direction =
                sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        var pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return pageable;
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update device",
            description = "Partially updates a device (only provided fields are updated)")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Device updated successfully"),
                @ApiResponse(responseCode = "404", description = "Device not found"),
                @ApiResponse(
                        responseCode = "409",
                        description = "Device in use or optimistic lock conflict"),
                @ApiResponse(responseCode = "422", description = "Invalid state transition")
            })
    public ResponseEntity<DeviceResponse> update(
            @PathVariable UUID id, @Valid @RequestBody DeviceUpdateRequest request) {
        var updatedDevice = deviceMapper.toEntity(request);
        return ResponseEntity.ok(deviceMapper.toResponse(deviceService.update(id, updatedDevice)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete device", description = "Deletes a device")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Device not found"),
                @ApiResponse(
                        responseCode = "409",
                        description = "Device is in use and cannot be deleted")
            })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
