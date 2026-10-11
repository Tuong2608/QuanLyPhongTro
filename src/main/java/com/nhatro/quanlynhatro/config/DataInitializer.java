package com.nhatro.quanlynhatro.config;

import com.nhatro.quanlynhatro.entity.*;
import com.nhatro.quanlynhatro.enums.*;
import com.nhatro.quanlynhatro.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            NguoiDungRepository nguoiDungRepo,
            KhuTroRepository khuTroRepo,
            PhongTroRepository phongTroRepo,
            DichVuRepository dichVuRepo,
            ChiSoDienNuocRepository chiSoRepo,
            HopDongRepository hopDongRepo,
            HoaDonRepository hoaDonRepo,
            GiaoDichRepository giaoDichRepo,
            YeuCauSuCoRepository suCoRepo,
            YeuCauGiaHanRepository giaHanRepo,
            PhuLucHopDongRepository phuLucRepo,
            ThongBaoRepository thongBaoRepo,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // ═══════════════════════════════════════════════════════════
            // Skip nếu dữ liệu đã tồn tại (chạy lại không trùng)
            // ═══════════════════════════════════════════════════════════
            if (nguoiDungRepo.count() > 2) {
                System.out.println("=== DU LIEU DA TON TAI - SKIP SEED DATA ===");
                return;
            }

            // ─────────────────────────────────────────────────────────
            // 1. TẠO TÀI KHOẢN CHỦ TRỌ + KHÁCH THUÊ
            // ─────────────────────────────────────────────────────────
            NguoiDung chuTro = upsertUser(nguoiDungRepo, passwordEncoder,
                    "chutro@gmail.com", "123456", "Nguyễn Văn Chủ",
                    "0901234567", "012345678901", VaiTro.CHU_TRO);

            // Tài khoản cũ từ data.sql (khách thuê)
            NguoiDung khach1 = upsertUser(nguoiDungRepo, passwordEncoder,
                    "t@gmail.com", "123456", "Trần Thị Khách",
                    "0912345678", "012345678902", VaiTro.KHACH_THUE);

            // Tạo thêm 3 khách thuê mới
            NguoiDung khach2 = createUser(nguoiDungRepo, passwordEncoder,
                    "ngoc@gmail.com", "123456", "Lê Thị Ngọc",
                    "0933123456", "023456789012", VaiTro.KHACH_THUE);

            NguoiDung khach3 = createUser(nguoiDungRepo, passwordEncoder,
                    "hieu@gmail.com", "123456", "Phạm Văn Hiếu",
                    "0977123456", "034567890123", VaiTro.KHACH_THUE);

            NguoiDung khach4 = createUser(nguoiDungRepo, passwordEncoder,
                    "linh@gmail.com", "123456", "Hoàng Thị Linh",
                    "0966123456", "045678901234", VaiTro.KHACH_THUE);

            // ─────────────────────────────────────────────────────────
            // 2. TẠO 1 KHU TRỌ
            // ─────────────────────────────────────────────────────────
            KhuTro khuA = KhuTro.builder()
                    .tenKhu("Khu A")
                    .diaChi("123 Đường Nguyễn Văn Cừ, Quận 5, TP.HCM")
                    .soTang(3)
                    .moTa("Khu trọ chính, gần trường đại học, có camera an ninh")
                    .build();
            khuA = khuTroRepo.save(khuA);

            // ─────────────────────────────────────────────────────────
            // 3. TẠO 9 PHÒNG TRỌ (3 tầng x 3 phòng)
            // ─────────────────────────────────────────────────────────
            PhongTro p101 = createPhong(phongTroRepo, khuA, "101", 1, 20f,
                    new BigDecimal("3000000"), TrangThaiPhong.DA_THUE, "Phòng đơn, có ban công, view sân");

            PhongTro p102 = createPhong(phongTroRepo, khuA, "102", 1, 25f,
                    new BigDecimal("3500000"), TrangThaiPhong.TRONG, "Phòng đôi, có toilet riêng, thoáng mát");

            PhongTro p103 = createPhong(phongTroRepo, khuA, "103", 1, 22f,
                    new BigDecimal("3200000"), TrangThaiPhong.TRONG, "Phòng đơn, cửa sổ lớn, sáng thoáng");

            PhongTro p201 = createPhong(phongTroRepo, khuA, "201", 2, 20f,
                    new BigDecimal("3000000"), TrangThaiPhong.DA_THUE, "Phòng đơn tầng 2, yên tĩnh");

            PhongTro p202 = createPhong(phongTroRepo, khuA, "202", 2, 30f,
                    new BigDecimal("4000000"), TrangThaiPhong.DA_THUE, "Phòng rộng, có bếp riêng, view đường");

            PhongTro p203 = createPhong(phongTroRepo, khuA, "203", 2, 22f,
                    new BigDecimal("3200000"), TrangThaiPhong.TRONG, "Phòng đơn tầng 2, có gác lửng nhỏ");

            PhongTro p301 = createPhong(phongTroRepo, khuA, "301", 3, 20f,
                    new BigDecimal("3000000"), TrangThaiPhong.DA_THUE, "Phòng đơn tầng 3, có sân thượng");

            PhongTro p302 = createPhong(phongTroRepo, khuA, "302", 3, 25f,
                    new BigDecimal("3500000"), TrangThaiPhong.TRONG, "Phòng đôi tầng 3, thoáng mát");

            PhongTro p303 = createPhong(phongTroRepo, khuA, "303", 3, 22f,
                    new BigDecimal("3200000"), TrangThaiPhong.TRONG, "Phòng đơn tầng 3, view thành phố");

            // ─────────────────────────────────────────────────────────
            // 4. DỊCH VỤ (tạo mới nếu chưa có)
            // ─────────────────────────────────────────────────────────
            if (dichVuRepo.count() == 0) {
                dichVuRepo.saveAll(List.of(
                        DichVu.builder().tenDV("Điện").donGia(new BigDecimal("3500")).donViTinh("kWh").build(),
                        DichVu.builder().tenDV("Nước").donGia(new BigDecimal("15000")).donViTinh("m3").build(),
                        DichVu.builder().tenDV("Internet").donGia(new BigDecimal("100000")).donViTinh("tháng").build(),
                        DichVu.builder().tenDV("Rác").donGia(new BigDecimal("20000")).donViTinh("tháng").build()
                ));
            }

            // ─────────────────────────────────────────────────────────
            // 5. HỢP ĐỒNG (4 phòng đã thuê)
            // ───────────────────────────────────────────────────────��─
            // HĐ1: phòng 101 - Trần Thị Khách - từ 15/01/2026 đến 15/07/2026
            HopDong hd1 = createHopDong(hopDongRepo, p101, khach1,
                    LocalDate.of(2026, 1, 15), LocalDate.of(2026, 7, 15),
                    new BigDecimal("3000000"), new BigDecimal("6000000"),
                    TrangThaiHopDong.DANG_HIEU_LUC, "Hợp đồng chính, đặt cọc 2 tháng");

            // HĐ2: phòng 201 - Lê Thị Ngọc - từ 01/02/2026 đến 01/08/2026
            HopDong hd2 = createHopDong(hopDongRepo, p201, khach2,
                    LocalDate.of(2026, 2, 1), LocalDate.of(2026, 8, 1),
                    new BigDecimal("3000000"), new BigDecimal("6000000"),
                    TrangThaiHopDong.DANG_HIEU_LUC, "Thuê 6 tháng, thanh toán hàng tháng");

            // HĐ3: phòng 202 - Phạm Văn Hiếu - từ 20/01/2026 đến 20/04/2026 (sắp hết hạn)
            HopDong hd3 = createHopDong(hopDongRepo, p202, khach3,
                    LocalDate.of(2026, 1, 20), LocalDate.of(2026, 4, 20),
                    new BigDecimal("4000000"), new BigDecimal("8000000"),
                    TrangThaiHopDong.SAP_HET_HAN, "Thuê 3 tháng, có bếp riêng, thanh toán trước");

            // HĐ4: phòng 301 - Hoàng Thị Linh - từ 10/03/2026 đến 10/09/2026
            HopDong hd4 = createHopDong(hopDongRepo, p301, khach4,
                    LocalDate.of(2026, 3, 10), LocalDate.of(2026, 9, 10),
                    new BigDecimal("3000000"), new BigDecimal("6000000"),
                    TrangThaiHopDong.DANG_HIEU_LUC, "Thuê 6 tháng, phòng có sân thượng riêng");

            // ─────────────────────────────────────────────────────────
            // 6. CHỈ SỐ ĐIỆN NƯỚC (mỗi phòng đã thuê có đủ 4 tháng: T1-T4/2026)
            // ─────────────────────────────────────────────────────────
            // Phòng 101 (HĐ1)
            createChiSo(chiSoRepo, p101, "2026-01", 100, 280, 10, 22);
            createChiSo(chiSoRepo, p101, "2026-02", 280, 490, 22, 40);
            createChiSo(chiSoRepo, p101, "2026-03", 490, 720, 40, 60);
            createChiSo(chiSoRepo, p101, "2026-04", 720, 980, 60, 80);

            // Phòng 201 (HĐ2) — bắt đầu từ tháng 2
            createChiSo(chiSoRepo, p201, "2026-02", 50, 200, 8, 20);
            createChiSo(chiSoRepo, p201, "2026-03", 200, 400, 20, 38);
            createChiSo(chiSoRepo, p201, "2026-04", 400, 650, 38, 58);

            // Phòng 202 (HĐ3) — 4 tháng
            createChiSo(chiSoRepo, p202, "2026-01", 80, 250, 8, 18);
            createChiSo(chiSoRepo, p202, "2026-02", 250, 480, 18, 35);
            createChiSo(chiSoRepo, p202, "2026-03", 480, 750, 35, 55);
            createChiSo(chiSoRepo, p202, "2026-04", 750, 1020, 55, 78);

            // Phòng 301 (HĐ4) — bắt đầu từ tháng 3
            createChiSo(chiSoRepo, p301, "2026-03", 120, 350, 12, 28);
            createChiSo(chiSoRepo, p301, "2026-04", 350, 620, 28, 48);

            // ─────────────────────────────────────────────────────────
            // 7. HÓA ĐƠN
            // Tính: tiền phòng + tiền điện (kWh x 3500) + tiền nước (m3 x 15000) + dịch vụ (100k internet + 20k rác)
            // Giá điện: 3500/kWh, nước: 15000/m³, dịch vụ: 120000 (internet 100k + rác 20k)
            // ─────────────────────────────────────────────────────────

            // HÓA ĐƠN HĐ1 (p101): tiền phòng 3M
            HoaDon hd1_t1 = createHoaDon(hoaDonRepo, hd1, "2026-01",
                    new BigDecimal("3000000"), new BigDecimal("630000"), new BigDecimal("180000"), new BigDecimal("120000"),
                    new BigDecimal("3930000"), LocalDate.of(2026, 2, 5),
                    TrangThaiHoaDon.DA_THANH_TOAN);

            HoaDon hd1_t2 = createHoaDon(hoaDonRepo, hd1, "2026-02",
                    new BigDecimal("3000000"), new BigDecimal("735000"), new BigDecimal("270000"), new BigDecimal("120000"),
                    new BigDecimal("4125000"), LocalDate.of(2026, 3, 5),
                    TrangThaiHoaDon.DA_THANH_TOAN);

            HoaDon hd1_t3 = createHoaDon(hoaDonRepo, hd1, "2026-03",
                    new BigDecimal("3000000"), new BigDecimal("805000"), new BigDecimal("300000"), new BigDecimal("120000"),
                    new BigDecimal("4225000"), LocalDate.of(2026, 4, 5),
                    TrangThaiHoaDon.THANH_TOAN_MOT_PHAN);

            HoaDon hd1_t4 = createHoaDon(hoaDonRepo, hd1, "2026-04",
                    new BigDecimal("3000000"), new BigDecimal("910000"), new BigDecimal("300000"), new BigDecimal("120000"),
                    new BigDecimal("4330000"), LocalDate.of(2026, 5, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            // HÓA ĐƠN HĐ2 (p201): tiền phòng 3M
            HoaDon hd2_t2 = createHoaDon(hoaDonRepo, hd2, "2026-02",
                    new BigDecimal("3000000"), new BigDecimal("525000"), new BigDecimal("180000"), new BigDecimal("120000"),
                    new BigDecimal("3825000"), LocalDate.of(2026, 3, 5),
                    TrangThaiHoaDon.DA_THANH_TOAN);

            HoaDon hd2_t3 = createHoaDon(hoaDonRepo, hd2, "2026-03",
                    new BigDecimal("3000000"), new BigDecimal("700000"), new BigDecimal("270000"), new BigDecimal("120000"),
                    new BigDecimal("4090000"), LocalDate.of(2026, 4, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            HoaDon hd2_t4 = createHoaDon(hoaDonRepo, hd2, "2026-04",
                    new BigDecimal("3000000"), new BigDecimal("875000"), new BigDecimal("300000"), new BigDecimal("120000"),
                    new BigDecimal("4295000"), LocalDate.of(2026, 5, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            // HÓA ĐƠN HĐ3 (p202): tiền phòng 4M
            HoaDon hd3_t1 = createHoaDon(hoaDonRepo, hd3, "2026-01",
                    new BigDecimal("4000000"), new BigDecimal("595000"), new BigDecimal("150000"), new BigDecimal("120000"),
                    new BigDecimal("4865000"), LocalDate.of(2026, 2, 5),
                    TrangThaiHoaDon.DA_THANH_TOAN);

            HoaDon hd3_t2 = createHoaDon(hoaDonRepo, hd3, "2026-02",
                    new BigDecimal("4000000"), new BigDecimal("805000"), new BigDecimal("255000"), new BigDecimal("120000"),
                    new BigDecimal("5180000"), LocalDate.of(2026, 3, 5),
                    TrangThaiHoaDon.DA_THANH_TOAN);

            HoaDon hd3_t3 = createHoaDon(hoaDonRepo, hd3, "2026-03",
                    new BigDecimal("4000000"), new BigDecimal("945000"), new BigDecimal("300000"), new BigDecimal("120000"),
                    new BigDecimal("5365000"), LocalDate.of(2026, 4, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            // HÓA ĐƠN HĐ4 (p301): tiền phòng 3M
            HoaDon hd4_t3 = createHoaDon(hoaDonRepo, hd4, "2026-03",
                    new BigDecimal("3000000"), new BigDecimal("805000"), new BigDecimal("240000"), new BigDecimal("120000"),
                    new BigDecimal("4165000"), LocalDate.of(2026, 4, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            HoaDon hd4_t4 = createHoaDon(hoaDonRepo, hd4, "2026-04",
                    new BigDecimal("3000000"), new BigDecimal("945000"), new BigDecimal("300000"), new BigDecimal("120000"),
                    new BigDecimal("4365000"), LocalDate.of(2026, 5, 5),
                    TrangThaiHoaDon.CHUA_THANH_TOAN);

            // ─────────────────────────────────────────────────────────
            // 8. GIAO DỊCH (thanh toán hóa đơn)
            // ─────────────────────────────────────────────────────────
            // HĐ1: thanh toán T1 (tiền mặt)
            createGiaoDich(giaoDichRepo, hd1_t1, new BigDecimal("3930000"),
                    PhuongThucThanhToan.TIEN_MAT, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Thanh toán tiền mặt tháng 01/2026", null);

            // HĐ1: thanh toán T2 (chuyển khoản)
            createGiaoDich(giaoDichRepo, hd1_t2, new BigDecimal("4125000"),
                    PhuongThucThanhToan.CHUYEN_KHOAN, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Chuyển khoản tháng 02/2026", null);

            // HĐ1: thanh toán một phần T3 (1.5M, còn thiếu 2.725M)
            createGiaoDich(giaoDichRepo, hd1_t3, new BigDecimal("1500000"),
                    PhuongThucThanhToan.CHUYEN_KHOAN, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Thanh toán một phần tháng 03/2026", null);

            // HĐ2: thanh toán T2 (tiền mặt)
            createGiaoDich(giaoDichRepo, hd2_t2, new BigDecimal("3825000"),
                    PhuongThucThanhToan.TIEN_MAT, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Thanh toán tiền mặt tháng 02/2026", null);

            // HĐ3: thanh toán T1 (tiền mặt)
            createGiaoDich(giaoDichRepo, hd3_t1, new BigDecimal("4865000"),
                    PhuongThucThanhToan.TIEN_MAT, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Thanh toán tiền mặt tháng 01/2026", null);

            // HĐ3: thanh toán T2 (chuyển khoản)
            createGiaoDich(giaoDichRepo, hd3_t2, new BigDecimal("5180000"),
                    PhuongThucThanhToan.CHUYEN_KHOAN, TrangThaiGiaoDich.DA_XAC_NHAN,
                    "Chuyển khoản tháng 02/2026", null);

            // HĐ2: T3 đang chờ xác nhận (khách chuyển khoản ngày 02/04)
            createGiaoDich(giaoDichRepo, hd2_t3, new BigDecimal("4090000"),
                    PhuongThucThanhToan.CHUYEN_KHOAN, TrangThaiGiaoDich.CHO_XAC_NHAN,
                    "Chuyển khoản tháng 03/2026 - đang chờ xác nhận", LocalDateTime.of(2026, 4, 2, 14, 30));

            // ─────────────────────────────────────────────────────────
            // 9. YÊU CẦU SỰ CỐ
            // ─────────────────────────────────────────────────────────
            // Phòng 101: bình nóng lạnh hỏng → đã xong
            YeuCauSuCo sc1 = YeuCauSuCo.builder()
                    .phongTro(p101)
                    .khachThue(khach1)
                    .loaiSuCo("Bình nóng lạnh")
                    .moTa("Bình nóng lạnh không nóng, có thể thanh nhiệt bị hỏng. Ngày 15/02 gọi thợ đến sửa.")
                    .mucDoUuTien(MucDoUuTien.CAO)
                    .trangThai(TrangThaiSuCo.DA_XONG)
                    .ngayTao(LocalDateTime.of(2026, 2, 10, 9, 0))
                    .ghiChuXuLy("Đã thay thanh nhiệt, bình hoạt động bình thường từ ngày 15/02")
                    .build();
            suCoRepo.save(sc1);

            // Phòng 202: quạt hỏng → đang sửa
            YeuCauSuCo sc2 = YeuCauSuCo.builder()
                    .phongTro(p202)
                    .khachThue(khach3)
                    .loaiSuCo("Quạt")
                    .moTa("Quạt trần phòng 202 kêu to, rotor có thể bị cong. Cần thay mới hoặc sửa lại cánh quạt.")
                    .mucDoUuTien(MucDoUuTien.TRUNG_BINH)
                    .trangThai(TrangThaiSuCo.DANG_SUA)
                    .ngayTao(LocalDateTime.of(2026, 3, 25, 16, 0))
                    .ghiChuXuLy("Đã gọi thợ, hẹn ngày 28/03 đến sửa")
                    .build();
            suCoRepo.save(sc2);

            // Phòng 201: rò nước → mới
            YeuCauSuCo sc3 = YeuCauSuCo.builder()
                    .phongTro(p201)
                    .khachThue(khach2)
                    .loaiSuCo("Vòi nước")
                    .moTa("Vòi nước bồn rửa mặt bị rò rỉ, nhỏ giọt liên tục. Cần thay gioăng hoặc vòi mới.")
                    .mucDoUuTien(MucDoUuTien.THAP)
                    .trangThai(TrangThaiSuCo.MOI)
                    .ngayTao(LocalDateTime.of(2026, 4, 1, 10, 0))
                    .build();
            suCoRepo.save(sc3);

            // ─────────────────────────────────────────────────────────
            // 10. PHỤ LỤC HỢP ĐỒNG (chủ trọ gửi điều chỉnh giá cho HĐ1)
            // ─────────────────────────────────────────────────────────
            PhuLucHopDong pl1 = PhuLucHopDong.builder()
                    .hopDong(hd1)
                    .giaThueMMoi(new BigDecimal("3300000"))
                    .ngayKetThucMoi(LocalDate.of(2027, 1, 15))
                    .trangThai(TrangThaiYeuCau.CHO_PHE_DUYET)
                    .ghiChu("Tăng giá thuê từ 3M lên 3.3M/tháng do thị trường tăng. Phụ lục có hiệu lực sau khi khách duyệt.")
                    .ngayTao(LocalDateTime.of(2026, 3, 20, 9, 0))
                    .build();
            phuLucRepo.save(pl1);

            // ─────────────────────────────────────────────────────────
            // 11. YÊU CẦU GIA HẠN (khách thuê HĐ2 xin gia hạn)
            // ─────────────────────────────────────────────────────────
            YeuCauGiaHan ycgh1 = YeuCauGiaHan.builder()
                    .khachThue(khach2)
                    .hopDong(hd2)
                    .thoiGianGiaHan(6)
                    .trangThai(TrangThaiYeuCau.CHO_PHE_DUYET)
                    .ghiChu("Em muốn gia hạn thêm 6 tháng, ký từ ngày 01/08/2026. Em sẽ thanh toán sớm hơn hạn.")
                    .ngayTao(LocalDateTime.of(2026, 3, 28, 15, 0))
                    .build();
            giaHanRepo.save(ycgh1);

            // ─────────────────────────────────────────────────────────
            // 12. THÔNG BÁO
            // ─────────────────────────────────────────────────────────
            // Cho chủ trọ
            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(chuTro)
                    .tieuDe("Có hóa đơn chờ xác nhận")
                    .noiDung("Khách Lê Thị Ngọc (phòng 201) đã chuyển khoản 4.090.000đ cho hóa đơn tháng 03/2026. Vui lòng xác nhận.")
                    .link("/landlord/hoa-don")
                    .daDoc(false)
                    .ngayTao(LocalDateTime.of(2026, 4, 2, 14, 35))
                    .build());

            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(chuTro)
                    .tieuDe("Hóa đơn phòng 101 chưa thanh toán đủ")
                    .noiDung("Hóa đơn tháng 03/2026 của phòng 101 còn nợ 2.725.000đ. Khách đã thanh toán một phần 1.500.000đ.")
                    .link("/landlord/hoa-don")
                    .daDoc(true)
                    .ngayTao(LocalDateTime.of(2026, 3, 8, 10, 0))
                    .build());

            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(chuTro)
                    .tieuDe("Yêu cầu gia hạn từ khách Lê Thị Ngọc")
                    .noiDung("Khách Lê Thị Ngọc (phòng 201) gửi yêu cầu gia hạn HĐ thêm 6 tháng. Vui lòng duyệt.")
                    .link("/landlord/yeu-cau-gia-han")
                    .daDoc(false)
                    .ngayTao(LocalDateTime.of(2026, 3, 28, 15, 5))
                    .build());

            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(chuTro)
                    .tieuDe("Phòng 202 sắp hết hạn hợp đồng")
                    .noiDung("Hợp đồng phòng 202 của Phạm Văn Hiếu sẽ hết hạn vào ngày 20/04/2026. Cần liên hệ khách xác nhận.")
                    .link("/landlord/hop-dong")
                    .daDoc(false)
                    .ngayTao(LocalDateTime.of(2026, 4, 1, 8, 0))
                    .build());

            // Cho khách 1 (Trần Thị Khách - p101)
            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(khach1)
                    .tieuDe("Phụ lục điều ch���nh giá thuê")
                    .noiDung("Chủ trọ đã gửi phụ lục điều chỉnh giá thuê mới: 3.300.000đ/tháng (tăng từ 3M). Vui lòng duyệt hoặc từ chối.")
                    .link("/tenant/hop-dong")
                    .daDoc(false)
                    .ngayTao(LocalDateTime.of(2026, 3, 20, 9, 5))
                    .build());

            thongBaoRepo.save(ThongBao.builder()
                    .nguoiNhan(khach1)
                    .tieuDe("Sự cố bình nóng lạnh đã được xử lý")
                    .noiDung("Sự cố bình nóng lạnh phòng 101 đã được sửa xong vào ngày 15/02/2026. Bình hoạt động bình thường.")
                    .link("/tenant/su-co")
                    .daDoc(true)
                    .ngayTao(LocalDateTime.of(2026, 2, 15, 17, 0))
                    .build());

            // ─────────────────────────────────────────────────────────
            // 13. IN BẢNG TÓM TẮT
            // ─────────────────────────────────────────────────────────
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║       SEED DATA HOÀN CHỈNH - ĐÃ TẠO THÀNH CÔNG               ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  Khu trọ:          1 (Khu A)                               ║");
            System.out.println("║  Phòng trọ:        9 (3 tầng x 3 phòng)                    ║");
            System.out.println("║    - Đã thuê:      4 (101, 201, 202, 301)                   ║");
            System.out.println("║    - Trống:        5 (102, 103, 203, 302, 303)             ║");
            System.out.println("║  Khách thuê:      4 người                                 ║");
            System.out.println("║  Hợp đồng:        4 (1 sắp hết hạn, 3 đang hiệu lực)    ║");
            System.out.println("║  Hóa đơn:         11 (đã TT: 5, còn nợ: 6)                ║");
            System.out.println("║  Giao dịch:       8 bản ghi                               ║");
            System.out.println("║  Chỉ số điện nước: 13 bản ghi (4 phòng x ~3 tháng)       ║");
            System.out.println("║  Sự cố:           3 ticket (đã xong / đang sửa / mới)    ║");
            System.out.println("║  Phụ lục HĐ:       1 (chờ duyệt)                          ║");
            System.out.println("║  Yêu cầu gia hạn:  1 (chờ duyệt)                          ║");
            System.out.println("║  Thông báo:        6                                       ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║  TÀI KHOẢN ĐĂNG NHẬP:                                      ║");
            System.out.println("║  Chủ trọ:  chutro@gmail.com / 123456                       ║");
            System.out.println("║  Khách 1:  t@gmail.com / 123456 (phòng 101)               ║");
            System.out.println("║  Khách 2:  ngoc@gmail.com / 123456 (phòng 201)            ║");
            System.out.println("║  Khách 3:  hieu@gmail.com / 123456 (phòng 202 - sắp hết)  ║");
            System.out.println("║  Khách 4:  linh@gmail.com / 123456 (phòng 301)            ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
        };
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════

    private NguoiDung upsertUser(NguoiDungRepository repo, PasswordEncoder encoder,
                                 String email, String rawPw, String hoTen,
                                 String sdt, String cccd, VaiTro vaiTro) {
        Optional<NguoiDung> existing = repo.findByEmail(email);
        if (existing.isPresent()) {
            NguoiDung u = existing.get();
            u.setMatKhau(encoder.encode(rawPw));
            return repo.save(u);
        }
        return createUser(repo, encoder, email, rawPw, hoTen, sdt, cccd, vaiTro);
    }

    private NguoiDung createUser(NguoiDungRepository repo, PasswordEncoder encoder,
                                 String email, String rawPw, String hoTen,
                                 String sdt, String cccd, VaiTro vaiTro) {
        NguoiDung u = NguoiDung.builder()
                .hoTen(hoTen)
                .email(email)
                .matKhau(encoder.encode(rawPw))
                .sdt(sdt)
                .cccd(cccd)
                .vaiTro(vaiTro)
                .trangThai(TrangThaiTaiKhoan.HOAT_DONG)
                .build();
        return repo.save(u);
    }

    private PhongTro createPhong(PhongTroRepository repo, KhuTro khu, String soPhong,
                                 int tang, Float dienTich, BigDecimal giaThue,
                                 TrangThaiPhong trangThai, String moTa) {
        PhongTro p = PhongTro.builder()
                .khuTro(khu)
                .soPhong(soPhong)
                .tang(tang)
                .dienTich(dienTich)
                .giaThue(giaThue)
                .trangThai(trangThai)
                .moTa(moTa)
                .build();
        return repo.save(p);
    }

    private HopDong createHopDong(HopDongRepository repo, PhongTro phong,
                                  NguoiDung khach, LocalDate ngayBatDau,
                                  LocalDate ngayKetThuc, BigDecimal giaThue,
                                  BigDecimal tienCoc, TrangThaiHopDong trangThai,
                                  String ghiChu) {
        HopDong hd = HopDong.builder()
                .phongTro(phong)
                .khachThue(khach)
                .ngayBatDau(ngayBatDau)
                .ngayKetThuc(ngayKetThuc)
                .giaThue(giaThue)
                .tienCoc(tienCoc)
                .trangThai(trangThai)
                .ghiChu(ghiChu)
                .build();
        return repo.save(hd);
    }

    private void createChiSo(ChiSoDienNuocRepository repo, PhongTro phong,
                             String kyGhi, int dienCu, int dienMoi, int nuocCu, int nuocMoi) {
        if (!repo.existsByPhongTro_PhongIdAndKyGhi(phong.getPhongId(), kyGhi)) {
            ChiSoDienNuoc cs = ChiSoDienNuoc.builder()
                    .phongTro(phong)
                    .kyGhi(kyGhi)
                    .dienCu(dienCu)
                    .dienMoi(dienMoi)
                    .nuocCu(nuocCu)
                    .nuocMoi(nuocMoi)
                    .ngayGhi(LocalDate.now())
                    .build();
            repo.save(cs);
        }
    }

    private HoaDon createHoaDon(HoaDonRepository repo, HopDong hopDong, String ky,
                                BigDecimal tienPhong, BigDecimal tienDien,
                                BigDecimal tienNuoc, BigDecimal phiDichVu,
                                BigDecimal tongTien, LocalDate hanTT,
                                TrangThaiHoaDon trangThai) {
        if (repo.existsByHopDong_HopDongIdAndKyThanhToan(hopDong.getHopDongId(), ky)) {
            return null; // skip duplicate
        }
        HoaDon hd = HoaDon.builder()
                .hopDong(hopDong)
                .kyThanhToan(ky)
                .tienPhong(tienPhong)
                .tienDien(tienDien)
                .tienNuoc(tienNuoc)
                .phiDichVu(phiDichVu)
                .tongTien(tongTien)
                .hanThanhToan(hanTT)
                .trangThai(trangThai)
                .build();
        return repo.save(hd);
    }

    private void createGiaoDich(GiaoDichRepository repo, HoaDon hoaDon, BigDecimal soTien,
                                PhuongThucThanhToan phuongThuc, TrangThaiGiaoDich trangThai,
                                String ghiChu, LocalDateTime ngayGD) {
        GiaoDich gd = GiaoDich.builder()
                .hoaDon(hoaDon)
                .soTien(soTien)
                .phuongThuc(phuongThuc)
                .trangThaiGD(trangThai)
                .ghiChu(ghiChu)
                .ngayGiaoDich(ngayGD != null ? ngayGD : LocalDateTime.now())
                .build();
        repo.save(gd);
    }
}