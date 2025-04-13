-- Cách Sử Dụng:

-- Bước 1: Bôi đen tạo dòng database ở dưới trước, sau khi đã tạo nhớ Comment lại:

--CREATE DATABASE QLVT;

-- Bước 2: Bôi đen chọn cơ sở dữ liệu muốn sử dụng ở dưới, sau đó nhớ Comment lại:

--USE QLVT;

-- Bước 3: Sau khi đã hoàn thành các bước trên nhấn Execute hoặc F5 để chạy toàn bộ.

-- Nếu gặp lỗi và muốn tạo lại database thì hãy xóa database bằng cách mở comment và bôi đen để chạy 3 dòng comment ở dưới để xóa database QLVT vừa tạo:

--USE master;
--ALTER DATABASE QLVT SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
--DROP DATABASE QLVT;

-- Sau khi đã xóa thành công database QLVT thì hãy chạy lại 3 bước tạo database ở trên cùng để tạo lại database.

BEGIN

-- Xóa bảng phòng ban:
--DROP TABLLE PHONGBAN;

-- Tạo bảng phòng ban:
CREATE TABLE PHONGBAN (
    MaPhongBan VARCHAR(50) PRIMARY KEY,
    TenPhongBan NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(255) NOT NULL,
    MaTruongPhong VARCHAR(50) NULL,
);

-- Thêm dữ liệu cho bảng phòng ban:
INSERT INTO PHONGBAN (MaPhongBan, TenPhongBan, DiaChi) VALUES
('PB01', N'Phòng Giáo Dục', N'123 Đường 1'),
('PB02', N'Phòng Kỹ Thuật', N'456 Đường 2'),
('PB03', N'Phòng Nhân Sự', N'789 Đường 3'),
('PB04', N'Phòng Hành Chính', N'101 Đường 4'),
('PB05', N'Phòng Kinh Doanh', N'112 Đường 5'),
('PB06', N'Phòng IT', N'131 Đường 6'),
('PB07', N'Phòng Marketing', N'415 Đường 7'),
('PB08', N'Phòng R&D', N'161 Đường 8'),
('PB09', N'Phòng Sản Xuất', N'718 Đường 9'),
('PB10', N'Phòng Mua Hàng', N'192 Đường 10');


-- Xóa bảng chức vụ:
--DROP TABLE CHUCVU;

-- Tạo bảng chức vụ:
CREATE TABLE CHUCVU (
    MaChucVu VARCHAR(50) PRIMARY KEY,
    TenChucVu NVARCHAR(100) NOT NULL
);

-- Thêm dữ liệu cho bảng chức vụ:
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


-- Xóa bảng nhân viên:
--DROP TABLE NHANVIEN;

-- Tạo bảng nhân viên:
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

-- Thêm dữ liệu cho bảng nhân viên:
INSERT INTO NHANVIEN (MaNhanVien, TenNhanVien, MaChucVu, MaPhongBan, Email, SoDienThoai, TrangThai) VALUES
	('NV01', N'Thầy Thiện Đạt', 'CV01', 'PB01', 'bsquocdat@gmail.com', '0911111111', N'Đang Làm Việc'),
	('NV02', N'Đinh Tuấn Duy', 'CV02', 'PB01', 'duy@gmail.com', '0922222222', N'Đang Làm Việc'),
	('NV03', N'Bùi Minh Hiếu', 'CV03', 'PB01', 'hieu@gmail.com', '0933333333', N'Đang Làm Việc'),
	('NV04', N'Hoàng Đặng Anh Khoa', 'CV04', 'PB01', 'khoa@gmail.com', '0944444444', N'Đang Làm Việc'),
	('NV05', N'Hà Tuấn Vũ', 'CV05', 'PB01', 'vu@gmail.com', '0955555555', N'Đang Làm Việc'),
	('NV06', N'Trần Trọng Nghĩa', 'CV06', 'PB01', 'nghia@gmail.com', '0966666666', N'Đang Làm Việc'),
	('NV07', N'Phạm Thái Lâm Khánh', 'CV07', 'PB01', 'khanh@gmail.com', '0977777777', N'Đang Làm Việc'),
	('NV08', N'Đoàn Ngọc Dương', 'CV08', 'PB01', 'duong@gmail.com', '0988888888', N'Đang Làm Việc'),
	('NV09', 'a', 'CV09', 'PB02', 'a@gmail.com', '0811111111', N'Đang Làm Việc'),
	('NV10', 'b', 'CV10', 'PB02', 'b@gmail.com', '0822222222', N'Đang Làm Việc'),
	('NV11', 'c', 'CV11', 'PB02', 'c@gmail.com', '0833333333', N'Đang Làm Việc'),
	('NV12', 'd', 'CV12', 'PB02', 'd@gmail.com', '0844444444', N'Đã Nghỉ Việc'),
	('NV13', 'e', 'CV13', 'PB03', 'e@gmail.com', '0711111111', N'Đang Làm Việc'),
	('NV14', 'f', 'CV14', 'PB03', 'f@gmail.com', '0722222222', N'Đang Làm Việc'),
	('NV15', 'g', 'CV15', 'PB03', 'g@gmail.com', '0733333333', N'Đang Làm Việc'),
	('NV16', 'h', 'CV16', 'PB03', 'h@gmail.com', '0611111111', N'Đã Nghỉ Việc'),
	('NV17', 'i', 'CV17', 'PB03', 'i@gmail.com', '0622222222', N'Đang Làm Việc'),
	('NV18', 'j', 'CV18', 'PB03', 'j@gmail.com', '0633333333', N'Đang Làm Việc'),
	('NV19', 'k', 'CV19', 'PB03', 'k@gmail.com', '0644444444', N'Đang Làm Việc'),
	('NV20', 'l', 'CV20', 'PB03', 'l@gmail.com', '0511111111', N'Đã Nghỉ Việc'),
	('NV21', 'm', 'CV21', 'PB03', 'm@gmail.com', '0522222222', N'Đang Làm Việc'),
	('NV22', 'n', 'CV22', 'PB03', 'n@gmail.com', '0533333333', N'Đang Làm Việc'),
	('NV23', 'o', 'CV23', 'PB03', 'o@gmail.com', '0411111111', N'Đang Làm Việc'),
	('NV24', 'p', 'CV24', 'PB03', 'p@gmail.com', '0422222222', N'Đã Nghỉ Việc');


