/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_Kho {
    private String maKho;
    private String tenKho;
    private String maloaivatTu;
    private String diaChi;

    public model_Kho() {
    }

    public model_Kho(String maKho, String tenKho, String maloaivatTu, String diaChi) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.maloaivatTu = maloaivatTu;
        this.diaChi = diaChi;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getMaloaivatTu() {
        return maloaivatTu;
    }

    public void setMaloaivatTu(String maloaivatTu) {
        this.maloaivatTu = maloaivatTu;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    // Override toString() để hiển thị tên kho trong JComboBox
    @Override
    public String toString() {
        return tenKho != null ? tenKho : "Không có tên";
    }
    
    
}
