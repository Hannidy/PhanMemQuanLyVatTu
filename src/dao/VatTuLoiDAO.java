
package dao;

import entity.model_VatTuLoi;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;
import java.sql.ResultSet;

public class VatTuLoiDAO {

public List<model_VatTuLoi> selectAll() {
    String sql = """
        SELECT 
            k.MaKho, 
            vt.MaVatTu, 
            ncc.MaNhaCungCap,  -- Thêm MaNhaCungCap
            ncc.TenNhaCungCap AS NhaCungCap,
            N'Hàng lỗi' as TrangThai
        FROM VATTU vt
        JOIN CHITIETPHIEUNHAP ctpn ON vt.MaVatTu = ctpn.MaVatTu
        JOIN PHIEUNHAP pn ON ctpn.MaPhieuNhap = pn.MaPhieuNhap
        JOIN NHACUNGCAP ncc ON pn.MaNhaCungCap = ncc.MaNhaCungCap
        JOIN KHO k ON k.MaKho = k.MaKho
        GROUP BY 
            k.MaKho, vt.MaVatTu, ncc.MaNhaCungCap, ncc.TenNhaCungCap
    """;
    List<model_VatTuLoi> list = new ArrayList<>();
    try (ResultSet rs = JDBCHelper.query(sql)) {
        while (rs.next()) {
            model_VatTuLoi item = new model_VatTuLoi();
            item.setMaKho(rs.getString("MaKho"));
            item.setMaVatTu(rs.getString("MaVatTu"));
            item.setMaNhaCungCap(rs.getString("MaNhaCungCap"));  // Thêm MaNhaCungCap
            item.setNhaCungCap(rs.getString("NhaCungCap"));
            item.setTrangThai(rs.getString("TrangThai"));
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

public void delete(String maVatTu) throws Exception {
    String sql = "DELETE FROM VATTU WHERE MaVatTu = ?"; // Giả sử bảng VATTU chứa vật tư lỗi
    JDBCHelper.update(sql, maVatTu);
}

 
}
