/*
  Fill tables for Postgres
  BOOK
*/
/*insert into author_cache select * from library_author.author;

insert into publisher_cache select * from library_publisher.publisher;*/

insert into author_cache (id, name) values (1, 'Джош Лонг'),
				(2,'Кеннет Бастани'),
				(3,'Кей Хорстманн'),
				(4,'Гари Корнелл'),
				(5,'Козмина Юлиана'),
				(6,'Харроп Роб'),
				(7,'Шеффер Крис'),
				(8,'Хо Кларенс'),
				(9,'Сьерра Кэтти'),
				(10,'Бэйтс Берт'),
				(11,'Тарик Рашид'),
				(12,'Марко Кэнту'),
				(13,'Осипов Дмитрий Леонидович'),
				(14,'Эванс Бенджамин'),
				(15,'Кларк Джейсон'),
				(16,'Фербург Мартин'),
				(17,'И. В. Курбатова'),
				(18,'А. В. Печкуров'),
				(19,'Лауренциу Спилкэ'),
				(20,'Тимур Машнин'),
				(21,'Прохоренок Н. А.'),
				(22,'Фелипе Гутьеррес'), 
				(23,'Гриффитс Дэвид'), 
				(24,'Гриффитс Дон'), 
				(25,'Анураг Шривастава'), 
				(26,'Дмитрий Жемеров'), 
				(27,'Светлана Исакова'), 
				(28,'Валлери Лэнси'), 
				(29,'Джеймс Стронг'), 
				(30,'Дэвид Флэнаган'), 
				(31,'John Carnell'), 
				(32,'Dinesh Rajput'), 
				(33,'Rajesh R V'), 
				(34,'Delete_test');

insert into publisher_cache (id, name, site) values (1,'нет данных',''), 
					(2,'Питер', 'https://www.piter.com'), 
					(3,'Издательский дом "Вильямс"', 'http://www.williamspublishing.com'),
					(4,'Диалектика','http://www.dialektika.com'), 
					(5,'Эксмо','https://eksmo.ru'),   
					(6,'Издательские решения по лицензии Ridero','https://ridero.ru'), 
					(7,'Лань','https://lanbook.com'),
					(8,'БХВ-Петербург','https://bhv.ru'), 
					(9,'ДМК-Пресс','https://dmkpress.com'), 
					(10,'Питер (Айлиб)',''), 
					(11,'СПРИНТ БУК','https://sprintbook.kz'), 
					(12,'Manning','https://www.manning.com'),
					(13,'Packt Publishing','https://www.packtpub.com');

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java. Библиотека профессионала, том 2. Расширенные средства программирования, 10-е издание', (SELECT distinct id FROM author_cache where name='Кей Хорстманн'), 2019,
     (SELECT distinct id FROM publisher_cache where name='Диалектика'), '32.973.26-018.2.75', '978-5-9909445-0-3', 976);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Создаём нейронную сеть', (SELECT distinct id FROM author_cache where name='Тарик Рашид'), 2018,
     (SELECT distinct id FROM publisher_cache where name='Диалектика'), '32.973.26-018.2.75', '978-5-9909445-7-2', 272);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Delphi 7: Для профессионалов', (SELECT distinct id FROM author_cache where name='Марко Кэнту'), 2004,
     (SELECT distinct id FROM publisher_cache where name='Питер'), '32.973-018.2', '5-94723-593-5', 1101);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Delphi. Программирование для Android: библиотека FireMonkey', (SELECT distinct id FROM author_cache where name='Осипов Дмитрий Леонидович'), 2016,
     (SELECT distinct id FROM publisher_cache where name='Издательские решения по лицензии Ridero'), '', '978-5-4474-7944-2', 632);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Spring быстро', (SELECT distinct id FROM author_cache where name='Лауренциу Спилкэ'), 2023,
     (SELECT distinct id FROM publisher_cache where name='Питер'), '32.988.02-018', '978-5-4461-1969-1', 448);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Основы программирования с Java', (SELECT distinct id FROM author_cache where name='Тимур Машнин'), 2022,
     (SELECT distinct id FROM publisher_cache where name='нет данных'), '', '', 631);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java FX', (SELECT distinct id FROM author_cache where name='Прохоренок Н. А.'), 2019,
     (SELECT distinct id FROM publisher_cache where name='БХВ-Петербург'), '32.973.26-018.1', '978-5-9775-4072-8', 767);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Spring Boot 2: лучшие практики для профессионалов', (SELECT distinct id FROM author_cache where name='Фелипе Гутьеррес'), 2020,
     (SELECT distinct id FROM publisher_cache where name='Питер'), '32.973.2-018.1', '978-5-4461-1587-7', 464);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java в облаке', (SELECT distinct id FROM author_cache where name='Джош Лонг'), 2019,
     (SELECT distinct id FROM publisher_cache where name='Питер'), '32.988.02-018', '978-5-4461-0713-1', 624);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java. Библиотека профессионала, том 1. Основы, 9-е издание', (SELECT distinct id FROM author_cache where name='Кей Хорстманн'), 2014,
     (SELECT distinct id FROM publisher_cache where name='Издательский дом "Вильямс"'), '32.973.26-018.2.75', '978-5-8459-1869-7', 864);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Spring 5 для профессионалов', (SELECT distinct id FROM author_cache where name='Козмина Юлиана'), 2019,
     (SELECT distinct id FROM publisher_cache where name='Диалектика'), '32.973.26-018.2.75', '978-5-907114-07-4', 1120);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java для опытных разработчиков. Второе издание', (SELECT distinct id FROM author_cache where name='Эванс Бенджамин'), 2024,
     (SELECT distinct id FROM publisher_cache where name='Питер (Айлиб)'), '32.973.2-018.1', '978-5-4461-2406-0', 736);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java. устранение проблем. Чтение, отладка и оптимизация JVM-приложений', (SELECT distinct id FROM author_cache where name='Лауренциу Спилкэ'), 2023,
     (SELECT distinct id FROM publisher_cache where name='ДМК-Пресс'), '', '978-5-93700-215-0', 356);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Elasticsearch для разработчиков. Индексирование, анализ, поиск и агрегирование данных', (SELECT distinct id FROM author_cache where name='Анураг Шривастава'), 2024,
     (SELECT distinct id FROM publisher_cache where name='СПРИНТ БУК'), '', '978-5-4461-4211-8', 336);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Head First. Kotlin', (SELECT distinct id FROM author_cache where name='Гриффитс Дэвид'), 2022,
     (SELECT distinct id FROM publisher_cache where name='Питер'), '', '978-5-4461-1335-4', 464);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Kotlin в действии. Второе издание', (SELECT distinct id FROM author_cache where name='Дмитрий Жемеров'), 2023,
     (SELECT distinct id FROM publisher_cache where name='СПРИНТ БУК'), '', '978-5-4461-4198-2', 560);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Kubernetes и сети. Многоуровневый подход', (SELECT distinct id FROM author_cache where name='Валлери Лэнси'), 2021,
     (SELECT distinct id FROM publisher_cache where name='БХВ-Петербург'), '', '978-5-9775-1855-0', 317);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Java. Справочник разработчика', (SELECT distinct id FROM author_cache where name='Дэвид Флэнаган'), 2019,
     (SELECT distinct id FROM publisher_cache where name='нет данных'), '', '978-5-907144-61-3', 594);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Spring Microservices in Action', (SELECT distinct id FROM author_cache where name='John Carnell'), 2017,
     (SELECT distinct id FROM publisher_cache where name='Manning'), '', '978-1-61729398-6', 384);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Building Microservices', (SELECT distinct id FROM author_cache where name='Dinesh Rajput'), 2018,
     (SELECT distinct id FROM publisher_cache where name='Packt Publishing'), '', '978-1-78995-564-4', 502);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Изучаем Java', (SELECT distinct id FROM author_cache where name='Сьерра Кэтти'), 2019,
     (SELECT distinct id FROM publisher_cache where name='Эксмо'), '32.973.26-018.1', '978-5-699-54574-2', 720);

