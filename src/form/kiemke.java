package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dao.TonKhoDAO;
import java.awt.Font;
import java.util.Date;
import javax.swing.UIManager;

public class kiemke extends javax.swing.JFrame {

    private DefaultTableModel tableModel1;

    public kiemke() {
        initComponents();
        tableModel1 = (DefaultTableModel) tblkiemke.getModel();
        kiemThuKiemKe();
        setLocationRelativeTo(this);
    }

    private void kiemThuKiemKe() {
        List<Object[]> danhSachHangHoa = TonKhoDAO.kiemKeHangHoa();

        if (danhSachHangHoa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu kiểm kê!", "Kiểm kê", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel1.setRowCount(0); // Xóa dữ liệu cũ

        for (Object[] row : danhSachHangHoa) {
            tableModel1.addRow(new Object[]{
                row[0], // MaKho
                row[1], // MaVatTu
                row[2], // NgayNhap (có thể null)
                row[3], // SoLuongDauKy
                row[4], // SoLuongNhap
                row[5], // SoLuongXuat
                row[6] // SoLuongTonCuoiKy
            });
        }

        JOptionPane.showMessageDialog(this, "Kiểm kê thành công!", "Kiểm kê", JOptionPane.INFORMATION_MESSAGE);
    }

    public void locDuLieuTheoThoiGian() {
        tableModel1.setRowCount(0);

        java.util.Date selectedDate = data_boloc.getDate();
        if (selectedDate != null) {
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
            System.out.println("Ngày được chọn: " + sqlDate);

            List<Object[]> danhSach = TonKhoDAO.kiemKePhieuNhap(sqlDate);

            if (!danhSach.isEmpty()) {
                for (Object[] row : danhSach) {
                    tableModel1.addRow(new Object[]{
                        row[0], // MaKho
                        row[1], // MaVatTu
                        row[2], // NgayNhap
                        row[3], // SoLuongDauKy
                        row[4], // SoLuongNhap
                        row[5], // SoLuongXuat
                        row[6] // SoLuongTonCuoiKy
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu kiểm kê cho ngày đã chọn.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày.");
        }
    }

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
                "Mã kho", "Mã vật tư", "Ngày nhập", "Số lượng đầu kỳ", "Số lượng nhập", "Số lượng xuất", "Số lượng tồn cuối kỳ"
            }
        ));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 379, Short.MAX_VALUE)
                        .addComponent(data_boloc, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BtnKiemTra, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(data_boloc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BtnKiemTra)))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKiemTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKiemTraActionPerformed
    locDuLieuTheoThoiGian();
    }//GEN-LAST:event_BtnKiemTraActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    dispose();
    }//GEN-LAST:event_formWindowClosing

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnKiemTra;
    private com.toedter.calendar.JDateChooser data_boloc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblkiemke;
    // End of variables declaration//GEN-END:variables
}
