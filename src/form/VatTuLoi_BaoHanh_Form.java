
package form;

import dao.VatTuLoiDAO;
import dao.LichSuHoatDongDAO;
import entity.model_VatTuLoi;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import tabbed.TabbedForm;
import form.BaoHanh_from;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.SQLException;
import java.util.Properties;
public class VatTuLoi_BaoHanh_Form extends TabbedForm {
    private DefaultTableModel tbl_model;
    private Map<String, String> trangThaiMap = new HashMap<>();
    private BaoHanh_from baoHanhForm;
    private List<Object[]> danhSachGuiBaoHanh = new ArrayList<>();
    private static VatTuLoi_BaoHanh_Form instance;
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
    
    public VatTuLoi_BaoHanh_Form() {
        initComponents();
        instance = this;  // Lưu instance
        cbo_TimKiem.setModel(new DefaultComboBoxModel<>(new String[]{
            "Mã kho", "Mã vật tư"
        }));
        fillTable();
//        initTable();
        addSearchFilter();
        addSearchButtonAction();
    }
    
    public static VatTuLoi_BaoHanh_Form getInstance() {
    return instance;
}
 private void fillTable() {
        tbl_model = (DefaultTableModel) tbl_VatTuLoi.getModel();
        tbl_model.setRowCount(0); // Xóa sạch bảng

        try {
            VatTuLoiDAO vatTuDao = new VatTuLoiDAO();
            List<model_VatTuLoi> list = vatTuDao.selectAll(); // Lấy dữ liệu từ bảng VatTuLoi

            for (model_VatTuLoi vt : list) {
                String maVatTu = vt.getMaVatTu();
                // Lấy trạng thái từ bảng BaoHanh, nếu không có thì lấy từ VatTuLoi
                String trangThai = vatTuDao.getTrangThai(maVatTu);
                if (trangThai == null) {
                    trangThai = vt.getTrangThai(); // Mặc định từ VatTuLoi
                    // Nếu chưa có trong bảng BaoHanh, thêm bản ghi mới
                    if (!vatTuDao.exists(maVatTu)) {
                        vatTuDao.insert(maVatTu, vt.getMaNhaCungCap(), trangThai);
                    }
                }
                tbl_model.addRow(new Object[]{
                    vt.getMaKho(),
                    maVatTu,
                    vt.getMaNhaCungCap(),
                    vt.getNhaCungCap(),
                    trangThai
                });
            }

            // Thêm listener để cập nhật trạng thái vào bảng BaoHanh khi thay đổi
            tbl_model.addTableModelListener(e -> {
                if (e.getColumn() == 4) { // Cột "Trạng Thái"
                    int row = e.getFirstRow();
                    String maVatTu = tbl_model.getValueAt(row, 1).toString();
                    String trangThai = tbl_model.getValueAt(row, 4).toString();
                    try {
                        if (!vatTuDao.exists(maVatTu)) {
                            String maNhaCungCap = tbl_model.getValueAt(row, 2).toString();
                            vatTuDao.insert(maVatTu, maNhaCungCap, trangThai);
                        } else {
                            vatTuDao.updateTrangThai(maVatTu, trangThai);
                        }
                        System.out.println("Đã cập nhật trạng thái vào bảng BaoHanh: MaVatTu=" + maVatTu + ", TrangThai=" + trangThai);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái vào bảng BaoHanh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu vật tư lỗi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void getBaoHanhTableModel(BaoHanh_from baoHanhForm) {
    this.baoHanhForm = baoHanhForm;
    }
public void initTable() {
    tbl_model = (DefaultTableModel) tbl_VatTuLoi.getModel();
    tbl_VatTuLoi.setModel(tbl_model);

    // Tạo ComboBox trạng thái
    String[] trangThaiOptions = {"Hàng lỗi", "Đang chờ duyệt", "Đã được bảo hành"};
    JComboBox<String> comboBoxTrangThai = new JComboBox<>(trangThaiOptions);

    // Gán ComboBox làm CellEditor cho cột trạng thái (index 3)
    TableColumn column = tbl_VatTuLoi.getColumnModel().getColumn(4);
    column.setCellEditor(new DefaultCellEditor(comboBoxTrangThai));

    // Thêm listener để kiểm tra trạng thái
    tbl_VatTuLoi.getModel().addTableModelListener(e -> {
        if (e.getColumn() == 4) {
            int row = e.getFirstRow();
            String newStatus = tbl_VatTuLoi.getValueAt(row, 4).toString();
            if (!Arrays.asList(trangThaiOptions).contains(newStatus)) {
                JOptionPane.showMessageDialog(this, "Trạng thái không hợp lệ!");
                tbl_VatTuLoi.setValueAt("Hàng lỗi", row, 4); // Reset về trạng thái mặc định
            }
        }
    });

}

private void xoaVatTuLoi() {
        int selectedRow = tbl_VatTuLoi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vật tư để xóa!");
            return;
        }

        int modelRow = tbl_VatTuLoi.convertRowIndexToModel(selectedRow);
        String trangThai = tbl_VatTuLoi.getValueAt(selectedRow, 4).toString();
        String maVatTu = tbl_VatTuLoi.getValueAt(selectedRow, 1).toString();

        if (trangThai.equalsIgnoreCase("Đang bảo hành") || trangThai.equalsIgnoreCase("Đã được bảo hành")) {
            JOptionPane.showMessageDialog(this, "Không thể xóa vật tư đang bảo hành!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa vật tư này khỏi danh sách hiển thị?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
            model.removeRow(modelRow);

            JOptionPane.showMessageDialog(this, "Đã xóa vật tư khỏi bảng hiển thị!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Xóa", "Vật Tư Lỗi", "Xóa vật tư lỗi với mã " + maVatTu + " khỏi danh sách hiển thị");
        }
    }

public JTable getTbl_VatTuLoi() {
    return tbl_VatTuLoi;
}


public void addSearchFilter() {
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

        private void autoSearch() {
            String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
            String keyword = txt_timKiem.getText() != null ? txt_timKiem.getText().trim() : "";
            int columnIndex = -1;

            if (selectedCriteria == null) return;

            switch (selectedCriteria) {
                case "Mã kho":
                    columnIndex = 0;
                    break;
                case "Mã vật tư":
                    columnIndex = 1;
                    break;
                case "Nhà cung cấp":
                    columnIndex = 2;
                    break;
                case "Trạng thái":
                    columnIndex = 3;
                    break;
                default:
                    return;
            }

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_VatTuLoi.getModel());
            tbl_VatTuLoi.setRowSorter(sorter);
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
                return;
            }
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        }
    });
}
private void addSearchButtonAction() {
    btn_TimKiem.addActionListener(e -> {
        String selectedCriteria = (String) cbo_TimKiem.getSelectedItem();
        String keyword = txt_timKiem.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        int columnIndex = -1;
        switch (selectedCriteria) {
            case "Mã kho":
                columnIndex = 0;
                break;
            case "Mã vật tư":
                columnIndex = 1;
                break;
            case "Nhà cung cấp":
                columnIndex = 2;
                break;
            case "Trạng thái":
                columnIndex = 3;
                break;
        }

        if (columnIndex == -1) {
            JOptionPane.showMessageDialog(this, "Tiêu chí tìm kiếm không hợp lệ!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_VatTuLoi.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
    });
}
 public void guiVatTuBaoHanh() {
        DefaultTableModel modelLoi = (DefaultTableModel) tbl_VatTuLoi.getModel();
        int selectedRow = tbl_VatTuLoi.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để gửi bảo hành!");
            return;
        }

        Object maKho = modelLoi.getValueAt(selectedRow, 0);
        Object maVatTu = modelLoi.getValueAt(selectedRow, 1);
        Object maNCC = modelLoi.getValueAt(selectedRow, 2);
        Object tenNCC = modelLoi.getValueAt(selectedRow, 3);
        Object trangThai = modelLoi.getValueAt(selectedRow, 4);

        if (maKho == null || maVatTu == null || maNCC == null || tenNCC == null || trangThai == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            return;
        }

        if (!trangThai.toString().equalsIgnoreCase("Hàng lỗi")) {
            JOptionPane.showMessageDialog(this, "Chỉ gửi vật tư có trạng thái 'Hàng lỗi'");
            return;
        }

        Object[] dongMoi = new Object[]{maKho, maVatTu, maNCC, tenNCC, "Đang chờ duyệt"};
        danhSachGuiBaoHanh.add(dongMoi);
        modelLoi.setValueAt("Đang chờ duyệt", selectedRow, 4);

        System.out.println("Đã thêm vào danhSachGuiBaoHanh: " + maVatTu);

        if (baoHanhForm == null) {
            baoHanhForm = new BaoHanh_from(this.danhSachGuiBaoHanh);
        }

        baoHanhForm.setData(danhSachGuiBaoHanh);

        JOptionPane.showMessageDialog(this, "Đã gửi đi bảo hành");

        // Ghi vào bảng LICHSUHOATDONG
        lshdDao.saveThaoTac("Gửi Bảo Hành", "Vật Tư Lỗi", "Gửi bảo hành vật tư lỗi với mã " + maVatTu);
    }



public List<Object[]> getDanhSachGuiBaoHanh() {
    return danhSachGuiBaoHanh;
}

public void capNhatTrangThaiVatTu(String maKho, String maVatTu, String trangThai) {
        DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String tableMaKho = model.getValueAt(i, 0).toString();
            String tableMaVatTu = model.getValueAt(i, 1).toString();
            if (tableMaKho.equals(maKho) && tableMaVatTu.equals(maVatTu)) {
                model.setValueAt(trangThai, i, 4);
                try {
                    VatTuLoiDAO vatTuDao = new VatTuLoiDAO();
                    vatTuDao.updateTrangThai(maVatTu, trangThai);
                    System.out.println("Đã cập nhật trạng thái vật tư: MaVatTu=" + maVatTu + ", TrangThai=" + trangThai);

                    // Ghi vào bảng LICHSUHOATDONG
                    lshdDao.saveThaoTac("Cập nhật trạng thái", "Vật Tư Lỗi", "Cập nhật trạng thái vật tư lỗi với mã " + maVatTu + " thành " + trangThai);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
        }
    }


public List<Object[]> getDanhSachDangChoDuyet() {
    List<Object[]> danhSachDangChoDuyet = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) tbl_VatTuLoi.getModel();
    
    for (int i = 0; i < model.getRowCount(); i++) {
        String trangThai = model.getValueAt(i, 4).toString(); // Cột trạng thái (index 4)
        if (trangThai.equalsIgnoreCase("Đang chờ duyệt")) {
            Object[] dong = new Object[]{
                model.getValueAt(i, 0), // MaKho
                model.getValueAt(i, 1), // MaVatTu
                model.getValueAt(i, 2), // MaNhaCungCap
                model.getValueAt(i, 3), // TenNhaCungCap
                model.getValueAt(i, 4)  // TrangThai
            };
            danhSachDangChoDuyet.add(dong);
        }
    }
    
    return danhSachDangChoDuyet;
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_VatTuLoi = new javax.swing.JTable();
        btn_Gui = new javax.swing.JButton();
        btn_TimKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbo_TimKiem = new javax.swing.JComboBox<>();
        btn_Xoa = new javax.swing.JButton();

        tbl_VatTuLoi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã kho", "Mã vật tư", "Mã Nhà Cung Cấp", "Nhà Cung Cấp", "Trạng Thái"
            }
        ));
        jScrollPane1.setViewportView(tbl_VatTuLoi);

        btn_Gui.setText("Gửi");
        btn_Gui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuiActionPerformed(evt);
            }
        });

        btn_TimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Vật tư lỗi bảo hành");

        cbo_TimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã vật tư", "nhà cung cấp" }));

        btn_Xoa.setText("Xóa");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 608, Short.MAX_VALUE)
                                .addComponent(btn_Xoa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Gui)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Gui)
                            .addComponent(cbo_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Xoa))
                        .addGap(4, 4, 4))
                    .addComponent(btn_TimKiem))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuiActionPerformed
        getBaoHanhTableModel(baoHanhForm); // gán cho class hiện tại
       guiVatTuBaoHanh();
    }//GEN-LAST:event_btn_GuiActionPerformed

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        xoaVatTuLoi();
    }//GEN-LAST:event_btn_XoaActionPerformed

@Override
public boolean formClose() {
    return true;
}
    @Override
    public void formOpen() {
         System.out.println("Duy Dep Trai");
   // docTrangThaiTuFile(); // Đọc trạng thái từ file trước
    fillTable(); // Điền
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Gui;
    private javax.swing.JButton btn_TimKiem;
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JComboBox<String> cbo_TimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_VatTuLoi;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
