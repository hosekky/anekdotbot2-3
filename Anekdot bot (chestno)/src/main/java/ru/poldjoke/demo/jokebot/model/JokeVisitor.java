package ru.poldjoke.demo.jokebot.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity(name = "Jokes")
@Table(name = "Jokes")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Joke {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "joke_seq_gen")
    @SequenceGenerator(name = "joke_seq_gen", sequenceName = "joke_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @OneToMany(mappedBy = "jokeId", cascade = CascadeType.ALL)
    private List<JokeVisitor> jokeVisitors;

    public Joke(String text, Date createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }
}
