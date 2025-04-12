
package dao;

import entity.model_QuyenHan;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class QuyenHanDAO {
    
    public void insert(model_QuyenHan qh) {
    String sql = "INSERT INTO QUYENHAN (MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa) VALUES (?, ? , ? , ? , ? , ? , ?)";
    String newQuyenhan  = this.selectMaxId(); // Lấy mã vật tư mới
    JDBCHelper.update(sql,
            newQuyenhan,
            qh.getMachucvu(),
            qh.getQuanLy(),
            qh.getXem(),
            qh.getXuatexcel(),
            qh.getThem(),
            qh.getXoa(),
            qh.getSua()
    ); 
}

    public void update(model_QuyenHan qh) {
String sql = "UPDATE QUYENHAN SET QuanLy = ?, Xem = ?, XuatExcel = ?, Them = ?, Sua = ?, Xoa = ? WHERE MaChucVu = ?";
JDBCHelper.update(sql,
        qh.getQuanLy(),
        qh.getXem(),
        qh.getXuatexcel(),
        qh.getThem(),
        qh.getSua(),
        qh.getXoa(),
        qh.getMachucvu());
}

    public void delete(String maCV) {
        String sql = "DELETE FROM ChuCVu WHERE MaChucVu = ?";
        JDBCHelper.update(sql, maCV);
    }
    
    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaChucVu, 3, LEN(MaChucVu)-2) AS INT)) FROM QUYENHAN";
        String newMaChucVu = "CV01"; // Mặc định nếu bảng rỗng.

        try (java.sql.ResultSet rs = JDBCHelper.query(sql)) {
            if (rs != null && rs.next() && rs.getObject(1) != null) {
                int maxMaChucVu = rs.getInt(1);
                newMaChucVu = "CV" + (maxMaChucVu + 1); // Tạo mã chức vụ tiếp theo
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return newMaChucVu;
    }
    
    public List<model_QuyenHan> selectById(String maChucVu) {
        String sql = "SELECT * FROM QUYENHAN WHERE MaChucVu = ?";
        return selectBySQL(sql, maChucVu);
    }
    
    public List<model_QuyenHan> selectAll() {
        String sql = "SELECt * FROM QUYENHAN";
        return this.selectBySQL(sql);
    }

    protected List<model_QuyenHan> selectBySQL(String sql, Object... args) {
        List<model_QuyenHan> list_Quyenhan = new ArrayList<>();
        java.sql.ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_QuyenHan qh = new model_QuyenHan();
                qh.setMachucvu(rs.getString("MaChucVu")); 
                qh.setQuanLy(rs.getString("QuanLy"));
                qh.setXem(rs.getInt("Xem"));
                qh.setXuatexcel(rs.getInt("XuatExcel"));
                qh.setThem(rs.getInt("Them"));
                qh.setSua(rs.getInt("Sua"));
                qh.setXoa(rs.getInt("Xoa"));

                list_Quyenhan.add(qh);
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
        return list_Quyenhan;
    }
    
    public List<model_QuyenHan> selectByKeyWord(String keyWord, String columnQuyenHan) {
    String sql;
    String keyWord_2 = "%" + keyWord + "%";

    switch (columnQuyenHan) {
        case "Mã Chức Vụ":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE MaChucVu LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Quản Lý":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE QuanLy LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Xem":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE Xem LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Xuất Excel":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE XuatExcel LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Thêm":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE Them LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Sửa":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE Sua LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        case "Xóa":
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE Xoa LIKE ?";
            return this.selectBySQL(sql, keyWord_2);
        default:
            // Nếu không chỉ định cột cụ thể, tìm trên tất cả các cột
            sql = "SELECT MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE "
                    + "MaChucVu LIKE ? OR "
                    + "QuanLy LIKE ? OR "
                    + "Xem LIKE ? OR "
                    + "XuatExcel LIKE ? OR "
                    + "Them LIKE ? OR "
                    + "Sua LIKE ? OR "
                    + "Xoa LIKE ?";
            return this.selectBySQL(sql, keyWord_2, keyWord_2, keyWord_2, keyWord_2, keyWord_2, keyWord_2, keyWord_2);
    }
}
    

    
}
