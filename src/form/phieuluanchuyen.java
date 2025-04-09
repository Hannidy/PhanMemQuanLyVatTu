
package form;
import dao.TonKhoDAO;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class phieuluanchuyen extends javax.swing.JFrame {

    public phieuluanchuyen(String maVatTu, String khoXuat) {
        initComponents();
        setLocationRelativeTo(null);
        txt_KhoXuat.setText(maVatTu);
        txt_MaVatTu.setText(khoXuat);
      

        // Khóa lại không cho chỉnh sửa
        txt_MaVatTu.setEditable(false);
        txt_KhoXuat.setEditable(false);
        
        txt_MaVatTu.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                capNhatDanhSachKhoNhan(txt_MaVatTu.getText().trim(), txt_KhoXuat.getText().trim());
            }
        });

        txt_KhoXuat.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                capNhatDanhSachKhoNhan(txt_MaVatTu.getText().trim(), txt_KhoXuat.getText().trim());
            }
        });
    }
    
    public void capNhatDanhSachKhoNhan(String maVatTu, String khoXuat) {
    TonKhoDAO dao = new TonKhoDAO();
    List<String> danhSachKho = dao.layTatCaKhoChuaMaVatTu(maVatTu);

    cbo_KhoNhan.removeAllItems(); // Xóa hết

    for (String maKho : danhSachKho) {
        if (!maKho.equalsIgnoreCase(khoXuat)) {
            cbo_KhoNhan.addItem(maKho); // Chỉ add nếu khác kho xuất
        }
    }

    if (cbo_KhoNhan.getItemCount() == 0) {
        cbo_KhoNhan.addItem("Không có kho phù hợp");
    }
}

    
    
 public void chuyendoi() {
    String maVatTu = txt_MaVatTu.getText().trim();
    String khoXuat = txt_KhoXuat.getText().trim();
    String khoNhan = cbo_KhoNhan.getSelectedItem().toString().trim();

    // Kiểm tra kho nhận
    if (khoNhan.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn kho nhận!");
        return;
    }

    // Không cho chọn trùng kho xuất
    if (khoNhan.equalsIgnoreCase(khoXuat)) {
        JOptionPane.showMessageDialog(this, "Kho nhận không được trùng với kho xuất!");
        return;
    }

    // Kiểm tra số lượng
    int soLuong = 0;
    try {
        soLuong = Integer.parseInt(txt_SoLuong.getText().trim());
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
        return;
    }

    TonKhoDAO tk = new TonKhoDAO();

    // Lấy danh sách các kho có chứa mã vật tư này
    List<String> danhSachKho = tk.layCacKhoChuaMaVatTu(maVatTu, khoXuat, khoNhan);

    // Phải có đủ cả hai kho thì mới cho chuyển
    if (!danhSachKho.contains(khoXuat) || !danhSachKho.contains(khoNhan)) {
        JOptionPane.showMessageDialog(this, "Chỉ chuyển được khi cả hai kho đều có mã vật tư này!");
        return;
    }
    
    // Kiểm tra số lượng tồn kho trước khi chuyển
    boolean duSoLuong = tk.kiemTraSoLuongTruocKhiChuyen(maVatTu, khoXuat, soLuong);
    if (!duSoLuong) {
    JOptionPane.showMessageDialog(this, 
        "Kho xuất không đủ số lượng hoặc số lượng còn lại sau khi chuyển dưới 10!\n" +
        "Vui lòng kiểm tra lại tồn kho.");
    return;
}


    // Gọi chuyển hàng
    boolean thanhCong = tk.chuyenHang(maVatTu, khoXuat, khoNhan, soLuong);
    if (thanhCong) {
        JOptionPane.showMessageDialog(this, "Chuyển hàng thành công!");
    } else {
        JOptionPane.showMessageDialog(this, "Chuyển hàng thất bại!");
    }
}


     
    
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_Chuyen = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_MaVatTu = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_KhoXuat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_SoLuong = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbo_KhoNhan = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_Chuyen.setText("Chuyển");
        btn_Chuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ChuyenActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        jLabel1.setText("Phiếu luân chuyển");

        jLabel2.setText("Mã vật tư:");

        jLabel3.setText("Kho xuất:");

        jLabel4.setText("Kho nhận:");

        jLabel5.setText("Số lượng:");

        cbo_KhoNhan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "K01", "K02", "K03", "K04", "K05", "K06", "K07", "K08", "K09", "K10" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_SoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                            .addComponent(txt_MaVatTu)
                            .addComponent(txt_KhoXuat)
                            .addComponent(cbo_KhoNhan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(btn_Chuyen, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_MaVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txt_KhoXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbo_KhoNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_SoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(45, 45, 45)
                .addComponent(btn_Chuyen)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ChuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ChuyenActionPerformed
        chuyendoi();
    }//GEN-LAST:event_btn_ChuyenActionPerformed

  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Chuyen;
    private javax.swing.JComboBox<String> cbo_KhoNhan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txt_KhoXuat;
    private javax.swing.JTextField txt_MaVatTu;
    private javax.swing.JTextField txt_SoLuong;
    // End of variables declaration//GEN-END:variables
}
