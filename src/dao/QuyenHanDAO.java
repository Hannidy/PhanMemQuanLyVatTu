package dao;

import entity.model_QuyenHan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class QuyenHanDAO {

    public void insert(model_QuyenHan qh) {
        String sql = "INSERT INTO QUYENHAN (MaChucVu, QuanLy, Xem, XuatExcel, Them, Sua, Xoa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        JDBCHelper.update(sql,
            qh.getMachucvu(),
            qh.getQuanLy(),
            qh.getXem(),
            qh.getXuatexcel(),
            qh.getThem(),
            qh.getSua(),
            qh.getXoa()
        );
    }

    public void update(model_QuyenHan qh, String originalMaCV, String originalQuanLy) {
    String sql = "UPDATE QUYENHAN SET MaChucVu = ?, QuanLy = ?, Xem = ?, XuatExcel = ?, Them = ?, Sua = ?, Xoa = ? WHERE MaChucVu = ? AND QuanLy = ?";
    JDBCHelper.update(sql,
        qh.getMachucvu(),
        qh.getQuanLy(),
        qh.getXem(),
        qh.getXuatexcel(),
        qh.getThem(),
        qh.getSua(),
        qh.getXoa(),
        originalMaCV,
        originalQuanLy
    );
}

    public void delete(String maCV, String quanLy) {
        String sql = "DELETE FROM QUYENHAN WHERE MaChucVu = ? AND QuanLy = ?";
        JDBCHelper.update(sql, maCV, quanLy);
    }

    public List<model_QuyenHan> selectAll() {
        String sql = "SELECT * FROM QUYENHAN";
        return selectBySql(sql);
    }

    public model_QuyenHan selectById(String maChucVu, String quanLy) {
        String sql = "SELECT * FROM QUYENHAN WHERE MaChucVu = ? AND QuanLy = ?";
        List<model_QuyenHan> list = selectBySql(sql, maChucVu, quanLy);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<model_QuyenHan> selectByKeyWord(String keyword, String column) {
        String sql = "SELECT * FROM QUYENHAN WHERE " + column + " LIKE ?";
        return selectBySql(sql, "%" + keyword + "%");
    }

    public boolean isExist(String maChucVu, String quanLy) {
        String sql = "SELECT COUNT(*) FROM QUYENHAN WHERE MaChucVu = ? AND QuanLy = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maChucVu, quanLy)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean isReferenced(String maChucVu) {
        String sql = "SELECT COUNT(*) FROM NHANVIEN WHERE MaChucVu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maChucVu)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra khóa ngoại: " + e.getMessage());
        }
        return false;
    }

    private List<model_QuyenHan> selectBySql(String sql, Object... args) {
        List<model_QuyenHan> list = new ArrayList<>();
        try (ResultSet rs = JDBCHelper.query(sql, args)) {
            while (rs.next()) {
                model_QuyenHan qh = new model_QuyenHan();
                qh.setMachucvu(rs.getString("MaChucVu"));
                qh.setQuanLy(rs.getString("QuanLy"));
                qh.setXem(rs.getInt("Xem"));
                qh.setXuatexcel(rs.getInt("XuatExcel"));
                qh.setThem(rs.getInt("Them"));
                qh.setSua(rs.getInt("Sua"));
                qh.setXoa(rs.getInt("Xoa"));
                list.add(qh);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    
   
    
}
