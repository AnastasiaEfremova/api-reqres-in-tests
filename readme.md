# 🚀 API Automation Framework для Reqres.in

![Reqres.in API Testing](./images/req.png)

> Проект автоматизированного тестирования REST API Reqres.in.

## 📋 О проекте

Проект представляет собой комплексный фреймворк для автоматизированного тестирования [Reqres.in API](https://reqres.in) - публичного REST API для тестирования, включающий:

- **REST API тестирование** всех доступных эндпоинтов
- **Положительные и отрицательные сценарии** тестирования
- **Параметризованные тесты** с различными наборами данных
- **Интеграционное тестирование** бизнес-сценариев
- **Продвинутую отчетность** с Allure Framework

---

## 📚 Содержание

- [Технологии и инструменты](#технологии-и-инструменты)
- [Архитектура тестов](#архитектура-тестов)
- [Тест-кейсы](#тест-кейсы)
- [Запуск тестов](#запуск-тестов)
- [Сборка в Jenkins](#-сборка-в-jenkins)
- [Allure отчет](#-allure-отчет)
- [Интеграция с TestOps](#-интеграция-с-testops)
- [Интеграция с Jira](#-интеграция-с-jira)
- [Телеграмм-бот с уведомлениями о результатах тестов](#-телеграмм-бот-с-уведомлениями-о-результатах-тестов)
- [Пример записи видео при выполнении тестов в Selenoid](#-пример-записи-видео-при-выполнении-тестов-в-selenoid)

---

<a id="технологии-и-инструменты"></a>
## 🛠 Технологии и инструменты

<p align="center">  
<a href="https://www.jetbrains.com/idea/"><img src="images/logo/Intelij_IDEA.svg" width="50" height="50"  alt="IDEA"/></a>  
<a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50"  alt="Java"/></a>  
<a href="https://github.com/"><img src="images/logo/Github.svg" width="50" height="50"  alt="Github"/></a>  
<a href="https://junit.org/junit5/"><img src="images/logo/JUnit5.svg" width="50" height="50"  alt="JUnit 5"/></a>  
<a href="https://gradle.org/"><img src="images/logo/Gradle.svg" width="50" height="50"  alt="Gradle"/></a>
<a href="https://qameta.io/"><img src="images/logo/Allure.png" width="50" height="50"  alt="Allure TestOps"/></a>   
<a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50"  alt="Jenkins"/></a>  
<a href="https://www.atlassian.com/ru/software/jira/"><img src="images/logo/Jira.png" width="50" height="50"  alt="Jira"/></a>  
</p>

---

<a id="архитектура-тестов"></a>
## 🏗 Архитектура тестов

```bash
src/test/java/
├── 📁 api/                          # API клиенты (HTTP слой)
│   ├── AuthApiClient.java          # 🔐 Аутентификация
│   ├── RegisterApiClient.java      # 📝 Регистрация  
│   └── UsersApiClient.java         # 👥 Управление пользователями
├── 📁 config/                      # ⚙️ Конфигурация
│   ├── ReqresConfig.java           # Интерфейс конфигурации
│   └── ReqresConfigProvider.java   # Singleton провайдер
├── 📁 helpers/                     # 🛠 Вспомогательные классы
│   └── CustomAllureListener.java   # Кастомные Allure листенеры
├── 📁 models/                      # 📊 Модели данных (DTO)
│   ├── login/                      # Модели для авторизации
│   ├── register/                   # Модели для регистрации
│   ├── createUser/                 # Создание пользователей
│   ├── updateUser/                 # Обновление пользователей
│   └── getUsers/                   # Получение пользователей
├── 📁 services/                    # 🏭 Бизнес-сервисы
│   ├── AuthService.java            # Сервис аутентификации
│   ├── RegisterService.java        # Сервис регистрации
│   └── UserService.java            # Сервис управления пользователями
├── 📁 specs/                       # 📋 Спецификации API
│   └── ApiSpecs.java               # Базовые Request/Response спецификации
├── 📁 tests/                       # 🧪 Тестовые классы
│   ├── AuthTests.java              # Тесты авторизации
│   ├── RegisterTests.java          # Тесты регистрации
│   ├── GetUsersTests.java          # Тесты чтения данных
│   └── UserWriteTests.java         # Тесты записи данных
└── 📁 utils/                       # 🎲 Утилиты
    └── TestDataGenerator.java      # Генератор тестовых данных
````


---

<a id="Покрытие функциональности"></a>

🔐 Авторизация (/api/login)
✅ Успешная авторизация с валидными учетными данными

✅ Параметризованная авторизация различных пользователей

✅ Негативные сценарии с неверными данными

✅ Валидация обязательных полей

📝 Регистрация (/api/register)
✅ Успешная регистрация предопределенных пользователей

✅ Негативные сценарии с неопределенными пользователями

✅ Валидация обязательных полей (email, password)

✅ Параметризованное тестирование граничных случаев

👥 Управление пользователями (/api/users)
✅ CRUD операции: Create, Read, Update, Delete

✅ Пагинация и получение списка пользователей

✅ Полное и частичное обновление данных

✅ Параметризованное тестирование с различными данными

✅ Валидация структуры ответов

---

<a id="запуск-тестов"></a>
## 🚀 Запуск тестов

### Локальный запуск всех тестов
```bash
# Все тесты
./gradlew clean test

# По категориям
./gradlew clean test -Dgroups="auth"
./gradlew clean test -Dgroups="registration" 
./gradlew clean test -Dgroups="users"
```

## <img width="4%" style="vertical-align:middle" title="Jenkins" src="images/logo/Jenkins.svg"> Сборка в Jenkins
[Сборка в Jenkins](https://jenkins.autotests.cloud/job/api-reqres-in-tests/)
<p align="center">
    <img title="Jenkins Build" src="images/BuildJenkins.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="images/logo/AllureReport.png"> Allure-отчет
[Allure отчет](https://jenkins.autotests.cloud/job/api-reqres-in-tests/allure/)
<p align="center">
    <img title="Allure Overview" src="images/AllureJenkins.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="images/logo/Allure.png"> Интеграция с TestOps
[Интеграция с TestOps](https://allure.autotests.cloud/project/4966/dashboards)
<p align="center">
    <img title="Allure Overview" src="images/TestOpsDashboards.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="images/logo/Jira.png"> Интеграция с Jira
[Интеграция с Jira](https://jira.autotests.cloud/browse/HOMEWORK-1517)
<p align="center">
    <img title="Jira Integration" src="images/JiraTask.png">
</p>

## <img width="4%" style="vertical-align:middle" title="Allure Report" src="images/logo/Telegram.png"> Телеграмм-бот с уведомлениями о результатах тестов
<p align="center">
<img width="70%" title="Telegram Notifications" src="images/TelegramBot.png">
</p>

