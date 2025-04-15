package form;

import dao.KhoDAO;
import dao.LoaiVatTuDAO;
import dao.VatTuDAO;
import entity.model_LoaiVatTu;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import util.Message;
import raven.toast.Notifications;

public class LoaiVatTu_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelLoaiVatTu;
    private LoaiVatTuDAO lvtdao = new LoaiVatTuDAO();
    private KhoDAO kDAO = new KhoDAO();
    private VatTuDAO vtDAO = new VatTuDAO();
    private List<model_LoaiVatTu> list_LoaiVatTu = new ArrayList<model_LoaiVatTu>();
    private String selectedTenLVT = "";  // Biến để lấy dữ liệu dòng

    public LoaiVatTu_Form() {
        initComponents();
        tbl_ModelLoaiVatTu = (DefaultTableModel) tbl_loaivatTu.getModel();
        fillToTableLoaiVatTu();
        this.addSearchFilter();
        this.addSearchButtonAction();
        initcomboBox();
    }

    public void initcomboBox() {
        cbo_timKiem.addItem("Mã loại vật tư");
        cbo_timKiem.addItem("Tên loại vật tư");
    }

    public void fillToTableLoaiVatTu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelLoaiVatTu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_LoaiVatTu = lvtdao.selectAll();
            if (list_LoaiVatTu != null) {
                for (model_LoaiVatTu lvt : list_LoaiVatTu) {
                    tbl_ModelLoaiVatTu.addRow(new Object[]{
                        lvt.getMaloaivatTu(), // Chỉ lấy Mã Vật Tư
                        lvt.getTenloaivatTu(), // Chỉ lấy Tên Vật Tư
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteLoaiVatTu() {
        int[] selectedRows = tbl_loaivatTu.getSelectedRows(); // Lấy các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " Loại vật tư?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>();
            List<String> danhSachBiRangBuoc = new ArrayList<>();

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String maLoai = tbl_loaivatTu.getValueAt(row, 0).toString();

                // ✅ Kiểm tra ràng buộc ở cả Kho và Vật tư
                boolean dangDungTrongKho = kDAO.isMaLoaiDangDuocDungChoKho(maLoai);
                boolean dangDungTrongVatTu = vtDAO.isMaLoaiDangDuocDungChoVatTu(maLoai);

                if (dangDungTrongKho || dangDungTrongVatTu) {
                    String message = maLoai + " (";
                    if (dangDungTrongKho) {
                        message += "Kho";
                    }
                    if (dangDungTrongKho && dangDungTrongVatTu) {
                        message += " + ";
                    }
                    if (dangDungTrongVatTu) {
                        message += "Vật tư";
                    }
                    message += ")";
                    danhSachBiRangBuoc.add(message);
                    continue;
                }

                // Nếu không ràng buộc, tiến hành xóa
                lvtdao.delete(maLoai);
                danhSachXoa.add(maLoai);
            }

            // ✅ Thông báo kết quả
            if (!danhSachXoa.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        "Đã xóa " + danhSachXoa.size() + " loại vật tư!");
            }

            if (!danhSachBiRangBuoc.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        "Không thể xóa do đang được sử dụng: " + String.join(", ", danhSachBiRangBuoc));
            }

            fillToTableLoaiVatTu(); // Cập nhật lại bảng

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Lỗi khi xóa loại vật tư!");
            e.printStackTrace();
        }
    }

    public void updateLoaiVatTu() {
        int row = tbl_loaivatTu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        String maLVT = tbl_loaivatTu.getValueAt(row, 0).toString();
        String tenLVT = tbl_loaivatTu.getValueAt(row, 1).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenLVT.isEmpty() || maLVT.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_LoaiVatTu lvt = new model_LoaiVatTu();
        lvt.setMaloaivatTu(maLVT);
        lvt.setTenloaivatTu(tenLVT);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật loại vật tư có mã '" + maLVT + "'?");
        if (confirm) {
            try {
                lvtdao.update(lvt); // Cập nhật vào CSDL
                fillToTableLoaiVatTu(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật loại vật tư thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
//                themThongBao("Cập nhật", tenLVT);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật loại vật tư thất bại!");
            }
        }
    }

    public Set<String> getDanhSachMaLoai() {
        Set<String> dsMaLoai = new HashSet<>();
        for (int i = 0; i < tbl_loaivatTu.getRowCount(); i++) {
            String maLoai = tbl_loaivatTu.getValueAt(i, 0).toString();
            dsMaLoai.add(maLoai);
        }
        return dsMaLoai;
    }

    public void addSearchFilter() {  // Gắn một listener (trình theo dõi) vào txt_timKiem
        txt_timKiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void removeUpdate(DocumentEvent e) {
                autoSearch();
            }

            public void changedUpdate(DocumentEvent e) {
                autoSearch();
            }

            private void autoSearch() {  // Tìm kiếm theo combobox
                String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
                String keyword = txt_timKiem.getText().trim();

                int columnIndex = -1;
                switch (selectedCriteria) {
                    case "Mã loại vật tư":
                        columnIndex = 0;
                        break;
                    case "Tên loại vật tư":
                        columnIndex = 1;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_loaivatTu.getModel());
                    tbl_loaivatTu.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_loaivatTu.getModel());
                tbl_loaivatTu.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_loaivatTu.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_loaivatTu.setRowSorter(sorter);

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int i = 0; i < 3; i++) { // Cột 0: Mã vật tư, 1: Tên, 2: Mã loại
                    String value = entry.getStringValue(i).toLowerCase();
                    if (value.contains(keyword)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void addSearchButtonAction() {
        btn_timKiem.addActionListener(e -> {
            String selectedCriteria = (String) cbo_timKiem.getSelectedItem();
            String keyword = txt_timKiem.getText().trim();

            if (keyword.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            int columnIndex = -1;

            switch (selectedCriteria) {
                case "Mã loại vật tư":
                    columnIndex = 0;
                    break;
                case "Tên loại vật tư":
                    columnIndex = 1;
                    break;
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_loaivatTu.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_loaivatTu.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_loaivatTu = new javax.swing.JTable();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        cbo_timKiem = new javax.swing.JComboBox<>();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Loại Vật Tư");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_loaivatTu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Loại Vật Tư", "Tên Loại Vật Tư"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_loaivatTu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_loaivatTuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_loaivatTu);

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

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 935, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_sua)
                        .addComponent(btn_xoa)
                        .addComponent(btn_them)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deleteLoaiVatTu();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updateLoaiVatTu();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void tbl_loaivatTuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_loaivatTuMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_loaivatTu.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenLVatTu = tbl_loaivatTu.getValueAt(selectedRow, 1).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedTenLVT = tenLVatTu;
        }
    }//GEN-LAST:event_tbl_loaivatTuMouseClicked

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_LoaiVatTu dialogLVT = new DiaLog_LoaiVatTu(null, true, this);
        dialogLVT.setdata(selectedTenLVT);
        dialogLVT.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

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
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_loaivatTu;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
