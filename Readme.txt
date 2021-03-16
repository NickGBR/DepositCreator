Инструкция по настройки приложения deposit creator.

1. скачать 13-ю версию PostgresSql https://www.postgresql.org/download/
2. установить скаченный файл, при настройке установить пароль postgres, порт оставить по умолчанию
3. установить Intellij idea, c jdk 8.
4. в Intellij idea выбрать File-> New -> Project from version control
5. Указать в поле URL - https://github.com/NickGBR/DepositCreator
6. Нажать кнопу Clone
7. Idea скачает проект и все необходимые зависимости
8. Установите Lombok plugin File->Settings->Plugins.
9. Запустите панель управления postgres. По умолчанию "C:\Program Files\PostgreSQL\13\pgAdmin 4\bin\pgAdmin4.exe"
10. Создайте базу данных deposit
11. В Idea выбираем ветку develop и запускаем приложение
12. Заходим на http://localhost:8080/api/v1/view/login
13. Для заполнения базы персональных данных, открываем плагин flyway
14. Нажимаем clean
15. Запускае проект, в процессе запуска создадутся необходимые таблицы в Бд
16. Открывем плагин flyway, нажимаем baseline, затем migrate
17. Скачать и установть ActiveMq https://activemq.apache.org/components/classic/download/