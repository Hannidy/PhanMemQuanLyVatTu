/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.LoaiVatTuDAO;
import entity.model_LoaiVatTu;
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
public class DiaLog_LoaiVatTu extends javax.swing.JDialog {

    private DefaultTableModel tbl_ModelLoaiVatTu;
    private LoaiVatTuDAO lvtdao = new LoaiVatTuDAO();
    private List<model_LoaiVatTu> list_LoaiVatTu = new ArrayList<model_LoaiVatTu>();
    private LoaiVatTu_Form pnloaiVatTuRef;
    /**
     * Creates new form DiaLog_LoaiVatTu
     */
    public DiaLog_LoaiVatTu(java.awt.Frame parent, boolean modal, LoaiVatTu_Form parentPanel) {
        super(parent, modal);
        initComponents();
        pnloaiVatTuRef = parentPanel;
        setLocationRelativeTo(null);
    }
    
    public void setdata(String tenLVT) {
        txt_tenloaivatTu.setText(tenLVT);
    }
    
     public void lamMoi() {
        pnloaiVatTuRef.fillToTableLoaiVatTu();
        txt_tenloaivatTu.setText("");
    }

     public void fillToTableLoaiVatTu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelLoaiVatTu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_LoaiVatTu = lvtdao.selectAll();
            if (list_LoaiVatTu != null) {
                for (model_LoaiVatTu lvt : list_LoaiVatTu) {
                    tbl_ModelLoaiVatTu.addRow(new Object[]{
                        lvt.getMaloaivatTu(), // Chỉ lấy Mã Vật Tư
                        lvt.getTenloaivatTu(), // Chỉ lấy Tên Vật Tư
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
     
     public void addLoaiVatTu() {
        boolean isValid = true;

        // Reset viền trước khi kiểm tra
        resetBorder(txt_tenloaivatTu);

        // Kiểm tra từng field
        String tenLoaiVatTu = txt_tenloaivatTu.getText().trim();
        if (tenLoaiVatTu.isEmpty()) {
            setErrorBorder(txt_tenloaivatTu);
            isValid = false;
        }

        // Nếu có lỗi nhập liệu, hiển thị thông báo và dừng lại
        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đủ thông tin!");
            return;
        }

        // 🔎 Kiểm tra tên đã tồn tại chưa
        if (lvtdao.isTenLoaiVatTuExist(tenLoaiVatTu)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên loại vật tư đã tồn tại!");
            setErrorBorder(txt_tenloaivatTu);
            return;
        }

        // Nếu hợp lệ, tiếp tục thêm vật tư
        model_LoaiVatTu lvt = new model_LoaiVatTu();
        lvt.setTenloaivatTu(txt_tenloaivatTu.getText().trim());

        try {
            lvtdao.insert(lvt);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm loại vật tư thành công!");

            // 🔔 Cập nhật bảng trong pnVatTu
            if (pnloaiVatTuRef != null) {
                pnloaiVatTuRef.fillToTableLoaiVatTu();
                //pnLVTRef.themThongBao("Thêm", lvt.getTenLoaiVatTu()); // Cập nhật thông báo
            }

            // Đợi thông báo hiển thị xong rồi mới đóng form
            new Timer(700, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.INFO, "Thêm loại vật tư thất bại!");
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

        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_tenloaivatTu = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Loại Vật Tư");

        jLabel2.setText("Tên Loại Vật Tư:");

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_tenloaivatTu))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(btn_them)
                .addGap(18, 18, 18)
                .addComponent(btn_lamMoi)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(94, 94, 94)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_tenloaivatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addGap(49, 49, 49))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        addLoaiVatTu();
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
                DiaLog_LoaiVatTu dialog = new DiaLog_LoaiVatTu(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txt_tenloaivatTu;
    // End of variables declaration//GEN-END:variables
}
