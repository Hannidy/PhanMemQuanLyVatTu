--﻿USE QLVT;
--GO

-- 1. Xóa dữ liệu để làm sạch
--DELETE FROM BAOHANH;
--DELETE FROM LICHSUHOATDONG;
--DELETE FROM CHITIETPHIEUXUAT;
--DELETE FROM PHIEUXUAT;
--DELETE FROM CHITIETPHIEUYEUCAUVATTU;
--DELETE FROM PHIEUYEUCAUVATTU;
--DELETE FROM CHITIETPHIEUNHAP;
--DELETE FROM PHIEUNHAP;
--DELETE FROM QuyDoiDonVi;
--DELETE FROM DonViTinh;
--DELETE FROM NHACUNGCAP;
--DELETE FROM TONKHO;
--DELETE FROM VATTU;
--DELETE FROM KHO_LOAIVATTU;
--DELETE FROM KHO;
--DELETE FROM LOAIVATTU;
--DELETE FROM TAIKHOAN;
--DELETE FROM QUYENHAN;
--DELETE FROM NHANVIEN;
--DELETE FROM PHONGBAN;
--DELETE FROM CHUCVU;
GO

-- 2. Thêm dữ liệu vào CHUCVU
INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES
    ('CV01', N'Bộ trưởng Bộ Giáo dục và Đào tạo'),
    ('CV02', N'Tổng giám đốc công ty Two Fingers'),
    ('CV03', N'Tổng giám đốc công ty lò vi sóng'),
    ('CV04', N'Tổng vệ sinh'),
    ('CV05', N'Tổng quản lý phân phối các giống chó Phú Quốc'),
    ('CV06', N'Tổng giám đốc công ty nguyên vật liệu đường tàu kiêm giám đốc công ty cổ phần bột rau má'),
    ('CV07', N'Tổng giám đốc công ty phân phối dầu, mỡ cho phương tiện di chuyển'),
    ('CV08', N'Chủ tịch sữa chua'),
    ('CV09', N'Kế Toán 1'),
    ('CV10', N'Kế toán 2'),
    ('CV11', N'Nhân Viên Quèn'),
    ('CV12', N'Nhân Viên Siêu Quèn 1'),
    ('CV13', N'Nhân Viên Siêu Quèn 2'),
    ('CV14', N'Nhân Viên Siêu Quèn 3'),
    ('CV15', N'Nhân Viên Siêu Quèn 4'),
    ('CV16', N'Nhân Viên Siêu Quèn 5'),
    ('CV17', N'Nhân Viên Siêu Quèn 6'),
    ('CV18', N'Nhân Viên Siêu Quèn 7'),
    ('CV19', N'Nhân Viên Siêu Quèn 8'),
    ('CV20', N'Nhân Viên Siêu Quèn 9'),
    ('CV21', N'Nhân Viên Siêu Quèn 10'),
    ('CV22', N'Nhân Viên Siêu Quèn 11'),
    ('CV23', N'Nhân Viên Siêu Quèn 12'),
    ('CV24', N'Nhân Viên Siêu Quèn 13');
GO

-- 3. Thêm dữ liệu vào PHONGBAN
INSERT INTO PHONGBAN (MaPhongBan, TenPhongBan, DiaChi, MaTruongPhong) VALUES
    ('PB01', N'Phòng kho', N'123 Nguyễn Văn Cừ', NULL),
    ('PB02', N'Phòng kế toán', N'456 Lê Lợi', NULL),
    ('PB03', N'Phòng hành chính', N'789 Hai Bà Trưng', NULL),
    ('PB04', N'Phòng kỹ thuật', N'101 Trần Phú', NULL),
    ('PB05', N'Phòng kinh doanh', N'202 Phạm Ngũ Lão', NULL);
GO

