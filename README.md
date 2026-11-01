# Task Tracker: PostgreSQL

REST API для управления задачами на базе Spring Boot и PostgreSQL

## Цель

Заменить хранение задач в List<Task> на PostgreSQL, сохранив существующий REST API

Стартовое состояние проекта — финальная версия предыдущего этапа.

Предыдущий этап: [task-tracker-02-tests-ci](https://github.com/konstantin-bushuev/task-tracker-02-tests-ci)

## Стек

* Java 21
* Spring Boot 4.1.1
* Gradle (Kotlin DSL)
* JUnit 5
* Mockito
* MockMvc
* GitHub Actions
* Spring Web MVC
* Spring Data JPA
* Hibernate
* PostgreSQL 16+
* Spring Boot Test


## План

* [x] `chore: initialize project`
* [x] `build: add PostgreSQL and Spring Data JPA dependencies`
* [x] `config: configure PostgreSQL datasource`
* [x] `feat: map Task as JPA entity`
* [x] `ci: add PostgreSQL service for tests`
* [x] `feat: replace in-memory repository with Spring Data JPA`
* [x] `refactor: update updateTask() and completeTask()`
* [x] `test: verify task persistence and retrieval`
* [x] `test: verify task updates and deletions`
* [x] `test: add PostgreSQL integration tests`
* [x] `test: configure database test environment`
* [x] `ci: run tests with PostgreSQL`
* [x] `docs: complete README.md`

## Запуск приложения

1. Перед запуском требуется:
  - Установить Java 21
  - Установить PostgreSQL 16 или новее
  - Настроить PostgreSQL
    - Настройки подключения к базе данных находятся в файле: src/main/resources/application.yaml
    - Если настройки локального PostgreSQL отличаются, изменить URL, имя пользователя и пароль в этом файле
2. Клонировать репозиторий

3. Запуск приложения:

```bash
./gradlew bootRun
```

Приложение подключится к PostgreSQL с использованием указанных настроек.

Приложение будет доступно по адресу http://localhost:8080

## Запуск тестов

```bash
./gradlew test
```
Для тестов используется отдельный файл конфигурации: src/test/resources/application.yaml

Тестовое окружение также использует PostgreSQL. 
Для управления схемой базы данных применяется стратегия Hibernate create-drop.

## Архитектура

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

## API

| Метод | Endpoint | Описание |
|---|---|---|
| GET | `/api/tasks` | Получить все задачи |
| GET | `/api/tasks/{id}` | Получить задачу по id |
| POST | `/api/tasks` | Создать задачу |
| PUT | `/api/tasks/{id}` | Обновить задачу |
| DELETE | `/api/tasks/{id}` | Удалить задачу |
| PATCH | `/api/tasks/{id}/complete` | Завершить задачу |

## Модель задачи

Каждая задача содержит следующие поля:

| Поле | Описание |
|---|---|
| `id` | Уникальный идентификатор задачи |
| `title` | Название задачи |
| `description` | Описание задачи |
| `status` | Текущий статус задачи |
| `createdAt` | Дата и время создания задачи |
| `updatedAt` | Дата и время последнего обновления |

Доступные статусы задач:

- `TODO` — ожидает выполнения
- `IN_PROGRESS` — выполняется
- `DONE` — выполнена

## Возможности

- Создание задач
- Получение списка всех задач
- Получение задачи по ID
- Обновление информации о задаче
- Отметка задачи как выполненной
- Удаление задач
- Хранение данных в PostgreSQL
- Валидация входящих запросов
- Обработка ошибок приложения
- Модульные, репозиторные, контроллерные и интеграционные тесты

## Стратегия тестирования

В проекте используются несколько уровней тестирования:

- Модульные тесты (Unit Tests) — проверяют бизнес-логику сервисного слоя с использованием Mockito.
- Тесты контроллеров (Controller Tests) — проверяют работу REST API с помощью MockMvc.
- Тесты репозиториев (Repository Tests) — проверяют операции сохранения и получения данных через Spring Data JPA.
- Интеграционные тесты (Integration Tests) — проверяют создание, получение, обновление и удаление задач через сервисный слой с использованием PostgreSQL.

## Непрерывная интеграция (CI)

GitHub Actions автоматически запускает тесты при отправке изменений в репозиторий и создании Pull Request.

Процесс CI включает следующие этапы:

1. Получение исходного кода репозитория.
2. Установка Java 21.
3. Запуск PostgreSQL 16 в качестве сервиса.
4. Выполнение команды Gradle для запуска тестов.

Конфигурация workflow находится в файле: .github/workflows/ci.yml

## Структура проекта

```text
src/main/java/io/github/konstantinbushuev/tasktracker03postgresql/
├── model
│   ├── Task.java                           # модель данных
│   └── TaskStatus.java                     # статусы задач   
├── dto
│   ├── TaskRequestDTO.java                 # входные данные для запросов
│   └── ErrorResponse.java                  # формат ошибок  
├── repository
│   └── TaskRepository.java                 # управление репозиторием
├── service
│   └── TaskService.java                    # бизнес-логика
├── controller
│   └── TaskController.java                 # HTTP endpoints
└── exception/                       
    ├── GlobalExceptionHandler.java         # глобальная обработка ошибок API
    └── TaskNotFoundException.java          # исключение для ненайденной задачи    
    
    
src/main/resources   
         └── application.yaml  
    
src/test/java/io/github/konstantinbushuev/tasktracker03postgresql/    
├── service
│   ├── TaskServiceTest.java                # TaskService unit tests  
│   └── TaskServiceIntegrationTest.java     # Integration tests  
├── controller
│   ├── TaskControllerTest.java             # TaskController integration tests
│   └── TaskControllerValidationTest.java   # Validation and error handling tests  
└── repository
    └── TaskRepositoryTest.java             # Repository tests   
    
src/test/resources   
         └── application.yaml      
        
```


## Следующий этап

[task-tracker-04-docker](https://github.com/konstantin-bushuev/task-tracker-04-docker)

Добавление Docker и Docker Compose

