package com.gadgetry.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.domain.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DeviceDeleteIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldDeleteDevice() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("iPad Air", "Apple", DeviceState.AVAILABLE);

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
        mockMvc.perform(delete("/api/devices/" + createdDevice.id()))
                .andExpect(status().isNoContent());

        // then
        mockMvc.perform(get("/api/devices/" + createdDevice.id()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Device Not Found"));
    }

    @Test
    void shouldNotDeleteDeviceInUse() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("iPhone 14", "Apple", DeviceState.IN_USE);

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

        // when & then
        mockMvc.perform(delete("/api/devices/" + createdDevice.id()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Cannot delete device in use"))
                .andExpect(jsonPath("$.title").value("Conflict"));
    }
}
