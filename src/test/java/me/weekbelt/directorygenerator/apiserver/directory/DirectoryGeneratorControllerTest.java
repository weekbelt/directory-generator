package me.weekbelt.directorygenerator.apiserver.directory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
@SpringBootTest
public class DirectoryGeneratorControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OldDepartmentJson에서 newDepatmentJson으로 변환 - 성공")
    public void testcase() throws Exception {
        // given
        String requestUrl = "/api/v1/directory/new-generator/br";

        // when
        ResultActions resultActions = mockMvc.perform(get(requestUrl));

        // then
        resultActions
            .andExpect(status().isOk());
    }
}