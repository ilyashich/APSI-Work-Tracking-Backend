package com.apsiworktracking.apsiworktracking.controller;

import com.apsiworktracking.apsiworktracking.model.Person;
import com.apsiworktracking.apsiworktracking.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="https://ashy-ground-0223e9e03.1.azurestaticapps.net")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PersonController
{
    private final PersonService personService;

    @GetMapping("/persons")
    public List<Person> getPersons()
    {
        return personService.getPersons();
    }

    @GetMapping("/persons/{id}")
    public Person getPerson(@PathVariable("id") Long id)
    {
        return personService.getPerson(id);
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person)
    {
        return personService.createPerson(person);
    }

    @PutMapping("/persons/{id}")
    public Person updatePerson(@PathVariable("id") Long id, @RequestBody Person person)
    {
        return personService.updateUser(id, person);
    }

    @DeleteMapping("/persons/{id}")
    public void deletePerson(@PathVariable("id") Long id)
    {
        personService.deletePerson(id);
    }
}
