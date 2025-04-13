
package entity;

public class model_VatTuLoi {
    private String maKho;
    private String maVatTu;
    private String maNhaCungCap;  // Thêm MaNhaCungCap
    private String nhaCungCap;
    private String trangThai;

    public model_VatTuLoi(String maKho, String maVatTu, String maNhaCungCap, String nhaCungCap, String trangThai) {
        this.maKho = maKho;
        this.maVatTu = maVatTu;
        this.maNhaCungCap = maNhaCungCap;
        this.nhaCungCap = nhaCungCap;
        this.trangThai = trangThai;
    }

    public model_VatTuLoi() {
    }

    // Getters và setters
    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public void setMaVatTu(String maVatTu) {
        this.maVatTu = maVatTu;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(String nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    
}
