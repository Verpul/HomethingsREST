package ru.homethings.overall.controllers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.homethings.overall.model.Promise;
import ru.homethings.overall.repositories.PromiseRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/overall/promise", produces = "application/json")
public class PromisesController {

    private PromiseRepository promiseRepository;

    public PromisesController(PromiseRepository promiseRepository) {
        this.promiseRepository = promiseRepository;
    }

    @GetMapping
    public Iterable<Promise> getPromisesList() {
        return promiseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promise> getPromiseById(@PathVariable("id") Long id) {
        Optional<Promise> optionalPromise = promiseRepository.findById(id);
        return optionalPromise.map(promise -> new ResponseEntity<>(promise, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Promise createPromise(@RequestBody Promise promise) {
        return promiseRepository.save(promise);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Promise editPromise(@PathVariable("id") Long id,
                               @RequestBody Promise promise) {
        promise.setId(id);
        return promiseRepository.save(promise);
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePromise(@PathVariable("id") Long id) {
        try {
            promiseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {}
    }
}
