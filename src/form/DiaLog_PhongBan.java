/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.PhongBanDAO;
import entity.model_PhongBan;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

/**
 *
 * @author RubyNgoc
 */
public class DiaLog_PhongBan extends javax.swing.JDialog {

    public DefaultTableModel tbl_ModelPhongBan;
    public PhongBanDAO pbdao = new PhongBanDAO();
    public List<model_PhongBan> list_PB = new ArrayList<model_PhongBan>();
    private PhongBan_Form pnPBRef;

    /**
     * Creates new form DiaLog_PhongBan
     */
    public DiaLog_PhongBan(java.awt.Frame parent, boolean modal, PhongBan_Form parentPanel) {
        super(parent, modal);
        initComponents();
        pnPBRef = parentPanel;
        setLocationRelativeTo(null);
    }

    public void setData(String tenPhongBan, String DiaChi, String MatruongPhong) {
        txt_tenphongBan.setText(tenPhongBan);
        txt_diaChi.setText(DiaChi);
        txt_matruongPhong.setText(MatruongPhong);
    }

    public void fillToTablePhongBan() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelPhongBan.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_PB = pbdao.selectAll();
            if (list_PB != null) {
                for (model_PhongBan pb : list_PB) {
                    tbl_ModelPhongBan.addRow(new Object[]{
                        pb.getMaphongBan(), // Chỉ lấy Mã Vật Tư
                        pb.getTenphongBan(), // Chỉ lấy Tên Vật Tư
                        pb.getDiaChi(), // Chỉ lấy Mã Loại Vật Tư
                        pb.getMatruongPhong()
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void lamMoi() {
        pnPBRef.fillToTablePhongBan();
        txt_tenphongBan.setText("");
        txt_diaChi.setText("");
        txt_matruongPhong.setText("");
    }

    public void addPhongBan() {
        boolean isValid = true;

        // Reset viền trước khi kiểm tra
        resetBorder(txt_tenphongBan);
        resetBorder(txt_diaChi); // Chỉ cần kiểm tra TenVatTu và MaLoaiVatTu
        resetBorder(txt_matruongPhong);

        // Kiểm tra từng field
        String tenPB = txt_tenphongBan.getText().trim();
        if (tenPB.isEmpty()) {
            setErrorBorder(txt_tenphongBan);
            isValid = false;
        }
        if (txt_diaChi.getText().trim().isEmpty()) {
            setErrorBorder(txt_diaChi);
            isValid = false;
        }

        if (txt_matruongPhong.getText().trim().isEmpty()) {
            setErrorBorder(txt_matruongPhong);
            isValid = false;
        }

        // Nếu có lỗi, hiển thị thông báo và dừng lại
        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO,"Vui lòng nhập đủ thông tin!");
            return;
        }

        // 🔎 Kiểm tra tên đã tồn tại chưa
        if (pbdao.isTenLoaiVatTuExist(tenPB)) {
            Notifications.getInstance().show(Notifications.Type.INFO,"Tên loại vật tư đã tồn tại!");
            setErrorBorder(txt_tenphongBan);
            return;
        }

        // Nếu hợp lệ, tiếp tục thêm vật tư
        model_PhongBan pb = new model_PhongBan();
        pb.setTenphongBan(txt_tenphongBan.getText().trim());
        pb.setDiaChi(txt_diaChi.getText().trim()); // Chỉ thêm MaLoaiVatTu
        pb.setMatruongPhong(txt_matruongPhong.getText().trim());

        try {
            pbdao.insert(pb);
            Notifications.getInstance().show(Notifications.Type.SUCCESS,"Thêm vật tư thành công!");

            // 🔔 Cập nhật bảng trong pnVatTu
            if (pnPBRef != null) {
                pnPBRef.fillToTablePhongBan();
                //pnPBRef.thongke();
                //pnPBRef.themThongBao("Thêm", pb.getTenPhongBan()); // Cập nhật thông báo
            }

            // Đợi thông báo hiển thị xong rồi mới đóng form
            new Timer(1000, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO,"Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.INFO,"Thêm vật tư thất bại!");
        }
    }

    private void setErrorBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED)); // Gạch đỏ dưới
    }

    // Đặt lại viền mặc định cho JTextField khi nhập đúng
    private void resetBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200))); // Viền xám nhạt
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_tenphongBan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_matruongPhong = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Phòng Ban");

        jLabel2.setText("Tên Phòng Ban:");

        jLabel3.setText("Địa Chỉ:");

        jLabel4.setText("Mã Trưởng Phòng:");

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_lamMoi.setText("Làm Mới");
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_tenphongBan)
                    .addComponent(txt_diaChi)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_matruongPhong))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(btn_them)
                .addGap(18, 18, 18)
                .addComponent(btn_lamMoi)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_tenphongBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txt_matruongPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        addPhongBan();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed
        // TODO add your handling code here:
        lamMoi();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DiaLog_PhongBan dialog = new DiaLog_PhongBan(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_them;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_matruongPhong;
    private javax.swing.JTextField txt_tenphongBan;
    // End of variables declaration//GEN-END:variables
}
