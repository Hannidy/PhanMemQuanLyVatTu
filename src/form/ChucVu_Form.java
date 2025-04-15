package form;

import dao.ChucVuDAO;
import entity.model_ChucVu;
import entity.model_VatTu;
import dao.LichSuHoatDongDAO;
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
import java.util.HashSet;
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
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Message;

public class ChucVu_Form extends TabbedForm {

    public DefaultTableModel tbl_ModelChucVu;
    private ChucVuDAO cvdao = new ChucVuDAO();
    private LichSuHoatDongDAO lshdDao = new LichSuHoatDongDAO();
    private List<model_ChucVu> list_ChucVu = new ArrayList<model_ChucVu>();
    private String selectedtenCV = "";

    private static final String LOG_FILE = "chucvu_log.txt";
    // Danh sách lưu trữ thông báo
    private List<String> actionLogs = new ArrayList<>();
    // Biến đếm số lượng thông báo
    private int notificationCount = 0;
    private static final long TWENTY_FOUR_HOURS = 24 * 60 * 60 * 1000; // 24 tiếng tính bằng milliseconds

    public ChucVu_Form() {
        initComponents();
        initcomboBox();
        addSearchFilter();
        addSearchButtonAction();
        tbl_ModelChucVu = (DefaultTableModel) tbl_chucVu.getModel();
        fillToTableChucVu();
        addBellButtonActionCV();
    }

    public void initcomboBox() {
        cbo_timKiem.addItem("Mã chức vụ");
        cbo_timKiem.addItem("Tên chức vụ");
    }

    public void fillToTableChucVu() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelChucVu.setRowCount(0);

            // Lấy danh sách vật tư từ database
            list_ChucVu = cvdao.selectAll();
            if (list_ChucVu != null) {
                for (model_ChucVu cv : list_ChucVu) {
                    tbl_ModelChucVu.addRow(new Object[]{
                        cv.getMaChucVu(), // Chỉ lấy Mã Vật Tư
                        cv.getTenChucVu(), // Chỉ lấy Tên Vật Tư                
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteChucVu() {
        int[] selectedRows = tbl_chucVu.getSelectedRows();

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " chức vụ?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>();

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String maCV = tbl_chucVu.getValueAt(row, 0).toString();
                cvdao.delete(maCV);
                danhSachXoa.add(maCV);
            }

            fillToTableChucVu();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " chức vụ!");

            // Ghi vào bảng LICHSUHOATDONG
            lshdDao.saveThaoTac("Xóa", "Chức Vụ", "Xóa " + selectedRows.length + " chức vụ, mã: " + String.join(", ", danhSachXoa));

            // Ghi log vào file (giữ nguyên)
            String log = String.format("Xóa|%s|%s|%s",
                    danhSachXoa.get(0), tbl_chucVu.getValueAt(selectedRows[0], 1).toString().trim(),
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
            writeLogToFile(log);
            notificationCount++;
            updateBellIcon();

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa chức vụ!");
        }
    }

    private void updateChucVu() {
        int row = tbl_chucVu.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        String maCV = tbl_chucVu.getValueAt(row, 0).toString();
        String tenCV = tbl_chucVu.getValueAt(row, 1).toString().trim();

        if (tenCV.isEmpty() || maCV.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        model_ChucVu cv = new model_ChucVu();
        cv.setMaChucVu(maCV);
        cv.setTenChucVu(tenCV);

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật chức vụ có mã '" + maCV + "'?");
        if (confirm) {
            try {
                cvdao.update(cv);
                fillToTableChucVu();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật chức vụ thành công!");

                // Ghi vào bảng LICHSUHOATDONG
                lshdDao.saveThaoTac("Sửa", "Chức Vụ", "Sửa thông tin chức vụ với mã " + maCV);

                // Ghi log vào file (giữ nguyên)
                String log = String.format("Cập nhật|%s|%s|%s",
                        maCV, tenCV,
                        new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()));
                writeLogToFile(log);
                notificationCount++;
                updateBellIcon();

            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật chức vụ thất bại!");
            }
        }
    }

    private void addBellButtonActionCV() {
        for (ActionListener al : btn_bell.getActionListeners()) {
            btn_bell.removeActionListener(al);
        }

        btn_bell.addActionListener(e -> {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(btn_bell), "Lịch sử hành động");
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            String[] columns = {"Hành động", "Mã chức vụ", "Tên chức vụ", "Thời gian"};
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
                if (parts.length == 4) {
                    model.addRow(new Object[]{
                        parts[0], parts[1], parts[2], parts[3]
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
            if (parts.length == 4) {
                try {
                    Date logTime = dateFormat.parse(parts[3]); // Parse thời gian từ log
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
                    case "Mã chức vụ":
                        columnIndex = 0;
                        break;
                    case "Tên chức vụ":
                        columnIndex = 1;
                        break;
                }

                if (keyword.isEmpty()) {
                    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_chucVu.getModel());
                    tbl_chucVu.setRowSorter(sorter);
                    sorter.setRowFilter(null);
                    return;
                }

                // ✅ Lọc tự động (không giới hạn mã phải theo định dạng gì)
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tbl_chucVu.getModel());
                tbl_chucVu.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
            }
        });
    }

    public void searchFilter() {
        String keyword = txt_timKiem.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl_chucVu.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl_chucVu.setRowSorter(sorter);

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
                case "Mã chức vụ":
                    columnIndex = 0;
                    break;
                case "Tên chức vụ":
                    columnIndex = 1;
                    break;
            }

            if (columnIndex == -1) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Tiêu chí tìm kiếm không hợp lệ!");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) tbl_chucVu.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tbl_chucVu.setRowSorter(sorter);

            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), columnIndex));
        });
    }

    public Set<String> getDanhSachMaChucvu() {  // Lấy danh sách chức vụ
        Set<String> dsMaLoaiCV = new HashSet<>();
        for (int i = 0; i < tbl_chucVu.getRowCount(); i++) {
            String maLoai = tbl_chucVu.getValueAt(i, 0).toString();
            dsMaLoaiCV.add(maLoai);
        }
        return dsMaLoaiCV;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_timKiem = new javax.swing.JButton();
        txt_timKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_chucVu = new javax.swing.JTable();
        btn_xoa = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();
        cbo_timKiem = new javax.swing.JComboBox<>();
        btn_bell = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Chức Vụ");

        btn_timKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/icon/search.png"))); // NOI18N

        tbl_chucVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã Chức Vụ", "Tên Chức Vụ"
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
        tbl_chucVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_chucVuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_chucVu);

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

        btn_bell.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawer/image/icon.png"))); // NOI18N

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(btn_bell))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_xoa)
                        .addComponent(btn_them)
                        .addComponent(btn_sua)
                        .addComponent(cbo_timKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_chucVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_chucVuMouseClicked
        // TODO add your handling code here:
        int selectedRow = tbl_chucVu.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String tenCV = tbl_chucVu.getValueAt(selectedRow, 1).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedtenCV = tenCV;
        }
    }//GEN-LAST:event_tbl_chucVuMouseClicked

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        deleteChucVu();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        updateChucVu();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        DiaLog_ChucVu dialog = new DiaLog_ChucVu(null, true, this);
        dialog.setdata(selectedtenCV);
        dialog.setVisible(true);
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
    private javax.swing.JButton btn_bell;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_timKiem;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbo_timKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_chucVu;
    private javax.swing.JTextField txt_timKiem;
    // End of variables declaration//GEN-END:variables
}
