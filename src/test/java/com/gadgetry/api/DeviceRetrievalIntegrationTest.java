package com.gadgetry.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.domain.model.DeviceState;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DeviceRetrievalIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetDeviceById() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("MacBook Pro", "Apple", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var createdDevice =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        mockMvc.perform(get("/api/devices/" + createdDevice.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDevice.id().toString()))
                .andExpect(jsonPath("$.displayName").value("MacBook Pro"))
                .andExpect(jsonPath("$.displayBrand").value("Apple"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentDevice() throws Exception {
        // given
        var nonExistentId = UUID.randomUUID();

        // when & then
        mockMvc.perform(get("/api/devices/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.title").value("Device Not Found"));
    }
}
