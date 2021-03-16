const errors = {
    PERSONAL_DATA_ALREADY_EXIST: "PERSONAL_DATA_ALREADY_EXIST",
    PASSPORT_ALREADY_EXIST: "PASSPORT_ALREADY_EXIST",
    USER_NAME_IS_TOO_SHORT: "USER_NAME_IS_TOO_SHORT",
    USER_NAME_IS_TOO_LONG: "USER_NAME_IS_TOO_LONG",
    USER_NAME_IS_INVALID: "USER_NAME_IS_INVALID",

    USER_SURNAME_IS_TOO_SHORT: "USER_SURNAME_IS_TOO_SHORT",
    USER_SURNAME_IS_TOO_LONG: "USER_SURNAME_IS_TOO_LONG",
    USER_SURNAME_IS_INVALID: "USER_SURNAME_IS_INVALID",

    PASSPORT_NUMBER_LENGTH: "PASSPORT_NUMBER_LENGTH",
    PASSPORT_CONTAINS_WRONG_SYMBOLS: "PASSPORT_CONTAINS_WRONG_SYMBOLS",

    USER_AGE_IS_UNDER_14: "USER_AGE_IS_UNDER_14",

    INVALID_LOGIN: "INVALID_LOGIN",
    INVALID_PASSWORD: "INVALID_PASSWORD",
    USER_LOGIN_ALREADY_EXIST: "USER_LOGIN_ALREADY_EXIST",

    MVD_PERSONAL_DATA_DOESNT_EXIST: "MVD_PERSONAL_DATA_DOESNT_EXIST",
    MVD_TERRORIST_ERROR: "MVD_TERRORIST_ERROR",
    MVD_TIME_OUT_ERROR: "MVD_TIME_OUT_ERROR",

    text: text = {
        PERSONAL_DATA_ALREADY_EXIST: "Персональные данные уже добавлены.",
        PASSPORT_ALREADY_EXIST: "Пользователь с таким паспортном уже зарегистрирован, " +
            "обратитесь в поддержку по номеру телефона +7 914 151 56 77.",
        USER_NAME_IS_TOO_SHORT: "Имя пользователя должно содержать минимум один символ",
        USER_NAME_IS_TOO_LONG: "Имя пользователя должно содержать не более 40 символов",
        USER_NAME_IS_INVALID: "Имя пользоватлеля может содержать буквы русского алфавита, " +
            "а также символы пробела и - ." +
            "Имя пользователя должно начинаться со строчной или прописной буквы.",

        USER_SURNAME_IS_TOO_SHORT: "Фамилия пользователя должна содержать минимум один символ",
        USER_SURNAME_IS_TOO_LONG: "Фамилия пользователя должна содержать не более 40 символов",
        USER_SURNAME_IS_INVALID: "Фамилия пользоватлеля может содержать буквы русского алфавита, " +
            "а также символы пробела и  - ." +
            "Имя пользователя должно начинаться со строчной или прописной буквы.",

        PASSPORT_NUMBER_LENGTH: "Проверьте корректность номера паспорта",
        PASSPORT_CONTAINS_WRONG_SYMBOLS: "Номер паспорта содержит недопустимые символы",

        USER_AGE_IS_UNDER_14: "Пользователи малдше 14 лет, не могут пользоваться услугами нашего банка.",

        INVALID_LOGIN: "INVALID_LOGIN",
        INVALID_PASSWORD: "INVALID_PASSWORD",
        USER_LOGIN_ALREADY_EXIST: "USER_LOGIN_ALREADY_EXIST",

        MVD_PERSONAL_DATA_DOESNT_EXIST: "MVD_PERSONAL_DATA_DOESNT_EXIST",
        MVD_TERRORIST_ERROR: "MVD_TERRORIST_ERROR",
        MVD_TIME_OUT_ERROR: "MVD_TIME_OUT_ERROR",
    }
}