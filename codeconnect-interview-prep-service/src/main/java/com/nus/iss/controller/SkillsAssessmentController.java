package com.nus.iss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/skills-assessments")
public class SkillsAssessmentController {

    @Autowired
    private SkillsAssessmentService skillsAssessmentService;

    // Get a list of 5 random questions for the specified skills test type
    @GetMapping("/skills-test/{type}")
    public List<String> getSkillsTest(@PathVariable String type) {
        return skillsAssessmentService.getSkillsTest(type);
    }

    // Grade the skills test
    @PostMapping("/skills-test/{type}/grade")
    public int gradeSkillsTest(@PathVariable String type, @RequestBody Map<String, List<String>> requestBody) {
        List<String> questions = requestBody.get("questions");
        List<String> answers = requestBody.get("answers");
        return skillsAssessmentService.gradeSkillsTest(type, questions, answers);
    }

    // Get all questions for the specified skills test type
    @GetMapping("/questions/{type}")
    public List<String> getAllQuestions(@PathVariable String type) {
        return skillsAssessmentService.getAllQuestions(type);
    }

    // Add a new question to the specified skills test type
    @PostMapping("/questions/{type}")
    public void addQuestion(@PathVariable String type, @RequestBody String question) {
        skillsAssessmentService.addQuestion(type, question);
    }

    // Remove a question from the specified skills test type
    @DeleteMapping("/questions/{type}")
    public void removeQuestion(@PathVariable String type, @RequestBody String question) {
        skillsAssessmentService.removeQuestion(type, question);
    }
}