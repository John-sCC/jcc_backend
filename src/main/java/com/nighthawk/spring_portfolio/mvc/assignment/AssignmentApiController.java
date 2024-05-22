package com.nighthawk.spring_portfolio.mvc.assignment;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriod;
import com.nighthawk.spring_portfolio.mvc.classPeriod.ClassPeriodDetailsService;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtTokenUtil;
import com.nighthawk.spring_portfolio.mvc.person.Person;
import com.nighthawk.spring_portfolio.mvc.person.PersonJpaRepository;


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
    GET individual Assignment using ID
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

    @GetMapping("/cookie/{id}")
    public ResponseEntity<?> getAssignmentWithCookie(@CookieValue("jwt") String jwtToken,
                                                              @PathVariable long id) {
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Assignment assignment = repository.findById(id);
        if (assignment == null) {  // bad ID
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // OK HTTP response: status code, headers, and body
        }
        HashMap<String, Object> assignmentData = new HashMap<>();
        assignmentData.put("role", null);
        // good ID, so continue to check relationship
        for (ClassPeriod cp : classService.getClassPeriodsByAssignment(assignment)) {
            for (Person student : cp.getStudents()) {
                if (student.getEmail().equals(existingPerson.getEmail())) {
                    assignmentData.put("role", "student"); // person has teacher access to the assignment
                }
            }
            for (Person leader : cp.getLeaders()) {
                if (leader.getEmail().equals(existingPerson.getEmail())) {
                    assignmentData.put("role", "teacher"); // person has teacher access to the assignment
                }
            }
        }
        // handler for invalid user accessing data
        if (assignmentData.get("role") == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // if the student is determined to have no relationship to the assignment, null indicates they cannot access w/ role
        assignmentData.put("data", assignment);
        // NOW MAKE IT ALSO FETCH PREVIOUS SUBMISSION
        ArrayList<AssignmentSubmission> personSubmissions = new ArrayList<>();
        for (AssignmentSubmission submission : assignment.getSubmissions()) {
            if (submission.getSubmitter().getEmail().equals(existingPerson.getEmail())) { // verifying user identity with email
                personSubmissions.add(submission);
            }
        }
        assignmentData.put("submissions", personSubmissions.toArray());
        return new ResponseEntity<>(assignmentData, HttpStatus.OK);    
    }

    @GetMapping("/cookie/{id}/grading")
    public ResponseEntity<?> getAssignmentSubmissionsWithCookie(@CookieValue("jwt") String jwtToken,
                                                                @PathVariable long id) {
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Assignment assignment = repository.findById(id);
        if (assignment == null) {  // bad ID
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // OK HTTP response: status code, headers, and body
        }
        boolean isTeacher = false;
        // good ID, so continue to check relationship
        for (ClassPeriod cp : classService.getClassPeriodsByAssignment(assignment)) {
            for (Person leader : cp.getLeaders()) {
                if (leader.getEmail().equals(existingPerson.getEmail())) {
                    isTeacher = true; // person has teacher access to the assignment
                }
            }
        }
        // handler for invalid user accessing data
        if (!(isTeacher)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // sending all submissions from a given assignment
        return new ResponseEntity<>(assignment.getSubmissions(), HttpStatus.OK);    
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
        Assignment assignment = new Assignment(request.getName(), request.getDateCreated(), request.getDateDue(), request.getContent(), request.getPoints(), request.getAllowedSubmissions(), request.getAllowedFileTypes());
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

    @PostMapping("/submit/{id}/{submissionTimeString}")
    public ResponseEntity<Object> handleFileUpload(@CookieValue("jwt") String jwtToken,
                                                   @RequestPart("file") MultipartFile file,
                                                   @PathVariable long id,
                                                   @PathVariable String submissionTimeString) {
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
        Assignment submittedAssignment = assignmentDetailsService.get(id); // getting requested assignment
        List<ClassPeriod> classesWithAssignment = classService.getClassPeriodsByAssignment(submittedAssignment); // classes with assignment
        for (ClassPeriod classPeriod : classesWithAssignment) {
            for (Person student : classPeriod.getStudents()) {
                if (student.getEmail().equals(existingPerson.getEmail())) {
                    userInClasses = true; // user is in a class period with the given assignment
                }
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
        // converting submissionTime String to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        Date submissionTime;
        try {
            submissionTime = dateFormat.parse(submissionTimeString);
        } catch (ParseException e) {
            // handling parse exception
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            AssignmentSubmission submission = new AssignmentSubmission(existingPerson, finalFilePath, submissionTime, submissionNumber);
            subDetailsService.save(submission); // saving the new submission
            assignmentDetailsService.addSubmissionToAssignment(submittedAssignment, submission); // adding the submission to the assignment
            assignmentDetailsService.save(submittedAssignment);
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
        fileTypes.put("txt", MediaType.TEXT_PLAIN_VALUE);
        
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

    @GetMapping("/previewCheck")
    public ResponseEntity<String> getFilePreview(@CookieValue("jwt") String jwtToken, @RequestParam("assignmentID") long id) {
        if (jwtToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // getting user data
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Person existingPerson = personRepository.findByEmail(userEmail);
        if (existingPerson == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Assignment assignment = repository.findById(id); //find the assignment

        boolean isLeader = false;
        List<AssignmentSubmission> allSubmissionsOut = new ArrayList<>();
        loop1: for (AssignmentSubmission sub : assignment.getSubmissions()) //get all the submissions
        {
            // Person user = sub.getSubmitter(); //get the submitter for each submission to the assignment
            // if (user.getEmail() == userEmail) //if the user is the same as the one in the JWT token
            // {
                List<ClassPeriod> classesInAssignment = classService.getClassPeriodsByAssignment(assignment);
                for (ClassPeriod classP : classesInAssignment) //go through all class periods in the assignment
                {
                    Collection<Person> lead = classP.getLeaders(); //get the leaders of the class period
                    for (Person leaderOfClassPeriod : lead)
                    {
                        if (existingPerson.equals(leaderOfClassPeriod)) //if the user from the JWT token is a leader then grant access
                        {
                            isLeader = true; //after this, send a link describing each submission to the assignment
                            allSubmissionsOut.addAll(assignment.getSubmissions());
                            break loop1;
                        }
                    }
                }
            // }
        }
        if (isLeader)
        {
            String submissionData = "";
            for (AssignmentSubmission eachSub : allSubmissionsOut) {
                submissionData += "Submission ID: " + eachSub.getId() +
                                "\nSubmitter: " + eachSub.getSubmitter().getName() +
                                "\nFile Path: " + eachSub.getFilePath() +
                                "\nTime Submitted: " + eachSub.getTimeSubmitted() +
                                "\nSubmission Number: " + eachSub.getSubmissionNumber() + "\n";
            }
            return ResponseEntity.ok(submissionData);

        }
        else
        {
            return new ResponseEntity<>("Existing user is not a leader for the assignment", HttpStatus.BAD_REQUEST);
        }
        
    }

    @GetMapping("/showFilePreview")
    public ResponseEntity<Resource> showFilePreview(@RequestParam("assignmentID") long id, @RequestParam("submitter") String submitterEmail) {
        // Find the assignment
        Assignment assignment = repository.findById(id);

        // Check if the assignment exists
        if (assignment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Iterate through all submissions to find the one with the matching submitter's email
        for (AssignmentSubmission sub : assignment.getSubmissions()) {
            if (sub.getSubmitter().getEmail().equals(submitterEmail)) {
                File file = new File(sub.getFilePath());
                if (file.exists()) {
                    Resource resource = new FileSystemResource(file);
                    return new ResponseEntity<>(resource, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }

        // If no matching submission is found
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}