ALTER TABLE PHONGBAN
ADD CONSTRAINT FK_PB_MaTruongPhong FOREIGN KEY (MaTruongPhong) REFERENCES NHANVIEN (MaNhanVien) ON DELETE SET NULL

UPDATE PHONGBAN
SET MaTruongPhong = CASE 
    WHEN MaPhongBan = 'PB01' THEN 'NV01'
    WHEN MaPhongBan = 'PB02' THEN 'NV02'
    WHEN MaPhongBan = 'PB03' THEN 'NV03'
    WHEN MaPhongBan = 'PB04' THEN 'NV04'
    WHEN MaPhongBan = 'PB05' THEN 'NV05'
    WHEN MaPhongBan = 'PB06' THEN 'NV06'
    WHEN MaPhongBan = 'PB07' THEN 'NV07'
    WHEN MaPhongBan = 'PB08' THEN 'NV08'
    WHEN MaPhongBan = 'PB09' THEN 'NV09'
    WHEN MaPhongBan = 'PB10' THEN 'NV10'
    ELSE MaTruongPhong 
END;



-- DROP TABLE QUYENHAN:
CREATE TABLE QUYENHAN(
	MaChucVu VARCHAR(50),
	QuanLy NVARCHAR(100),
	Xem BIT DEFAULT 0,
	XuatExcel BIT DEFAULT 0,
	Them BIT DEFAULT 0,
	Sua BIT DEFAULT 0,
	Xoa BIT DEFAULT 0,	
	PRIMARY KEY (MaChucVu, QuanLy),
	CONSTRAINT FK_QH_MaChucVu FOREIGN KEY (MaChucVu) REFERENCES ChucVu(MaChucVu)
);

INSERT INTO QUYENHAN (MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa) VALUES
--CV01
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
-- CV02
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
	('CV02', N'Lịch Sử Hoạt Động', 1, 1, 1, 1, 1),
-- CV03

-- CV09
	('CV09', N'Quản Lý Loại Vật Tư', 1, 1, 1, 1, 1),
	('CV09', N'Quản Lý Vật Tư', 1, 1, 1, 1, 1),
	('CV09', N'Quản Lý Đơn Vị Tính', 1, 1, 1, 1, 1),
	('CV09', N'Quản Lý Vật Tư Lỗi - Bảo Hành', 1, 1, 1, 1, 1),
	('CV09', N'Quản Lý Phòng Ban', 1, 1, 1, 1, 1),
-- CV10
	('CV10', N'Quản Lý Kho', 1, 1, 1, 1, 1),
	('CV10', N'Quản Lý Kho - Loại Vật Tư', 1, 1, 1, 1, 1),
	('CV10', N'Quản Lý Tồn Kho', 1, 1, 1, 1, 1),
	('CV10', N'Quản Lý Phiếu Xuất', 1, 1, 1, 1, 1),
	('CV10', N'Quản Lý Phòng Ban', 1, 1, 1, 1, 1),
	('CV10', N'Lịch Sử Hoạt Động', 1, 1, 1, 1, 1);
