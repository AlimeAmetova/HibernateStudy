package org.example.service;

import org.example.pojo.Employee;
import org.example.pojo.EmployeeTask;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeService {
    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());
    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory sessionFactory) {
        EmployeeService.sessionFactory = sessionFactory;
    }

    public static void addEmployee(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении employee", e);
        }
    }

    public static void updateEmployee(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(employee);
            transaction.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при изменении employee", e);
        }
    }

    public static Employee getIdEmployee(int id) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.get(Employee.class, id);
            Hibernate.initialize(employee.getTasks());
            return employee;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении id employee", e);
            return null;
        }
    }


    public static List<Employee> getAllEmployees() {
        try (Session session = sessionFactory.openSession()) {
            List<Employee> employees = session.createQuery("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.tasks", Employee.class).list();
            return employees;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении всех id employee", e);
            return null;
        }
    }
// Ошибка, при получении списка всех сотрудников и их задач выполняются отдельные запросы к базе данных для каждого сотрудника.
// Чтобы исправить это, нужно использовать JOIN FETCH в запросе, чтобы одновременно загрузить сотрудников и их задач

    public static void deleteEmployeeById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);

            if (employee != null) {
                // Удаляем все задачи, связанные с работником
                for (EmployeeTask task : employee.getTasks()) {
                    session.remove(task);
                }

                session.remove(employee);
                transaction.commit();
                System.out.println("Работник с id " + id + " успешно удален");
            } else {
                System.out.println("Работник с id " + id + " не найден");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении employee по id", e);
        }
    }
// второстепенные методы 
    public static void printSalaryEmployee(){
        try (Session session = sessionFactory.openSession()) {
            List<Object[]> resultList = session.createQuery("SELECT name, salary FROM Employee", Object[].class).list();
            System.out.println("Информация о зарплате:");
            for (Object[] result : resultList) {
                System.out.println("Имя: " + result[0] + ", Зарплата: " + result[1]);
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении информации о зарплате", e);
        }
    }

    public static List<Employee> printIdWhereGreaterThan(){
        try (Session session = sessionFactory.openSession()){
            return session.createQuery("from Employee where id > 1", Employee.class).list();
        }
    }
}
