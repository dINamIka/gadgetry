package com.gadgetry.domain.service;

import com.gadgetry.domain.exception.DeviceInUseException;
import com.gadgetry.domain.exception.DeviceNotFoundException;
import com.gadgetry.domain.model.Device;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.persistence.DeviceSpecification;
import com.gadgetry.persistence.repository.DeviceRepository;
import com.gadgetry.util.StringNormalizationUtil;
import java.time.Clock;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final Clock clock;

    @Transactional
    public Device create(Device device) {
        // defaults to AVAILABLE if not set
        if (device.getState() == null) {
            device.setState(DeviceState.AVAILABLE);
        }
        // populate normalized fields for searching
        device.setName(StringNormalizationUtil.normalize(device.getDisplayName()));
        device.setBrand(StringNormalizationUtil.normalize(device.getDisplayBrand()));
        return deviceRepository.save(device);
    }

    @Transactional(readOnly = true)
    public Device findById(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Device> search(String name, String brand, DeviceState state, Pageable pageable) {
        Specification<Device> spec = null;

        var nameSpec = DeviceSpecification.hasName(name);
        var brandSpec = DeviceSpecification.hasBrand(brand);
        var stateSpec = state != null ? DeviceSpecification.hasState(state) : null;

        if (nameSpec != null) {
            spec = nameSpec;
        }
        if (brandSpec != null) {
            spec = (spec == null) ? brandSpec : spec.and(brandSpec);
        }
        if (stateSpec != null) {
            spec = (spec == null) ? stateSpec : spec.and(stateSpec);
        }

        if (spec != null) {
            return deviceRepository.findAll(spec, pageable);
        } else {
            return deviceRepository.findAll(pageable);
        }
    }

    @Transactional
    public Device update(UUID id, Device updateData) {
        var existingDevice = findById(id);

        if (existingDevice.isInUse()) {
            if (updateData.getDisplayName() != null) {
                var normalizedNewName =
                        StringNormalizationUtil.normalize(updateData.getDisplayName());
                if (!normalizedNewName.equals(existingDevice.getName())) {
                    throw new DeviceInUseException("Cannot update name of device in use");
                }
            }
            if (updateData.getDisplayBrand() != null) {
                var normalizedNewBrand =
                        StringNormalizationUtil.normalize(updateData.getDisplayBrand());
                if (!normalizedNewBrand.equals(existingDevice.getBrand())) {
                    throw new DeviceInUseException("Cannot update brand of device in use");
                }
            }
        }

        if (updateData.getDisplayName() != null) {
            existingDevice.setDisplayName(updateData.getDisplayName());
            existingDevice.setName(StringNormalizationUtil.normalize(updateData.getDisplayName()));
        }
        if (updateData.getDisplayBrand() != null) {
            existingDevice.setDisplayBrand(updateData.getDisplayBrand());
            existingDevice.setBrand(
                    StringNormalizationUtil.normalize(updateData.getDisplayBrand()));
        }
        if (updateData.getState() != null) {
            existingDevice.setState(updateData.getState());
        }

        return deviceRepository.save(existingDevice);
    }

    @Transactional
    public void delete(UUID id) {
        var device = findById(id);

        if (device.isInUse()) {
            throw new DeviceInUseException("Cannot delete device in use");
        }

        device.setDeletedAt(clock.instant());
        deviceRepository.save(device);
    }
}