-- CV11









-- Xóa bảng tài khoản:
--DROP TABLE TAIKHOAN;

-- Tạo bảng tài khoản:
CREATE TABLE TAIKHOAN (
    TaiKhoan VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(50) NOT NULL,
    MaNhanVien VARCHAR(50) NOT NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Đang Chờ Xử Lý',
    CONSTRAINT FK_TK_MaNhanVien FOREIGN KEY (MaNhanVien) REFERENCES NHANVIEN(MaNhanVien)
);

-- Thêm dữ liệu cho bảng tài khoản:
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





-- Xóa bảng loại vật tư:
--DROP TABLE LOAIVATTU;

-- Tạo bảng loại vật tư:
CREATE TABLE LOAIVATTU (
    MaLoaiVatTu VARCHAR(10) PRIMARY KEY,
    TenLoaiVatTu NVARCHAR(100),
    NhomVatTu NVARCHAR(50)
);


-- Thêm dữ liệu mới
INSERT INTO LOAIVATTU (MaLoaiVatTu, TenLoaiVatTu, NhomVatTu) VALUES
-- Nhóm Khối Lượng
('KL01', N'Sắt', N'Khối Lượng'),
('KL02', N'Thép', N'Khối Lượng'),
('KL03', N'Xi măng', N'Khối Lượng'),
('KL04', N'Gạo', N'Khối Lượng'),
('KL05', N'Phân bón', N'Khối Lượng'),

-- Nhóm Thể Tích
('TT01', N'Nước', N'Thể Tích'),
('TT02', N'Dầu', N'Thể Tích'),
('TT03', N'Sơn', N'Thể Tích'),
('TT04', N'Xăng', N'Thể Tích'),

-- Nhóm Chiều Dài
('CD01', N'Thép cây', N'Chiều Dài'),
('CD02', N'Ống nhựa', N'Chiều Dài'),
('CD03', N'Dây điện', N'Chiều Dài'),

-- Nhóm Đóng Gói
('DG01', N'Gỗ xẻ kiện', N'Đơn Vị Đóng Gói'),
('DG02', N'Hộp đinh', N'Đơn Vị Đóng Gói'),
('DG03', N'Bịch ốc vít', N'Đơn Vị Đóng Gói'),
('DG04', N'Thùng sơn', N'Đơn Vị Đóng Gói'),
('DG05', N'Hộp khẩu trang', N'Đơn Vị Đóng Gói');



-- Xóa bảng kho:
--DROP TABLE KHO;

-- Tạo bảng kho:
CREATE TABLE KHO (
    MaKho VARCHAR(10) PRIMARY KEY,
    TenKho VARCHAR(100) NOT NULL,
	MaLoaiVatTu NVARCHAR(100) NULL,
	DiaChi NVARCHAR(255) NOT NULL
);

-- Thêm dữ liệu cho bảng kho:
INSERT INTO KHO (MaKho, TenKho, MaLoaiVatTu, DiaChi) VALUES
('K01', 'Kho A', 'LVT01', N'123 Đường A'),
('K02', 'Kho B', 'LVT02', N'456 Đường B'),
('K03', 'Kho C', 'LVT03', N'789 Đường C'),
('K04', 'Kho D', 'LVT04', N'101 Đường D'),
('K05', 'Kho E', 'LVT05', N'112 Đường E'),
('K06', 'Kho F', 'LVT06', N'131 Đường F'),
('K07', 'Kho G', 'LVT07', N'415 Đường G'),
('K08', 'Kho H', 'LVT08', N'161 Đường H'),
('K09', 'Kho I', 'LVT09', N'718 Đường I'),
('K10', 'Kho J', 'LVT10', N'192 Đường J');


-- Xóa bảng kho_loaivattu:
--DROP TABLE KHO_LOAIVATTU;

-- Tạo bảng kho_loaivattu:
CREATE TABLE KHO_LOAIVATTU (
    MaKho VARCHAR(10) NOT NULL,
    MaLoaiVatTu VARCHAR(10) NOT NULL,
    PRIMARY KEY (MaKho, MaLoaiVatTu),
    CONSTRAINT FK_KLV_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
    CONSTRAINT FK_KLV_MaLoaiVatTu FOREIGN KEY (MaLoaiVatTu) REFERENCES LOAIVATTU (MaLoaiVatTu)
);

-- Thêm dữ liệu cho bảng kho_loaivattu:
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
('K01', 'KL01'),
('K02', 'KL02'),
('K03', 'KL03'),
('K01', 'KL04'),
('K02', 'KL05');

-- Thể Tích → K04, K05
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
('K04', 'TT01'),
('K04', 'TT02'),
('K05', 'TT03'),
('K05', 'TT04');

