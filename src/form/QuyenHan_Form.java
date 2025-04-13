
package form;

import dao.ChucVuDAO;
import dao.QuyenHanDAO;
import entity.model_QuyenHan;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Message;
import tabbed.TabbedForm;


public class QuyenHan_Form extends TabbedForm {
    
    public DefaultTableModel tbl_ModelQuyenHan;
    private QuyenHanDAO qhdao = new QuyenHanDAO();
    private ChucVuDAO cvDAO = new ChucVuDAO();
    private List<model_QuyenHan> list_QuyenHan = new ArrayList<model_QuyenHan>();
    
    private String selectedMaCV = "";
    private String selectedQuanLy = "";
    private int selectedXem = 0;
    private int selectedXuatExcel = 0;
    private int selectedThem = 0;
    private int selectedXoa = 0;
    private int selectedSua = 0;
    
            
    public QuyenHan_Form() {
        initComponents();
        tbl_ModelQuyenHan = (DefaultTableModel) tbl_QuyenHan.getModel();   
        initTable();
        initSearchComboBox();
        fillToTableQuyenHan();
        searchFilter();
        addSearchButtonAction();
    }

    private void initTable() {
        String[] binaryValues = {"0", "1"};
        for (int i = 2; i <= 6; i++) {
            setComboBoxForColumn(i, binaryValues);
        }

        String[] quanLyOptions = {
            "Quản Lý Vật Tư", "Quản Lý Loại Vật Tư", "Quản Lý Đơn Vị Tính",
            "Quản Lý Vật Tư Lỗi - Bảo Hành", "Quản Lý Kho", "Quản Lý Kho - Loại Vật Tư",
            "Quản Lý Tồn Kho", "Quản Lý Nhân Viên", "Quản Lý Chức Vụ", "Quản Lý Quyền Hạn",
            "Quản Lý Tài Khoản", "Quản Lý Phiếu Nhập", "Quản Lý Phiếu Yêu Cầu Vật Tư",
            "Quản Lý Phiếu Xuất", "Quản Lý Phòng Ban", "Quản Lý Nhà Cung Cấp",
            "Lịch Sử Hoạt Động"
        };
        setComboBoxForColumn(1, quanLyOptions);
    }
    
    private void initSearchComboBox() {
        String[] searchOptions = {
            "Mã Chức Vụ", "Quản Lý"
        };
        cbo_timKiem.setModel(new DefaultComboBoxModel<>(searchOptions));
    }
    
    private void setComboBoxForColumn(int colIndex, String[] values) {
        JComboBox<String> comboBox = new JComboBox<>(values);
        comboBox.setSelectedIndex(-1);
        TableColumn column = tbl_QuyenHan.getColumnModel().getColumn(colIndex);
        column.setCellEditor(new DefaultCellEditor(comboBox));
    }

    // hiện danh sách quyền hạn lên 
    public void fillToTableQuyenHan() {
        try {
            tbl_ModelQuyenHan.setRowCount(0);
            list_QuyenHan = qhdao.selectAll();
            if (list_QuyenHan != null) {
                for (model_QuyenHan qh : list_QuyenHan) {
                    tbl_ModelQuyenHan.addRow(new Object[]{
                        qh.getMachucvu(),
                        qh.getQuanLy(),
                        qh.getXem(),
                        qh.getXuatexcel(),
                        qh.getThem(),
                        qh.getXoa(),
                        qh.getSua()
                    });
                }
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }
    }
    
    
public void deleteQuyenhan() {
    int[] selectedRows = tbl_QuyenHan.getSelectedRows();
    if (selectedRows.length == 0) {
        Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
        return;
    }

    boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " quyền hạn?");
    if (!confirm) {
        return;
    }

    int soLuongXoaThanhCong = 0;

    try {
        for (int row : selectedRows) {
            String maCV = tbl_QuyenHan.getValueAt(row, 0) != null ? tbl_QuyenHan.getValueAt(row, 0).toString() : "";
            String quanLy = tbl_QuyenHan.getValueAt(row, 1) != null ? tbl_QuyenHan.getValueAt(row, 1).toString() : "";

            if (maCV.isEmpty() || quanLy.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        "Dữ liệu không hợp lệ tại dòng " + (row + 1) + "!");
                continue;
            }

            if (qhdao.isReferenced(maCV)) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        "Không thể xóa quyền hạn với mã " + maCV + " vì đang được sử dụng!");
                continue;
            }

