package com.nighthawk.spring_portfolio.mvc.textAI;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextsJpaRepository extends JpaRepository<Text, Long> {
    /* JPA has many built in methods: https://www.tutorialspoint.com/spring_boot_jpa/spring_boot_jpa_repository_methods.htm
    The below custom methods are prototyped for this application
    */
    void save(String word);
    Text findByName(String name);
}
