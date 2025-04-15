
package form;

import dao.KhoDAO;
import dao.Kho_LoaiVatTuDAO;
import dao.LoaiVatTuDAO;
import entity.model_Kho;
import entity.model_KhoLoaiVatTu;
import entity.model_LoaiVatTu;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import dao.LichSuHoatDongDAO;

import tabbed.TabbedForm;
import util.Message;

public class Kho_LoaiVatTu_Form extends TabbedForm {

    private DefaultTableModel tbl_ModelKhoLoaiVatTu;
    private Kho_LoaiVatTuDAO kltDao = new Kho_LoaiVatTuDAO();
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
    private KhoDAO khoDao = new KhoDAO();
    private LoaiVatTuDAO loaiVatTuDao = new LoaiVatTuDAO();
    private List<model_KhoLoaiVatTu> list_KhoLoaiVatTu = new ArrayList<>();
    private List<String> listMaKho = new ArrayList<>();
    private List<String> listMaLoaiVatTu = new ArrayList<>();
    
    public Kho_LoaiVatTu_Form() {
        initComponents();
        tbl_ModelKhoLoaiVatTu = (DefaultTableModel) tbl_KhoLoaiVatTu.getModel();
        initSearchComboBox();
        fillToTableKhoLoaiVatTu();
        searchKhoLoaiVatTu();
        searchFilter();
       
    }
    
    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_KhoLoaiVatTu.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_KhoLoaiVatTu.setRowSorter(sorter);

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
                    case "Mã Kho":
                        columnIndex = 0;
                        break;
                    case "Mã Loại Vật Tư":
                        columnIndex = 1;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_KhoLoaiVatTu.getModel());
                    tbl_KhoLoaiVatTu.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_KhoLoaiVatTu.getModel());
                tbl_KhoLoaiVatTu.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }
    
    private void initSearchComboBox() {
        String[] searchOptions = {"Mã Kho", "Mã Loại Vật Tư"};
        cbo_timKiem.setModel(new DefaultComboBoxModel<>(searchOptions));
    }
    
    public void fillToTableKhoLoaiVatTu() {
        try {
            tbl_ModelKhoLoaiVatTu.setRowCount(0);
            list_KhoLoaiVatTu = kltDao.selectAll();
            if (list_KhoLoaiVatTu != null) {
                for (model_KhoLoaiVatTu klt : list_KhoLoaiVatTu) {
                    tbl_ModelKhoLoaiVatTu.addRow(new Object[]{
                        klt.getMaKho(),
                        klt.getMaLoaiVatTu()
                    });
                }
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }
    }
    
public void deleteKhoLoaiVatTu() {
        int[] selectedRows = tbl_KhoLoaiVatTu.getSelectedRows();
        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " bản ghi?");
        if (!confirm) {
            return;
        }

        boolean hasError = false;
        List<String> danhSachXoa = new ArrayList<>();

        for (int row : selectedRows) {
            try {
                Object maKhoObj = tbl_KhoLoaiVatTu.getValueAt(row, 0);
                Object maLoaiVatTuObj = tbl_KhoLoaiVatTu.getValueAt(row, 1);

                if (maKhoObj == null || maLoaiVatTuObj == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Dữ liệu không hợp lệ tại dòng " + (row + 1) + "!");
                    hasError = true;
                    continue;
                }

                String maKho = maKhoObj.toString().trim();
                String maLoaiVatTu = maLoaiVatTuObj.toString().trim();

                if (maKho.isEmpty() || maLoaiVatTu.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Mã Kho hoặc Mã Loại Vật Tư trống tại dòng " + (row + 1) + "!");
                    hasError = true;
                    continue;
                }

                kltDao.delete(maKho, maLoaiVatTu);
                danhSachXoa.add(maKho + " - " + maLoaiVatTu);
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    "Lỗi xóa bản ghi tại dòng " + (row + 1) + ": " + e.getMessage());
                hasError = true;
            }
        }

        fillToTableKhoLoaiVatTu();
        if (!hasError) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa bản ghi thành công!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Xóa", "Kho - Loại Vật Tư", "Xóa " + selectedRows.length + " bản ghi: " + String.join(", ", danhSachXoa));
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Một số bản ghi không thể xóa!");

            // Ghi vào bảng LICHSUHOATDONG khi có lỗi
            lshdDao.saveThaoTac("Xóa thất bại", "Kho - Loại Vật Tư", "Xóa " + selectedRows.length + " bản ghi thất bại, có lỗi xảy ra");
        }
    }

    public void updateKhoLoaiVatTu() {
        int row = tbl_KhoLoaiVatTu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        try {
            Object maKhoObj = tbl_KhoLoaiVatTu.getValueAt(row, 0);
            Object maLoaiVatTuObj = tbl_KhoLoaiVatTu.getValueAt(row, 1);

            if (maKhoObj == null || maLoaiVatTuObj == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Dữ liệu không hợp lệ tại dòng " + (row + 1) + "!");
                return;
            }

            String maKho = maKhoObj.toString().trim();
            String maLoaiVatTu = maLoaiVatTuObj.toString().trim();

            if (maKho.isEmpty() || maLoaiVatTu.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, 
                    "Vui lòng nhập đầy đủ Mã Kho và Mã Loại Vật Tư!");
                return;
            }

            String maKhoBeforeEdit = tbl_KhoLoaiVatTu.getValueAt(row, 0).toString().trim();
            String maLoaiVatTuBeforeEdit = tbl_KhoLoaiVatTu.getValueAt(row, 1).toString().trim();
            model_KhoLoaiVatTu originalKLT = kltDao.selectById(maKhoBeforeEdit, maLoaiVatTuBeforeEdit);
            if (originalKLT == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Bản ghi với Mã Kho '" + maKhoBeforeEdit + "' và Mã Loại Vật Tư '" + maLoaiVatTuBeforeEdit + "' không tồn tại!");
                return;
            }

            if (!maKho.equals(originalKLT.getMaKho()) || !maLoaiVatTu.equals(originalKLT.getMaLoaiVatTu())) {
                if (kltDao.isExist(maKho, maLoaiVatTu)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Bản ghi với Mã Kho '" + maKho + "' và Mã Loại Vật Tư '" + maLoaiVatTu + "' đã tồn tại!");
                    return;
                }
            }

            model_KhoLoaiVatTu klt = new model_KhoLoaiVatTu();
            klt.setMaKho(maKho);
            klt.setMaLoaiVatTu(maLoaiVatTu);

            boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật bản ghi với Mã Kho '" + maKho + "'?");
            if (confirm) {
                kltDao.update(klt, originalKLT.getMaKho(), originalKLT.getMaLoaiVatTu());
                fillToTableKhoLoaiVatTu();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật bản ghi thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Sửa", "Kho - Loại Vật Tư", "Cập nhật bản ghi: Mã Kho: " + maKho + ", Mã Loại Vật Tư: " + maLoaiVatTu);
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi cập nhật bản ghi: " + e.getMessage());

            // Ghi vào bảng LICHSUHOATDONG khi thất bại
            String maKho = tbl_KhoLoaiVatTu.getValueAt(row, 0) != null ? tbl_KhoLoaiVatTu.getValueAt(row, 0).toString() : "N/A";
            String maLoaiVatTu = tbl_KhoLoaiVatTu.getValueAt(row, 1) != null ? tbl_KhoLoaiVatTu.getValueAt(row, 1).toString() : "N/A";
            lshdDao.saveThaoTac("Sửa thất bại", "Kho - Loại Vật Tư", "Cập nhật bản ghi thất bại: Mã Kho: " + maKho + ", Mã Loại Vật Tư: " + maLoaiVatTu);
        }
    }

    public void viewDetailsKhoLoaiVatTu() {
        int row = tbl_KhoLoaiVatTu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để xem chi tiết!");
            return;
        }

        try {
            Object maKhoObj = tbl_KhoLoaiVatTu.getValueAt(row, 0);
            Object maLoaiVatTuObj = tbl_KhoLoaiVatTu.getValueAt(row, 1);

            if (maKhoObj == null || maLoaiVatTuObj == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Dữ liệu không hợp lệ tại dòng " + (row + 1) + "!");
                return;
            }

            String maKho = maKhoObj.toString().trim();
            String maLoaiVatTu = maLoaiVatTuObj.toString().trim();

            if (maKho.isEmpty() || maLoaiVatTu.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Mã Kho hoặc Mã Loại Vật Tư trống tại dòng " + (row + 1) + "!");
                return;
            }

            model_KhoLoaiVatTu klt = kltDao.selectById(maKho, maLoaiVatTu);
            if (klt != null) {
                XemChiTiet_K_LVT detailPanel = new XemChiTiet_K_LVT(this);
                detailPanel.setDetails(maKho, maLoaiVatTu);
                jScrollPane1.setViewportView(detailPanel);
            } else {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Không tìm thấy bản ghi với Mã Kho '" + maKho + "' và Mã Loại Vật Tư '" + maLoaiVatTu + "'!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi xem chi tiết: " + e.getMessage());
        }
    }

    // Phương thức này được gọi khi nhấn "Quay Lại" từ XemChiTiet_K_LVT
    public void showTable() {
        jScrollPane1.setViewportView(tbl_KhoLoaiVatTu);
        fillToTableKhoLoaiVatTu();
    }

    private void searchKhoLoaiVatTu() {
        String keyword = txt_timKiem.getText().trim();
        String column = cbo_timKiem.getSelectedItem() != null ? cbo_timKiem.getSelectedItem().toString() : "";
        if (keyword.isEmpty()) {
            fillToTableKhoLoaiVatTu();
            return;
        }

        try {
            list_KhoLoaiVatTu = kltDao.selectByKeyWord(keyword, column);
            tbl_ModelKhoLoaiVatTu.setRowCount(0);
            for (model_KhoLoaiVatTu klt : list_KhoLoaiVatTu) {
                tbl_ModelKhoLoaiVatTu.addRow(new Object[]{
                    klt.getMaKho(),
                    klt.getMaLoaiVatTu()
                });
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_KhoLoaiVatTu = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btn_XemChiTiet = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        btn_Xoa = new javax.swing.JButton();
        btn_Them = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        cbo_timKiem = new javax.swing.JComboBox<>();

        tbl_KhoLoaiVatTu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Kho", "Mã Loại Vật Tư"
            }
        ));
        jScrollPane1.setViewportView(tbl_KhoLoaiVatTu);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Kho - Loại Vật Tư");

        btn_XemChiTiet.setText("Xem Chi Tiết ");
        btn_XemChiTiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XemChiTietActionPerformed(evt);
            }
        });

        btn_Sua.setText("Sửa");
        btn_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaActionPerformed(evt);
            }
        });

        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        btn_Them.setText("Thêm");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addComponent(btn_Them)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Xoa)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Sua)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XemChiTiet)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XemChiTiet)
                    .addComponent(btn_Sua)
                    .addComponent(btn_Xoa)
                    .addComponent(btn_Them))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        deleteKhoLoaiVatTu();
    }//GEN-LAST:event_btn_XoaActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        updateKhoLoaiVatTu();
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_XemChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemChiTietActionPerformed
        viewDetailsKhoLoaiVatTu();
    }//GEN-LAST:event_btn_XemChiTietActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        DiaLog_KhoLoaiVatTu dialog = new DiaLog_KhoLoaiVatTu(null, true, this);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_ThemActionPerformed

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Sua;
    private javax.swing.JButton btn_Them;
    private javax.swing.JButton btn_XemChiTiet;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_KhoLoaiVatTu;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