-- 4. Thêm dữ liệu vào NHANVIEN
INSERT INTO NHANVIEN (MaNhanVien, TenNhanVien, MaChucVu, MaPhongBan, Email, SoDienThoai, HinhAnh, TrangThai) VALUES
    ('NV01', N'Thầy Thiện Đạt', 'CV01', 'PB01', 'bsquocdat@gmail.com', '0911111111', NULL, N'Đang Làm Việc'),
    ('NV02', N'Đinh Tuấn Duy', 'CV02', 'PB01', 'duy@gmail.com', '0922222222', NULL, N'Đang Làm Việc'),
    ('NV03', N'Bùi Minh Hiếu', 'CV03', 'PB01', 'hieu@gmail.com', '0933333333', NULL, N'Đang Làm Việc'),
    ('NV04', N'Hoàng Đặng Anh Khoa', 'CV04', 'PB01', 'khoa@gmail.com', '0944444444', NULL, N'Đang Làm Việc'),
    ('NV05', N'Hà Tuấn Vũ', 'CV05', 'PB01', 'vu@gmail.com', '0955555555', NULL, N'Đang Làm Việc'),
    ('NV06', N'Trần Trọng Nghĩa', 'CV06', 'PB01', 'nghia@gmail.com', '0966666666', NULL, N'Đang Làm Việc'),
    ('NV07', N'Phạm Thái Lâm Khánh', 'CV07', 'PB01', 'khanh@gmail.com', '0977777777', NULL, N'Đang Làm Việc'),
    ('NV08', N'Đoàn Ngọc Dương', 'CV08', 'PB01', 'duong@gmail.com', '0988888888', NULL, N'Đang Làm Việc'),
    ('NV09', N'a', 'CV09', 'PB02', 'a@gmail.com', '0811111111', NULL, N'Đang Làm Việc'),
    ('NV10', N'b', 'CV10', 'PB02', 'b@gmail.com', '0822222222', NULL, N'Đang Làm Việc'),
    ('NV11', N'c', 'CV11', 'PB02', 'c@gmail.com', '0833333333', NULL, N'Đang Làm Việc'),
    ('NV12', N'd', 'CV12', 'PB02', 'd@gmail.com', '0844444444', NULL, N'Đã Nghỉ Việc'),
    ('NV13', N'e', 'CV13', 'PB03', 'e@gmail.com', '0711111111', NULL, N'Đang Làm Việc'),
    ('NV14', N'f', 'CV14', 'PB03', 'f@gmail.com', '0722222222', NULL, N'Đang Làm Việc'),
    ('NV15', N'g', 'CV15', 'PB03', 'g@gmail.com', '0733333333', NULL, N'Đang Làm Việc'),
    ('NV16', N'h', 'CV16', 'PB03', 'h@gmail.com', '0611111111', NULL, N'Đã Nghỉ Việc'),
    ('NV17', N'i', 'CV17', 'PB03', 'i@gmail.com', '0622222222', NULL, N'Đang Làm Việc'),
    ('NV18', N'j', 'CV18', 'PB03', 'j@gmail.com', '0633333333', NULL, N'Đang Làm Việc'),
    ('NV19', N'k', 'CV19', 'PB03', 'k@gmail.com', '0644444444', NULL, N'Đang Làm Việc'),
    ('NV20', N'l', 'CV20', 'PB03', 'l@gmail.com', '0511111111', NULL, N'Đã Nghỉ Việc'),
    ('NV21', N'm', 'CV21', 'PB03', 'm@gmail.com', '0522222222', NULL, N'Đang Làm Việc'),
    ('NV22', N'n', 'CV22', 'PB03', 'n@gmail.com', '0533333333', NULL, N'Đang Làm Việc'),
    ('NV23', N'o', 'CV23', 'PB03', 'o@gmail.com', '0411111111', NULL, N'Đang Làm Việc'),
    ('NV24', N'p', 'CV24', 'PB03', 'p@gmail.com', '0422222222', NULL, N'Đã Nghỉ Việc');
GO

-- 5. Cập nhật MaTruongPhong cho PHONGBAN
UPDATE PHONGBAN SET MaTruongPhong = 'NV01' WHERE MaPhongBan = 'PB01';
UPDATE PHONGBAN SET MaTruongPhong = 'NV03' WHERE MaPhongBan = 'PB02';
UPDATE PHONGBAN SET MaTruongPhong = 'NV04' WHERE MaPhongBan = 'PB03';
GO

