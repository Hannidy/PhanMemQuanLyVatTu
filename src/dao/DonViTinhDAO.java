package dao;

import entity.model_DonViTinh;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class DonViTinhDAO {

    // Thêm đơn vị tính mới
    public void insert(model_DonViTinh dvt) {
        String sql = "INSERT INTO DonViTinh (TenDonVi, NhomVatTu) VALUES (?, ?)";
        JDBCHelper.update(sql, dvt.getTenDonVi(), dvt.getNhomVatTu());
    }

    // Cập nhật đơn vị tính
    public void update(model_DonViTinh dvt) {
        String sql = "UPDATE DonViTinh SET TenDonVi = ?, NhomVatTu = ? WHERE MaDonVi = ?";
        JDBCHelper.update(sql, dvt.getTenDonVi(), dvt.getNhomVatTu(), dvt.getMaDonVi());
    }

    // Xóa đơn vị tính theo mã
    public void delete(int maDonVi) {
        String sql = "DELETE FROM DonViTinh WHERE MaDonVi = ?";
        JDBCHelper.update(sql, maDonVi);
    }

    // Lấy đơn vị tính theo mã
    public model_DonViTinh selectById(int maDonVi) {
        String sql = "SELECT * FROM DonViTinh WHERE MaDonVi = ?";
        List<model_DonViTinh> list = this.selectBySQL(sql, maDonVi);
        return list.isEmpty() ? null : list.get(0);
    }

    // Lấy toàn bộ đơn vị tính
    public List<model_DonViTinh> selectAll() {
        String sql = "SELECT * FROM DonViTinh";
        return this.selectBySQL(sql);
    }

    // Tìm kiếm theo từ khóa
    public List<model_DonViTinh> selectByKeyword(String keyword, String column) {
        String kw = "%" + keyword + "%";
        String sql;

        switch (column) {
            case "Mã đơn vị" -> sql = "SELECT * FROM DonViTinh WHERE MaDonVi LIKE ?";
            case "Tên đơn vị" -> sql = "SELECT * FROM DonViTinh WHERE TenDonVi LIKE ?";
            case "Nhóm vật tư" -> sql = "SELECT * FROM DonViTinh WHERE NhomVatTu LIKE ?";
            default -> sql = "SELECT * FROM DonViTinh WHERE MaDonVi LIKE ? OR TenDonVi LIKE ? OR NhomVatTu LIKE ?";
        }

        return column.equals("Tất cả")
                ? this.selectBySQL(sql, kw, kw, kw)
                : this.selectBySQL(sql, kw);
    }

    // Lấy dữ liệu từ SQL
    protected List<model_DonViTinh> selectBySQL(String sql, Object... args) {
        List<model_DonViTinh> list = new ArrayList<>();
        try (ResultSet rs = JDBCHelper.query(sql, args)) {
            while (rs.next()) {
                model_DonViTinh dvt = new model_DonViTinh();
                dvt.setMaDonVi(rs.getInt("MaDonVi"));
                dvt.setTenDonVi(rs.getString("TenDonVi"));
                dvt.setNhomVatTu(rs.getString("NhomVatTu"));
                list.add(dvt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn DonViTinh: " + e.getMessage(), e);
        }
        return list;
    }

    // Kiểm tra tên đơn vị đã tồn tại
    public boolean isTenDonViExist(String tenDonVi) {
        String sql = "SELECT COUNT(*) FROM DonViTinh WHERE TenDonVi = ?";
        try (ResultSet rs = JDBCHelper.query(sql, tenDonVi)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra nhóm vật tư có đang dùng trong đơn vị tính khác không
    public boolean isNhomVatTuDangDuocDung(String nhom) {
        String sql = "SELECT COUNT(*) FROM DonViTinh WHERE NhomVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, nhom)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
   public String getTenDonViById(int maDonVi) {
    String sql = "SELECT TenDonVi FROM DonViTinh WHERE MaDonVi = ?";
    try (ResultSet rs = JDBCHelper.query(sql, maDonVi)) {
        if (rs.next()) {
            return rs.getString("TenDonVi");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Không rõ";
}

    
}