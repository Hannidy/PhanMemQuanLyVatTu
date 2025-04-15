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
    ('PB05', N'Phòng kinh doanh', N'202 Phạm Ngũ Lão', NULL),
	('PB06', N'Phòng quản lý chất lượng', N'303 Lê Văn Sỹ, TP.HCM', NULL),
	('PB07', N'Phòng marketing', N'505 Nguyễn Trãi, Hà Nội', NULL),
	('PB08', N'Phòng nghiên cứu phát triển', N'707 Trần Hưng Đạo, Đà Nẵng', NULL),
	('PB09', N'Phòng nhân sự', N'909 Nguyễn Văn Linh, TP.HCM', NULL),
	('PB10', N'Phòng dự án 1', N'111 Hai Bà Trưng, Hà Nội', NULL),
	('PB11', N'Phòng chăm sóc khách hàng', N'222 Phạm Văn Đồng, Đà Nẵng', NULL),
	('PB12', N'Phòng vận hành', N'333 Lý Thường Kiệt, TP.HCM', NULL),
	('PB13', N'Phòng tài chính', N'444 Nguyễn Huệ, Huế', NULL),
	('PB14', N'Phòng kho 2', N'555 Điện Biên Phủ, Hà Nội', NULL),
	('PB15', N'Phòng kỹ thuật 2', N'666 Lê Lợi, Cần Thơ', NULL),
	('PB16', N'Phòng kế hoạch', N'777 Cách Mạng Tháng Tám, TP.HCM', NULL),
	('PB17', N'Phòng pháp chế', N'888 Nguyễn Đình Chiểu, Hà Nội', NULL),
	('PB18', N'Phòng kinh doanh 2', N'999 Hoàng Diệu, Đà Nẵng', NULL),
	('PB19', N'Phòng sản xuất', N'101 Bạch Đằng, TP.HCM', NULL),
	('PB20', N'Phòng kiểm toán nội bộ', N'202 Võ Thị Sáu, Hà Nội', NULL),
	('PB21', N'Phòng công nghệ thông tin', N'303 Nguyễn Công Trứ, Cần Thơ', NULL),
	('PB22', N'Phòng đào tạo', N'404 Lê Đại Hành, TP.HCM', NULL),
	('PB23', N'Phòng thiết kế', N'505 Nam Kỳ Khởi Nghĩa, Hà Nội', NULL),
	('PB24', N'Phòng xuất nhập khẩu', N'606 Trường Chinh, Đà Nẵng', NULL),
	('PB25', N'Phòng logistics', N'707 An Dương Vương, TP.HCM', NULL),
	('PB26', N'Phòng bảo trì', N'808 Nguyễn Văn Trỗi, Hà Nội', NULL),
	('PB27', N'Phòng kho 3', N'909 Lê Quang Định, Cần Thơ', NULL),
	('PB28', N'Phòng marketing 2', N'101 Nguyễn Thiện Thuật, TP.HCM', NULL),
	('PB29', N'Phòng hành chính 2', N'212 Bùi Thị Xuân, TP.HCM', NULL),
	('PB30', N'Phòng kỹ thuật 3', N'313 Trần Phú, Hà Nội', NULL),
	('PB31', N'Phòng kinh doanh 3', N'414 Lê Hồng Phong, Đà Nẵng', NULL),
	('PB32', N'Phòng quản lý rủi ro', N'515 Nguyễn Kiệm, TP.HCM', NULL),
	('PB33', N'Phòng truyền thông', N'616 Tô Hiến Thành, Hà Nội', NULL),
	('PB34', N'Phòng nghiên cứu thị trường', N'717 Lý Tự Trọng, Cần Thơ', NULL),
	('PB35', N'Phòng dịch vụ khách hàng', N'818 Nguyễn Văn Cừ, TP.HCM', NULL),
	('PB36', N'Phòng kế toán 2', N'919 Hoàng Văn Thụ, Hà Nội', NULL),
	('PB37', N'Phòng phát triển sản phẩm', N'101 Phạm Ngũ Lão, Đà Nẵng', NULL),
	('PB38', N'Phòng kho 4', N'202 Lê Văn Sỹ, TP.HCM', NULL),
	('PB39', N'Phòng an toàn lao động', N'303 Nguyễn Trãi, Hà Nội', NULL),
	('PB40', N'Phòng quản lý dự án', N'404 Trần Hưng Đạo, Cần Thơ', NULL),
	('PB41', N'Phòng marketing 3', N'505 Nguyễn Văn Linh, TP.HCM', NULL),
	('PB42', N'Phòng kỹ thuật 4', N'606 Hai Bà Trưng, Hà Nội', NULL),
	('PB43', N'Phòng kinh doanh 4', N'707 Phạm Văn Đồng, Đà Nẵng', NULL),
	('PB44', N'Phòng kiểm soát chất lượng', N'808 Lý Thường Kiệt, TP.HCM', NULL),
	('PB45', N'Phòng tài chính 2', N'909 Nguyễn Huệ, Huế', NULL),
	('PB46', N'Phòng nhân sự 2', N'101 Điện Biên Phủ, Hà Nội', NULL),
	('PB47', N'Phòng công nghệ thông tin 2', N'202 Cách Mạng Tháng Tám, TP.HCM', NULL),
	('PB48', N'Phòng đào tạo 2', N'303 Nguyễn Đình Chiểu, Cần Thơ', NULL),
	('PB49', N'Phòng thiết kế 2', N'404 Hoàng Diệu, Hà Nội', NULL),
	('PB50', N'Phòng xuất nhập khẩu 2', N'505 Nam Kỳ Khởi Nghĩa, TP.HCM', NULL),
	('PB51', N'Phòng logistics 2', N'606 Trường Chinh, Đà Nẵng', NULL),
	('PB52', N'Phòng bảo trì 2', N'707 An Dương Vương, Hà Nội', NULL),
	('PB53', N'Phòng kho 5', N'808 Nguyễn Văn Trỗi, TP.HCM', NULL),
	('PB54', N'Phòng marketing 4', N'909 Lê Quang Định, Cần Thơ', NULL),
	('PB55', N'Phòng hành chính 3', N'101 Nguyễn Thiện Thuật, Hà Nội', NULL),
	('PB56', N'Phòng kỹ thuật 5', N'202 Bùi Thị Xuân, TP.HCM', NULL),
	('PB57', N'Phòng kinh doanh 5', N'303 Trần Phú, Đà Nẵng', NULL),
	('PB58', N'Phòng quản lý chuỗi cung ứng', N'404 Lê Hồng Phong, Hà Nội', NULL),
	('PB59', N'Phòng nghiên cứu công nghệ', N'505 Nguyễn Kiệm, TP.HCM', NULL),
	('PB60', N'Phòng truyền thông 2', N'606 Tô Hiến Thành, Huế', NULL),
	('PB61', N'Phòng dịch vụ hậu mãi', N'707 Lý Tự Trọng, Hà Nội', NULL),
	('PB62', N'Phòng kho 6', N'808 Nguyễn Văn Cừ, TP.HCM', NULL),
	('PB63', N'Phòng kế toán 3', N'909 Hoàng Văn Thụ, Cần Thơ', NULL),
	('PB64', N'Phòng phát triển kinh doanh', N'101 Phạm Ngũ Lão, Hà Nội', NULL),
	('PB65', N'Phòng an ninh', N'202 Lê Văn Sỹ, TP.HCM', NULL),
	('PB66', N'Phòng quản lý tài sản', N'303 Nguyễn Trãi, Đà Nẵng', NULL),
	('PB67', N'Phòng marketing 5', N'404 Trần Hưng Đạo, Hà Nội', NULL),
	('PB68', N'Phòng kỹ thuật 6', N'505 Nguyễn Văn Linh, TP.HCM', NULL),
	('PB69', N'Phòng kinh doanh 6', N'606 Hai Bà Trưng, Cần Thơ', NULL),
	('PB70', N'Phòng kiểm toán 2', N'707 Phạm Văn Đồng, Hà Nội', NULL),
	('PB71', N'Phòng nhân sự 3', N'808 Lý Thường Kiệt, TP.HCM', NULL),
	('PB72', N'Phòng công nghệ thông tin 3', N'909 Nguyễn Huệ, Đà Nẵng', NULL),
	('PB73', N'Phòng đào tạo 3', N'101 Điện Biên Phủ, Hà Nội', NULL),
	('PB74', N'Phòng thiết kế 3', N'202 Cách Mạng Tháng Tám, TP.HCM', NULL),
	('PB75', N'Phòng xuất nhập khẩu 3', N'303 Nguyễn Đình Chiểu, Cần Thơ', NULL),
	('PB76', N'Phòng logistics 3', N'404 Hoàng Diệu, Hà Nội', NULL),
	('PB77', N'Phòng bảo trì 3', N'505 Nam Kỳ Khởi Nghĩa, TP.HCM', NULL),
	('PB78', N'Phòng kho 7', N'606 Trường Chinh, Đà Nẵng', NULL),
	('PB79', N'Phòng marketing 6', N'707 An Dương Vương, Hà Nội', NULL),
	('PB80', N'Phòng hành chính 4', N'808 Nguyễn Văn Trỗi, TP.HCM', NULL),
	('PB81', N'Phòng kỹ thuật 7', N'909 Lê Quang Định, Cần Thơ', NULL),
	('PB82', N'Phòng kinh doanh 7', N'101 Nguyễn Thiện Thuật, Hà Nội', NULL),
	('PB83', N'Phòng quản lý năng lượng', N'202 Bùi Thị Xuân, TP.HCM', NULL),
	('PB84', N'Phòng nghiên cứu dữ liệu', N'303 Trần Phú, Đà Nẵng', NULL),
	('PB85', N'Phòng truyền thông 3', N'404 Lê Hồng Phong, Hà Nội', NULL),
	('PB86', N'Phòng dịch vụ khách hàng 2', N'505 Nguyễn Kiệm, TP.HCM', NULL),
	('PB87', N'Phòng kho 8', N'606 Tô Hiến Thành, Huế', NULL),
	('PB88', N'Phòng kế toán 4', N'707 Lý Tự Trọng, Hà Nội', NULL),
	('PB89', N'Phòng phát triển phần mềm', N'808 Nguyễn Văn Cừ, TP.HCM', NULL),
	('PB90', N'Phòng an toàn thông tin', N'909 Hoàng Văn Thụ, Cần Thơ', NULL),
	('PB91', N'Phòng quản lý môi trường', N'101 Phạm Ngũ Lão, Hà Nội', NULL),
	('PB92', N'Phòng marketing 7', N'202 Lê Văn Sỹ, TP.HCM', NULL),
	('PB93', N'Phòng kỹ thuật 8', N'303 Nguyễn Trãi, Đà Nẵng', NULL),
	('PB94', N'Phòng kinh doanh 8', N'404 Trần Hưng Đạo, Hà Nội', NULL),
	('PB95', N'Phòng kiểm soát nội bộ', N'505 Nguyễn Văn Linh, TP.HCM', NULL),
	('PB96', N'Phòng nhân sự 4', N'606 Hai Bà Trưng, Cần Thơ', NULL),
	('PB97', N'Phòng công nghệ thông tin 4', N'707 Phạm Văn Đồng, Hà Nội', NULL),
	('PB98', N'Phòng đào tạo 4', N'808 Lý Thường Kiệt, TP.HCM', NULL),
	('PB99', N'Phòng thiết kế 4', N'909 Nguyễn Huệ, Đà Nẵng', NULL),
	('PB100', N'Phòng xuất nhập khẩu 4', N'101 Điện Biên Phủ, Hà Nội', NULL),
	('PB101', N'Phòng logistics 4', N'202 Cách Mạng Tháng Tám, TP.HCM', NULL),
	('PB102', N'Phòng bảo trì 4', N'303 Nguyễn Đình Chiểu, Cần Thơ', NULL),
	('PB103', N'Phòng kho 9', N'404 Hoàng Diệu, Hà Nội', NULL),
	('PB104', N'Phòng marketing 8', N'505 Nam Kỳ Khởi Nghĩa, TP.HCM', NULL),
	('PB105', N'Phòng hành chính 5', N'606 Trường Chinh, Đà Nẵng', NULL);
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
('K01', 'Kho A', 'KL01', N'101 Đường Nguyễn Văn Cừ'),
('K02', 'Kho B', 'TT01', N'202 Đường Lê Lợi'),
('K03', 'Kho C', 'CD01', N'303 Đường Hai Bà Trưng'),
('K04', 'Kho D', 'DG01', N'404 Đường Trần Phú'),
('K05', 'Kho E', 'KL02', N'505 Đường Phạm Ngũ Lão'),
('K06', 'Kho F', 'KL01', N'201 Đường Lê Lợi'),
('K07', 'Kho G', 'TT01', N'302 Đường Nguyễn Huệ'),
('K08', 'Kho H', 'CD01', N'403 Đường Trần Phú'),
('K09', 'Kho I', 'DG01', N'504 Đường Lý Thường Kiệt'),
('K10', 'Kho J', 'KL02', N'605 Đường Hùng Vương'),
('K11', 'Kho K', 'TT02', N'706 Đường Nguyễn Trãi'),
('K12', 'Kho L', 'CD02', N'807 Đường Phạm Văn Đồng'),
('K13', 'Kho M', 'DG02', N'908 Đường Võ Văn Tần'),
('K14', 'Kho N', 'KL03', N'109 Đường Lê Đại Hành'),
('K15', 'Kho O', 'TT03', N'210 Đường Nguyễn Đình Chiểu'),
('K16', 'Kho P', 'CD03', N'311 Đường Hoàng Diệu'),
('K17', 'Kho Q', 'DG03', N'412 Đường Nam Kỳ Khởi Nghĩa'),
('K18', 'Kho R', 'KL04', N'513 Đường Cách Mạng Tháng Tám'),
('K19', 'Kho S', 'TT04', N'614 Đường Điện Biên Phủ'),
('K20', 'Kho T', 'DG04', N'715 Đường Hai Bà Trưng'),
('K21', 'Kho U', 'KL05', N'816 Đường Lê Hồng Phong'),
('K22', 'Kho V', 'DG05', N'917 Đường Nguyễn Văn Cừ'),
('K23', 'Kho W', 'KL01', N'108 Đường Trường Chinh'),
('K24', 'Kho X', 'TT01', N'209 Đường Võ Thị Sáu'),
('K25', 'Kho Y', 'CD01', N'310 Đường Nguyễn Thị Minh Khai'),
('K26', 'Kho Z', 'DG01', N'411 Đường Bạch Đằng'),
('K27', 'Kho 27', 'KL02', N'512 Đường Lê Văn Sỹ'),
('K28', 'Kho 28', 'TT02', N'613 Đường Hoàng Văn Thụ'),
('K29', 'Kho 29', 'CD02', N'714 Đường Nguyễn Công Trứ'),
('K30', 'Kho 30', 'DG02', N'815 Đường Phạm Ngũ Lão'),
('K31', 'Kho 31', 'KL03', N'916 Đường An Dương Vương'),
('K32', 'Kho 32', 'TT03', N'107 Đường Nguyễn Văn Trỗi'),
('K33', 'Kho 33', 'CD03', N'208 Đường Lê Quang Định'),
('K34', 'Kho 34', 'DG03', N'309 Đường Nguyễn Thiện Thuật'),
('K35', 'Kho 35', 'KL04', N'410 Đường Bùi Thị Xuân'),
('K36', 'Kho 36', 'TT04', N'511 Đường Trần Hưng Đạo'),
('K37', 'Kho 37', 'DG04', N'612 Đường Lý Tự Trọng'),
('K38', 'Kho 38', 'KL05', N'713 Đường Nguyễn Kiệm'),
('K39', 'Kho 39', 'DG05', N'814 Đường Tô Hiến Thành'),
('K40', 'Kho 40', 'KL04', N'915 Đường Lê Văn Tám'),
('K41', 'Kho 41', 'TT04', N'106 Đường Nguyễn Du'),
('K42', 'Kho 42', 'CD03', N'207 Đường Phạm Hồng Thái'),
('K43', 'Kho 43', 'DG03', N'308 Đường Nguyễn Bỉnh Khiêm'),
('K44', 'Kho 44', 'KL05', N'409 Đường Điện Biên Phủ'),
('K45', 'Kho 45', 'DG04', N'510 Đường Lê Văn Sỹ'),
('K46', 'Kho 46', 'TT01', N'611 Đường Hoàng Văn Thụ'),
('K47', 'Kho 47', 'KL01', N'712 Đường Nguyễn Công Trứ'),
('K48', 'Kho 48', 'CD01', N'813 Đường Phạm Ngũ Lão'),
('K49', 'Kho 49', 'DG01', N'914 Đường An Dương Vương'),
('K50', 'Kho 50', 'KL02', N'105 Đường Nguyễn Văn Trỗi'),
('K51', 'Kho 51', 'TT02', N'206 Đường Lê Quang Định'),
('K52', 'Kho 52', 'CD02', N'307 Đường Nguyễn Thiện Thuật'),
('K53', 'Kho 53', 'DG02', N'408 Đường Bùi Thị Xuân'),
('K54', 'Kho 54', 'KL03', N'509 Đường Trần Hưng Đạo'),
('K55', 'Kho 55', 'TT03', N'610 Đường Lý Tự Trọng');
GO 

