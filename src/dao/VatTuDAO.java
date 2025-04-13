/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.model_VatTu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

/**
 *
 * @author doann
 */
public class VatTuDAO {

    public void insert(model_VatTu vt) {
        // Đảm bảo mã đã được gán trước khi insert
        if (vt.getMavatTu() == null) {
            throw new IllegalStateException("Mã vật tư chưa được sinh!");
        }
        String sql = "INSERT INTO VATTU (MaVatTu, TenVatTu, MaLoaiVatTu) VALUES (?, ?, ?)";
        JDBCHelper.update(sql, vt.getMavatTu(), vt.getTenVatTu(), vt.getMaloaivatTu());
    }

    public void update(model_VatTu vt) {
        String sql = "UPDATE VATTU SET TenVatTu = ?, MaLoaiVatTu = ? WHERE MaVatTu = ?";
        JDBCHelper.update(sql,
                vt.getTenVatTu(),
                vt.getMaloaivatTu(), // Thêm tham số còn thiếu
                vt.getMavatTu());     // Chuyển tham số này xuống đúng vị trí
    }

    public void delete(String maVatTu) {
        String sql = "DELETE FROM VATTU WHERE MaVatTu = ?";
        JDBCHelper.update(sql, maVatTu);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaVatTu, 3, LEN(MaVatTu)-2) AS INT)) FROM VATTU WHERE MaVatTu LIKE 'VT[0-9]%'";
        String newMaVatTu = "VT01";
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaVatTu;
            }
            if (rs.next()) {
                int maxMaVatTu = rs.getInt(1);
                if (maxMaVatTu > 0) {
                    newMaVatTu = "VT" + (maxMaVatTu + 1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy mã vật tư mới nhất: " + e.getMessage());
        }
        return newMaVatTu;
    }

    public List<model_VatTu> selectById(String maVatTu) {
        String sql = "SELECT * FROM VATTU WHERE MaVatTu = ?";
        return selectBySQL(sql, maVatTu);
    }

    public List<model_VatTu> selectByKeyWord(String keyWord, String columnVatTu) {
        String sql;
        String keyWord_2 = "%" + keyWord + "%";

        switch (columnVatTu) {
            case "Mã Vật Tư":
                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE MaVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Tên Vật Tư":
                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE TenVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            case "Mã Loại Vật Tư":
                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE MaLoaiVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2);
            default:
                // Nếu không chỉ định cột cụ thể, tìm trên tất cả 3 cột
                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE "
                        + "MaVatTu LIKE ? OR "
                        + "TenVatTu LIKE ? OR "
                        + "MaLoaiVatTu LIKE ?";
                return this.selectBySQL(sql, keyWord_2, keyWord_2, keyWord_2);
        }
    }

    public List<model_VatTu> selectAll() {
        String sql = "SELECt * FROM VATTU";
        return this.selectBySQL(sql);
    }

    protected List<model_VatTu> selectBySQL(String sql, Object... args) {
        List<model_VatTu> list_VatTu = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_VatTu vt = new model_VatTu();
                vt.setMavatTu(rs.getString("MaVatTu")); // Lấy Mã Vật Tư
                vt.setTenVatTu(rs.getString("TenVatTu")); // Lấy Tên Vật Tư
                vt.setMaloaivatTu(rs.getString("MaLoaiVatTu")); // Lấy Mã Loại Vật Tư

                list_VatTu.add(vt);
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
        return list_VatTu;
    }

    public boolean isTenVatTuExist(String ten) {  // Hàm kiểm tra tênvat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM VatTu WHERE TenVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean isMaLoaiDangDuocDungChoVatTu(String maLoai) {  // Hàm check ràng buộc
        String sql = "SELECT COUNT(*) FROM VatTu WHERE MaLoaiVatTu = ?";
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
