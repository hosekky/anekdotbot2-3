package ru.poldjoke.demo.jokebot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.poldjoke.demo.jokebot.model.Joke;

import java.util.Optional;

public interface JokeService {

    Page<Joke> getAllJokes(Pageable pageable);

  
    Page<Joke> getTopJokes(Pageable pageable);


    void createJoke(Joke joke);

 
    Optional<Joke> getJokeById(Long id);

  
    boolean deleteJokeById(Long id);


    Joke getRandomJoke();

    void updateJokeById(Long id, Joke updatedJoke);
}