-- 10. Thêm dữ liệu vào KHO_LOAIVATTU
INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES
('K06', 'KL02'), ('K06', 'TT01'), ('K07', 'TT02'), ('K07', 'CD01'),
('K08', 'CD02'), ('K08', 'DG01'), ('K09', 'DG02'), ('K09', 'KL03'),
('K10', 'KL04'), ('K10', 'TT03'), ('K11', 'TT04'), ('K11', 'CD03'),
('K12', 'DG03'), ('K12', 'KL05'), ('K13', 'DG04'), ('K13', 'TT01'),
('K14', 'KL01'), ('K14', 'CD01'), ('K15', 'TT02'), ('K15', 'DG05'),
('K16', 'CD02'), ('K16', 'KL02'), ('K17', 'DG01'), ('K17', 'TT03'),
('K18', 'KL03'), ('K18', 'CD03'), ('K19', 'DG02'), ('K19', 'TT04'),
('K20', 'KL04'), ('K20', 'DG03'), ('K21', 'TT01'), ('K21', 'KL05'),
('K22', 'DG04'), ('K22', 'CD01'), ('K23', 'KL01'), ('K23', 'TT02'),
('K24', 'CD02'), ('K24', 'DG05'), ('K25', 'KL02'), ('K25', 'TT03'),
('K26', 'DG01'), ('K26', 'CD03'), ('K27', 'KL03'), ('K27', 'TT04'),
('K28', 'DG02'), ('K28', 'KL04'), ('K29', 'TT01'), ('K29', 'DG03'),
('K30', 'KL05'), ('K30', 'CD01'), ('K31', 'DG04'), ('K31', 'TT02'),
('K32', 'KL01'), ('K32', 'CD02'), ('K33', 'DG05'), ('K33', 'TT03'),
('K34', 'KL02'), ('K34', 'CD03'), ('K35', 'DG01'), ('K35', 'TT04'),
('K36', 'KL03'), ('K36', 'DG02'), ('K37', 'TT01'), ('K37', 'KL04'),
('K38', 'DG03'), ('K38', 'CD01'), ('K39', 'KL05'), ('K39', 'TT02'),
('K40', 'DG04'), ('K40', 'CD02'), ('K41', 'KL01'), ('K41', 'DG05'),
('K42', 'TT03'), ('K42', 'KL02'), ('K43', 'CD03'), ('K43', 'DG01'),
('K44', 'TT04'), ('K44', 'KL03'), ('K45', 'DG02'), ('K45', 'TT01'),
('K46', 'KL04'), ('K46', 'CD01'), ('K47', 'DG03'), ('K47', 'KL05'),
('K48', 'TT02'), ('K48', 'DG04'), ('K49', 'CD02'), ('K49', 'KL01'),
('K50', 'TT03'), ('K50', 'DG05'), ('K51', 'KL02'), ('K51', 'CD03'),
('K52', 'TT04'), ('K52', 'DG01'), ('K53', 'KL03'), ('K53', 'TT01'),
('K54', 'DG02'), ('K54', 'KL04'), ('K55', 'CD01'), ('K55', 'DG03');
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
    ('VT17', N'Hộp khẩu trang y tế', 'DG05'),
	('VT18', N'Sắt hộp 50x50', 'KL01'),
	('VT19', N'Sắt tròn phi 12', 'KL01'),
	('VT20', N'Sắt U100', 'KL01'),
	('VT21', N'Sắt tấm 2mm', 'KL01'),
	('VT22', N'Sắt góc 40x40', 'KL01'),
	('VT23', N'Thép tấm 5mm', 'KL02'),
	('VT24', N'Thép cuộn phi 6', 'KL02'),
	('VT25', N'Thép I200', 'KL02'),
	('VT26', N'Thép hộp 60x120', 'KL02'),
	('VT27', N'Thép ray 10kg', 'KL02'),
	('VT28', N'Bao xi măng Bỉm Sơn', 'KL03'),
	('VT29', N'Xi măng trắng Thái Bình', 'KL03'),
	('VT30', N'Bao xi măng Nghi Sơn', 'KL03'),
	('VT31', N'Xi măng rời', 'KL03'),
	('VT32', N'Xi măng đa dụng', 'KL03'),
	('VT33', N'Gạo tám thơm', 'KL04'),
	('VT34', N'Gạo nếp cái', 'KL04'),
	('VT35', N'Gạo lứt đỏ', 'KL04'),
	('VT36', N'Gạo tẻ thường', 'KL04'),
	('VT37', N'Gạo hạt ngọc', 'KL04'),
	('VT38', N'Phân DAP 50kg', 'KL05'),
	('VT39', N'Phân lân 25kg', 'KL05'),
	('VT40', N'Phân kali 10kg', 'KL05'),
	('VT41', N'Phân hữu cơ vi sinh', 'KL05'),
	('VT42', N'Phân ure 50kg', 'KL05'),
	('VT43', N'Nước uống đóng chai 500ml', 'TT01'),
	('VT44', N'Nước tinh khiết 5L', 'TT01'),
	('VT45', N'Nước khoáng Lavie 1.5L', 'TT01'),
	('VT46', N'Nước suối Aquafina', 'TT01'),
	('VT47', N'Nước ion kiềm 1L', 'TT01'),
	('VT48', N'Dầu diesel 0.05S', 'TT02'),
	('VT49', N'Dầu thủy lực 68', 'TT02'),
	('VT50', N'Dầu máy may', 'TT02'),
	('VT51', N'Dầu động cơ 4T', 'TT02'),
	('VT52', N'Dầu truyền nhiệt', 'TT02'),
	('VT53', N'Sơn nước Maxilite 5L', 'TT03'),
	('VT54', N'Sơn dầu Bạch Tuyết', 'TT03'),
	('VT55', N'Sơn lót chống kiềm', 'TT03'),
	('VT56', N'Sơn phủ ngoại thất', 'TT03'),
	('VT57', N'Sơn chống thấm 10L', 'TT03'),
	('VT58', N'Xăng A92', 'TT04'),
	('VT59', N'Xăng E5 RON92', 'TT04'),
	('VT60', N'Xăng máy bay Jet A1', 'TT04'),
	('VT61', N'Xăng sinh học E10', 'TT04'),
	('VT62', N'Xăng RON95-III', 'TT04'),
	('VT63', N'Thép cây phi 16', 'CD01'),
	('VT64', N'Thép cây phi 20', 'CD01'),
	('VT65', N'Thép cây dài 12m', 'CD01'),
	('VT66', N'Thép cây phi 25', 'CD01'),
	('VT67', N'Thép cây D10', 'CD01'),
	('VT68', N'Ống nhựa PPR D32', 'CD02'),
	('VT69', N'Ống nhựa uPVC D90', 'CD02'),
	('VT70', N'Ống nhựa HDPE D50', 'CD02'),
	('VT71', N'Ống nhựa PVC D110', 'CD02'),
	('VT72', N'Ống nhựa mềm 25mm', 'CD02'),
	('VT73', N'Dây điện Trần Phú 1.5', 'CD03'),
	('VT74', N'Dây cáp điện 4x16', 'CD03'),
	('VT75', N'Dây điện đôi 2x2.5', 'CD03'),
	('VT76', N'Dây cáp mạng Cat6', 'CD03'),
	('VT77', N'Dây điện Lioa 4mm', 'CD03'),
	('VT78', N'Gỗ thông xẻ 2m', 'DG01'),
	('VT79', N'Gỗ cao su kiện 1.5m3', 'DG01'),
	('VT80', N'Gỗ trắc xẻ 50cm', 'DG01'),
	('VT81', N'Gỗ lim kiện 2m3', 'DG01'),
	('VT82', N'Gỗ sồi xẻ 1m', 'DG01'),
	('VT83', N'Hộp đinh 5cm', 'DG02'),
	('VT84', N'Hộp đinh bê tông 3cm', 'DG02'),
	('VT85', N'Hộp đinh gỗ 4cm', 'DG02'),
	('VT86', N'Hộp đinh thép 6cm', 'DG02'),
	('VT87', N'Hộp đinh vít 1kg', 'DG02'),
	('VT88', N'Bịch ốc vít 3cm', 'DG03'),
	('VT89', N'Bịch ốc lục giác M8', 'DG03'),
	('VT90', N'Bịch ốc vít tự khoan', 'DG03'),
	('VT91', N'Bịch ốc neo 10mm', 'DG03'),
	('VT92', N'Bịch ốc vít inox', 'DG03'),
	('VT93', N'Thùng sơn Nippon 20L', 'DG04'),
	('VT94', N'Thùng sơn Kova 18L', 'DG04'),
	('VT95', N'Thùng sơn chống cháy', 'DG04'),
	('VT96', N'Thùng sơn epoxy 15L', 'DG04'),
	('VT97', N'Thùng sơn PU 10L', 'DG04'),
	('VT98', N'Hộp khẩu trang 3M', 'DG05'),
	('VT99', N'Hộp khẩu trang vải', 'DG05'),
	('VT100', N'Hộp khẩu trang trẻ em', 'DG05'),
	('VT101', N'Hộp khẩu trang N95', 'DG05'),
	('VT102', N'Hộp khẩu trang kháng khuẩn', 'DG05'),
	('VT103', N'Sắt tấm 3mm', 'KL01'),
	('VT104', N'Thép cây phi 8', 'KL02'),
	('VT105', N'Bao xi măng Vicem', 'KL03'),
	('VT106', N'Gạo nếp gừng', 'KL04'),
	('VT107', N'Phân bón lá 1kg', 'KL05'),
	('VT108', N'Nước khoáng Vĩnh Hảo', 'TT01'),
	('VT109', N'Dầu hộp số 80W90', 'TT02'),
	('VT110', N'Sơn Jotun 5L', 'TT03'),
	('VT111', N'Xăng RON97', 'TT04'),
	('VT112', N'Thép cây phi 32', 'CD01'),
	('VT113', N'Ống nhựa PPR D20', 'CD02'),
	('VT114', N'Dây cáp điện 3x4', 'CD03'),
	('VT115', N'Gỗ căm xe kiện', 'DG01'),
	('VT116', N'Hộp đinh 7cm', 'DG02'),
	('VT117', N'Bịch ốc vít 4cm', 'DG03');
