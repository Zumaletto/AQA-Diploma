## Дипломный проект профессии "Тестировщик"
___

### Документация по проекту
1. [План тестирования](https://github.com/Zumaletto/AQA-Diploma/blob/master/Plan.md) 
2. Отчет по итогам автоматизированного тестирования
3. Отчет по итогам автоматизации

### Запуск приложения

Для запуска приложения необходим **Docker** или **Docker Toolbox**.

**Примечание**: Приложение запускалось через Docker на вирутальной машине. По-этому, при написании сетевых адресов в файле "application.properties", вместо "localhost:" прописано "185.119.57.164:", что является IP-адресом виртуальной машины, на которой работает Docker.

* склонировать репозиторий ```https://github.com/Zumaletto/AQA-Diploma.git```
* переключиться в папку AQA-Diploma
* проверить есть ли образы MySql, PostgreSQL и Node.js командой ```docker image ls```
* скачать нужный образ командой ```docker image pull <имя образа>```
* запустить docker container ```docker-compose up -d```. Дождаться пока контейнеры запустятся
* в терминале IntelliJ IDEA запустить SUT:
    - с использованием БД MySQL командой ```java "-Dspring.datasource.url=jdbc:mysql://185.119.57.164:3306/app" -jar artifacts/aqa-shop.jar```
    - с использованием БД PostgreSQL командой ```java "-Dspring.datasource.url=jdbc:postgresql://185.119.57.164:5432/app" -jar artifacts/aqa-shop.jar```
* запустить автотесты командой:
  - для конфигурации БД MySql:  
    ```gradlew clean test -Ddb.url=jdbc:mysql://185.119.57.164:3306/app ```
  - для конфигурации БД PostgreSQL:  
    ```gradlew clean test -Ddb.url=jdbc:postgresql://185.119.57.164:5432/app ```
* запустить отчеты командой:
```./gradlew allureReport (первоначальная команда)```

```./gradlew allureServe (запуск и открытие отчетов)```
* остановить SUT комбдинацией клавиш ```CTRL+C```

* Остановить контейнеры командой ```docker-compose stop``` и после удалить контейнеры командой
```docker-compose down```