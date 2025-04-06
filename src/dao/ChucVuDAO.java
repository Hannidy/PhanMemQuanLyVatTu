package dao;

import entity.model_ChucVu;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class ChucVuDAO {

    public void insert(model_ChucVu cv) {
        String sql = "INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES (?, ?)";
        String newMaChucVu = this.selectMaxId(); // Lấy mã vật tư mới
        JDBCHelper.update(sql,
                newMaChucVu,
                cv.getTenChucVu()); // Sửa dấu chấm phẩy thành dấu đóng ngoặc
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
        String sql = "SELECT MAX(CAST(SUBSTRING(MaChucVu, 3, LEN(MaChucVu)-2) AS INT)) FROM ChucVu";
        String newMaChucVu = "CV01"; // Mặc định nếu bảng rỗng.

        try (java.sql.ResultSet rs = JDBCHelper.query(sql)) {
            if (rs != null && rs.next() && rs.getObject(1) != null) {
                int maxMaChucVu = rs.getInt(1);
                newMaChucVu = "CV" + (maxMaChucVu + 1); // Tạo mã chức vụ tiếp theo
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
