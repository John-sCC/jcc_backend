package com.nighthawk.spring_portfolio.mvc.textAI;

// import java.time.LocalTime;
// import java.time.ZoneId;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController // annotation to simplify the creation of RESTful web services
// @RequestMapping("/api/texts")  // all requests in file begin with this URI
// public class TextsApiController {

//     // Autowired enables Control to connect URI request and POJO Object to easily for Database CRUD operations
//     @Autowired
//     private TextsJpaRepository repository;

//     @PostMapping("/create")
//     public ResponseEntity<Text> createText(String name, String text) {
//         Text t1 = new Text();
//         t1.setName(name);
//         t1.setText(text);
//         ZoneId zoneId1 = ZoneId.of("America/Los_Angeles");
//         t1.setTimeUpdated(LocalTime.now(zoneId1));
//         t1.setTested(false);
//         t1.setScore(0);
//         repository.save(t1);
//         return new ResponseEntity<>(t1, HttpStatus.OK);
//     }

//     @GetMapping("/")
//     public ResponseEntity<List<Text>> getAllTexts() {
//         // ResponseEntity returns List of Jokes provide by JPA findAll()
//         return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
//     }

//     @GetMapping("/get/{name}")
//     public ResponseEntity<Text> getTextByName(@PathVariable String name) {
//         // ResponseEntity returns List of Jokes provide by JPA findAll()
//         List<Text> texts = repository.findAll();
//         for (int i=0;i<texts.size();i++){
//             if (texts.get(i).getName().equals(name)){
//                 return new ResponseEntity<>(texts.get(i), HttpStatus.OK);
//             }
//         }
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }
    
//     @PutMapping("/updateText/{name}")
//     public ResponseEntity<Text> updateTextByName(@PathVariable String name, String text) {
//         // ResponseEntity returns List of Jokes provide by JPA findAll()
//         List<Text> texts = repository.findAll();
//         for (int i=0;i<texts.size();i++){
//             if (texts.get(i).getName().equals(name)){
//                 texts.get(i).setText(text);
//                 ZoneId zoneId1 = ZoneId.of("America/Los_Angeles");
//                 texts.get(i).setTimeUpdated(LocalTime.now(zoneId1));
//                 return new ResponseEntity<>(texts.get(i), HttpStatus.OK);
//             }
//         }
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }

//     @PutMapping("/updateScore/{name}")
//     public ResponseEntity<Text> updateScoreByName(@PathVariable String name, int score) {
//         // ResponseEntity returns List of Jokes provide by JPA findAll()
//         List<Text> texts = repository.findAll();
//         for (int i=0;i<texts.size();i++){
//             if (texts.get(i).getName().equals(name)){
//                 texts.get(i).setTested(true);
//                 texts.get(i).setScore(score);
//                 ZoneId zoneId1 = ZoneId.of("America/Los_Angeles");
//                 texts.get(i).setTimeUpdated(LocalTime.now(zoneId1));
//                 return new ResponseEntity<>(texts.get(i), HttpStatus.OK);
//             }
//         }
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }

//     @DeleteMapping("/delete/{name}")
//     public ResponseEntity<Text> deleteText(@PathVariable String name) {
//         // ResponseEntity returns List of Jokes provide by JPA findAll()
//         List<Text> texts = repository.findAll();
//         for (int i=0;i<texts.size();i++){
//             if (texts.get(i).getName().equals(name)){
//                 Text t1 = texts.get(i);
//                 repository.delete(texts.get(i));
//                 return new ResponseEntity<>(t1, HttpStatus.OK);
//             }
//         }
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//     }
// }
