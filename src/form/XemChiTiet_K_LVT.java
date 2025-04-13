
package form;

import dao.KhoDAO;
import dao.LoaiVatTuDAO;
import entity.model_Kho;
import entity.model_LoaiVatTu;
import java.util.List;
import raven.toast.Notifications;

public class XemChiTiet_K_LVT extends javax.swing.JPanel {

    private KhoDAO khoDao = new KhoDAO();
    private LoaiVatTuDAO loaiVatTuDao = new LoaiVatTuDAO();
    private Kho_LoaiVatTu_Form parentForm; // Tham chiếu đến form cha

    public XemChiTiet_K_LVT(Kho_LoaiVatTu_Form parentForm) {
        this.parentForm = parentForm;
        initComponents();
        txt_MaKho.setEditable(false);
        txt_TenKho.setEditable(false);
        txt_MaLoaiVatTu.setEditable(false);
        txt_TenLoaiVatTu.setEditable(false);
    }

    public void setDetails(String maKho, String maLoaiVatTu) {
        try {
            // Điền Mã Kho và Tên Kho
            txt_MaKho.setText(maKho);
            List<model_Kho> khoList = khoDao.selectById(maKho);
            if (khoList != null && !khoList.isEmpty()) {
                txt_TenKho.setText(khoList.get(0).getTenKho());
            } else {
                txt_TenKho.setText("Không tìm thấy");
            }

            // Điền Mã Loại Vật Tư và Tên Loại Vật Tư
            txt_MaLoaiVatTu.setText(maLoaiVatTu);
            List<model_LoaiVatTu> loaiVatTuList = loaiVatTuDao.selectById(maLoaiVatTu);
            if (loaiVatTuList != null && !loaiVatTuList.isEmpty()) {
                txt_TenLoaiVatTu.setText(loaiVatTuList.get(0).getTenloaivatTu());
            } else {
                txt_TenLoaiVatTu.setText("Không tìm thấy");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi tải chi tiết: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txt_MaKho = new javax.swing.JTextField();
        txt_TenKho = new javax.swing.JTextField();
        txt_MaLoaiVatTu = new javax.swing.JTextField();
        txt_TenLoaiVatTu = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btn_QuayLai = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Chi Tiết Kho - Loại Vật Tư");

        txt_MaKho.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txt_MaKho.setPreferredSize(new java.awt.Dimension(65, 34));

        txt_TenKho.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txt_TenKho.setPreferredSize(new java.awt.Dimension(65, 34));

        txt_MaLoaiVatTu.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txt_MaLoaiVatTu.setPreferredSize(new java.awt.Dimension(65, 34));

        txt_TenLoaiVatTu.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txt_TenLoaiVatTu.setPreferredSize(new java.awt.Dimension(65, 34));

        jLabel2.setText("Mã Kho");

        jLabel3.setText("Mã Loại Vật Tư");

        jLabel4.setText("Tên Kho");

        jLabel5.setText("Tên Loại Vật Tư");

        btn_QuayLai.setBackground(new java.awt.Color(242, 242, 242));
        btn_QuayLai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_QuayLai.setText("Quay Lại");
        btn_QuayLai.setBorder(null);
        btn_QuayLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_QuayLaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_MaKho, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                    .addComponent(txt_TenKho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(58, 58, 58))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6))
                    .addComponent(txt_MaLoaiVatTu, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(txt_TenLoaiVatTu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addComponent(btn_QuayLai, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(btn_QuayLai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_MaKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_MaLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_TenKho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TenLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(203, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_QuayLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_QuayLaiActionPerformed
        parentForm.showTable();
    }//GEN-LAST:event_btn_QuayLaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_QuayLai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txt_MaKho;
    private javax.swing.JTextField txt_MaLoaiVatTu;
    private javax.swing.JTextField txt_TenKho;
    private javax.swing.JTextField txt_TenLoaiVatTu;
    // End of variables declaration//GEN-END:variables
}
