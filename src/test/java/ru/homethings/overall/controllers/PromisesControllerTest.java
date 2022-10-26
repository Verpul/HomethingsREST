package ru.homethings.overall.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.homethings.overall.model.Promise;
import ru.homethings.overall.repositories.PromiseRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromisesController.class)
class PromisesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromiseRepository promiseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Promise testPromise;

    @BeforeEach
    void setUp() {
        testPromise = new Promise();
        testPromise.setId(1L);
        testPromise.setText("Example");
    }

    @Test
    void getPromisesList() throws Exception{
        List<Promise> allPromises = Collections.singletonList(testPromise);

        given(promiseRepository.findAll()).willReturn(allPromises);

        mockMvc.perform(get("/overall/promise")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].text", is(testPromise.getText())));
    }

    @Test
    void getPromiseById() throws Exception {
        given(promiseRepository.findById(1L)).willReturn(Optional.of(testPromise));

        mockMvc.perform(get("/overall/promise/" + testPromise.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testPromise.getId().intValue())))
                .andExpect(jsonPath("$.text", is(testPromise.getText())));
    }

    @Test
    void shouldNotGetPromiseById() throws Exception {
        given(promiseRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/overall/promise/" + 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPromise() throws Exception{
        Promise savedPromise = new Promise();
        savedPromise.setId(2L);
        savedPromise.setText("Saved promise");
        savedPromise.setBeginDate(LocalDate.of(2022, 1, 1));
        savedPromise.setEndDate(LocalDate.of(2023, 1, 1));

        mockMvc.perform(post("/overall/promise")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedPromise)))
                .andExpect(status().isCreated());
    }

    @Test
    void editPromise() throws Exception {
        testPromise.setText("Updated text");
        testPromise.setBeginDate(LocalDate.of(2021, 12, 12));
        testPromise.setEndDate(LocalDate.of(2024, 6, 6));

        when(promiseRepository.save(testPromise)).thenReturn(testPromise);

        mockMvc.perform(put("/overall/promise/" + testPromise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPromise)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Updated text")))
                .andExpect(jsonPath("$.beginDate", is(testPromise.getBeginDate().toString())))
                .andExpect(jsonPath("$.endDate", is(testPromise.getEndDate().toString())));
    }

    @Test
    void deletePromise() throws Exception{
        doNothing().when(promiseRepository).deleteById(testPromise.getId());

        mockMvc.perform(delete("/overall/promise/" + testPromise.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}