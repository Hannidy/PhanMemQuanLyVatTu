/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.DonViTinhDAO;
import entity.model_DonViTinh;
import entity.model_VatTu;
import dao.LichSuHoatDongDAO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

public class DiaLog_DonViTinh extends javax.swing.JDialog {
    private DefaultTableModel tbl_ModelDonViTinh;
    private DonViTinhDAO dvtDAO = new DonViTinhDAO();
    private DonViTinh_Form pnDonViTinhRef;
    private List<model_DonViTinh> list_DonViTinh = new ArrayList<model_DonViTinh>();
    private DonViTinh_Form parentPanel;
    private final Set<String> dsNhomVatTu;
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
  


    public DiaLog_DonViTinh(Frame parent, boolean modal, DonViTinh_Form parentPanel, Set<String> dsNhomVatTu) {
    super(parent, modal);
    this.parentPanel = parentPanel;
    this.dsNhomVatTu = dsNhomVatTu;
    initComponents();

    // Cập nhật combobox
    cbo_NhomVatTu.removeAllItems();
    for (String nhom : dsNhomVatTu) {
        cbo_NhomVatTu.addItem(nhom);
    }
    cbo_NhomVatTu.revalidate();
    cbo_NhomVatTu.repaint();
}
    public void setData(String tenDonVi, String nhomVatTu) {
    txt_TenDonVi.setText(tenDonVi != null ? tenDonVi : "");
    cbo_NhomVatTu.setSelectedItem(nhomVatTu); // Nếu là comboBox
}
    // Cập nhật lại danh sách nhóm vật tư nếu cần
    public void setNhomVatTuData(Set<String> dsNhomVatTu) {
        cbo_NhomVatTu.removeAllItems();
        for (String nhom : dsNhomVatTu) {
            cbo_NhomVatTu.addItem(nhom);
        }
    }
    public void fillToTableDonViTinh() {
    try {
        // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
        tbl_ModelDonViTinh.setRowCount(0);

        // Lấy danh sách đơn vị tính từ DAO
        list_DonViTinh = dvtDAO.selectAll();
        if (list_DonViTinh != null) {
            for (model_DonViTinh dvt : list_DonViTinh) {
                tbl_ModelDonViTinh.addRow(new Object[]{
                    dvt.getMaDonVi(),     // Mã đơn vị
                    dvt.getTenDonVi(),    // Tên đơn vị
                    dvt.getNhomVatTu()    // Nhóm vật tư
                });
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
    public void lamMoi() {
    parentPanel.fillToTableDonViTinh();
    this.txt_TenDonVi.setText("");
    this.cbo_NhomVatTu.setSelectedIndex(0);
}
   public void addDonViTinh() {
        boolean isValid = true;

        // Reset viền trước khi kiểm tra
        resetBorder(txt_TenDonVi);

        // Kiểm tra tên đơn vị
        String tenDonVi = txt_TenDonVi.getText().trim();
        if (tenDonVi.isEmpty()) {
            setErrorBorder(txt_TenDonVi);
            isValid = false;
        }

        // Nếu có lỗi nhập liệu, hiển thị thông báo và dừng lại
        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đủ thông tin!");
            return;
        }

        // 🔎 Kiểm tra tên đã tồn tại chưa
        if (dvtDAO.isTenDonViExist(tenDonVi)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên đơn vị đã tồn tại!");
            setErrorBorder(txt_TenDonVi);
            return;
        }

        // Nếu hợp lệ, tiếp tục thêm đơn vị
        model_DonViTinh dvt = new model_DonViTinh();
        dvt.setTenDonVi(tenDonVi);
        dvt.setNhomVatTu((String) cbo_NhomVatTu.getSelectedItem());

        try {
            dvtDAO.insert(dvt);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "✅ Thêm đơn vị tính thành công!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Thêm", "Đơn Vị Tính", "Thêm đơn vị tính: Tên: " + tenDonVi + ", Nhóm: " + cbo_NhomVatTu.getSelectedItem());

            // 🔔 Cập nhật bảng trong form chính
            if (parentPanel != null) {
                parentPanel.fillToTableDonViTinh();
                // Nếu có notification thêm thì gọi thêm ở đây
            }

            // Đợi thông báo hiển thị xong rồi đóng
            new Timer(700, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "❌ Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.INFO, "❌ Thêm đơn vị tính thất bại!");

            // Ghi vào bảng LICHSUHOATDONG khi thất bại
            lshdDao.saveThaoTac("Thêm thất bại", "Đơn Vị Tính", "Thêm đơn vị tính thất bại: Tên: " + tenDonVi + ", Nhóm: " + cbo_NhomVatTu.getSelectedItem());
        }
    }
    // Đặt viền đỏ cho JTextField khi có lỗi
    private void setErrorBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED)); // Gạch đỏ dưới
    }

    // Đặt lại viền mặc định cho JTextField khi nhập đúng
    private void resetBorder(JTextField field) {
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200))); // Viền xám nhạt
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_TenDonVi = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbo_NhomVatTu = new javax.swing.JComboBox<>();
        btn_them = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jLabel3.setText("Nhóm vật tư:");

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Đơn vị tính");

        jLabel2.setText("Tên đơn vị:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_TenDonVi)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbo_NhomVatTu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(btn_them)
                .addGap(18, 18, 18)
                .addComponent(btn_lamMoi)
                .addContainerGap(147, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_TenDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(cbo_NhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_them)
                    .addComponent(btn_lamMoi))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
addDonViTinh();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_lamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lamMoiActionPerformed

        lamMoi();
    }//GEN-LAST:event_btn_lamMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    // Cài đặt giao diện FlatLaf và font Roboto
    FlatRobotoFont.install();
    FlatLaf.registerCustomDefaultsSource("themes");
    UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
    FlatMacDarkLaf.setup();

    // Khởi chạy giao diện
    java.awt.EventQueue.invokeLater(() -> {
        // Tạo form chính (null hoặc JFrame giả định)
        javax.swing.JFrame parent = new javax.swing.JFrame();

        // Tạo danh sách nhóm vật tư mẫu để truyền vào
        Set<String> dsNhom = new HashSet<>();
        dsNhom.add("Khối Lượng");
        dsNhom.add("Chiều Dài");
        dsNhom.add("Thể Tích");
        dsNhom.add("Đơn Vị Đóng Gói");

        // Khởi tạo dialog và gán sự kiện
        DiaLog_DonViTinh dialog = new DiaLog_DonViTinh(parent, true, null, dsNhom);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        // Hiển thị dialog
        dialog.setVisible(true);
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_them;
    private javax.swing.JComboBox<String> cbo_NhomVatTu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txt_TenDonVi;
    // End of variables declaration//GEN-END:variables
}
