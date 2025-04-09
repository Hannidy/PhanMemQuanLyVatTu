
package entity;

public class model_NhanVien {
    
    private String maNhanVien ;
    private String tenNhanVien ;
    private String maChucVu ;
    private String maPhongBan ;
    private String email ;
    private String soDienthoai ;
    private String trangThai ;

    public model_NhanVien() {
    }

    public model_NhanVien(String maNhanVien, String tenNhanVien, String maChucVu, String maPhongBan, String email, String soDienthoai, String trangThai) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.maChucVu = maChucVu;
        this.maPhongBan = maPhongBan;
        this.email = email;
        this.soDienthoai = soDienthoai;
        this.trangThai = trangThai;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienthoai() {
        return soDienthoai;
    }

    public void setSoDienthoai(String soDienthoai) {
        this.soDienthoai = soDienthoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    
}
