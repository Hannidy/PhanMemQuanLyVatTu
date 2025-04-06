/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_VatTu {
    private String mavatTu;
    private String tenVatTu;
    private String maloaivatTu;

    public model_VatTu() {
    }

    public model_VatTu(String mavatTu, String tenVatTu, String maloaivatTu) {
        this.mavatTu = mavatTu;
        this.tenVatTu = tenVatTu;
        this.maloaivatTu = maloaivatTu;
    }

    public String getMavatTu() {
        return mavatTu;
    }

    public void setMavatTu(String mavatTu) {
        this.mavatTu = mavatTu;
    }

    public String getTenVatTu() {
        return tenVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu = tenVatTu;
    }

    public String getMaloaivatTu() {
        return maloaivatTu;
    }

    public void setMaloaivatTu(String maloaivatTu) {
        this.maloaivatTu = maloaivatTu;
    }
    
    
}
