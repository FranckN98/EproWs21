package de.thbingen.epro21.controller;

import de.thbingen.epro21.model.CompanyObjective;
import de.thbingen.epro21.repository.CompanyObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/objectives")
public class ObjectiveController {

    @Autowired
    private final CompanyObjectiveRepository objectiveRepository;

    public ObjectiveController(CompanyObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    @GetMapping("/greeting")
    public String getGreat()
    {
            return "hello i great you.";
    }


    //add objective
    @PostMapping("/add")
    public CompanyObjective save(@RequestBody CompanyObjective objective) {
        return this.objectiveRepository.save(objective);
    }

    // get objectives
    @GetMapping("/all")
    public List<CompanyObjective> getAll()
    {
        return this.objectiveRepository.findAll();
    }
}
