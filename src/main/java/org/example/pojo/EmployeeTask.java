package org.example.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="task")
@Data
public class EmployeeTask {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="name")
    private String taskName;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name="deadline")
    private LocalDate deadline;

}