-- 6. Thêm dữ liệu vào QUYENHAN
INSERT INTO QUYENHAN (MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa) VALUES
    ('CV01', N'Quản Lý Loại Vật Tư', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Vật Tư', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Đơn Vị Tính', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Vật Tư Lỗi - Bảo Hành', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Kho', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Kho - Loại Vật Tư', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Tồn Kho', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Nhân Viên', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Chức Vụ', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Quyền Hạn', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Tài Khoản', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Phiếu Nhập', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Phiếu Yêu Cầu Vật Tư', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Phiếu Xuất', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Phòng Ban', 1, 1, 1, 1, 1),
    ('CV01', N'Quản Lý Nhà Cung Cấp', 1, 1, 1, 1, 1),
    ('CV01', N'Lịch Sử Hoạt Động', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Loại Vật Tư', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Vật Tư', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Đơn Vị Tính', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Vật Tư Lỗi - Bảo Hành', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Kho', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Kho - Loại Vật Tư', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Tồn Kho', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Phiếu Nhập', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Phiếu Yêu Cầu Vật Tư', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Phiếu Xuất', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Phòng Ban', 1, 1, 1, 1, 1),
    ('CV02', N'Quản Lý Nhà Cung Cấp', 1, 1, 1, 1, 1),
    ('CV02', N'Lịch Sử Hoạt Động', 1, 1, 1, 1, 1);
GO

-- 7. Thêm dữ liệu vào TAIKHOAN
INSERT INTO TAIKHOAN (TaiKhoan, MatKhau, MaNhanVien, TrangThai) VALUES
    ('Admin', 'c4ca4238a0b923820dcc509a6f75849b', 'NV01', N'Hoạt Động'),
    ('duy', 'c81e728d9d4c2f636f067f89cc14862c', 'NV02', N'Hoạt Động'),
    ('hieu', 'c81e728d9d4c2f636f067f89cc14862c', 'NV03', N'Hoạt Động'),
    ('khoa', 'c81e728d9d4c2f636f067f89cc14862c', 'NV04', N'Hoạt Động'),
    ('vu', 'c81e728d9d4c2f636f067f89cc14862c', 'NV05', N'Hoạt Động'),
    ('nghia', 'c81e728d9d4c2f636f067f89cc14862c', 'NV06', N'Hoạt Động'),
    ('khanh', 'c81e728d9d4c2f636f067f89cc14862c', 'NV07', N'Hoạt Động'),
    ('duong', 'c81e728d9d4c2f636f067f89cc14862c', 'NV08', N'Không Hoạt Động'),
    ('a', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV09', N'Hoạt Động'),
    ('b', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV10', N'Hoạt Động'),
    ('c', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV11', N'Hoạt Động'),
    ('d', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV12', N'Đang Chờ Xử Lý'),
    ('e', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV13', N'Hoạt Động'),
    ('f', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', 'NV14', N'Hoạt Động');
GO

-- 8. Thêm dữ liệu vào LOAIVATTU
INSERT INTO LOAIVATTU (MaLoaiVatTu, TenLoaiVatTu, NhomVatTu) VALUES
    ('KL01', N'Sắt', N'Khối Lượng'),
    ('KL02', N'Thép', N'Khối Lượng'),
    ('KL03', N'Xi măng', N'Khối Lượng'),
    ('KL04', N'Gạo', N'Khối Lượng'),
    ('KL05', N'Phân bón', N'Khối Lượng'),
    ('TT01', N'Nước', N'Thể Tích'),
    ('TT02', N'Dầu', N'Thể Tích'),
    ('TT03', N'Sơn', N'Thể Tích'),
    ('TT04', N'Xăng', N'Thể Tích'),
    ('CD01', N'Thép cây', N'Chiều Dài'),
    ('CD02', N'Ống nhựa', N'Chiều Dài'),
    ('CD03', N'Dây điện', N'Chiều Dài'),
    ('DG01', N'Gỗ xẻ kiện', N'Đơn Vị Đóng Gói'),
    ('DG02', N'Hộp đinh', N'Đơn Vị Đóng Gói'),
    ('DG03', N'Bịch ốc vít', N'Đơn Vị Đóng Gói'),
    ('DG04', N'Thùng sơn', N'Đơn Vị Đóng Gói'),
    ('DG05', N'Hộp khẩu trang', N'Đơn Vị Đóng Gói');
GO

-- 9. Thêm dữ liệu vào KHO
INSERT INTO KHO (MaKho, TenKho, MaLoaiVatTu, DiaChi) VALUES
    ('K01', 'Kho A', 'KL01', N'123 Đường A'),
    ('K02', 'Kho B', 'TT01', N'456 Đường B'),
    ('K03', 'Kho C', 'CD01', N'789 Đường C'),
    ('K04', 'Kho D', 'DG01', N'101 Đường D'),
    ('K05', 'Kho E', 'KL02', N'112 Đường E');
GO

-- 10. Thêm dữ liệu vào KHO_LOAIVATTU
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
    ('K01', 'KL01'),
    ('K02', 'TT01'),
    ('K03', 'CD01'),
    ('K04', 'DG01'),
    ('K05', 'KL02');
GO

-- 11. Thêm dữ liệu vào VATTU
INSERT INTO VATTU (MaVatTu, TenVatTu, MaLoaiVatTu) VALUES
    ('VT01', N'Sắt cây phi 10', 'KL01'),
    ('VT02', N'Thép cuộn', 'KL02'),
    ('VT03', N'Bao xi măng Hà Tiên', 'KL03'),
    ('VT04', N'Gạo ST25', 'KL04'),
    ('VT05', N'Phân NPK', 'KL05'),
    ('VT06', N'Nước tinh khiết 20L', 'TT01'),
    ('VT07', N'Dầu nhớt động cơ', 'TT02'),
    ('VT08', N'Sơn Dulux 18L', 'TT03'),
    ('VT09', N'Xăng A95', 'TT04'),
    ('VT10', N'Thép cây dài 6m', 'CD01'),
    ('VT11', N'Ống nhựa PVC D60', 'CD02'),
    ('VT12', N'Dây điện Cadivi 2.5', 'CD03'),
    ('VT13', N'Kiện gỗ xẻ 1m3', 'DG01'),
    ('VT14', N'Hộp đinh 2cm', 'DG02'),
    ('VT15', N'Bịch ốc vít 500g', 'DG03'),
    ('VT16', N'Thùng sơn Jotun', 'DG04'),
    ('VT17', N'Hộp khẩu trang y tế', 'DG05');
GO

-- 12. Thêm dữ liệu vào TONKHO
INSERT INTO TONKHO (MaKho, MaVatTu, SoLuong, DonVi, ViTri) VALUES
    ('K01', 'VT01', 100, N'kg', N'Kệ A1'),
    ('K02', 'VT06', 200, N'lít', N'Kệ B2'),
    ('K03', 'VT10', 300, N'm', N'Kệ C3'),
    ('K04', 'VT13', 400, N'kiện', N'Kệ D4'),
    ('K05', 'VT02', 500, N'kg', N'Kệ E5');
GO

-- 13. Thêm dữ liệu vào NHACUNGCAP
INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoai, Email, DiaChi) VALUES
    ('NCC01', N'Công ty A', '0901234567', 'ncc1@gmail.com', N'123 Lý Thường Kiệt'),
    ('NCC02', N'Công ty B', '0912345678', 'ncc2@gmail.com', N'456 Nguyễn Huệ'),
    ('NCC03', N'Công ty C', '0923456789', 'ncc3@gmail.com', N'789 Hai Bà Trưng'),
    ('NCC04', N'Công ty D', '0934567890', 'ncc4@gmail.com', N'101 Trần Phú'),
    ('NCC05', N'Công ty E', '0945678901', 'ncc5@gmail.com', N'202 Phạm Ngũ Lão');
GO

-- 14. Thêm dữ liệu vào DONVITINH
-- Bật IDENTITY_INSERT để chỉ định MaDonVi
SET IDENTITY_INSERT DonViTinh ON;

-- Thêm dữ liệu mẫu
INSERT INTO DonViTinh (MaDonVi, TenDonVi, NhomVatTu) VALUES
    -- Khối Lượng
    (1, N'Tấn', N'Khối Lượng'),
    (2, N'Kilogram', N'Khối Lượng'),
    (3, N'Gram', N'Khối Lượng'),
    -- Chiều Dài
    (4, N'Kilomet', N'Chiều Dài'),
    (5, N'Mét', N'Chiều Dài'),
    (6, N'Centimet', N'Chiều Dài'),
    -- Thể Tích
    (7, N'Mét khối', N'Thể Tích'),
    (8, N'Lít', N'Thể Tích'),
    (9, N'Mililit', N'Thể Tích'),
    -- Đơn Vị Đóng Gói
    (10, N'Kiện', N'Đơn Vị Đóng Gói'),
    (11, N'Thùng', N'Đơn Vị Đóng Gói'),
    (12, N'Hộp', N'Đơn Vị Đóng Gói'),
    (13, N'Cái', N'Đơn Vị Đóng Gói');

-- Tắt IDENTITY_INSERT
SET IDENTITY_INSERT DonViTinh OFF;

-- Kiểm tra DonViTinh
IF (SELECT COUNT(*) FROM DonViTinh) != 13
BEGIN
    RAISERROR ('Bảng DonViTinh không có đủ 13 bản ghi!', 16, 1);
    RETURN;
END
GO

-- Thêm dữ liệu mẫu
INSERT INTO QuyDoiDonVi (DonViGoc, DonViDich, HeSo) VALUES
    -- Khối Lượng
    (1, 2, 1000.0),     -- Tấn → Kilogram
    (2, 1, 0.001),      -- Kilogram → Tấn
    (2, 3, 1000.0),     -- Kilogram → Gram
    (3, 2, 0.001),      -- Gram → Kilogram
    (1, 3, 1000000.0),  -- Tấn → Gram
    (3, 1, 0.000001),   -- Gram → Tấn
    -- Chiều Dài
    (4, 5, 1000.0),     -- Kilomet → Mét
    (5, 4, 0.001),      -- Mét → Kilomet
    (5, 6, 100.0),      -- Mét → Centimet
    (6, 5, 0.01),       -- Centimet → Mét
    (4, 6, 100000.0),   -- Kilomet → Centimet
    (6, 4, 0.00001),    -- Centimet → Kilomet
    -- Thể Tích
    (7, 8, 1000.0),     -- Mét khối → Lít
    (8, 7, 0.001),      -- Lít → Mét khối
    (8, 9, 1000.0),     -- Lít → Mililit
    (9, 8, 0.001),      -- Mililit → Lít
    (7, 9, 1000000.0),  -- Mét khối → Mililit
    (9, 7, 0.000001),   -- Mililit → Mét khối
    -- Đơn Vị Đóng Gói
    (10, 11, 10.0),     -- Kiện → Thùng
    (11, 10, 0.1),      -- Thùng → Kiện
    (11, 12, 24.0),     -- Thùng → Hộp
    (12, 11, 1.0/24),   -- Hộp → Thùng
    (12, 13, 12.0),     -- Hộp → Cái
    (13, 12, 1.0/12),   -- Cái → Hộp
    (10, 12, 240.0),    -- Kiện → Hộp
    (12, 10, 1.0/240),  -- Hộp → Kiện
    (10, 13, 2880.0),   -- Kiện → Cái
    (13, 10, 1.0/2880), -- Cái → Kiện
    (11, 13, 288.0),    -- Thùng → Cái
    (13, 11, 1.0/288);  -- Cái → Thùng

-- Kiểm tra QuyDoiDonVi
IF (SELECT COUNT(*) FROM QuyDoiDonVi) != 30
BEGIN
    RAISERROR ('Bảng QuyDoiDonVi không có đủ 30 bản ghi!', 16, 1);
    RETURN;
END
GO

-- 16. Thêm dữ liệu vào PHIEUNHAP
INSERT INTO PHIEUNHAP (MaPhieuNhap, NgayNhap, MaKho, MaNhaCungCap, TrangThai) VALUES
    ('PN01', '2024-12-01', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN02', '2024-10-02', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN03', '2024-11-03', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN04', '2024-12-04', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN05', '2024-12-05', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN06', '2024-12-02', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN07', '2024-12-03', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN08', '2024-12-04', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN09', '2024-12-05', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN10', '2024-12-06', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN11', '2024-12-07', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN12', '2024-12-08', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN13', '2024-12-09', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN14', '2024-12-10', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN15', '2024-12-11', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN16', '2024-12-12', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN17', '2024-12-13', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN18', '2024-12-14', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN19', '2024-12-15', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN20', '2024-12-16', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN21', '2024-12-17', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN22', '2024-12-18', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN23', '2024-12-19', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN24', '2024-12-20', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN25', '2024-12-21', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN26', '2024-12-22', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN27', '2024-12-23', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN28', '2024-12-24', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN29', '2024-12-25', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN30', '2024-12-26', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN31', '2024-12-27', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN32', '2024-12-28', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN33', '2024-12-29', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN34', '2024-12-30', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN35', '2024-12-31', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN36', '2025-01-01', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN37', '2025-01-02', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN38', '2025-01-03', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN39', '2025-01-04', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN40', '2025-01-05', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN41', '2025-01-06', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN42', '2025-01-07', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN43', '2025-01-08', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN44', '2025-01-09', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN45', '2025-01-10', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN46', '2025-01-11', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN47', '2025-01-12', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN48', '2025-01-13', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN49', '2025-01-14', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN50', '2025-01-15', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN51', '2025-01-16', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN52', '2025-01-17', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN53', '2025-01-18', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN54', '2025-01-19', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN55', '2025-01-20', 'K05', 'NCC05', N'Chờ duyệt');
GO

-- 17. Thêm dữ liệu vào CHITIETPHIEUNHAP
INSERT INTO CHITIETPHIEUNHAP (MaPhieuNhap, MaVatTu, DonViChuyenDoi, DonViGoc, SoLuongNhap, SoLuongQuyDoi) VALUES
    ('PN01', 'VT01', 2, 2, 50, 50),    -- Kilogram thay cho kg
    ('PN02', 'VT06', 8, 8, 100, 100),   -- Lít thay cho lít
    ('PN03', 'VT10', 5, 5, 150, 150),   -- Mét thay cho m
    ('PN04', 'VT13', 10, 10, 200, 200), -- Kiện thay cho kiện
    ('PN05', 'VT02', 13, 13, 250, 250), -- Cái thay cho cái
    ('PN06', 'VT01', 1, 1, 50, 50),
    ('PN07', 'VT02', 2, 2, 60, 60),
    ('PN08', 'VT03', 3, 3, 70, 70),
    ('PN09', 'VT04', 4, 4, 80, 80),
    ('PN10', 'VT05', 5, 5, 90, 90),
    ('PN11', 'VT06', 1, 1, 100, 100),
    ('PN12', 'VT07', 2, 2, 110, 110),
    ('PN13', 'VT08', 3, 3, 120, 120),
    ('PN14', 'VT09', 4, 4, 130, 130),
    ('PN15', 'VT10', 5, 5, 140, 140),
    ('PN16', 'VT11', 1, 1, 150, 150),
    ('PN17', 'VT12', 2, 2, 160, 160),
    ('PN18', 'VT13', 3, 3, 170, 170),
    ('PN19', 'VT14', 4, 4, 180, 180),
    ('PN20', 'VT15', 5, 5, 190, 190),
    ('PN21', 'VT16', 1, 1, 200, 200),
    ('PN22', 'VT17', 2, 2, 210, 210),
    ('PN23', 'VT01', 3, 3, 220, 220),
    ('PN24', 'VT02', 4, 4, 230, 230),
    ('PN25', 'VT03', 5, 5, 240, 240),
    ('PN26', 'VT04', 1, 1, 250, 250),
    ('PN27', 'VT05', 2, 2, 260, 260),
    ('PN28', 'VT06', 3, 3, 270, 270),
    ('PN29', 'VT07', 4, 4, 280, 280),
    ('PN30', 'VT08', 5, 5, 290, 290),
    ('PN31', 'VT09', 1, 1, 300, 300),
    ('PN32', 'VT10', 2, 2, 310, 310),
    ('PN33', 'VT11', 3, 3, 320, 320),
    ('PN34', 'VT12', 4, 4, 330, 330),
    ('PN35', 'VT13', 5, 5, 340, 340),
    ('PN36', 'VT14', 1, 1, 350, 350),
    ('PN37', 'VT15', 2, 2, 360, 360),
    ('PN38', 'VT16', 3, 3, 370, 370),
    ('PN39', 'VT17', 4, 4, 380, 380),
    ('PN40', 'VT01', 5, 5, 390, 390),
    ('PN41', 'VT02', 1, 1, 400, 400),
    ('PN42', 'VT03', 2, 2, 410, 410),
    ('PN43', 'VT04', 3, 3, 420, 420),
    ('PN44', 'VT05', 4, 4, 430, 430),
    ('PN45', 'VT06', 5, 5, 440, 440),
    ('PN46', 'VT07', 1, 1, 450, 450),
    ('PN47', 'VT08', 2, 2, 460, 460),
    ('PN48', 'VT09', 3, 3, 470, 470),
    ('PN49', 'VT10', 4, 4, 480, 480),
    ('PN50', 'VT11', 5, 5, 490, 490),
    ('PN51', 'VT12', 1, 1, 500, 500),
    ('PN52', 'VT13', 2, 2, 510, 510),
    ('PN53', 'VT14', 3, 3, 520, 520),
    ('PN54', 'VT15', 4, 4, 530, 530),
    ('PN55', 'VT16', 5, 5, 540, 540);
GO

-- 18. Thêm dữ liệu vào PHIEUYEUCAUVATTU
INSERT INTO PHIEUYEUCAUVATTU (MaPhieuYeuCauVatTu, NgayYeuCau, MaKho, MaPhongBanYeuCau, MaNguoiYeuCau, MaNguoiDuyet, LyDo, TrangThai) VALUES
    ('PYC01', '2024-12-01', 'K01', 'PB01', 'NV01', 'NV04', N'Cần vật tư cho sản xuất', N'Chờ duyệt'),
    ('PYC02', '2024-12-02', 'K02', 'PB02', 'NV02', 'NV04', N'Cần vật tư cho bảo trì', N'Chờ duyệt'),
    ('PYC03', '2024-12-03', 'K03', 'PB03', 'NV03', 'NV04', N'Cần vật tư cho dự án', N'Chờ duyệt'),
    ('PYC04', '2024-12-04', 'K04', 'PB04', 'NV04', 'NV01', N'Cần vật tư cho xây dựng', N'Chờ duyệt'),
    ('PYC05', '2024-12-05', 'K05', 'PB05', 'NV05', 'NV01', N'Cần vật tư cho nghiên cứu', N'Chờ duyệt');
GO

-- 19. Thêm dữ liệu vào CHITIETPHIEUYEUCAUVATTU
INSERT INTO CHITIETPHIEUYEUCAUVATTU (MaPhieuYeuCauVatTu, MaVatTu, SoLuong) VALUES
    ('PYC01', 'VT01', 10),
    ('PYC02', 'VT06', 20),
    ('PYC03', 'VT10', 30),
    ('PYC04', 'VT13', 40),
    ('PYC05', 'VT02', 50);
GO

-- 20. Thêm dữ liệu vào PHIEUXUAT
INSERT INTO PHIEUXUAT (MaPhieuXuat, NgayChungTu, NgayXuat, MaKho, MaPhongBanYeuCau, MaNguoiXuat, MaNguoiNhan, LyDo, TrangThai) VALUES
    ('PX01', '2024-12-01', '2024-12-02', 'K01', 'PB01', 'NV01', 'NV02', N'Xuất vật tư cho sản xuất', N'Chờ Duyệt'),
    ('PX02', '2024-12-02', '2024-12-03', 'K02', 'PB02', 'NV02', 'NV03', N'Xuất vật tư cho bảo trì', N'Chờ Duyệt'),
    ('PX03', '2024-12-03', '2024-12-04', 'K03', 'PB03', 'NV03', 'NV04', N'Xuất vật tư cho dự án', N'Chờ Duyệt'),
    ('PX04', '2024-12-04', '2024-12-05', 'K04', 'PB04', 'NV04', 'NV05', N'Xuất vật tư cho xây dựng', N'Chờ Duyệt'),
    ('PX05', '2024-12-05', '2024-12-06', 'K05', 'PB05', 'NV05', 'NV01', N'Xuất vật tư cho nghiên cứu', N'Chờ Duyệt'),
    ('PX06', '2024-12-02', '2024-12-03', 'K01', 'PB01', 'NV07', 'NV08', N'Xuất vật tư cho dự án 6', N'Chờ Duyệt'),
    ('PX07', '2024-12-03', '2024-12-04', 'K02', 'PB02', 'NV08', 'NV09', N'Xuất vật tư cho dự án 7', N'Chờ Duyệt'),
    ('PX08', '2024-12-04', '2024-12-05', 'K03', 'PB03', 'NV09', 'NV10', N'Xuất vật tư cho dự án 8', N'Chờ Duyệt'),
    ('PX09', '2024-12-05', '2024-12-06', 'K04', 'PB04', 'NV10', 'NV11', N'Xuất vật tư cho dự án 9', N'Chờ Duyệt'),
    ('PX10', '2024-12-06', '2024-12-07', 'K05', 'PB05', 'NV11', 'NV12', N'Xuất vật tư cho dự án 10', N'Chờ Duyệt'),
    ('PX11', '2024-12-07', '2024-12-08', 'K01', 'PB01', 'NV12', 'NV13', N'Xuất vật tư cho dự án 11', N'Chờ Duyệt'),
    ('PX12', '2024-12-08', '2024-12-09', 'K02', 'PB02', 'NV13', 'NV14', N'Xuất vật tư cho dự án 12', N'Chờ Duyệt'),
    ('PX13', '2024-12-09', '2024-12-10', 'K03', 'PB03', 'NV14', 'NV15', N'Xuất vật tư cho dự án 13', N'Chờ Duyệt'),
    ('PX14', '2024-12-10', '2024-12-11', 'K04', 'PB04', 'NV15', 'NV16', N'Xuất vật tư cho dự án 14', N'Chờ Duyệt'),
    ('PX15', '2024-12-11', '2024-12-12', 'K05', 'PB05', 'NV16', 'NV17', N'Xuất vật tư cho dự án 15', N'Chờ Duyệt'),
    ('PX16', '2024-12-12', '2024-12-13', 'K01', 'PB01', 'NV17', 'NV18', N'Xuất vật tư cho dự án 16', N'Chờ Duyệt'),
    ('PX17', '2024-12-13', '2024-12-14', 'K02', 'PB02', 'NV18', 'NV19', N'Xuất vật tư cho dự án 17', N'Chờ Duyệt'),
    ('PX18', '2024-12-14', '2024-12-15', 'K03', 'PB03', 'NV19', 'NV20', N'Xuất vật tư cho dự án 18', N'Chờ Duyệt'),
    ('PX19', '2024-12-15', '2024-12-16', 'K04', 'PB04', 'NV20', 'NV21', N'Xuất vật tư cho dự án 19', N'Chờ Duyệt'),
    ('PX20', '2024-12-16', '2024-12-17', 'K05', 'PB05', 'NV21', 'NV22', N'Xuất vật tư cho dự án 20', N'Chờ Duyệt'),
    ('PX21', '2024-12-17', '2024-12-18', 'K01', 'PB01', 'NV22', 'NV23', N'Xuất vật tư cho dự án 21', N'Chờ Duyệt'),
    ('PX22', '2024-12-18', '2024-12-19', 'K02', 'PB02', 'NV23', 'NV24', N'Xuất vật tư cho dự án 22', N'Chờ Duyệt'),
    ('PX23', '2024-12-19', '2024-12-20', 'K03', 'PB03', 'NV24', 'NV01', N'Xuất vật tư cho dự án 23', N'Chờ Duyệt'),
    ('PX24', '2024-12-20', '2024-12-21', 'K04', 'PB04', 'NV01', 'NV02', N'Xuất vật tư cho dự án 24', N'Chờ Duyệt'),
    ('PX25', '2024-12-21', '2024-12-22', 'K05', 'PB05', 'NV02', 'NV03', N'Xuất vật tư cho dự án 25', N'Chờ Duyệt'),
    ('PX26', '2024-12-22', '2024-12-23', 'K01', 'PB01', 'NV03', 'NV04', N'Xuất vật tư cho dự án 26', N'Chờ Duyệt'),
    ('PX27', '2024-12-23', '2024-12-24', 'K02', 'PB02', 'NV04', 'NV05', N'Xuất vật tư cho dự án 27', N'Chờ Duyệt'),
    ('PX28', '2024-12-24', '2024-12-25', 'K03', 'PB03', 'NV05', 'NV06', N'Xuất vật tư cho dự án 28', N'Chờ Duyệt'),
    ('PX29', '2024-12-25', '2024-12-26', 'K04', 'PB04', 'NV06', 'NV07', N'Xuất vật tư cho dự án 29', N'Chờ Duyệt'),
    ('PX30', '2024-12-26', '2024-12-27', 'K05', 'PB05', 'NV07', 'NV08', N'Xuất vật tư cho dự án 30', N'Chờ Duyệt'),
    ('PX31', '2024-12-27', '2024-12-28', 'K01', 'PB01', 'NV08', 'NV09', N'Xuất vật tư cho dự án 31', N'Chờ Duyệt'),
    ('PX32', '2024-12-28', '2024-12-29', 'K02', 'PB02', 'NV09', 'NV10', N'Xuất vật tư cho dự án 32', N'Chờ Duyệt'),
    ('PX33', '2024-12-29', '2024-12-30', 'K03', 'PB03', 'NV10', 'NV11', N'Xuất vật tư cho dự án 33', N'Chờ Duyệt'),
    ('PX34', '2024-12-30', '2024-12-31', 'K04', 'PB04', 'NV11', 'NV12', N'Xuất vật tư cho dự án 34', N'Chờ Duyệt'),
    ('PX35', '2024-12-31', '2025-01-01', 'K05', 'PB05', 'NV12', 'NV13', N'Xuất vật tư cho dự án 35', N'Chờ Duyệt'),
    ('PX36', '2025-01-01', '2025-01-02', 'K01', 'PB01', 'NV13', 'NV14', N'Xuất vật tư cho dự án 36', N'Chờ Duyệt'),
    ('PX37', '2025-01-02', '2025-01-03', 'K02', 'PB02', 'NV14', 'NV15', N'Xuất vật tư cho dự án 37', N'Chờ Duyệt'),
    ('PX38', '2025-01-03', '2025-01-04', 'K03', 'PB03', 'NV15', 'NV16', N'Xuất vật tư cho dự án 38', N'Chờ Duyệt'),
    ('PX39', '2025-01-04', '2025-01-05', 'K04', 'PB04', 'NV16', 'NV17', N'Xuất vật tư cho dự án 39', N'Chờ Duyệt'),
    ('PX40', '2025-01-05', '2025-01-06', 'K05', 'PB05', 'NV17', 'NV18', N'Xuất vật tư cho dự án 40', N'Chờ Duyệt'),
    ('PX41', '2025-01-06', '2025-01-07', 'K01', 'PB01', 'NV18', 'NV19', N'Xuất vật tư cho dự án 41', N'Chờ Duyệt'),
    ('PX42', '2025-01-07', '2025-01-08', 'K02', 'PB02', 'NV19', 'NV20', N'Xuất vật tư cho dự án 42', N'Chờ Duyệt'),
    ('PX43', '2025-01-08', '2025-01-09', 'K03', 'PB03', 'NV20', 'NV21', N'Xuất vật tư cho dự án 43', N'Chờ Duyệt'),
    ('PX44', '2025-01-09', '2025-01-10', 'K04', 'PB04', 'NV21', 'NV22', N'Xuất vật tư cho dự án 44', N'Chờ Duyệt'),
    ('PX45', '2025-01-10', '2025-01-11', 'K05', 'PB05', 'NV22', 'NV23', N'Xuất vật tư cho dự án 45', N'Chờ Duyệt'),
    ('PX46', '2025-01-11', '2025-01-12', 'K01', 'PB01', 'NV23', 'NV24', N'Xuất vật tư cho dự án 46', N'Chờ Duyệt'),
    ('PX47', '2025-01-12', '2025-01-13', 'K02', 'PB02', 'NV24', 'NV01', N'Xuất vật tư cho dự án 47', N'Chờ Duyệt'),
    ('PX48', '2025-01-13', '2025-01-14', 'K03', 'PB03', 'NV01', 'NV02', N'Xuất vật tư cho dự án 48', N'Chờ Duyệt'),
    ('PX49', '2025-01-14', '2025-01-15', 'K04', 'PB04', 'NV02', 'NV03', N'Xuất vật tư cho dự án 49', N'Chờ Duyệt'),
    ('PX50', '2025-01-15', '2025-01-16', 'K05', 'PB05', 'NV03', 'NV04', N'Xuất vật tư cho dự án 50', N'Chờ Duyệt'),
    ('PX51', '2025-01-16', '2025-01-17', 'K01', 'PB01', 'NV04', 'NV05', N'Xuất vật tư cho dự án 51', N'Chờ Duyệt'),
    ('PX52', '2025-01-17', '2025-01-18', 'K02', 'PB02', 'NV05', 'NV06', N'Xuất vật tư cho dự án 52', N'Chờ Duyệt'),
    ('PX53', '2025-01-18', '2025-01-19', 'K03', 'PB03', 'NV06', 'NV07', N'Xuất vật tư cho dự án 53', N'Chờ Duyệt'),
    ('PX54', '2025-01-19', '2025-01-20', 'K04', 'PB04', 'NV07', 'NV08', N'Xuất vật tư cho dự án 54', N'Chờ Duyệt'),
    ('PX55', '2025-01-20', '2025-01-21', 'K05', 'PB05', 'NV08', 'NV09', N'Xuất vật tư cho dự án 55', N'Chờ Duyệt');
GO

-- 21. Thêm dữ liệu vào CHITIETPHIEUXUAT
INSERT INTO CHITIETPHIEUXUAT (MaPhieuXuat, MaVatTu, DonViChuyenDoi, SoLuongXuat, DonViGoc, SoLuongQuyDoi) VALUES
    ('PX01', 'VT01', 2, 5, 2, 5),    -- Kilogram
    ('PX02', 'VT06', 8, 10, 8, 10),   -- Lít
    ('PX03', 'VT10', 5, 15, 5, 15),   -- Mét
    ('PX04', 'VT13', 10, 20, 10, 20), -- Kiện
    ('PX05', 'VT02', 13, 5, 13, 5),   -- Cái
    ('PX06', 'VT01', 1, 5, 1, 5),
    ('PX07', 'VT02', 2, 7, 2, 7),
    ('PX08', 'VT03', 3, 9, 3, 9),
    ('PX09', 'VT04', 4, 11, 4, 11),
    ('PX10', 'VT05', 5, 13, 5, 13),
    ('PX11', 'VT06', 1, 15, 1, 15),
    ('PX12', 'VT07', 2, 17, 2, 17),
    ('PX13', 'VT08', 3, 19, 3, 19),
    ('PX14', 'VT09', 4, 21, 4, 21),
    ('PX15', 'VT10', 5, 23, 5, 23),
    ('PX16', 'VT11', 1, 25, 1, 25),
    ('PX17', 'VT12', 2, 27, 2, 27),
    ('PX18', 'VT13', 3, 29, 3, 29),
    ('PX19', 'VT14', 4, 31, 4, 31),
    ('PX20', 'VT15', 5, 33, 5, 33),
    ('PX21', 'VT16', 1, 35, 1, 35),
    ('PX22', 'VT17', 2, 37, 2, 37),
    ('PX23', 'VT01', 3, 39, 3, 39),
    ('PX24', 'VT02', 4, 41, 4, 41),
    ('PX25', 'VT03', 5, 43, 5, 43),
    ('PX26', 'VT04', 1, 45, 1, 45),
    ('PX27', 'VT05', 2, 47, 2, 47),
    ('PX28', 'VT06', 3, 49, 3, 49),
    ('PX29', 'VT07', 4, 51, 4, 51),
    ('PX30', 'VT08', 5, 53, 5, 53),
    ('PX31', 'VT09', 1, 55, 1, 55),
    ('PX32', 'VT10', 2, 57, 2, 57),
    ('PX33', 'VT11', 3, 59, 3, 59),
    ('PX34', 'VT12', 4, 61, 4, 61),
    ('PX35', 'VT13', 5, 63, 5, 63),
    ('PX36', 'VT14', 1, 65, 1, 65),
    ('PX37', 'VT15', 2, 67, 2, 67),
    ('PX38', 'VT16', 3, 69, 3, 69),
    ('PX39', 'VT17', 4, 71, 4, 71),
    ('PX40', 'VT01', 5, 73, 5, 73),
    ('PX41', 'VT02', 1, 75, 1, 75),
    ('PX42', 'VT03', 2, 77, 2, 77),
    ('PX43', 'VT04', 3, 79, 3, 79),
    ('PX44', 'VT05', 4, 81, 4, 81),
    ('PX45', 'VT06', 5, 83, 5, 83),
    ('PX46', 'VT07', 1, 85, 1, 85),
    ('PX47', 'VT08', 2, 87, 2, 87),
    ('PX48', 'VT09', 3, 89, 3, 89),
    ('PX49', 'VT10', 4, 91, 4, 91),
    ('PX50', 'VT11', 5, 93, 5, 93),
    ('PX51', 'VT12', 1, 95, 1, 95),
    ('PX52', 'VT13', 2, 97, 2, 97),
    ('PX53', 'VT14', 3, 99, 3, 99),
    ('PX54', 'VT15', 4, 101, 4, 101),
    ('PX55', 'VT16', 5, 103, 5, 103);
GO

-- 22. Thêm dữ liệu vào LICHSUHOATDONG
INSERT INTO LICHSUHOATDONG (MaLichSu, ThoiGian, MaNhanVien, TenNhanVien, ChucVu, ThaoTac, QuanLy, NoiDungThaoTac) VALUES
    ('LS01', '2024-12-01', 'NV01', N'Thầy Thiện Đạt', N'Bộ trưởng Bộ Giáo dục và Đào tạo', N'Thêm', N'Kho', N'Thêm phiếu nhập PN01'),
    ('LS02', '2024-12-02', 'NV02', N'Đinh Tuấn Duy', N'Tổng giám đốc công ty Two Fingers', N'Xem', N'Kho', N'Xem tồn kho K02'),
    ('LS03', '2024-12-03', 'NV03', N'Bùi Minh Hiếu', N'Tổng giám đốc công ty lò vi sóng', N'Xuất Excel', N'Kế toán', N'Xuất báo cáo tồn kho'),
    ('LS04', '2024-12-04', 'NV04', N'Hoàng Đặng Anh Khoa', N'Tổng vệ sinh', N'Duyệt', N'Hành chính', N'Duyệt phiếu yêu cầu PYC04'),
    ('LS05', '2024-12-05', 'NV05', N'Hà Tuấn Vũ', N'Tổng quản lý phân phối các giống chó Phú Quốc', N'Sửa', N'Kỹ thuật', N'Sửa thông tin vật tư VT02');
GO

-- 23. Thêm dữ liệu vào BAOHANH
INSERT INTO BAOHANH (MaVatTu, MaNhaCungCap, MaKho, TenVatTu, TrangThai) VALUES
    ('VT01', 'NCC01', 'K01', N'Sắt cây phi 10', N'Còn bảo hành'),
    ('VT06', 'NCC02', 'K02', N'Nước tinh khiết 20L', N'Còn bảo hành'),
    ('VT10', 'NCC03', 'K03', N'Thép cây dài 6m', N'Hết bảo hành'),
    ('VT13', 'NCC04', 'K04', N'Kiện gỗ xẻ 1m3', N'Còn bảo hành'),
    ('VT02', 'NCC05', 'K05', N'Thép cuộn', N'Còn bảo hành');
GO