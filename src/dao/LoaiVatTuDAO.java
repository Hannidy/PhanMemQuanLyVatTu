package dao;

import entity.model_LoaiVatTu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class LoaiVatTuDAO {

    public void insert(model_LoaiVatTu lvt) {
        // Đảm bảo mã đã được gán trước khi insert
        if (lvt.getMaloaivatTu() == null) {
            throw new IllegalStateException("Mã vật tư chưa được sinh!");
        }
        String sql = "INSERT INTO LOAIVATTU (MaLoaiVatTu, TenLoaiVatTu) VALUES (?, ?)";
        JDBCHelper.update(sql, lvt.getMaloaivatTu(), lvt.getTenloaivatTu());
    }

    public void update(model_LoaiVatTu lvt) {
        String sql = "UPDATE LOAIVATTU SET TenLoaiVatTu = ? WHERE MaLoaiVatTu = ?";
        JDBCHelper.update(sql,
                lvt.getTenloaivatTu(),
                lvt.getMaloaivatTu()); // Thêm tham số còn thiếu
    }

    public void delete(String maLoaiVatTu) {
        String sql = "DELETE FROM LOAIVATTU WHERE MaLoaiVatTu = ?";
        JDBCHelper.update(sql, maLoaiVatTu);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaLoaiVatTu, 4, LEN(MaLoaiVatTu)-3) AS INT)) FROM LOAIVATTU WHERE MaLoaiVatTu LIKE 'LVT[0-9]%'";
        String newMaLoaiVatTu = "LVT01";
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaLoaiVatTu;
            }
            if (rs.next()) {
                int maxMaLoaiVatTu = rs.getInt(1);
                if (maxMaLoaiVatTu > 0) {
                    newMaLoaiVatTu = "LVT" + (maxMaLoaiVatTu + 1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy mã vật tư mới nhất: " + e.getMessage());
        }
        return newMaLoaiVatTu;
    }

    public List<model_LoaiVatTu> selectById(String maLoaiVatTu) {
        String sql = "SELECT * FROM LOAIVATTU WHERE MaLoaiVatTu = ?";
        return selectBySQL(sql, maLoaiVatTu);
    }

    public List<model_LoaiVatTu> selectByKeyWord(String keyWord, String columnLoaiVatTu) {
        String sql;
        String keyWord_2 = "%" + keyWord + "%";

        switch (columnLoaiVatTu) {
            case "Mã Loại Vật Tư" -> {
                sql = "SELECT MaLoaiVatTu, TenVatTu FROM LOAIVATTU WHERE MaLoaiVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            }
            case "Tên Loại Vật Tư" -> {
                sql = "SELECT MaLoaiVatTu, TenVatTu FROM LOAIVATTU WHERE TenLoaiVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            }
            default -> {
                // Nếu không chỉ định cột cụ thể, tìm trên tất cả 3 cột
                sql = "SELECT MaLoaiVatTu, TenLoaiVatTu FROM LOAIVATTU WHERE "
                        + "MaLoaiVatTu LIKE ? OR "
                        + "TenLoaiVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2, keyWord_2);
            }
        }
    }

    public List<model_LoaiVatTu> selectAll() {
        String sql = "SELECT * FROM LOAIVATTU";
        return this.selectBySQL(sql);
    }

    protected List<model_LoaiVatTu> selectBySQL(String sql, Object... args) {
        List<model_LoaiVatTu> list_LoaiVatTu = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_LoaiVatTu lvt = new model_LoaiVatTu();
                lvt.setMaloaivatTu(rs.getString("MaLoaiVatTu")); // Lấy Mã Vật Tư
                lvt.setTenloaivatTu(rs.getString("TenLoaiVatTu")); // Lấy Tên Vật Tư
                list_LoaiVatTu.add(lvt);
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
        return list_LoaiVatTu;
    }

    public boolean isTenLoaiVatTuExist(String ten) {  // Hàm kiểm tra tên loai vat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM LoaiVatTu WHERE TenLoaiVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
}
