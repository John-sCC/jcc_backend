package com.nighthawk.spring_portfolio.mvc.assignment;

import java.io.File;
import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.CookieValue;

import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodDetailsService;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;

import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;
import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriod;
import java.util.HashMap;

@RestController
@RequestMapping("/api/assignment")
public class AssignmentApiController {
    //     @Autowired
    // private JwtTokenUtil jwtGen;
    /*
    #### RESTful API ####
    Resource: https://spring.io/guides/gs/rest-service/
    */

    // Autowired enables Control to connect POJO Object through JPA
    @Autowired
    private AssignmentJpaRepository repository;

    @Autowired
    private AssignmentDetailsService assignmentDetailsService;

    @Autowired
    private AssignmentSubmissionJpaRepository subRepository;

    @Autowired
    private AssignmentSubmissionDetailsService subDetailsService;

    @Autowired
    private ClassPeriodDetailsService classService;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Autowired
    private PersonJpaRepository personRepository;

    /*
    GET individual Person using ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable long id) {
        Assignment assignment = repository.findById(id);
        if (assignment != null) {  // Good ID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);       
    }

    /*
    DELETE individual Person using ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable long id) {
        Assignment assignment = repository.findById(id);
        if (assignment != null) {  // Good ID
            repository.deleteById(id);  // value from findByID
            return new ResponseEntity<>(assignment, HttpStatus.OK);  // OK HTTP response: status code, headers, and body
        }
        // Bad ID
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    /*
    POST Aa record by Requesting Parameters from URI
     */
    @PostMapping("/post")
    public ResponseEntity<Object> postAssignment(@CookieValue("jwt") String jwtToken, 
                                                 @RequestBody AssignmentRequest request) {
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        for (String className : request.getClassNames()) {
            if (classService.getByName(className) == null) {
                return new ResponseEntity<>("One or more classes was invalid", HttpStatus.BAD_REQUEST);
            }
        }
        // A assignment object WITHOUT ID will create a new record with default roles as student
        Assignment assignment = new Assignment(request.getName(), request.getDateCreated(), request.getDateDue(), request.getContent());
        boolean saved = false;
        for (String className : request.getClassNames()) {
            if (classService.getByName(className).getLeaders().contains(existingPerson)) {
                if (!(saved)) {
                    assignmentDetailsService.save(assignment);
                    saved = true;
                }
                classService.addAssignmentToClass(assignment.getId(), className);
            }
        }
        if(saved) {
            return new ResponseEntity<>(assignment.getName() + " is created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("The assignment couldn't be created. Leadership role could not be found.", HttpStatus.BAD_REQUEST);
    }


    @Value("${java.io.tmpdir}")
    private String tempUploadDir;

    private String uploadDir = "src/main/java/com/nighthawk/spring_portfolio/mvc/assignment/StoredAssignments";

    @PostMapping("/submit")
    public ResponseEntity<Object> handleFileUpload(@CookieValue("jwt") String jwtToken,
                                                   @RequestPart("file") MultipartFile file,
                                                   @RequestBody SubmissionRequest submissionRequest) {
        // jwt processing for user info
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // determining the relevant assignment and its relation to user
        boolean userInClasses = false;
        Assignment submittedAssignment = assignmentDetailsService.get(submissionRequest.getId()); // getting requested assignment
        List<ClassPeriod> classesWithAssignment = classService.getClassPeriodsByAssignment(submittedAssignment); // classes with assignment
        for (ClassPeriod classPeriod : classesWithAssignment) {
            if (classPeriod.getStudents().contains(existingPerson)) {
                userInClasses = true; // user is in a class period with the given assignment
            }
        }
        if (!userInClasses) { // if the user is not in the necessary class
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // checking the submission number
        int submissionNumber = 1;
        for (AssignmentSubmission submission : submittedAssignment.getSubmissions()) {
            if (submission.getSubmitter().getEmail().equals(existingPerson.getEmail())) {
                if (submission.getSubmissionNumber() >= submissionNumber) {
                    submissionNumber = submission.getSubmissionNumber() + 1;
                }
            }
        }

        // processing file upload if the user has been verified (RAYMOND CODE)
        try {
            //check if file type is null: edge case
            String contentType = file.getContentType();

            if (contentType == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File content type is not supported");
            }

            String fileExtension = getFileExtension(file.getOriginalFilename());

            if (!isValidFileType(fileExtension, contentType)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File type is not supported");
            }
    
            // Create the temporary upload directory if it doesn't exist
            File tempDirectory = new File(tempUploadDir);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            // Save the file to the temporary upload directory
            String tempFilePath = tempUploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(tempFilePath));

            // Move the file to the final destination
            String finalFilePath = uploadDir + File.separator + file.getOriginalFilename();
            new File(tempFilePath).renameTo(new File(finalFilePath));

            // saving the new assignment submission following (DREW CODE)
            AssignmentSubmission submission = new AssignmentSubmission(existingPerson, finalFilePath, submissionRequest.getSubmissionTime(), submissionNumber);
            subDetailsService.save(submission); // saving the new submission
            assignmentDetailsService.addSubmissionToAssignment(submittedAssignment, submission); // adding the submission to the assignment
            return new ResponseEntity<>("Submission to the assignment \"" + submittedAssignment.getName() + "\" was successful!", HttpStatus.CREATED);
            // finished processing!!! wow!!!
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            //check if file type is null: edge case
            String contentType = file.getContentType();

            if (contentType == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File content type is not supported");
            }

            String fileExtension = getFileExtension(file.getOriginalFilename());

            if (!isValidFileType(fileExtension, contentType)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File type is not supported");
            }
    
            // Create the temporary upload directory if it doesn't exist
            File tempDirectory = new File(tempUploadDir);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            // Save the file to the temporary upload directory
            String tempFilePath = tempUploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(tempFilePath));

            // Move the file to the final destination
            String finalFilePath = uploadDir + File.separator + file.getOriginalFilename();
            new File(tempFilePath).renameTo(new File(finalFilePath));
            
            return ResponseEntity.ok(finalFilePath);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }

    private boolean isValidFileType(String fileExtension, String contentType) {
        HashMap<String, String> fileTypes = new HashMap<>();
        fileTypes.put("pdf", MediaType.APPLICATION_PDF_VALUE);
        fileTypes.put("jpg", MediaType.IMAGE_JPEG_VALUE);
        fileTypes.put("jpeg", MediaType.IMAGE_JPEG_VALUE);
        fileTypes.put("png", MediaType.IMAGE_PNG_VALUE);
        fileTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        
        if (fileTypes.containsKey(fileExtension) && fileTypes.get(fileExtension).equals(contentType))
        {
            return true;
        }
        return false;
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf('.');
        return filename.substring(dotIndex + 1);
    }
}
