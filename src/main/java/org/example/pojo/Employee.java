package org.example.pojo;

import lombok.Data;
import org.hibernate.Session;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;


@Entity
@Table(name = "employee")
@Data
public class Employee {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="salary")
    private Integer salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeTask> tasks = new ArrayList<>();

    // Атрибут mappedBy = "employee" указывает, что связь между Employee и EmployeeTask устанавливается через поле employee в классе EmployeeTask. Это означает, что поле employee в классе
    // EmployeeTask является владельцем связи и содержит информацию о связанном объекте Employee.

    // Атрибут orphanRemoval = true указывает, что при удалении связанного объекта EmployeeTask, он должен быть автоматически удален из коллекции tasks в объекте Employee.
    // Это обеспечивает автоматическую синхронизацию коллекции tasks с базой данных.


    @Override
    public String toString() {
        return "Employee(id=" + id + ", name=" + name + ", salary=" + salary + ")";
    }

    // переполнение стека (StackOverflowError) из-за циклической зависимости при попытке преобразовать объект EmployeeTask в строку в методе toString().
}
