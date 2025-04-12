
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

    public model_PhieuNhap selectById(String maPhieuNhap) {
        String sql = "SELECT * FROM PhieuNhap WHERE MaPhieuNhap = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maPhieuNhap)) {
            if (rs.next()) {
                model_PhieuNhap pn = new model_PhieuNhap();
                pn.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
                pn.setNgayNhap(rs.getDate("NgayNhap"));
                pn.setMaKho(rs.getString("MaKho"));
                pn.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                pn.setTrangThai(rs.getString("TrangThai"));
                return pn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
