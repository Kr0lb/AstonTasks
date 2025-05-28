package org.example;

import org.example.config.ExceptionHandler;
import org.example.config.HibernateUtils;
import org.example.entity.User;
import org.example.repository.impl.UserDAOImpl;
import org.hibernate.Session;

import java.util.Scanner;

public class HibernateApplication {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Session session = HibernateUtils.getSessionFactory().openSession();

        UserDAOImpl rep = new UserDAOImpl(session);

        while (true) {
            System.out.print("""
                    1 - просмотреть всех пользователей
                    2 - создать нового пользователя
                    3 - изменить данные пользователя
                    4 - удалить пользователя
                    5 - найти пользователя по id
                    0 - выход
                    Выберите действие:
                    """);

            switch (input.next()) {
                case "1" -> ExceptionHandler.runSafe(() -> System.out.println(rep.findAll()));
                case "2" -> ExceptionHandler.runSafe(() -> {
                    User user = new User();
                    System.out.print("Введите имя:");
                    user.setName(input.next());
                    System.out.print("Введите email:");
                    user.setEmail(input.next());
                    System.out.print("Введите возраст:");
                    user.setAge(Integer.parseInt(input.next()));
                    rep.save(user);
                    System.out.println("Пользователь был сохранен");
                });
                case "3" -> ExceptionHandler.runSafe(() -> {
                    System.out.print("Введите id пользователя для изменения данных:");
                    User user = rep.findById(Long.parseLong(input.next()));
                    System.out.println("""
                            1 - имя
                            2 - email
                            3 - возраст
                            Выберете параметр для изменения:
                            """);
                    switch (input.next()) {
                        case "1" -> user.setName(input.next());
                        case "2" -> user.setEmail(input.next());
                        case "3" -> user.setAge(Integer.parseInt(input.next()));
                        default -> System.out.println("Команда не распознан");
                    }
                    rep.update(user);
                    System.out.println("Данные пользователя обновлены");
                });
                case "4" -> ExceptionHandler.runSafe(() -> {
                    System.out.print("Введите id пользователя:");
                    rep.delete(Long.parseLong(input.next()));

                });
                case "5" -> ExceptionHandler.runSafe(() -> {
                    System.out.print("Введите id пользователя:");
                    System.out.println(rep.findById(Long.parseLong(input.next())));
                });
                case "0" -> {
                    System.out.println("Сессия завершена");
                    session.close();
                    return;
                }
                default -> System.out.println("Команда не распознана!");
            }
        }
    }
}