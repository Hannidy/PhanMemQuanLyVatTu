
package form;

import dao.ChiTietPhieuXuatDAO;
import dao.PhieuXuatDAO;
import entity.model_ChiTietPhieuXuat;
import entity.model_PhieuXuat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tabbed.TabbedForm;
import java.text.ParseException; 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import util.JDBCHelper;
import util.Message;

public class PhieuXuat_Form extends TabbedForm {
private PhieuXuatDAO pxdao = new PhieuXuatDAO();

private Map<String, List<String>> nhomVatTuMap;
    private Map<String, Integer> donViTinhMap;
private PhieuXuatDAO phieuXuatDAO = new PhieuXuatDAO();  // Khai báo DAO cho PhieuXuat

private ChiTietPhieuXuatDAO chiTietPhieuXuatDAO;
private DefaultTableModel tbl_ModelChiTietPhieuXuatModel;


  public PhieuXuat_Form() {
    initComponents();
   chiTietPhieuXuatDAO = new ChiTietPhieuXuatDAO();

    
    // Khởi tạo DefaultTableModel cho bảng Chi Tiết Phiếu Xuất
    tbl_ModelChiTietPhieuXuatModel = new DefaultTableModel(
            
        new Object[][]{},  // Mảng dữ liệu trống
        new String[] {
            "Mã Phiếu Xuất", "Mã Vật Tư", "Đơn Vị Gốc", "Đơn Vị Chuyển Đổi", "Số Lượng Xuất", "Số Lượng Quy Đổi"
        }
    );
    
    // Liên kết DefaultTableModel với JTable
    tbl_ModelChiTietPhieuXuat.setModel(tbl_ModelChiTietPhieuXuatModel);

    // Gọi phương thức để điền dữ liệu vào bảng
    fillToTableChiTietPhieuXuat();
    fillToTablePhieuXuat();
   
    
    
    
     loadNhomVatTu(); // Khởi tạo các nhóm vật tư
    resetFormPhieuXuat();
    setDefaultNgayXuat();
    setCalendarToVietnamese();
    
    
    addEventHandlers();
    initEventHandlers();
}

    
  private void fillToTablePhieuXuat() {
    DefaultTableModel model = (DefaultTableModel) tbl_PhieuXuat.getModel();
    model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

    try {
        // Lấy danh sách phiếu xuất từ database thông qua DAO
        List<model_PhieuXuat> list_PhieuXuat = pxdao.selectAll();  // Gọi phương thức selectAll() trong DAO để lấy dữ liệu

        if (list_PhieuXuat == null || list_PhieuXuat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Không có phiếu xuất nào trong CSDL.");
            return;
        }

        // Định dạng ngày
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        // Duyệt qua tất cả các phiếu xuất và thêm vào bảng
        for (model_PhieuXuat px : list_PhieuXuat) {
            String ngayChungTuFormatted = sdf.format(px.getNgayChungTu());
            String ngayXuatFormatted = sdf.format(px.getNgayXuat());

            // Cập nhật bảng
            Object[] row = new Object[] {
                px.getMaPhieuXuat(),
                ngayChungTuFormatted,
                ngayXuatFormatted,
                px.getMaKho(),
                px.getMaPhongBanYeuCau(),
                px.getMaNguoiXuat(),
                px.getMaNguoiNhan(),
                px.getTrangThai()
            };

            model.addRow(row);  // Thêm dòng vào bảng
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi tải danh sách phiếu xuất: " + e.getMessage());
    }
}



    
    


// MouseClickedPhieuXuat
private void MouseClickedPhieuXuat(java.awt.event.MouseEvent evt) {
    int row = tbl_PhieuXuat.getSelectedRow(); // Lấy chỉ số dòng được chọn trong bảng
    
    if (row != -1) { // Kiểm tra nếu có dòng được chọn
        // Lấy giá trị từ bảng theo chỉ số cột và dòng được chọn
        String maPhieuXuat = tbl_PhieuXuat.getValueAt(row, 0).toString(); // Cột 0: Mã Phiếu Xuất
        String ngayChungTu = tbl_PhieuXuat.getValueAt(row, 1).toString(); // Cột 1: Ngày Chứng Từ
        String ngayXuat = tbl_PhieuXuat.getValueAt(row, 2).toString(); // Cột 2: Ngày Xuất
        String maKho = tbl_PhieuXuat.getValueAt(row, 3).toString(); // Cột 3: Mã Kho
        String maPBYC = tbl_PhieuXuat.getValueAt(row, 4).toString(); // Cột 4: Mã PB Yêu Cầu
        String maNguoiXuat = tbl_PhieuXuat.getValueAt(row, 5).toString(); // Cột 5: Mã Người Xuất
        String maNguoiNhan = tbl_PhieuXuat.getValueAt(row, 6).toString(); // Cột 6: Mã Người Nhận
        String trangThai = tbl_PhieuXuat.getValueAt(row, 7).toString(); // Cột 7: Trạng Thái
        
        // Fill dữ liệu vào các trường nhập liệu
        txt_MaPhieuXuat.setText(maPhieuXuat); // Điền mã phiếu xuất vào trường txt_MaPhieuXuat
        
        try {
            // Định dạng ngày tháng cho Ngày Chứng Từ
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Định dạng ngày chuẩn
            Date ngayChungTuDate = sdf.parse(ngayChungTu); // Parse Ngày Chứng Từ
            txt_NgayChungTu.setDate(ngayChungTuDate); // Điền Ngày Chứng Từ vào JDateChooser
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi định dạng Ngày Chứng Từ.");
        }
        
        try {
            // Kiểm tra nếu giá trị ngày Xuất có hợp lệ
            if (!ngayXuat.isEmpty() && !ngayXuat.equals("null")) { // Kiểm tra xem ngày có giá trị hợp lệ
                // Định dạng ngày tháng cho Ngày Xuất
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Định dạng ngày chuẩn
                Date ngayXuatDate = sdf.parse(ngayXuat); // Parse Ngày Xuất
                txt_NgayXuat.setDate(ngayXuatDate); // Điền Ngày Xuất vào JDateChooser
            } else {
                JOptionPane.showMessageDialog(this, "❌ Ngày Xuất không hợp lệ.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi định dạng Ngày Xuất.");
        }

        // Cập nhật các trường nhập liệu khác
        txt_MaKho1.setText(maKho); // Điền mã kho vào txt_MaKho1
        txt_MaPBYC.setText(maPBYC); // Điền mã phòng ban yêu cầu vào txt_MaPBYC
        txt_MaNguoiXuat.setText(maNguoiXuat); // Điền mã người xuất vào txt_MaNguoiXuat
        txt_MaNguoiNhan.setText(maNguoiNhan); // Điền mã người nhận vào txt_MaNguoiNhan
        txt_Lydo.setText(""); // Có thể điền lý do nếu có thêm thông tin lý do từ bảng
    } else {
        JOptionPane.showMessageDialog(this, "⚠ Chưa chọn phiếu xuất nào.");
    }
}
private void deletePhieuXuat() {
    // Lấy chỉ số dòng được chọn
    int row = tbl_PhieuXuat.getSelectedRow();

    // Kiểm tra nếu có dòng được chọn
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn một phiếu xuất để xóa!");
        return;
    }

    // Lấy mã phiếu xuất từ cột 0 (Mã Phiếu Xuất)
    String maPhieuXuat = tbl_PhieuXuat.getValueAt(row, 0).toString(); 

    // Xác nhận xóa
    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phiếu xuất " + maPhieuXuat + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.NO_OPTION) {
        return; // Nếu người dùng chọn "NO", không làm gì cả
    }

    try {
        // Gọi phương thức xóa trong DAO để xóa phiếu xuất
        PhieuXuatDAO pxdao = new PhieuXuatDAO();
        pxdao.delete(maPhieuXuat);

        // Cập nhật lại bảng phiếu xuất
        fillToTablePhieuXuat();

        // Thông báo xóa thành công
        JOptionPane.showMessageDialog(this, "✔ Xóa phiếu xuất thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi xóa phiếu xuất: " + e.getMessage());
    }
}





    
    
    
 void setCalendarToVietnamese() {
    Locale.setDefault(new Locale("vi", "VN")); // Đặt ngôn ngữ mặc định là Tiếng Việt
    txt_NgayXuat.setLocale(new Locale("vi", "VN")); // Thiết lập Locale cho JDateChooser
    }
 
