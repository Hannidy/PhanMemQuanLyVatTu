package entity;

public class model_ChiTietPhieuXuat {
    private String maPhieuXuat;
    private String maVatTu;
    private String donViChuyenDoi;  // Đơn vị chuyển đổi (kiểu String)
    private String donViGoc;        // Đơn vị gốc (kiểu String)
    private float soLuongXuat;      // Số lượng xuất
    private float soLuongQuyDoi;    // Số lượng quy đổi

    // Constructors, getters, setters
    public model_ChiTietPhieuXuat() {}

    public model_ChiTietPhieuXuat(String maPhieuXuat, String maVatTu, String donViGoc, String donViChuyenDoi, float soLuongXuat, float soLuongQuyDoi) {
        this.maPhieuXuat = maPhieuXuat;
        this.maVatTu = maVatTu;
        this.donViGoc = donViGoc;
        this.donViChuyenDoi = donViChuyenDoi;
        this.soLuongXuat = soLuongXuat;
        this.soLuongQuyDoi = soLuongQuyDoi;
    }
    
    // Getters và Setters
    public String getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(String maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(String maVatTu) {
        this.maVatTu = maVatTu;
    }

    public String getDonViChuyenDoi() {
        return donViChuyenDoi;
    }

    public void setDonViChuyenDoi(String donViChuyenDoi) {
        this.donViChuyenDoi = donViChuyenDoi;
    }

    public String getDonViGoc() {
        return donViGoc;
    }

    public void setDonViGoc(String donViGoc) {
        this.donViGoc = donViGoc;
    }

    public float getSoLuongXuat() {
        return soLuongXuat;
    }

    public void setSoLuongXuat(float soLuongXuat) {
        this.soLuongXuat = soLuongXuat;
    }

    public float getSoLuongQuyDoi() {
        return soLuongQuyDoi;
    }

    public void setSoLuongQuyDoi(float soLuongQuyDoi) {
        this.soLuongQuyDoi = soLuongQuyDoi;
    }
}
