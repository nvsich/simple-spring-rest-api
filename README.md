# Система управления заказами в ресторане

## Условие задания
Разработать систему управления заказами в ресторане, которая поддерживает два типа пользователей: посетителей и администраторов. Система должна обрабатывать заказы в многопоточном режиме, позволяя клиентам добавлять блюда в заказ в реальном времени, а также отображать статусы заказов. Администраторы могут управлять меню, добавляя или удаляя блюда. Все решения должны соблюдать принципы ООП и SOLID, а также использовать шаблоны проектирования, где это уместно.

#### Требования
- Реализуйте систему аутентификации для двух типов пользователей: посетителей и администраторов.

- Администратор может добавлять и удалять блюда из меню, а также устанавливать их количество, цену и сложность выполнения (время, которое оно будет готовиться).

- Посетители могут составлять заказ, выбирая блюда из актуального меню.

- Заказы обрабатываются в отдельных потоках, симулируя процесс приготовления.

- Посетители могут добавлять блюда в существующий заказ, пока он находится в обработке.

- Посетители должны иметь возможность отменять заказ до того, как он будет готов.

- Система должна отображать актуальный статус заказа (например, "принят", "готовится", "готов").

- По завершению выполнения заказа посетитель должен иметь возможность его оплатить.

- Необходимо сохранять состояния программы: меню, сумму выручки, пользователей в системе, а также то, что вы посчитаете нужным.

#### Опциональное задание

- Дать возможность клиентам оставлять отзывы о блюдах после оплаты заказа. Отзывы должны включать оценку от 1 до 5 и текстовый комментарий.

- Реализовать функционал, позволяющий администратору просматривать статистику по заказам и отзывам (например, самые популярные блюда, средняя оценка блюд, количество заказов за период).

- Система приоритетов для обработки заказов (вы должны сами решить, по какому принципу приоритезированы заказы, объяснив своё решение в readme). 



## Пояснение к решению

Приложение реализовано в виде REST API на Java Spring Boot с подключением к PostgreSQL. Чтобы запустить приложение необходимо иметь базу данных на PostgreSQL (конфигурацию можно посмотреть в application.yml файле), либо воспользоваться compose.yaml файлом для запуска docker-compose. Для тестирования приложения рекомендуется использовать Postman или другие инструменты для тестирования API.

Заказы обрабатываются многопоточно с помощью ExecutorService. Максимальное количество потоков можно регулировать в application.yml файле orders.oneTimeMax. Система приоритетов заказов - FIFO.

Ниже представлены энпоинты для каждого контроллера (также в приложении работает Swagger UI ~~но он не настроен и отображает много лишнего~~). Подразумевается, что приложение работает на localhost:8080
 

### Авторизация
Реализация через Spring Security. У пользователя могут быть роли, некоторые эндпоинты доступны только пользоватям с ролью 'ROLE_ADMIN'. Без получения токена авторизации будет нельзя использовать другие эндпоинты.

- Для создания пользователя необходимо выполнить _POST_ ``/users``
```json
{
  "name": "exampleUsername",
  "password": "examplePassword",
  "roles": "ROLE_ADMIN"
}
```
- Для авторизации необходимо выполнить _POST_ форму ``/login`` (form-data в Postman, также для дальнейшего тестирования _**будет необходимо хранить cookie авторизации**_ - настраивается в Postman отдельно)
```text
    username: exampleUsername
    password: examplePassword
```

### Users

- _GET_  ``/api/users/me``  
Просмотр информации о себе

- _GET_ ``/api/users``  
Просмотр информации о всех пользователях. Доступ только у админов 

- _GET_ ``/api/users/{id}``  
Просмотр информации о пользователе по id. Доступ только у админов

- _DELETE_ ``/api/users/{id}``  
Удаление пользователя по id. Доступ только у админов

- _POST_ ``/api/users``  
Добавление нового пользователя. Для получения прав админа необходимо указать ROLE_ADMIN
```json
{
  "name": "exampleUsername",
  "password": "examplePassword",
  "roles": "ROLE_ADMIN"
}
```


### Menu

- _GET_ ``/api/menu``  
Просмотр всего списка блюд.
- _POST_ ``/api/menu``  
Добавление блюда в меню. Доступ только у админов
```json 
{
  "name": "exampleName",
  "price": 123,
  "time": 10, 
  "amount": 5
}
```
- _PUT_ ``/api/menu/{id}``  
Изменение блюда по id. Все поля опциональны. Доступ только у админов
```json
{
  "name": "exampleName", 
  "price": 123,          
  "time": 10,            
  "amount": 5            
}
```

- _DELETE_ ``/api/menu/{id}``  
Удаление блюда по id. Доступ только у админов

### Order

- _GET_ ``/api/orders/all-orders``  
Просмотр всех заказов. Доступ только у админов

- _GET_ ``/api/orders``  
Просмотр своих заказов

- _POST_ ``/api/orders``  
Добавление нового заказа. Необходимо указать id блюд в списке
```json
{
  "menuIds": [1, 2, 3]
}
```

- _DELETE_ ``/api/orders/{id}``  
Удаление заказа по id. Можно удалить только свой заказ, который имеет статус RECEIVED

- _PUT_ ``/api/orders/{id}``  
Изменение заказа по id. Можно изменить только свой заказ
```json
{
  "menuIds": [4, 5, 6]
}
```

- _POST_ ``/api/orders/pay/{id}``  
Оплата заказа по id. Можно оплатить только свой заказ, имеющий статус READY


### Review

- _GET_ ``/api/reviews/{menuId}``  
Получить отзывы блюда по его id

- _POST_ ``/api/reviews``  
Оставить отзыв на блюдо. Можно оставить отзыв только на блюдо, которые было хотя бы в одном из собственных заказов и имеет статус PAID
```json
{
  "menuId": 1,
  "rating": 5,
  "text": "delicious dish!"
}
```

### Stats

- _GET_ ``/api/stats/revenue``  
Получить полную выручку. Считается только по заказам со статусом PAID. Доступ только у админов

- _GET_ ``/api/stats/rating/{id}``  
Получить среднюю оценку блюда по id. Доступ только у админов

