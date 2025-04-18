﻿-- Tạo database (chạy lần đầu, sau đó comment lại)
-- CREATE DATABASE QLVT;
-- GO
-- USE QLVT;
-- GO
--USE master;
--IF EXISTS (SELECT * FROM sys.databases WHERE name = 'QLVT')
--BEGIN
--    ALTER DATABASE QLVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
--    DROP DATABASE QLVT;
--END;

-- Tạo các bảng
-- 1. Tạo bảng CHUCVU
CREATE TABLE CHUCVU (
    MaChucVu VARCHAR(50) PRIMARY KEY,
    TenChucVu NVARCHAR(100) NOT NULL
);
GO

-- 2. Tạo bảng PHONGBAN
CREATE TABLE PHONGBAN (
    MaPhongBan VARCHAR(50) PRIMARY KEY,
    TenPhongBan NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(255) NOT NULL,
    MaTruongPhong VARCHAR(50) NULL
);
GO

-- 3. Tạo bảng NHANVIEN
CREATE TABLE NHANVIEN (
    MaNhanVien VARCHAR(50) PRIMARY KEY,
    TenNhanVien NVARCHAR(50) NOT NULL,
    MaChucVu VARCHAR(50) NOT NULL,
    MaPhongBan VARCHAR(50) NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    HinhAnh VARCHAR(200) NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Đang Làm Việc',
    CONSTRAINT CHK_NV_SoDienThoai CHECK (SoDienThoai LIKE '0[0-9]%' OR SoDienThoai LIKE '+84[0-9]%'),
    CONSTRAINT FK_NV_MaChucVu FOREIGN KEY (MaChucVu) REFERENCES CHUCVU (MaChucVu),
    CONSTRAINT FK_NV_MaPhongBan FOREIGN KEY (MaPhongBan) REFERENCES PHONGBAN (MaPhongBan)
);
GO

-- 4. Thêm khóa ngoại cho PHONGBAN (MaTruongPhong)
ALTER TABLE PHONGBAN
ADD CONSTRAINT FK_PB_MaTruongPhong FOREIGN KEY (MaTruongPhong) REFERENCES NHANVIEN (MaNhanVien) ON DELETE SET NULL;
GO

-- 5. Tạo bảng QUYENHAN
CREATE TABLE QUYENHAN (
    MaChucVu VARCHAR(50),
    QuanLy NVARCHAR(100),
    Xem BIT DEFAULT 0,
    XuatExcel BIT DEFAULT 0,
    Them BIT DEFAULT 0,
    Sua BIT DEFAULT 0,
    Xoa BIT DEFAULT 0,
    PRIMARY KEY (MaChucVu, QuanLy),
    CONSTRAINT FK_QH_MaChucVu FOREIGN KEY (MaChucVu) REFERENCES CHUCVU (MaChucVu)
);
GO

-- 6. Tạo bảng TAIKHOAN
CREATE TABLE TAIKHOAN (
    TaiKhoan VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(50) NOT NULL,
    MaNhanVien VARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Đang Chờ Xử Lý',
    CONSTRAINT FK_TK_MaNhanVien FOREIGN KEY (MaNhanVien) REFERENCES NHANVIEN (MaNhanVien)
);
GO

-- 7. Tạo bảng LOAIVATTU
CREATE TABLE LOAIVATTU (
    MaLoaiVatTu VARCHAR(10) PRIMARY KEY,
    TenLoaiVatTu NVARCHAR(100),
    NhomVatTu NVARCHAR(50)
);
GO

-- 8. Tạo bảng KHO
CREATE TABLE KHO (
    MaKho VARCHAR(10) PRIMARY KEY,
    TenKho VARCHAR(100) NOT NULL,
    MaLoaiVatTu VARCHAR(10) NULL,
    DiaChi NVARCHAR(255) NOT NULL,
    CONSTRAINT FK_KHO_MaLoaiVatTu FOREIGN KEY (MaLoaiVatTu) REFERENCES LOAIVATTU (MaLoaiVatTu)
);
GO

-- 9. Tạo bảng KHO_LOAIVATTU
CREATE TABLE KHO_LOAIVATTU (
    MaKho VARCHAR(10) NOT NULL,
    MaLoaiVatTu VARCHAR(10) NOT NULL,
    PRIMARY KEY (MaKho, MaLoaiVatTu),
    CONSTRAINT FK_KLV_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
    CONSTRAINT FK_KLV_MaLoaiVatTu FOREIGN KEY (MaLoaiVatTu) REFERENCES LOAIVATTU (MaLoaiVatTu)
);
GO

-- 10. Tạo bảng VATTU
CREATE TABLE VATTU (
    MaVatTu VARCHAR(10) PRIMARY KEY,
    TenVatTu NVARCHAR(100) NOT NULL,
    MaLoaiVatTu VARCHAR(10) NOT NULL,
    CONSTRAINT FK_VT_MaLoaiVatTu FOREIGN KEY (MaLoaiVatTu) REFERENCES LOAIVATTU (MaLoaiVatTu)
);
GO

