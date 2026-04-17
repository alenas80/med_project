🧠 Medical Multimodal AI Application

# 📌 Описание проекта

Данное приложение представляет собой backend-систему для обработки мультимодальных медицинских данных, реализованную на основе:
Spring Boot
Spring AI
Ollama


# 🎯 Цель проекта

Построение AI-пайплайна, который:
принимает MRI-данные (.nii.gz);
обрабатывает их через workflow;
преобразует в embeddings;
сохраняет в vector database (pgvector);
выполняет RAG (вопрос-ответ).
---


# 🚀 Функциональность

Ingest pipeline

загрузка MRI через REST API

парсинг .nii.gz

извлечение срезов (slices)

конвертация в PNG

генерация текстовых признаков

## Embeddings

text embeddings через Ollama

заготовка под multimodal embeddings (image + text)

## Storage

PostgreSQL + pgvector

хранение embeddings + metadata

## Retrieval

semantic search (top-K)

## RAG

генерация ответов через LLM
использование контекста из vector DB
---


# 🏗 Архитектура
```
MRI (.nii.gz)
      ↓
IngestController
      ↓
IngestWorkflow
      ↓
MriParser
├── NiftiUtils (read volume)
├── Slice extraction
├── PNG conversion
└── Feature extraction (text)
      ↓
EmbeddingService (Ollama)
      ↓
VectorStoreWriter (pgvector)
      ↓
VectorSearchService
      ↓
RagService (ChatClient)
```
---
## Основные компоненты

### Ingest (загрузка и запуск пайплайна)

Точка входа в систему.

### IngestController

REST endpoint /api/ingest, который принимает:

- MRI файл (.nii.gz)
- patientId
- тип данных (modality)

### IngestWorkflow

Управляет всем процессом обработки.

Последовательно вызывает шаги:
- парсинг
- извлечение данных
- генерацию embeddings
- сохранение

### IngestContext

Объект, который передаётся между всеми этапами.
Внутри него:
- исходный файл
- извлечённые изображения (PNG)
- текстовые данные
- metadata
- embedding

## MRI Processing (обработка MRI)

### MriParser

Главный класс обработки MRI:
- сохраняет файл во временный
- читает .nii.gz
- разбивает 3D-объём на 2D срезы (slices)
  для каждого slice:
- создаёт изображение (PNG)
- генерирует текстовое описание

### NiftiUtils

Парсер формата NIfTI:
- читает бинарные данные
- извлекает размеры (X, Y, Z)
- возвращает объём (3D массив)

### MriImageConverter

Преобразует числовые данные в изображение:
- нормализует значения
- переводит в grayscale(ч/б)
- сохраняет как PNG

### SimpleMriFeatureExtractor

Делает простую аналитику по каждому срезу:
- среднее значение
- минимум
- максимум

И превращает это в текст:
MRI slice 5: mean=..., min=..., max=...

Этот текст потом используется для embeddings.

Embeddings (преобразование в вектора).
Данные переводятся в формат, пригодный для поиска.

### MultimodalEmbeddingService

Интерфейс — задаёт общий контракт:
- embedding для текста
- embedding для изображений

### OllamaMultimodalEmbeddingService

Реализация через Ollama.

## Vector Store (хранение и поиск)

### VectorStoreWriter

Сохраняет данные в базу:
- текст (описание)
- metadata
- embedding

  Один MRI → один или несколько документов в базе

### VectorSearchService

Выполняет поиск:
- принимает запрос
- находит ближайшие embedding и возвращает top 3 результата

## RAG (вопрос-ответ)

### QaController

Принимает вопрос пользователя

### RagService

Делает три шага:
- ищет релевантные документы через VectorSearchService
- формирует prompt(вопрос + найденный контекст)
- вызывает LLM через ChatClient

Модель отвечает только на основе контекста
если данных нет — говорит об этом

---
# ⚙️ Технологии

Java 21

Spring Boot

Spring AI

Ollama

PostgreSQL + pgvector

Gradle



## 🚀 Первый запуск проекта
1. Требования

   Java 21

   Docker

   Ollama
---

2. Клонирование
```
   git clone https://github.com/AlexeyLitovchenko/med_project.git
   cd med_project
```
---

3. PostgreSQL + pgvector
   ```
   docker run -d \
   --name pgvector-db \
   -p 5432:5432 \
   -e POSTGRES_DB=medvec \
   -e POSTGRES_USER=med \
   -e POSTGRES_PASSWORD=med \
   ankane/pgvector
   docker exec -it pgvector-db psql -U med -d medvec
   CREATE EXTENSION IF NOT EXISTS vector;
   ```
---

4. Ollama
   ollama serve
   В новом терминале:
   ollama pull qwen2.5:7b
   ollama pull nomic-embed-text
---
5. Конфигурация
```
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/medvec
       username: med
       password: med

   servlet:
     multipart:
       max-file-size: 512MB
       max-request-size: 512MB

   ai:
     ollama:
       base-url: http://localhost:11434
```
---

6. MRI файлы
```
   data/sub-10_inplaneT2.nii.gz
   data/sub-10_T1w.nii.gz
```
---
7. Запуск
```
   ./gradlew bootRun
```
---

8. Ingest

Запрос:
```
curl -X POST "http://localhost:8080/api/ingest" \
-F patientId=sub-1 \
-F modality=MRI \
-F sourceName=sub-10_inplaneT2.nii.gz \
-F file=@data/sub-10_inplaneT2.nii.gz
```
Ответ:
```
{
"status": "OK",
"metadata":{"slices":192,
"modality": "MRI",
"metadata": {
"parsed": true,
"dimensions": "160x192x192"}
}
"patientId": "sub-10",
}
```
---

9. QA

Запрос:
```
curl -X POST "http://localhost:8080/api/qa" \
-H "Content-Type: application/json" \
-d '{"question":"Какие MRI-данные загружены для пациента sub-10?"}'
```
Ответ:
```
{
"answer":"Для пациента sub-10 загружены следующие MRI данные:
\n\n1. sub-10_T1w.nii.gz\n2. sub-10_inplaneT2.nii.gz\n\n
Оба файла имеют метаданные с полем sequence, указывающим на то, что это MRI данные."
}
```
---

## ⚠️ Ограничения

Упрощённый парсинг NIfTI

Feature extraction — базовый (статистика)

Multimodal embeddings — частично реализованы

Нет segmentation / detection моделей

---

## 📌 Статус

MVP / Prototype (Backend AI pipeline)

## 📌 Файлы

https://s3.amazonaws.com/openneuro.org/ds000001/sub-10/anat/sub-10_T1w.nii.gz?versionId=pF1l_l_AxVOSzdmTHpQbfdBEiEV63MTt

https://s3.amazonaws.com/openneuro.org/ds000001/sub-10/anat/sub-10_inplaneT2.nii.gz?versionId=5dD7aky7dwByQdK1w84qVXPREU9ll8Gl
