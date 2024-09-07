# servlet-for-the-phonebook

**RU**

Сервлет для работы с записной книжкой. В записной книжке для каждого человека хранится его имя и список телефонов (их может быть несколько). 
При старте сервлет загружает записную книжку из текстового файла. 

Сервлет позволяет:
1. Просматривать список записей
2. Добавлять нового пользователя
3. Добавлять новый телефон

На главной странице сервлет находится список записей. Вверху страницы ссылки --- добавить. Каждая из ссылок ведет на отдельную страницу, где с помощью элементов <input type="text" name="username" /> в форме вводятся необходимые данные. Для отправки данных сервлету есть кнопка submit. 

В качестве контейнера сервлетов используется либо сервер Tomcat.

Учтена синхронизация при работе нескольких пользователей с одной записной книжкой.

**ENG**

Servlet for a phonebook. The phonebook for each person stores his name and a list of phone numbers (there can be several of them). 
On startup, the servlet loads the phonebook from a text file. 

The servlet allows you to:
1. View the list of entries
2. Add a new user
3. Add a new phone number

On the main page of the servlet is a list of records. At the top of the page are links --- add. Each of the links leads to a separate page, where using <input type=“text” name=“username” /> elements in the form the necessary data is entered. To submit the data to the servlet, there is a submit button. 

Either Tomcat server is used as a servlet container.

Synchronization when several users work with one directory is taken into account.

Tomcat 10.1.28
