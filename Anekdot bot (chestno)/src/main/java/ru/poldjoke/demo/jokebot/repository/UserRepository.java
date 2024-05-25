package ru.poldjoke.demo.jokebot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.poldjoke.demo.jokebot.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

   
    User findByUsername(String username);
}
