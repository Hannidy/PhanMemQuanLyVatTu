package dao;

import entity.model_ChiTietPhieuNhap;
import entity.model_TonKho;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class ChiTietPhieuNhapDAO {

   
    public void insert(model_ChiTietPhieuNhap ct) {
    String sql = "INSERT INTO CHITIETPHIEUNHAP (MaPhieuNhap, MaVatTu, DonViChuyenDoi, DonViGoc, SoLuongNhap, SoLuongQuyDoi) VALUES (?, ?, ?, ?, ?, ?)";
    String checkSql = "SELECT COUNT(*) FROM PHIEUNHAP WHERE MaPhieuNhap = ?";
    try {
        // Kiểm tra xem MaPhieuNhap có tồn tại không
        ResultSet rs = JDBCHelper.query(checkSql, ct.getMaPhieuNhap());
        rs.next();
        if (rs.getInt(1) == 0) {
            throw new RuntimeException("Mã phiếu nhập " + ct.getMaPhieuNhap() + " không tồn tại trong bảng PHIEUNHAP!");
        }

        // Thực hiện insert nếu MaPhieuNhap hợp lệ
        JDBCHelper.update(sql,
            ct.getMaPhieuNhap(),
            ct.getMaVatTu(),
            ct.getDonViChuyenDoi(),
            ct.getDonViGoc(),
            ct.getSoLuongNhap(),
            ct.getSoLuongQuyDoi()
        );
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi thêm chi tiết phiếu nhập: " + e.getMessage());
    }
}



   public void insert(model_TonKho tk) {
    String sql = "INSERT INTO TonKho (MaKho, MaVatTu, SoLuong, DonVi ViTri) VALUES ( ?, ?, ?, ?, ?)";
    
    // Đảm bảo rằng bạn đang truyền đúng 7 tham số.
    JDBCHelper.update(sql,
            tk.getMaKho(),
            tk.getMaVatTu(),
            tk.getSoLuong(),
            tk.getDonVi(),
            tk.getViTri());

}
   

 
   





public List<model_ChiTietPhieuNhap> selectByMaPhieu(String maPhieuNhap) {
    String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaPhieuNhap = ?";
    return selectBySQL(sql, maPhieuNhap);
}
public List<model_ChiTietPhieuNhap> selectByMaVatTu(String maVatTu) {
    String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaVatTu = ?";
    return selectBySQL(sql, maVatTu);
}


     // Phương thức để xóa chi tiết phiếu nhập
    public void delete(String maPhieuNhap, String maVatTu) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try {
            JDBCHelper.update(sql, maPhieuNhap, maVatTu);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa chi tiết phiếu nhập: " + e.getMessage());
        }
    }

    // Phương thức để kiểm tra xem phiếu nhập còn chi tiết nào không
    public List<model_ChiTietPhieuNhap> selectByPhieuNhap(String maPhieuNhap) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ?";
        List<model_ChiTietPhieuNhap> resultList = new ArrayList<>();

        try (ResultSet rs = JDBCHelper.query(sql, maPhieuNhap)) {
            // Duyệt qua ResultSet và ánh xạ dữ liệu
            while (rs.next()) {
                model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();

                // Ánh xạ dữ liệu từ ResultSet vào đối tượng model_ChiTietPhieuNhap
                ct.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
                ct.setMaVatTu(rs.getString("MaVatTu"));
                ct.setDonViGoc(rs.getInt("DonViGoc"));
                ct.setDonViChuyenDoi(rs.getInt("DonViChuyenDoi"));
                ct.setSoLuongNhap(rs.getFloat("SoLuongNhap"));
                ct.setSoLuongQuyDoi(rs.getFloat("SoLuongQuyDoi"));

                // Thêm vào danh sách kết quả
                resultList.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi kiểm tra chi tiết phiếu nhập: " + e.getMessage());
        }

        return resultList;
    
    }
     

    public List<model_ChiTietPhieuNhap> selectAll() {
        String sql = "SELECT * FROM CHITIETPHIEUNHAP";
        return selectBySQL(sql);
    }

    public List<model_ChiTietPhieuNhap> selectByMaPhieuNhap(String maPN) {
        String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaPhieuNhap = ?";
        return selectBySQL(sql, maPN);
    }

    private List<model_ChiTietPhieuNhap> selectBySQL(String sql, Object... args) {
        List<model_ChiTietPhieuNhap> list = new ArrayList<>();
        try (ResultSet rs = JDBCHelper.query(sql, args)) {
            while (rs.next()) {
                model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();
                ct.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
                ct.setMaVatTu(rs.getString("MaVatTu"));
                ct.setDonViChuyenDoi(rs.getInt("DonViChuyenDoi"));
                ct.setDonViGoc(rs.getInt("DonViGoc"));
                ct.setSoLuongNhap(rs.getFloat("SoLuongNhap"));
                ct.setSoLuongQuyDoi(rs.getFloat("SoLuongQuyDoi"));
                list.add(ct);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn CHITIETPHIEUNHAP: " + e.getMessage(), e);
        }
        return list;
    }
    public List<model_ChiTietPhieuNhap> selectBySql(String sql, Object... args) {
    List<model_ChiTietPhieuNhap> list = new ArrayList<>();
    try (ResultSet rs = JDBCHelper.query(sql, args)) {
        while (rs.next()) {
            model_ChiTietPhieuNhap ct = new model_ChiTietPhieuNhap();
            ct.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
            ct.setMaVatTu(rs.getString("MaVatTu"));
            ct.setDonViGoc(rs.getInt("DonViGoc"));
            ct.setDonViChuyenDoi(rs.getInt("DonViChuyenDoi"));
            ct.setSoLuongNhap(rs.getFloat("SoLuongNhap"));
            ct.setSoLuongQuyDoi(rs.getFloat("SoLuongQuyDoi"));
            list.add(ct);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi truy vấn chi tiết phiếu nhập: " + e.getMessage());
    }
    return list;
}
// Phương thức cập nhật chi tiết phiếu nhập
public void update(model_ChiTietPhieuNhap ct) {
    String sql = "UPDATE CHITIETPHIEUNHAP SET DonViChuyenDoi = ?, DonViGoc = ?, SoLuongNhap = ?, SoLuongQuyDoi = ? WHERE MaPhieuNhap = ? AND MaVatTu = ?";
    
    try {
        JDBCHelper.update(sql, 
            ct.getDonViChuyenDoi(),
            ct.getDonViGoc(),
            ct.getSoLuongNhap(),
            ct.getSoLuongQuyDoi(),
            ct.getMaPhieuNhap(),
            ct.getMaVatTu()
        );
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi cập nhật chi tiết phiếu nhập: " + e.getMessage());
    }
}
    

    private List<model_ChiTietPhieuNhap> selectBySql(String sql, String maPhieuNhap) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
