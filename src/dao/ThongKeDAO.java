package dao;

import util.JDBCHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {

    public int getSoPhieuNhapTheoThang(int thang, int nam) {
        String sql = "SELECT COUNT(*) FROM PHIEUNHAP WHERE MONTH(NgayNhap) = ? AND YEAR(NgayNhap) = ?";
        ResultSet rs = JDBCHelper.executeQuery(sql, thang, nam);
        try {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoPhieuXuatTheoThang(int thang, int nam) {
        String sql = "SELECT COUNT(*) FROM PHIEUXUAT WHERE MONTH(NgayXuat) = ? AND YEAR(NgayXuat) = ?";
        ResultSet rs = JDBCHelper.executeQuery(sql, thang, nam);
        try {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getSoLuongTonTheoLoaiVatTu() {
        String sql = """
            SELECT lv.TenLoaiVatTu, SUM(tk.SoLuong) AS TongSoLuong
            FROM TONKHO tk
            JOIN VATTU vt ON tk.MaVatTu = vt.MaVatTu
            JOIN LOAIVATTU lv ON vt.MaLoaiVatTu = lv.MaLoaiVatTu
            GROUP BY lv.TenLoaiVatTu
        """;
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(sql);
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getNhapXuatTheoNgay(int thang, int nam) {
        String sql = """
            SELECT Ngay, 
                   SUM(ISNULL(SoPN, 0)) AS TongNhap,
                   SUM(ISNULL(SoPX, 0)) AS TongXuat
            FROM (
                SELECT CAST(NgayNhap AS DATE) AS Ngay, COUNT(*) AS SoPN, NULL AS SoPX
                FROM PHIEUNHAP
                WHERE MONTH(NgayNhap) = ? AND YEAR(NgayNhap) = ?
                GROUP BY CAST(NgayNhap AS DATE)
                
                UNION ALL
                
                SELECT CAST(NgayXuat AS DATE) AS Ngay, NULL, COUNT(*)
                FROM PHIEUXUAT
                WHERE MONTH(NgayXuat) = ? AND YEAR(NgayXuat) = ?
                GROUP BY CAST(NgayXuat AS DATE)
            ) AS combined
            GROUP BY Ngay
            ORDER BY Ngay
        """;
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = JDBCHelper.executeQuery(sql, thang, nam, thang, nam);
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getDate("Ngay"), 
                    rs.getInt("TongNhap"), 
                    rs.getInt("TongXuat")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
