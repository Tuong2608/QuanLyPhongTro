# Quản lý nhà trọ - Spring Boot / Kiểm thử phần mềm

Bản lưu source web Java/Spring Boot để tiếp tục môn Kiểm thử phần mềm. **Không phải bản Flutter của môn Lập trình di động nâng cao.**

Project gốc: https://github.com/toane54111/QuanLyPhongTro. Snapshot 2026-10-11 giữ các sửa đổi source local, không kèm lịch sử Git cũ, database thực tế, ảnh upload hoặc báo cáo.

## Yêu cầu

- JDK 17 và Maven 3.9.x (mvn -v phải nhận Java 17).
- MySQL 8, localhost:3306.
- Spring Boot 3.2.5, Thymeleaf, Spring Security, Spring Data JPA.

## Chạy trên Windows CMD

```bat
git clone https://github.com/Tuong2608/QuanLyPhongTro.git
cd QuanLyPhongTro
```

Tạo database demo riêng trong MySQL:

```sql
CREATE DATABASE IF NOT EXISTS quanlynhatro_katalon
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Trong CMD, thay mật khẩu mẫu bằng mật khẩu MySQL của máy bạn:

```bat
set "DB_USERNAME=root"
set "DB_PASSWORD=YOUR_MYSQL_PASSWORD"
mvn spring-boot:run
```

PowerShell: dùng `$env:DB_USERNAME = 'root'` và `$env:DB_PASSWORD = 'YOUR_MYSQL_PASSWORD'`. Có thể đặt DB_URL để đổi server/cổng/database. Không commit mật khẩu thật.

Mở http://localhost:8080/login sau khi ứng dụng khởi động thành công. Giữ terminal mở khi dùng website và Katalon; Ctrl+C để dừng.

Schema và dữ liệu mẫu được tạo khi khởi động. KHÔNG trỏ vào database sản xuất: bản demo có cập nhật schema và khởi tạo dữ liệu.

## Tài khoản demo local

| Vai trò | Email | Mật khẩu |
| --- | --- | --- |
| Chủ trọ | chutro@gmail.com | 123456 |
| Khách thuê | t@gmail.com | 123456 |

Chỉ dùng demo, không triển khai công khai với mật khẩu mặc định. DataInitializer đặt lại mật khẩu hai tài khoản này khi khởi động. Gợi ý trên giao diện vẫn ghi khach1@gmail.com nhưng tài khoản thực tế là **t@gmail.com**; giữ nguyên để phản ánh bản đang kiểm thử.

## Demo Katalon

| Test case | Dữ liệu | Mong đợi |
| --- | --- | --- |
| TC01_Login_ChuTro | chutro@gmail.com / 123456 | /landlord/dashboard, đúng email |
| TC02_Login_SaiMatKhau | chutro@gmail.com / Sai123456 | /login?error=true, thông báo lỗi |
| TC03_Login_KhachThue | t@gmail.com / 123456 | /tenant/dashboard, đúng email |

Suite TS_Demo_DangNhap đã có kết quả 3 Passed trong lần demo trước; không đồng nghĩa toàn bộ hệ thống không có lỗi. Ca sai mật khẩu phải Passed khi hệ thống từ chối đúng mong đợi. WebDriver phải tương thích trình duyệt.

**Project Katalon và báo cáo thực thi lưu riêng, chưa nằm trong repo này.** Không kèm dữ liệu MySQL và ảnh uploads/ của máy cũ.

## Build

```bat
mvn test
mvn package -DskipTests
```

Build thành công không thay thế việc chạy với MySQL và kiểm thử nghiệp vụ.
