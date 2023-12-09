package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.views.Views;

import java.util.List;

@Entity
@Getter
@Setter
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String header;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String descript;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String status;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String priority;

    @JsonIgnore
    @Schema(hidden = true)
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_author")
    private Account author;

    @JsonView(Views.ViewForTasks.class)
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "task_executors",
            joinColumns = @JoinColumn(name = "id_task"),
            inverseJoinColumns = @JoinColumn(name = "id_executor"))
    private List<Account> executors;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "task_comments",
            joinColumns = @JoinColumn(name = "id_task"),
            inverseJoinColumns = @JoinColumn(name = "id_comment")
    )
    private List<Com> comments;
}