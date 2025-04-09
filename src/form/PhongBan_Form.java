package form;

import dao.PhongBanDAO;
import entity.model_PhongBan;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tabbed.TabbedForm;
import raven.toast.Notifications;
import util.Message;

public class PhongBan_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelPhongBan;
    public PhongBanDAO pbdao = new PhongBanDAO();
    public List<model_PhongBan> list_PB = new ArrayList<model_PhongBan>();

    private String selectedTenPhongBan = "";
    private String selectedDiaChi = "";
    private String selectedMatruongPhong = "";

    public PhongBan_Form() {
        initComponents();
        tbl_ModelPhongBan = (DefaultTableModel) tbl_phongBan.getModel();
        fillToTablePhongBan();
    }

    public void fillToTablePhongBan() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelPhongBan.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_PB = pbdao.selectAll();
            if (list_PB != null) {
                for (model_PhongBan pb : list_PB) {
                    tbl_ModelPhongBan.addRow(new Object[]{
                        pb.getMaphongBan(), // Chỉ lấy Mã Vật Tư
                        pb.getTenphongBan(), // Chỉ lấy Tên Vật Tư
                        pb.getDiaChi(), // Chỉ lấy Mã Loại Vật Tư
                        pb.getMatruongPhong()
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletePhongBan() {
        int[] selectedRows = tbl_phongBan.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " vật tư?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                String maPhongBan = tbl_phongBan.getValueAt(row, 0).toString();
                pbdao.delete(maPhongBan); // Xóa từng vật tư
                danhSachXoa.add(maPhongBan); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTablePhongBan(); // Cập nhật lại bảng sau khi xóa
            //thongke();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " vật tư!");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maPhongBan : danhSachXoa) {
                //themThongBao("Xóa", maPhongBan);
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa vật tư!");
        }
    }

    public void updatePhongBan() {
        int row = tbl_phongBan.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        String maPhongBan = tbl_phongBan.getValueAt(row, 0).toString();
        String tenPhongBan = tbl_phongBan.getValueAt(row, 1).toString().trim();
        String diaChi = tbl_phongBan.getValueAt(row, 2).toString().trim();
        String matruongPhong = tbl_phongBan.getValueAt(row, 3).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenPhongBan.isEmpty() || diaChi.isEmpty() || matruongPhong.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_PhongBan pb = new model_PhongBan();
        pb.setMaphongBan(maPhongBan);
        pb.setTenphongBan(tenPhongBan);
        pb.setDiaChi(diaChi);
        pb.setMatruongPhong(matruongPhong);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật vật tư có mã '" + maPhongBan + "'?");
        if (confirm) {
            try {
                pbdao.update(pb); // Cập nhật vào CSDL
                fillToTablePhongBan(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật vật tư thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenPhongBan);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật vật tư thất bại!");
            }
        }
    }
    
    public Set<String> getDanhSachMaPhongBan() {
        Set<String> dsMaLoai = new HashSet<>();
        for (int i = 0; i < tbl_phongBan.getRowCount(); i++) {
            String maLoai = tbl_phongBan.getValueAt(i, 0).toString();
            dsMaLoai.add(maLoai);
        }
        return dsMaLoai;
    }
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_phongBan = new javax.swing.JTable();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Phòng Ban");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_phongBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã Phòng Ban", "Tên Phòng Ban", "Địa Chỉ ", "Mã Trưởng Phòng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_phongBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_phongBanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_phongBan);

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                        .addComponent(btn_them)
                        .addGap(18, 18, 18)
                        .addComponent(btn_xoa)
                        .addGap(18, 18, 18)
                        .addComponent(btn_sua)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_xoa)
                                .addComponent(btn_sua)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_them)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_phongBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_phongBanMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_phongBan.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenPhongBan = tbl_phongBan.getValueAt(selectedRow, 1).toString();
            String DiaChi = tbl_phongBan.getValueAt(selectedRow, 2).toString();
            String maTP = tbl_phongBan.getValueAt(selectedRow, 3).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedTenPhongBan = tenPhongBan;
            selectedDiaChi = DiaChi;
            selectedMatruongPhong = maTP;
        }
    }//GEN-LAST:event_tbl_phongBanMouseClicked

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_PhongBan dialog = new DiaLog_PhongBan(null, true, this);
        dialog.setData(selectedTenPhongBan, selectedDiaChi, selectedMatruongPhong);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deletePhongBan();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updatePhongBan();
    }//GEN-LAST:event_btn_suaActionPerformed

    @Override
    public boolean formClose() {
        return true;

    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_phongBan;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
