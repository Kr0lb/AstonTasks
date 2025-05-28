package org.example.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandler {
    public static void runSafe(Runnable runnable) {
        try {
            runnable.run();
        } catch (NumberFormatException e) {
            log.error("Ошибка преобразования числа: {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Объект не найден: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getMessage());
        }
    }
}
