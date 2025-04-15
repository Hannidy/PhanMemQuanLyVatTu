
package form;

import dao.ThongKeDAO;
import tabbed.TabbedForm;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import raven.chart.ChartLegendRenderer;
import raven.chart.bar.HorizontalBarChart;
import raven.chart.data.category.DefaultCategoryDataset;
import raven.chart.data.pie.DefaultPieDataset;
import raven.chart.line.LineChart;
import raven.chart.pie.PieChart;

public class ThongKe_Form extends TabbedForm {

    public ThongKe_Form() {
        init();
    }

    public void formRefresh() {
        loadThongKe();
        lineChart.startAnimation();
        pieChart1.startAnimation();
        pieChart2.startAnimation();
        pieChart3.startAnimation();
        barChart1.startAnimation();
        barChart2.startAnimation();
    }

    public void formInitAndOpen() {
        System.out.println("init and open");
    }

    @Override
    public void formOpen() {
        System.out.println("Open Duy Dep Trai");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,gap 10", "fill"));
        createFilterPanel();
        createPieChart();
        createLineChart();
//        createBarChart();
        loadThongKe(); // mặc định khi mở form
    }

    private void createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5,$Component.borderColor,,20");

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);

        cboNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 4; i <= currentYear; i++) cboNam.addItem(i);

        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        cboNam.setSelectedItem(currentYear);

        cboThang.addActionListener(e -> loadThongKe());
        cboNam.addActionListener(e -> loadThongKe());

        filterPanel.add(new JLabel("Tháng:"));
        filterPanel.add(cboThang);
        filterPanel.add(new JLabel("Năm:"));
        filterPanel.add(cboNam);

        add(filterPanel, "wrap");
    }

    private void createPieChart() {
        pieChart1 = new PieChart();
        pieChart1.setHeader(new JLabel("Tỷ lệ Phiếu Nhập"));
        pieChart1.setDataset(new DefaultPieDataset<>());
        add(pieChart1, "split 3,height 290");

        pieChart2 = new PieChart();
        pieChart2.setHeader(new JLabel("Tỷ lệ Phiếu Xuất"));
        pieChart2.setDataset(new DefaultPieDataset<>());
        add(pieChart2, "height 290");

        pieChart3 = new PieChart();
        pieChart3.setHeader(new JLabel("Tồn kho theo loại vật tư"));
        pieChart3.setDataset(new DefaultPieDataset<>());
        add(pieChart3, "height 290");
    }

    private void createLineChart() {
        lineChart = new LineChart();
        lineChart.setChartType(LineChart.ChartType.CURVE);
        lineChart.setHeader(new JLabel("Biểu đồ Nhập - Xuất theo ngày"));
        add(lineChart);
    }



    private void loadThongKe() {
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ThongKeDAO dao = new ThongKeDAO();

        DefaultPieDataset<String> dsNhap = new DefaultPieDataset<>();
        int pn = dao.getSoPhieuNhapTheoThang(thang, nam);
        dsNhap.setValue("Phiếu Nhập", pn);
        dsNhap.setValue("Còn lại", 100 - pn);
        pieChart1.setDataset(dsNhap);

        DefaultPieDataset<String> dsXuat = new DefaultPieDataset<>();
        int px = dao.getSoPhieuXuatTheoThang(thang, nam);
        dsXuat.setValue("Phiếu Xuất", px);
        dsXuat.setValue("Còn lại", 100 - px);
        pieChart2.setDataset(dsXuat);

        DefaultPieDataset<String> dsTon = new DefaultPieDataset<>();
        List<Object[]> tonKho = dao.getSoLuongTonTheoLoaiVatTu();
        for (Object[] row : tonKho) {
            dsTon.setValue((String) row[0], (int) row[1]);
        }
        pieChart3.setDataset(dsTon);

        DefaultCategoryDataset<String, String> lineDS = new DefaultCategoryDataset<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        for (Object[] row : dao.getNhapXuatTheoNgay(thang, nam)) {
            String ngay = sdf.format((Date) row[0]);
            lineDS.addValue((int) row[1], "Nhập", ngay);
            lineDS.addValue((int) row[2], "Xuất", ngay);
        }
        lineChart.setCategoryDataset(lineDS);
    }

    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private LineChart lineChart;
    private HorizontalBarChart barChart1;
    private HorizontalBarChart barChart2;
    private PieChart pieChart1;
    private PieChart pieChart2;
    private PieChart pieChart3;

    // Các biến JPanel pnlPieNhap, pnlPieXuat, pnlBar, pnlTonKho và JComboBox cboThang, cboNam cần được khai báo và khởi tạo trong initComponents hoặc giao diện thiết kế
}