  void setDefaultNgayXuat(){
        txt_NgayXuat.setDate(new Date());
//      java.util.Date ngayNhapUtil = txt_NgayNhap.getDate();
//      java.sql.Date ngayNhapSQL = new java.sql.Date(ngayNhapUtil.getTime());
//        Date ngayNhapJava = txt_NgayNhap.getDate();
//        txt_NgayNhap.setDate(ngayNhapJava);
      
    }
private void addPhieuXuat() {
    // Kiểm tra dữ liệu nhập vào
    if (
        txt_MaPhieuXuat.getText().isEmpty() ||
        txt_MaKho1.getText().isEmpty() ||
        txt_MaPBYC.getText().isEmpty() ||
        txt_MaNguoiXuat.getText().isEmpty() ||
        txt_MaNguoiNhan.getText().isEmpty() ||
        txt_NgayXuat.getDate() == null
    ) {
        Message.alert("Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    // Tạo đối tượng model_PhieuXuat để lưu dữ liệu
    model_PhieuXuat px = new model_PhieuXuat();
    px.setMaPhieuXuat(txt_MaPhieuXuat.getText().trim());
    px.setMaKho(txt_MaKho1.getText().trim());
    px.setMaPhongBanYeuCau(txt_MaPBYC.getText().trim());
    px.setMaNguoiXuat(txt_MaNguoiXuat.getText().trim());
    px.setMaNguoiNhan(txt_MaNguoiNhan.getText().trim());
    px.setNgayXuat(txt_NgayXuat.getDate());

    // Thiết lập giá trị cho Ngày Chứng Từ (Ngày hiện tại)
    px.setNgayChungTu(new java.sql.Date(System.currentTimeMillis()));  // Ngày chứng từ là ngày hiện tại

    px.setTrangThai("Chờ duyệt");  // Mặc định trạng thái là "Chờ duyệt"

    try {
        // 1. Thêm phiếu xuất vào cơ sở dữ liệu
        pxdao.insert(px);

        // 2. Cập nhật lại bảng phiếu xuất
        fillToTablePhieuXuat();

        // 3. Gán mã phiếu xuất vừa tạo sang phần chi tiết
        txt_MaPhieuXuatCTPX.setText(px.getMaPhieuXuat());

        // 4. Hiển thị thông báo thành công và mời người dùng nhập chi tiết
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Phiếu xuất " + px.getMaPhieuXuat() + " đã được tạo. Mời bạn nhập chi tiết cho phiếu xuất này.",
            "Thêm Phiếu Xuất Thành Công", 
            JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        // 5. Kiểm tra người dùng chọn "Yes" (Xác nhận)
        if (confirm == JOptionPane.YES_OPTION) {
            // Chuyển sang tab Chi Tiết Phiếu Xuất
            tab1_PhieuNhap.setSelectedIndex(1); // Tab thứ 1 (Chi Tiết Phiếu Xuất)
        } else {
            // Nếu người dùng chọn "No" (Hủy), vẫn ở lại tab Phiếu Xuất
            JOptionPane.showMessageDialog(this, "Bạn vẫn ở lại tab Phiếu Xuất để tiếp tục nhập.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        Message.error("❌ Lỗi khi thêm phiếu xuất: " + e.getMessage());
    }
}




 private void resetFormPhieuXuat() {
    txt_MaPhieuXuat.setText("");
    txt_NgayXuat.setDate(new Date());  // Set lại ngày hiện tại
    txt_MaKho1.setText("");
    txt_MaPBYC.setText("");
    txt_MaNguoiXuat.setText("");
    txt_MaNguoiNhan.setText("");
    txt_Lydo.setText("");
}

private void updatePhieuXuat() {
    String maPhieuXuat = txt_MaPhieuXuat.getText().trim();
    Date ngayXuat = txt_NgayXuat.getDate();
    String maKho = txt_MaKho1.getText().trim();
    String maPBYC = txt_MaPBYC.getText().trim();
    String maNguoiXuat = txt_MaNguoiXuat.getText().trim();
    String maNguoiNhan = txt_MaNguoiNhan.getText().trim();
    String lyDo = txt_Lydo.getText().trim();

    // Kiểm tra dữ liệu đầu vào
    if (maPhieuXuat.isEmpty() || ngayXuat == null || maKho.isEmpty() || maPBYC.isEmpty() || 
        maNguoiXuat.isEmpty() || maNguoiNhan.isEmpty()) {
        JOptionPane.showMessageDialog(this, "⚠ Vui lòng điền đầy đủ thông tin!");
        return;
    }

    // Kiểm tra nếu người dùng cố gắng sửa mã phiếu xuất
    if (!maPhieuXuat.equals(txt_MaPhieuXuat.getText())) {
        JOptionPane.showMessageDialog(this, "❌ Bạn không thể sửa mã phiếu xuất!");
        return;
    }

    // Kiểm tra nếu ngày chứng từ chưa được nhập, gán ngày hiện tại
    Date ngayChungTu = txt_NgayChungTu.getDate();
    if (ngayChungTu == null) {
        ngayChungTu = new Date();  // Gán ngày hiện tại
    }

    // Tạo đối tượng model_PhieuXuat để truyền vào DAO
    model_PhieuXuat px = new model_PhieuXuat();
    px.setMaPhieuXuat(maPhieuXuat); // Đảm bảo mã phiếu xuất không thay đổi
    px.setNgayChungTu(new java.sql.Date(ngayChungTu.getTime()));  // Chuyển sang java.sql.Date
    px.setNgayXuat(new java.sql.Date(ngayXuat.getTime()));  // Chuyển đổi sang java.sql.Date
    px.setMaKho(maKho);
    px.setMaPhongBanYeuCau(maPBYC);
    px.setMaNguoiXuat(maNguoiXuat);
    px.setMaNguoiNhan(maNguoiNhan);
    px.setLyDo(lyDo);
    px.setTrangThai("Chờ Duyệt");  // Hoặc lấy giá trị trạng thái từ form nếu cần

    try {
        // Cập nhật phiếu xuất
        pxdao.update(px);  // Gọi DAO để cập nhật dữ liệu vào DB

        // Làm mới bảng phiếu xuất để hiển thị thông tin mới
        fillToTablePhieuXuat();  // Đảm bảo rằng bảng sẽ được cập nhật ngay lập tức

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "✔ Cập nhật phiếu xuất thành công!");

        // Làm mới các trường nhập liệu
        resetFormPhieuXuat();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi cập nhật phiếu xuất: " + e.getMessage());
    }
}

// Tìm kiếm phiếu xuất
private void searchPhieuXuat() {
    // Lấy giá trị từ JTextField và JComboBox
    String tuKhoa = txt_TimKiemPX.getText().trim();  // Từ khóa tìm kiếm
    String tieuChi = (String) cbo_TimKiemPX.getSelectedItem();  // Tiêu chí tìm kiếm (Mã Phiếu Xuất, Mã Kho, Trạng Thái, Mã Người Xuất, v.v.)

    // Kiểm tra nếu không có từ khóa tìm kiếm
    if (tuKhoa.isEmpty()) {
        Message.alert("⚠ Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    // Xây dựng câu truy vấn SQL theo tiêu chí
    String sql = "SELECT * FROM PHIEUXUAT WHERE ";

    switch (tieuChi) {
        case "Mã Phiếu Xuất":
            sql += "MaPhieuXuat LIKE ?";
            break;
        case "Mã Kho":
            sql += "MaKho LIKE ?";
            break;
        case "Trạng Thái":
            sql += "TrangThai LIKE ?";
            break;
        case "Mã Người Xuất":
            sql += "MaNguoiXuat LIKE ?";
            break;
        case "Mã Người Nhận":
            sql += "MaNguoiNhan LIKE ?";
            break;
        default:
            sql = "SELECT * FROM PHIEUXUAT";  // Trường hợp mặc định (không có tiêu chí)
            break;
    }

    // Tìm kiếm với câu truy vấn SQL
    try {
        // Gọi phương thức selectBySql từ phieuXuatDAO để thực hiện tìm kiếm
        List<model_PhieuXuat> list_PhieuXuat = phieuXuatDAO.selectBySql(sql, "%" + tuKhoa + "%");

        // Nếu không tìm thấy kết quả
        if (list_PhieuXuat.isEmpty()) {
            Message.alert("⚠ Không tìm thấy kết quả phù hợp!");

            // Khi không có kết quả, hiển thị tất cả phiếu xuất
            fillToTablePhieuXuat(); // Hiển thị lại toàn bộ phiếu xuất
            return;
        }

        // Xóa dữ liệu cũ trong bảng
        DefaultTableModel model = (DefaultTableModel) tbl_PhieuXuat.getModel();
        model.setRowCount(0);  // Xóa tất cả các dòng hiện có trong bảng

        // Hiển thị kết quả tìm kiếm lên bảng
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (model_PhieuXuat px : list_PhieuXuat) {
            // Định dạng ngày tháng nếu cần thiết
            String ngayChungTuFormatted = (px.getNgayChungTu() != null) ? sdf.format(px.getNgayChungTu()) : "";
            String ngayXuatFormatted = (px.getNgayXuat() != null) ? sdf.format(px.getNgayXuat()) : "";

            // Tạo một dòng mới trong bảng với dữ liệu phiếu xuất
            Object[] row = new Object[]{
                px.getMaPhieuXuat(),
                ngayChungTuFormatted,
                ngayXuatFormatted,
                px.getMaKho(),
                px.getMaPhongBanYeuCau(),
                px.getMaNguoiXuat(),
                px.getMaNguoiNhan(),
                px.getTrangThai()
            };
            
            model.addRow(row);  // Thêm dòng vào bảng
        }

    } catch (Exception e) {
        e.printStackTrace();
        Message.error("❌ Lỗi khi tìm kiếm phiếu xuất: " + e.getMessage());
    }
}





 private void loadNhomVatTu() {
    nhomVatTuMap = new HashMap<>();

    // Khối lượng
    nhomVatTuMap.put("Khối Lượng", Arrays.asList("Tấn", "Kilogram", "Gram"));
    // Chiều dài
    nhomVatTuMap.put("Chiều Dài", Arrays.asList("Kilomet", "Mét", "Centimet"));
    // Thể tích
    nhomVatTuMap.put("Thể Tích", Arrays.asList("Mét khối", "Lít", "Mililit"));
    // Đóng gói
    nhomVatTuMap.put("Đơn Vị Đóng Gói", Arrays.asList("Kiện", "Thùng", "Hộp", "Cái"));

    cboNhomVatTu.removeAllItems();
    for (String nhom : nhomVatTuMap.keySet()) {
        cboNhomVatTu.addItem(nhom);
    }
}

     // Xử lý sự kiện
     // Sự kiện ComboBox
private void addEventHandlers() {
    cboNhomVatTu.addActionListener(e -> {
        String selectedGroup = (String) cboNhomVatTu.getSelectedItem();
        if (selectedGroup == null) return;

        loadDonViTinh(); // Đảm bảo luôn có map mới

        List<String> donViList = nhomVatTuMap.get(selectedGroup);

        cboDonViGoc.removeAllItems();
        cboDonViDich.removeAllItems();

        if (donViList != null) {
            for (String donVi : donViList) {
                cboDonViGoc.addItem(donVi);
                cboDonViDich.addItem(donVi);
            }
        }

        loadLoaiVatTuTheoNhom(selectedGroup);
    });

    btnChuyenDoi.addActionListener(e -> chuyenDoiDonVi());
}

    
    // Load danh sách đơn vị tính từ DB
  private void loadDonViTinh() {
    donViTinhMap = new HashMap<>();
    String query = "SELECT MaDonVi, TenDonVi FROM DonViTinh";

    try (ResultSet rs = JDBCHelper.query(query)) {
        while (rs.next()) {
            String tenDonVi = rs.getString("TenDonVi").trim();
            int maDonVi = rs.getInt("MaDonVi");
            donViTinhMap.put(tenDonVi, maDonVi);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải đơn vị tính: " + e.getMessage());
    }

    System.out.println("✅ Đơn vị tải lên từ DB: " + donViTinhMap.keySet());
}

    // Phương thức để tải dữ liệu vào donViTinhMap


private void chuyenDoiDonVi() {
    try {
        if (cboDonViGoc.getSelectedItem() == null || cboDonViDich.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ đơn vị gốc và đơn vị đích.");
            return;
        }

        String donViGoc = cboDonViGoc.getSelectedItem().toString().trim();
        String donViDich = cboDonViDich.getSelectedItem().toString().trim();

        if (!donViTinhMap.containsKey(donViGoc) || !donViTinhMap.containsKey(donViDich)) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy đơn vị trong dữ liệu.");
            return;
        }

        // Kiểm tra xem người dùng có nhập đúng số lượng hay không
        double soLuong = 0;
        try {
            soLuong = Double.parseDouble(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số cho Số Lượng.");
            return;
        }

        // Lấy mã đơn vị gốc và đơn vị chuyển đổi
        int maDonViGoc = donViTinhMap.get(donViGoc);
        int maDonViDich = donViTinhMap.get(donViDich);

        // Truy vấn hệ số quy đổi từ cơ sở dữ liệu
        double heSo = layHeSoQuyDoi(maDonViGoc, maDonViDich);

        double ketQua = soLuong * heSo;
        txtKetQua.setText(String.valueOf(ketQua));

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng số.");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi: " + e.getMessage());
    }
}


private double layHeSoQuyDoi(int maGoc, int maDich) {
    double heSo = 1.0;
    String sql = "SELECT HeSo FROM QuyDoiDonVi WHERE DonViGoc = ? AND DonViDich = ?";

    try (ResultSet rs = JDBCHelper.query(sql, maGoc, maDich)) {
        if (rs.next()) {
            heSo = rs.getDouble("HeSo");
        } else {
            // Nếu không có chiều gốc → đích, thử tìm chiều ngược và chia
            try (ResultSet rsReverse = JDBCHelper.query(sql, maDich, maGoc)) {
                if (rsReverse.next()) {
                    heSo = 1.0 / rsReverse.getDouble("HeSo");
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy hệ số quy đổi giữa hai đơn vị.");
                    heSo = 1.0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi truy vấn hệ số quy đổi: " + e.getMessage());
    }

    return heSo;
}


      // Lấy hệ số từ database
     
  private double layHeSoChuyenDoi(int maDonViGoc, int maDonViDich) throws SQLException {
    String query = "SELECT HeSo FROM QuyDoiDonVi WHERE MaDonViGoc = ? AND MaDonViDich = ?";
    try (ResultSet rs = JDBCHelper.query(query, maDonViGoc, maDonViDich)) {
        if (rs.next()) {
            return rs.getDouble("HeSo");
        } else {
            // Thử chiều ngược lại
            ResultSet rs2 = JDBCHelper.query(query, maDonViDich, maDonViGoc);
            if (rs2.next()) {
                return 1.0 / rs2.getDouble("HeSo");
            }
        }
    }
    throw new SQLException("Không tìm thấy hệ số chuyển đổi.");
}

private void loadLoaiVatTuTheoNhom(String nhomVatTu) {
    cboLoaiVatTu.removeAllItems();
    try {
        String sql = "SELECT TenLoaiVatTu FROM LOAIVATTU WHERE NhomVatTu = ?";
        ResultSet rs = JDBCHelper.query(sql, nhomVatTu);
        boolean found = false;
        while (rs.next()) {
            cboLoaiVatTu.addItem(rs.getString("TenLoaiVatTu"));
            found = true;
        }
        if (!found) {
            cboLoaiVatTu.addItem("Không có loại phù hợp");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void loadDonViTheoLoaiVatTu(String tenLoai, JComboBox<String> cboDonVi) {
    try {
        ResultSet rs = JDBCHelper.query("SELECT TenDonVi FROM DonViTinh WHERE NhomVatTu = ?", tenLoai);
        cboDonVi.removeAllItems();
        while (rs.next()) {
            cboDonVi.addItem(rs.getString("TenDonVi"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void chuyenDoiTheoLoaiVatTu(String donViGoc, String donViDich, double soLuong) {
    try {
        int maDVGoc = donViTinhMap.get(donViGoc);
        int maDVDich = donViTinhMap.get(donViDich);
        double heSo = layHeSoChuyenDoi(maDVGoc, maDVDich);
        double ketQua = soLuong * heSo;
        System.out.println("Kết quả chuyển đổi: " + ketQua);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Không tìm thấy hệ số chuyển đổi!");
    }
}

public void setMaPhieuXuat(String maPhieu) {
    txt_MaPhieuXuat.setText(maPhieu);
}

private void fillToTableChiTietPhieuXuat() {
    tbl_ModelChiTietPhieuXuatModel.setRowCount(0);  // Xóa dữ liệu cũ trong bảng

    // Lấy danh sách chi tiết phiếu xuất từ DAO
    List<model_ChiTietPhieuXuat> list = chiTietPhieuXuatDAO.selectAll();

    // Nếu danh sách rỗng, thông báo lỗi
    if (list == null || list.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có chi tiết phiếu xuất nào trong CSDL.");
        return;
    }

    // Duyệt qua các chi tiết phiếu xuất và thêm vào bảng
    for (model_ChiTietPhieuXuat ct : list) {
        String donViGoc = ct.getDonViGoc() != null ? ct.getDonViGoc() : "Chưa xác định";
        String donViChuyenDoi = ct.getDonViChuyenDoi() != null ? ct.getDonViChuyenDoi() : "Chưa xác định";

        Object[] row = new Object[] {
            ct.getMaPhieuXuat(),          // Mã phiếu xuất
            ct.getMaVatTu(),              // Mã vật tư
            donViGoc,                     // Đơn vị gốc
            donViChuyenDoi,               // Đơn vị chuyển đổi
            ct.getSoLuongXuat(),          // Số lượng xuất
            ct.getSoLuongQuyDoi()         // Số lượng quy đổi
        };

        tbl_ModelChiTietPhieuXuatModel.addRow(row);
    }
}








// Hàm giúp load lại các đơn vị đúng cho các combobox DonViGoc và DonViDich
private void loadDonViGocAndDich(String donViGoc, String donViChuyenDoi) {
    // Tải các nhóm đơn vị
    loadNhomVatTu();

    // Đảm bảo rằng chỉ những đơn vị của nhóm vật tư mới được hiển thị
    if (nhomVatTuMap.containsKey(donViGoc)) {
        // Clear các item hiện tại trong combobox
        cboDonViGoc.removeAllItems();
        cboDonViDich.removeAllItems();
        
        List<String> donViList = nhomVatTuMap.get(donViGoc);
        
        // Thêm các đơn vị vào combobox
        for (String donVi : donViList) {
            cboDonViGoc.addItem(donVi);
            cboDonViDich.addItem(donVi);
        }
        
        // Chọn đơn vị hiện tại
        cboDonViGoc.setSelectedItem(donViGoc);
        cboDonViDich.setSelectedItem(donViChuyenDoi);
    }
}




private void initEventHandlers() {
    // Sự kiện mouseClicked khi người dùng click vào dòng trong bảng tbl_ModelChiTietPhieuXuat
    tbl_ModelChiTietPhieuXuat.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int selectedRow = tbl_ModelChiTietPhieuXuat.getSelectedRow();
            if (selectedRow >= 0) {
                // Gọi phương thức để điền dữ liệu vào các trường khi click vào một dòng
                ModelChiTietPhieuXuatMouseClicked(selectedRow);
            }
        }
    });
}

private void ModelChiTietPhieuXuatMouseClicked(int row) {
    try {
        // Lấy giá trị từ bảng theo chỉ số cột và dòng được chọn
        String maPhieuXuat = tbl_ModelChiTietPhieuXuat.getValueAt(row, 0).toString(); // Cột 0: Mã Phiếu Xuất
        String maVatTu = tbl_ModelChiTietPhieuXuat.getValueAt(row, 1).toString(); // Cột 1: Mã Vật Tư
        String donViGoc = tbl_ModelChiTietPhieuXuat.getValueAt(row, 2).toString(); // Cột 2: Đơn vị gốc
        String donViChuyenDoi = tbl_ModelChiTietPhieuXuat.getValueAt(row, 3).toString(); // Cột 3: Đơn vị chuyển đổi
        float soLuongXuat = Float.parseFloat(tbl_ModelChiTietPhieuXuat.getValueAt(row, 4).toString()); // Cột 4: Số lượng xuất
       

        // Điền mã phiếu xuất vào trường txt_MaPhieuXuatCTPX
        txt_MaPhieuXuatCTPX.setText(maPhieuXuat);  // Điền mã phiếu xuất vào trường txt_MaPhieuXuatCTPX

        // Điền dữ liệu vào các trường nhập liệu khác
        txt_MaVatTu.setText(maVatTu);  // Điền mã vật tư vào trường txt_MaVatTu
        txt_soluongXuat.setText(String.valueOf(soLuongXuat)); // Điền số lượng xuất vào trường txt_soluongXuat
        
        // Kiểm tra và điền đơn vị vào ComboBox
        cboDonViGoc.setSelectedItem(donViGoc); // Điền đơn vị gốc vào ComboBox cboDonViGoc
        cboDonViDich.setSelectedItem(donViChuyenDoi); // Điền đơn vị chuyển đổi vào ComboBox cboDonViDich

        // Lấy thông tin loại và nhóm vật tư từ cơ sở dữ liệu
        try (
            ResultSet rsLoai = JDBCHelper.query(""" 
                SELECT lvt.TenLoaiVatTu 
                FROM VATTU vt 
                JOIN LOAIVATTU lvt ON vt.MaLoaiVatTu = lvt.MaLoaiVatTu 
                WHERE vt.MaVatTu = ?
            """, maVatTu);
            
            ResultSet rsNhom = JDBCHelper.query(
                "SELECT NhomVatTu FROM DonViTinh WHERE TenDonVi = ?", donViGoc
            )
        ) {
            // Kiểm tra nhóm vật tư và điền vào cboNhomVatTu
            if (rsNhom.next()) {
                String nhomVatTu = rsNhom.getString("NhomVatTu");
                cboNhomVatTu.setSelectedItem(nhomVatTu); // Điền nhóm vật tư vào cboNhomVatTu
                loadLoaiVatTuTheoNhom(nhomVatTu); // Load loại tương ứng
            }

            // Kiểm tra loại vật tư và điền vào cboLoaiVatTu
            if (rsLoai.next()) {
                String loaiVatTu = rsLoai.getString("TenLoaiVatTu");
                cboLoaiVatTu.setSelectedItem(loaiVatTu); // Điền loại vật tư vào cboLoaiVatTu
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Lỗi khi lấy dữ liệu từ cơ sở dữ liệu: " + ex.getMessage());
        }

        // Đảm bảo đơn vị được load đúng vào combobox
        loadDonViGocAndDich(donViGoc, donViChuyenDoi);  // Hàm này sẽ giúp load lại đơn vị theo từng loại

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi xử lý sự kiện click: " + e.getMessage());
    }
}
    

public void themChiTietPhieuXuat() {
    try {
        // Kiểm tra các combo box có giá trị hay không
        if (cboNhomVatTu.getSelectedItem() == null ||
            cboDonViGoc.getSelectedItem() == null ||
            cboDonViDich.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn đầy đủ Nhóm, Đơn vị gốc, Đơn vị đích.");
            return;
        }

        // Lấy dữ liệu đầu vào từ các trường nhập liệu
        String maPhieuXuat = txt_MaPhieuXuatCTPX.getText().trim();
        String maVatTu = txt_MaVatTu.getText().trim();
        String nhomVatTu = cboNhomVatTu.getSelectedItem().toString();
        String tenDonViGoc = cboDonViGoc.getSelectedItem().toString();
        String tenDonViDich = cboDonViDich.getSelectedItem().toString();

        if (maPhieuXuat.isEmpty() || maVatTu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Mã phiếu xuất hoặc mã vật tư không được để trống!");
            return;
        }

        double soLuongXuat = 0;
        double soLuongQuyDoi = 0;

        try {
            // Chuyển đổi số lượng xuất và số lượng quy đổi sang kiểu số
            soLuongXuat = Double.parseDouble(txt_soluongXuat.getText().trim());
            soLuongQuyDoi = Double.parseDouble(txtKetQua.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đúng định dạng số!");
            return;
        }

        // Lấy mã đơn vị gốc và chuyển đổi
        int maDonViGoc = donViTinhMap.getOrDefault(tenDonViGoc, -1);
        int maDonViDich = donViTinhMap.getOrDefault(tenDonViDich, -1);

        if (maDonViGoc == -1 || maDonViDich == -1) {
            JOptionPane.showMessageDialog(this, "⚠ Không tìm thấy mã đơn vị tương ứng!");
            return;
        }

        // Tạo đối tượng chi tiết phiếu xuất
        model_ChiTietPhieuXuat ct = new model_ChiTietPhieuXuat();
        ct.setMaPhieuXuat(maPhieuXuat);                // Mã phiếu xuất
        ct.setMaVatTu(maVatTu);                        // Mã vật tư
        ct.setDonViGoc(tenDonViGoc);                    // Đơn vị gốc
        ct.setDonViChuyenDoi(tenDonViDich);             // Đơn vị chuyển đổi
        ct.setSoLuongXuat((float) soLuongXuat);        // Số lượng xuất
        ct.setSoLuongQuyDoi((float) soLuongQuyDoi);    // Số lượng quy đổi

        // Gọi DAO để thêm chi tiết phiếu xuất vào cơ sở dữ liệu
        new ChiTietPhieuXuatDAO().insert(ct);

        // Cập nhật lại bảng chi tiết phiếu xuất
        fillToTableChiTietPhieuXuat();

        // Thông báo thành công
        JOptionPane.showMessageDialog(this, "✅ Thêm chi tiết phiếu xuất thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi thêm chi tiết: " + e.getMessage());
    }
}




private void deleteChiTietPhieuXuat() {
    try {
        // Lấy dòng đã chọn trong bảng Chi Tiết Phiếu Xuất
        int selectedRow = tbl_ModelChiTietPhieuXuat.getSelectedRow();
        
        if (selectedRow < 0) {
            // Nếu chưa chọn dòng nào
            JOptionPane.showMessageDialog(this, "⚠ Vui lòng chọn một dòng để xóa!");
            return;
        }

        // Lấy Mã Phiếu Xuất và Mã Vật Tư từ dòng được chọn
        String maPhieuXuat = tbl_ModelChiTietPhieuXuat.getValueAt(selectedRow, 0).toString();
        String maVatTu = tbl_ModelChiTietPhieuXuat.getValueAt(selectedRow, 1).toString();

        // Hỏi người dùng có chắc chắn xóa không
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa chi tiết phiếu xuất này?", 
            "Xóa Chi Tiết", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa chi tiết phiếu xuất
            new ChiTietPhieuXuatDAO().delete(maPhieuXuat, maVatTu);

            // Kiểm tra xem phiếu xuất có còn chi tiết nào không
            List<model_ChiTietPhieuXuat> remainingDetails = new ChiTietPhieuXuatDAO().selectByMaPhieuXuat(maPhieuXuat);
            if (remainingDetails.isEmpty()) {
                // Nếu không còn chi tiết nào, xóa phiếu xuất
                new PhieuXuatDAO().delete(maPhieuXuat);
            }

            // Cập nhật lại bảng chi tiết phiếu xuất và phiếu xuất
            fillToTableChiTietPhieuXuat();
            fillToTablePhieuXuat();

            JOptionPane.showMessageDialog(this, "✅ Xóa chi tiết phiếu xuất thành công!");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi xóa chi tiết phiếu xuất: " + e.getMessage());
    }
}



private void updateChiTietPhieuXuat() {
    // Lấy các giá trị từ các trường nhập liệu
    String maPhieuXuat = txt_MaPhieuXuatCTPX.getText().trim();
    String maVatTu = txt_MaVatTu.getText().trim();
    String donViGoc = cboDonViGoc.getSelectedItem().toString();
    String donViChuyenDoi = cboDonViDich.getSelectedItem().toString();

    // Kiểm tra nếu mã phiếu xuất hoặc mã vật tư trống
    if (maPhieuXuat.isEmpty() || maVatTu.isEmpty() || donViGoc.isEmpty() || donViChuyenDoi.isEmpty()) {
        JOptionPane.showMessageDialog(this, "⚠ Vui lòng nhập đầy đủ thông tin!");
        return;
    }

    // Kiểm tra và lấy số lượng xuất
    float soLuongXuat = 0;
    try {
        String soLuongXuatStr = txt_soluongXuat.getText().trim();
        if (!soLuongXuatStr.isEmpty()) {
            soLuongXuat = Float.parseFloat(soLuongXuatStr);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập số hợp lệ cho Số Lượng Xuất!");
        return;
    }

    // Kiểm tra và lấy số lượng quy đổi
    float soLuongQuyDoi = 0;
    try {
        String soLuongQuyDoiStr = txtKetQua.getText().trim();
        if (!soLuongQuyDoiStr.isEmpty()) {
            soLuongQuyDoi = Float.parseFloat(soLuongQuyDoiStr);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập số hợp lệ cho Số Lượng Quy Đổi!");
        return;
    }

    // Tạo đối tượng model_ChiTietPhieuXuat để lưu thông tin
    model_ChiTietPhieuXuat ct = new model_ChiTietPhieuXuat();
    ct.setMaPhieuXuat(maPhieuXuat);
    ct.setMaVatTu(maVatTu);
    ct.setDonViGoc(donViGoc);
    ct.setDonViChuyenDoi(donViChuyenDoi);
    ct.setSoLuongXuat(soLuongXuat);
    ct.setSoLuongQuyDoi(soLuongQuyDoi);

    // Gọi phương thức update từ DAO
    try {
        new ChiTietPhieuXuatDAO().update(ct);  // Gọi phương thức update trong DAO để sửa chi tiết phiếu xuất

        // Cập nhật lại bảng chi tiết phiếu xuất sau khi sửa
        fillToTableChiTietPhieuXuat();

        JOptionPane.showMessageDialog(this, "✔ Sửa chi tiết phiếu xuất thành công!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "❌ Lỗi khi sửa chi tiết phiếu xuất: " + e.getMessage());
    }
}


private void searchChiTietPhieuXuat() {
    // Lấy giá trị từ JTextField và JComboBox
    String tuKhoa = txt_TimKiemCTPX.getText().trim();  // Từ khóa tìm kiếm
    String tieuChi = (String) cbo_TimKiemCTPX.getSelectedItem();  // Tiêu chí tìm kiếm (Mã Phiếu Xuất, Mã Vật Tư, v.v.)

    // Kiểm tra nếu không có từ khóa tìm kiếm
    if (tuKhoa.isEmpty()) {
        Message.alert("⚠ Vui lòng nhập từ khóa tìm kiếm!");
        return;
    }

    // Khởi tạo câu truy vấn cơ bản
    String sql = "SELECT * FROM CHITIETPHIEUXUAT WHERE ";

    // Xây dựng câu truy vấn SQL theo tiêu chí tìm kiếm
    switch (tieuChi) {
        case "Mã Phiếu Xuất":
            sql += "MaPhieuXuat LIKE ?";
            break;
        case "Mã Vật Tư":
            sql += "MaVatTu LIKE ?";
            break;
        default:
            sql = "SELECT * FROM CHITIETPHIEUXUAT";  // Trường hợp mặc định (không có tiêu chí)
            break;
    }

    // Tìm kiếm với câu truy vấn SQL
    try {
        // Gọi phương thức selectBySql từ DAO để thực hiện tìm kiếm
        List<model_ChiTietPhieuXuat> list_ChiTietPhieuXuat = chiTietPhieuXuatDAO.selectBySql(sql, "%" + tuKhoa + "%");

        // Nếu không tìm thấy kết quả
        if (list_ChiTietPhieuXuat == null || list_ChiTietPhieuXuat.isEmpty()) {
            Message.alert("⚠ Không tìm thấy kết quả phù hợp!");

            // Khi không có kết quả, hiển thị lại tất cả chi tiết phiếu xuất
            fillToTableChiTietPhieuXuat(); // Hiển thị lại toàn bộ chi tiết phiếu xuất
            return;
        }

        // Xóa dữ liệu cũ trong bảng
        DefaultTableModel model = (DefaultTableModel) tbl_ModelChiTietPhieuXuat.getModel();
        model.setRowCount(0);  // Xóa tất cả các dòng hiện có trong bảng

        // Hiển thị kết quả tìm kiếm lên bảng
        for (model_ChiTietPhieuXuat ct : list_ChiTietPhieuXuat) {
            Object[] row = new Object[]{
                ct.getMaPhieuXuat(),
                ct.getMaVatTu(),
                ct.getDonViGoc(),
                ct.getDonViChuyenDoi(),
                ct.getSoLuongXuat(),
                ct.getSoLuongQuyDoi()
            };
            model.addRow(row);  // Thêm dòng vào bảng
        }

    } catch (Exception e) {
        e.printStackTrace();
        Message.error("❌ Lỗi khi tìm kiếm chi tiết phiếu xuất: " + e.getMessage());
    }
}




 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tab1_PhieuNhap = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txt_TimKiemPX = new javax.swing.JTextField();
        btn_TimKiemPX = new javax.swing.JButton();
        btn_ThemPX = new javax.swing.JButton();
        btn_XoaPX = new javax.swing.JButton();
        btn_SuaPX = new javax.swing.JButton();
        txt_NgayXuat = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_MaPhieuXuat = new javax.swing.JTextField();
        txt_MaPBYC = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_PhieuXuat = new javax.swing.JTable();
        btn_LamMoiPX = new javax.swing.JButton();
        btn_XemCT2 = new javax.swing.JButton();
        btn_XuatExcel2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txt_MaKho1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_MaNguoiXuat = new javax.swing.JTextField();
        txt_MaNguoiNhan = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_Lydo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_NgayChungTu = new com.toedter.calendar.JDateChooser();
        cbo_TimKiemPX = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txt_MaPhieuXuatCTPX = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_MaVatTu = new javax.swing.JTextField();
        txt_soluongXuat = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_ModelChiTietPhieuXuat = new javax.swing.JTable();
        txt_TimKiemCTPX = new javax.swing.JTextField();
        btn_ThemCTPX = new javax.swing.JButton();
        btn_XoaCTPX = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btn_SuaCTPX = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btn_LamMoiCTPX = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btn_XemCT1 = new javax.swing.JButton();
        btn_TimCTPX = new javax.swing.JButton();
        btn_XuatExcel1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cboNhomVatTu = new javax.swing.JComboBox<>();
        cboLoaiVatTu = new javax.swing.JComboBox<>();
        cboDonViGoc = new javax.swing.JComboBox<>();
        cboDonViDich = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        txtKetQua = new javax.swing.JTextField();
        btnChuyenDoi = new javax.swing.JButton();
        cbo_TimKiemCTPX = new javax.swing.JComboBox<>();

        tab1_PhieuNhap.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btn_TimKiemPX.setText("Tìm Kiếm");
        btn_TimKiemPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimKiemPXActionPerformed(evt);
            }
        });

        btn_ThemPX.setText("Thêm");
        btn_ThemPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemPXActionPerformed(evt);
            }
        });

        btn_XoaPX.setText("Xóa");
        btn_XoaPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaPXActionPerformed(evt);
            }
        });

        btn_SuaPX.setText("Sửa");
        btn_SuaPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaPXActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        jLabel2.setText("Phiếu Xuất");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Ngày Xuất:");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Mã Kho:");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Mã PB yêu cầu:");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tbl_PhieuXuat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Xuất", "Ngày Chứng Từ", "Ngày Xuất", "Mã Kho", "Mã PB Yêu Cầu", "Mã Người Xuất", "Mã Người Nhận", "Trạng Thái"
            }
        ));
        tbl_PhieuXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_PhieuXuatMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_PhieuXuat);

        btn_LamMoiPX.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_LamMoiPX.setText("Làm Mới");
        btn_LamMoiPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiPXActionPerformed(evt);
            }
        });

        btn_XemCT2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemCT2.setText("Xem Chi Tiết");
        btn_XemCT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XemCT2ActionPerformed(evt);
            }
        });

        btn_XuatExcel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XuatExcel2.setText("Xuất Excel");
        btn_XuatExcel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatExcel2ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Mã Phiếu Xuất:");
        jLabel17.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Mã Người Xuất:");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Mã Người Nhận:");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Lý do:");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Ngày Chứng Từ:");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        cbo_TimKiemPX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Phiếu Xuất", "Mã PB Yêu Cầu", "Mã Người Xuất", "Mã Người Nhận" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_MaKho1)
                            .addComponent(txt_NgayXuat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaNguoiXuat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(txt_MaNguoiNhan)
                            .addComponent(txt_MaPBYC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(txt_Lydo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(txt_NgayChungTu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaPhieuXuat, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(19, 19, 19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(btn_ThemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_XoaPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_SuaPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(btn_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_LamMoiPX, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbo_TimKiemPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_NgayXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(txt_NgayChungTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txt_MaPhieuXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_MaKho1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_MaPBYC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(txt_MaNguoiXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(txt_MaNguoiNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txt_Lydo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_XoaPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_ThemPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_SuaPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_XuatExcel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XemCT2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_LamMoiPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );

        tab1_PhieuNhap.addTab("Phiếu Xuất", jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(988, 700));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Số Lượng :");

        tbl_ModelChiTietPhieuXuat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Xuất", "Mã Vật Tư", "Đơn vị gốc", "Đơn vị chuyển đổi", "Số lượng xuất", "Số lượng quy đổi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbl_ModelChiTietPhieuXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ModelChiTietPhieuXuatMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_ModelChiTietPhieuXuat);

        btn_ThemCTPX.setText("Thêm");
        btn_ThemCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemCTPXActionPerformed(evt);
            }
        });

        btn_XoaCTPX.setText("Xóa");
        btn_XoaCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XoaCTPXActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel6.setText("Phiếu Xuất Chi Tiết");

        btn_SuaCTPX.setText("Sửa");
        btn_SuaCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaCTPXActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Mã phiếu xuất:");

        btn_LamMoiCTPX.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_LamMoiCTPX.setText("Làm Mới");
        btn_LamMoiCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LamMoiCTPXActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Mã Vật Tư :");

        btn_XemCT1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XemCT1.setText("Xem Chi Tiết");
        btn_XemCT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XemCT1ActionPerformed(evt);
            }
        });

        btn_TimCTPX.setText("Tìm Kiếm");
        btn_TimCTPX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TimCTPXActionPerformed(evt);
            }
        });

        btn_XuatExcel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_XuatExcel1.setText("Xuất Excel");
        btn_XuatExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_XuatExcel1ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Chuyển đổi đơn vị");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Nhóm vật tư:\n");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Loại vật tư:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Đơn vị gốc:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Đơn vị đích:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Số lượng chuyển đổi:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Kết quả:");

        cboNhomVatTu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        txtKetQua.setEditable(false);

        btnChuyenDoi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnChuyenDoi.setText("Chuyển đổi");
        btnChuyenDoi.setToolTipText("");
        btnChuyenDoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChuyenDoiActionPerformed(evt);
            }
        });

        cbo_TimKiemCTPX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Phiếu Xuất", "Mã Vật Tư" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(170, 170, 170))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txt_MaVatTu))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txt_MaPhieuXuatCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addComponent(btn_ThemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(46, 46, 46))
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel18)
                                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(jLabel21)
                                                                .addComponent(jLabel20)
                                                                .addComponent(jLabel19)))
                                                        .addGap(47, 47, 47)))
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(txtKetQua)
                                                            .addComponent(cboNhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                                            .addComponent(cboLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(cboDonViGoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(cboDonViDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(txtSoLuong)))
                                                    .addComponent(btnChuyenDoi)))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txt_soluongXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(18, 18, 18)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel22))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel23)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_TimCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_TimKiemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbo_TimKiemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_LamMoiCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TimKiemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_TimCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbo_TimKiemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txt_MaPhieuXuatCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txt_MaVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(txt_soluongXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(cboNhomVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(cboLoaiVatTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboDonViGoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(cboDonViDich, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtKetQua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnChuyenDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_LamMoiCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XemCT1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XuatExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ThemCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_XoaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_SuaCTPX, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        tab1_PhieuNhap.addTab("Chi tiết phiếu xuất", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap, javax.swing.GroupLayout.DEFAULT_SIZE, 1305, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab1_PhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_ModelChiTietPhieuXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ModelChiTietPhieuXuatMouseClicked
  
    }//GEN-LAST:event_tbl_ModelChiTietPhieuXuatMouseClicked

    private void btn_ThemCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemCTPXActionPerformed
        themChiTietPhieuXuat();
    }//GEN-LAST:event_btn_ThemCTPXActionPerformed

    
    private void btn_XoaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaCTPXActionPerformed
        deleteChiTietPhieuXuat();
    }//GEN-LAST:event_btn_XoaCTPXActionPerformed

    private void btn_LamMoiCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiCTPXActionPerformed
        //        fillToTableCTPN();
        //        fillToTablePN();
        //        txt_NgayNhap.setDate(null);
        //        txt_MaKho.setText(null);
        //        txt_MaNhaCungCap.setText(null);
        //        txt_MaPhieuNhapCTPN.setText(null);
        //        txt_MaPhieuNhap.setText(null);
        //        txt_soluong.setText(null);
        //        txt_TimKiemCTPN.setText(null);
        //        txt_TimKiemPN.setText(null);
    }//GEN-LAST:event_btn_LamMoiCTPXActionPerformed

    private void btn_XemCT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemCT1ActionPerformed
        //viewChiTietPhieuNhapInPN();
    }//GEN-LAST:event_btn_XemCT1ActionPerformed

    private void btn_TimCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimCTPXActionPerformed
        searchChiTietPhieuXuat();
    }//GEN-LAST:event_btn_TimCTPXActionPerformed

    private void btn_XuatExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatExcel1ActionPerformed
        //exportToExcel();
    }//GEN-LAST:event_btn_XuatExcel1ActionPerformed

    private void btn_TimKiemPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TimKiemPXActionPerformed
