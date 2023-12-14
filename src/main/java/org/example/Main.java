package org.example;


import org.example.pojo.Employee;
import org.example.pojo.EmployeeTask;
import org.example.service.EmployeeService;
import org.example.service.EmployeeTaskService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.example.service.EmployeeService.*;
import static org.example.service.EmployeeTaskService.*;


public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static SessionFactory sessionFactory;


    public static void main(String[] args) {
        configureSessionFactory();
        EmployeeService.setSessionFactory(sessionFactory);
        EmployeeTaskService.setSessionFactory(sessionFactory);


        // Создание записи о работнике
        Employee newEmployee = new Employee();
        newEmployee.setName("Юля");
        newEmployee.setSalary(53000);

        // Добавление записи о работнике
        addEmployee(newEmployee);

        // Изменение записи о работнике
        //newEmployee.setSalary(75000);
        //updateEmployee(newEmployee);

        // Получение работника по id
        Employee getEmployee = getIdEmployee(newEmployee.getId());
        System.out.println("Получение записи по id: " + getEmployee);

        // Получение списка работников
        List<Employee> allEmployees = getAllEmployees();
        System.out.println("Все записи:");
        for (Employee employee : allEmployees) {
            System.out.println(employee);
        }

        // Добавление задачи
        LocalDate deadline = LocalDate.of(2023, 12, 28);
        addTaskToEmployee(getEmployee, "Выполнить задачу 1", deadline);

        // Получение задачи по id
        printEmployeeTasks(getEmployee);

        // Получение всех записей с задачами
        printAllEmployeesWithTasks();

        // Получение записи по зарплате
        printSalaryEmployee();

        // Получение записи где id больше 1
        List<Employee> getId = printIdWhereGreaterThan();
        System.out.println("Получение записи где id больше 1: ");
        for (Employee employee : getId){
            System.out.println(employee);
        }
        // Удаление записи
          deleteEmployeeById(newEmployee.getId());

        // Закрытие sessionFactory
        sessionFactory.close();
    }

    private static void configureSessionFactory() {

        try{
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(EmployeeTask.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Ошибка при конфигурации sessionFactory ", e);
        }
    }
}






