package ru.poldjoke.demo.jokebot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.poldjoke.demo.jokebot.model.Joke;
import ru.poldjoke.demo.jokebot.repository.JokesRepository;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JokeServiceimpl implements JokeService {
    private final JokesRepository jokesRepository;

    @Override
    public Page<Joke> getJokes(Pageable pageable) {
        return jokesRepository.findAll(pageable);
    }

    @Override
    public Page<Joke> getTopJokes(Pageable pageable) {
        return jokesRepository.findByOrderByJokeVisitorDesc(pageable);
    }

    @Override
    public void registerJoke(Joke joke) {
        jokesRepository.save(joke);
    }

    @Override
    public Optional<Joke> getJokeById(Long id) {
        return jokesRepository.findById(id);
    }

    @Override
    public boolean deleteJokeById(Long id) {
        Optional<Joke> joke = jokesRepository.findById(id);
        if (joke.isPresent()) {
            jokesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Joke getRandomJoke() {
        return jokesRepository.findRandomJoke();
    }

    @Override
    public void updateJokeById(Long id, Joke updatedJoke) {
        Optional<Joke> existingJoke = jokesRepository.findById(id);
        if (existingJoke.isPresent()) {
            existingJoke.get().setText(updatedJoke.getText());
            existingJoke.get().setUpdatedAt(new Date());
            jokesRepository.save(existingJoke.get());
        }
    }
}
