  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

//import DAO.DAO_PHIEUXUAT;
//import dao.DAO_KHO;
//import dao.DAO_PHIEUNHAP;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.TonKhoDAO;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.UIManager;

/**
 *
 * @author ANH KHOA
 */
public class kiemke extends javax.swing.JFrame {
  
      TonKhoDAO tk = new TonKhoDAO();
    //List<Object[]> danhSachHangHoa = DAO_KHO.kiemKeHangHoa();
                
    /**
     * Creates new form kiemke
     */                                                         
    public kiemke() {
        initComponents();
        DefaultTableModel tableModel1 = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã kho", "Mã vật tư", "Ngày kiểm kê", "Số lượng đầu kỳ", "Số lượng nhập", "Số lượng xuất", "Số lượng tồn cuối kỳ"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // chặn người dùng sửa bảng
            }
        };

        tblkiemke.setModel(tableModel1);

       
         kiemThuKiemKe();
         setLocationRelativeTo(this);
         
    } 
    private void kiemThuKiemKe() {
    List<Object[]> danhSachHangHoa = tk.kiemKeHangHoa();
    
    if (danhSachHangHoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu kiểm kê!", "Kiểm thử", JOptionPane.WARNING_MESSAGE);
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblkiemke.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    for (Object[] row : danhSachHangHoa) {
        model.addRow(row); // Thêm dữ liệu vào bảng
    }

    JOptionPane.showMessageDialog(this, "kiểm kê thành công!", "Kiểm kê", JOptionPane.INFORMATION_MESSAGE);

    }
    
    /**
     * 
     * private void kiemThuKiemKe() {
   

    if (danhSachHangHoa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu kiểm kê!", "Kiểm thử", JOptionPane.WARNING_MESSAGE);
        return;
    }

    DefaultTableModel model = (DefaultTableModel) tblkiemke.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ

    for (Object[] row : danhSachHangHoa) {
        int soLuongTonKho = Integer.parseInt(row[0].toString()); // Cột 0: Số lượng tồn kho ban đầu
        int soLuongNhap = Integer.parseInt(row[1].toString());   // Cột 1: Số lượng nhập
        int soLuongXuat = Integer.parseInt(row[2].toString());   // Cột 2: Số lượng xuất
        
        // Cập nhật số lượng tồn kho theo công thức mới
        soLuongTonKho = soLuongTonKho + soLuongNhap - soLuongXuat;

        model.addRow(new Object[]{soLuongTonKho, soLuongNhap, soLuongXuat});
    }

    JOptionPane.showMessageDialog(this, "Kiểm thử kiểm kê thành công!", "Kiểm thử", JOptionPane.INFORMATION_MESSAGE);
}

     */
public void locDuLieuTheoThoiGian() {
    DefaultTableModel model = (DefaultTableModel) tblkiemke.getModel();
    model.setRowCount(0);

    java.util.Date selectedDate = data_boloc.getDate();
    if (selectedDate != null) {
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        System.out.println("Ngày được chọn: " + sqlDate);

        List<Object[]> danhSach = tk.kiemKePhieuNhap(sqlDate);

        if (!danhSach.isEmpty()) {
            for (Object[] row : danhSach) {
                model.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu kiểm kê cho ngày đã chọn.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày.");
    }
}


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new KiemKeView().setVisible(true));
//    }

public void xemchitiet(){
      int selectedRow = tblkiemke.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần xem chi tiết.");
            return;
        }

        // Lấy mã kho và mã vật tư từ bảng
        String maKho = tblkiemke.getValueAt(selectedRow, 0).toString();
        String maVatTu = tblkiemke.getValueAt(selectedRow, 1).toString();
        String ngayKiemKe = tblkiemke.getValueAt(selectedRow, 2).toString();
        String soLuongDauKy = tblkiemke.getValueAt(selectedRow, 3).toString();
        String soLuongNhap = tblkiemke.getValueAt(selectedRow, 4).toString();
        String soLuongXuat = tblkiemke.getValueAt(selectedRow, 5).toString();
        String soLuongCuoiKy = tblkiemke.getValueAt(selectedRow, 6).toString();

        // Truy vấn tên kho và tên vật tư từ CSDL
        String tenKho = tk.getTenKho(maKho);
        String tenVatTu = tk.getTenVatTu(maVatTu);

        // Hiển thị thông tin chi tiết
        String message = String.format(
            "Tên kho: %s\nTên vật tư: %s\nNgày kiểm kê: %s\nSố lượng đầu kỳ: %s\nSố lượng nhập: %s\nSố lượng xuất: %s\nSố lượng tồn cuối kỳ: %s",
            tenKho, tenVatTu, ngayKiemKe, soLuongDauKy, soLuongNhap, soLuongXuat, soLuongCuoiKy
        );

        JOptionPane.showMessageDialog(null, message, "Chi tiết kiểm kê", JOptionPane.INFORMATION_MESSAGE);
    
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblkiemke = new javax.swing.JTable();
        BtnKiemTra = new javax.swing.JButton();
        data_boloc = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel1.setText("Kiểm kê");

        tblkiemke.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã kho", "Mã vật tư", "Ngày kiểm kê", "Số lượng đầu kỳ", "Số lượng nhập", "Số lượng xuất", "Số lượng tồn cuối kỳ"
            }
        ));
        tblkiemke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblkiemkeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblkiemke);

        BtnKiemTra.setText("Bộ lọc");
        BtnKiemTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKiemTraActionPerformed(evt);
            }
        });

        data_boloc.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                        .addComponent(data_boloc, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnKiemTra, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(data_boloc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtnKiemTra, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKiemTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKiemTraActionPerformed
        // TODO add your handling code here:
      locDuLieuTheoThoiGian();
    }//GEN-LAST:event_BtnKiemTraActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void tblkiemkeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblkiemkeMouseClicked
        // TODO add your handling code here:
                if (evt.getClickCount() == 2 && tblkiemke.getSelectedRow() != -1) {
            xemchitiet();
        }
    }//GEN-LAST:event_tblkiemkeMouseClicked

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        FlatRobotoFont.install();
//        FlatLaf.registerCustomDefaultsSource("themes");
//        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
//        FlatMacDarkLaf.setup();
//        
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
//            java.util.logging.Logger.getLogger(kiemke.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(kiemke.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(kiemke.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(kiemke.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new kiemke().setVisible(true);
//               
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnKiemTra;
    private com.toedter.calendar.JDateChooser data_boloc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblkiemke;
    // End of variables declaration//GEN-END:variables
}
