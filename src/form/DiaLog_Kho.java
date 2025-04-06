/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package form;

import dao.KhoDAO;
import entity.model_Kho;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

/**
 *
 * @author RubyNgoc
 */
public class DiaLog_Kho extends javax.swing.JDialog {

    private DefaultTableModel tbl_ModelKho;
    KhoDAO kDAO = new KhoDAO();
    private Kho_Form pnKhoRef;
    private List<model_Kho> list_Kho = new ArrayList<model_Kho>();

    /**
     * Creates new form DiaLog_Kho
     */
    public DiaLog_Kho(java.awt.Frame parent, boolean modal, Kho_Form parentPanel, Set<String> dsMaLoai) {
        super(parent, modal);
        initComponents();
        pnKhoRef = parentPanel;
        setLocationRelativeTo(null);
        cbo_maloaivatTu.removeAllItems();
        for (String maLoai : dsMaLoai) {
            cbo_maloaivatTu.addItem(maLoai);
        }
        cbo_maloaivatTu.revalidate();
        cbo_maloaivatTu.repaint();
    }

    public void setData(String tenKho, String maloaivatTu, String diaChi) {
        txt_tenKho.setText(tenKho);
        cbo_maloaivatTu.setSelectedItem(maloaivatTu);
        txt_diaChi.setText(diaChi);
    }

    public void setMaLoaiData(Set<String> dsMaLoai) { // Hàm truyền dữ liệu vào combobox
        cbo_maloaivatTu.removeAllItems(); // Xóa dữ liệu cũ nếu có
        for (String maLoai : dsMaLoai) {
            cbo_maloaivatTu.addItem(maLoai);
        }
    }

    public void fillToTableKho() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelKho.setRowCount(0);

            // Lấy danh sách kho từ database
            list_Kho = kDAO.selectAll();
            if (list_Kho != null) {
                for (model_Kho k : list_Kho) {
                    tbl_ModelKho.addRow(new Object[]{
                        k.getMaKho(), // Mã Kho
                        k.getTenKho(), // Tên Kho
                        k.getMaloaivatTu(), // Mã Loại Vật Tư
                        k.getDiaChi() // Địa Chỉ
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addKho() {
        boolean isValid = true;

        // Reset viền trước khi kiểm tra
        resetBorder(txt_tenKho);
        resetBorder(txt_diaChi);

        // Kiểm tra từng field
        String tenKho = txt_tenKho.getText().trim();
        if (tenKho.isEmpty()) {
            setErrorBorder(txt_tenKho);
            isValid = false;
        }
        if (txt_diaChi.getText().trim().isEmpty()) {
            setErrorBorder(txt_diaChi);
            isValid = false;
        }

        // Nếu có lỗi, hiển thị thông báo và dừng lại
        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đủ thông tin!");
            return;
        }

        // 🔎 Kiểm tra tên đã tồn tại chưa
        if (kDAO.isTenKhoExist(tenKho)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên loại vật tư đã tồn tại!");
            setErrorBorder(txt_tenKho);
            return;
        }

        // Nếu hợp lệ, tiếp tục thêm kho
        model_Kho k = new model_Kho();
        k.setTenKho(txt_tenKho.getText().trim());
        k.setMaloaivatTu((String) cbo_maloaivatTu.getSelectedItem());
        k.setDiaChi(txt_diaChi.getText().trim());

        try {
            kDAO.insert(k);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm kho thành công!");

            // 🔔 Cập nhật bảng nếu có tham chiếu đến pnKho
            if (pnKhoRef != null) {
                pnKhoRef.fillToTableKho();
                //pnKhoRef.themThongBao("Thêm", k.getTenKho()); // Cập nhật thông báo
            }

            // Đợi thông báo hiển thị xong rồi mới đóng form
            new Timer(1000, e -> dispose()).start();

        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.INFO, "Thêm kho thất bại!");
        }
    }

    public void lamMoi() {
        pnKhoRef.fillToTableKho();
        this.txt_tenKho.setText("");
        this.txt_diaChi.setText("");
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
        txt_tenKho = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();
        cbo_maloaivatTu = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Kho");

        jLabel2.setText("Tên Kho:");

        jLabel3.setText("Mã Loại Vật Tư:");

        jLabel4.setText("Địa Chỉ:");

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

        cbo_maloaivatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_tenKho)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txt_diaChi)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(btn_them)
                        .addGap(18, 18, 18)
                        .addComponent(btn_lamMoi)
                        .addGap(0, 74, Short.MAX_VALUE))
                    .addComponent(cbo_maloaivatTu, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_tenKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(cbo_maloaivatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        addKho();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed
        // TODO add your handling code here:
        lamMoi();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DiaLog_Kho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DiaLog_Kho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DiaLog_Kho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DiaLog_Kho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DiaLog_Kho dialog = new DiaLog_Kho(new javax.swing.JFrame(), true, null, null);
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
    private javax.swing.JComboBox<String> cbo_maloaivatTu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_tenKho;
    // End of variables declaration//GEN-END:variables
}
