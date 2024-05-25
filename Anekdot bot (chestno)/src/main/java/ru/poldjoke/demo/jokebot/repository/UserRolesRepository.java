package ru.poldjoke.demo.jokebot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.poldjoke.demo.jokebot.model.UserRole;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {}
