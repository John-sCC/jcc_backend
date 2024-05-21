package com.nighthawk.spring_portfolio.mvc.search;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api/student")
// @CrossOrigin(origins = "https://john-scc.github.io/")
// public class StudentController {

//     private List<Student> students = new ArrayList<>();

//     public StudentController() {
//         // Initialize your student list
//         students.add(new Student("John Kim", Arrays.asList("Math", "Physics"), "Chicago", true));
//         // Add more sample students here...
//     }

//     @PostMapping("/add")
//     public ResponseEntity<String> addStudent(@RequestBody Student newStudent) {
//         students.add(newStudent);
//         return ResponseEntity.ok("Student added successfully");
//     }

//     @PostMapping("/findMostRelevant")
//     public ResponseEntity<Student> findMostRelevantStudent(@RequestBody Student newStudent,
//             @RequestParam int k) {
//         Student mostRelevantStudent = KNNStudentSelection.findMostRelevantStudent(students, newStudent, k);
//         return ResponseEntity.ok(mostRelevantStudent);
//     }

//     @GetMapping("/allStudents")
//     public ResponseEntity<List<Student>> getAllStudents() {
//         return ResponseEntity.ok(students);
//     }
// }
