package com.nighthawk.spring_portfolio.mvc.qrCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.nighthawk.spring_portfolio.mvc.person.*;
import com.nighthawk.spring_portfolio.mvc.assignment.*;

/*
This class has an instance of Java Persistence API (JPA)
-- @Autowired annotation. Allows Spring to resolve and inject collaborating beans into our bean.
-- Spring Data JPA will generate a proxy instance
-- Below are some CRUD methods that we can use with our database
*/
@Service
@Transactional
public class QrCodeDetailsService implements UserDetailsService {  // "implements" ties ModelRepo to Spring Security
    // Encapsulate many object into a single Bean (Person, Roles, and Scrum)
    @Autowired  // Inject PersonJpaRepository
    private QrCodeJpaRepository QrCodeJpaRepository;
    @Autowired  // Inject RoleJpaRepository
    private PersonJpaRepository personJpaRepository;

    /* UserDetailsService Overrides and maps Person & Roles POJO into Spring Security */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Person person = personJpaRepository.findByEmail(email); // setting variable user equal to the method finding the username in the database
        // if(person==null) {
		// 	throw new UsernameNotFoundException("User not found with username: " + email);
        // }
        // Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // person.getRoles().forEach(role -> { //loop through roles
        //     authorities.add(new SimpleGrantedAuthority(role.getName())); //create a SimpleGrantedAuthority by passed in role, adding it all to the authorities list, list of roles gets past in for spring security
        // });
        // // train spring security to User and Authorities
        // return new org.springframework.security.core.userdetails.User(person.getEmail(), person.getPassword(), authorities);
        return null;
    }


    public void addQrCodeToUser(String personEmail, Long qrCodeId) { // by passing in the two strings you are giving the class that certain leader
        Person person = personJpaRepository.findByEmail(personEmail);
        if (person != null) {   // verify person
            Optional<QrCode> qrCode = QrCodeJpaRepository.findById(qrCodeId);
            if (qrCode != null) { // verify role
                person.getQrCodes().add(qrCode.get());
            }
        }
    }
}