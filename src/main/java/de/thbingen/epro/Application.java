package de.thbingen.epro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping
    public String getTest() {
        return "YAY";
    }

    //open to everyone with any Role
    @GetMapping("/something")
    public String getSomething() { return "Something";}

    //restricted, normal users cannot access
    @GetMapping("restricted")
    public String getRestricted() {return "Restricted";}

    //restricted, only accessible to CO Admin
    @GetMapping("/administration")
    public String getAdministration() {return "Administration";}
}
