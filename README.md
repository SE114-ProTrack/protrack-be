# ProTrack - Task & Project Management Backend

**ProTrack** là hệ thống backend (Spring Boot) cho ứng dụng quản lý dự án, công việc, giao tiếp nhóm. Đây là sản phẩm của đồ án môn học SE114 - Nhập môn phát triển ứng dụng di động.

---

## Công nghệ sử dụng

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA (PostgreSQL)
- Spring Security (JWT)
- Spring Validation
- Spring WebSocket (Real-time chat, notification)
- Redis (cache, queue)
- Cloudinary (lưu trữ file)
- Mail SMTP (Gmail)
- Maven

---

## Cài đặt & Chạy dự án

### 1. **Clone dự án**

```bash
git clone https://github.com/SE114-ProTrack/protrack-be.git
cd protrack-be
````

### 2. **Cấu hình môi trường**

* File cấu hình: `src/main/resources/application.properties`
* DB: PostgreSQL
* Redis: `localhost:6379` (mặc định)
* Mail: Gmail (có thể chỉnh sửa user/pass)
* Cloudinary: Thông tin API key/secret đã có sẵn

> **Lưu ý:**
> Không để thông tin secret/production DB thật lên public repo.
> Hiện tại, để thầy dễ dàng kiểm thử, cấu hình môi trường đã được cài sẵn.

### 3. **Build và chạy**

```bash
./mvnw clean install
./mvnw spring-boot:run
```

* Mặc định chạy ở port `8080`

### 4. **Swagger UI - Tài liệu API**

* Truy cập: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* OpenAPI docs: `/v3/api-docs`

---

## Kết nối CSDL

* **PostgreSQL**:
  Thông tin cấu hình trong `application.properties`:

  ```
  spring.datasource.url=jdbc:postgresql://<host>:5432/postgres
  spring.datasource.username=<username>
  spring.datasource.password=<password>
  ```
* **Redis**:

  ```
  spring.redis.host=localhost
  spring.redis.port=6379
  ```
--- 

## Các chức năng chính

* **Quản lý dự án**: Tạo, mời thành viên, phân quyền, xem tiến độ.
* **Quản lý task**: Giao việc, phân công, gán nhãn, file đính kèm, subtask, comment, lịch sử hoạt động.
* **Notification**: Real-time và lưu lịch sử cho từng thành viên (qua WebSocket hoặc polling API).
* **Chat nhóm**: Nhắn tin nội bộ project.
* **Phân quyền chi tiết**: Chủ dự án, thành viên, vai trò.
* **Tìm kiếm dự án, task, cuộc trò chuyện.**

---

## Author & Liên hệ

* 23520951@gm.uit.edu.vn
* 23520771@gm.uit.edu.vn

---

## Lưu ý

* **Không commit thông tin secret lên public!**.
* DB migrations, cấu trúc bảng có thể cần cập nhật khi phát triển tiếp.
* Đọc kỹ code repository/service để nắm rõ phân quyền trước khi sửa logic nghiệp vụ.