-- 11. Tạo bảng TONKHO
CREATE TABLE TONKHO (
    MaKho VARCHAR(10) NOT NULL,
    MaVatTu VARCHAR(10) NOT NULL,
    SoLuong INT DEFAULT 0,
    DonVi NVARCHAR(20) NULL,
    ViTri NVARCHAR(255) NULL,
    CONSTRAINT PK_TK_MaKho_MaVatTu PRIMARY KEY (MaKho, MaVatTu),
    CONSTRAINT FK_TK_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
    CONSTRAINT FK_TK_MaVatTu FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
    CONSTRAINT CHK_TK_SoLuong CHECK (SoLuong >= 0)
);
GO

-- 12. Tạo bảng NHACUNGCAP
CREATE TABLE NHACUNGCAP (
    MaNhaCungCap VARCHAR(10) PRIMARY KEY,
    TenNhaCungCap NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    DiaChi NVARCHAR(255) NOT NULL,
    CONSTRAINT CHK_NCC_SoDienThoai CHECK (SoDienThoai LIKE '0[0-9]%' OR SoDienThoai LIKE '+84[0-9]%')
);
GO

-- 13. Tạo bảng DONVITINH
CREATE TABLE DonViTinh (
    MaDonVi INT PRIMARY KEY IDENTITY(1,1),
    TenDonVi NVARCHAR(50),
    NhomVatTu NVARCHAR(50)
);
GO

-- 14. Tạo bảng QUYDOIDONVI
CREATE TABLE QuyDoiDonVi (
    MaQuyDoi INT PRIMARY KEY IDENTITY(1,1),
    DonViGoc INT,
    DonViDich INT,
    HeSo FLOAT
);
GO

-- 15. Tạo bảng PHIEUNHAP
CREATE TABLE PHIEUNHAP (
    MaPhieuNhap VARCHAR(10) PRIMARY KEY,
    NgayNhap DATE NOT NULL,
    MaKho VARCHAR(10) NOT NULL,
    MaNhaCungCap VARCHAR(10) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Chờ duyệt',
    CONSTRAINT FK_PN_MaNhaCungCap FOREIGN KEY (MaNhaCungCap) REFERENCES NHACUNGCAP (MaNhaCungCap) ON DELETE NO ACTION,
    CONSTRAINT FK_PN_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho) ON DELETE NO ACTION
);
GO

-- 16. Tạo bảng CHITIETPHIEUNHAP
CREATE TABLE CHITIETPHIEUNHAP (
    MaPhieuNhap VARCHAR(10),
    MaVatTu VARCHAR(10),
    DonViChuyenDoi INT,
    DonViGoc INT,
    SoLuongNhap FLOAT,
    SoLuongQuyDoi FLOAT,
    PRIMARY KEY (MaPhieuNhap, MaVatTu),
    CONSTRAINT FK_CTPN_MaPhieuNhap FOREIGN KEY (MaPhieuNhap) REFERENCES PHIEUNHAP (MaPhieuNhap),
    CONSTRAINT FK_CTPN_MaVatTu FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
    CONSTRAINT FK_CTPN_DonViGoc FOREIGN KEY (DonViGoc) REFERENCES DonViTinh (MaDonVi),
    CONSTRAINT FK_CTPN_DonViChuyenDoi FOREIGN KEY (DonViChuyenDoi) REFERENCES DonViTinh (MaDonVi),
    CONSTRAINT CHK_CTPN_SoLuongNhap CHECK (SoLuongNhap > 0),
    CONSTRAINT CHK_CTPN_SoLuongQuyDoi CHECK (SoLuongQuyDoi > 0)
);
GO

-- 17. Tạo bảng PHIEUYEUCAUVATTU
CREATE TABLE PHIEUYEUCAUVATTU (
    MaPhieuYeuCauVatTu VARCHAR(10) PRIMARY KEY,
    NgayYeuCau DATE DEFAULT GETDATE(),
    MaKho VARCHAR(10),
    MaPhongBanYeuCau VARCHAR(50),
    MaNguoiYeuCau VARCHAR(50),
    MaNguoiDuyet VARCHAR(50),
    LyDo NVARCHAR(255) NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Chờ duyệt',
    CONSTRAINT FK_PYCVT_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
    CONSTRAINT FK_PYCVT_MaPhongBan FOREIGN KEY (MaPhongBanYeuCau) REFERENCES PHONGBAN (MaPhongBan),
    CONSTRAINT FK_PYCVT_MaNguoiYeuCau FOREIGN KEY (MaNguoiYeuCau) REFERENCES NHANVIEN (MaNhanVien),
    CONSTRAINT FK_PYCVT_MaNguoiDuyet FOREIGN KEY (MaNguoiDuyet) REFERENCES NHANVIEN (MaNhanVien)
);
GO