-- Chiều Dài → K06, K07
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
('K06', 'CD01'),
('K06', 'CD02'),
('K07', 'CD03');

-- Đóng Gói → K08, K09, K10
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
('K08', 'DG01'),
('K08', 'DG02'),
('K09', 'DG03'),
('K09', 'DG04'),
('K10', 'DG05');


-- Xóa bảng vật tư:
--DROP TABLE VATTU;

--Tạo bảng vật tư:
CREATE TABLE VATTU (
    MaVatTu VARCHAR(10) PRIMARY KEY,
    TenVatTu NVARCHAR(100) NOT NULL,
	MaLoaiVatTu VARCHAR(10) NOT NULL,
	CONSTRAINT FK_VT_MaLoaiVatTu FOREIGN KEY (MaLoaiVatTu) REFERENCES LOAIVATTU (MaLoaiVatTu)
);

-- Thêm dữ liệu cho bảng vật tư:
INSERT INTO VATTU (MaVatTu, TenVatTu, MaLoaiVatTu) VALUES
-- Khối Lượng
('VT01', N'Sắt cây phi 10', 'KL01'),
('VT02', N'Thép cuộn', 'KL02'),
('VT03', N'Bao xi măng Hà Tiên', 'KL03'),
('VT04', N'Gạo ST25', 'KL04'),
('VT05', N'Phân NPK', 'KL05'),

-- Thể Tích
('VT06', N'Nước tinh khiết 20L', 'TT01'),
('VT07', N'Dầu nhớt động cơ', 'TT02'),
('VT08', N'Sơn Dulux 18L', 'TT03'),
('VT09', N'Xăng A95', 'TT04'),

-- Chiều Dài
('VT10', N'Thép cây dài 6m', 'CD01'),
('VT11', N'Ống nhựa PVC D60', 'CD02'),
('VT12', N'Dây điện Cadivi 2.5', 'CD03'),

-- Đơn Vị Đóng Gói
('VT13', N'Kiện gỗ xẻ 1m3', 'DG01'),
('VT14', N'Hộp đinh 2cm', 'DG02'),
('VT15', N'Bịch ốc vít 500g', 'DG03'),
('VT16', N'Thùng sơn Jotun', 'DG04'),
('VT17', N'Hộp khẩu trang y tế', 'DG05');



-- Xóa bảng tồn kho:
--DROP TABLE TONKHO;

-- Tạo bảng tồn kho:
CREATE TABLE TONKHO(
	MaKho VARCHAR(10) NOT NULL,
	MaVatTu VARCHAR(10) NOT NULL,
	SoLuong INT DEFAULT 0,
	DonVi NVARCHAR(20) NULL,
	TonToiThieu INT DEFAULT 0,
	TonToiDa INT DEFAULT 0,
	ViTri NVARCHAR(255) NULL,
	CONSTRAINT PK_TK_MaKho_MaVatTu PRIMARY KEY  (MaKho, MaVatTu),
	CONSTRAINT FK_TK_MaKho FOREIGN KEY (MaKho) REFERENCES KHO (MaKho),
	CONSTRAINT FK_TK_MaVatTu FOREIGN KEY (MaVatTu) REFERENCES VATTU (MaVatTu),
	CONSTRAINT CHK_TK_SoLuong CHECK ( SoLuong >= 0 )
);


-- Thêm dữ liệu cho bảng tồn kho:
INSERT INTO TONKHO (MaKho, MaVatTu, SoLuong, DonVi, TonToiThieu, TonToiDa, ViTri) VALUES
-- Khối lượng
('K02', 'VT05', 60, N'Tấn', 30, 150, N'Kệ B2'),

-- Thể tích
('K03', 'VT02', 120, N'Lít', 40, 300, N'Kệ C1'),

-- Chiều dài
('K04', 'VT03', 90, N'Mét', 20, 200, N'Kệ D1'),

-- Đơn vị đóng gói
('K05', 'VT04', 40, N'Thùng', 10, 100, N'Kệ E1');


-- Xóa bảng nhà cung cấp:
--DROP TABLE NHACUNGCAP;

-- Tạo bảng nhà cung cấp:
CREATE TABLE NHACUNGCAP (
    MaNhaCungCap VARCHAR(10) PRIMARY KEY,
    TenNhaCungCap NVARCHAR(100) NOT NULL,
    SoDienThoai VARCHAR(15) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    DiaChi NVARCHAR(255) NOT NULL,
	CONSTRAINT CHK_NCC_SoDienThoai CHECK (SoDienThoai LIKE '0[0-9]%' OR SoDienThoai LIKE '+84[0-9]%')
);

