package com.gadgetry.domain.model;

import com.gadgetry.persistence.DeviceEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "devices")
@EntityListeners(DeviceEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "display_name", nullable = false, length = 255)
    private String displayName;

    @Column(name = "display_brand", nullable = false, length = 100)
    private String displayBrand;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private DeviceState state;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public boolean isInUse() {
        return state == DeviceState.IN_USE;
    }
}
