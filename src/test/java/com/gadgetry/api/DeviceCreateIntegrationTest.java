package com.gadgetry.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gadgetry.api.dto.DeviceCreateRequest;
import com.gadgetry.api.dto.DeviceResponse;
import com.gadgetry.domain.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DeviceCreateIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateDevice() throws Exception {
        // given
        var request = new DeviceCreateRequest("iPhone 15 Pro", "Apple", DeviceState.AVAILABLE);

        // when
        var result =
                mockMvc.perform(
                                post("/api/devices")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.displayName").value("iPhone 15 Pro"))
                        .andExpect(jsonPath("$.displayBrand").value("Apple"))
                        .andExpect(jsonPath("$.state").value("AVAILABLE"))
                        .andExpect(jsonPath("$.createdAt").exists())
                        .andExpect(jsonPath("$.updatedAt").exists())
                        .andExpect(jsonPath("$.version").value(0))
                        .andReturn();

        // then
        var content = result.getResponse().getContentAsString();
        var response = objectMapper.readValue(content, DeviceResponse.class);
        assertThat(response.id()).isNotNull();
        assertThat(response.displayName()).isEqualTo("iPhone 15 Pro");
        assertThat(response.displayBrand()).isEqualTo("Apple");
    }

    @Test
    void shouldValidateRequiredFieldsOnCreate() throws Exception {
        // given
        var invalidRequest = "{}";

        // when & then
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
