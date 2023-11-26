package ru.troyvpn.adduser.service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@";


    public String addUser(String username) {
        String password = generatePassword(6);
        try (FileWriter writer = new FileWriter("/etc/ipsec.secrets", true)) {
            writer.write(username +
                    " : EAP \"" + password + "\"");
        } catch (IOException e) {
            return "Ошибка при записи";
        }
        return password;
    }
    public String getPasswordByUsername(String username) {
        Path path = Paths.get("/etc/ipsec.secrets");

        // Читаем все строки из файла
        try {
            List<String> lines = Files.readAllLines(path);

            // Создаем регулярное выражение для поиска пароля в строке
            String regex = "\\b" + username + "\\s*:\\s*EAP\\s*\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);

            // Ищем соответствие регулярному выражению в каждой строке
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Возвращаем null, если пользователь не найден или пароль не может быть извлечен
        return null;
    }
//    public String getPasswordByUsername(String username) {
//        Path path = Paths.get("/etc/ipsec.secrets");
//        try {
//            // Читаем все строки из файла
//            List<String> lines = Files.readAllLines(path);
//
//            // Ищем строку, содержащую указанное имя пользователя
//            for (String line : lines) {
//                if (line.contains(username)) {
//                    int startIndex = line.indexOf("\"");
//                    int endIndex = line.lastIndexOf("\"");
//                    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
//                        return line.substring(startIndex + 1, endIndex);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Возвращаем null, если пользователь не найден или пароль не может быть извлечен
//        return null;
//    }

    public String removeUser(String username) {
        String filePath = "/etc/ipsec.secrets";
        Path path = Paths.get(filePath);

        // Читаем все строки из файла в список
        try {
            backupFile(filePath);
            List<String> lines = Files.readAllLines(path);

            // Фильтруем строки, оставляя только те, которые не содержат указанное имя пользователя
            List<String> filteredLines = lines.stream()
                    .filter(line -> !line.contains(username))
                    .collect(Collectors.toList());

            // Перезаписываем файл с отфильтрованными строками
            Files.write(path, filteredLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Пользователь удален";
    }

    private void backupFile(String filePath) throws IOException {
        Path sourcePath = Paths.get(filePath);
        Path backupPath = Paths.get("/home/zelim/backup/ipsec.conf" + ".bak");

        // Копируем файл в резервный файл
        Files.copy(sourcePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Backup created: " + backupPath);
    }

    public String generatePassword(int length) {
        return RANDOM.ints(length, 0, SYMBOLS.length())
                .mapToObj(SYMBOLS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
