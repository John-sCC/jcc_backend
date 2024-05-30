package com.nighthawk.spring_portfolio.mvc.textAI;

// import java.time.LocalTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data  // Annotations to simplify writing code (ie constructors, setters)
// @NoArgsConstructor
// @AllArgsConstructor
// @Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
// public class Text {
//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private Long id;
//     @Column(unique=true)
//     private String name;
//     @Column
//     private String text;
//     @Column
//     private LocalTime timeUpdated;
//     @Column
//     private boolean tested;
//     @Column
//     private int score;

//     public static Text[] init() {
//         Text t1 = new Text();
//         t1.setName("New Text");
//         t1.setText("");
//         t1.setTimeUpdated(LocalTime.MIDNIGHT);
//         t1.setTested(false);
//         t1.setScore(0);
//         Text texts[] = {t1};
//         return texts;
//     }
// }
