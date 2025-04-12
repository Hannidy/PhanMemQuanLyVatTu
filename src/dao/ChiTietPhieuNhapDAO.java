package dao;

import entity.model_ChiTietPhieuNhap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class ChiTietPhieuNhapDAO {

    public void insert(model_ChiTietPhieuNhap ct) {
        String sql = "INSERT INTO CHITIETPHIEUNHAP "
                   + "(MaPhieuNhap, MaVatTu, DonViChuyenDoi, DonViGoc, SoLuongNhap, SoLuongQuyDoi) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        JDBCHelper.update(sql,
            ct.getMaPhieuNhap(),
            ct.getMaVatTu(),
            ct.getDonViChuyenDoi(),
            ct.getDonViGoc(),
            ct.getSoLuongNhap(),
            ct.getSoLuongQuyDoi()
        );
    }
public List<model_ChiTietPhieuNhap> selectByMaPhieu(String maPhieuNhap) {
    String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE MaPhieuNhap = ?";
    return selectBySql(sql, maPhieuNhap);
}

    public void delete(String maPhieuNhap, String maVatTu) {
        String sql = "DELETE FROM CHITIETPHIEUNHAP WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        JDBCHelper.update(sql, maPhieuNhap, maVatTu);
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

    private List<model_ChiTietPhieuNhap> selectBySql(String sql, String maPhieuNhap) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
