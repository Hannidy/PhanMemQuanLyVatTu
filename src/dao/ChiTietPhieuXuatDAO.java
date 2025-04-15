package dao;

import entity.model_ChiTietPhieuXuat;
import util.JDBCHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ChiTietPhieuXuatDAO {

    // Phương thức để thêm chi tiết phiếu xuất vào cơ sở dữ liệu
    // Phương thức để thêm chi tiết phiếu xuất vào cơ sở dữ liệu
    public void insert(model_ChiTietPhieuXuat ct) {
        String sql = "INSERT INTO CHITIETPHIEUXUAT (MaPhieuXuat, MaVatTu, DonViChuyenDoi, DonViGoc, SoLuongXuat, SoLuongQuyDoi) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            // Thực hiện câu lệnh update vào cơ sở dữ liệu
            JDBCHelper.update(sql,
                ct.getMaPhieuXuat(),
                ct.getMaVatTu(),
                ct.getDonViChuyenDoi(),
                ct.getDonViGoc(),
                ct.getSoLuongXuat(),
                ct.getSoLuongQuyDoi()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm chi tiết phiếu xuất: " + e.getMessage());
        }
    }



    // Phương thức để lấy tất cả chi tiết phiếu xuất
  // Phương thức để lấy tất cả chi tiết phiếu xuất
   public List<model_ChiTietPhieuXuat> selectAll() {
    String sql = "SELECT * FROM CHITIETPHIEUXUAT";
    List<model_ChiTietPhieuXuat> list = new ArrayList<>();
    
    try (ResultSet rs = JDBCHelper.query(sql)) {
        while (rs.next()) {
            model_ChiTietPhieuXuat ct = new model_ChiTietPhieuXuat();
            ct.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
            ct.setMaVatTu(rs.getString("MaVatTu"));
            ct.setDonViGoc(rs.getString("DonViGoc"));  // Lấy trực tiếp giá trị String
            ct.setDonViChuyenDoi(rs.getString("DonViChuyenDoi"));  // Lấy trực tiếp giá trị String
            ct.setSoLuongXuat(rs.getFloat("SoLuongXuat"));
            ct.setSoLuongQuyDoi(rs.getFloat("SoLuongQuyDoi"));
            list.add(ct);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi lấy chi tiết phiếu xuất: " + e.getMessage());
    }
    return list;
}


    // Phương thức để lấy chi tiết phiếu xuất theo mã phiếu xuất
   // Phương thức để lấy chi tiết phiếu xuất theo mã phiếu xuất
public List<model_ChiTietPhieuXuat> selectByMaPhieuXuat(String maPhieuXuat) {
    String sql = "SELECT * FROM CHITIETPHIEUXUAT WHERE MaPhieuXuat = ?";
    List<model_ChiTietPhieuXuat> list = new ArrayList<>();
    
    try (ResultSet rs = JDBCHelper.query(sql, maPhieuXuat)) {
        while (rs.next()) {
            model_ChiTietPhieuXuat ct = new model_ChiTietPhieuXuat();
            ct.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
            ct.setMaVatTu(rs.getString("MaVatTu"));
            ct.setDonViGoc(rs.getString("DonViGoc"));
            ct.setDonViChuyenDoi(rs.getString("DonViChuyenDoi"));
            ct.setSoLuongXuat(rs.getInt("SoLuongXuat"));
            ct.setSoLuongQuyDoi(rs.getInt("SoLuongQuyDoi"));
            list.add(ct);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return list;
}

    // Phương thức để xóa chi tiết phiếu xuất
    public void delete(String maPhieuXuat, String maVatTu) {
        String sql = "DELETE FROM CHITIETPHIEUXUAT WHERE MaPhieuXuat = ? AND MaVatTu = ?";
        try {
            JDBCHelper.update(sql, maPhieuXuat, maVatTu);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa chi tiết phiếu xuất: " + e.getMessage());
        }
    }

// Phương thức để cập nhật chi tiết phiếu xuất trong cơ sở dữ liệu
public void update(model_ChiTietPhieuXuat ct) {
    String sql = "UPDATE CHITIETPHIEUXUAT SET MaVatTu = ?, DonViChuyenDoi = ?, DonViGoc = ?, SoLuongXuat = ?, SoLuongQuyDoi = ? WHERE MaPhieuXuat = ? AND MaVatTu = ?";
    
    try {
        // Thực hiện câu lệnh update vào cơ sở dữ liệu
        JDBCHelper.update(sql,
            ct.getMaVatTu(),               // Mã Vật Tư mới
            ct.getDonViChuyenDoi(),         // Đơn Vị Chuyển Đổi
            ct.getDonViGoc(),               // Đơn Vị Gốc
            ct.getSoLuongXuat(),           // Số Lượng Xuất
            ct.getSoLuongQuyDoi(),         // Số Lượng Quy Đổi
            ct.getMaPhieuXuat(),           // Mã Phiếu Xuất
            ct.getMaVatTu()                // Mã Vật Tư cũ
        );
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi sửa chi tiết phiếu xuất: " + e.getMessage());
    }
}
public List<model_ChiTietPhieuXuat> selectBySql(String sql, String... args) {
    List<model_ChiTietPhieuXuat> list = new ArrayList<>();
    try {
        ResultSet rs = JDBCHelper.query(sql, args);  // Thực hiện câu truy vấn
        while (rs.next()) {
            model_ChiTietPhieuXuat ct = new model_ChiTietPhieuXuat();
            ct.setMaPhieuXuat(rs.getString("MaPhieuXuat"));
            ct.setMaVatTu(rs.getString("MaVatTu"));
            ct.setDonViGoc(rs.getString("DonViGoc"));
            ct.setDonViChuyenDoi(rs.getString("DonViChuyenDoi"));
            ct.setSoLuongXuat(rs.getFloat("SoLuongXuat"));
            ct.setSoLuongQuyDoi(rs.getFloat("SoLuongQuyDoi"));
            list.add(ct);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}


    
}
