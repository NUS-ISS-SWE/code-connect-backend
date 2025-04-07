package com.nus.iss.controller;

import com.nus.iss.service.SkillsAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SkillsAssessmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SkillsAssessmentService skillsAssessmentService;

    @InjectMocks
    private SkillsAssessmentController skillsAssessmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skillsAssessmentController).build();
    }

    @Test
    void testGetAllQuestions() throws Exception {
        List<String> questions = Arrays.asList(
            "What is the correct syntax for a functional component in React?\nA) function MyComponent() {}\nB) const MyComponent = () => {}\nC) class MyComponent extends React.Component {}\nD) Both A and B",
            "Which hook is used to manage state in a functional component?\nA) useEffect\nB) useState\nC) useContext\nD) useReducer"
        );

        when(skillsAssessmentService.getAllQuestions("frontend")).thenReturn(questions);

        mockMvc.perform(get("/skills-assessments/questions/frontend"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0]").value("What is the correct syntax for a functional component in React?\nA) function MyComponent() {}\nB) const MyComponent = () => {}\nC) class MyComponent extends React.Component {}\nD) Both A and B"))
            .andExpect(jsonPath("$[1]").value("Which hook is used to manage state in a functional component?\nA) useEffect\nB) useState\nC) useContext\nD) useReducer"));

        verify(skillsAssessmentService, times(1)).getAllQuestions("frontend");
    }

    @Test
    void testAddQuestion() throws Exception {
        String question = "What is the purpose of the useState hook in React?\nA) To manage state\nB) To handle side effects\nC) To manage context\nD) To manage refs";

        mockMvc.perform(post("/skills-assessments/questions/frontend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(question))
            .andExpect(status().isOk());

        verify(skillsAssessmentService, times(1)).addQuestion("frontend", question);
    }

    @Test
    void testRemoveQuestion() throws Exception {
        String question = "What is the purpose of the useState hook in React?\nA) To manage state\nB) To handle side effects\nC) To manage context\nD) To manage refs";

        mockMvc.perform(delete("/skills-assessments/questions/frontend")
            .contentType(MediaType.APPLICATION_JSON)
            .content(question))
            .andExpect(status().isOk());

        verify(skillsAssessmentService, times(1)).removeQuestion("frontend", question);
    }
}