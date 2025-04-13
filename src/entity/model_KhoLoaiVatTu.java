
package entity;


public class model_KhoLoaiVatTu {
    private String maKho;
    private String maLoaiVatTu;

    // Constructor mặc định
    public model_KhoLoaiVatTu() {
    }

    // Constructor đầy đủ
    public model_KhoLoaiVatTu(String maKho, String maLoaiVatTu) {
        this.maKho = maKho;
        this.maLoaiVatTu = maLoaiVatTu;
    }

    // Getter và Setter
    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getMaLoaiVatTu() {
        return maLoaiVatTu;
    }

    public void setMaLoaiVatTu(String maLoaiVatTu) {
        this.maLoaiVatTu = maLoaiVatTu;
    }
}
