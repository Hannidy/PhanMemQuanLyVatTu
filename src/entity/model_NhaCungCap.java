/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author RubyNgoc
 */
public class model_NhaCungCap {
    private String manhacungCap;
    private String tennhacungCap;
    private String sodienThoai;
    private String email;
    private String diaChi;

    public model_NhaCungCap() {
    }

    public model_NhaCungCap(String manhacungCap, String tennhacungCap, String sodienThoai, String email, String diaChi) {
        this.manhacungCap = manhacungCap;
        this.tennhacungCap = tennhacungCap;
        this.sodienThoai = sodienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public String getManhacungCap() {
        return manhacungCap;
    }

    public void setManhacungCap(String manhacungCap) {
        this.manhacungCap = manhacungCap;
    }

    public String getTennhacungCap() {
        return tennhacungCap;
    }

    public void setTennhacungCap(String tennhacungCap) {
        this.tennhacungCap = tennhacungCap;
    }

    public String getSodienThoai() {
        return sodienThoai;
    }

    public void setSodienThoai(String sodienThoai) {
        this.sodienThoai = sodienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    
}