searchPhieuXuat();
    }//GEN-LAST:event_btn_TimKiemPXActionPerformed

    private void btn_ThemPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemPXActionPerformed
       addPhieuXuat();
    }//GEN-LAST:event_btn_ThemPXActionPerformed

    private void btn_XoaPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XoaPXActionPerformed
     deletePhieuXuat();
    }//GEN-LAST:event_btn_XoaPXActionPerformed

    private void btn_SuaPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaPXActionPerformed
      updatePhieuXuat();
    }//GEN-LAST:event_btn_SuaPXActionPerformed

    private void tbl_PhieuXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_PhieuXuatMouseClicked
        MouseClickedPhieuXuat(evt);
    }//GEN-LAST:event_tbl_PhieuXuatMouseClicked

    private void btn_LamMoiPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LamMoiPXActionPerformed
     resetFormPhieuXuat();
    }//GEN-LAST:event_btn_LamMoiPXActionPerformed

    private void btn_XemCT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XemCT2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_XemCT2ActionPerformed

    private void btn_XuatExcel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_XuatExcel2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_XuatExcel2ActionPerformed

    private void btnChuyenDoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChuyenDoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChuyenDoiActionPerformed

    private void btn_SuaCTPXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaCTPXActionPerformed
           updateChiTietPhieuXuat();       
    }//GEN-LAST:event_btn_SuaCTPXActionPerformed

    @Override
    public boolean formClose() {
        return true;
        
    }

    @Override
    public void formOpen() {
        System.out.println("Duy Dep Trai");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChuyenDoi;
    private javax.swing.JButton btn_LamMoiCTPX;
    private javax.swing.JButton btn_LamMoiPX;
    private javax.swing.JButton btn_SuaCTPX;
    private javax.swing.JButton btn_SuaPX;
    private javax.swing.JButton btn_ThemCTPX;
    private javax.swing.JButton btn_ThemPX;
    private javax.swing.JButton btn_TimCTPX;
    private javax.swing.JButton btn_TimKiemPX;
    private javax.swing.JButton btn_XemCT1;
    private javax.swing.JButton btn_XemCT2;
    private javax.swing.JButton btn_XoaCTPX;
    private javax.swing.JButton btn_XoaPX;
    private javax.swing.JButton btn_XuatExcel1;
    private javax.swing.JButton btn_XuatExcel2;
    private javax.swing.JComboBox<String> cboDonViDich;
    private javax.swing.JComboBox<String> cboDonViGoc;
    private javax.swing.JComboBox<String> cboLoaiVatTu;
    private javax.swing.JComboBox<String> cboNhomVatTu;
    private javax.swing.JComboBox<String> cbo_TimKiemCTPX;
    private javax.swing.JComboBox<String> cbo_TimKiemPX;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tab1_PhieuNhap;
    private javax.swing.JTable tbl_ModelChiTietPhieuXuat;
    private javax.swing.JTable tbl_PhieuXuat;
    private javax.swing.JTextField txtKetQua;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txt_Lydo;
    private javax.swing.JTextField txt_MaKho1;
    private javax.swing.JTextField txt_MaNguoiNhan;
    private javax.swing.JTextField txt_MaNguoiXuat;
    private javax.swing.JTextField txt_MaPBYC;
    private javax.swing.JTextField txt_MaPhieuXuat;
    private javax.swing.JTextField txt_MaPhieuXuatCTPX;
    private javax.swing.JTextField txt_MaVatTu;
    private com.toedter.calendar.JDateChooser txt_NgayChungTu;
    private com.toedter.calendar.JDateChooser txt_NgayXuat;
    private javax.swing.JTextField txt_TimKiemCTPX;
    private javax.swing.JTextField txt_TimKiemPX;
    private javax.swing.JTextField txt_soluongXuat;
    // End of variables declaration//GEN-END:variables
}
