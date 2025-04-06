
package entity;

public class model_TaiKhoan {
    private String taiKhoan;
    private String matKhau;
    private String maNhanVien;
    private String trangThai;

    public model_TaiKhoan() {
    }

    public model_TaiKhoan(String taiKhoan, String matKhau, String maNhanVien, String trangThai) {
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.maNhanVien = maNhanVien;
        this.trangThai = trangThai;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    

}
