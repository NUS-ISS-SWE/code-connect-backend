package com.nus.iss.controller;

import com.nus.iss.service.SkillsAssessmentService;
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

    // Grade the skills test and save the attempt
    @PostMapping("/skills-test/{type}/grade")
    public int gradeSkillsTest(@PathVariable String type, @RequestBody Map<String, Object> requestBody) {
        String candidateName = (String) requestBody.get("candidateName");
        List<String> questions = (List<String>) requestBody.get("questions");
        List<String> candidateAnswers = (List<String>) requestBody.get("answers");

        // Grade the test
        int score = skillsAssessmentService.gradeSkillsTest(type, questions, candidateAnswers);

        // Get expected answers
        List<String> expectedAnswers = skillsAssessmentService.getExpectedAnswers(type, questions);

        // Save the attempt
        skillsAssessmentService.saveAssessmentAttempt(candidateName, type, questions, candidateAnswers, expectedAnswers, score);

        return score;
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

    // Get assessment history for a candidate
    @GetMapping("/history/{candidateName}")
    public List<Map<String, Object>> getAssessmentHistory(@PathVariable String candidateName) {
        return skillsAssessmentService.getAssessmentHistory(candidateName);
    }

    // Get leaderboard for a specific skills test type and time period
    @GetMapping("/leaderboard/{type}")
    public List<Map<String, Object>> getLeaderboard(
            @PathVariable String type,
            @RequestParam String timePeriod) {
        return skillsAssessmentService.getLeaderboard(type, timePeriod);
    }
}