-- 18. Tạo bảng CHITIETPHIEUYEUCAUVATTU
CREATE TABLE CHITIETPHIEUYEUCAUVATTU (
    MaPhieuYeuCauVatTu VARCHAR(10),
    MaVatTu VARCHAR(10),
    SoLuong INT,
    PRIMARY KEY (MaPhieuYeuCauVatTu, MaVatTu),
    CONSTRAINT FK_CTPYCVT_MaPhieuYeuCau FOREIGN KEY (MaPhieuYeuCauVatTu) REFERENCES PHIEUYEUCAUVATTU (MaPhieuYeuCauVatTu),
    CONSTRAINT FK_CTPYCVT_MaVatTu FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
    CONSTRAINT CHK_CTPYCVT_SoLuong CHECK (SoLuong > 0)
);
GO

-- 19. Tạo bảng PHIEUXUAT
CREATE TABLE PHIEUXUAT (
    MaPhieuXuat VARCHAR(10) PRIMARY KEY,
    NgayChungTu DATE NOT NULL,
    NgayXuat DATE NOT NULL,
    MaKho VARCHAR(10),
    MaPhongBanYeuCau VARCHAR(50),
    MaNguoiXuat VARCHAR(50),
    MaNguoiNhan VARCHAR(50),
    LyDo NVARCHAR(255) NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Chờ Duyệt',
    CONSTRAINT FK_PX_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
    CONSTRAINT FK_PX_MaPhongBan FOREIGN KEY (MaPhongBanYeuCau) REFERENCES PHONGBAN (MaPhongBan),
    CONSTRAINT FK_PX_MaNguoiXuat FOREIGN KEY (MaNguoiXuat) REFERENCES NHANVIEN (MaNhanVien),
    CONSTRAINT FK_PX_MaNguoiNhan FOREIGN KEY (MaNguoiNhan) REFERENCES NHANVIEN (MaNhanVien)
);
GO

-- 20. Tạo bảng CHITIETPHIEUXUAT
CREATE TABLE CHITIETPHIEUXUAT (
    MaPhieuXuat VARCHAR(10),
    MaVatTu VARCHAR(10),
    DonViChuyenDoi INT,
    SoLuongXuat FLOAT,
    DonViGoc INT,
    SoLuongQuyDoi FLOAT,
    PRIMARY KEY (MaPhieuXuat, MaVatTu),
    CONSTRAINT FK_CTPX_MaPhieuXuat FOREIGN KEY (MaPhieuXuat) REFERENCES PHIEUXUAT (MaPhieuXuat),
    CONSTRAINT FK_CTPX_MaVatTu FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
    CONSTRAINT FK_CTPX_DonViChuyenDoi FOREIGN KEY (DonViChuyenDoi) REFERENCES DonViTinh (MaDonVi),
    CONSTRAINT FK_CTPX_DonViGoc FOREIGN KEY (DonViGoc) REFERENCES DonViTinh (MaDonVi)
);
GO

-- 21. Tạo bảng LICHSUHOATDONG
CREATE TABLE LICHSUHOATDONG (
    MaLichSu VARCHAR(50) PRIMARY KEY,
    ThoiGian DATE NOT NULL,
    MaNhanVien VARCHAR(50) NOT NULL,
    TenNhanVien NVARCHAR(50) NOT NULL,
    ChucVu NVARCHAR(50) NOT NULL,
    ThaoTac NVARCHAR(50) NOT NULL,
    QuanLy NVARCHAR(50) NOT NULL,
    NoiDungThaoTac NVARCHAR(255) NOT NULL,
    CONSTRAINT FK_LSHOATDONG_MaNhanVien FOREIGN KEY (MaNhanVien) REFERENCES NHANVIEN (MaNhanVien)
);
GO

-- 22. Tạo bảng BAOHANH
CREATE TABLE BAOHANH (
    MaVatTu VARCHAR(10) NOT NULL,
    MaNhaCungCap VARCHAR(10) NOT NULL,
    MaKho VARCHAR(10),
    TenVatTu NVARCHAR(100),
    TrangThai NVARCHAR(50),
    PRIMARY KEY (MaVatTu, MaNhaCungCap),
    CONSTRAINT FK_BAOHANH_VATTU FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
    CONSTRAINT FK_BAOHANH_NCC FOREIGN KEY (MaNhaCungCap) REFERENCES NHACUNGCAP (MaNhaCungCap)
);
GO

-- 23. Tạo hàm LayHeSoQuyDoi
CREATE FUNCTION dbo.LayHeSoQuyDoi (@DonViGoc INT, @DonViDich INT)
RETURNS FLOAT
AS
BEGIN
    DECLARE @HeSo FLOAT;
    SELECT @HeSo = HeSo
    FROM QuyDoiDonVi
    WHERE DonViGoc = @DonViGoc AND DonViDich = @DonViDich;
    RETURN ISNULL(@HeSo, 1);
END;
GO