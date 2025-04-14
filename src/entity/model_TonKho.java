/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_TonKho {
   private String maKho;
    private String maVatTu;
    private int soLuong; 
    private String donVi;
    private String viTri;

    public model_TonKho() {
    }

    public model_TonKho(String maKho, String maVatTu, int soLuong, String donVi, String viTri) {
        this.maKho = maKho;
        this.maVatTu = maVatTu;
        this.soLuong = soLuong;
        this.donVi = donVi;
        this.viTri = viTri;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

   
    
}
