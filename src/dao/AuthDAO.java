
package dao;

import entity.model_TaiKhoan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import raven.toast.Notifications;
import util.JDBCHelper;

public class AuthDAO {
        public static boolean kiemTraTaiKhoan(String taiKhoan, String matKhauMaHoa){
        String sql = "SELECT MatKhau, TrangThai FROM TAIKHOAN WHERE TaiKhoan=?";

        try{
            
        ResultSet rs = JDBCHelper.query(sql, taiKhoan);
        
            if (rs.next()) {
                String matKhauCSDL = rs.getString("MatKhau");
                String trangThaiCSDL = rs.getString("TrangThai");

                if (!matKhauCSDL.equals(matKhauMaHoa)) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Tài khoản hoặc mật khẩu không chính xác!");
                    return false;
                } else if (!trangThaiCSDL.equals("Hoạt Động")) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Tài khoản của bạn đã bị khóa!");
                    return false;
                } else {
                    return true;
                }
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tài khoản không tồn tại!");
                return false;
            }

        } catch (SQLException ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Đã xảy ra lỗi kết nối!");
            return false;
        }
        
        }

    public ResultSet layThongTinNguoiDung(String taiKhoan) {
        String sql = """
            SELECT nv.MaNhanVien, nv.TenNhanVien, nv.HinhAnh,
                   cv.MaChucVu, cv.TenChucVu
            FROM TaiKhoan tk
            JOIN NhanVien nv ON tk.MaNhanVien = nv.MaNhanVien
            JOIN ChucVu cv ON nv.MaChucVu = cv.MaChucVu
            WHERE tk.TaiKhoan = ?
        """;
        return JDBCHelper.query(sql, taiKhoan);
    }

    // Lấy quyền truy cập các form (QuanLy) theo MaChucVu
    public Map<String, Map<String, Boolean>> layQuyenHan(String maChucVu) {
        
        String sql = "SELECT QuanLy, Xem, XuatExcel, Them, Sua, Xoa FROM QUYENHAN WHERE MaChucVu = ?";
        Map<String, Map<String, Boolean>> quyenHan = new HashMap<>();
        try (ResultSet rs = JDBCHelper.query(sql, maChucVu)) {
            while (rs.next()) {
                String form = rs.getString("QuanLy");
                Map<String, Boolean> actions = new HashMap<>();
                actions.put("Xem", rs.getBoolean("Xem"));
                actions.put("XuatExcel", rs.getBoolean("XuatExcel"));
                actions.put("Them", rs.getBoolean("Them"));
                actions.put("Sua", rs.getBoolean("Sua"));
                actions.put("Xoa", rs.getBoolean("Xoa"));
                quyenHan.put(form, actions);
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, e.getMessage());
        }
        return quyenHan;
    }
    }