-- Thêm dữ liệu cho bảng NHACUNGCAP
INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoai, Email, DiaChi) VALUES
('NCC01', N'Nhà cung cấp A', '0123456789', 'ncc1@gmail.com', N'1 Đường A'),
('NCC02', N'Nhà cung cấp B', '0987654321', 'ncc2@gmail.com', N'2 Đường B'),
('NCC03', N'Nhà cung cấp C', '0931234567', 'ncc3@gmail.com', N'3 Đường C'),
('NCC04', N'Nhà cung cấp D', '0909876543', 'ncc4@gmail.com', N'4 Đường D'),
('NCC05', N'Nhà cung cấp E', '0912345678', 'ncc5@gmail.com', N'5 Đường E'),
('NCC06', N'Nhà cung cấp F', '0981111222', 'ncc6@gmail.com', N'6 Đường F'),
('NCC07', N'Nhà cung cấp G', '0909999888', 'ncc7@gmail.com', N'7 Đường G'),
('NCC08', N'Nhà cung cấp H', '0933334444', 'ncc8@gmail.com', N'8 Đường H'),
('NCC09', N'Nhà cung cấp I', '0966667777', 'ncc9@gmail.com', N'9 Đường I'),
('NCC10', N'Nhà cung cấp J', '0977778888', 'ncc10@gmail.com', N'10 Đường J');

--BẢNG ĐƠN VỊ TÍNH để quản lý các đơn vị--
CREATE TABLE DonViTinh (
    MaDonVi INT PRIMARY KEY IDENTITY(1,1),
    TenDonVi NVARCHAR(50),
    NhomVatTu NVARCHAR(50)
);

-- Thêm dữ liệu mẫu
SET IDENTITY_INSERT DonViTinh ON;

INSERT INTO DonViTinh (MaDonVi, TenDonVi, NhomVatTu) VALUES
(1, N'Tấn', N'Khối Lượng'),
(2, N'Kilogram', N'Khối Lượng'),
(3, N'Gram', N'Khối Lượng');

-- Chiều dài
INSERT INTO DonViTinh (MaDonVi, TenDonVi, NhomVatTu) VALUES
(4, N'Kilomet', N'Chiều Dài'),
(5, N'Mét', N'Chiều Dài'),
(6, N'Centimet', N'Chiều Dài');

-- Thể tích
INSERT INTO DonViTinh (MaDonVi, TenDonVi, NhomVatTu) VALUES
(7, N'Mét khối', N'Thể Tích'),
(8, N'Lít', N'Thể Tích'),
(9, N'Mililit', N'Thể Tích');

-- Đơn vị đóng gói
INSERT INTO DonViTinh (MaDonVi, TenDonVi, NhomVatTu) VALUES
(10, N'Kiện', N'Đơn Vị Đóng Gói'),
(11, N'Thùng', N'Đơn Vị Đóng Gói'),
(12, N'Hộp', N'Đơn Vị Đóng Gói'),
(13, N'Cái', N'Đơn Vị Đóng Gói');


-- Tạo bảng Quy đổi đơn vị để lưu hệ số quy đổi giữa các đơn vị:
CREATE TABLE QuyDoiDonVi (
    MaQuyDoi INT PRIMARY KEY IDENTITY(1,1),
    DonViGoc INT,           -- không cần FOREIGN KEY
    DonViDich INT,          -- không cần FOREIGN KEY
    HeSo FLOAT
);




-- Thêm dữ liệu mẫu
-- Khối lượng
INSERT INTO QuyDoiDonVi VALUES
(1, 2, 1000.0),  -- Tấn → Kilogram
(2, 1, 0.001),   -- Kilogram → Tấn
(2, 3, 1000.0),  -- Kilogram → Gram
(3, 2, 0.001),   -- Gram → Kilogram
(1, 3, 1000000.0), -- Tấn → Gram
(3, 1, 0.000001);  -- Gram → Tấn

-- Chiều dài

INSERT INTO QuyDoiDonVi VALUES
(4, 5, 1000.0),    -- Km → m
(5, 4, 0.001),
(5, 6, 100.0),     -- m → cm
(6, 5, 0.01),
(4, 6, 100000.0),  -- Km → cm
(6, 4, 0.00001);   -- cm → Km

-- Thể tích
INSERT INTO QuyDoiDonVi VALUES
(7, 8, 1000.0),   -- m³ → L
(8, 7, 0.001),
(8, 9, 1000.0),   -- L → mL
(9, 8, 0.001),
(7, 9, 1000000.0),  -- Mét khối → Mililit
(9, 7, 0.000001);   -- Mililit → Mét khối

-- Đóng gói
INSERT INTO QuyDoiDonVi VALUES
(10, 11, 10.0),       -- 1 Kiện = 10 Thùng
(11, 10, 0.1),        -- 1 Thùng = 0.1 Kiện

