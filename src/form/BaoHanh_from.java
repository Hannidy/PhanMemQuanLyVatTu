/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import dao.VatTuLoiDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import entity.model_BaoHanh;
import util.JDBCHelper;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

public class BaoHanh_from extends javax.swing.JFrame {
    VatTuLoiDAO VTL = new VatTuLoiDAO();
    private DefaultTableModel baohanhModel;
    private List<Object[]> danhSach;
    /**
     * Creates new form BaoHanh_from
     */
    public BaoHanh_from(List<Object[]> danhSach) { 
    initComponents();
    this.danhSach = danhSach;
    setLocationRelativeTo(this);
    initTable();
    baohanhModel = (DefaultTableModel) tbl_BaoHanh.getModel();
    setData(danhSach);
    }
    
    public void initTable() {
    baohanhModel = (DefaultTableModel) tbl_BaoHanh.getModel();
    tbl_BaoHanh.setModel(baohanhModel);

    // Cập nhật tiêu đề cột
    baohanhModel.setColumnIdentifiers(new String[]{"Mã Kho", "Mã Vật Tư", "Nhà Cung Cấp", "Trạng Thái"});

    // Tạo ComboBox trạng thái
    String[] trangThaiOptions = {"Hàng lỗi", "Đang chờ duyệt", "Đã được bảo hành"};
    JComboBox<String> comboBoxTrangThai = new JComboBox<>(trangThaiOptions);

    // Gán ComboBox làm CellEditor cho cột trạng thái (index 3)
    TableColumn column = tbl_BaoHanh.getColumnModel().getColumn(3);
    column.setCellEditor(new DefaultCellEditor(comboBoxTrangThai));

    // Thêm listener để kiểm tra trạng thái
    tbl_BaoHanh.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 3) {
            int row = e.getFirstRow();
            String newStatus = tbl_BaoHanh.getValueAt(row, 3).toString();
            if (!Arrays.asList(trangThaiOptions).contains(newStatus)) {
                JOptionPane.showMessageDialog(this, "Trạng thái không hợp lệ!");
                tbl_BaoHanh.setValueAt("Hàng lỗi", row, 3); // Reset về trạng thái mặc định
            }
        }
    });
}
        public JTable getTbl_BaoHanh() {
        return tbl_BaoHanh;
    }
    
     public void setData(List<Object[]> danhSach) {
        DefaultTableModel model = (DefaultTableModel) tbl_BaoHanh.getModel();
        model.setRowCount(0);
        for (Object[] dong : danhSach) {
            if ("Đang chờ duyệt".equalsIgnoreCase(dong[4].toString())) {
                model.addRow(new Object[]{dong[0], dong[1], dong[3], dong[4]});
            }
        }
    }
