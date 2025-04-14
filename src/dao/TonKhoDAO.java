
package dao;

import entity.model_TonKho;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class TonKhoDAO {
    
    public void insert(model_TonKho tk) {
    String sql = "INSERT INTO TonKho (MaKho, MaVatTu, SoLuong, DonVi, ViTri) VALUES (?, ?, ?, ?, ?)";
    String newMaKho = this.selectMaxId();
    JDBCHelper.update(sql,
            newMaKho,
            tk.getMaVatTu(),
            tk.getSoLuong(),
            tk.getDonVi(),
            tk.getViTri());
}

    public void update(model_TonKho tk) {
    String sql = "UPDATE TonKho SET MaVatTu = ?, SoLuong = ?, DonVi = ?, ViTri = ? WHERE MaKho = ?";
    JDBCHelper.update(sql,
            tk.getMaVatTu(),
            tk.getSoLuong(),
            tk.getDonVi(),
            tk.getViTri(),
            tk.getMaKho());
}

    public void delete(String maNhanVien) {
        String sql = "DELETE FROM TonKho WHERE MaKho = ?";
        JDBCHelper.update(sql, maNhanVien);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaKho, 2, LEN(MaKho)-1) AS INT)) FROM TonKho";
        String newMaKho = "K01";// Mặc định nếu bảng rỗng.
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaKho;
            }
            if (rs.next() && rs.getObject(1) != null) {
                int maxMaKho = rs.getInt(1);
                newMaKho = "K" + (maxMaKho + 1); // Tạo NV tiếp theo
            }
            if (rs != null && rs.getObject(1) != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newMaKho;
    }

    public List<model_TonKho> selectById(String maVatTu) {
        String sql = "SELECT * FROM TonKho WHERE MaKho = ?";
        return selectBySQL(sql, maVatTu);
    }


    public List<model_TonKho> selectAll() {
        String sql = "SELECt * FROM TonKho";
        return this.selectBySQL(sql);
    }

