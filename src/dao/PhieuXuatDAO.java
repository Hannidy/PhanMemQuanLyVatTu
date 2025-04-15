package dao;

import entity.model_ChiTietPhieuXuat;
import entity.model_PhieuXuat;
import java.sql.*;
import java.util.*;
import util.JDBCHelper;

public class PhieuXuatDAO {

    // Phương thức lấy tất cả phiếu xuất từ cơ sở dữ liệu
    public List<model_PhieuXuat> selectAll() {
        List<model_PhieuXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUXUAT";  // Truy vấn tất cả phiếu xuất

        try (ResultSet rs = JDBCHelper.query(sql)) {
            while (rs.next()) {
                model_PhieuXuat px = new model_PhieuXuat();
                px.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
                px.setNgayChungTu(rs.getDate("NgayChungTu"));
                px.setNgayXuat(rs.getDate("NgayXuat"));
                px.setMaKho(rs.getString("MaKho"));
                px.setMaPhongBanYeuCau(rs.getString("MaPhongBanYeuCau"));
                px.setMaNguoiXuat(rs.getString("MaNguoiXuat"));
                px.setMaNguoiNhan(rs.getString("MaNguoiNhan"));
                px.setLyDo(rs.getString("LyDo"));
                px.setTrangThai(rs.getString("TrangThai"));

                list.add(px);  // Thêm phiếu xuất vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy danh sách phiếu xuất: " + e.getMessage());
        }
        return list;  // Trả về danh sách các phiếu xuất
    }
    
    
    
    public void insert(model_PhieuXuat px) {
    String sql = "INSERT INTO PHIEUXUAT (MaPhieuXuat, NgayChungTu, NgayXuat, MaKho, MaPhongBanYeuCau, MaNguoiXuat, MaNguoiNhan, LyDo, TrangThai) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement pstmt = JDBCHelper.getStmt(sql, px.getMaPhieuXuat(), px.getNgayChungTu(), px.getNgayXuat(), 
                px.getMaKho(), px.getMaPhongBanYeuCau(), px.getMaNguoiXuat(), px.getMaNguoiNhan(), px.getLyDo(), px.getTrangThai());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi thêm phiếu xuất vào cơ sở dữ liệu: " + e.getMessage());
    }
}
    
    
    
    public void delete(String maPhieuXuat) {
    String sql = "DELETE FROM PHIEUXUAT WHERE MaPhieuXuat = ?";
    
    try {
        PreparedStatement stmt = JDBCHelper.getStmt(sql, maPhieuXuat);
        stmt.executeUpdate(); // Thực thi câu lệnh DELETE
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi xóa phiếu xuất: " + e.getMessage());
    }
}
    
    
public void update(model_PhieuXuat px) {
    String sql = "UPDATE PHIEUXUAT SET NgayChungTu = ?, NgayXuat = ?, MaKho = ?, MaPhongBanYeuCau = ?, MaNguoiXuat = ?, MaNguoiNhan = ?, LyDo = ?, TrangThai = ? WHERE MaPhieuXuat = ?";

    try (PreparedStatement stmt = JDBCHelper.getStmt(sql, 
                                                     px.getNgayChungTu(), 
                                                     px.getNgayXuat(), 
                                                     px.getMaKho(), 
                                                     px.getMaPhongBanYeuCau(), 
                                                     px.getMaNguoiXuat(), 
                                                     px.getMaNguoiNhan(), 
                                                     px.getLyDo(), 
                                                     px.getTrangThai(), 
                                                     px.getMaPhieuXuat())) {
        stmt.executeUpdate(); // Thực thi câu lệnh UPDATE
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi cập nhật phiếu xuất: " + e.getMessage());
    }
}
public List<model_PhieuXuat> selectBySql(String sql, String... args) {
    List<model_PhieuXuat> list = new ArrayList<>();
    try {
        // Đảm bảo gọi phương thức query của JDBCHelper và truyền đúng tham số
        ResultSet rs = JDBCHelper.query(sql, args);  // Truyền các tham số args vào câu lệnh SQL

        while (rs.next()) {
            model_PhieuXuat px = new model_PhieuXuat();
            px.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
            px.setNgayChungTu(rs.getDate("NgayChungTu"));
            px.setNgayXuat(rs.getDate("NgayXuat"));
            px.setMaKho(rs.getString("MaKho"));
            px.setMaPhongBanYeuCau(rs.getString("MaPhongBanYeuCau"));
            px.setMaNguoiXuat(rs.getString("MaNguoiXuat"));
            px.setMaNguoiNhan(rs.getString("MaNguoiNhan"));
            px.setTrangThai(rs.getString("TrangThai"));
            list.add(px);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}




}