/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_PhongBan {
    private String maphongBan;
    private String tenphongBan;
    private String diaChi;
    private String matruongPhong;

    public model_PhongBan() {
    }

    public model_PhongBan(String maphongBan, String tenphongBan, String diaChi, String matruongPhong) {
        this.maphongBan = maphongBan;
        this.tenphongBan = tenphongBan;
        this.diaChi = diaChi;
        this.matruongPhong = matruongPhong;
    }

    public String getMaphongBan() {
        return maphongBan;
    }

    public void setMaphongBan(String maphongBan) {
        this.maphongBan = maphongBan;
    }

    public String getTenphongBan() {
        return tenphongBan;
    }

    public void setTenphongBan(String tenphongBan) {
        this.tenphongBan = tenphongBan;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMatruongPhong() {
        return matruongPhong;
    }

    public void setMatruongPhong(String matruongPhong) {
        this.matruongPhong = matruongPhong;
    }
    
    
}
