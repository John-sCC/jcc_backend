package com.nighthawk.spring_portfolio.mvc.person;

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

// import com.nighthawk.spring_portfolio.mvc.search.Student;

// @RestController
// @RequestMapping("/api/student")
// @CrossOrigin(origins = "https://rik-csa.github.io/")
// public class StudentController {

//     private List<Person> students = new ArrayList<>();

//     public StudentController() {
//         // Initialize your student list
//         students.add(new Person("John Kim", Arrays.asList("Math", "Physics"), "Chicago", true));
//         students.add(new Person("Alex Kim", Arrays.asList("Math", "Physics"), "Chicago", true));
//         // Add more sample students here...
//     }

//     @PostMapping("/add")
//     public ResponseEntity<String> addStudent(@RequestBody Person newStudent) {
//         students.add(newStudent);
//         return ResponseEntity.ok("Student added successfully");
//     }

//     @PostMapping("/findMostRelevant")
//     public ResponseEntity<Person> findMostRelevantStudent(@RequestBody Person newStudent,
//             @RequestParam int k) {
//         Person mostRelevantStudent = KNNStudentSelection.findMostRelevantStudent(students, newStudent, k);
//         return ResponseEntity.ok(mostRelevantStudent);
//     }

//     @GetMapping("/allStudents")
//     public ResponseEntity<List<Person>> getAllStudents() {
//         return ResponseEntity.ok(students);
//     }
// }
