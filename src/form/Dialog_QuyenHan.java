
package form;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import dao.QuyenHanDAO;
import entity.model_QuyenHan;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

public class Dialog_QuyenHan extends javax.swing.JDialog {

    private QuyenHanDAO qhdao = new QuyenHanDAO();
    private QuyenHan_Form pnQuyenHanRef;

    public Dialog_QuyenHan(java.awt.Frame parent, boolean modal  ,QuyenHan_Form parentPanel, Set<String> dsMaLoaiCV) {
        super(parent, modal);
        initComponents();
        this.pnQuyenHanRef = parentPanel;
        this.setLocationRelativeTo(null);
        
        // Cập nhật lại combobox mã chức vụ
        cbo_maCV.removeAllItems();
        for (String maLoai : dsMaLoaiCV) {
            cbo_maCV.addItem(maLoai);
        }
        cbo_maCV.revalidate();
        cbo_maCV.repaint();

        // Mảng giá trị cho comboBox Quản Lý
        String[] quanLyOptions = {
            "Quản Lý Vật Tư", "Quản Lý Loại Vật Tư", "Quản Lý Đơn Vị Tính",
            "Quản Lý Vật Tư Lỗi - Bảo Hành", "Quản lý Kho", "Quản Lý Kho - Loại Vật Tư",
            "Quản Lý Tồn Kho", "Quản Lý Nhân Viên", "Quản Lý Chức Vụ", "Quản Lý Quyền Hạn",
            "Quản Lý Tài Khoản", "Quản Lý Phiếu Nhập", "Quản Lý Phiếu Yêu Cầu Vật Tư",
            "Quản Lý Phiếu Xuất", "Quản Lý Phòng Ban", "Quản Lý Nhà Cung Cấp",
            "Lịch Sử Hoạt Động"
        };

        // Gán dữ liệu vào cbo_QuanLy
        DefaultComboBoxModel<String> modelQuanLy = new DefaultComboBoxModel<>(quanLyOptions);
        cbo_quanLy.setModel(modelQuanLy);

        // Dữ liệu cho các comboBox dạng nhị phân 0 - 1
        String[] binaryOptions = {"0", "1"};
        DefaultComboBoxModel<String> modelBinary = new DefaultComboBoxModel<>(binaryOptions);
        cbo_Xem.setModel(modelBinary);
        cbo_Xuatexcel.setModel(modelBinary);
        cbo_Them.setModel(modelBinary);
        cbo_Xoa.setModel(modelBinary);
        cbo_Sua.setModel(modelBinary);

        // Đặt giá trị mặc định cho các JComboBox
        cbo_Xem.setSelectedIndex(0);
        cbo_Xuatexcel.setSelectedIndex(0);
        cbo_Them.setSelectedIndex(0);
        cbo_Xoa.setSelectedIndex(0);
        cbo_Sua.setSelectedIndex(0);
    }
    
    
    public void setData(String maCV, String quanLy, int xem, int xuatExcel, int them, int xoa, int sua) {
        cbo_maCV.setSelectedItem(maCV);
        cbo_quanLy.setSelectedItem(quanLy);
        cbo_Xem.setSelectedItem(String.valueOf(xem));
        cbo_Xuatexcel.setSelectedItem(String.valueOf(xuatExcel));
        cbo_Them.setSelectedItem(String.valueOf(them));
        cbo_Xoa.setSelectedItem(String.valueOf(xoa));
        cbo_Sua.setSelectedItem(String.valueOf(sua));
    }
    
    
    public void setMaLoaiData(Set<String> dsMaLoai) {
        cbo_maCV.removeAllItems();
        for (String maLoai : dsMaLoai) {
            cbo_maCV.addItem(maLoai);
        }
    }
    
    // thêm quyền hạn 
     public void addQuyenHan() {
        boolean isValid = true;
        StringBuilder errorMsg = new StringBuilder("Vui lòng nhập đầy đủ thông tin: ");

        resetBorder(cbo_maCV);
        resetBorder(cbo_quanLy);

        String maCV = cbo_maCV.getSelectedItem() != null ? cbo_maCV.getSelectedItem().toString() : "";
        String quanLy = cbo_quanLy.getSelectedItem() != null ? cbo_quanLy.getSelectedItem().toString() : "";
        int xem = parseComboValue(cbo_Xem);
        int xuatExcel = parseComboValue(cbo_Xuatexcel);
        int them = parseComboValue(cbo_Them);
        int xoa = parseComboValue(cbo_Xoa);
        int sua = parseComboValue(cbo_Sua);

        if (maCV.isEmpty()) {
            setErrorBorder(cbo_maCV);
            errorMsg.append("Mã Chức Vụ, ");
            isValid = false;
        }
        if (quanLy.isEmpty()) {
            setErrorBorder(cbo_quanLy);
            errorMsg.append("Quản Lý, ");
            isValid = false;
        }

        if (!isValid) {
            errorMsg.setLength(errorMsg.length() - 2); // Xóa dấu phẩy cuối
            Notifications.getInstance().show(Notifications.Type.WARNING, errorMsg.toString());
            return;
        }

        try {
            if (qhdao.isExist(maCV, quanLy)) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Quyền hạn đã tồn tại cho chức vụ này!");
                return;
            }

            model_QuyenHan qh = new model_QuyenHan();
            qh.setMachucvu(maCV);
            qh.setQuanLy(quanLy);
            qh.setXem(xem);
            qh.setXuatexcel(xuatExcel);
            qh.setThem(them);
            qh.setXoa(xoa);
            qh.setSua(sua);

            qhdao.insert(qh);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm quyền hạn thành công!");
            new Timer(700, e -> dispose()).start();
            if (pnQuyenHanRef != null) {
                pnQuyenHanRef.fillToTableQuyenHan();
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi thêm quyền hạn: " + ex.getMessage());
        }
    }
     
