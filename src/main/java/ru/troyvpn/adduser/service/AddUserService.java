package ru.troyvpn.adduser.service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class AddUserService {
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
            return "IpsecSecret added successfully";

    }
    public String generatePassword(int length) {
        return RANDOM.ints(length, 0, SYMBOLS.length())
                .mapToObj(SYMBOLS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
