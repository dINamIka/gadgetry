package com.gadgetry.persistance;

import com.gadgetry.domain.model.Device;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class DeviceEntityListener {

    private final Clock clock;

    public DeviceEntityListener(Clock clock) {
        this.clock = clock;
    }

    @PrePersist
    public void prePersist(Device device) {
        var now = clock.instant();
        device.setCreatedAt(now);
        device.setUpdatedAt(now);
        sanitizeFields(device);
    }

    @PreUpdate
    public void preUpdate(Device device) {
        device.setUpdatedAt(clock.instant());
        sanitizeFields(device);
    }

    private void sanitizeFields(Device device) {
        if (device.getDisplayName() != null) {
            device.setDisplayName(device.getDisplayName().trim());
        }
        if (device.getDisplayBrand() != null) {
            device.setDisplayBrand(device.getDisplayBrand().trim());
        }
    }
}
