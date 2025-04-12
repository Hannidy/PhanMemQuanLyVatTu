
package form;

import javax.swing.ImageIcon;
import tabbed.TabbedForm;

public class ThongTinCaNhan_Form extends TabbedForm {

    public ThongTinCaNhan_Form() {
        initComponents();
        loadThongTinCaNhan();
    }
    private void loadThongTinCaNhan() {
    lblTen.setText(util.Auth.getTenNhanVien());
    lblChucVu.setText(util.Auth.getTenChucVu());
    lblEmail.setText(util.Auth.getEmail());
    lblSDT.setText(util.Auth.getSoDienThoai());
    lblPhongBan.setText(util.Auth.getTenPhongBan());
    lblChucVu.setText(util.Auth.getTenChucVu());


    // Nếu có hiển thị hình ảnh
    if (util.Auth.getHinhAnh() != null) {
        ImageIcon icon = new ImageIcon("src/images/" + util.Auth.getHinhAnh());
        lblAvatar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
    }
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblTen = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblChucVu = new javax.swing.JLabel();
        lblPhongBan = new javax.swing.JLabel();
        lblAvatar = new javax.swing.JLabel();
        lblTen1 = new javax.swing.JLabel();
        lblEmail1 = new javax.swing.JLabel();
        lblSDT1 = new javax.swing.JLabel();
        lblChucVu1 = new javax.swing.JLabel();
        lblPhongBan1 = new javax.swing.JLabel();
        lblAvatar1 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thông Tin Cá Nhân");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblTen.setText("Tên người dùng");

        lblEmail.setText("Email");

        lblSDT.setText("Số Điện Thoại ");

        lblChucVu.setText("Chức vụ");

        lblPhongBan.setText("Phòng Ban");

        lblAvatar.setText("Trống");

        lblTen1.setText("Tên người dùng");

        lblEmail1.setText("Email");

        lblSDT1.setText("Số Điện Thoại ");

        lblChucVu1.setText("Chức vụ");

        lblPhongBan1.setText("Phòng Ban");

        lblAvatar1.setText("Hình ảnh");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblAvatar1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTen1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(lblEmail1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSDT1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblChucVu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPhongBan1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblTen, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                        .addComponent(lblEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblChucVu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPhongBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTen1)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmail1)
                        .addGap(18, 18, 18)
                        .addComponent(lblSDT1)
                        .addGap(18, 18, 18)
                        .addComponent(lblChucVu1)
                        .addGap(18, 18, 18)
                        .addComponent(lblPhongBan1)
                        .addGap(70, 70, 70)
                        .addComponent(lblAvatar1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTen)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmail)
                        .addGap(18, 18, 18)
                        .addComponent(lblSDT)
                        .addGap(18, 18, 18)
                        .addComponent(lblChucVu)
                        .addGap(18, 18, 18)
                        .addComponent(lblPhongBan)
                        .addGap(70, 70, 70)
                        .addComponent(lblAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(255, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblAvatar1;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblChucVu1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmail1;
    private javax.swing.JLabel lblPhongBan;
    private javax.swing.JLabel lblPhongBan1;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblSDT1;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblTen1;
    // End of variables declaration//GEN-END:variables
}
