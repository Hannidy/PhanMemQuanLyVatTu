
package login;

import com.formdev.flatlaf.FlatClientProperties;
import component.PasswordStrengthStatus;
import static component.PasswordStrengthStatus.mucDoMatKhau;
import dao.TaiKhoanDAO;
import entity.model_TaiKhoan;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import manager.FormsManager;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;
import util.MaHoaMD5;

public class DangKi_Form extends javax.swing.JPanel {
    
    DefaultComboBoxModel<String> comboBoxModel_MaNhanVien;
    private TaiKhoanDAO tkdao = new TaiKhoanDAO();
    private boolean placeholderRemoved = false;
    private JComboBox<String> cbo_MaNhanVien;
    private JTextField txt_TaiKhoan;
    private JPasswordField txt_MatKhau;
    private JPasswordField txt_XacNhanMatKhau;
    private JButton cmdDangKi;
    private PasswordStrengthStatus passwordStrengthStatus;
    
    public DangKi_Form() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 0", "[center]", "[center]"));
        
        // 1. Khởi tạo ComboBox không có placeholder trong model
    cbo_MaNhanVien = new JComboBox<>();
    comboBoxModel_MaNhanVien = new DefaultComboBoxModel<>();

    for (String maNV : tkdao.layDanhSachMaNhanVienChuaCoTaiKhoan()) {
        comboBoxModel_MaNhanVien.addElement(maNV);
    }

    cbo_MaNhanVien.setModel(comboBoxModel_MaNhanVien);
    cbo_MaNhanVien.setSelectedItem(null); // ❗ Không chọn item nào

