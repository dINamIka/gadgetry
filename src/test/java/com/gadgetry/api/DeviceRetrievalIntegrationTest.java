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

    @Test
    void shouldGetAllDevicesWithoutFilters() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone 13",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy S22",
                                                        "Samsung",
                                                        DeviceState.IN_USE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.pageable").exists());
    }

    @Test
    void shouldGetAllDevicesFilteredByName() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone 14 Pro",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPad Pro",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy Tab",
                                                        "Samsung",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("name", "iPhone 14 Pro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].displayName").value("iPhone 14 Pro"))
                .andExpect(jsonPath("$.totalElements").value(1));

        mockMvc.perform(get("/api/devices").param("name", "iPad Pro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetAllDevicesFilteredByBrand() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone 12",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "MacBook Air",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Pixel 7",
                                                        "Google",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("brand", "apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));

        mockMvc.perform(get("/api/devices").param("brand", "google"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].displayBrand").value("Google"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetAllDevicesFilteredByNameAndBrand() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy S23",
                                                        "Samsung",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy Tab S8",
                                                        "Samsung",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy Watch",
                                                        "Samsung",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone Galaxy Mockup",
                                                        "Apple",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("name", "Galaxy S23").param("brand", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].displayName").value("Galaxy S23"))
                .andExpect(jsonPath("$.totalElements").value(1));

        mockMvc.perform(
                        get("/api/devices")
                                .param("name", "Galaxy Tab S8")
                                .param("brand", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].displayName").value("Galaxy Tab S8"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetAllDevicesWithPagination() throws Exception {
        // given
        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(
                            post("/api/devices")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(
                                            objectMapper.writeValueAsString(
                                                    new DeviceCreateRequest(
                                                            "Device " + i,
                                                            "Brand",
                                                            DeviceState.AVAILABLE))))
                    .andExpect(status().isCreated());
        }

        // when & then
        mockMvc.perform(get("/api/devices").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(2));

        mockMvc.perform(get("/api/devices").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(1));

        mockMvc.perform(get("/api/devices").param("page", "2").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.number").value(2));
    }

    @Test
    void shouldGetAllDevicesWithSorting() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Device A",
                                                        "Brand",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        Thread.sleep(10); // small delay to ensure different timestamps

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Device B",
                                                        "Brand",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].displayName").value("Device B"))
                .andExpect(jsonPath("$.content[1].displayName").value("Device A"));

        mockMvc.perform(get("/api/devices").param("sort", "createdAt,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].displayName").value("Device A"))
                .andExpect(jsonPath("$.content[1].displayName").value("Device B"));
    }

    @Test
    void shouldReturnEmptyPageWhenNoDevicesMatchFilter() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone", "Apple", DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("brand", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void shouldGetAllDevicesFilteredByState() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Device 1",
                                                        "Brand",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Device 2", "Brand", DeviceState.IN_USE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Device 3",
                                                        "Brand",
                                                        DeviceState.INACTIVE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("state", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].state").value("AVAILABLE"))
                .andExpect(jsonPath("$.totalElements").value(1));

        mockMvc.perform(get("/api/devices").param("state", "IN_USE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].state").value("IN_USE"))
                .andExpect(jsonPath("$.totalElements").value(1));

        mockMvc.perform(get("/api/devices").param("state", "INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].state").value("INACTIVE"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetAllDevicesFilteredByBrandAndState() throws Exception {
        // given
        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPhone", "Apple", DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "iPad", "Apple", DeviceState.IN_USE))))
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/devices")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new DeviceCreateRequest(
                                                        "Galaxy",
                                                        "Samsung",
                                                        DeviceState.AVAILABLE))))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/devices").param("brand", "apple").param("state", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].displayBrand").value("Apple"))
                .andExpect(jsonPath("$.content[0].state").value("AVAILABLE"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
