package dao;

import entity.model_KhoLoaiVatTu;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

    public class Kho_LoaiVatTuDAO {

    public void insert(model_KhoLoaiVatTu klt) {
    String sql = "INSERT INTO KHO_LOAIVATTU (MaKho, MaLoaiVatTu) VALUES (?, ?)";
    JDBCHelper.update(sql, klt.getMaKho(), klt.getMaLoaiVatTu());
    }

    public void update(model_KhoLoaiVatTu klt, String originalMaKho, String originalMaLoaiVatTu) {
    String sql = "UPDATE KHO_LOAIVATTU SET MaKho = ?, MaLoaiVatTu = ? WHERE MaKho = ? AND MaLoaiVatTu = ?";
    JDBCHelper.update(sql,
    klt.getMaKho(),
    klt.getMaLoaiVatTu(),
    originalMaKho,
    originalMaLoaiVatTu
    );
    }

    public void delete(String maKho, String maLoaiVatTu) {
    String sql = "DELETE FROM KHO_LOAIVATTU WHERE MaKho = ? AND MaLoaiVatTu = ?";
    JDBCHelper.update(sql, maKho, maLoaiVatTu);
    }

    public List<model_KhoLoaiVatTu> selectAll() {
    String sql = "SELECT * FROM KHO_LOAIVATTU";
    return selectBySQL(sql);
    }

    public model_KhoLoaiVatTu selectById(String maKho, String maLoaiVatTu) {
    String sql = "SELECT * FROM KHO_LOAIVATTU WHERE MaKho = ? AND MaLoaiVatTu = ?";
    List<model_KhoLoaiVatTu> list = selectBySQL(sql, maKho, maLoaiVatTu);
    return list.isEmpty() ? null : list.get(0);
    }

    public List<model_KhoLoaiVatTu> selectByKeyWord(String keyWord, String column) {
    String sql;
    String keyWord_2 = "%" + keyWord + "%";

    switch (column) {
    case "Mã Kho":
    sql = "SELECT MaKho, MaLoaiVatTu FROM KHO_LOAIVATTU WHERE MaKho LIKE ?";
    return selectBySQL(sql, keyWord_2);
    case "Mã Loại Vật Tư":
    sql = "SELECT MaKho, MaLoaiVatTu FROM KHO_LOAIVATTU WHERE MaLoaiVatTu LIKE ?";
    return selectBySQL(sql, keyWord_2);
    default:
    // Nếu không chỉ định cột cụ thể, tìm trên cả hai cột
    sql = "SELECT MaKho, MaLoaiVatTu FROM KHO_LOAIVATTU WHERE MaKho LIKE ? OR MaLoaiVatTu LIKE ?";
    return selectBySQL(sql, keyWord_2, keyWord_2);
    }
    }

    public boolean isExist(String maKho, String maLoaiVatTu) {
    String sql = "SELECT COUNT(*) FROM KHO_LOAIVATTU WHERE MaKho = ? AND MaLoaiVatTu = ?";
    try (ResultSet rs = JDBCHelper.query(sql, maKho, maLoaiVatTu)) {
    if (rs.next()) {
    return rs.getInt(1) > 0;
    }
    } catch (SQLException e) {
    throw new RuntimeException("Lỗi kiểm tra sự tồn tại: " + e.getMessage());
    }
    return false;
    }

    protected List<model_KhoLoaiVatTu> selectBySQL(String sql, Object... args) {
    List<model_KhoLoaiVatTu> list_KhoLoaiVatTu = new ArrayList<>();
    ResultSet rs = null;

    try {
    rs = JDBCHelper.query(sql, args);
    while (rs.next()) {
    model_KhoLoaiVatTu klt = new model_KhoLoaiVatTu();
    klt.setMaKho(rs.getString("MaKho"));
    klt.setMaLoaiVatTu(rs.getString("MaLoaiVatTu"));
    list_KhoLoaiVatTu.add(klt);
    }
    } catch (SQLException e) {
    throw new RuntimeException("Lỗi truy vấn dữ liệu: " + e.getMessage());
    } finally {
    try {
    if (rs != null) {
    rs.getStatement().close();
    rs.close();
    }
    } catch (SQLException e) {
    // Bỏ qua lỗi đóng ResultSet
    }
    }
    return list_KhoLoaiVatTu;
    }
}