    // 2. Renderer để hiển thị placeholder khi chưa chọn gì
    cbo_MaNhanVien.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value == null) {
                setText("Chọn Mã Nhân Viên");
                setForeground(Color.GRAY);
            } else {
                setForeground(Color.GRAY);
            }

            return this;
        }
    });
    
    
    
    
        txt_TaiKhoan = new JTextField();
        txt_MatKhau = new JPasswordField();
        txt_XacNhanMatKhau = new JPasswordField();
        cmdDangKi = new JButton("Tạo Tài Khoản");
        
        //GÁN ENTER ĐỂ GỌI HÀNH ĐỘNG ĐĂNG KÍ
        cmdDangKi.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "clickDangKi"
        );
        cmdDangKi.getActionMap().put("clickDangKi", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdDangKi.doClick();  // Kích hoạt như click chuột
            }
        });

        cmdDangKi.addActionListener(e -> {
            
            if (kiemTraDuLieuTrong() && isMatchPassword()) {
                dangKiTaiKhoan();
            }
        });
        
        passwordStrengthStatus = new PasswordStrengthStatus();
        

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 50 60 50 60", "fill,800:800"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:25;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        cbo_MaNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txt_TaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txt_MatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txt_XacNhanMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cmdDangKi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        txt_TaiKhoan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên tài khoản");
        txt_MatKhau.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");
        txt_XacNhanMatKhau.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Xác nhận mật khẩu");
        txt_MatKhau.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txt_XacNhanMatKhau.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        cmdDangKi.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        JLabel lbl_TieuDe = new JLabel("Chào mừng đến với phần mềm quản lý vật tư của chúng tôi");
        JLabel lbl_MoTa = new JLabel("Hãy tham gia cùng chúng tôi để quản lý vật tư. Đăng ký ngay và bắt đầu sử dụng!");
        
        lbl_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl_MoTa.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        passwordStrengthStatus.initPasswordField(txt_MatKhau);

        panel.add(lbl_TieuDe, "gapy 10");
        panel.add(lbl_MoTa, "gapbottom 30");

        panel.add(new JLabel("Mã Nhân Viên"), "gapy 15");
        panel.add(cbo_MaNhanVien, "height 35!");
        panel.add(new JLabel("Tên Tài Khoản"), "gapy 15");
        panel.add(txt_TaiKhoan, "height 35!");
        panel.add(new JLabel("Mật Khẩu"), "gapy 15");
        panel.add(txt_MatKhau, "height 35!");
        panel.add(passwordStrengthStatus, "gapy 5");
        panel.add(new JLabel("Xác Nhận Mật Khẩu"), "gapy 15");
        panel.add(txt_XacNhanMatKhau, "height 35!");
        panel.add(cmdDangKi, "gapy 35,height 40!");
        panel.add(createLoginLabel(), "gapy 10");

        add(panel);
    }

    private Component createLoginLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton cmdDangNhap = new JButton("<html><a href=\"#\">Đăng nhập ngay</a></html>");
        cmdDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cmdDangNhap.setContentAreaFilled(false);
        cmdDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdDangNhap.putClientProperty(FlatClientProperties.STYLE, "" +
            "[light]background:darken(@background,10%);" +
            "[dark]background:lighten(@background,10%);" +
            "borderWidth:0;" +
            "focusWidth:0;" +
            "innerFocusWidth:0");
        
        cmdDangNhap.addActionListener(e -> {
            FormsManager.getInstance().showForm(new DangNhap_Form());
        });
        
        
        JLabel label = new JLabel("Bạn đã có tài khoản ?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%); [dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdDangNhap);
        
        return panel;
    }

    private boolean kiemTraDuLieuTrong(){


            if(cbo_MaNhanVien.getSelectedItem() == null){
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã nhân viên chưa được chọn. Hãy nhập đầy đủ thông tin!");
                return false;
            }else if(txt_TaiKhoan.getText().trim().equals("")){
                Notifications.getInstance().show(Notifications.Type.INFO, "Tài khoản chưa được nhập. Hãy nhập đầy đủ thông tin!");
                return false;
            }else if(txt_MatKhau.getPassword().length == 0){
                Notifications.getInstance().show(Notifications.Type.INFO, "Mật khẩu chưa được nhập. Hãy nhập đầy đủ thông tin!");
                return false;
            }else if(txt_XacNhanMatKhau.getPassword().length == 0){
                Notifications.getInstance().show(Notifications.Type.INFO, "Mật khẩu xác nhận chưa được nhập. Hãy nhập đầy đủ thông tin!");
                return false;
            }else {
                return true;
            }
    }
    
    private void dangKiTaiKhoan(){
        
        if(mucDoMatKhau == 1){
            Notifications.getInstance().show(Notifications.Type.INFO, "Mật khẩu yếu, vui lòng nhâp mật khẩu có ít nhất 8 kí tự (bao gồm chữ viết hoa, chữ thường, chữ số và kí tự đặc biệt");
            return;
        }else if(mucDoMatKhau == 2){
            Notifications.getInstance().show(Notifications.Type.INFO, "Mật khẩu trung bình, vui lòng nhâp mật khẩu có ít nhất 8 kí tự (bao gồm chữ viết hoa, chữ thường, chữ số và kí tự đặc biệt");
            return;
        }else if(mucDoMatKhau == 3){
        try {
            model_TaiKhoan tk = new model_TaiKhoan();
            tk.setTaiKhoan(txt_TaiKhoan.getText().trim());
            String matKhauMaHoa = MaHoaMD5.MD5encoder(String.valueOf(txt_MatKhau.getPassword()));
            tk.setMatKhau(matKhauMaHoa);
            tk.setMaNhanVien((String) cbo_MaNhanVien.getSelectedItem());
            
            if(tkdao.insert(tk)){
            
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đăng kí tài khoản thành công!");
                int sangDangNhap = JOptionPane.showConfirmDialog(null, "Bạn đã có tài khoản, bạn có muốn đăng nhập ngay bây giờ không?", "Xác Nhận", JOptionPane.YES_NO_OPTION);
                switch(sangDangNhap){
                    case JOptionPane.YES_OPTION:
                        FormsManager.getInstance().showForm(new DangNhap_Form());
                    case JOptionPane.NO_OPTION:
                        txt_TaiKhoan.setText("");
                        txt_MatKhau.setText("");
                        txt_XacNhanMatKhau.setText("");
                        cbo_MaNhanVien.setSelectedItem("");
                }
            }else{
                Notifications.getInstance().show(Notifications.Type.ERROR, "Đăng kí tài khoản thất bại!");
            }

            
        } catch(Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.ERROR, "Đăng kí tài khoản thất bại!");
        }
    }else{
        return;
    }
    }
    
    public boolean isMatchPassword() {
        String password = String.valueOf(txt_MatKhau.getPassword());
        String confirmPassword = String.valueOf(txt_XacNhanMatKhau.getPassword());
        if(password.equals(confirmPassword)){
            return true;
        }else{
            Notifications.getInstance().show(Notifications.Type.INFO, "Mật khẩu xác nhận phải giống với mật khẩu đã nhập");
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
