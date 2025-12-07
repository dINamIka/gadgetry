package com.gadgetry.persistence.repository;

import com.gadgetry.domain.model.Device;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository
        extends JpaRepository<Device, UUID>, JpaSpecificationExecutor<Device> {}
