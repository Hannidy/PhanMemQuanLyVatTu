
package main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import drawer.MyDrawerBuilder;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import login.DangNhap_Form;
import manager.FormsManager;
import raven.drawer.Drawer;
import raven.popup.GlassPanePopup;
import raven.toast.Notifications;
import tabbed.WindowsTabbed;  

public class Main extends javax.swing.JFrame {

    public Main() {
        GlassPanePopup.install(this);
        initComponents();
        init();
    }


    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setContentPane(new DangNhap_Form());
        Notifications.getInstance().setJFrame(this);
        FormsManager.getInstance().initMain(this);
    }
    
    public void initAfterLogin(){
        
        MyDrawerBuilder myDrawerBuilder = new MyDrawerBuilder();
        
        Drawer.getInstance().setDrawerBuilder(myDrawerBuilder);
        
        // Bây giờ WindowsTabbed sẽ gắn cả panelTabbed + body vào frame
        WindowsTabbed.getInstance().install(this, body);
        
        // Thêm tab đầu tiên khi đăng nhập thành công
//        WindowsTabbed.getInstance().addTab("Dashboard", new DashBoard_Form());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        body.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 1500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatLightLaf.setup();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    // End of variables declaration//GEN-END:variables
}
