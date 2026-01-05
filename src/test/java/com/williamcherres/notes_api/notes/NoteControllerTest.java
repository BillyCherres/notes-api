package com.williamcherres.notes_api.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.williamcherres.notes_api.dto.CreateNoteRequest;
import com.williamcherres.notes_api.dto.NoteResponse;
import com.williamcherres.notes_api.error.NoteNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired MockMvc mvc;

    private final ObjectMapper om = new ObjectMapper();

    @MockitoBean NoteService service;


    // sends invalid request to post/notes
    // expects correct json error message
    @Test
    void post_invalid_returns400_withValidationJson() throws Exception {
        var body = new CreateNoteRequest("", ""); // creates fake request

        mvc.perform(post("/notes") // sends to /notes
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    // sends invalid Id to /notes{id}
    // expects  error message returne in json
    @Test
    void get_missingId_returns404_withNotFoundJson() throws Exception {
        when(service.getById(999999L)).thenThrow(new NoteNotFoundException(999999L));

        mvc.perform(get("/notes/999999"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Note 999999 not found"))
            .andExpect(jsonPath("$.path").value("/notes/999999"));
    }

    // 
    @Test
    void get_notesPaged_returns200_pageShape() throws Exception {
        Instant now = Instant.parse("2026-01-04T00:00:00Z");
        NoteResponse n = new NoteResponse(1L, "t", "c", now, now);

        PageRequest pr = PageRequest.of(0, 5);
        Page<NoteResponse> page = new PageImpl<>(List.of(n), pr, 1);

        when(service.list(pr)).thenReturn(page);

        mvc.perform(get("/notes?page=0&size=5"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].title").value("t"));
    }
}