     // Lưa thêm và thêm mới 
     public void ThemMoi() {
    boolean isValid = true;
    StringBuilder errorMsg = new StringBuilder("Vui lòng nhập đầy đủ thông tin: ");

    resetBorder(cbo_maCV);
    resetBorder(cbo_quanLy);

    String maCV = cbo_maCV.getSelectedItem() != null ? cbo_maCV.getSelectedItem().toString() : "";
    String quanLy = cbo_quanLy.getSelectedItem() != null ? cbo_quanLy.getSelectedItem().toString() : "";
    int xem = parseComboValue(cbo_Xem);
    int xuatExcel = parseComboValue(cbo_Xuatexcel);
    int them = parseComboValue(cbo_Them);
    int xoa = parseComboValue(cbo_Xoa);
    int sua = parseComboValue(cbo_Sua);

    if (maCV.isEmpty()) {
        setErrorBorder(cbo_maCV);
        errorMsg.append("Mã Chức Vụ, ");
        isValid = false;
    }
    if (quanLy.isEmpty()) {
        setErrorBorder(cbo_quanLy);
        errorMsg.append("Quản Lý, ");
        isValid = false;
    }

    if (!isValid) {
        errorMsg.setLength(errorMsg.length() - 2); // Xóa dấu phẩy cuối
        Notifications.getInstance().show(Notifications.Type.WARNING, errorMsg.toString());
        return;
    }

    try {
        if (qhdao.isExist(maCV, quanLy)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Quyền hạn đã tồn tại cho chức vụ này!");
            return;
        }

        model_QuyenHan qh = new model_QuyenHan();
        qh.setMachucvu(maCV);
        qh.setQuanLy(quanLy);
        qh.setXem(xem);
        qh.setXuatexcel(xuatExcel);
        qh.setThem(them);
        qh.setXoa(xoa);
        qh.setSua(sua);

        qhdao.insert(qh);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm quyền hạn thành công!");

        if (pnQuyenHanRef != null) {
            pnQuyenHanRef.fillToTableQuyenHan();
        }

        // Reset form để thêm tiếp
        resetFields();

    } catch (Exception ex) {
        Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi thêm quyền hạn: " + ex.getMessage());
    }
}

     // làm mới cbo
     public void resetFields() {
        cbo_maCV.setSelectedIndex(0);
        cbo_quanLy.setSelectedIndex(0);
        cbo_Xem.setSelectedIndex(0);
        cbo_Xuatexcel.setSelectedIndex(0);
        cbo_Them.setSelectedIndex(0);
        cbo_Xoa.setSelectedIndex(0);
        cbo_Sua.setSelectedIndex(0);

        cbo_maCV.requestFocus();
}


   // Hàm hỗ trợ lấy giá trị int từ ComboBox
       private int parseComboValue(JComboBox<?> comboBox) {
        Object selected = comboBox.getSelectedItem();
        if (selected != null) {
            try {
                return Integer.parseInt(selected.toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private void setErrorBorder(JComboBox<?> comboBox) {
        comboBox.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED));
    }

    private void resetBorder(JComboBox<?> comboBox) {
        comboBox.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY));
    }




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cbo_maCV = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbo_quanLy = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbo_Xem = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cbo_Xuatexcel = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cbo_Them = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cbo_Xoa = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbo_Sua = new javax.swing.JComboBox<>();
        btn_Them = new javax.swing.JButton();
        btn_ThemMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quyền Hạn");

        cbo_maCV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Mã Chức Vụ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Quản Lý");

        cbo_quanLy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Xem");

        cbo_Xem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Xuất Excel");

        cbo_Xuatexcel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Thêm");

        cbo_Them.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Xóa");

        cbo_Xoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Sửa");

        cbo_Sua.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btn_Them.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_Them.setText("Thêm và Lưa");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_ThemMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_ThemMoi.setText("Thêm Mới ");
        btn_ThemMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemMoiActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cbo_maCV, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_quanLy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_Xem, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_Xuatexcel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_Them, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_Xoa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbo_Sua, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(btn_Them)
                .addGap(30, 30, 30)
                .addComponent(btn_ThemMoi)
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_maCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_quanLy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_Xem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_Xuatexcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_Them, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Them)
                    .addComponent(btn_ThemMoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        addQuyenHan();
    }//GEN-LAST:event_btn_ThemActionPerformed

    private void btn_ThemMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemMoiActionPerformed
       ThemMoi();
    }//GEN-LAST:event_btn_ThemMoiActionPerformed


   public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_QuyenHan dialog = new Dialog_QuyenHan(new javax.swing.JFrame(), true, null, null);
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
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_ThemMoi;
    private javax.swing.JComboBox<String> cbo_Sua;
    private javax.swing.JComboBox<String> cbo_Them;
    private javax.swing.JComboBox<String> cbo_Xem;
    private javax.swing.JComboBox<String> cbo_Xoa;
    private javax.swing.JComboBox<String> cbo_Xuatexcel;
    private javax.swing.JComboBox<String> cbo_maCV;
    private javax.swing.JComboBox<String> cbo_quanLy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    // End of variables declaration//GEN-END:variables
}
