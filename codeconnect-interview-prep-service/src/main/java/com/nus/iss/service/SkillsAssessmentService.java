package com.nus.iss.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillsAssessmentService {

    private static final List<String> FRONTEND_QUESTIONS = new ArrayList<>(Arrays.asList(
        "What is the correct syntax for a functional component in React?\nA) function MyComponent() {}\nB) const MyComponent = () => {}\nC) class MyComponent extends React.Component {}\nD) Both A and B",
        "Which hook is used to manage state in a functional component?\nA) useEffect\nB) useState\nC) useContext\nD) useReducer",
        "How do you pass data from a parent component to a child component?\nA) Using state\nB) Using props\nC) Using context\nD) Using refs",
        "What is the purpose of the key prop in a list of elements?\nA) To uniquely identify each element\nB) To style each element\nC) To bind event handlers\nD) To manage state",
        "Which of the following is a valid way to handle side effects in a functional component?\nA) useState\nB) useEffect\nC) useContext\nD) useReducer"
    ));

    private static final List<String> BACKEND_QUESTIONS = new ArrayList<>(Arrays.asList(
        "What is the time complexity of accessing an element in an array?\nA) O(1)\nB) O(n)\nC) O(log n)\nD) O(n log n)",
        "Which data structure is used for implementing LIFO (Last In First Out) order?\nA) Queue\nB) Stack\nC) Linked List\nD) Tree",
        "What is the best case time complexity of QuickSort?\nA) O(n^2)\nB) O(n log n)\nC) O(n)\nD) O(log n)",
        "Which algorithm is used to find the shortest path in a graph?\nA) Depth First Search\nB) Breadth First Search\nC) Dijkstra's Algorithm\nD) Kruskal's Algorithm",
        "What is the space complexity of Merge Sort?\nA) O(1)\nB) O(n)\nC) O(log n)\nD) O(n log n)"
    ));

    private static final Map<String, String> FRONTEND_ANSWERS = new HashMap<String, String>() {{
        put("What is the correct syntax for a functional component in React?\nA) function MyComponent() {}\nB) const MyComponent = () => {}\nC) class MyComponent extends React.Component {}\nD) Both A and B", "D");
        put("Which hook is used to manage state in a functional component?\nA) useEffect\nB) useState\nC) useContext\nD) useReducer", "B");
        put("How do you pass data from a parent component to a child component?\nA) Using state\nB) Using props\nC) Using context\nD) Using refs", "B");
        put("What is the purpose of the key prop in a list of elements?\nA) To uniquely identify each element\nB) To style each element\nC) To bind event handlers\nD) To manage state", "A");
        put("Which of the following is a valid way to handle side effects in a functional component?\nA) useState\nB) useEffect\nC) useContext\nD) useReducer", "B");
    }};

    private static final Map<String, String> BACKEND_ANSWERS = new HashMap<String, String>() {{
        put("What is the time complexity of accessing an element in an array?\nA) O(1)\nB) O(n)\nC) O(log n)\nD) O(n log n)", "A");
        put("Which data structure is used for implementing LIFO (Last In First Out) order?\nA) Queue\nB) Stack\nC) Linked List\nD) Tree", "B");
        put("What is the best case time complexity of QuickSort?\nA) O(n^2)\nB) O(n log n)\nC) O(n)\nD) O(log n)", "B");
        put("Which algorithm is used to find the shortest path in a graph?\nA) Depth First Search\nB) Breadth First Search\nC) Dijkstra's Algorithm\nD) Kruskal's Algorithm", "C");
        put("What is the space complexity of Merge Sort?\nA) O(1)\nB) O(n)\nC) O(log n)\nD) O(n log n)", "B");
    }};

    private final Map<String, List<Map<String, Object>>> assessmentHistory = new HashMap<>();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<String> getSkillsTest(String type) {
        List<String> questions;
        if ("frontend".equalsIgnoreCase(type)) {
            questions = FRONTEND_QUESTIONS;
        } else if ("backend".equalsIgnoreCase(type)) {
            questions = BACKEND_QUESTIONS;
        } else {
            throw new IllegalArgumentException("Invalid test type: " + type);
        }
        Collections.shuffle(questions);
        return questions.subList(0, 5);
    }

    public int gradeSkillsTest(String type, List<String> questions, List<String> answers) {
        Map<String, String> correctAnswers;
        if ("frontend".equalsIgnoreCase(type)) {
            correctAnswers = FRONTEND_ANSWERS;
        } else if ("backend".equalsIgnoreCase(type)) {
            correctAnswers = BACKEND_ANSWERS;
        } else {
            throw new IllegalArgumentException("Invalid test type: " + type);
        }

        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i);
            String answer = answers.get(i);
            if (correctAnswers.containsKey(question) && correctAnswers.get(question).equalsIgnoreCase(answer)) {
                score++;
            }
        }
        return score;
    }

    public List<String> getAllQuestions(String type) {
        if ("frontend".equalsIgnoreCase(type)) {
            return new ArrayList<>(FRONTEND_QUESTIONS);
        } else if ("backend".equalsIgnoreCase(type)) {
            return new ArrayList<>(BACKEND_QUESTIONS);
        } else {
            throw new IllegalArgumentException("Invalid question type: " + type);
        }
    }

    public void addQuestion(String type, String question) {
        if ("frontend".equalsIgnoreCase(type)) {
            FRONTEND_QUESTIONS.add(question);
        } else if ("backend".equalsIgnoreCase(type)) {
            BACKEND_QUESTIONS.add(question);
        } else {
            throw new IllegalArgumentException("Invalid question type: " + type);
        }
    }

    public void removeQuestion(String type, String question) {
        if ("frontend".equalsIgnoreCase(type)) {
            FRONTEND_QUESTIONS.remove(question);
        } else if ("backend".equalsIgnoreCase(type)) {
            BACKEND_QUESTIONS.remove(question);
        } else {
            throw new IllegalArgumentException("Invalid question type: " + type);
        }
    }

    // Save an assessment attempt
    public void saveAssessmentAttempt(String candidateName, String type, List<String> questions, List<String> candidateAnswers, List<String> expectedAnswers, int score) {
        List<Map<String, Object>> attempts = assessmentHistory.computeIfAbsent(candidateName, k -> new ArrayList<>());

        Map<String, Object> attempt = new HashMap<>();
        attempt.put("type", type);
        attempt.put("questions", questions);
        attempt.put("candidateAnswers", candidateAnswers);
        attempt.put("expectedAnswers", expectedAnswers);
        attempt.put("score", score);

        String formattedTimestamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(DATE_TIME_FORMATTER);
        attempt.put("timestamp", formattedTimestamp);

        attempts.add(attempt);
    }

    // Retrieve the assessment history for a candidate
    public List<Map<String, Object>> getAssessmentHistory(String candidateName) {
        return assessmentHistory.getOrDefault(candidateName, new ArrayList<>());
    }

    // Get expected answers for a list of questions
    public List<String> getExpectedAnswers(String type, List<String> questions) {
        Map<String, String> correctAnswers;
        if ("frontend".equalsIgnoreCase(type)) {
            correctAnswers = FRONTEND_ANSWERS;
        } else if ("backend".equalsIgnoreCase(type)) {
            correctAnswers = BACKEND_ANSWERS;
        } else {
            throw new IllegalArgumentException("Invalid test type: " + type);
        }

        List<String> expectedAnswers = new ArrayList<>();
        for (String question : questions) {
            expectedAnswers.add(correctAnswers.getOrDefault(question, "N/A"));
        }
        return expectedAnswers;
    }

    // Retrieve the leaderboard
    public List<Map<String, Object>> getLeaderboard(String type, String timePeriod) {
        Instant now = Instant.now();
        Instant startTime;

        // Determine the start time based on the time period
        switch (timePeriod.toLowerCase()) {
            case "weekly":
                startTime = now.minusSeconds(7 * 24 * 60 * 60); // 7 days
                break;
            case "monthly":
                startTime = now.minusSeconds(30 * 24 * 60 * 60); // 30 days
                break;
            case "all-time":
                startTime = Instant.EPOCH; // All-time
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod);
        }

        // Filter and aggregate scores
        Map<String, Integer> scores = new HashMap<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : assessmentHistory.entrySet()) {
            String candidateName = entry.getKey();
            List<Map<String, Object>> attempts = entry.getValue();

            int totalScore = attempts.stream()
                .filter(attempt -> type.equalsIgnoreCase((String) attempt.get("type")))
                .filter(attempt -> {
                    String timestamp = (String) attempt.get("timestamp");
                    LocalDateTime attemptTime = LocalDateTime.parse(timestamp, DATE_TIME_FORMATTER);
                    return attemptTime.atZone(ZoneId.systemDefault()).toInstant().isAfter(startTime);
                })
                .mapToInt(attempt -> (int) attempt.get("score"))
                .sum();

            if (totalScore > 0) {
                scores.put(candidateName, totalScore);
            }
        }

        // Sort by score in descending order and limit to top 3
        return scores.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(3) // Limit to top 3 performers
            .map(entry -> {
                Map<String, Object> result = new HashMap<>();
                result.put("candidateName", entry.getKey());
                result.put("totalScore", entry.getValue());
                return result;
            })
            .collect(Collectors.toList());
    }
}