(11, 12, 24.0),       -- 1 Thùng = 24 Hộp
(12, 11, 1.0/24),     -- 1 Hộp = 1/24 Thùng

(12, 13, 12.0),       -- 1 Hộp = 12 Cái
(13, 12, 1.0/12),     -- 1 Cái = 1/12 Hộp

(10, 12, 240.0),      -- 1 Kiện = 240 Hộp
(12, 10, 1.0/240),    -- 1 Hộp = 1/240 Kiện

(10, 13, 2880.0),     -- 1 Kiện = 2880 Cái
(13, 10, 1.0/2880),   -- 1 Cái = 1/2880 Kiện

(11, 13, 288.0),      -- 1 Thùng = 288 Cái
(13, 11, 1.0/288);    -- 1 Cái = 1/288 Thùng

-- Đồng bộ tên nhóm trong DonViTinh
UPDATE DonViTinh SET NhomVatTu = 'Khối Lượng' WHERE NhomVatTu = 'Khối lượng';
UPDATE DonViTinh SET NhomVatTu = 'Thể Tích' WHERE NhomVatTu = 'Thể tích';
UPDATE DonViTinh SET NhomVatTu = 'Chiều Dài' WHERE NhomVatTu = 'Chiều dài';
UPDATE DonViTinh SET NhomVatTu = 'Đơn Vị Đóng Gói' WHERE NhomVatTu = 'Đơn vị đóng gói';

-- Xóa bảng phiếu nhập:
--DROP TABLE PHIEUNHAP;

-- Tạo bảng phiếu nhập:
CREATE TABLE PHIEUNHAP (
    MaPhieuNhap VARCHAR(10) PRIMARY KEY,
    NgayNhap DATE NOT NULL,
	MaKho VARCHAR(10) NOT NULL,
    MaNhaCungCap VARCHAR(10) NOT NULL,
	TrangThai NVARCHAR(50) DEFAULT N'Chờ duyệt',
	CONSTRAINT FK_PN_MaNhaCungCap FOREIGN KEY (MaNhaCungCap) REFERENCES NHACUNGCAP(MaNhaCungCap) ON DELETE NO ACTION,
	CONSTRAINT FK_PN_MaKho FOREIGN KEY (MaKho) REFERENCES KHO(MaKho) ON DELETE NO ACTION
);

-- Thêm dữ liệu cho bảng phiếu nhập:
INSERT INTO PHIEUNHAP (MaPhieuNhap, NgayNhap, MaKho, MaNhaCungCap) VALUES
('PN01', '2024-12-01', 'K01', 'NCC01'),
('PN02', '2024-10-02', 'K02', 'NCC02'),
('PN03', '2024-11-03', 'K03', 'NCC03'),
('PN04', '2024-12-04', 'K04', 'NCC04'),
('PN05', '2024-12-05', 'K05', 'NCC05'),
('PN06', '2024-12-06', 'K06', 'NCC06'),
('PN07', '2024-12-07', 'K07', 'NCC07'),
('PN08', '2024-12-08', 'K08', 'NCC08'),
('PN09', '2024-12-09', 'K09', 'NCC09'),
('PN10', '2024-12-10', 'K10', 'NCC10');


-- Xóa bảng chi tiết phiếu nhập:
--DROP TABLE CHITIETPHIEUNHAP;

-- Tạo bảng chi tiết phiếu nhập:
CREATE TABLE CHITIETPHIEUNHAP (
    MaPhieuNhap VARCHAR(10),
    MaVatTu VARCHAR(10),
    DonViChuyenDoi INT,
    DonViGoc INT,
    SoLuongNhap FLOAT,
    SoLuongQuyDoi FLOAT,
    PRIMARY KEY (MaPhieuNhap, MaVatTu),
    FOREIGN KEY (MaPhieuNhap) REFERENCES PHIEUNHAP(MaPhieuNhap),
    FOREIGN KEY (MaVatTu) REFERENCES VATTU(MaVatTu),
    FOREIGN KEY (DonViGoc) REFERENCES DonViTinh(MaDonVi),
    FOREIGN KEY (DonViChuyenDoi) REFERENCES DonViTinh(MaDonVi),
    CHECK (SoLuongNhap > 0),
    CHECK (SoLuongQuyDoi > 0)
);


-- Thêm dữ liệu cho bảng chi tiết phiếu nhập:
INSERT INTO CHITIETPHIEUNHAP (
    MaPhieuNhap, MaVatTu, DonViChuyenDoi, DonViGoc, SoLuongNhap, SoLuongQuyDoi
) VALUES 
('PN01', 'VT01', 1, 2, 1, 1000),         -- Tấn → Kilogram
('PN01', 'VT02', 10, 13, 2, 5760),       -- Kiện → Cái
('PN02', 'VT03', 8, 8, 50, 50),          -- Lít → Lít (không chuyển đổi)
('PN03', 'VT04', 12, 13, 10, 120),       -- Hộp → Cái
('PN04', 'VT05', 5, 6, 5, 500);          -- Mét → Centimet


