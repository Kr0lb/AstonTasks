package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.config.HibernateUtils;
import org.example.entity.User;
import org.example.repository.impl.UserDAOImpl;

import java.util.Scanner;

@Slf4j
public class HibernateApplication {

    private static final Scanner input = new Scanner(System.in);
    private static UserDAOImpl rep;

    public static void main(String[] args) {

        rep = new UserDAOImpl(HibernateUtils.getSessionFactory());

        while (true) {
            System.out.print("""
                    1 - просмотреть всех пользователей
                    2 - создать нового пользователя
                    3 - изменить данные пользователя
                    4 - удалить пользователя
                    5 - найти пользователя по id
                    0 - выход
                    Выберите действие:""");

            switch (input.next()) {
                case "1" -> {
                    for (User user : rep.findAll())
                        System.out.println(user);
                }
                case "2" -> {
                    try {
                        createUser();
                    } catch (Exception e) {
                        System.err.println("Сохранение было прервано");
                        break;
                    }
                    System.out.println("Пользователь успешно сохранен");
                }
                case "3" -> {
                    try {
                        updateUser();
                    } catch (Exception e) {
                        System.err.println("Обновление было прервано");
                        break;
                    }
                    System.out.println("Данные пользователя обновлены");
                }
                case "4" -> {
                    try {
                        deleteUser();
                    } catch (Exception e) {
                        System.err.println("Удаление было прервано");
                        break;
                    }
                    System.out.println("Пользователь успешно удален");
                }
                case "5" -> {
                    try {
                        System.out.print("Введите id пользователя:");
                        System.out.println(rep.findById(Long.parseLong(input.next())));
                    } catch (NumberFormatException e) {
                        System.err.println("Неправильный ввод id пользователя");
                        log.error("Number conversion error: {}", e.getMessage());
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                case "0" -> {
                    System.out.println("Сессия завершена");
                    HibernateUtils.shutdown();
                    return;
                }
                default -> System.out.println("Команда не распознана!");
            }
        }
    }

    public static void createUser() {
        try {
            User user = new User();
            System.out.print("Введите имя:");
            user.setName(input.next());
            System.out.print("Введите email:");
            user.setEmail(input.next());
            System.out.print("Введите возраст:");
            user.setAge(Integer.parseInt(input.next()));
            rep.save(user);
        } catch (NumberFormatException e) {
            System.err.println("Неправильный ввод возраста");
            log.error("Number conversion error: {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    public static void updateUser() {
        try {
            boolean stop = false;
            System.out.print("Введите id пользователя для изменения данных:");
            User user = rep.findById(Long.parseLong(input.next()));
            System.out.println("""
                    1 - имя
                    2 - email
                    3 - возраст
                    0 - завершить
                    Выберете параметр для изменения:""");
            while (!stop) {
                switch (input.next()) {
                    case "1" -> {
                        System.out.print("Введите имя:");
                        user.setName(input.next());
                    }
                    case "2" -> user.setEmail(input.next());
                    case "3" -> user.setAge(Integer.parseInt(input.next()));
                    case "0" -> stop = true;
                    default -> System.out.println("Команда не распознан");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Неправильный ввод числа");
            log.error("Number conversion error: {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    public static void deleteUser() {
        try {
            System.out.print("Введите id пользователя:");
            rep.delete(Long.parseLong(input.next()));
        } catch (NumberFormatException e) {
            System.err.println("Неправильный ввод id пользователя");
            log.error("Number conversion error: {}", e.getMessage());
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}