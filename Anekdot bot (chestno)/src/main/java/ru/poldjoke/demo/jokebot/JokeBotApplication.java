package ru.poldjoke.demo.jokebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JokeBotApplication {
	/*
	POST /jokes - body: id, text, date upload, date update
	GET /jokes - выдать все шутки
	GET /jokes/id выдать шутка с этим id
	PUT /jokes/id
	DELETE /jokes/id
	 */

	public static void main(String[] args) {
		SpringApplication.run(JokeBotApplication.class, args);
	}

}
