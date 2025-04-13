package dao;

import entity.model_ChucVu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.JDBCHelper;

public class ChucVuDAO {

    public void insert(model_ChucVu cv) {
        // Đảm bảo mã đã được gán trước khi insert
        if (cv.getMaChucVu()== null) {
            throw new IllegalStateException("Mã chức vụ chưa được sinh!");
        }
        String sql = "INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES (?, ?)";
        JDBCHelper.update(sql, cv.getMaChucVu(), cv.getTenChucVu());
    }

    public void update(model_ChucVu cv) {
        String sql = "UPDATE ChucVu SET TenChucVu = ?  WHERE MaChucVu = ?";
        JDBCHelper.update(sql,
                cv.getTenChucVu(),
                cv.getMaChucVu());
    }

    public void delete(String maChucVu) {
        String sql = "DELETE FROM ChuCVu WHERE MaChucVu = ?";
        JDBCHelper.update(sql, maChucVu);
    }

     public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaChucVu, 3, LEN(MaChucVu)-2) AS INT)) FROM CHUCVU WHERE MaChucVu LIKE 'CV[0-9]%'";
        String newMaChucVu = "CV01";
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaChucVu;
            }
            if (rs.next()) {
                int maxMaCV = rs.getInt(1);
                if (maxMaCV > 0) {
                    newMaChucVu = "CV" + (maxMaCV + 1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy mã chuwsc vuj mới nhất: " + e.getMessage());
        }
        return newMaChucVu;
    }

    public List<model_ChucVu> selectById(String maChucVu) {
        String sql = "SELECT * FROM ChucVu WHERE MaChucVu = ?";
        return selectBySQL(sql, maChucVu);
    }

    public List<model_ChucVu> selectByKeyWord(String keyWord, String columnVatTu) {
        String sql;
        String keyWord_2 = "%" + keyWord + "%";

        switch (columnVatTu) {
            case "Mã Chức Vụ":
                sql = "SELECT MaChucVu, TenChucVu FROM ChucVu WHERE MaChucVu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Tên Chức Vụ":
                sql = "SELECT MaChucVu, TenChucVu FROM ChucVu WHERE TenChucVu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            default:
                // Nếu không chỉ định cột cụ thể, tìm trên tất cả 3 cột
                sql = "SELECT MaChucVu, TenChucVu FROM ChucVu WHERE "
                        + "MaChucVu LIKE ? OR "
                        + "TenChucVu LIKE ?";
                return this.selectBySQL(sql, keyWord_2, keyWord_2);
        }
    }

    public List<model_ChucVu> selectAll() {
        String sql = "SELECt * FROM ChucVu";
        return this.selectBySQL(sql);
    }

    protected List<model_ChucVu> selectBySQL(String sql, Object... args) {
        List<model_ChucVu> list_ChucVu = new ArrayList<>();
        java.sql.ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_ChucVu cv = new model_ChucVu();
                cv.setMaChucVu(rs.getString("MaChucVu")); // Lấy Mã Vật Tư
                cv.setTenChucVu(rs.getString("TenChucVu")); // Lấy Tên Vật Tư

                list_ChucVu.add(cv);
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
        return list_ChucVu;
    }

    public boolean isTenChucVuExist(String ten) {  // Hàm kiểm tra tên loai vat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM ChucVu WHERE TenChucVu = ?";
        try (java.sql.ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    public Set<String> getDanhSachMaChucVu() {
    Set<String> dsMaChucVu = new HashSet<>();
    String sql = "SELECT MaChucVu FROM CHUCVU";
    try (java.sql.ResultSet rs = JDBCHelper.query(sql)) {
        while (rs.next()) {
            dsMaChucVu.add(rs.getString("MaChucVu"));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return dsMaChucVu;
} 
}