protected List<model_TonKho> selectBySQL(String sql, Object... args) {
    List<model_TonKho> list_TonKho = new ArrayList<>();
    ResultSet rs = null;

    try {
        rs = JDBCHelper.query(sql, args);
        while (rs.next()) {
            model_TonKho tk = new model_TonKho();
            tk.setMaKho(rs.getString("MaKho"));
            tk.setMaVatTu(rs.getString("MaVatTu"));
            tk.setSoLuong(rs.getInt("SoLuong")); // Sửa từ getString sang getInt
            tk.setDonVi(rs.getString("DonVi"));
            tk.setViTri(rs.getString("ViTri"));
            list_TonKho.add(tk);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            if (rs != null) {
                rs.getStatement().close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return list_TonKho;
}


     public static List<Object[]> kiemKeHangHoa() {
    List<Object[]> danhSachHangHoa = new ArrayList<>();
    String sql = """
        SELECT 
            tk.MaKho, 
            tk.MaVatTu, 
            tk.SoLuong AS SoLuongTonDauThang,
            COALESCE((
                SELECT SUM(ctpn.SoLuong) 
                FROM CHITIETPHIEUNHAP ctpn 
                WHERE ctpn.MaVatTu = tk.MaVatTu
            ), 0) AS SoLuongNhap,
            COALESCE((
                SELECT SUM(ctpx.SoLuong) 
                FROM CHITIETPHIEUXUAT ctpx 
                WHERE ctpx.MaVatTu = tk.MaVatTu
            ), 0) AS SoLuongXuat,
            (tk.SoLuong + 
             COALESCE((
                 SELECT SUM(ctpn.SoLuong) 
                 FROM CHITIETPHIEUNHAP ctpn 
                 WHERE ctpn.MaVatTu = tk.MaVatTu
             ), 0) - 
             COALESCE((
                 SELECT SUM(ctpx.SoLuong) 
                 FROM CHITIETPHIEUXUAT ctpx 
                 WHERE ctpx.MaVatTu = tk.MaVatTu
             ), 0)) AS SoLuongTonCuoiThang
        FROM TONKHO tk
        ORDER BY tk.MaKho, tk.MaVatTu;
    """;

    try (ResultSet rs = JDBCHelper.query(sql)) {
        while (rs.next()) {
            danhSachHangHoa.add(new Object[]{
                rs.getString("MaKho"),
                rs.getString("MaVatTu"),
                null, // Không có ngày cụ thể
                rs.getInt("SoLuongTonDauThang"),
                rs.getInt("SoLuongNhap"),
                rs.getInt("SoLuongXuat"),
                rs.getInt("SoLuongTonCuoiThang")
            });
        }
    } catch (SQLException e) {
        System.err.println("Lỗi khi kiểm kê hàng hóa: " + e.getMessage());
        e.printStackTrace();
    }
    return danhSachHangHoa;
}
     
     public static List<Object[]> kiemKePhieuNhap(java.sql.Date ngay) {
    List<Object[]> danhSachHangHoa = new ArrayList<>();
    String sql = """
        SELECT 
            tk.MaKho, 
            tk.MaVatTu, 
            tk.SoLuong AS SoLuongDauKy,
            COALESCE((
                SELECT SUM(ctpn.SoLuong) 
                FROM CHITIETPHIEUNHAP ctpn 
                JOIN PHIEUNHAP pn ON ctpn.MaPhieuNhap = pn.MaPhieuNhap 
                WHERE ctpn.MaVatTu = tk.MaVatTu AND pn.NgayNhap = ?
            ), 0) AS SoLuongNhap,
            COALESCE((
                SELECT SUM(ctpx.SoLuong) 
                FROM CHITIETPHIEUXUAT ctpx 
                JOIN PHIEUXUAT px ON ctpx.MaPhieuXuat = px.MaPhieuXuat 
                WHERE ctpx.MaVatTu = tk.MaVatTu AND px.NgayXuat = ?
            ), 0) AS SoLuongXuat,
            (tk.SoLuong + 
             COALESCE((
                 SELECT SUM(ctpn.SoLuong) 
                 FROM CHITIETPHIEUNHAP ctpn 
                 JOIN PHIEUNHAP pn ON ctpn.MaPhieuNhap = pn.MaPhieuNhap 
                 WHERE ctpn.MaVatTu = tk.MaVatTu AND pn.NgayNhap = ?
             ), 0) - 
             COALESCE((
                 SELECT SUM(ctpx.SoLuong) 
                 FROM CHITIETPHIEUXUAT ctpx 
                 JOIN PHIEUXUAT px ON ctpx.MaPhieuXuat = px.MaPhieuXuat 
                 WHERE ctpx.MaVatTu = tk.MaVatTu AND px.NgayXuat = ?
             ), 0)) AS SoLuongTonCuoiKy
        FROM TONKHO tk
        ORDER BY tk.MaKho, tk.MaVatTu;
    """;

    try (ResultSet rs = JDBCHelper.query(sql, ngay, ngay, ngay, ngay)) {
        while (rs.next()) {
            danhSachHangHoa.add(new Object[]{
                rs.getString("MaKho"),
                rs.getString("MaVatTu"),
                ngay, // Trả về ngày được chọn
                rs.getInt("SoLuongDauKy"),
                rs.getInt("SoLuongNhap"),
                rs.getInt("SoLuongXuat"),
                rs.getInt("SoLuongTonCuoiKy")
            });
        }
    } catch (SQLException e) {
        System.err.println("Lỗi khi lấy dữ liệu kiểm kê: " + e.getMessage());
        e.printStackTrace();
    }
    
    return danhSachHangHoa;
}
     
     public boolean chuyenHang(String maVatTu, String khoXuat, String khoNhan, int soLuong) {
        String sqlCheck = "SELECT SoLuong FROM TonKho WHERE MaKho = ? AND MaVatTu = ?";
        int soLuongTon = 0;

        try {
            ResultSet rs = JDBCHelper.query(sqlCheck, khoXuat, maVatTu);
            if (rs.next()) {
                soLuongTon = rs.getInt("SoLuong");
            }
            rs.close();

            if (soLuongTon < soLuong) {
                System.out.println("Kho xuất không đủ hàng để chuyển!");
                return true;
            }

            // Giảm số lượng trong kho xuất
            String sqlUpdateXuat = "UPDATE TonKho SET SoLuong = SoLuong - ? WHERE MaKho = ? AND MaVatTu = ?";
            JDBCHelper.update(sqlUpdateXuat, soLuong, khoXuat, maVatTu);

            // Kiểm tra kho nhận đã có vật tư chưa
            ResultSet rsNhan = JDBCHelper.query(sqlCheck, khoNhan, maVatTu);
            if (!rsNhan.next()) {
                // Nếu chưa có, thêm mới với số lượng ban đầu là 0 để tránh lỗi
                String sqlInsert = "INSERT INTO TonKho (MaKho, MaVatTu, SoLuong) VALUES (?, ?, 0)";
                JDBCHelper.update(sqlInsert, khoNhan, maVatTu);
            }
            rsNhan.close();

            // Cập nhật số lượng kho nhận
            String sqlUpdateNhan = "UPDATE TonKho SET SoLuong = SoLuong + ? WHERE MaKho = ? AND MaVatTu = ?";
            JDBCHelper.update(sqlUpdateNhan, soLuong, khoNhan, maVatTu);

            System.out.println("Chuyển hàng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
     
     public List<String> layCacKhoChuaMaVatTu(String maVatTu, String khoXuat, String khoNhan) {
    String sql = "SELECT MaKho FROM TonKho WHERE MaVatTu = ? AND (MaKho = ? OR MaKho = ?)";
    List<String> danhSachKho = new ArrayList<>();
    
    try (ResultSet rs = JDBCHelper.query(sql, maVatTu, khoXuat, khoNhan)) {
        while (rs.next()) {
            danhSachKho.add(rs.getString("MaKho"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return danhSachKho;
}
     
     public List<String> layTatCaKhoChuaMaVatTu(String maVatTu) {
    String sql = "SELECT MaKho FROM TonKho WHERE MaVatTu = ?";
    List<String> danhSach = new ArrayList<>();
    try (ResultSet rs = JDBCHelper.query(sql, maVatTu)) {
        while (rs.next()) {
            danhSach.add(rs.getString("MaKho"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return danhSach;
}
     
     public boolean kiemTraSoLuongTruocKhiChuyen(String maVatTu, String khoXuat, int soLuongCanChuyen) {
    String sql = "SELECT SoLuong FROM TonKho WHERE MaVatTu = ? AND MaKho = ?";
    try (ResultSet rs = JDBCHelper.query(sql, maVatTu, khoXuat)) {
        if (rs.next()) {
            int soLuongHienTai = rs.getInt("SoLuong");
            
            // Điều kiện: còn lại ít nhất 10 và không âm
            return soLuongCanChuyen > 0 
                && soLuongHienTai >= soLuongCanChuyen
                && (soLuongHienTai - soLuongCanChuyen) >= 10;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}




}
