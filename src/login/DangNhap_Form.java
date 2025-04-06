
package login;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;                                                                                                                  
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import main.Main;
import manager.FormsManager;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;
import java.util.ArrayList;
import entity.model_TaiKhoan;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import util.Auth;

public class DangNhap_Form extends javax.swing.JPanel {
    
    private List<model_TaiKhoan> list_TaiKhoan = new ArrayList<model_TaiKhoan>();

    public DangNhap_Form() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 50", "[center]", "[center]"));
        txt_TaiKhoan = new JTextField();
        txt_MatKhau = new JPasswordField();
        cmdDangNhap = new JButton("Đăng Nhập");


        
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 50 60 50 60", "fill,350:400"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:25;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        // Định dạng lại TextField, PasswordField lớn hơn, dễ nhìn hơn
        txt_TaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txt_MatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cmdDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        txt_MatKhau.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdDangNhap.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txt_TaiKhoan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên tài khoản");
        txt_MatKhau.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");

        
        
        JLabel lbl_TieuDe = new JLabel("Chào mừng trở lại!");
        JLabel lbl_MoTa = new JLabel("Vui lòng đăng nhập để truy cập tài khoản của bạn");
        
        lbl_TieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl_MoTa.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl_TieuDe.setHorizontalAlignment(SwingConstants.LEFT);
        lbl_MoTa.setHorizontalAlignment(SwingConstants.LEFT);
        
        lbl_TieuDe.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        lbl_MoTa.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbl_TieuDe, "gapy 10");
        panel.add(lbl_MoTa, "gapbottom 10");
        panel.add(new JLabel("Tên Tài Khoản"), "gapy 20");
        panel.add(txt_TaiKhoan, "height 40!,");
        panel.add(new JLabel("Mật Khẩu"), "gapy 20");
        panel.add(txt_MatKhau, "height 40!");
//        panel.add(chRememberMe, "grow 0");
        panel.add(cmdDangNhap, "gapy 30, height 45!");
        panel.add(taoLabelDangKi(), "gapy 20");
        panel.add(taoLabelQuenMatKhau(), "gapy 10");
        add(panel);
        
        //GÁN ENTER ĐỂ GỌI HÀNH ĐỘNG ĐĂNG KÍ
        cmdDangNhap.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            "clickDangKi"
        );
        cmdDangNhap.getActionMap().put("clickDangKi", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdDangNhap.doClick();  // Kích hoạt như click chuột
            }
        });
        
        cmdDangNhap.addActionListener(e -> {
        String taiKhoan = txt_TaiKhoan.getText();
        String matKhau = String.valueOf(txt_MatKhau.getPassword()).trim();
        
        if(taiKhoan.equals("")){
            Notifications.getInstance().show(Notifications.Type.INFO, "Bạn chưa nhập tài khoản, vui lòng nhập đầy đủ thông tin!");
            return;
        }else if(matKhau.equals("")){
            Notifications.getInstance().show(Notifications.Type.INFO, "Bạn chưa nhập mật khẩu, vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        if (Auth.xacThucDangNhap(taiKhoan, matKhau)) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đăng Nhập Thành Công!!!");
            dangNhapThanhCong();
        } else {
            //Chưa có gì ở đây cả
        }
        
        });
        
    }

    private Component taoLabelDangKi() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton cmdDangKi = new JButton("<html><a href=\"#\">Đăng kí</a></html>");
        cmdDangKi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cmdDangKi.setContentAreaFilled(false);
        cmdDangKi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdDangKi.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        cmdDangKi.addActionListener(e -> {
            FormsManager.getInstance().showForm(new DangKi_Form());
//            Main mainFrame = (Main) SwingUtilities.getWindowAncestor(this);
//            mainFrame.showForm("register");
        });
        JLabel label = new JLabel("Bạn chưa có tài khoản?");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdDangKi);
        return panel;
    }
    
    private Component taoLabelQuenMatKhau(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JLabel label = new JLabel("hoặc");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.putClientProperty(FlatClientProperties.STYLE, 
        "[light]foreground:lighten(@foreground,30%);" +
        "[dark]foreground:darken(@foreground,30%)");
        JButton cmdQuenMatKhau = new JButton("<html><a href=\"#\">Quên mật khẩu</a></html>");
        cmdQuenMatKhau.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cmdQuenMatKhau.setContentAreaFilled(false);
        cmdQuenMatKhau.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdQuenMatKhau.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        cmdQuenMatKhau.addActionListener(e -> {
            FormsManager.getInstance().showForm(new ForgetPassword_Form());
        });
        panel.add(label);
        panel.add(cmdQuenMatKhau);
        return panel;
    }
    

    
    
    private void dangNhapThanhCong(){
        Main mainFrame = (Main) SwingUtilities.getWindowAncestor(this);
        mainFrame.initAfterLogin();  // hàm này ta sẽ tạo tiếp
//        FormsManager.getInstance().showForm(new JPanel()); // Đặt tab trống ban đầu hoặc giao diện chính tùy bạn
        
        // Hiện lại frame chính (nếu đã bị ẩn)
        mainFrame.setVisible(true);
        
    }


    private JTextField txt_TaiKhoan;
    private JPasswordField txt_MatKhau;
    private JButton cmdDangNhap;
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
