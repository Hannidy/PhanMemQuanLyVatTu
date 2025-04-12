
package util;

import dao.AuthDAO;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import raven.toast.Notifications;

public class Auth {
    
    public static final AuthDAO authdao = new AuthDAO();
    
    // Thông tin chung của người dùng hiện tại:
    public static String maNhanVien;
    public static String tenNhanVien;
    public static String hinhAnh;
    public static String maChucVu;
    public static String tenChucVu;
    public static String email;
    public static String soDienThoai;
    public static String tenPhongBan;

    
    // Danh sách quyền: QuanLy -> (Xem, Them, Sua, Xoa, XuatExcel)
    private static Map<String, Map<String, Boolean>> quyenHan = new HashMap<>();
    
    // Xác thực tài khoản
    public static boolean xacThucDangNhap(String taiKhoan, String matKhau) {
        try {
            String matKhauMaHoa = MaHoaMD5.MD5encoder(matKhau);
            boolean hopLe = authdao.kiemTraTaiKhoan(taiKhoan, matKhauMaHoa);
            if (hopLe) {
                ganThongtinNguoiDung(taiKhoan);
                if(maChucVu == "NV01"){
                    quyenHan = authdao.layQuyenHan(maChucVu);
                }else{
                    quyenHan = authdao.layQuyenHan(maChucVu);
                }
            }
            return hopLe;
        } catch (NoSuchAlgorithmException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi mã hóa mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    
    // Gán thông tin người dùng sau khi đăng nhập
    public static void ganThongtinNguoiDung(String taiKhoan) {
    try {
        ResultSet rs = authdao.layThongTinNguoiDung(taiKhoan); // SELECT như ở trên
        if (rs.next()) {
            maNhanVien = rs.getString("MaNhanVien");
            tenNhanVien = rs.getString("TenNhanVien");
            email = rs.getString("Email");
            soDienThoai = rs.getString("SoDienThoai");
            hinhAnh = rs.getString("HinhAnh");
            maChucVu = rs.getString("MaChucVu");
            tenChucVu = rs.getString("TenChucVu");
            tenPhongBan = rs.getString("TenPhongBan");
        }
    } catch (Exception e) {
        Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi lấy thông tin người dùng: " + e.getMessage());
    }
}



    // Đăng xuất
    public static void dangXuat() {
        maNhanVien = null;
        tenNhanVien = null;
        hinhAnh = null;
        maChucVu = null;
        tenChucVu = null;
        quyenHan.clear();
    }

    // Kiểm tra trạng thái đăng nhập
    public static boolean daDangNhap() {
        return maNhanVien != null;
    }

    // Kiểm tra quyền truy cập quản lý và chức năng:
    public static boolean coThe(String quanLy, String chucNang) {
        return quyenHan.getOrDefault(quanLy, Collections.emptyMap()).getOrDefault(chucNang, false);
    }

    // Getter
    public static String getMaNhanVien() {
        return maNhanVien;
    }

    public static String getTenNhanVien() {
        return tenNhanVien != null ? tenNhanVien : "";
    }

    public static String getHinhAnh() {
        return hinhAnh;
    }

    public static String getMaChucVu() {
        return maChucVu;
    }

    public static String getTenChucVu() {
        return tenChucVu;
    }
    public static String getEmail() {
    return email != null ? email : "";
    }

    public static String getSoDienThoai() {
        return soDienThoai != null ? soDienThoai : "";
    }

    public static String getTenPhongBan() {
        return tenPhongBan != null ? tenPhongBan : "";
    }



}
