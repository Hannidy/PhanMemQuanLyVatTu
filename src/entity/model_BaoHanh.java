/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author ANH KHOA
 */
public class model_BaoHanh {
    public String maKho;
    public String maVatTu;
    public String tenVatTu;
    public String trangThai;

    public model_BaoHanh(String maKho, String maVatTu, String tenVatTu, String trangThai) {
        this.maKho = maKho;
        this.maVatTu = maVatTu;
        this.tenVatTu = tenVatTu;
        this.trangThai = trangThai;
    }

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

    public String getTenVatTu() {
        return tenVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu = tenVatTu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }


}
