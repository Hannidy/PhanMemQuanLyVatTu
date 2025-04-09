package form;

import com.formdev.flatlaf.FlatClientProperties;
import dao.VatTuDAO;
import entity.model_VatTu;
import java.util.ArrayList;
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

public class VatTu_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelVatTu;
    private VatTuDAO vtdao = new VatTuDAO();
    private List<model_VatTu> list_VatTu = new ArrayList<model_VatTu>();

    private String selectedTenVT = "";  // Biến để lấy dữ liệu dòng
    private String selectedmaLoaiVatTu = "";  // Biến để lấy dữ liệu dòng

    public VatTu_Form() {
        initComponents();
        initSearchComboBox();
        addSearchFilter();
        addSearchButtonAction();
        tbl_ModelVatTu = (DefaultTableModel) tbl_vatTu.getModel();
        //iniSetEnabledButton();
        fillToTableVatTu();
        styleUI();
    }

    private void initSearchComboBox() {
        cbo_timKiem.removeAllItems();
        cbo_timKiem.addItem("Mã vật tư");
        cbo_timKiem.addItem("Tên vật tư");
        cbo_timKiem.addItem("Mã loại vật tư");
    }

    public void styleUI() {
        txt_timKiem.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "borderWidth:1;"
                + "focusWidth:1;"
                + "innerFocusWidth:0;");

    }
    // Hiển thị danh sách vật tư lên bảng
    public void fillToTableVatTu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelVatTu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_VatTu = vtdao.selectAll();
            if (list_VatTu != null) {
                for (model_VatTu vt : list_VatTu) {
                    tbl_ModelVatTu.addRow(new Object[]{
                        vt.getMavatTu(), // Chỉ lấy Mã Vật Tư
                        vt.getTenVatTu(), // Chỉ lấy Tên Vật Tư
                        vt.getMaloaivatTu()// Chỉ lấy Mã Loại Vật Tư
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteVatTu() {
        int[] selectedRows = tbl_vatTu.getSelectedRows(); // Lấy tất cả các dòng được chọn

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
                String maVatTu = tbl_vatTu.getValueAt(row, 0).toString();
                vtdao.delete(maVatTu); // Xóa từng vật tư
                danhSachXoa.add(maVatTu); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableVatTu(); // Cập nhật lại bảng sau khi xóa
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " vật tư!");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maVatTu : danhSachXoa) {
                //themThongBao("Xóa", maVatTu);
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa vật tư!");
        }
    }

    // Cập nhật vật tư
    public void updateVatTu() {
        int row = tbl_vatTu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        String maVT = tbl_vatTu.getValueAt(row, 0).toString();
        String tenVT = tbl_vatTu.getValueAt(row, 1).toString().trim();
        String maLoaiVT = tbl_vatTu.getValueAt(row, 2).toString().trim();

        // Kiểm tra nếu có ô nào bị bỏ trống
        if (tenVT.isEmpty() || maLoaiVT.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Tạo đối tượng Vật Tư mới
        model_VatTu vt = new model_VatTu();
        vt.setMavatTu(maVT);
        vt.setTenVatTu(tenVT);
        vt.setMaloaivatTu(maLoaiVT); // Chỉ lấy MaLoaiVatTu thay vì các trường khác

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật vật tư có mã '" + maVT + "'?");
        if (confirm) {
            try {
                vtdao.update(vt); // Cập nhật vào CSDL
                fillToTableVatTu(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật vật tư thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenVT);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật vật tư thất bại!");
            }
        }
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
                    case "Mã vật tư":
                        columnIndex = 0;
                        break;
                    case "Tên vật tư":
                        columnIndex = 1;
                        break;
                    case "Mã loại vật tư":
                        columnIndex = 2;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_vatTu.getModel());
                    tbl_vatTu.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ⚠️ Chỉ kiểm tra nếu đang tìm theo TÊN mà lại nhập toàn số (hiếm gặp)
                if (selectedCriteria.equals("Tên vật tư") && keyword.matches("\\d+")) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Tên vật tư không thể là số!");
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_vatTu.getModel());
                tbl_vatTu.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_vatTu.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_vatTu.setRowSorter(sorter);

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
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
                case "Mã vật tư":
                    columnIndex = 0;
                    break;
                case "Tên vật tư":
                    columnIndex = 1;
                    break;
                case "Mã loại vật tư":
                    columnIndex = 2;
                    break;
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            if (selectedCriteria.equals("Tên vật tư") && keyword.matches("\\d+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tên vật tư không thể là số!");
                return;
            }

            if ((selectedCriteria.equals("Mã vật tư") || selectedCriteria.equals("Mã loại vật tư")) && !keyword.matches("\\w+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_vatTu.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_vatTu.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_vatTu = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        btn_xoa = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        cbo_timKiem = new javax.swing.JComboBox<>();

        tbl_vatTu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã Vật Tư", "Tên Vật Tư", "Mã Loại Vật Tư"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_vatTu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_vatTuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_vatTu);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Vật Tư");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N
        btn_timKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timKiemActionPerformed(evt);
            }
        });

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_xoa)
                        .addComponent(btn_sua)
                        .addComponent(btn_them)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_timKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_timKiemActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        deleteVatTu();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
        updateVatTu();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        LoaiVatTu_Form lvt = new LoaiVatTu_Form();
        Set<String> dsMaLoai = lvt.getDanhSachMaLoai();

        // Tạo dialog và truyền dữ liệu
        DiaLog_VatTu dialog = new DiaLog_VatTu(null, true, this, dsMaLoai);
        dialog.setMaLoaiData(dsMaLoai);
        dialog.setData(selectedTenVT, selectedmaLoaiVatTu);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_themActionPerformed

    private void tbl_vatTuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_vatTuMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_vatTu.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenVT = tbl_vatTu.getValueAt(selectedRow, 1).toString();
            String maLoaiVatTu = tbl_vatTu.getValueAt(selectedRow, 2).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedTenVT = tenVT;
            selectedmaLoaiVatTu = maLoaiVatTu;
        }
    }//GEN-LAST:event_tbl_vatTuMouseClicked

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
    private javax.swing.JTable tbl_vatTu;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
