
package entity;

public class model_QuyenHan {
    
    private String Machucvu ;
    private String quanLy ;
    private Integer Xem ;
    private Integer Xuatexcel ;
    private Integer them ;
    private Integer xoa ;
    private Integer sua ;

    public model_QuyenHan() {
    }

    public model_QuyenHan(String Machucvu, String quanLy, Integer Xem, Integer Xuatexcel, Integer them, Integer xoa, Integer sua) {
        this.Machucvu = Machucvu;
        this.quanLy = quanLy;
        this.Xem = Xem;
        this.Xuatexcel = Xuatexcel;
        this.them = them;
        this.xoa = xoa;
        this.sua = sua;
    }

    public String getMachucvu() {
        return Machucvu;
    }

    public void setMachucvu(String Machucvu) {
        this.Machucvu = Machucvu;
    }

    public String getQuanLy() {
        return quanLy;
    }

    public void setQuanLy(String quanLy) {
        this.quanLy = quanLy;
    }

    public Integer getXem() {
        return Xem;
    }

    public void setXem(Integer Xem) {
        this.Xem = Xem;
    }

    public Integer getXuatexcel() {
        return Xuatexcel;
    }

    public void setXuatexcel(Integer Xuatexcel) {
        this.Xuatexcel = Xuatexcel;
    }

    public Integer getThem() {
        return them;
    }

    public void setThem(Integer them) {
        this.them = them;
    }

    public Integer getXoa() {
        return xoa;
    }

    public void setXoa(Integer xoa) {
        this.xoa = xoa;
    }

    public Integer getSua() {
        return sua;
    }

    public void setSua(Integer sua) {
        this.sua = sua;
    }
    
    
}
