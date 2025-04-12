
package form;

import dao.ChucVuDAO;
import dao.QuyenHanDAO;
import entity.model_QuyenHan;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Message;
import tabbed.TabbedForm;


public class QuyenHan_Form extends TabbedForm {
    
    public DefaultTableModel tbl_ModelQuyenHan;
    private QuyenHanDAO qhdao = new QuyenHanDAO();
    private ChucVuDAO cvDAO = new ChucVuDAO();
    private List<model_QuyenHan> list_QuyenHan = new ArrayList<model_QuyenHan>();
    
    private String selectedmaCV = "";
    private String selectedquanLy = "";
    private String selectedXem = "";
    private String selectedXuatexcel = "";
    private String selectedthem = "";
    private String selectedxoa = "";
    private String selectedsua = "";
    
            
    public QuyenHan_Form() {
        initComponents();
        tbl_ModelQuyenHan = (DefaultTableModel) tbl_QuyenHan.getModel();   
        fillToTableQuyenHan();
        initTable();
    }

    public void initTable(){
                // Cho các cột 2 → 6 là combo 0/1
        String[] binaryValues = {"0", "1"};
        for (int i = 2; i <= 6; i++) {
            setComboBoxForColumn(i, binaryValues);
        }

        // Cột 1 là danh sách quản lý (trạng thái)
        String[] quanLyOptions = {
            "Quản Lý Vật Tư" ,
            "Quản Lý Loại Vật Tư" ,
            "Quản Lý Đơn Vị Tính" ,
            "Quản Lý Vật Tư Lỗi - Bảo Hành" ,
            "Quản lý Kho", "Quản Lý Kho - Loại Vật Tư",
            "Quản Lý Tồn Kho", "Quản Lý Nhân Viên",
            "Quản Lý Chức Vụ", "Quản Lý Quyền Hạn",
            "Quản Lý Tài Khoản", "Quản Lý Phiếu Nhập",
            "Quản Lý Phiếu Yêu Cầu Vật Tư", "Quản Lý Phiếu Xuất",
            "Quản Lý Phòng Ban", "Quản Lý Nhà Cung Cấp",
            "Lịch Sử Hoạt Động"
        };
        setComboBoxForColumn(1, quanLyOptions);

    }
    
    // Gán combo box cho một cột cụ thể của bảng với danh sách giá trị truyền vào
    private void setComboBoxForColumn(int colIndex, String[] values) {
        // Tạo ComboBox chứa các giá trị truyền vào
        JComboBox<String> comboBox = new JComboBox<>(values);
        comboBox.setSelectedIndex(-1); // Không chọn mặc định ban đầu (nếu muốn)

        // Gán ComboBox làm trình chỉnh sửa (editor) cho cột được chỉ định
        TableColumn column = tbl_QuyenHan.getColumnModel().getColumn(colIndex);
        column.setCellEditor(new DefaultCellEditor(comboBox));
    }