public DefaultTableModel getBaoHanhTableModel() {
    return (DefaultTableModel) tbl_BaoHanh.getModel();
}
public void setData2(List<Object[]> danhSach) {
    DefaultTableModel model = (DefaultTableModel) tbl_BaoHanh.getModel();
    model.setRowCount(0);
    for (Object[] dong : danhSach) {
        if (dong.length >= 5 && "Đang chờ duyệt".equalsIgnoreCase(dong[4].toString())) {
            model.addRow(new Object[]{dong[0], dong[1], dong[3], dong[4]}); // MaKho, MaVatTu, TenNhaCungCap, TrangThai
        }
    }
}
/**
public void duyetDong(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng để duyệt.");
        return;
    }

    // Lấy dữ liệu từ dòng được chọn
    String maKho = table.getValueAt(selectedRow, 0).toString();
    String maVatTu = table.getValueAt(selectedRow, 1).toString();
    String tenNhaCungCap = table.getValueAt(selectedRow, 2).toString();
    String trangThai = "Đã được bảo hành";

    // Lấy MaNhaCungCap từ danhSachGuiBaoHanh
    String maNhaCungCap = null;
    Object[] selectedDong = null;
    List<Object[]> danhSach = VatTuLoi_BaoHanh_Form.getInstance().getDanhSachGuiBaoHanh();
    for (Object[] dong : danhSach) {
        if (dong[0].toString().equals(maKho) && dong[1].toString().equals(maVatTu) && dong[3].toString().equals(tenNhaCungCap)) {
            maNhaCungCap = dong[2].toString();
            selectedDong = dong;
            break;
        }
    }

    if (maNhaCungCap == null) {
        JOptionPane.showMessageDialog(null, "Không tìm thấy mã nhà cung cấp!");
        return;
    }

    // Cập nhật trạng thái trên giao diện
    table.setValueAt(trangThai, selectedRow, 3);

    // Lưu vào cơ sở dữ liệu
    String sql = "INSERT INTO BAOHANH (MaVatTu, MaNhaCungCap, MaKho, TenVatTu, TrangThai, NgayDuyet) VALUES (?, ?, ?, ?, ?, GETDATE())";
    try {
        // Lấy TenVatTu từ bảng VATTU
        String sqlTenVatTu = "SELECT TenVatTu FROM VATTU WHERE MaVatTu = ?";
        String tenVatTu;
        try (ResultSet rs = JDBCHelper.query(sqlTenVatTu, maVatTu)) {
            tenVatTu = rs.next() ? rs.getString("TenVatTu") : "Unknown";
        }

        JDBCHelper.update(sql, maVatTu, maNhaCungCap, maKho, tenVatTu, trangThai);
        JOptionPane.showMessageDialog(null, "Đã lưu thông tin bảo hành!");

        // Cập nhật trạng thái trong VatTuLoi_BaoHanh_Form
        VatTuLoi_BaoHanh_Form vtlForm = VatTuLoi_BaoHanh_Form.getInstance();
        if (vtlForm != null) {
            vtlForm.capNhatTrangThaiVatTu(maKho, maVatTu, "Đã được bảo hành");
        }

        // Xóa dòng khỏi danhSachGuiBaoHanh
        if (selectedDong != null) {
            danhSach.remove(selectedDong);
        }

        // Xóa dòng khỏi bảng sau 5 phút
        Timer timer = new Timer(300000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                    model.removeRow(selectedRow);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi lưu thông tin bảo hành: " + e.getMessage());
    }
}

*/
public void duyetDong(JTable table) {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng để duyệt.");
        return;
    }

    // Lấy dữ liệu từ dòng được chọn
    String maKho = table.getValueAt(selectedRow, 0).toString();
    String maVatTu = table.getValueAt(selectedRow, 1).toString();
    String tenNhaCungCap = table.getValueAt(selectedRow, 2).toString();
    String trangThai = "Đã được bảo hành";

    // Tìm MaNhaCungCap từ tbl_VatTuLoi trong VatTuLoi_BaoHanh_Form
    String maNhaCungCap = null;
    VatTuLoi_BaoHanh_Form vtlForm = VatTuLoi_BaoHanh_Form.getInstance();
    if (vtlForm != null) {
        DefaultTableModel modelVatTuLoi = (DefaultTableModel) vtlForm.getTbl_VatTuLoi().getModel();
        for (int i = 0; i < modelVatTuLoi.getRowCount(); i++) {
            String tableMaKho = modelVatTuLoi.getValueAt(i, 0).toString();
            String tableMaVatTu = modelVatTuLoi.getValueAt(i, 1).toString();
            if (tableMaKho.equals(maKho) && tableMaVatTu.equals(maVatTu)) {
                maNhaCungCap = modelVatTuLoi.getValueAt(i, 2).toString(); // Cột MaNhaCungCap (index 2)
                break;
            }
        }
    }

    // Nếu không tìm thấy MaNhaCungCap, vẫn tiếp tục (hoặc đặt giá trị mặc định nếu cần)
    if (maNhaCungCap == null) {
        maNhaCungCap = "UNKNOWN"; // Giá trị mặc định, thay đổi nếu cơ sở dữ liệu yêu cầu
        System.out.println("Không tìm thấy MaNhaCungCap cho MaKho: " + maKho + ", MaVatTu: " + maVatTu);
    }

    // Cập nhật trạng thái trên giao diện
    table.setValueAt(trangThai, selectedRow, 3);

    // Lưu vào cơ sở dữ liệu
    String sql = "INSERT INTO BAOHANH (MaVatTu, MaNhaCungCap, MaKho, TenVatTu, TrangThai) VALUES (?, ?, ?, ?, ?)";
    try {
        // Lấy TenVatTu từ bảng VATTU
        String sqlTenVatTu = "SELECT TenVatTu FROM VATTU WHERE MaVatTu = ?";
        String tenVatTu;
        try (ResultSet rs = JDBCHelper.query(sqlTenVatTu, maVatTu)) {
            tenVatTu = rs.next() ? rs.getString("TenVatTu") : "Unknown";
        }

        JDBCHelper.update(sql, maVatTu, maNhaCungCap, maKho, tenVatTu, trangThai);
        JOptionPane.showMessageDialog(null, "Đã lưu thông tin bảo hành!");

        // Cập nhật trạng thái trong VatTuLoi_BaoHanh_Form
        if (vtlForm != null) {
            vtlForm.capNhatTrangThaiVatTu(maKho, maVatTu, "Đã được bảo hành");
        }

        // Xóa dòng khỏi danhSach nếu cần
        if (danhSach != null) {
            danhSach.removeIf(dong -> dong[0].toString().equals(maKho) && dong[1].toString().equals(maVatTu));
        }

        // Xóa dòng khỏi bảng sau 5 phút
        Timer timer = new Timer(300000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                    model.removeRow(selectedRow);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi lưu thông tin bảo hành: " + e.getMessage());
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_BaoHanh = new javax.swing.JTable();
        btn_Duyet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setText("Bảo Hành");

        tbl_BaoHanh.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Kho", "Mã vật tư", "Nhà Cung Cấp", "Trạng Thái"
            }
        ));
        jScrollPane1.setViewportView(tbl_BaoHanh);

        btn_Duyet.setText("Duyệt Bảo Hành");
        btn_Duyet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DuyetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Duyet))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(14, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_Duyet)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_DuyetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DuyetActionPerformed
        duyetDong(tbl_BaoHanh);
    }//GEN-LAST:event_btn_DuyetActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(BaoHanh_from.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(BaoHanh_from.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(BaoHanh_from.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(BaoHanh_from.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new BaoHanh_from().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Duyet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_BaoHanh;
    // End of variables declaration//GEN-END:variables
}