-- Xóa bảng phiếu yêu cầu vật tư:
--DROP TABLE PHIEUYEUCAUVATTU;

-- Tạo bảng phiếu yêu cầu vật tư:
CREATE TABLE PHIEUYEUCAUVATTU (
    MaPhieuYeuCauVatTu VARCHAR(10) PRIMARY KEY,
    NgayYeuCau DATE DEFAULT GETDATE(),
    MaKho VARCHAR(10),
	MaPhongBanYeuCau VARCHAR(50),
    MaNguoiYeuCau VARCHAR(50),
    MaNguoiDuyet VARCHAR(50),
    LyDo NVARCHAR(255) NULL,
    TrangThai NVARCHAR(50) DEFAULT N'Chờ duyệt',
    CONSTRAINT FK_PYCVT_MaKho FOREIGN KEY (MaKho) REFERENCES KHO(MaKho),
	CONSTRAINT FK_PYCVT_MaPhongBan FOREIGN KEY (MaPhongBanYeuCau) REFERENCES PHONGBAN(MaPhongBan),
    CONSTRAINT FK_PYCVT_MaNguoiYeuCau FOREIGN KEY (MaNguoiYeuCau) REFERENCES NHANVIEN(MaNhanVien),
    CONSTRAINT FK_PYCVT_MaNguoiDuyet FOREIGN KEY (MaNguoiDuyet) REFERENCES NHANVIEN(MaNhanVien)
);


-- Xóa bảng chi tiết phiếu yêu cầu:
--DROP TABLE CHITIETPHIEUYEUCAU;

-- Tạo bảng chi tiết phiếu yêu cầu:
CREATE TABLE CHITIETPHIEUYEUCAUVATTU(
    MaPhieuYeuCauVatTu VARCHAR(10),
    MaVatTu VARCHAR(10),
    SoLuong INT,
    PRIMARY KEY(MaPhieuYeuCauVatTu, MaVatTu),
    FOREIGN KEY (MaPhieuYeuCauVatTu) REFERENCES PHIEUYEUCAUVATTU(MaPhieuYeuCauVatTu),
    FOREIGN KEY (MaVatTu) REFERENCES VATTU(MaVatTu),
	CONSTRAINT CHK_CTPYCVT_SoLuong CHECK (SoLuong > 0)
);


-- Xóa bảng phiếu xuất:
--DROP TABLE PHIEUXUAT;

-- Tạo bảng phiếu xuất:
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
    CONSTRAINT FK_PX_MaKho FOREIGN KEY (MaKho) REFERENCES KHO(MaKho),
    CONSTRAINT FK_PX_MaPhongBan FOREIGN KEY (MaPhongBanYeuCau) REFERENCES PHONGBAN(MaPhongBan),
	CONSTRAINT FK_PX_MaNguoiXuat FOREIGN KEY (MaNguoiXuat) REFERENCES NHANVIEN(MaNhanVien),
    CONSTRAINT FK_PX_MaNguoiNhan FOREIGN KEY (MaNguoiNhan) REFERENCES NHANVIEN(MaNhanVien)
);

-- Thêm dữ liệu cho bảng phiếu xuất:
INSERT INTO PHIEUXUAT (MaPhieuXuat, NgayChungTu, NgayXuat, MaKho, MaPhongBanYeuCau, MaNguoiXuat, MaNguoiNhan) VALUES
('PX01', '2024-12-01', '2024-12-01', 'K01', 'PB01', 'NV09', 'NV04'),
('PX02', '2024-12-02', '2024-12-02', 'K02', 'PB02', 'NV10', 'NV04'),
('PX03', '2024-12-03', '2024-12-03', 'K03', 'PB03', 'NV09', 'NV04'),
('PX04', '2024-12-04', '2024-12-04', 'K04', 'PB04', 'NV09', 'NV05'),
('PX05', '2024-12-05', '2024-12-05', 'K05', 'PB05', 'NV09', 'NV06'),
('PX06', '2024-12-06', '2024-12-06', 'K06', 'PB06', 'NV09', 'NV07'),
('PX07', '2024-12-07', '2024-12-07', 'K07', 'PB07', 'NV09', 'NV08'),
('PX08', '2024-12-08', '2024-12-08', 'K08', 'PB08', 'NV10', 'NV06'),
('PX09', '2024-12-09', '2024-12-09', 'K09', 'PB09', 'NV10', 'NV04'),
('PX10', '2024-12-10', '2024-12-10', 'K10', 'PB10', 'NV10', 'NV05');


