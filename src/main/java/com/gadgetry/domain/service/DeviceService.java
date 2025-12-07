package com.gadgetry.domain.service;

import com.gadgetry.domain.model.Device;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.persistance.repository.DeviceRepository;
import com.gadgetry.util.StringNormalizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

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
}
