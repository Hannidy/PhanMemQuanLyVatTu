
package dao;

import entity.model_TaiKhoan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import raven.toast.Notifications;
import util.JDBCHelper;

public class TaiKhoanDAO{
    
    public boolean insert(model_TaiKhoan tk) {
        
        if(kiemTraTrungKhoaChinh(tk)){
        
        String sql = "INSERT INTO TAIKHOAN (TaiKhoan, MatKhau, MaNhanVien) VALUES (?, ?, ?)";
        JDBCHelper.update(sql,
            tk.getTaiKhoan(),
            tk.getMatKhau(),
            tk.getMaNhanVien());
        
        return true;
        
        }else{
            return false;
        }
    }
       
    
    public boolean kiemTraTrungKhoaChinh(model_TaiKhoan tk){
        String sql_KiemTraTrungMaNhanVien = "SELECT 1 FROM TAIKHOAN WHERE MaNhanVien = ?";
        ResultSet rs_KiemTraTrungMaNhanVien = JDBCHelper.query(sql_KiemTraTrungMaNhanVien, tk.getMaNhanVien());
        
        String sql_KiemTraTrungTaiKhoan = "SELECT 1 FROM TAIKHOAN WHERE TaiKhoan = ?";
        ResultSet rs_KiemTraTrungTaiKhoan = JDBCHelper.query(sql_KiemTraTrungTaiKhoan, tk.getTaiKhoan());
        
        try {
            if(rs_KiemTraTrungMaNhanVien.next() && rs_KiemTraTrungMaNhanVien.getObject(1) != null){
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã nhân viên đã có tài khoản");
                return false;
            }else if(rs_KiemTraTrungTaiKhoan.next() && rs_KiemTraTrungTaiKhoan.getObject(1) != null){
                Notifications.getInstance().show(Notifications.Type.INFO, "Tài khoản đã tồn tại");
                return false;
            }else{
                return true;
            }
        } catch (SQLException ex) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi");
            return false;
        }
    }
    
    public void update(model_TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET  TrangThai = ? WHERE MaNhanVien = ?";
        JDBCHelper.update(sql, 
                               tk.getTrangThai(),
                               tk.getMaNhanVien());

    }
    
    public void updateByAdmin(model_TaiKhoan tk) {
        String sql = "UPDATE TAIKHOAN SET TaiKhoan = ?, MatKhau = ?, TrangThai = ? WHERE MaNhanVien = ?";
        JDBCHelper.update(sql, tk.getTaiKhoan(),
                               tk.getMatKhau(),
                               tk.getTrangThai(),
                               tk.getMaNhanVien());
    }

    public void delete(String maNhanVien) {
        String sql = "DELETE FROM TAIKHOAN WHERE MaNhanVien = ?";
        JDBCHelper.update(sql, maNhanVien);
    }


    public List<model_TaiKhoan> selectById(String maNhanVien) {
        String sql = "SELECT * FROM TAIKHOAN WHERE MaNhanVien = ?";
        return selectBySQL(sql, maNhanVien);
    }

    public List<model_TaiKhoan>selectByKeyWord(String keyWord, String columnTaiKhoan){
        String sql1 = "SELECT * FROM TAIKHOAN WHERE TaiKhoan LIKE ?";
        String sql2 = "SELECT * FROM TAIKHOAN WHERE MatKhau LIKE ?";
        String sql3 = "SELECT * FROM TAIKHOAN WHERE MaNhanVien LIKE ?";
        String sql4 = "SELECT * FROM TAIKHOAN WHERE TrangThai LIKE ?";
        String sql5 = "SELECT * FROM TAIKHOAN WHERE"
                + " TaiKhoan LIKE ? OR "
                + " MatKhau LIKE ? OR "
                + " MaNhanVien LIKE ? OR "
                + " TrangThai LIKE ? ";
        
        String keyWord_2 = "%" + keyWord + "%";
        if(columnTaiKhoan.equals("Tài Khoản")){
            return this.selectBySQL(sql1, keyWord_2);
        }else if(columnTaiKhoan.equals("Mật Khẩu")){
            return this.selectBySQL(sql2, keyWord_2);
        }else if(columnTaiKhoan.equals("Mã Nhân Viên")){
            return this.selectBySQL(sql3, keyWord_2);
        }else if(columnTaiKhoan.equals("Trạng Thái")){
            return this.selectBySQL(sql4, keyWord_2);
        }else{
            return this.selectBySQL(sql5, keyWord_2, keyWord_2, keyWord_2, keyWord_2);
        }
        
    }

    public List<model_TaiKhoan> selectAll() {
        String sql = "SELECT * FROM TAIKHOAN";
        return this.selectBySQL(sql);
    }
    
    
    protected List<model_TaiKhoan> selectBySQL(String sql, Object... args) {
        List<model_TaiKhoan> list_TaiKhoan = new ArrayList<model_TaiKhoan>();
        try{
            ResultSet rs = null;
            
            try{
                rs = JDBCHelper.query(sql, args);
                while(rs.next()){
                model_TaiKhoan tk = new model_TaiKhoan();
                tk.setTaiKhoan(rs.getString("TaiKhoan"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setMaNhanVien(rs.getString("MaNhanVien"));
                tk.setTrangThai(rs.getString("TrangThai"));
                
                list_TaiKhoan.add(tk);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list_TaiKhoan;
    }
    
    
    public List<String> layDanhSachMaNhanVienChuaCoTaiKhoan(){
        List<String> list_MaNhanVien = new ArrayList<String>();
        String sql = "SELECT MaNhanVien FROM NHANVIEN WHERE MaNhanVien NOT IN (SELECT MaNhanVien FROM TAIKHOAN)";
        
        try(ResultSet rs = JDBCHelper.query(sql)){
            if(rs != null)
            while(rs.next()){
                list_MaNhanVien.add(rs.getString("MaNhanVien"));
            }
        } catch (SQLException ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi lấy danh sách mã nhân viên: " + ex.getMessage());
        }
        return list_MaNhanVien;
    }
    
} 
