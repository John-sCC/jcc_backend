package com.nighthawk.spring_portfolio.mvc.person;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import com.nighthawk.spring_portfolio.mvc.person.Person;

// public class KNNStudentSelection {

//     // KNN algorithm for student selection
//     public static Person findMostRelevantStudent(List<Person> students, Person newStudent, int k) {
//         Map<Double, Person> distanceMap = new HashMap<>();

//         // Calculate Euclidean distance for each student
//         for (Person student : students) {
//             double distance = calculateDistance(student, newStudent);
//             distanceMap.put(distance, student);
//         }

//         // Sort distances and get the top k neighbors
//         List<Double> distances = new ArrayList<>(distanceMap.keySet());
//         Collections.sort(distances);

//         // Return the most relevant student
//         return distanceMap.get(distances.get(0));
//     }

//     private static double calculateDistance(Person student1, Person student2) {
//         // Simple distance calculation based on preferences
//         double subjectDistance = calculateSubjectDistance(student1.getSubjectsKnown(), student2.getSubjectsKnown());
//         double locationDistance = student1.getPreferredLocation().equals(student2.getPreferredLocation()) ? 0 : 1;

//         // Euclidean distance
//         return Math.sqrt(Math.pow(subjectDistance, 2) + Math.pow(locationDistance, 2));
//     }

//     private static double calculateSubjectDistance(List<String> subjects1, List<String> subjects2) {
//         // Jaccard similarity for subject distance
//         List<String> union = new ArrayList<>(subjects1);
//         union.addAll(subjects2);

//         List<String> intersection = new ArrayList<>(subjects1);
//         intersection.retainAll(subjects2);

//         return 1 - ((double) intersection.size() / union.size());
//     }
// }
