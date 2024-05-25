package ru.poldjoke.demo.jokebot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.poldjoke.demo.jokebot.model.Joke;

public interface JokesRepository extends JpaRepository<Joke, Long> {

  
    @Query("SELECT j FROM Jokes j ORDER BY RAND() LIMIT 1")
    Joke getRandomJoke();

 
    Page<Joke> getAllJokes(Pageable pageable);

   
    Page<Joke> getTopJokesByPopularity(Pageable pageable);
}
