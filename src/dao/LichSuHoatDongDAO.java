package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import entity.model_LichSuHoatDong;
import util.Auth;
import util.JDBCHelper;

public class LichSuHoatDongDAO {

    // Lấy danh sách lịch sử hoạt động của nhân viên hiện tại
    public List<model_LichSuHoatDong> getByMaNhanVien(String maNhanVien) {
    List<model_LichSuHoatDong> list = new ArrayList<>();
    String sql = "SELECT * FROM LICHSUHOATDONG WHERE MaNhanVien = ? ORDER BY ThoiGian DESC";
    try {
        ResultSet rs = JDBCHelper.query(sql, maNhanVien);
        while (rs.next()) {
            model_LichSuHoatDong lshd = new model_LichSuHoatDong(
                rs.getString("MaLichSu"),
                rs.getString("ThoiGian"),
                rs.getString("MaNhanVien"),
                rs.getString("TenNhanVien"),
                rs.getString("ChucVu"),
                rs.getString("ThaoTac"),
                rs.getString("QuanLy"),
                rs.getString("NoiDungThaoTac")
            );
            list.add(lshd);
        }
        System.out.println("Số bản ghi tìm thấy cho MaNhanVien " + maNhanVien + ": " + list.size());
        JDBCHelper.close(rs);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    // Xóa một bản ghi lịch sử hoạt động
    public boolean delete(String maLichSu) {
        String sql = "DELETE FROM LICHSUHOATDONG WHERE MaLichSu = ?";
        try {
            JDBCHelper.update(sql, maLichSu);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lưu thao tác của nhân viên
    public void saveThaoTac(String thaoTac, String quanLy, String noiDung) {
        if (!Auth.daDangNhap()) {
            System.out.println("Người dùng chưa đăng nhập, không thể lưu thao tác!");
            return;
        }

        String sql = "INSERT INTO LICHSUHOATDONG (MaLichSu, ThoiGian, MaNhanVien, TenNhanVien, ChucVu, ThaoTac, QuanLy, NoiDungThaoTac) "
                   + "VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?)";
        try {
            String maLichSu = "LS" + UUID.randomUUID().toString().substring(0, 8);
            JDBCHelper.update(sql, maLichSu, Auth.getMaNhanVien(), Auth.getTenNhanVien(), 
                             Auth.getTenChucVu(), thaoTac, quanLy, noiDung);
            System.out.println("Đã lưu thao tác: " + thaoTac + " - " + quanLy + " - " + noiDung);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}