package entity;

import java.util.Date;

public class model_PhieuXuat {
    private String maPhieuXuat;      // Mã phiếu xuất
    private Date ngayChungTu;        // Ngày chứng từ
    private Date ngayXuat;           // Ngày xuất
    private String maKho;            // Mã kho
    private String maPhongBanYeuCau; // Mã phòng ban yêu cầu
    private String maNguoiXuat;      // Mã người xuất
    private String maNguoiNhan;      // Mã người nhận
    private String lyDo;             // Lý do xuất
    private String trangThai;        // Trạng thái phiếu xuất

    // Getter và Setter cho các thuộc tính
    public String getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(String maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public Date getNgayChungTu() {
        return ngayChungTu;
    }

    public void setNgayChungTu(Date ngayChungTu) {
        this.ngayChungTu = ngayChungTu;
    }

    public Date getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(Date ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getMaPhongBanYeuCau() {
        return maPhongBanYeuCau;
    }

    public void setMaPhongBanYeuCau(String maPhongBanYeuCau) {
        this.maPhongBanYeuCau = maPhongBanYeuCau;
    }

    public String getMaNguoiXuat() {
        return maNguoiXuat;
    }

    public void setMaNguoiXuat(String maNguoiXuat) {
        this.maNguoiXuat = maNguoiXuat;
    }

    public String getMaNguoiNhan() {
        return maNguoiNhan;
    }

    public void setMaNguoiNhan(String maNguoiNhan) {
        this.maNguoiNhan = maNguoiNhan;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
