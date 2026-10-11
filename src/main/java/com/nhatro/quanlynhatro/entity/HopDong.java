package com.nhatro.quanlynhatro.entity;

import com.nhatro.quanlynhatro.enums.TrangThaiHopDong;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "hop_dong")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HopDong {

    // ===================================================================
    // CLASS: HopDong - Entity dai dien bang hop_dong trong CSDL
    // Nghiep vu: Luu thong tin hop dong thue phong giua chu tro va khach thue.
    // Moi hop dong xac dinh: phong nao, ai thue, gia bao nhieu, thoi han ra sao.
    // He thong dung HopDong de xac dinh phong dang cho thue hay trong, tinh tien
    // phong hang thang (qua HoaDon), va quan ly gia han/cham dut hop dong.
    // ===================================================================


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hopDongId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phong_id", nullable = false)
    private PhongTro phongTro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_thue_id", nullable = false)
    private NguoiDung khachThue;

    @Column(nullable = false)
    private LocalDate ngayBatDau;

    @Column(nullable = false)
    private LocalDate ngayKetThuc;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal giaThue;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal tienCoc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiHopDong trangThai = TrangThaiHopDong.DANG_HIEU_LUC;

    @Column(columnDefinition = "TEXT")
    private String ghiChu;

    // Relationships
    @OneToMany(mappedBy = "hopDong")
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "hopDong")
    private List<YeuCauGiaHan> yeuCauGiaHans;

    @OneToMany(mappedBy = "hopDong")
    private List<YeuCauChamDut> yeuCauChamDuts;

    @OneToMany(mappedBy = "hopDong")
    private List<PhuLucHopDong> phuLucHopDongs;
}
