
package dao;

import entity.model_PhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class PhieuNhapDAO {

    public List<model_PhieuNhap> selectAll() {
    List<model_PhieuNhap> list = new ArrayList<>();
    String sql = "SELECT MaPhieuNhap, NgayNhap, MaKho, MaNhaCungCap, TrangThai FROM PHIEUNHAP";

    try {
        ResultSet rs = JDBCHelper.query(sql);
        while (rs.next()) {
            model_PhieuNhap pn = new model_PhieuNhap();
            pn.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
            pn.setNgayNhap(rs.getDate("NgayNhap"));
            pn.setMaKho(rs.getString("MaKho"));
            pn.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
            pn.setTrangThai(rs.getString("TrangThai"));
            list.add(pn);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

   public List<model_PhieuNhap> selectById(String maPhieuNhap) {
    List<model_PhieuNhap> list = new ArrayList<>();
    String sql = "SELECT * FROM PhieuNhap WHERE MaPhieuNhap LIKE ?";  // Sử dụng LIKE để tìm kiếm linh hoạt

    try (ResultSet rs = JDBCHelper.query(sql, "%" + maPhieuNhap + "%")) {  // Thêm % để tìm kiếm theo phần từ khóa
        while (rs.next()) {
            model_PhieuNhap pn = new model_PhieuNhap();
            pn.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
            pn.setNgayNhap(rs.getDate("NgayNhap"));
            pn.setMaKho(rs.getString("MaKho"));
            pn.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
            pn.setTrangThai(rs.getString("TrangThai"));
            list.add(pn);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;  // Trả về danh sách phiếu nhập tìm được
}
// Phương thức tìm kiếm tùy chỉnh theo SQL
    public List<model_PhieuNhap> selectBySql(String sql, Object... args) {
        List<model_PhieuNhap> list = new ArrayList<>();
        try (ResultSet rs = JDBCHelper.query(sql, args)) {
            while (rs.next()) {
                model_PhieuNhap pn = new model_PhieuNhap();
                pn.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
                pn.setNgayNhap(rs.getDate("NgayNhap"));
                pn.setMaKho(rs.getString("MaKho"));
                pn.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                pn.setTrangThai(rs.getString("TrangThai"));
                list.add(pn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn dữ liệu phiếu nhập: " + e.getMessage(), e);
        }
        return list;
    }

    

    public void insert(model_PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap (MaPhieuNhap, NgayNhap, MaKho, MaNhaCungCap, TrangThai) VALUES (?, ?, ?, ?, ?)";
        JDBCHelper.update(sql,
            pn.getMaPhieuNhap(),
            new java.sql.Date(pn.getNgayNhap().getTime()),
            pn.getMaKho(),
            pn.getMaNhaCungCap(),
            pn.getTrangThai()
        );
    }

    public void update(model_PhieuNhap pn) {
        String sql = "UPDATE PhieuNhap SET NgayNhap = ?, MaKho = ?, MaNhaCungCap = ?, TrangThai = ? WHERE MaPhieuNhap = ?";
        JDBCHelper.update(sql,
            new java.sql.Date(pn.getNgayNhap().getTime()),
            pn.getMaKho(),
            pn.getMaNhaCungCap(),
            pn.getTrangThai(),
            pn.getMaPhieuNhap()
        );
    }

    public void delete(String maPhieuNhap) {
        String sql = "DELETE FROM PhieuNhap WHERE MaPhieuNhap = ?";
        JDBCHelper.update(sql, maPhieuNhap);
    }
    
}
