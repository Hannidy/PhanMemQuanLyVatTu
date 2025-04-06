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
    private String mavatTu;
    private String soLuong;
    private String donVi;
    private String tontoiThieu;
    private String tontoiDa;
    private String viTri;

    public model_TonKho() {
    }

    public model_TonKho(String maKho, String mavatTu, String soLuong, String donVi, String tontoiThieu, String tontoiDa, String viTri) {
        this.maKho = maKho;
        this.mavatTu = mavatTu;
        this.soLuong = soLuong;
        this.donVi = donVi;
        this.tontoiThieu = tontoiThieu;
        this.tontoiDa = tontoiDa;
        this.viTri = viTri;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getMavatTu() {
        return mavatTu;
    }

    public void setMavatTu(String mavatTu) {
        this.mavatTu = mavatTu;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public String getTontoiThieu() {
        return tontoiThieu;
    }

    public void setTontoiThieu(String tontoiThieu) {
        this.tontoiThieu = tontoiThieu;
    }

    public String getTontoiDa() {
        return tontoiDa;
    }

    public void setTontoiDa(String tontoiDa) {
        this.tontoiDa = tontoiDa;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
    
    
}