insert into book (name, author_id, print_year, publisher_id, bbk, isbn, pages) values
    ('Основы программирования на языке Java. Учебное пособие для вузов', (SELECT distinct id FROM author_cache where name='И. В. Курбатова'), 2024,
     (SELECT distinct id FROM publisher_cache where name='Лань'), '32.973.2.73', '978-5-507-48515-4  ', 350);

insert into author_to_book (book_id, author_id) (select id, author_id from book);
insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Java в облаке'), (select distinct id from author_cache where name='Кеннет Бастани'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Java. Библиотека профессионала, том 1. Основы, 9-е издание'),
                                                        (select distinct id from author_cache where name='Гари Корнелл'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Spring 5 для профессионалов'),
                                                        (select distinct id from author_cache where name='Харроп Роб'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Spring 5 для профессионалов'),
                                                        (select distinct id from author_cache where name='Шеффер Крис'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Spring 5 для профессионалов'),
                                                        (select distinct id from author_cache where name='Хо Кларенс'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Изучаем Java'),
                                                        (select distinct id from author_cache where name='Бэйтс Берт'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Java для опытных разработчиков. Второе издание'),
                                                        (select distinct id from author_cache where name='Кларк Джейсон'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Java для опытных разработчиков. Второе издание'),
                                                        (select distinct id from author_cache where name='Фербург Мартин'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Основы программирования на языке Java. Учебное пособие для вузов'),
                                                        (select distinct id from author_cache where name='А. В. Печкуров'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Head First. Kotlin'),
                                                        (select distinct id from author_cache where name='Гриффитс Дон'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Kotlin в действии. Второе издание'),
                                                        (select distinct id from author_cache where name='Светлана Исакова'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Kubernetes и сети. Многоуровневый подход'),
                                                        (select distinct id from author_cache where name='Джеймс Стронг'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Java. Справочник разработчика'),
                                                        (select distinct id from author_cache where name='Эванс Бенджамин'));

insert into author_to_book (book_id, author_id) values ((select distinct id from book where name='Building Microservices'),
                                                        (select distinct id from author_cache where name='Rajesh R V'));

