# PHP Task Manager (CRUD + Authentication)

This project is a simple task management system built in PHP using procedural programming (no OOP). It was developed as practice for a technical exam and focuses on backend fundamentals.

## 📌 Features

- User authentication (login / logout)
- Session management
- Create, read, update and delete tasks (CRUD)
- Task attributes:
  - Title
  - Description
  - Priority
  - Deadline date
  - Completion status
- Protected routes (only logged-in users can access tasks)

## 🧠 Technologies Used

- PHP (procedural)
- MySQL / MariaDB
- PDO (PHP Data Objects)
- HTML forms

## 🔐 Backend Concepts Practiced

- Sessions (`$_SESSION`)
- Cookies (if applicable in extensions)
- Form handling with POST requests
- Redirections using `header()`
- Prepared statements (PDO)
- SQL injection prevention

## 🗄️ Database Structure

The project uses two main tables:

- `usuarios`
- `tareas`

With a one-to-many relationship between users and tasks.

## ⚙️ Key Concepts

- CRUD operations
- Authentication system
- Relational database design
- Server-side validation
- Basic MVC separation (without formal structure)

## 🚀 Purpose

This project was built as exam practice to reinforce core PHP backend concepts without using frameworks or OOP.