-- Xóa bảng chi tiết phiếu xuất:
--DROP TABLE CHITIETPHIEUXUAT;

-- Tạo bảng chi tiết phiếu xuất:
CREATE TABLE CHITIETPHIEUXUAT (
    MaPhieuXuat VARCHAR(10),
    MaVatTu VARCHAR(10),
    DonViChuyenDoi NVARCHAR(50),
    SoLuongXuat FLOAT,
    DonViGoc NVARCHAR(50),
    SoLuongQuyDoi FLOAT,
    PRIMARY KEY (MaPhieuXuat, MaVatTu),
    FOREIGN KEY (MaPhieuXuat) REFERENCES PHIEUXUAT(MaPhieuXuat),
    FOREIGN KEY (MaVatTu) REFERENCES VATTU(MaVatTu)
);

-- Thêm dữ liệu cho bảng chi tiết phiếu xuất:
INSERT INTO CHITIETPHIEUXUAT (
    MaPhieuXuat, MaVatTu, DonViChuyenDoi, SoLuongXuat, DonViGoc, SoLuongQuyDoi
) VALUES 
('PX01', 'VT01', N'Tấn', 0.5, N'Kg', 500),
('PX01', 'VT02', N'Kiện', 1, N'Cái', 2880),
('PX02', 'VT03', N'Lít', 100, N'Lít', 100),
('PX03', 'VT04', N'Hộp', 5, N'Cái', 60),
('PX04', 'VT05', N'Mét', 10, N'Centimet', 1000);


-- Bổ sung cho bảng PHIEUNHAP nếu chưa có
ALTER TABLE PHIEUNHAP ADD DonViChuyenDoi NVARCHAR(50);
ALTER TABLE PHIEUNHAP ADD DonViGoc NVARCHAR(50);
ALTER TABLE PHIEUNHAP ADD SoLuongQuyDoi FLOAT;

-- Bổ sung cho bảng PHIEUXUAT nếu chưa có
-- Thay đổi CHITIETPHIEUXUAT:
ALTER TABLE CHITIETPHIEUXUAT DROP COLUMN DonViChuyenDoi, DonViGoc;
ALTER TABLE CHITIETPHIEUXUAT ADD DonViChuyenDoi INT FOREIGN KEY REFERENCES DonViTinh(MaDonVi);
ALTER TABLE CHITIETPHIEUXUAT ADD DonViGoc INT;

-- Xóa bảng lịch sử hoạt động:
--DROP TABLE LICHSUHOATDONG;

-- Tạo bảng lịch sử hoạt động:
CREATE TABLE LICHSUHOATDONG (
    MaLichSu VARCHAR PRIMARY KEY,
	ThoiGian DATE NOT NULL,
	MaNhanVien VARCHAR(50) NOT NULL,
	TenNhanVien NVARCHAR(50) NOT NULL,
	ChucVu NVARCHAR(50) NOT NULL,
	ThaoTac NVARCHAR(50) NOT NULL,
	QuanLy NVARCHAR(50) NOT NULL,
	NoiDungThaoTac NVARCHAR(255) NOT NULL,
	CONSTRAINT FK_MaNhanVien FOREIGN KEY (MaNhanVien) REFERENCES NHANVIEN (MaNhanVien)
);


CREATE FUNCTION dbo.LayHeSoQuyDoi(@DonViGoc INT, @DonViDich INT)
RETURNS FLOAT
AS
BEGIN
    DECLARE @HeSo FLOAT
    SELECT @HeSo = HeSo
    FROM QuyDoiDonVi
    WHERE DonViGoc = @DonViGoc AND DonViDich = @DonViDich
    RETURN ISNULL(@HeSo, 1)
END ;


CREATE TABLE BAOHANH (
    MaBaoHanh INT PRIMARY KEY IDENTITY(1,1),
    MaVatTu VARCHAR(10) NOT NULL,
    MaNhaCungCap VARCHAR(10) NOT NULL,
    MaKho VARCHAR(10),
    TenVatTu NVARCHAR(100),
    TrangThai NVARCHAR(50),
    NgayBaoHanh DATE,
    HanBaoHanh INT,
    NoiDung NVARCHAR(255),
    NgayDuyet DATE,
    CONSTRAINT FK_BAOHANH_VATTU FOREIGN KEY (MaVatTu) REFERENCES VATTU(MaVatTu),
    CONSTRAINT FK_BAOHANH_NCC FOREIGN KEY (MaNhaCungCap) REFERENCES NHACUNGCAP(MaNhaCungCap)
);
end 