GO

-- 12. Thêm dữ liệu vào TONKHO
INSERT INTO TONKHO (MaKho, MaVatTu, SoLuong, DonVi, ViTri) VALUES
    ('K01', 'VT01', 100, N'kg', N'Kệ A1'),
    ('K02', 'VT06', 200, N'lít', N'Kệ B2'),
    ('K03', 'VT10', 300, N'm', N'Kệ C3'),
    ('K04', 'VT13', 400, N'kiện', N'Kệ D4'),
    ('K05', 'VT02', 500, N'kg', N'Kệ E5'),
	('K06', 'VT18', 150, N'kg', N'Kệ F1'),
	('K06', 'VT23', 200, N'kg', N'Kệ F2'),
	('K07', 'VT43', 300, N'lít', N'Kệ G1'),
	('K07', 'VT48', 250, N'lít', N'Kệ G2'),
	('K08', 'VT63', 400, N'm', N'Kệ H1'),
	('K08', 'VT68', 350, N'm', N'Kệ H2'),
	('K09', 'VT78', 100, N'kiện', N'Kệ I1'),
	('K09', 'VT83', 200, N'hộp', N'Kệ I2'),
	('K10', 'VT24', 180, N'kg', N'Kệ J1'),
	('K10', 'VT28', 300, N'bao', N'Kệ J2'),
	('K11', 'VT49', 220, N'lít', N'Kệ K1'),
	('K11', 'VT53', 270, N'lít', N'Kệ K2'),
	('K12', 'VT69', 500, N'm', N'Kệ L1'),
	('K12', 'VT73', 600, N'm', N'Kệ L2'),
	('K13', 'VT88', 150, N'bịch', N'Kệ M1'),
	('K13', 'VT93', 120, N'thùng', N'Kệ M2'),
	('K14', 'VT19', 250, N'kg', N'Kệ N1'),
	('K14', 'VT33', 400, N'kg', N'Kệ N2'),
	('K15', 'VT44', 320, N'lít', N'Kệ O1'),
	('K15', 'VT58', 280, N'lít', N'Kệ O2'),
	('K16', 'VT64', 450, N'm', N'Kệ P1'),
	('K16', 'VT74', 700, N'm', N'Kệ P2'),
	('K17', 'VT79', 110, N'kiện', N'Kệ Q1'),
	('K17', 'VT98', 300, N'hộp', N'Kệ Q2'),
	('K18', 'VT29', 350, N'bao', N'Kệ R1'),
	('K18', 'VT38', 500, N'bao', N'Kệ R2'),
	('K19', 'VT54', 260, N'lít', N'Kệ S1'),
	('K19', 'VT59', 290, N'lít', N'Kệ S2'),
	('K20', 'VT70', 550, N'm', N'Kệ T1'),
	('K20', 'VT84', 180, N'hộp', N'Kệ T2'),
	('K21', 'VT20', 200, N'kg', N'Kệ U1'),
	('K21', 'VT34', 420, N'kg', N'Kệ U2'),
	('K22', 'VT94', 130, N'thùng', N'Kệ V1'),
	('K22', 'VT99', 250, N'hộp', N'Kệ V2'),
	('K23', 'VT25', 230, N'kg', N'Kệ W1'),
	('K23', 'VT39', 480, N'bao', N'Kệ W2'),
	('K24', 'VT45', 310, N'lít', N'Kệ X1'),
	('K24', 'VT60', 270, N'lít', N'Kệ X2'),
	('K25', 'VT65', 500, N'm', N'Kệ Y1'),
	('K25', 'VT75', 650, N'm', N'Kệ Y2'),
	('K26', 'VT80', 140, N'kiện', N'Kệ Z1'),
	('K26', 'VT89', 160, N'bịch', N'Kệ Z2'),
	('K27', 'VT30', 360, N'bao', N'Kệ A2'),
	('K27', 'VT40', 510, N'bao', N'Kệ A3'),
	('K28', 'VT55', 240, N'lít', N'Kệ B3'),
	('K28', 'VT61', 300, N'lít', N'Kệ B4'),
	('K29', 'VT71', 520, N'm', N'Kệ C4'),
	('K29', 'VT85', 190, N'hộp', N'Kệ C5'),
	('K30', 'VT21', 210, N'kg', N'Kệ D5'),
	('K30', 'VT35', 430, N'kg', N'Kệ D6'),
	('K31', 'VT95', 150, N'thùng', N'Kệ E6'),
	('K31', 'VT100', 280, N'hộp', N'Kệ E7'),
	('K32', 'VT26', 240, N'kg', N'Kệ F3'),
	('K32', 'VT41', 490, N'bao', N'Kệ F4'),
	('K33', 'VT46', 330, N'lít', N'Kệ G3'),
	('K33', 'VT62', 260, N'lít', N'Kệ G4'),
	('K34', 'VT66', 470, N'm', N'Kệ H3'),
	('K34', 'VT76', 680, N'm', N'Kệ H4'),
	('K35', 'VT81', 120, N'kiện', N'Kệ I3'),
	('K35', 'VT90', 170, N'bịch', N'Kệ I4'),
	('K36', 'VT31', 370, N'bao', N'Kệ J3'),
	('K36', 'VT42', 520, N'bao', N'Kệ J4'),
	('K37', 'VT56', 250, N'lít', N'Kệ K3'),
	('K37', 'VT103', 220, N'kg', N'Kệ K4'),
	('K38', 'VT72', 530, N'm', N'Kệ L3'),
	('K38', 'VT86', 200, N'hộp', N'Kệ L4'),
	('K39', 'VT96', 140, N'thùng', N'Kệ M3'),
	('K39', 'VT101', 290, N'hộp', N'Kệ M4'),
	('K40', 'VT27', 250, N'kg', N'Kệ N3'),
	('K40', 'VT36', 440, N'kg', N'Kệ N4'),
	('K41', 'VT47', 340, N'lít', N'Kệ O3'),
	('K41', 'VT104', 230, N'kg', N'Kệ O4'),
	('K42', 'VT67', 480, N'm', N'Kệ P3'),
	('K42', 'VT77', 690, N'm', N'Kệ P4'),
	('K43', 'VT82', 130, N'kiện', N'Kệ Q3'),
	('K43', 'VT91', 180, N'bịch', N'Kệ Q4'),
	('K44', 'VT32', 380, N'bao', N'Kệ R3'),
	('K44', 'VT105', 350, N'bao', N'Kệ R4'),
	('K45', 'VT57', 260, N'lít', N'Kệ S3'),
	('K45', 'VT106', 410, N'kg', N'Kệ S4'),
	('K46', 'VT73', 540, N'm', N'Kệ T3'),
	('K46', 'VT87', 210, N'hộp', N'Kệ T4'),
	('K47', 'VT97', 150, N'thùng', N'Kệ U3'),
	('K47', 'VT102', 300, N'hộp', N'Kệ U4'),
	('K48', 'VT22', 260, N'kg', N'Kệ V3'),
	('K48', 'VT37', 450, N'kg', N'Kệ V4'),
	('K49', 'VT48', 350, N'lít', N'Kệ W3'),
	('K49', 'VT107', 500, N'kg', N'Kệ W4'),
	('K50', 'VT68', 490, N'm', N'Kệ X3'),
	('K50', 'VT88', 190, N'bịch', N'Kệ X4'),
	('K51', 'VT83', 220, N'hộp', N'Kệ Y3'),
	('K51', 'VT108', 360, N'lít', N'Kệ Y4'),
	('K52', 'VT109', 270, N'lít', N'Kệ Z3'),
	('K52', 'VT110', 280, N'lít', N'Kệ Z4'),
	('K53', 'VT111', 290, N'lít', N'Kệ A4'),
	('K53', 'VT112', 510, N'm', N'Kệ A5'),
	('K54', 'VT113', 520, N'm', N'Kệ B5'),
	('K54', 'VT114', 700, N'm', N'Kệ B6'),
	('K55', 'VT115', 140, N'kiện', N'Kệ C6');
