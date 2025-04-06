
package dao;

import entity.model_TonKho;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class TonKhoDAO {
    public void insert(model_TonKho tk) {
        String sql = "INSERT INTO TonKho (MaKho, MaVatTu, SoLuong, DonVi, TonToiThieu, TonToiDa, ViTri) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String newMaKho = this.selectMaxId(); // Lấy mã vật tư mới
        JDBCHelper.update(sql,
                newMaKho,
                tk.getMaKho(),
                tk.getMavatTu(),
                tk.getSoLuong(),
                tk.getDonVi(),
                tk.getTontoiThieu(),
                tk.getTontoiDa(),
                tk.getViTri());
    }

    public void update(model_TonKho tk) {
        String sql = "UPDATE TonKho SET MaVatTu = ?, SoLuong = ?, DonVi = ?, TonToiThieu = ?, TonToiDa = ?, ViTri = ? WHERE MaKho = ?";
        JDBCHelper.update(sql,
                tk.getSoLuong(),
                tk.getDonVi(),
                tk.getTontoiThieu(),
                tk.getTontoiDa(),
                tk.getViTri(),
                tk.getMaKho(),
                tk.getMavatTu());
    }

    public void delete(String maNhanVien) {
        String sql = "DELETE FROM TonKho WHERE MaKho = ?";
        JDBCHelper.update(sql, maNhanVien);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaKho, 2, LEN(MaKho)-1) AS INT)) FROM TonKho";
        String newMaKho = "K01";// Mặc định nếu bảng rỗng.
        try {
            ResultSet rs = JDBCHelper.query(sql);
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaKho;
            }
            if (rs.next() && rs.getObject(1) != null) {
                int maxMaKho = rs.getInt(1);
                newMaKho = "K" + (maxMaKho + 1); // Tạo NV tiếp theo
            }
            if (rs != null && rs.getObject(1) != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newMaKho;
    }

    public List<model_TonKho> selectById(String maVatTu) {
        String sql = "SELECT * FROM TonKho WHERE MaKho = ?";
        return selectBySQL(sql, maVatTu);
    }

//    public List<model_TonKho> selectByKeyWord(String keyWord, String columnVatTu) {
//        String sql;
//        String keyWord_2 = "%" + keyWord + "%";
//
//        switch (columnVatTu) {
//            case "Mã Vật Tư":
//                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE MaVatTu LIKE ?";
//                return this.selectBySQL(sql, keyWord_2);
//            case "Tên Vật Tư":
//                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE TenVatTu LIKE ?";
//                return this.selectBySQL(sql, keyWord_2);
//            case "Mã Loại Vật Tư":
//                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE MaLoaiVatTu LIKE ?";
//                return this.selectBySQL(sql, keyWord_2);
//            default:
//                // Nếu không chỉ định cột cụ thể, tìm trên tất cả 3 cột
//                sql = "SELECT MaVatTu, TenVatTu, MaLoaiVatTu FROM VATTU WHERE "
//                        + "MaVatTu LIKE ? OR "
//                        + "TenVatTu LIKE ? OR "
//                        + "MaLoaiVatTu LIKE ?";
//                return this.selectBySQL(sql, keyWord_2, keyWord_2, keyWord_2);
//        }
//    }

    public List<model_TonKho> selectAll() {
        String sql = "SELECt * FROM TonKho";
        return this.selectBySQL(sql);
    }

    protected List<model_TonKho> selectBySQL(String sql, Object... args) {
        List<model_TonKho> list_TonKho = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = JDBCHelper.query(sql, args);
            while (rs.next()) {
                model_TonKho tk = new model_TonKho();
                tk.setMaKho(rs.getString("MaKho"));
                tk.setMavatTu(rs.getString("MaVatTu")); // Lấy Mã Vật Tư
                tk.setSoLuong(rs.getString("SoLuong"));
                tk.setDonVi(rs.getString("DonVi"));
                tk.setTontoiThieu(rs.getString("TonToiThieu"));
                tk.setTontoiDa(rs.getString("TonToiDa"));
                tk.setViTri(rs.getString("ViTri"));
                list_TonKho.add(tk);
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
        return list_TonKho;
    }

//    public boolean isTenVatTuExist(String ten) {  // Hàm kiểm tra tênvat tu có tồn tại hay chưa
//        String sql = "SELECT COUNT(*) FROM TonKho WHERE MaKho = ?";
//        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        } catch (SQLException e) {
//        }
//        return false;
//    }
}
