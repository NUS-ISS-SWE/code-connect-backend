package com.nus.iss.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillsAssessmentServiceTest {

    private SkillsAssessmentService skillsAssessmentService;

    @BeforeEach
    void setUp() {
        skillsAssessmentService = new SkillsAssessmentService();
    }

    @Test
    void testGetSkillsTest() {
        List<String> questions = skillsAssessmentService.getSkillsTest("frontend");
        assertNotNull(questions);
        assertEquals(5, questions.size());
    }

    @Test
    void testGradeSkillsTest() {
        List<String> questions = Arrays.asList(
            "What is the correct syntax for a functional component in React?\nA) function MyComponent() {}\nB) const MyComponent = () => {}\nC) class MyComponent extends React.Component {}\nD) Both A and B",
            "Which hook is used to manage state in a functional component?\nA) useEffect\nB) useState\nC) useContext\nD) useReducer"
        );
        List<String> answers = Arrays.asList("D", "B");

        int score = skillsAssessmentService.gradeSkillsTest("frontend", questions, answers);
        assertEquals(2, score);
    }

    @Test
    void testGetAllQuestions() {
        List<String> questions = skillsAssessmentService.getAllQuestions("frontend");
        assertNotNull(questions);
        assertTrue(questions.size() > 0);
    }

    @Test
    void testAddQuestion() {
        String newQuestion = "What is the purpose of the useState hook in React?\nA) To manage state\nB) To handle side effects\nC) To manage context\nD) To manage refs";
        skillsAssessmentService.addQuestion("frontend", newQuestion);

        List<String> questions = skillsAssessmentService.getAllQuestions("frontend");
        assertTrue(questions.contains(newQuestion));
    }

    @Test
    void testRemoveQuestion() {
        String questionToRemove = "What is the purpose of the useState hook in React?\nA) To manage state\nB) To handle side effects\nC) To manage context\nD) To manage refs";
        skillsAssessmentService.addQuestion("frontend", questionToRemove);
        skillsAssessmentService.removeQuestion("frontend", questionToRemove);

        List<String> questions = skillsAssessmentService.getAllQuestions("frontend");
        assertFalse(questions.contains(questionToRemove));
    }
}