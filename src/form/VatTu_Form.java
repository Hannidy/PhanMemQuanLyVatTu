package form;

import com.formdev.flatlaf.FlatClientProperties;
import dao.VatTuDAO;
import entity.model_VatTu;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
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

    private static final String LOG_FILE = "vattu_log.txt";
    // Danh sách lưu trữ thông báo
    private List<String> actionLogs = new ArrayList<>();
    // Biến đếm số lượng thông báo
    private int notificationCount = 0;
    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000; // 24 tiếng tính bằng milliseconds

    public VatTu_Form() {
        initComponents();
        initSearchComboBox();
        addSearchFilter();
        searchFilter();
        addSearchButtonAction();
        tbl_ModelVatTu = (DefaultTableModel) tbl_vatTu.getModel();
        fillToTableVatTu();
        addBellButtonAction();
        notificationCount = readLogsFromFile().size();
        updateBellIcon();
    }

    private void initSearchComboBox() {
        cbo_timKiem.removeAllItems();
        cbo_timKiem.addItem("Mã vật tư");
        cbo_timKiem.addItem("Tên vật tư");
        cbo_timKiem.addItem("Mã loại vật tư");
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
        int rows = tbl_vatTu.getSelectedRow();
        int[] selectedRows = tbl_vatTu.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        String maVT = tbl_vatTu.getValueAt(rows, 0).toString();
        String tenVT = tbl_vatTu.getValueAt(rows, 1).toString().trim();
        String maLoaiVT = tbl_vatTu.getValueAt(rows, 2).toString().trim();

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

            String log = String.format("Xóa|%s|%s|%s|%s",
                    maVT, tenVT, maLoaiVT,
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            notificationCount++; // Tăng số thông báo
            updateBellIcon(); // Cập nhật giao diện chuông

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
        vt.setMaloaivatTu(maLoaiVT);

        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật vật tư có mã '" + maVT + "'?");
        if (confirm) {
            try {
                vtdao.update(vt); // Cập nhật vào CSDL
                fillToTableVatTu(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật vật tư thành công!");

                // 🔔 Ghi log vào file
                String log = String.format("Cập nhật|%s|%s|%s|%s",
                        maVT, tenVT, maLoaiVT,
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
                writeLogToFile(log);
                notificationCount++; // Tăng số thông báo
                updateBellIcon(); // Cập nhật giao diện chuông

            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật vật tư thất bại!");
            }
        }
    }

    private void addBellButtonAction() {
        for (ActionListener al : btn_bell.getActionListeners()) {
            btn_bell.removeActionListener(al);
        }

        btn_bell.addActionListener(e -> {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(btn_bell), "Lịch sử hành động");
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] columns = {"Hành động", "Mã vật tư", "Tên vật tư", "Mã loại vật tư", "Thời gian"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable logTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(logTable);
            dialog.add(scrollPane);

            List<String> logs = readLogsFromFile(); // Đọc và tự động xóa log cũ
            if (logs.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Chưa có hành động nào!");
                dialog.dispose();
                return;
            }

            for (String log : logs) {
                String[] parts = log.split("\\|");
                if (parts.length == 5) {
                    model.addRow(new Object[]{
                        parts[0], parts[1], parts[2], parts[3], parts[4]
                    });
                }
            }

            // Cập nhật notificationCount dựa trên số dòng log còn lại
            notificationCount = logs.size();
            updateBellIcon();

            dialog.setVisible(true);
        });
    }

    private List<String> readLogsFromFile() {
        List<String> logs = new ArrayList<>();
        List<String> logsToKeep = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date currentTime = new Date();

        // Đọc file log
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    logs.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi đọc log: " + e.getMessage());
        }

        // Lọc các log cũ hơn 24 tiếng
        for (String log : logs) {
            String[] parts = log.split("\\|");
            if (parts.length == 5) {
                try {
                    Date logTime = dateFormat.parse(parts[4]); // Parse thời gian từ log
                    long timeDiff = currentTime.getTime() - logTime.getTime();
                    if (timeDiff <= TWENTY_FOUR_HOURS) {
                        logsToKeep.add(log); // Giữ lại log nếu chưa quá 24 tiếng
                    }
                } catch (Exception e) {
                    // Nếu parse thời gian lỗi, bỏ qua dòng này
                    System.err.println("Lỗi parse thời gian log: " + log);
                }
            }
        }

        // Ghi lại file log với các dòng còn lại
        if (logs.size() != logsToKeep.size()) { // Chỉ ghi nếu có dòng bị xóa
            writeLogsToFile(logsToKeep);
        }

        return logsToKeep;
    }

    private void writeLogsToFile(List<String> logs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            for (String log : logs) {
                writer.write(log);
                writer.newLine();
            }
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
    }

    private void writeLogToFile(String log) {
        // Ghi log mới và kiểm tra xóa log cũ
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(log);
            writer.newLine();
        } catch (IOException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi ghi log: " + e.getMessage());
        }
        // Sau khi ghi log mới, kiểm tra và xóa log cũ
        readLogsFromFile();
    }

    private void updateBellIcon() {
        if (notificationCount > 0) {
            btn_bell.setToolTipText("Có " + notificationCount + " hành động");
        } else {
            btn_bell.setToolTipText("Không có hành động nào");
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
        btn_bell = new javax.swing.JButton();

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

        btn_bell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/image/icon.png"))); // NOI18N
        btn_bell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bellActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_bell))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btn_bell))
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

    private void btn_bellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bellActionPerformed

    }//GEN-LAST:event_btn_bellActionPerformed

    @Override
    public boolean formClose() {
        return true;

    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_bell;
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
