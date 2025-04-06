
package drawer;

import form.ChucVu_Form;
import form.ThongKe_Form;
import form.DonViTinh_Form;
import form.GiaoDien_Form;
import form.Kho_Form;
import form.Kho_LoaiVatTu_Form;
import form.LichSuHoatDong_Form;
import form.LoaiVatTu_Form;
import form.NhaCungCap_Form;
import form.NhanVien_Form;
import form.PhieuNhap_Form;
import form.PhieuXuat_Form;
import form.PhieuYeuCauVatTu_Form;
import form.PhongBan_Form;
import form.QuyenHan_Form;
import form.TaiKhoan_Form;
import form.ThongTinCaNhan_Form;
import form.TonKho_Form;
import form.VatTuLoi_BaoHanh_Form;
import form.VatTu_Form;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import login.DangNhap_Form;
import manager.FormsManager;
import raven.drawer.component.SimpleDrawerBuilder;
import raven.drawer.component.footer.SimpleFooterData;
import raven.drawer.component.header.SimpleHeaderData;
import raven.drawer.component.menu.SimpleMenuOption;
import raven.swing.AvatarIcon;
import tabbed.WindowsTabbed;
import util.Auth;




public class MyDrawerBuilder extends SimpleDrawerBuilder{
    
        private SimpleMenuOption menu;



    public MyDrawerBuilder() {
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
            .setIcon(new AvatarIcon(getClass().getResource("drawer/image/dnd.png"), 60, 60, 999))
            .setTitle(Auth.getTenNhanVien())
            .setDescription(Auth.getMaNhanVien());
    }
    
