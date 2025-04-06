/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_LoaiVatTu {
    private String maloaivatTu;
    private String tenloaivatTu;

    public model_LoaiVatTu() {
    }

    public model_LoaiVatTu(String maloaivatTu, String tenloaivatTu) {
        this.maloaivatTu = maloaivatTu;
        this.tenloaivatTu = tenloaivatTu;
    }

    public String getMaloaivatTu() {
        return maloaivatTu;
    }

    public void setMaloaivatTu(String maloaivatTu) {
        this.maloaivatTu = maloaivatTu;
    }

    public String getTenloaivatTu() {
        return tenloaivatTu;
    }

    public void setTenloaivatTu(String tenloaivatTu) {
        this.tenloaivatTu = tenloaivatTu;
    }
    
    
}
