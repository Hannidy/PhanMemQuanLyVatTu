
package form;

import dao.KhoDAO;
import dao.Kho_LoaiVatTuDAO;
import dao.LoaiVatTuDAO;
import entity.model_Kho;
import entity.model_KhoLoaiVatTu;
import entity.model_LoaiVatTu;
import java.util.List;
import raven.toast.Notifications;
import dao.LichSuHoatDongDAO;

public class DiaLog_KhoLoaiVatTu extends javax.swing.JDialog {

private KhoDAO khoDao = new KhoDAO();
    private LoaiVatTuDAO loaiVatTuDao = new LoaiVatTuDAO();
    private Kho_LoaiVatTuDAO kltDao = new Kho_LoaiVatTuDAO();
    private Kho_LoaiVatTu_Form parentForm;
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();

    public DiaLog_KhoLoaiVatTu(java.awt.Frame parent, boolean modal, Kho_LoaiVatTu_Form parentForm) {
        super(parent, modal);
        this.parentForm = parentForm;
        initComponents();
        fillComboBoxKho();
        fillComboBoxLoaiVatTu();
        setLocationRelativeTo(null); // Căn giữa dialog
    }
    
    // Điền danh sách kho vào JComboBox
    private void fillComboBoxKho() {
        try {
            List<model_Kho> khoList = khoDao.selectAll();
            cbo_MaKho.removeAllItems();
            for (model_Kho kho : khoList) {
                cbo_MaKho.addItem(kho); // Thêm đối tượng model_Kho, sẽ hiển thị tên nhờ toString()
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi tải danh sách kho: " + e.getMessage());
        }
    }

    // Điền danh sách loại vật tư vào JComboBox
    private void fillComboBoxLoaiVatTu() {
        try {
            List<model_LoaiVatTu> loaiVatTuList = loaiVatTuDao.selectAll();
            cbo_MaLoaiVatTu.removeAllItems();
            for (model_LoaiVatTu lvt : loaiVatTuList) {
                cbo_MaLoaiVatTu.addItem(lvt); // Thêm đối tượng model_LoaiVatTu, sẽ hiển thị tên nhờ toString()
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi tải danh sách loại vật tư: " + e.getMessage());
        }
    }

    // Thêm bản ghi mới vào bảng KHO_LOAIVATTU
   private void themKhoLoaiVatTu() {
        try {
            model_Kho selectedKho = (model_Kho) cbo_MaKho.getSelectedItem();
            model_LoaiVatTu selectedLoaiVatTu = (model_LoaiVatTu) cbo_MaLoaiVatTu.getSelectedItem();

            if (selectedKho == null || selectedLoaiVatTu == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn Kho và Loại Vật Tư!");
                return;
            }

            String maKho = selectedKho.getMaKho();
            String maLoaiVatTu = selectedLoaiVatTu.getMaloaivatTu();

            if (kltDao.isExist(maKho, maLoaiVatTu)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Bản ghi đã tồn tại!");
                return;
            }

            model_KhoLoaiVatTu klt = new model_KhoLoaiVatTu();
            klt.setMaKho(maKho);
            klt.setMaLoaiVatTu(maLoaiVatTu);

            kltDao.insert(klt);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm bản ghi thành công!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Thêm", "Kho - Loại Vật Tư", "Thêm bản ghi với Mã Kho: " + maKho + ", Mã Loại Vật Tư: " + maLoaiVatTu);

            if (parentForm != null) {
                parentForm.fillToTableKhoLoaiVatTu();
            }
            this.dispose();
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi thêm bản ghi: " + e.getMessage());

            // Ghi vào bảng LICHSUHOATDONG khi thất bại
            String maKho = cbo_MaKho.getSelectedItem() != null ? ((model_Kho) cbo_MaKho.getSelectedItem()).getMaKho() : "N/A";
            String maLoaiVatTu = cbo_MaLoaiVatTu.getSelectedItem() != null ? ((model_LoaiVatTu) cbo_MaLoaiVatTu.getSelectedItem()).getMaloaivatTu() : "N/A";
            lshdDao.saveThaoTac("Thêm thất bại", "Kho - Loại Vật Tư", "Thêm bản ghi thất bại: Mã Kho: " + maKho + ", Mã Loại Vật Tư: " + maLoaiVatTu);
        }
    }

    // Làm mới các JComboBox
    private void lamMoi() {
        fillComboBoxKho();
        fillComboBoxLoaiVatTu();

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbo_MaKho = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbo_MaLoaiVatTu = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Kho - Loại Vật Tư");

        jLabel2.setText("Mã Kho");

        cbo_MaKho.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Mã Loại Vật Tư");

        cbo_MaLoaiVatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Làm Mới");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbo_MaKho, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_MaLoaiVatTu, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(43, 43, 43))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_MaKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_MaLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        themKhoLoaiVatTu();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        lamMoi();
    }//GEN-LAST:event_jButton2ActionPerformed


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
            java.util.logging.Logger.getLogger(DiaLog_KhoLoaiVatTu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DiaLog_KhoLoaiVatTu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DiaLog_KhoLoaiVatTu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DiaLog_KhoLoaiVatTu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DiaLog_KhoLoaiVatTu dialog = new DiaLog_KhoLoaiVatTu(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JComboBox<Object> cbo_MaKho;
    private javax.swing.JComboBox<Object> cbo_MaLoaiVatTu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
