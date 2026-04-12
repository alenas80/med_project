🧠 Medical Multimodal AI Application
📌 Описание проекта
Данное приложение представляет собой backend-систему для обработки мультимодальных медицинских данных, реализованную на основе Spring Boot, Spring AI и Ollama.
Основная цель проекта — построение AI-пайплайна, который:
принимает MRI-данные (.nii.gz);
обрабатывает их через workflow;
преобразует в embeddings;
сохраняет в vector database (pgvector);
выполняет RAG (вопрос-ответ);
оценивает качество ответов.
🎯 Функциональность

Приложение реализует:

загрузку MRI-файлов через REST API;
обработку данных через pipeline (workflow);
генерацию embeddings;
сохранение в vector store;
поиск по контексту;
генерацию ответов на основе контекста (RAG);
оценку качества ответов:
релевантность контекста;
точность ответа;
релевантность ответа.


🏗 Архитектура

MRI (.nii.gz)
   ↓
Validate
   ↓
Parse (MriParser)
   ↓
Normalize
   ↓
Embedding (Ollama)
   ↓
Vector Store (pgvector)
   ↓
RAG (ChatClient)
   ↓
Evaluation


🧩 Структура проекта

src/main/java/com/example/med_project/
├── MedProjectApplication.java
├── config/               # конфигурация
├── controller/           # REST API
├── model/                # DTO и контекст пайплайна
├── modality/parser/      # парсеры данных (MRI)
├── embedding/            # генерация embeddings
├── vectorstore/          # работа с vector DB
├── workflow/             # pipeline (sequence pattern)
├── rag/                  # retrieval + answer generation
├── evaluation/           # шкала оценок
└── util/                 # утилиты

src/test/java/com/example/med_project/
├── ContextRelevancyTest.java
├── AnswerAccuracyTest.java
└── AnswerRelevancyTest.java

⚙️ Технологии
Java 21
Spring Boot
Spring AI
Ollama
PostgreSQL + pgvector
Gradle

🚀 Как запустить
Ниже приведён полный сценарий запуска проекта с нуля.

---

### 1. Требования

Убедитесь, что установлены:

- Java 21
- Docker
- Ollama
- Git (опционально)

---

### 2. Клонировать проект

```bash
git clone https://github.com/AlexeyLitovchenko/med_project.git
cd med_project


---

### 3. Запустить PostgreSQL с pgvector

```bash

docker run -d \
  --name pgvector-db \
  -p 5432:5432 \
  -e POSTGRES_DB=medvec \
  -e POSTGRES_USER=med \
  -e POSTGRES_PASSWORD=med \
  ankane/pgvector



Проверка:


```bash

docker ps


Проверка подключения к БД:

```bash

docker exec -it pgvector-db psql -U med -d medvec

Если все ок-будет:
medvec=#

Внутри psql выполни:
CREATE EXTENSION IF NOT EXISTS vector;

Если без ошибок, то вернет:
CREATE EXTENSION

---


### 4. Запустить Ollama

```bash

ollama serve

В отдельном терминале загрузить модели:

ollama pull qwen2.5:7b
ollama pull nomic-embed-text

Проверка:

ollama list

Ответ:

qwen2.5:7b
nomic-embed-text

!ИНФОРМАЦИОННО: ollama крутится на 11434 порту, приложение будет обращаться к ней там

http://localhost:11434


---


### 5. Проверить конфигурацию приложения

файл:
src/main/resources/application.yml


должен содержать:

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/medvec
    username: med
    password: med

  ai:
    ollama:
      base-url: http://localhost:11434


---

### 6. Проверить MRI-файлы

В корне проекта создайте папку data
Загрузите файлы данную папку

data/sub-1_T1w.nii.gz
data/sub-1_FLAIR.nii.gz

---



### 7. Первый запуск приложения

Linux / macOS:
```bash
./gradlew bootRun


Windows:
```bash
gradlew.bat bootRun


## Первый успешный запуск

Приложение было успешно запущено локально.

Признаки успешного запуска:
- Tomcat стартует на порту `8080`
- PostgreSQL подключается через HikariCP
- pgvector инициализирует таблицу `vector_store`
- приложение выводит сообщение:

```text
Started MedProjectApplication



---


### 8. Проверка, что приложение запустилось

Открыть в браузере:

http://localhost:8080

---

### 9. Загрузить MRI (первый реальный запрос)ь
```bash
curl -X POST "http://localhost:8080/api/ingest" \
  -F patientId=sub-1 \
  -F modality=MRI \
  -F sourceName=sub-1_T1w.nii.gz \
  -F file=@data/sub-1_T1w.nii.gz


Ожидаемый ответ:
{
  "status": "OK",
  "modality": "MRI",
  "patientId": "sub-1",
  "metadata": {
    "sequence": "MRI",
    "parsed": true,
    "sourceFile": "sub-1_T1w.nii.gz"
  }
}


---

### 10. Задать вопрос (RAG)

curl -X POST "http://localhost:8080/api/qa" \
  -H "Content-Type: application/json" \
  -d '{"question":"Какие MRI-данные загружены?"}'

---