    // hiện danh sách quyền hạn lên 
    public void fillToTableQuyenHan() {
        try {
            // Xóa toàn bộ dữ liệu cũ trước khi thêm mới
            tbl_ModelQuyenHan.setRowCount(0);

            // Lấy danh sách vật tư từ database
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
                        qh.getSua(),
                    });
                }
            }
        } catch (Exception e) { // In lỗi để dễ debug
            // In lỗi để dễ debug
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
        public void deleteQuyenhan() {
        int[] selectedRows = tbl_QuyenHan.getSelectedRows(); // Lấy tất cả các dòng được chọn

        if (selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn ít nhất một dòng để xóa!");
            return;
        }

        boolean confirm = Message.confirm("Bạn có chắc chắn muốn xóa " + selectedRows.length + " quyền hạn ?");
        if (!confirm) {
            return;
        }

        try {
            List<String> danhSachXoa = new ArrayList<>(); // Lưu các vật tư bị xóa để ghi vào thông báo

            for (int i = selectedRows.length - 1; i >= 0; i--) { // Xóa từ dưới lên để tránh lỗi chỉ số
                int row = selectedRows[i];
                String maCV = tbl_QuyenHan.getValueAt(row, 0).toString();
                cvDAO.delete(maCV); // Xóa từng vật tư
                danhSachXoa.add(maCV); // Thêm vào danh sách để ghi nhận thông báo
            }

            fillToTableQuyenHan(); // Cập nhật lại bảng sau khi xóa
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa " + selectedRows.length + " quyền hạn !");

            // 🔔 Cập nhật thông báo chuông sau khi hoàn tất tất cả các xóa
            for (String maCV : danhSachXoa) {
                //themThongBao("Xóa", maVatTu);
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không thể xóa quyền hạn !");
        }
    }
        
        
        public void updateQuyenHan() {
        int row = tbl_QuyenHan.getSelectedRow();
        if (row < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Chọn một dòng để cập nhật!");
            return;
        }

        // Lấy dữ liệu từ JTable chỉ với 3 cột
        // Lấy dữ liệu từ JTable đúng kiểu
        String maCV = tbl_QuyenHan.getValueAt(row, 0).toString();
        String Quanly = tbl_QuyenHan.getValueAt(row, 1).toString();
        int Xem = Integer.parseInt(tbl_QuyenHan.getValueAt(row, 2).toString().trim());
        int Xuetexcel = Integer.parseInt(tbl_QuyenHan.getValueAt(row, 3).toString().trim());
        int them = Integer.parseInt(tbl_QuyenHan.getValueAt(row, 4).toString().trim());
        int xoa = Integer.parseInt(tbl_QuyenHan.getValueAt(row, 5).toString().trim());
        int sua = Integer.parseInt(tbl_QuyenHan.getValueAt(row, 6).toString().trim());


        // Kiểm tra nếu có ô nào bị bỏ trống
        if (maCV.isEmpty() || Quanly.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

       // Tạo đối tượng quyền hạn mới
        model_QuyenHan qh = new model_QuyenHan();
        qh.setMachucvu(maCV);
        qh.setQuanLy(Quanly);
        qh.setXem(Xem);
        qh.setXuatexcel(Xuetexcel);
        qh.setThem(them);
        qh.setXoa(xoa);
        qh.setSua(sua);


        // Xác nhận cập nhật
        boolean confirm = Message.confirm("Bạn có chắc chắn muốn cập nhật quyền hạn có mã '" + maCV + "'?");
        if (confirm) {
            try {
                qhdao.update(qh); // Cập nhật vào CSDL
                fillToTableQuyenHan(); // Cập nhật lại bảng để hiển thị dữ liệu mới
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật quyền hạn thành công!");

                // 🔔 Ghi nhận thông báo vào hệ thống chuông
                //themThongBao("Cập nhật", tenVT);
            } catch (Exception e) {
                Message.error("Lỗi: " + e.getMessage());
                Notifications.getInstance().show(Notifications.Type.INFO, "Cập nhật quyền hạn thất bại!");
            }
        }
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
        int selectedRow = tbl_QuyenHan.getSelectedRow(); // Lấy dòng đang chọn

        if (selectedRow != -1) { // Kiểm tra có dòng được chọn không
            String maCV = tbl_QuyenHan.getValueAt(selectedRow, 0).toString();
            String quanLy = tbl_QuyenHan.getValueAt(selectedRow, 1).toString();
            String Xem = tbl_QuyenHan.getValueAt(selectedRow, 2).toString();
            String Xuatexcel = tbl_QuyenHan.getValueAt(selectedRow, 3).toString();
            String Them = tbl_QuyenHan.getValueAt(selectedRow, 4).toString();
            String Xoa = tbl_QuyenHan.getValueAt(selectedRow, 5).toString();
            String Sua = tbl_QuyenHan.getValueAt(selectedRow, 6).toString();

            // Lưu vào biến toàn cục để truyền vào JDialo
            selectedmaCV = maCV;
            selectedquanLy = quanLy;
            selectedXem = Xem;
            selectedXuatexcel = Xuatexcel;
            selectedthem = Them;
            selectedxoa = Xoa;
            selectedsua = Sua;
        }
    }//GEN-LAST:event_tbl_QuyenHanMouseClicked

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        updateQuyenHan();
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        ChucVu_Form cv = new ChucVu_Form();
        
        Set<String> dsMaLoaiCV = cv.getDanhSachMaChucvu();

        // Tạo dialog và truyền dữ liệu
        Dialog_QuyenHan dialog = new Dialog_QuyenHan(null, true, this, dsMaLoaiCV);
        dialog.setMaLoaiData(dsMaLoaiCV);
        dialog.setData(selectedmaCV, selectedquanLy, WIDTH, FRAMEBITS, WIDTH, ERROR, WIDTH);
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