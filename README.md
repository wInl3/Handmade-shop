# Handmade Shop

Handmade Shop là một ứng dụng web bán đồ lưu niệm (handmade / souvenir shop) được xây dựng bằng **Spring Boot**.  
Project mô phỏng một hệ thống bán hàng cơ bản với các chức năng quản lý sản phẩm, người dùng, xác thực và upload hình ảnh sản phẩm.

---

##  Features

- Quản lý sản phẩm (CRUD)
- Upload và hiển thị hình ảnh sản phẩm
- Xác thực & phân quyền người dùng với **Spring Security + JWT**
- Giao diện server-side render với **Thymeleaf**
- Kết nối cơ sở dữ liệu **PostgreSQL**
- Tự động tạo / cập nhật schema với **Hibernate JPA**

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- JWT (jjwt)

### Frontend
- Thymeleaf
- HTML / CSS

### Database
- PostgreSQL

### Build Tool
- Maven

---

## Image Upload

- Hình ảnh sản phẩm được upload và lưu **local** trong thư mục `uploads/`
- Thư mục `uploads/` **KHÔNG được commit lên GitHub**
- Thư mục này sẽ được tạo tự động khi ứng dụng chạy

> Uploaded product images are stored locally in the `uploads/` directory and are ignored by Git.

---

##  Security

- Sử dụng **Spring Security**
- Xác thực bằng **JWT**
- JWT secret và thời gian hết hạn được cấu hình trong `application.properties`

---

## Database Configuration

Ứng dụng sử dụng **PostgreSQL**.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/handmade_db
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

## How to Run

1️. Clone repository

git clone https://github.com/wInl3/Handmade-shop.git
cd Handmade-shop

2️. Tạo database PostgreSQL

CREATE DATABASE handmade_db;

3️. Cấu hình application.properties


Copy file application.properties


Chỉnh lại thông tin database cho phù hợp với máy của bạn

4️. Chạy ứng dụng

mvn spring-boot:run


Ứng dụng chạy tại:


http://localhost:8080

## Notes

Project được xây dựng cho mục đích học tập và thực hành Spring Boot

Không sử dụng cho môi trường production

Upload file hiện đang dùng local storage (chưa tích hợp cloud)
