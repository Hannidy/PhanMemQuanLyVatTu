    package entity;

public class model_ChiTietPhieuNhap {
    private String maPhieuNhap;
    private String maVatTu;
    private int donViChuyenDoi;  // đơn vị sau chuyển đổi
    private int donViGoc;        // đơn vị gốc ban đầu
    private float soLuongNhap;   // theo đơn vị gốc
    private float soLuongQuyDoi; // theo đơn vị chuyển đổi

    // Getters và Setters
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(String maVatTu) {
        this.maVatTu = maVatTu;
    }

    public int getDonViChuyenDoi() {
        return donViChuyenDoi;
    }

    public void setDonViChuyenDoi(int donViChuyenDoi) {
        this.donViChuyenDoi = donViChuyenDoi;
    }

    public int getDonViGoc() {
        return donViGoc;
    }

    public void setDonViGoc(int donViGoc) {
        this.donViGoc = donViGoc;
    }

    public float getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(float soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public float getSoLuongQuyDoi() {
        return soLuongQuyDoi;
    }

    public void setSoLuongQuyDoi(float soLuongQuyDoi) {
        this.soLuongQuyDoi = soLuongQuyDoi;
    }
}