    @Override
    public SimpleMenuOption getSimpleMenuOption() {
        SimpleMenuOption option = new SimpleMenuOption();

 
        List<String[]> menus = new ArrayList<>();
        List<String> icons = new ArrayList<>();

        menus.add(new String[]{"Thống Kê"});
        icons.add("dashboard_3.svg");


        // DANH MỤC VẬT TƯ 
            List<String> vatTuForms = new ArrayList<>();
            
            vatTuForms.add("Danh Mục Vật Tư");
            
            if (Auth.coThe("Quản Lý Loại Vật Tư", "Xem")) vatTuForms.add("Loại Vật Tư");
            if (Auth.coThe("Quản Lý Vật Tư", "Xem")) vatTuForms.add("Vật Tư");
            if (Auth.coThe("Quản Lý Đơn Vị Tính", "Xem")) vatTuForms.add("Đơn Vị Tính");
            if (Auth.coThe("Quản Lý Vật Tư Lỗi - Bảo Hành", "Xem")) vatTuForms.add("Vật Tư Lỗi - Bảo Hành");
            
            if (vatTuForms.size() > 1) {
                
                menus.add(vatTuForms.toArray(new String[0]));
                icons.add("danhmucvattu1.svg");
                
            }
            
        //DANH MỤC KHO
        
            List<String> khoForms = new ArrayList<>();
            
            khoForms.add("Danh Mục Kho");
            
            if (Auth.coThe("Quản Lý Kho", "Xem")) khoForms.add("Kho");
            if (Auth.coThe("Quản Lý Kho - Loại Vật Tư", "Xem")) khoForms.add("Kho - Loại Vật Tư");
            if (Auth.coThe("Quản Lý Tồn Kho", "Xem")) khoForms.add("Tồn Kho");
            
            if (khoForms.size() > 1) {
                
                menus.add(khoForms.toArray(new String[0]));
                icons.add("danhmuckho.svg");
                
            }

        // NHẬP - XUẤT
            List<String> nhapXuatForms = new ArrayList<>();
            
            nhapXuatForms.add("Nhập - Xuất");
            
            if (Auth.coThe("Quản Lý Phiếu Nhập", "Xem")) nhapXuatForms.add("Phiếu Nhập");
            if (Auth.coThe("Quản Lý Phiếu Yêu Cầu Vật Tư", "Xem")) nhapXuatForms.add("Phiếu Yêu Cầu Vật Tư");
            if (Auth.coThe("Quản Lý Phiếu Xuất", "Xem")) nhapXuatForms.add("Phiếu Xuất");
            if (Auth.coThe("Quản Lý Nhà Cung Cấp", "Xem")) nhapXuatForms.add("Nhà Cung Cấp");
            
            if (nhapXuatForms.size() > 1) {
                menus.add(nhapXuatForms.toArray(new String[0]));
                icons.add("nhapxuat.svg");
                
            }

        // NHÂN SỰ - PHÂN QUYỀN
            List<String> nhanSuForms = new ArrayList<>();
            
            nhanSuForms.add("Nhân Sự - Phân Quyền");
            
            if (Auth.coThe("Quản Lý Phòng Ban", "Xem")) nhanSuForms.add("Phòng Ban");
            if (Auth.coThe("Quản Lý Nhân Viên", "Xem")) nhanSuForms.add("Nhân Viên");
            if (Auth.coThe("Quản Lý Chức Vụ", "Xem")) nhanSuForms.add("Chức Vụ");
            if (Auth.coThe("Quản Lý Quyền Hạn", "Xem")) nhanSuForms.add("Quyền Hạn");
            if (Auth.coThe("Quản Lý Tài Khoản", "Xem")) nhanSuForms.add("Tài Khoản");
            
            if (nhanSuForms.size() > 1) {
                menus.add(nhanSuForms.toArray(new String[0]));
                icons.add("nhansuphanquyen.svg");
                
            }

        // LỊCH SỬ HOẠT ĐỘNG
        if (Auth.coThe("Lịch Sử Hoạt Động", "Xem")) {
            
            menus.add(new String[]{"Lịch Sử Hoạt Động"});
            icons.add("lichsuhoatdong.svg");
            
        }

        // KHÁC
//        menus.add(new String[]{"~KHÁC~"});
        
        List<String> caiDatForms = new ArrayList<>();
        
        caiDatForms.add("Cài Đặt");
        caiDatForms.add("Giao Diện");
        caiDatForms.add("Thông Tin Cá Nhân");
        caiDatForms.add("Đăng Xuất");
        
        menus.add(caiDatForms.toArray(new String[0]));
        icons.add("caidat.svg");
        
        // Lưu lại bản sao menus để dùng trong menuEvent (vì option không có getMenus)
        List<String[]> finalMenus = new ArrayList<>(menus);

        // Tạo menu,icon
        option.setMenus(menus.toArray(new String[0][]));
        option.setIcons(icons.toArray(new String[0]));
        option.setBaseIconPath("drawer/icon");
        option.setIconScale(0.4f);

        
    // Xử lý sự kiện click
    option.addMenuEvent((action, index, subIndex) -> {
    try {
        // Lấy mảng con theo chỉ mục chính
        String[] row = finalMenus.get(index);

        String menuEvent = row[subIndex];

        switch (menuEvent) {
            case "Thống Kê" ->{
                System.out.println("→ Mở form Thống Kê");
                WindowsTabbed.getInstance().addTab("Thống Kê", new ThongKe_Form());
            }
            case "Loại Vật Tư" ->{
                System.out.println("→ Mở form Loại Vật Tư");
                WindowsTabbed.getInstance().addTab("Loại Vật Tư", new LoaiVatTu_Form());
            }
            case "Vật Tư" ->{
                System.out.println("→ Mở form Vật Tư");
                WindowsTabbed.getInstance().addTab("Vật Tư", new VatTu_Form());
            }
            case "Đơn Vị Tính" ->{
                System.out.println("→ Mở form Đơn Vị Tính");
                WindowsTabbed.getInstance().addTab("Đơn Vị Tính", new DonViTinh_Form());
            }
            case "Vật Tư Lỗi - Bảo Hành" ->{
                System.out.println("→ Mở form Vật Tư Lỗi - Bảo Hành");
                WindowsTabbed.getInstance().addTab("Vật Tư Lỗi - Bảo Hành", new VatTuLoi_BaoHanh_Form());
            }
            case "Kho" ->{
                System.out.println("→ Mở form Kho");
                WindowsTabbed.getInstance().addTab("Kho", new Kho_Form());
            }
            case "Kho - Loại Vật Tư" ->{
                System.out.println("→ Mở form Kho - Loại Vật Tư");
                WindowsTabbed.getInstance().addTab("Kho - Loại Vật Tư", new Kho_LoaiVatTu_Form());
            }
            case "Tồn Kho" ->{
                System.out.println("→ Mở form Tồn Kho");
                WindowsTabbed.getInstance().addTab("Tồn Kho", new TonKho_Form());
            }
            case "Phiếu Nhập" ->{
                System.out.println("→ Mở form Phiếu Nhập");
                WindowsTabbed.getInstance().addTab("Phiếu Nhập", new PhieuNhap_Form());
            }
            case "Phiếu Yêu Cầu Vật Tư" ->{
                System.out.println("→ Mở form Phiếu Yêu Cầu Vật Tư");
                WindowsTabbed.getInstance().addTab("Phiếu Yêu Cầu Vật Tư", new PhieuYeuCauVatTu_Form());
            }
            case "Phiếu Xuất" ->{
                System.out.println("→ Mở form Phiếu Xuất");
                WindowsTabbed.getInstance().addTab("Phiếu Xuất", new PhieuXuat_Form());
            }
            case "Nhà Cung Cấp" ->{
                System.out.println("→ Mở form Nhà Cung Cấp");
                WindowsTabbed.getInstance().addTab("Nhà Cung Cấp", new NhaCungCap_Form());
            }
            case "Phòng Ban" ->{
                System.out.println("→ Mở form Phòng Ban");
                WindowsTabbed.getInstance().addTab("Phòng Ban", new PhongBan_Form());
            }
            case "Nhân Viên" ->{
                System.out.println("→ Mở form Nhân Viên");
                WindowsTabbed.getInstance().addTab("Nhân Viên", new NhanVien_Form());
            }
            case "Chức Vụ" ->{
                System.out.println("→ Mở form Chức Vụ");
                WindowsTabbed.getInstance().addTab("Chức Vụ", new ChucVu_Form());
            }
            case "Quyền Hạn" ->{
                System.out.println("→ Mở form Quyền Hạn");
                WindowsTabbed.getInstance().addTab("Quyền Hạn", new QuyenHan_Form());
            }
            case "Tài Khoản" ->{
                System.out.println("→ Mở form Tài Khoản");
                WindowsTabbed.getInstance().addTab("Tài Khoản", new TaiKhoan_Form());
            }
            case "Lịch Sử Hoạt Động" ->{
                System.out.println("→ Mở form Lịch Sử Hoạt Động");
                WindowsTabbed.getInstance().addTab("Lịch Sử Hoạt Động", new LichSuHoatDong_Form());
            }
            case "Giao Diện" ->{
                System.out.println("→ Mở form Giao Diện");
                WindowsTabbed.getInstance().addTab("Giao Diện", new GiaoDien_Form());
            }
            case "Thông Tin Cá Nhân" ->{
                System.out.println("→ Mở form Thông Tin Cá Nhân");
                WindowsTabbed.getInstance().addTab("Thông Tin Cá Nhân", new ThongTinCaNhan_Form());
            }
            case "Đăng Xuất" -> {
                System.out.println("→ Đăng xuất...");
                Auth.dangXuat();
                new DangNhap_Form().setVisible(true);

                // Dùng JFrame hiện tại để đóng form cũ
                JFrame current = FormsManager.getInstance().getMain();
                if (current != null) current.dispose();
            }

        }
    } catch (Exception e) {
        System.err.println("Lỗi khi xử lý menu: " + e.getMessage());
    }
});
        
        return option;
    }




    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
            .setTitle("TAOSWORK")
            .setDescription("Phiên Bản 1.0");
    }


    
}