            qhdao.delete(maCV, quanLy);
            soLuongXoaThanhCong++;
        }

        fillToTableQuyenHan();

        if (soLuongXoaThanhCong > 0) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    "Đã xóa thành công " + soLuongXoaThanhCong + " quyền hạn!");
        } else {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không có quyền hạn nào được xóa.");
        }
    } catch (Exception e) {
        Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi xóa quyền hạn: " + e.getMessage());
    }
}

    
 public void updateQuyenHan() {
        int row = tbl_QuyenHan.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        try {
            // Lấy giá trị ban đầu từ cơ sở dữ liệu
            String maCVBeforeEdit = tbl_QuyenHan.getValueAt(row, 0) != null ? tbl_QuyenHan.getValueAt(row, 0).toString() : "";
            String quanLyBeforeEdit = tbl_QuyenHan.getValueAt(row, 1) != null ? tbl_QuyenHan.getValueAt(row, 1).toString() : "";
            model_QuyenHan originalQH = qhdao.selectById(maCVBeforeEdit, quanLyBeforeEdit);
            if (originalQH == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Bản ghi với Mã Chức Vụ '" + maCVBeforeEdit + "' và Quản Lý '" + quanLyBeforeEdit + "' không tồn tại trong cơ sở dữ liệu!");
                return;
            }
            String originalMaCV = originalQH.getMachucvu();
            String originalQuanLy = originalQH.getQuanLy();

            // Lấy giá trị mới từ JTable
            String maCV = tbl_QuyenHan.getValueAt(row, 0) != null ? tbl_QuyenHan.getValueAt(row, 0).toString() : "";
            String quanLy = tbl_QuyenHan.getValueAt(row, 1) != null ? tbl_QuyenHan.getValueAt(row, 1).toString() : "";
            int xem = parseTableValue(tbl_QuyenHan.getValueAt(row, 2));
            int xuatExcel = parseTableValue(tbl_QuyenHan.getValueAt(row, 3));
            int them = parseTableValue(tbl_QuyenHan.getValueAt(row, 4));
            int xoa = parseTableValue(tbl_QuyenHan.getValueAt(row, 5));
            int sua = parseTableValue(tbl_QuyenHan.getValueAt(row, 6));

            if (maCV.isEmpty() || quanLy.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder("Vui lòng nhập đầy đủ thông tin: ");
                if (maCV.isEmpty()) errorMsg.append("Mã Chức Vụ, ");
                if (quanLy.isEmpty()) errorMsg.append("Quản Lý, ");
                errorMsg.setLength(errorMsg.length() - 2);
                Notifications.getInstance().show(Notifications.Type.INFO, errorMsg.toString());
                return;
            }

            if (!maCV.equals(originalMaCV) || !quanLy.equals(originalQuanLy)) {
                if (qhdao.isExist(maCV, quanLy)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Quyền hạn với Mã Chức Vụ '" + maCV + "' và Quản Lý '" + quanLy + "' đã tồn tại!");
                    return;
                }
            }

            model_QuyenHan qh = new model_QuyenHan();
            qh.setMachucvu(maCV);
            qh.setQuanLy(quanLy);
            qh.setXem(xem);
            qh.setXuatexcel(xuatExcel);
            qh.setThem(them);
            qh.setXoa(xoa);
            qh.setSua(sua);

            boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật quyền hạn có mã '" + maCV + "'?");
            if (confirm) {
                qhdao.update(qh, originalMaCV, originalQuanLy);
                // Kiểm tra xem bản ghi có thực sự được cập nhật hay không
                model_QuyenHan updatedQH = qhdao.selectById(maCV, quanLy);
                if (updatedQH != null && updatedQH.getMachucvu().equals(maCV) && updatedQH.getQuanLy().equals(quanLy)) {
                    fillToTableQuyenHan();
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật quyền hạn thành công!");
                } else {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Không thể cập nhật quyền hạn. Bản ghi không tồn tại hoặc dữ liệu không hợp lệ!");
                }
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi cập nhật quyền hạn: " + e.getMessage());
        }
    }
    
    private int parseTableValue(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
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
                case "Mã Chức Vụ":
                    columnIndex = 0;
                    break;
                case "Quản Lý":
                    columnIndex = 1;
                    break;
                
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            if (selectedCriteria.equals("Quản lý") && keyword.matches("\\d+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tên vật tư không thể là số!");
                return;
            }

            if ((selectedCriteria.equals("Mã Chức vụ") || selectedCriteria.equals("Mã loại vật tư")) && !keyword.matches("\\w+")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Mã không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_QuyenHan.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_QuyenHan.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }
    
    
        public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_QuyenHan.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_QuyenHan.setRowSorter(sorter);

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
        
        
        
        
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_QuyenHan = new javax.swing.JTable();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        btn_Them = new javax.swing.JButton();
        cbo_timKiem = new javax.swing.JComboBox<>();
        btn_Xoa = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Quyền Hạn");

        tbl_QuyenHan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Chức Vụ", "Quản Lý", "Xem", "Xuất Excel", "Thêm", "Xóa", "Sửa"
            }
        ));
        tbl_QuyenHan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_QuyenHanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_QuyenHan);

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        btn_Them.setText("Thêm");
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        btn_Sua.setText("Sửa");
        btn_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaActionPerformed(evt);
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_timKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                        .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Sua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_timKiem, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbo_timKiem)
                    .addComponent(btn_Xoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Them, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_QuyenHanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_QuyenHanMouseClicked
        int selectedRow = tbl_QuyenHan.getSelectedRow();
        if (selectedRow != -1) {
            selectedMaCV = tbl_QuyenHan.getValueAt(selectedRow, 0) != null ? tbl_QuyenHan.getValueAt(selectedRow, 0).toString() : "";
            selectedQuanLy = tbl_QuyenHan.getValueAt(selectedRow, 1) != null ? tbl_QuyenHan.getValueAt(selectedRow, 1).toString() : "";
            selectedXem = parseTableValue(tbl_QuyenHan.getValueAt(selectedRow, 2));
            selectedXuatExcel = parseTableValue(tbl_QuyenHan.getValueAt(selectedRow, 3));
            selectedThem = parseTableValue(tbl_QuyenHan.getValueAt(selectedRow, 4));
            selectedXoa = parseTableValue(tbl_QuyenHan.getValueAt(selectedRow, 5));
            selectedSua = parseTableValue(tbl_QuyenHan.getValueAt(selectedRow, 6));
        }
    }//GEN-LAST:event_tbl_QuyenHanMouseClicked

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        updateQuyenHan();
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        Set<String> dsMaLoaiCV = cvDAO.getDanhSachMaChucVu();
        Dialog_QuyenHan dialog = new Dialog_QuyenHan(null, true, this, dsMaLoaiCV);
        dialog.setMaLoaiData(dsMaLoaiCV);
        dialog.setVisible(true);
    }//GEN-LAST:event_btn_ThemActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
       deleteQuyenhan();
    }//GEN-LAST:event_btn_XoaActionPerformed

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
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_QuyenHan;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables

    

}