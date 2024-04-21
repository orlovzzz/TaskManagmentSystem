package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.views.Views;

import java.util.List;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String email;

    @JsonView({Views.ViewForAccounts.class, Views.ViewForTasks.class})
    private String password;

    @JsonView({Views.ViewForAccounts.class})
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "task_executors",
            joinColumns = @JoinColumn(name = "id_executor"),
            inverseJoinColumns = @JoinColumn(name = "id_task"))
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<Tasks> tasks;

}
