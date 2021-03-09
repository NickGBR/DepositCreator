package ru.interns.deposit.db.temprorary;

import ru.interns.deposit.external.mvd.dto.MvdResultCheckingDTO;

import java.util.HashMap;
import java.util.Map;

public class MvdStatus {
    // В это место кладем ответ от мвд сервиса. key - userLogin
    public static Map<String ,MvdResultCheckingDTO> mvdCheckResult = new HashMap<>();
}
