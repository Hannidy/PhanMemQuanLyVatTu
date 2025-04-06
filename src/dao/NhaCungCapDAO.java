
package dao;

import entity.model_NhaCungCap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBCHelper;

public class NhaCungCapDAO {
     public void insert(model_NhaCungCap ncc) {
        String sql = "INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?)";
        String newmaNCC = this.selectMaxId();

        JDBCHelper.update(sql,
                newmaNCC,
                ncc.getTennhacungCap(),
                ncc.getSodienThoai(),
                ncc.getEmail(),
                ncc.getDiaChi());
    }

    public void update(model_NhaCungCap ncc) {
        String sql = "UPDATE NHACUNGCAP SET TenNhaCungCap = ?, SoDienThoai = ?, Email = ?, DiaChi = ? WHERE MaNhaCungCap = ?";
        JDBCHelper.update(sql,
                ncc.getTennhacungCap(),
                ncc.getSodienThoai(),
                ncc.getEmail(),
                ncc.getDiaChi(),
                ncc.getManhacungCap());

    }

    public void delete(String maNhaCungCap) {
        String sql = "DELETE FROM NHACUNGCAP WHERE MaNhaCungCap = ?";
        JDBCHelper.update(sql, maNhaCungCap);
    }

    public String selectMaxId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(MaNhaCungCap, 4, LEN(MaNhaCungCap)-3) AS INT)) FROM NHACUNGCAP";
        String newMaNhaCungCap = "NCC01";// Mặc định nếu bảng rỗng.
        try {
            ResultSet rs = JDBCHelper.query(sql);;
            if (rs == null) {
                System.out.println("⚠ Không thể lấy dữ liệu: ResultSet trả về null.");
                return newMaNhaCungCap;
            }
            if (rs.next() && rs.getObject(1) != null) {
                int maxMaNhaCungCap = rs.getInt(1);
                newMaNhaCungCap = "NCC" + (maxMaNhaCungCap + 1); // Tạo NCC tiếp theo
            }
            if (rs != null && rs.getObject(1) != null) {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newMaNhaCungCap;
    }

    public List<model_NhaCungCap> selectById(String keyWord) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE MaNhaCungCap LIKE ? OR TenNhaCungCap LIKE ?";
        String keyWord_2 = "%" + keyWord + "%";
        return this.selectBySQL(sql, keyWord_2, keyWord_2);
    }

    public List<model_NhaCungCap> selectByKeyWord(String keyWord, String columnKho) {
        String sql1 = "SELECT * FROM NHACUNGCAP WHERE MaNhaCungCao LIKE ?";
        String sql2 = "SELECT * FROM NHACUNGCAP WHERE TenNhaCungCap LIKE ?";
        String sql3 = "SELECT * FROM NHACUNGCAP WHERE Email LIKE ?";
        String sql4 = "SELECT * FROM NHACUNGCAP WHERE DiaChi LIKE ?";

        String keyWord_2 = "%" + keyWord + "%";
//        if(){
        return this.selectBySQL(sql1, keyWord, keyWord_2);
//        }
    }

    public List<model_NhaCungCap> selectAll() {
        String sql = "SELECt * FROM NHACUNGCAP";
        return this.selectBySQL(sql);
    }

    protected List<model_NhaCungCap> selectBySQL(String sql, Object... args) {
        List<model_NhaCungCap> list_NhaCungCap = new ArrayList<model_NhaCungCap>();
        try {
            ResultSet rs = null;

            try {
                rs = JDBCHelper.query(sql, args);
                while (rs.next()) {
                    model_NhaCungCap ncc = new model_NhaCungCap();
                    ncc.setManhacungCap(rs.getString("MaNhaCungCap"));
                    ncc.setTennhacungCap(rs.getString("TenNhaCungCap"));
                    ncc.setSodienThoai(rs.getString("SoDienThoai"));
                    ncc.setEmail(rs.getString("Email"));
                    ncc.setDiaChi(rs.getString("DiaChi"));

                    list_NhaCungCap.add(ncc);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list_NhaCungCap;
    }
    
    
    public boolean isTenNhaCungCapExist(String ten) {  // Hàm kiểm tra tên loai vat tu có tồn tại hay chưa
        String sql = "SELECT COUNT(*) FROM NhaCungCap WHERE TenNhaCungCap = ?";
        try (ResultSet rs = JDBCHelper.query(sql, ten)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }
}
