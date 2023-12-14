package org.example.service;

import org.example.pojo.Employee;
import org.example.pojo.EmployeeTask;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.service.EmployeeService.getAllEmployees;

public class EmployeeTaskService {


    private static final Logger LOGGER = Logger.getLogger(EmployeeTaskService.class.getName());
    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory sessionFactory) {
        EmployeeTaskService.sessionFactory = sessionFactory;
    }

    public static void addTaskToEmployee(Employee employee, String taskName, LocalDate deadline) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee loadEmployee = session.get(Employee.class, employee.getId());

            EmployeeTask task = new EmployeeTask();
            task.setEmployee(loadEmployee);
            task.setTaskName(taskName);
            task.setDeadline(deadline);

            loadEmployee.getTasks().add(task); // Добавляем задачу к списку задач сотрудника

            session.saveOrUpdate(loadEmployee); // Сохраняем

            transaction.commit();
            System.out.println("Задача \"" + taskName + "\" успешно присвоена работнику " + loadEmployee.getName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при присвоении задачи работнику", e);
        }
    }


    public static void printEmployeeTasks(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Query taskQuery = session.createQuery("FROM EmployeeTask WHERE employee = :employeeTask", EmployeeTask.class);
            // Создается запрос taskQuery, который выбирает все объекты EmployeeTask, связанные с заданным сотрудником. Запрос использует именованный параметр :employeeTask, который будет заменен на конкретный сотрудник перед выполнением запроса.
            taskQuery.setParameter("employeeTask", employee);
            List<EmployeeTask> tasks = taskQuery.getResultList();
            System.out.println("Задачи работника " + employee.getName() + ":");
            for (EmployeeTask task : tasks) {
                System.out.println(task.getTaskName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении задач работника", e);
        }
    }

    public static void printAllEmployeesWithTasks() {
        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees) {
            System.out.println("Задачи работника " + employee.getName() + ":");
            for (EmployeeTask task : employee.getTasks()) {
                System.out.println(task.getTaskName());
            }
            System.out.println();
        }
    }

    public static void deleteAllEmployeesAndTasks() { // УДАЛЯЕТ ВСЕ
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Удаление всех задач
            String deleteTasks = "DELETE FROM EmployeeTask";
            session.createQuery(deleteTasks).executeUpdate();
            System.out.println("Удалены все записи из базы о задачах");

            // Удаление всех работников
            String deleteEmployees = "DELETE FROM Employee";
            session.createQuery(deleteEmployees).executeUpdate();
            System.out.println("Удалены все записи из базы о работниках");

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // откатит назад
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
