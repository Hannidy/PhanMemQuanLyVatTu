/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.model_PhongBan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

/**
 *
 * @author RubyNgoc
 */
public class PhongBanDAO {
    public void insert(model_PhongBan pb) {
        String sql = "INSERT INTO PHONGBAN (MaPhongBan, TenPhongBan, DiaChi, MaTruongPhong) VALUES (?, ?, ?, ?)";
        String newMaPhongBan = this.selectMaxId();
        JDBCHelper.update(sql,
                newMaPhongBan,
                pb.getTenphongBan(),
                pb.getDiaChi(),
                pb.getMatruongPhong());
                pb.getMatruongPhong();
    }

    public void update(model_PhongBan pb) {
        String sql = "UPDATE PHONGBAN SET TenPhongBan = ?, DiaChi = ?, MaTruongPhong = ? WHERE MaPhongBan = ?";
        JDBCHelper.update(sql,
                pb.getTenphongBan(),
                pb.getDiaChi(),
                pb.getMatruongPhong(),
                pb.getMaphongBan());

    }

    public void delete(String maPhongBan) {
        String sql = "DELETE FROM PHONGBAN WHERE MaPhongBan = ?";
        JDBCHelper.update(sql, maPhongBan);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaPhongBan, 3, LEN(MaPhongBan)-2) AS INT)) FROM PHONGBAN";
        String newMaPhongBan = "PB01";// Mặc định nếu bảng rỗng.
        try {
            ResultSet rs = JDBCHelper.query(sql);;
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaPhongBan;
            }
            if (rs.next() && rs.getObject(1) != null) {
                int maxMaPhongBan = rs.getInt(1);
                newMaPhongBan = "PB" + (maxMaPhongBan + 1); // Tạo mã phòng ban tiếp theo
            }
            if (rs != null && rs.getObject(1) != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newMaPhongBan;
    }

    public List<model_PhongBan> selectById(String maPhongBan) {
        String sql = "SELECT * FROM PHONGBAN WHERE MaPhongBan = ?";
        return this.selectBySQL(sql, maPhongBan);
    }

        public List<model_PhongBan>selectByKeyWord(String keyWord, String columnPhongBan){
        String sql1 = "SELECT * FROM PHONGBAN WHERE MaPhongBan LIKE ?";
        String sql2 = "SELECT * FROM PHONGBAN WHERE TenPhongBan LIKE ?";
        String sql3 = "SELECT * FROM PHONGBAN WHERE DiaChi LIKE ?";
        String sql4 = "SELECT * FROM PHONGBAN WHERE MaTruongPhong LIKE ?";
        
        String keyWord_2 = "%" + keyWord + "%";
//        if(){
        return this.selectBySQL(sql1, keyWord, keyWord_2);
//        }
    }
    
    public List<model_PhongBan> selectAll() {
        String sql = "SELECt * FROM PHONGBAN";
        return this.selectBySQL(sql);
    }

    protected List<model_PhongBan> selectBySQL(String sql, Object... args) {
        List<model_PhongBan> list_PhongBan = new ArrayList<model_PhongBan>();
        try {
            ResultSet rs = null;

            try {
                rs = JDBCHelper.query(sql, args);
                while (rs.next()) {
                    model_PhongBan pb = new model_PhongBan();
                    pb.setMaphongBan(rs.getString("MaPhongBan"));
                    pb.setTenphongBan(rs.getString("TenPhongBan"));
                    pb.setDiaChi(rs.getString("DiaChi"));
                    pb.setMatruongPhong(rs.getString("MaTruongPhong"));
                    list_PhongBan.add(pb);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list_PhongBan;
    }
    
    public boolean isTenLoaiVatTuExist(String ten) {  // Hàm kiểm tra tên loai vat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM PhongBan WHERE TenPhongBan = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
}
