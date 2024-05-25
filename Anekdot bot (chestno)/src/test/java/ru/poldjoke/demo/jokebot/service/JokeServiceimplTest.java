package ru.poldjoke.demo.jokebot.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.poldjoke.demo.jokebot.model.Joke;
import ru.poldjoke.demo.jokebot.repository.JokesRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Test JokeServiceimpl")
class JokeServiceimplTest {

    @MockBean
    private JokesRepository jokesRepository;

    @Autowired
    private JokeServiceimpl jokeService;

    @BeforeEach
    void setUp() {
        jokeService = new JokeServiceimpl(jokesRepository);
    }

    @Nested
    @DisplayName("Test register new joke")
    class RegisterJokeTests {

        @Test
        @DisplayName("Successfully register a new joke")
        void successfullyRegisterJoke() {
            // Create a joke
            Joke joke = new Joke("Test joke", new Date());

            // Perform the registration
            jokeService.registerJoke(joke);

            // Verify that the repository was called with the joke
            ArgumentCaptor<Joke> jokeCaptor = ArgumentCaptor.forClass(Joke.class);
            verify(jokesRepository, times(1)).save(jokeCaptor.capture());

            // Assertions
            assertEquals(joke, jokeCaptor.getValue());
        }
    }

    @Nested
    @DisplayName("Test get all jokes")
    class GetAllJokesTests {

        @Test
        @DisplayName("Successfully get all jokes")
        void successfullyGetAllJokes() {
            // Perform the get all operation
            jokeService.getAllJokes(Pageable.unpaged());

            // Verify that the repository was called
            verify(jokesRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Test get joke by ID")
    class GetJokeByIdTests {

        @Test
        @DisplayName("Successfully get joke by ID")
        void successfullyGetJokeById() {
            // Create a joke
            Joke joke = new Joke("Test joke", new Date());
            when(jokesRepository.findById(anyLong())).thenReturn(Optional.of(joke));

            // Perform the get operation
            jokeService.getJokeById(1L);

            // Verify that the repository was called
            verify(jokesRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Unsuccessfully get joke by ID")
        void unsuccessfullyGetJokeById() {
            // Perform the get operation
            when(jokesRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Assertions
            assertThrows(IllegalArgumentException.class, () -> jokeService.getJokeById(1L));
        }
    }

    @Nested
    @DisplayName("Test delete joke by ID")
    class DeleteJokeByIdTests {

        @Test
        @DisplayName("Successfully delete joke by ID")
        void successfullyDeleteJokeById() {
            // Perform the delete operation
            jokeService.deleteJokeById(1L);

            // Verify that the repository was called
            verify(jokesRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Test get random joke")
    class GetRandomJokeTests {

        @Test
        @DisplayName("Successfully get random joke")
        void successfullyGetRandomJoke() {
            // Create a joke
            Joke joke = new Joke("Test joke",new Date());
            when(jokesRepository.findRandomJoke()).thenReturn(joke);

            // Perform the get operation
            jokeService.getRandomJoke();

            // Verify that the repository was called
            verify(jokesRepository, times(1)).findRandomJoke();
        }
    }

    @Nested
    @DisplayName("Test update joke by ID")
    class UpdateJokeByIdTests {

        @Test
        @DisplayName("Successfully update joke by ID")
        void successfullyUpdateJokeById() {
            // Create a joke
            Joke joke = new Joke("Test joke", new Date());
            Joke updatedJoke = new Joke("Updated joke", new Date());
            when(jokesRepository.findById(anyLong())).thenReturn(Optional.of(joke));

            // Perform the update operation
            jokeService.updateJokeById(1L, updatedJoke);

            // Verify that the repository was called
            ArgumentCaptor<Joke> jokeCaptor = ArgumentCaptor.forClass(Joke.class);
            verify(jokesRepository, times(1)).save(jokeCaptor.capture());

            // Assertions
            assertEquals(updatedJoke, jokeCaptor.getValue());
        }

        @Test
        @DisplayName("Unsuccessfully update joke by ID")
        void unsuccessfullyUpdateJokeById() {
            // Perform the update operation
            when(jokesRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Assertions
            assertThrows(IllegalArgumentException.class, () -> jokeService.updateJokeById(1L, any(Joke.class)));
        }
    }
}
