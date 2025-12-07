package com.gadgetry.persistance;

import com.gadgetry.domain.model.Device;
import com.gadgetry.domain.model.DeviceState;
import com.gadgetry.util.StringNormalizationUtil;
import org.springframework.data.jpa.domain.Specification;

public class DeviceSpecification {

    private DeviceSpecification() {
        // prevent instantiation for util class
    }

    public static Specification<Device> hasName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        var normalizedName = StringNormalizationUtil.normalize(name);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), normalizedName);
    }

    public static Specification<Device> hasBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return null;
        }
        var normalizedBrand = StringNormalizationUtil.normalize(brand);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("brand"), normalizedBrand);
    }

    public static Specification<Device> hasState(DeviceState state) {
        if (state == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), state);
    }
}
