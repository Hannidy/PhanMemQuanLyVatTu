
package dao;

import entity.model_VatTuLoi;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VatTuLoiDAO {

public List<model_VatTuLoi> selectAll() {
    String sql = """
    SELECT 
        k.MaKho, 
        vt.MaVatTu, 
        ncc.MaNhaCungCap,  
        ncc.TenNhaCungCap AS NhaCungCap,
        bh.TrangThai
    FROM VATTU vt
    JOIN CHITIETPHIEUNHAP ctpn ON vt.MaVatTu = ctpn.MaVatTu
    JOIN PHIEUNHAP pn ON ctpn.MaPhieuNhap = pn.MaPhieuNhap
    JOIN NHACUNGCAP ncc ON pn.MaNhaCungCap = ncc.MaNhaCungCap
    JOIN KHO k ON pn.MaKho = k.MaKho -- sửa join đúng
    LEFT JOIN BAOHANH bh ON bh.MaVatTu = vt.MaVatTu
    GROUP BY 
        k.MaKho, vt.MaVatTu, ncc.MaNhaCungCap, ncc.TenNhaCungCap, bh.TrangThai
""";

List<model_VatTuLoi> list = new ArrayList<>();
try (ResultSet rs = JDBCHelper.query(sql)) {
    while (rs.next()) {
        model_VatTuLoi item = new model_VatTuLoi();
        item.setMaKho(rs.getString("MaKho"));
        item.setMaVatTu(rs.getString("MaVatTu"));
        item.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
        item.setNhaCungCap(rs.getString("NhaCungCap"));
        
        // Lấy giá trị trạng thái từ bảng BAOHANH
        String trangThai = rs.getString("TrangThai");
        if (trangThai == null || trangThai.trim().isEmpty()) {
            trangThai = "Hàng lỗi"; // hoặc để null nếu bạn muốn
        }
        item.setTrangThai(trangThai);

        list.add(item);
    }
} catch (Exception e) {
    e.printStackTrace();
}
return list;
}

public List<Object[]> getVatTuHangLoi() {
    List<Object[]> list = new ArrayList<>();
    String sql = """
        SELECT 
            k.MaKho, 
            vt.MaVatTu, 
            vt.TenVatTu AS TenVatTu,
            N'Đang chờ duyện' AS TrangThai
        FROM 
            VatTu vt
        JOIN 
            ChiTietPhieuNhap ctpn ON vt.MaVatTu = ctpn.MaVatTu
        JOIN 
            PhieuNhap pn ON ctpn.MaPhieuNhap = pn.MaPhieuNhap
        JOIN 
            Kho k ON pn.MaKho = k.MaKho
        GROUP BY 
            k.MaKho, vt.MaVatTu, vt.TenVatTu
    """;
    try (ResultSet rs = JDBCHelper.query(sql)) {
        while (rs.next()) {
            Object[] row = {
                rs.getString("MaKho"),
                rs.getString("MaVatTu"),
                rs.getString("TenVatTu"),
                rs.getString("TrangThai")
            };
            list.add(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    public void updateTrangThai(String maVatTu, String trangThai) throws SQLException {
        String sql = "UPDATE BaoHanh SET trangThai = ? WHERE maVatTu = ?";
        JDBCHelper.update(sql, trangThai, maVatTu);
       String updatedTrangThai = getTrangThai(maVatTu);
        if (updatedTrangThai == null) {
            throw new SQLException("Không tìm thấy bản ghi bảo hành cho vật tư: " + maVatTu);
        }
    }

    // Lấy trạng thái từ bảng BaoHanh
    public String getTrangThai(String maVatTu) throws SQLException {
        String sql = "SELECT trangThai FROM BaoHanh WHERE maVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maVatTu)) {
            if (rs.next()) {
                return rs.getString("trangThai");
            }
            return null; // Trả về null nếu không tìm thấy
        }
    }
  public boolean exists(String maVatTu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM BaoHanh WHERE maVatTu = ?";
        try (ResultSet rs = JDBCHelper.query(sql, maVatTu)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    // Thêm một bản ghi mới vào bảng BaoHanh
    public void insert(String maVatTu, String maNhaCungCap, String trangThai) throws SQLException {
        String sql = "INSERT INTO BaoHanh (maVatTu, maNhaCungCap, trangThai) VALUES ( ?, ?, ?)";
        JDBCHelper.update(sql, maVatTu, maNhaCungCap, trangThai);
    }
//    public void updateTrangThai(String maVatTu, String trangThai) throws SQLException {
//    String sql = "UPDATE BAOHANH SET TrangThai = ? WHERE MaVatTu = ?";
//    JDBCHelper.update(sql, trangThai, maVatTu);
//}
//    
}
