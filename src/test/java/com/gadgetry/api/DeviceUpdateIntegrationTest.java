package com.gadgetry.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.domain.model.DeviceState;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DeviceUpdateIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldPartiallyUpdateDeviceName() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("Old Name", "Apple", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        var updateRequest = "{\"displayName\": \"New Name\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id().toString()))
                .andExpect(jsonPath("$.displayName").value("New Name"))
                .andExpect(jsonPath("$.displayBrand").value("Apple"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void shouldPartiallyUpdateDeviceBrand() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("Phone", "Old Brand", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        DeviceResponse created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        var updateRequest = "{\"displayBrand\": \"New Brand\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Phone"))
                .andExpect(jsonPath("$.displayBrand").value("New Brand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void shouldPartiallyUpdateDeviceState() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("Device", "Brand", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        var updateRequest = "{\"state\": \"IN_USE\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Device"))
                .andExpect(jsonPath("$.displayBrand").value("Brand"))
                .andExpect(jsonPath("$.state").value("IN_USE"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void shouldPartiallyUpdateMultipleFields() throws Exception {
        // given
        var createRequest =
                new DeviceCreateRequest("Old Device", "Old Brand", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        var updateRequest = "{\"displayName\": \"Updated Device\", \"state\": \"IN_USE\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Updated Device"))
                .andExpect(jsonPath("$.displayBrand").value("Old Brand"))
                .andExpect(jsonPath("$.state").value("IN_USE"))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void shouldNotUpdateDeviceNameWhenInUse() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("Original Name", "Brand", DeviceState.IN_USE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when & then
        var updateRequest = "{\"displayName\": \"New Name\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Cannot update name of device in use"))
                .andExpect(jsonPath("$.title").value("Conflict"));
    }

    @Test
    void shouldNotUpdateDeviceBrandWhenInUse() throws Exception {
        // given
        var createRequest =
                new DeviceCreateRequest("Device Name", "Original Brand", DeviceState.IN_USE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when & then
        var updateRequest = "{\"displayBrand\": \"New Brand\"}";

        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Cannot update brand of device in use"))
                .andExpect(jsonPath("$.title").value("Conflict"));
    }

    @Test
    void shouldNotUpdateNonExistentDevice() throws Exception {
        // given
        var nonExistentId = UUID.randomUUID();
        var updateRequest = "{\"displayName\": \"New Name\"}";

        // when & then
        mockMvc.perform(
                        patch("/api/devices/" + nonExistentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Device Not Found"));
    }

    @Test
    void shouldUpdateDeviceWithEmptyRequestBody() throws Exception {
        // given
        var createRequest = new DeviceCreateRequest("Device", "Brand", DeviceState.AVAILABLE);

        var createResult =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(createRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        var created =
                objectMapper.readValue(
                        createResult.getResponse().getContentAsString(), DeviceResponse.class);

        // when
        var updateRequest = "{}";
        mockMvc.perform(
                        patch("/api/devices/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Device"))
                .andExpect(jsonPath("$.displayBrand").value("Brand"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.version").value(0));
    }
}
