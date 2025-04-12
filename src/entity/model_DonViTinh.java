package entity;

public class model_DonViTinh {
    private int maDonVi;
    private String tenDonVi;
    private String nhomVatTu;

    public model_DonViTinh() {
    }

    public model_DonViTinh(int maDonVi, String tenDonVi, String nhomVatTu) {
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
        this.nhomVatTu = nhomVatTu;
    }

    public int getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(int maDonVi) {
        this.maDonVi = maDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        this.tenDonVi = tenDonVi != null ? tenDonVi.trim() : null;
    }

    public String getNhomVatTu() {
        return nhomVatTu;
    }

    public void setNhomVatTu(String nhomVatTu) {
        this.nhomVatTu = nhomVatTu != null ? nhomVatTu.trim() : null;
    }

    @Override
    public String toString() {
        return tenDonVi; // tiện lợi nếu hiển thị trong combobox
    }
}