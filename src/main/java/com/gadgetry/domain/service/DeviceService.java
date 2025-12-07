package com.gadgetry.domain.service;

import com.gadgetry.domain.exception.DeviceInUseException;
import com.gadgetry.domain.exception.DeviceNotFoundException;
import com.gadgetry.domain.model.Device;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.persistance.repository.DeviceRepository;
import com.gadgetry.util.StringNormalizationUtil;
import java.time.Clock;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
