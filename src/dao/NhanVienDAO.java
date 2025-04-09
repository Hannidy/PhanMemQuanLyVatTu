
package dao;

import entity.model_NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class NhanVienDAO {
    
    public void insert(model_NhanVien nv) {
        String sql = "INSERT INTO NHANVIEN (MaNhanVien , TenNhanVien ,MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai) VALUES (? ,? , ? , ? , ? , ?, ?)";
        String newMaNhanVien = this.selectMaxId(); 
        JDBCHelper.update(sql,
                newMaNhanVien,
                nv.getTenNhanVien(),
                nv.getMaChucVu(),
                nv.getMaPhongBan(),
                nv.getEmail(),
                nv.getSoDienthoai(),
                nv.getTrangThai()); // Sửa dấu chấm phẩy thành dấu đóng ngoặc
    }
    
    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaNhanVien, 3, LEN(MaNhanVien)-2) AS INT)) FROM NHANVIEN";
        String newMaNhanVien = "NV1";// Mặc định nếu bảng rỗng.
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaNhanVien;
            }
            if (rs.next() && rs.getObject(1) != null) {
                int maxMaVatTu = rs.getInt(1);
                newMaNhanVien = "NV" + (maxMaVatTu + 1); // Tạo NV tiếp theo
            }
            if (rs != null && rs.getObject(1) != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newMaNhanVien;
    }
    
    public void update(model_NhanVien nv) {
        String sql = "UPDATE NHANVIEN SET TenNhanVien = ?, MaChucVu = ? ,MaPhongBan = ? , Email = ? , SoDienThoai = ? , TrangThai = ? Where MaNhanVien = ?";
        JDBCHelper.update(sql,
                nv.getTenNhanVien(),
                nv.getMaChucVu(),
                nv.getMaPhongBan(),
                nv.getEmail(),
                nv.getSoDienthoai(),
                nv.getTrangThai(),
                nv.getMaNhanVien());
    }
    
    public void delete(String maNhanVien) {
        String sql = "DELETE FROM NHANVIEN WHERE MaNhanVien = ? ";
        JDBCHelper.update(sql, maNhanVien);
    }
    
    public List<model_NhanVien> selectById(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE MaNhanVien = ? ";
        return selectBySQL(sql, maNhanVien);
    }
    
    protected List<model_NhanVien> selectBySQL(String sql, Object... args) {
        List<model_NhanVien> list_NhanVien = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_NhanVien nv = new model_NhanVien();
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setTenNhanVien(rs.getString("TenNhanVien"));
                nv.setMaChucVu(rs.getString("MaChucVu"));
                nv.setMaPhongBan(rs.getString("MaPhongBan"));
                nv.setEmail(rs.getString("Email"));
                nv.setSoDienthoai(rs.getString("SoDienThoai"));
                nv.setTrangThai(rs.getString("TrangThai"));
                list_NhanVien.add(nv);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.getStatement().close(); // Đóng Statement trước
                    rs.close(); // Đóng ResultSet sau
                }
            } catch (SQLException e) {
            }
        }
        return list_NhanVien;
    }
     
    public List<model_NhanVien> selectAll() {
        String sql = "SELECt * FROM NHANVIEN";
        return this.selectBySQL(sql);
    }
    
    public List<model_NhanVien> selectByKeyWord(String keyWord, String columnNhanVien) {
        String sql;
        String keyWord_2 = "%" + keyWord + "%";

        switch (columnNhanVien) {
            case "Mã Nhân Viên":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Tên Nhân Viên":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Mã Chức Vụ":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Mã Phòng Ban":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Email":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Số Điện Thoại":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Trạng Thái":
                sql = "SELECT MaNhanVien, TenNhanVien, MaChucVu , MaPhongBan ,Email , SoDienThoai , TrangThai FROM VATTU WHERE MaNhanVien LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            default:
                // Nếu không chỉ định cột cụ thể, tìm trên tất cả 3 cột
                sql = "SELECT MaNhanVien , TenNhanVien , MaChucVu , MaPhongBan , Email , SoDienThoai , TrangThai FROM NHANVIEN WHERE "
                        + "MaNhanVien LIKE ? OR "
                        + "TenNhanVien LIKE ? OR "
                        + "MaChucVu LIKE ? OR "
                        + "MaPhongBan LIKE ? OR "
                        + "Emal LIKE ? OR "
                        + "SoDienThoai LIKE ? OR "
                        + "TrangThai LIKE ?";
                return this.selectBySQL(sql, keyWord_2, keyWord_2, keyWord_2, keyWord_2,keyWord_2, keyWord_2, keyWord_2 );
        }
    }
    
    public boolean isTenNhanVienExist(String ten) {  // Hàm kiểm tra tênvat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE TenNhanVien = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean isDanBocNhanvien(String LoaiMa) {  // Hàm check ràng buộc
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE MaChucVu = ? and MaPhongBan = ?" ;
        try (ResultSet rs = JDBCHelper.query(sql, LoaiMa)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     
    
}
