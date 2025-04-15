
package form;

import dao.LichSuHoatDongDAO;
import entity.model_LichSuHoatDong;
import java.util.List;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;
import tabbed.TabbedForm;
import util.Auth;

public class LichSuHoatDong_Form extends TabbedForm {

    private LichSuHoatDongDAO dao = new LichSuHoatDongDAO();
    private DefaultTableModel tableModel;
    private Timer timer; // Timer để tự động cập nhật
    
    public LichSuHoatDong_Form() {
        initComponents();
        initTable();
        loadData();
        startAutoRefresh(); // Bắt đầu tự động cập nhật
    }
    
    // Khởi tạo JTable
    private void initTable() {
        tableModel = new DefaultTableModel(
            new String[]{"Mã Lịch Sử", "Thời Gian", "Mã Nhân Viên", "Tên Nhân Viên", 
                         "Chức Vụ", "Thao Tác", "Quản Lý", "Nội Dung"}, 0
        );
        tbl_LichSuHoatDong.setModel(tableModel);
    }

    // Tải dữ liệu vào JTable
    public void loadData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        if (Auth.daDangNhap()) {
            List<model_LichSuHoatDong> list = dao.getByMaNhanVien(Auth.getMaNhanVien());
            for (model_LichSuHoatDong lshd : list) {
                tableModel.addRow(new Object[]{
                    lshd.getMaLichSu(),
                    lshd.getThoiGian(),
                    lshd.getMaNhanVien(),
                    lshd.getTenNhanVien(),
                    lshd.getChucVu(),
                    lshd.getThaoTac(),
                    lshd.getQuanLy(),
                    lshd.getNoiDungThaoTac()
                });
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng đăng nhập để xem lịch sử hoạt động!");
        }
    }

    // Tự động cập nhật bảng mỗi 5 giây
    private void startAutoRefresh() {
        timer = new Timer(5000, e -> loadData()); // Cập nhật mỗi 5 giây
        timer.start();
    }
    
//    // Dừng tự động cập nhật khi đóng form
//    public void dispose() {
//        if (timer != null) {
//            timer.stop();
//        }
//        super.dispose();
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_LichSuHoatDong = new javax.swing.JTable();
        btn_Xoa = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        tbl_LichSuHoatDong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Lịch Sử", "Thời Gian ", "Mã Nhân Viên ", "Tên Nhân Viên ", "Chức Vụ ", "Thao Tác", "Quản Lý", "Nội Dung"
            }
        ));
        jScrollPane1.setViewportView(tbl_LichSuHoatDong);

        btn_Xoa.setText("Xóa ");
        btn_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Lịch Sử Hoạt Động");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Xoa)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(btn_Xoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaActionPerformed
        int selectedRow = tbl_LichSuHoatDong.getSelectedRow();
        if (selectedRow >= 0) {
            String maLichSu = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa bản ghi này?", "Xác nhận", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean deleted = dao.delete(maLichSu);
                if (deleted) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa thành công!");
                    loadData(); // Tải lại dữ liệu
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa thất bại!");
                }
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn một bản ghi để xóa!");
        }
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
    private javax.swing.JButton btn_Xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_LichSuHoatDong;
    // End of variables declaration//GEN-END:variables
}
