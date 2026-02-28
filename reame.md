<h1>Книги</h1>

_Продолжение проекта Книги._

Монолит заменен на микросервисы:
author-service
publisher-servive
book-service

Обмен информацией между микросервисами - через Kafka.

Используется БД Postgres

**Запуск:**

mvn clean install

При этом в Docker будет загружен образ Postgres, Kafka, инициализирована база и выполнены тесты. 