GO

-- 13. Thêm dữ liệu vào NHACUNGCAP
INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoai, Email, DiaChi) VALUES
('NCC01', N'Công ty TNHH Vật Liệu Xây Dựng Đại Phúc', '0912345678', 'daiphuc@gmail.com', N'123 Nguyễn Văn Cừ, Quận 5, TP.HCM'),
('NCC02', N'Công ty CP Nước Giải Khát Sài Gòn', '0923456789', 'nuocsaigon@gmail.com', N'456 Lê Lợi, Quận 1, TP.HCM'),
('NCC03', N'Công ty TNHH Thép Việt Nam', '0934567890', 'thepvietnam@gmail.com', N'789 Hai Bà Trưng, Quận 3, TP.HCM'),
('NCC04', N'Công ty CP Gỗ An Cường', '0945678901', 'goancuong@gmail.com', N'101 Trần Phú, Quận 7, TP.HCM'),
('NCC05', N'Công ty TNHH Thép Pomina', '0956789012', 'pomina@gmail.com', N'202 Phạm Ngũ Lão, Quận 1, TP.HCM'),
('NCC06', N'Công ty TNHH Minh Phát', '0931234567', 'minhphat@gmail.com', N'15 Lê Lợi, Quận 1, TP.HCM'),
('NCC07', N'Công ty CP Đại Lộc', '0913456789', 'dailoc@gmail.com', N'27 Nguyễn Trãi, Quận 5, TP.HCM'),
('NCC08', N'Công ty TNHH Hưng Thịnh', '0942345678', 'hungthinh@gmail.com', N'89 Võ Thị Sáu, Quận 3, TP.HCM'),
('NCC09', N'Công ty TNHH Thiên An', '0909876543', 'thienan@gmail.com', N'34 Bùi Thị Xuân, Quận Tân Bình, TP.HCM'),
('NCC10', N'Công ty CP Nam Long', '0928765432', 'namlong@gmail.com', N'56 Nguyễn Văn Cừ, Quận 1, TP.HCM'),
('NCC11', N'Công ty TNHH Thành Đạt', '0937654321', 'thanhdat@gmail.com', N'78 Lê Đại Hành, Quận 11, TP.HCM'),
('NCC12', N'Công ty TNHH Hòa Bình', '0916543210', 'hoabinh@gmail.com', N'12 Nguyễn Đình Chiểu, Quận Phú Nhuận, TP.HCM'),
('NCC13', N'Công ty TNHH Phúc Lộc', '0905432109', 'phucloc@gmail.com', N'45 Phạm Văn Đồng, Quận Gò Vấp, TP.HCM'),
('NCC14', N'Công ty CP Đông Á', '0944321098', 'dongacom@gmail.com', N'67 Nguyễn Thị Minh Khai, Quận 3, TP.HCM'),
('NCC15', N'Công ty TNHH An Phú', '0923210987', 'anphu@gmail.com', N'23 Lê Văn Sỹ, Quận Tân Bình, TP.HCM'),
('NCC16', N'Công ty TNHH Đại Phát', '0932109876', 'daiphat@gmail.com', N'88 Cách Mạng Tháng 8, Quận 3, TP.HCM'),
('NCC17', N'Công ty CP Quốc Tế', '0911098765', 'quocte@gmail.com', N'55 Lê Hồng Phong, Quận 5, TP.HCM'),
('NCC18', N'Công ty TNHH Vạn Thành', '0900987654', 'vanthanh@gmail.com', N'99 Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM'),
('NCC19', N'Công ty TNHH Hoàng Long', '0949876543', 'hoanglong@gmail.com', N'44 Trần Hưng Đạo, Quận 1, TP.HCM'),
('NCC20', N'Công ty CP Tân Tiến', '0928765432', 'tantien@gmail.com', N'66 Lê Quang Định, Quận Bình Thạnh, TP.HCM'),
('NCC21', N'Công ty TNHH Minh Tâm', '0937654321', 'minhtam@gmail.com', N'77 Nguyễn Thái Học, Quận 1, TP.HCM'),
('NCC22', N'Công ty TNHH Bảo Ngọc', '0916543210', 'baongoc@gmail.com', N'22 Đinh Công Tráng, Quận Tân Bình, TP.HCM'),
('NCC23', N'Công ty TNHH Việt Hưng', '0905432109', 'viethung@gmail.com', N'33 Nguyễn Công Trứ, Quận 1, TP.HCM'),
('NCC24', N'Công ty CP Thành Công', '0944321098', 'thanhcong@gmail.com', N'11 Nguyễn Hữu Cảnh, Quận Bình Thạnh, TP.HCM'),
('NCC25', N'Công ty TNHH Tân Phát', '0923210987', 'tanphat@gmail.com', N'44 Lê Thị Riêng, Quận 1, TP.HCM'),
('NCC26', N'Công ty TNHH Thiên Phát', '0932109876', 'thienphat@gmail.com', N'55 Trường Chinh, Quận Tân Bình, TP.HCM'),
('NCC27', N'Công ty CP Hòa Phát', '0911098765', 'hoaphat@gmail.com', N'66 Lý Tự Trọng, Quận 1, TP.HCM'),
('NCC28', N'Công ty TNHH Đại An', '0900987654', 'daian@gmail.com', N'77 Nguyễn Du, Quận 1, TP.HCM'),
('NCC29', N'Công ty TNHH Hạnh Phúc', '0949876543', 'hanhphuc@gmail.com', N'88 Phạm Hồng Thái, Quận 1, TP.HCM'),
('NCC30', N'Công ty CP Vĩnh Phát', '0928765432', 'vinhphat@gmail.com', N'99 Nguyễn Bỉnh Khiêm, Quận 1, TP.HCM'),
('NCC31', N'Công ty TNHH Ánh Sao', '0937654321', 'anhsao@gmail.com', N'12 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM'),
('NCC32', N'Công ty TNHH Minh Long', '0916543210', 'minhlong@gmail.com', N'23 Lê Văn Sỹ, Quận 3, TP.HCM'),
('NCC33', N'Công ty TNHH Thành Tâm', '0905432109', 'thanhtam@gmail.com', N'34 Nguyễn Thị Diệu, Quận 3, TP.HCM'),
('NCC34', N'Công ty CP Tân Thành', '0944321098', 'tanthanh@gmail.com', N'45 Lê Văn Tám, Quận 3, TP.HCM'),
('NCC35', N'Công ty TNHH Phú Quý', '0923210987', 'phuquy@gmail.com', N'56 Nguyễn Đình Chiểu, Quận 3, TP.HCM'),
('NCC36', N'Công ty TNHH Hoàng Phát', '0932109876', 'hoangphat@gmail.com', N'67 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM'),
('NCC37', N'Công ty CP Đại Thành', '0911098765', 'daithanh@gmail.com', N'78 Lý Chính Thắng, Quận 3, TP.HCM'),
('NCC38', N'Công ty TNHH Ngọc Minh', '0900987654', 'ngocminh@gmail.com', N'89 Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM'),
('NCC39', N'Công ty TNHH Vạn Phát', '0949876543', 'vanphat@gmail.com', N'90 Nguyễn Huệ, Quận 1, TP.HCM'),
('NCC40', N'Công ty CP Thiên Long', '0928765432', 'thienlong@gmail.com', N'11 Lê Đại Hành, Quận 11, TP.HCM'),
('NCC41', N'Công ty TNHH Bảo Minh', '0937654321', 'baominh@gmail.com', N'22 Phạm Văn Đồng, Quận Gò Vấp, TP.HCM'),
('NCC42', N'Công ty TNHH Thành Long', '0916543210', 'thanhlong@gmail.com', N'33 Nguyễn Văn Cừ, Quận 5, TP.HCM'),
('NCC43', N'Công ty TNHH Hưng Phát', '0905432109', 'hungphat@gmail.com', N'44 Nguyễn Thị Minh Khai, Quận 3, TP.HCM'),
('NCC44', N'Công ty CP Việt Phát', '0944321098', 'vietphat@gmail.com', N'55 Lê Hồng Phong, Quận 5, TP.HCM'),
('NCC45', N'Công ty TNHH Đại Minh', '0923210987', 'daiminh@gmail.com', N'66 Nguyễn Trãi, Quận 5, TP.HCM'),
('NCC46', N'Công ty TNHH Tân Long', '0932109876', 'tanlong@gmail.com', N'77 Võ Thị Sáu, Quận 3, TP.HCM'),
('NCC47', N'Công ty CP Minh Thành', '0911098765', 'minhthanh@gmail.com', N'88 Lê Lợi, Quận 1, TP.HCM'),
('NCC48', N'Công ty TNHH Hòa Thành', '0900987654', 'hoathanh@gmail.com', N'99 Cách Mạng Tháng 8, Quận 3, TP.HCM'),
('NCC49', N'Công ty TNHH Phúc Thành', '0949876543', 'phucthanh@gmail.com', N'12 Nguyễn Công Trứ, Quận 1, TP.HCM'),
('NCC50', N'Công ty CP An Thành', '0928765432', 'anthanh@gmail.com', N'23 Nguyễn Hữu Cảnh, Quận Bình Thạnh, TP.HCM'),
('NCC51', N'Công ty TNHH Đại Thành', '0937654321', 'daithanh2@gmail.com', N'34 Lê Thị Riêng, Quận 1, TP.HCM'),
('NCC52', N'Công ty TNHH Minh Phúc', '0916543210', 'minhphuc@gmail.com', N'45 Trường Chinh, Quận Tân Bình, TP.HCM'),
('NCC53', N'Công ty TNHH Hoàng Thành', '0905432109', 'hoangthanh@gmail.com', N'56 Lý Tự Trọng, Quận 1, TP.HCM'),
('NCC54', N'Công ty CP Tân Minh', '0944321098', 'tanminh@gmail.com', N'67 Nguyễn Du, Quận 1, TP.HCM'),
('NCC55', N'Công ty TNHH Vĩnh Thành', '0923210987', 'vinhthanh@gmail.com', N'78 Phạm Hồng Thái, Quận 1, TP.HCM'),
('NCC56', N'Công ty TNHH Ngọc Thành', '0932109876', 'ngocthanh@gmail.com', N'89 Nguyễn Bỉnh Khiêm, Quận 1, TP.HCM'),
('NCC57', N'Công ty CP Hòa Minh', '0911098765', 'hoaminh@gmail.com', N'90 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM'),
('NCC58', N'Công ty TNHH Phúc Minh', '0900987654', 'phucminh@gmail.com', N'11 Lê Văn Sỹ, Quận 3, TP.HCM'),
('NCC59', N'Công ty TNHH Thành Minh', '0949876543', 'thanhminh@gmail.com', N'22 Nguyễn Thị Diệu, Quận 3, TP.HCM'),
('NCC60', N'Công ty CP Đại Minh', '0928765432', 'daiminh2@gmail.com', N'33 Lê Văn Tám, Quận 3, TP.HCM'),
('NCC61', N'Công ty TNHH Tân Hưng', '0937654321', 'tanhung@gmail.com', N'44 Nguyễn Đình Chiểu, Quận 3, TP.HCM'),
('NCC62', N'Công ty TNHH Minh Hưng', '0916543210', 'minhhung@gmail.com', N'55 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM'),
('NCC63', N'Công ty TNHH Hoàng Hưng', '0905432109', 'hoanghung@gmail.com', N'66 Lý Chính Thắng, Quận 3, TP.HCM'),
('NCC64', N'Công ty CP Việt Minh', '0944321098', 'vietminh@gmail.com', N'77 Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM'),
('NCC65', N'Công ty TNHH Đại Hưng', '0923210987', 'daihung@gmail.com', N'88 Nguyễn Huệ, Quận 1, TP.HCM'),
('NCC66', N'Công ty TNHH Thành Hưng', '0932109876', 'thanhhung@gmail.com', N'99 Lê Đại Hành, Quận 11, TP.HCM'),
('NCC67', N'Công ty CP Phúc Hưng', '0911098765', 'phuchung@gmail.com', N'12 Phạm Văn Đồng, Quận Gò Vấp, TP.HCM'),
('NCC68', N'Công ty TNHH Minh Thành', '0900987654', 'minhthanh2@gmail.com', N'23 Nguyễn Văn Cừ, Quận 5, TP.HCM'),
('NCC69', N'Công ty TNHH Hòa Hưng', '0949876543', 'hoahung@gmail.com', N'34 Nguyễn Thị Minh Khai, Quận 3, TP.HCM'),
('NCC70', N'Công ty CP Tân Hưng', '0928765432', 'tanhung2@gmail.com', N'45 Lê Hồng Phong, Quận 5, TP.HCM'),
('NCC71', N'Công ty TNHH Đại Hùng', '0937654321', 'daihung2@gmail.com', N'56 Nguyễn Trãi, Quận 5, TP.HCM'),
('NCC72', N'Công ty TNHH Thành Hùng', '0916543210', 'thanhhung2@gmail.com', N'67 Võ Thị Sáu, Quận 3, TP.HCM'),
('NCC73', N'Công ty TNHH Minh Hùng', '0905432109', 'minhhung2@gmail.com', N'78 Lê Lợi, Quận 1, TP.HCM'),
('NCC74', N'Công ty CP Hoàng Hùng', '0944321098', 'hoanghung2@gmail.com', N'89 Cách Mạng Tháng 8, Quận 3, TP.HCM'),
('NCC75', N'Công ty TNHH Việt Hùng', '0923210987', 'viethung2@gmail.com', N'90 Nguyễn Công Trứ, Quận 1, TP.HCM'),
('NCC76', N'Công ty TNHH Phúc Hùng', '0932109876', 'phuchung2@gmail.com', N'11 Nguyễn Hữu Cảnh, Quận Bình Thạnh, TP.HCM'),
('NCC77', N'Công ty CP An Hùng', '0911098765', 'anhung@gmail.com', N'22 Lê Thị Riêng, Quận 1, TP.HCM'),
('NCC78', N'Công ty TNHH Tân Hùng', '0900987654', 'tanhung3@gmail.com', N'33 Trường Chinh, Quận Tân Bình, TP.HCM'),
('NCC79', N'Công ty TNHH Minh Hiền', '0949876543', 'minhhien@gmail.com', N'44 Lý Tự Trọng, Quận 1, TP.HCM'),
('NCC80', N'Công ty CP Đại Hiền', '0928765432', 'daihien@gmail.com', N'55 Nguyễn Du, Quận 1, TP.HCM'),
('NCC81', N'Công ty TNHH Thành Hiền', '0937654321', 'thanhhien@gmail.com', N'66 Phạm Hồng Thái, Quận 1, TP.HCM'),
('NCC82', N'Công ty TNHH Hoàng Hiền', '0916543210', 'hoanghien@gmail.com', N'77 Nguyễn Bỉnh Khiêm, Quận 1, TP.HCM'),
('NCC83', N'Công ty TNHH Việt Hiền', '0905432109', 'viethien@gmail.com', N'88 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM'),
('NCC84', N'Công ty CP Phúc Hiền', '0944321098', 'phuchien@gmail.com', N'99 Lê Văn Sỹ, Quận 3, TP.HCM'),
('NCC85', N'Công ty TNHH An Hiền', '0923210987', 'anhien@gmail.com', N'12 Nguyễn Thị Diệu, Quận 3, TP.HCM'),
('NCC86', N'Công ty TNHH Tân Hiền', '0932109876', 'tanhien@gmail.com', N'23 Lê Văn Tám, Quận 3, TP.HCM'),
('NCC87', N'Công ty CP Minh Hiền', '0911098765', 'minhhien2@gmail.com', N'34 Nguyễn Đình Chiểu, Quận 3, TP.HCM'),
('NCC88', N'Công ty TNHH Đại Hảo', '0900987654', 'daihao@gmail.com', N'45 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM'),
('NCC89', N'Công ty TNHH Thành Hảo', '0949876543', 'thanhhao@gmail.com', N'56 Lý Chính Thắng, Quận 3, TP.HCM'),
('NCC90', N'Công ty TNHH Hoàng Hảo', '0928765432', 'hoanghao@gmail.com', N'67 Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM'),
('NCC91', N'Công ty CP Việt Hảo', '0937654321', 'viethao@gmail.com', N'78 Nguyễn Huệ, Quận 1, TP.HCM'),
('NCC92', N'Công ty TNHH Phúc Hảo', '0916543210', 'phuchao@gmail.com', N'89 Lê Đại Hành, Quận 11, TP.HCM'),
('NCC93', N'Công ty TNHH An Hảo', '0905432109', 'anhao@gmail.com', N'90 Phạm Văn Đồng, Quận Gò Vấp, TP.HCM'),
('NCC94', N'Công ty CP Tân Hảo', '0944321098', 'tanhao@gmail.com', N'11 Nguyễn Văn Cừ, Quận 5, TP.HCM'),
('NCC95', N'Công ty TNHH Minh Hảo', '0923210987', 'minhhao@gmail.com', N'22 Nguyễn Thị Minh Khai, Quận 3, TP.HCM'),
('NCC96', N'Công ty TNHH Đại Hợp', '0932109876', 'daihop@gmail.com', N'33 Lê Hồng Phong, Quận 5, TP.HCM'),
('NCC97', N'Công ty CP Thành Hợp', '0911098765', 'thanhhop@gmail.com', N'44 Nguyễn Trãi, Quận 5, TP.HCM'),
('NCC98', N'Công ty TNHH Hoàng Hợp', '0900987654', 'hoanghop@gmail.com', N'55 Võ Thị Sáu, Quận 3, TP.HCM'),
('NCC99', N'Công ty TNHH Việt Hợp', '0949876543', 'viethop@gmail.com', N'66 Lê Lợi, Quận 1, TP.HCM'),
('NCC100', N'Công ty CP Phúc Hợp', '0928765432', 'phuchop@gmail.com', N'77 Cách Mạng Tháng 8, Quận 3, TP.HCM'),
('NCC101', N'Công ty TNHH An Hợp', '0937654321', 'anhop@gmail.com', N'88 Nguyễn Công Trứ, Quận 1, TP.HCM'),
('NCC102', N'Công ty TNHH Tân Hợp', '0916543210', 'tanhop@gmail.com', N'99 Nguyễn Hữu Cảnh, Quận Bình Thạnh, TP.HCM'),
('NCC103', N'Công ty CP Minh Hợp', '0905432109', 'minhhop@gmail.com', N'12 Lê Thị Riêng, Quận 1, TP.HCM'),
('NCC104', N'Công ty TNHH Đại Hòa', '0944321098', 'daihoa@gmail.com', N'23 Trường Chinh, Quận Tân Bình, TP.HCM'),
('NCC105', N'Công ty TNHH Thành Hòa', '0923210987', 'thanhhoa@gmail.com', N'34 Lý Tự Trọng, Quận 1, TP.HCM');
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
    ('PN01', '2024-01-01', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN02', '2024-01-15', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN03', '2024-02-03', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN04', '2024-02-20', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN05', '2024-03-05', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN06', '2024-03-12', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN07', '2024-04-03', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN08', '2024-04-18', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN09', '2024-05-05', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN10', '2024-05-22', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN11', '2024-06-07', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN12', '2024-06-15', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN13', '2024-07-09', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN14', '2024-07-25', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN15', '2024-08-11', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN16', '2024-08-19', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN17', '2024-09-13', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN18', '2024-09-28', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN19', '2024-10-15', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN20', '2024-10-30', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN21', '2024-11-07', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN22', '2024-11-22', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN23', '2024-12-09', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN24', '2024-12-24', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN25', '2024-01-10', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN26', '2024-02-14', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN27', '2024-03-21', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN28', '2024-04-27', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN29', '2024-05-16', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN30', '2024-06-30', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN31', '2024-07-12', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN32', '2024-08-26', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN33', '2024-09-17', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN34', '2024-10-23', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN35', '2024-11-29', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN36', '2024-01-04', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN37', '2024-02-18', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN38', '2024-03-25', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN39', '2024-04-08', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN40', '2024-05-29', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN41', '2024-06-13', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN42', '2024-07-27', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN43', '2024-08-14', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN44', '2024-09-30', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN45', '2024-10-16', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN46', '2024-11-21', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN47', '2024-12-06', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN48', '2024-01-23', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN49', '2024-02-28', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN50', '2024-03-14', 'K05', 'NCC05', N'Chờ duyệt'),
    ('PN51', '2024-04-19', 'K01', 'NCC01', N'Chờ duyệt'),
    ('PN52', '2024-05-25', 'K02', 'NCC02', N'Chờ duyệt'),
    ('PN53', '2024-06-08', 'K03', 'NCC03', N'Chờ duyệt'),
    ('PN54', '2024-07-20', 'K04', 'NCC04', N'Chờ duyệt'),
    ('PN55', '2024-08-31', 'K05', 'NCC05', N'Chờ duyệt');
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
    ('PX01', '2024-12-01', '2024-12-03', 'K01', 'PB01', 'NV01', 'NV02', N'Xuất vật tư cho sản xuất', N'Chờ Duyệt'),
    ('PX02', '2024-12-02', '2024-12-04', 'K02', 'PB02', 'NV02', 'NV03', N'Xuất vật tư cho bảo trì', N'Chờ Duyệt'),
    ('PX03', '2024-12-04', '2024-12-06', 'K03', 'PB03', 'NV03', 'NV04', N'Xuất vật tư cho dự án', N'Chờ Duyệt'),
    ('PX04', '2024-12-05', '2024-12-07', 'K04', 'PB04', 'NV04', 'NV05', N'Xuất vật tư cho xây dựng', N'Chờ Duyệt'),
    ('PX05', '2024-12-06', '2024-12-08', 'K05', 'PB05', 'NV05', 'NV01', N'Xuất vật tư cho nghiên cứu', N'Chờ Duyệt'),
    ('PX06', '2024-12-07', '2024-12-09', 'K01', 'PB01', 'NV07', 'NV08', N'Xuất vật tư cho dự án 6', N'Chờ Duyệt'),
    ('PX07', '2024-12-08', '2024-12-10', 'K02', 'PB02', 'NV08', 'NV09', N'Xuất vật tư cho dự án 7', N'Chờ Duyệt'),
    ('PX08', '2024-12-09', '2024-12-11', 'K03', 'PB03', 'NV09', 'NV10', N'Xuất vật tư cho dự án 8', N'Chờ Duyệt'),
    ('PX09', '2024-12-10', '2024-12-12', 'K04', 'PB04', 'NV10', 'NV11', N'Xuất vật tư cho dự án 9', N'Chờ Duyệt'),
    ('PX10', '2024-12-11', '2024-12-13', 'K05', 'PB05', 'NV11', 'NV12', N'Xuất vật tư cho dự án 10', N'Chờ Duyệt'),
    ('PX11', '2024-12-12', '2024-12-14', 'K01', 'PB01', 'NV12', 'NV13', N'Xuất vật tư cho dự án 11', N'Chờ Duyệt'),
    ('PX12', '2024-12-13', '2024-12-15', 'K02', 'PB02', 'NV13', 'NV14', N'Xuất vật tư cho dự án 12', N'Chờ Duyệt'),
    ('PX13', '2024-12-14', '2024-12-16', 'K03', 'PB03', 'NV14', 'NV15', N'Xuất vật tư cho dự án 13', N'Chờ Duyệt'),
    ('PX14', '2024-12-15', '2024-12-17', 'K04', 'PB04', 'NV15', 'NV16', N'Xuất vật tư cho dự án 14', N'Chờ Duyệt'),
    ('PX15', '2024-12-16', '2024-12-18', 'K05', 'PB05', 'NV16', 'NV17', N'Xuất vật tư cho dự án 15', N'Chờ Duyệt'),
    ('PX16', '2024-12-17', '2024-12-19', 'K01', 'PB01', 'NV17', 'NV18', N'Xuất vật tư cho dự án 16', N'Chờ Duyệt'),
    ('PX17', '2024-12-18', '2024-12-20', 'K02', 'PB02', 'NV18', 'NV19', N'Xuất vật tư cho dự án 17', N'Chờ Duyệt'),
    ('PX18', '2024-12-19', '2024-12-21', 'K03', 'PB03', 'NV19', 'NV20', N'Xuất vật tư cho dự án 18', N'Chờ Duyệt'),
    ('PX19', '2024-12-20', '2024-12-22', 'K04', 'PB04', 'NV20', 'NV21', N'Xuất vật tư cho dự án 19', N'Chờ Duyệt'),
    ('PX20', '2024-12-21', '2024-12-23', 'K05', 'PB05', 'NV21', 'NV22', N'Xuất vật tư cho dự án 20', N'Chờ Duyệt'),
    ('PX21', '2024-12-22', '2024-12-24', 'K01', 'PB01', 'NV22', 'NV23', N'Xuất vật tư cho dự án 21', N'Chờ Duyệt'),
    ('PX22', '2024-12-23', '2024-12-25', 'K02', 'PB02', 'NV23', 'NV24', N'Xuất vật tư cho dự án 22', N'Chờ Duyệt'),
    ('PX23', '2024-12-24', '2024-12-26', 'K03', 'PB03', 'NV24', 'NV01', N'Xuất vật tư cho dự án 23', N'Chờ Duyệt'),
    ('PX24', '2024-12-25', '2024-12-27', 'K04', 'PB04', 'NV01', 'NV02', N'Xuất vật tư cho dự án 24', N'Chờ Duyệt'),
    ('PX25', '2024-12-26', '2024-12-28', 'K05', 'PB05', 'NV02', 'NV03', N'Xuất vật tư cho dự án 25', N'Chờ Duyệt'),
    ('PX26', '2024-12-27', '2024-12-29', 'K01', 'PB01', 'NV03', 'NV04', N'Xuất vật tư cho dự án 26', N'Chờ Duyệt'),
    ('PX27', '2024-12-28', '2024-12-30', 'K02', 'PB02', 'NV04', 'NV05', N'Xuất vật tư cho dự án 27', N'Chờ Duyệt'),
    ('PX28', '2024-12-29', '2024-12-31', 'K03', 'PB03', 'NV05', 'NV06', N'Xuất vật tư cho dự án 28', N'Chờ Duyệt'),
    ('PX29', '2024-12-30', '2025-01-01', 'K04', 'PB04', 'NV06', 'NV07', N'Xuất vật tư cho dự án 29', N'Chờ Duyệt'),
    ('PX30', '2024-12-31', '2025-01-02', 'K05', 'PB05', 'NV07', 'NV08', N'Xuất vật tư cho dự án 30', N'Chờ Duyệt'),
    ('PX31', '2025-01-01', '2025-01-03', 'K01', 'PB01', 'NV08', 'NV09', N'Xuất vật tư cho dự án 31', N'Chờ Duyệt'),
    ('PX32', '2025-01-02', '2025-01-04', 'K02', 'PB02', 'NV09', 'NV10', N'Xuất vật tư cho dự án 32', N'Chờ Duyệt'),
    ('PX33', '2025-01-03', '2025-01-05', 'K03', 'PB03', 'NV10', 'NV11', N'Xuất vật tư cho dự án 33', N'Chờ Duyệt'),
    ('PX34', '2025-01-04', '2025-01-06', 'K04', 'PB04', 'NV11', 'NV12', N'Xuất vật tư cho dự án 34', N'Chờ Duyệt'),
    ('PX35', '2025-01-05', '2025-01-07', 'K05', 'PB05', 'NV12', 'NV13', N'Xuất vật tư cho dự án 35', N'Chờ Duyệt'),
    ('PX36', '2025-01-06', '2025-01-08', 'K01', 'PB01', 'NV13', 'NV14', N'Xuất vật tư cho dự án 36', N'Chờ Duyệt'),
    ('PX37', '2025-01-07', '2025-01-09', 'K02', 'PB02', 'NV14', 'NV15', N'Xuất vật tư cho dự án 37', N'Chờ Duyệt'),
    ('PX38', '2025-01-08', '2025-01-10', 'K03', 'PB03', 'NV15', 'NV16', N'Xuất vật tư cho dự án 38', N'Chờ Duyệt'),
    ('PX39', '2025-01-09', '2025-01-11', 'K04', 'PB04', 'NV16', 'NV17', N'Xuất vật tư cho dự án 39', N'Chờ Duyệt'),
    ('PX40', '2025-01-10', '2025-01-12', 'K05', 'PB05', 'NV17', 'NV18', N'Xuất vật tư cho dự án 40', N'Chờ Duyệt');
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
    ('PX40', 'VT01', 5, 73, 5, 73);
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