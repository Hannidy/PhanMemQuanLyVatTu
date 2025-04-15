package dao;

import entity.model_Kho;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class KhoDAO {

    public void insert(model_Kho kho) throws SQLException {
        String sql = "INSERT INTO KHO (MaKho, TenKho, MaLoaiVatTu, DiaChi) VALUES (?, ?, ?, ?)";
        String newMaKho = this.selectMaxId(); // Lấy mã vật tư mới

        JDBCHelper.update(sql,
                newMaKho,
                kho.getTenKho(),
                kho.getMaloaivatTu(),
                kho.getDiaChi()); // Sửa dấu chấm phẩy thành dấu đóng ngoặc
    }

    public void update(model_Kho kho) throws SQLException {
        String sql = "UPDATE KHO SET TenKho = ?, MaLoaiVatTu = ?, DiaChi = ? WHERE MaKho = ?";
        JDBCHelper.update(sql,
                kho.getTenKho(),
                kho.getMaloaivatTu(),
                kho.getDiaChi(),
                kho.getMaKho());
    }

    public void delete(String maKHO) throws SQLException {
        String sql = "DELETE FROM KHO WHERE MaKho = ?";
        JDBCHelper.update(sql, maKHO);
    }

    public String selectMaxId() throws SQLException {
    String sql = "SELECT MAX(CAST(SUBSTRING(MaKho, 2, LEN(MaKho) - 1) AS INT)) FROM KHO";
    String newMaKho = "K01"; // Mặc định nếu bảng rỗng
    ResultSet rs = null;
    try {
        rs = JDBCHelper.query(sql);
        if (rs.next() && rs.getObject(1) != null) {
            int maxMaKho = rs.getInt(1);
            newMaKho = String.format("K%02d", maxMaKho + 1); // Định dạng mã với 2 chữ số
        }
    } catch (SQLException e) {
        throw new RuntimeException("Không thể lấy mã kho lớn nhất: " + e.getMessage(), e);
    } finally {
        JDBCHelper.close(rs); // Đóng tài nguyên an toàn
    }
    return newMaKho;
}

    public List<model_Kho> selectById(String maKho) {
        String sql = "SELECT * FROM Kho WHERE MaKho = ?";
        return selectBySQL(sql, maKho);
    }

    public List<model_Kho> selectAll() {
        String sql = "SELECT * FROM KHO";
        return this.selectBySQL(sql);
    }

    protected List<model_Kho> selectBySQL(String sql, Object... args) {
        List<model_Kho> list_Kho = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_Kho kho = new model_Kho();
                kho.setMaKho(rs.getString("MaKho"));
                kho.setTenKho(rs.getString("TenKho"));
                kho.setMaloaivatTu(rs.getString("MaLoaiVatTu"));
                kho.setDiaChi(rs.getString("DiaChi"));
                list_Kho.add(kho);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn Kho: " + e.getMessage());
        } finally {
            JDBCHelper.close(rs);
        }
        return list_Kho;
    }

    public boolean isTenKhoExist(String ten) {  // Hàm kiểm tra tên loai vat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM Kho WHERE TenKho = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean isMaLoaiDangDuocDungChoKho(String maLoai) {  // Hàm check ràng buộc
        String sql = "SELECT COUNT(*) FROM Kho WHERE MaLoaiVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maLoai)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
