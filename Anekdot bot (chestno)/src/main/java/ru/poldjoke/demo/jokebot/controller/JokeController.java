package ru.poldjoke.demo.jokebot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.poldjoke.demo.jokebot.model.Joke;
import ru.poldjoke.demo.jokebot.service.JokeService;

@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class JokeController {

    private final JokeService jokeService;

    @GetMapping
    public ResponseEntity<Page<Joke>> getAllJokes(Pageable pageable) {
        return ResponseEntity.ok(jokeService.getAllJokes(pageable));
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Joke>> getTopJokes(Pageable pageable) {
        return ResponseEntity.ok(jokeService.getTopJokes(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> createJoke(@RequestBody Joke joke) {
        joke.setCreatedAt(new Date());
        jokeService.createJoke(joke);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joke> getJokeById(@PathVariable Long id) {
        return jokeService.getJokeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJokeById(@PathVariable Long id) {
        if (jokeService.deleteJokeById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateJoke(@PathVariable Long id, @RequestBody Joke updatedJoke) {
        jokeService.updateJokeById(id, updatedJoke);
        return ResponseEntity.ok().build();
    }
}
