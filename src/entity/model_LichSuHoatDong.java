package entity;

public class model_LichSuHoatDong {
    private String maLichSu;
    private String thoiGian;
    private String maNhanVien;
    private String tenNhanVien;
    private String chucVu;
    private String thaoTac;
    private String quanLy;
    private String noiDungThaoTac;

    // Constructor
    public model_LichSuHoatDong(String maLichSu, String thoiGian, String maNhanVien, String tenNhanVien, 
                          String chucVu, String thaoTac, String quanLy, String noiDungThaoTac) {
        this.maLichSu = maLichSu;
        this.thoiGian = thoiGian;
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.chucVu = chucVu;
        this.thaoTac = thaoTac;
        this.quanLy = quanLy;
        this.noiDungThaoTac = noiDungThaoTac;
    }

    // Getters và Setters
    public String getMaLichSu() { return maLichSu; }
    public void setMaLichSu(String maLichSu) { this.maLichSu = maLichSu; }

    public String getThoiGian() { return thoiGian; }
    public void setThoiGian(String thoiGian) { this.thoiGian = thoiGian; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public String getThaoTac() { return thaoTac; }
    public void setThaoTac(String thaoTac) { this.thaoTac = thaoTac; }

    public String getQuanLy() { return quanLy; }
    public void setQuanLy(String quanLy) { this.quanLy = quanLy; }

    public String getNoiDungThaoTac() { return noiDungThaoTac; }
    public void setNoiDungThaoTac(String noiDungThaoTac) { this.noiDungThaoTac = noiDungThaoTac; }
}