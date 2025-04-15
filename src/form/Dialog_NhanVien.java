
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.NhanVienDAO;
import entity.model_NhanVien;
import java.awt.Color;
import java.awt.Dimension;
import dao.LichSuHoatDongDAO;
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



public class Dialog_NhanVien extends javax.swing.JDialog {

    private DefaultTableModel tbl_ModelNhanVien;
    private NhanVienDAO nvdao = new NhanVienDAO();
    private List<model_NhanVien> list_NhanVien = new ArrayList<model_NhanVien>();
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
    private NhanVien_Form pnNhanVienRef;
    
    
    public Dialog_NhanVien(Frame parent, boolean modal, NhanVien_Form parentPanel ,Set<String> dsmaCV , Set<String> dsmaPB) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.pnNhanVienRef = parentPanel;

        Danhsachchucvu(dsmaCV);
        Danhsachphongban(dsmaPB);
        
    }
    
    public void Danhsachphongban (Set<String> dsmaPB){
        cbo_MaPB.removeAllItems();

        for (String maPhong : dsmaPB){
            cbo_MaPB.addItem(maPhong);
        }
        cbo_MaPB.revalidate(); // Làm mới lại layout của combobox
        cbo_MaPB.repaint(); // Vẽ lại giao diện combobox
    }
    
    public void Danhsachchucvu (Set<String> dsmaCV){
       cbo_MaCV.removeAllItems(); // Xóa item cũ trong combobox
        for (String maLoai : dsmaCV ) {
            cbo_MaCV.addItem(maLoai); // Thêm từng mã loại vào combobox
        }
        cbo_MaCV.revalidate(); // Làm mới lại layout của combobox
        cbo_MaCV.repaint(); // Vẽ lại giao diện combobox 
    }
    
    public void setData(String TenNV, String MaCv , String MaPB , String Email , String SDT , String Trangthai) {
        txt_TenNV.setText(TenNV);
        cbo_MaCV.setSelectedItem(MaCv);
        cbo_MaPB.setSelectedItem(MaPB);
        txt_email.setText(Email);
        txt_Sdt.setText(SDT);
        cbo_TrangThai.setSelectedItem(Trangthai);
    }
    
    // cập nhập lại dữ liệu cho combobox dsmaCV
    public void setDanhSachChucVu(Set<String> dsmaCV) { // Hàm truyền dữ liệu vào combobox
        cbo_MaCV.removeAllItems(); // Xóa dữ liệu cũ nếu có
        for (String maCV : dsmaCV) {
            cbo_MaCV.addItem(maCV);
        }
    }
     // cập nhập lại dữ liệu cho combobox dsmaCV
    public void setDanhsachPhongBan(Set<String> dsmaPB) { // Hàm truyền dữ liệu vào combobox
        cbo_MaPB.removeAllItems(); // Xóa dữ liệu cũ nếu có
        for (String maPB : dsmaPB) {
            cbo_MaPB.addItem(maPB);
        }
    }
    
     public void fillToTableNhanVien() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelNhanVien.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_NhanVien = nvdao.selectAll();
            if (list_NhanVien!= null) {
                for (model_NhanVien nv : list_NhanVien) {
                    tbl_ModelNhanVien.addRow(new Object[]{
                        nv.getMaNhanVien(),
                        nv.getTenNhanVien(),
                        nv.getMaChucVu(),
                        nv.getMaPhongBan(),
                        nv.getEmail(),
                        nv.getSoDienthoai(),
                        nv.getTrangThai()
                   });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    public void addNhanVien() {
        boolean isValid = true;

        resetBorder(txt_TenNV);
        resetBorder(txt_email);
        resetBorder(txt_Sdt);

        String TenNV = txt_TenNV.getText().trim();
        String email = txt_email.getText().trim();
        String sdt = txt_Sdt.getText().trim();

        if (TenNV.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
            if (TenNV.isEmpty()) setErrorBorder(txt_TenNV);
            if (email.isEmpty()) setErrorBorder(txt_email);
            if (sdt.isEmpty()) setErrorBorder(txt_Sdt);
            isValid = false;
        }

        if (!isValid) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đủ thông tin!");
            return;
        }

        if (nvdao.isTenNhanVienExist(TenNV)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Tên nhân viên đã tồn tại!");
            setErrorBorder(txt_TenNV);
            return;
        }

        model_NhanVien nv = new model_NhanVien();
        nv.setTenNhanVien(TenNV);
        nv.setMaChucVu((String) cbo_MaCV.getSelectedItem());
        nv.setMaPhongBan((String) cbo_MaPB.getSelectedItem());
        nv.setEmail(email);
        nv.setSoDienthoai(sdt);
        nv.setTrangThai((String) cbo_TrangThai.getSelectedItem());

        try {
            String maNV = nvdao.selectMaxId(); // Lấy mã nhân viên mới nhất
            nv.setMaNhanVien(maNV); // Gán mã vào đối tượng
            nvdao.insert(nv);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm nhân viên thành công!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Thêm", "Nhân Viên", "Thêm nhân viên mới với mã " + maNV);

            if (pnNhanVienRef != null) {
                pnNhanVienRef.fillToTableNhanVien();
            }

            new Timer(700, e -> dispose()).start();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi: " + e.getMessage());
            Notifications.getInstance().show(Notifications.Type.INFO, "Thêm nhân viên thất bại!");
        }
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_TenNV = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbo_MaCV = new javax.swing.JComboBox<>();
        cbo_MaPB = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_Sdt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbo_TrangThai = new javax.swing.JComboBox<>();
        btn_Them = new javax.swing.JButton();
        btn_LamMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nhân Viên");

        jLabel2.setText("Tên Nhân Viên");

        jLabel3.setText("Mã Chức Vụ");

        jLabel4.setText("Mã Phòng Ban");

        jLabel5.setText("Email");

        jLabel6.setText("Số Điện Thoại");

        jLabel7.setText("Trạng Thái");

        cbo_TrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đang Làm Việc", "Đã Nghỉ Việc" }));

        btn_Them.setText("Thêm");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_LamMoi.setText("Làm Mới");
        btn_LamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiActionPerformed(evt);
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
                    .addComponent(txt_TenNV)
                    .addComponent(cbo_MaCV, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_MaPB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_email)
                    .addComponent(txt_Sdt)
                    .addComponent(cbo_TrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(btn_LamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_MaCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_MaPB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Sdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_TrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_LamMoi)
                            .addComponent(btn_Them))
                        .addGap(32, 32, 32))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_LamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiActionPerformed
        pnNhanVienRef.fillToTableNhanVien();
        txt_TenNV.setText("");
        txt_email.setText("");
        txt_Sdt.setText("");
        cbo_MaCV.setSelectedItem("");
        cbo_MaPB.setSelectedItem("");
        cbo_TrangThai.setSelectedItem("");
    }//GEN-LAST:event_btn_LamMoiActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        addNhanVien();
    }//GEN-LAST:event_btn_ThemActionPerformed


    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                Frame frame = new Frame(); // hoặc ép kiểu từ JFrame nếu cần
                boolean modal = true;
                NhanVien_Form form = new NhanVien_Form(); // nếu bạn có constructor mặc định
                Set<String> set1 = new HashSet<>();
                Set<String> set2 = new HashSet<>();
                
                Dialog_NhanVien dialog = new Dialog_NhanVien(frame , modal , form , set1 , set2 );
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
    private javax.swing.JButton btn_LamMoi;
    private javax.swing.JButton btn_Them;
    private javax.swing.JComboBox<String> cbo_MaCV;
    private javax.swing.JComboBox<String> cbo_MaPB;
    private javax.swing.JComboBox<String> cbo_TrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txt_Sdt;
    private javax.swing.JTextField txt_TenNV;
    private javax.swing.JTextField txt_email;
    // End of variables declaration//GEN-END:variables
}
