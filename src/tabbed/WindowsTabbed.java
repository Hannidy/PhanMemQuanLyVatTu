
package tabbed;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import raven.drawer.Drawer;

//public class WindowsTabbed {
//
//    private static WindowsTabbed instance;
//    private JMenuBar menuBar;
//    private PanelTabbed panelTabbed;
//    private JPanel body;
//    private TabbedForm temp;
//    private final int LIMIT = 5; // -1 for unlimit
//    private final boolean REMOVE_WHEN_LIMIT = false;
//
//    public static WindowsTabbed getInstance() {
//        if (instance == null) {
//            instance = new WindowsTabbed();
//        }
//        return instance;
//    }
//
//    public void install(JFrame frame, JPanel body) {
//        this.body = body;
//        
//        menuBar = new JMenuBar();
//        menuBar.putClientProperty(FlatClientProperties.STYLE, ""
//                + "borderColor:$TitlePane.background;" +
//                "border:0,0,0,0;");
//
//        
//        panelTabbed = new PanelTabbed();
//        panelTabbed.putClientProperty(FlatClientProperties.STYLE, ""
//                + "background:$TitlePane.background;");
////                + "border:0,0,0,0;");
//        menuBar.add(createDrawerButton());
//        menuBar.add(createScroll(panelTabbed));
//        frame.setJMenuBar(menuBar);
//    }
//
//    public void removeAllTabbed() {
//        panelTabbed.removeAll();
//        panelTabbed.repaint();
//        panelTabbed.revalidate();
//        body.removeAll();
//        body.revalidate();
//        body.repaint();
//    }
//
//    public void showTabbed(boolean show) {
//        menuBar.setVisible(show);
//        if (!show) {
//            Drawer.getInstance().closeDrawer();
//        }
//    }
//
//    private JButton createDrawerButton() {
//        JButton cmd = new JButton(new FlatSVGIcon("drawer/icon/menu.svg", 0.45f));
//        cmd.addActionListener((ae) -> {
//            Drawer.getInstance().showDrawer();
//        });
//        cmd.putClientProperty(FlatClientProperties.STYLE, ""
//                + "borderWidth:0;"
//                + "focusWidth:0;"
//                + "innerFocusWidth:0;"
//                + "background:null;"
//                + "arc:5");
//        return cmd;
//    }
//
//    private JScrollPane createScroll(Component com) {
//        JScrollPane scroll = new JScrollPane(com);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//        scroll.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
//                + "width:0");
//        scroll.getHorizontalScrollBar().setUnitIncrement(10);
//        scroll.putClientProperty(FlatClientProperties.STYLE, ""
//                + "border:0,0,0,0");
//        return scroll;
//    }
//
//    public boolean addTab(String title, TabbedForm component) {
//        if (LIMIT != -1 && panelTabbed.getComponentCount() >= LIMIT) {
//            if (REMOVE_WHEN_LIMIT) {
//                panelTabbed.remove(0);
//            } else {
//                return false;
//            }
//        }
//        TabbedItem item = new TabbedItem(title, component);
//        item.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent ae) {
//                showForm(item.getComponent());
//            }
//        });
//        panelTabbed.addTab(item);
//        showForm(component);
//        item.setSelected(true);
//        return true;
//    }
//
//    public void removeTab(TabbedItem tab) {
//        if (tab.getComponent().formClose()) {
//            int index = panelTabbed.getComponentZOrder(tab);
//            boolean removedCurrentView = index == getTabSelectedIndex();
//
//            if (tab.isSelected()) {
//                body.removeAll();
//                body.revalidate();
//                body.repaint();
//            }
//            panelTabbed.remove(tab);
//            panelTabbed.revalidate();
//            panelTabbed.repaint();
//            if (removedCurrentView) {
//                // auto selected tab
//                int selectedIndex = Math.min(index, panelTabbed.getComponentCount() - 1);
//                if (selectedIndex >= 0) {
//                    TabbedItem item = (TabbedItem) panelTabbed.getComponent(selectedIndex);
//                    item.setSelected(true);
//                    showForm(item.getComponent());
//                }
//            }
//        }
//    }
//
//    public void removeTabAt(int index) {
//        Component com = panelTabbed.getComponent(index);
//        if (com instanceof TabbedItem) {
//            removeTab((TabbedItem) com);
//        }
//    }
//
//    public void removeTab(TabbedForm tab) {
//        for (Component com : panelTabbed.getComponents()) {
//            if (com instanceof TabbedItem) {
//                TabbedForm form = ((TabbedItem) com).getComponent();
//                if (form == tab) {
//                    removeTab((TabbedItem) com);
//                }
//            }
//        }
//    }
//
//    public String[] getTabName() {
//        List<String> list = new ArrayList<>();
//        for (Component com : panelTabbed.getComponents()) {
//            if (com instanceof TabbedItem) {
//                String name = ((TabbedItem) com).getTabbedName();
//                list.add(name);
//            }
//        }
//        String[] arr = new String[list.size()];
//        list.toArray(arr);
//        return arr;
//    }
//
//    public int getTabSelectedIndex() {
//        for (Component com : panelTabbed.getComponents()) {
//            if (com instanceof TabbedItem) {
//                if (((TabbedItem) com).isSelected()) {
//                    return panelTabbed.getComponentZOrder(com);
//                }
//            }
//        }
//        return -1;
//    }
//
//    public void showForm(TabbedForm component) {
//        body.removeAll();
//        body.add(component);
//        body.repaint();
//        body.revalidate();
//        panelTabbed.repaint();
//        panelTabbed.revalidate();
//        component.formOpen();
//        temp = component;
//    }
//}


public class WindowsTabbed {

    private static WindowsTabbed instance;
    private JPanel panelTabbedWrapper;
    private PanelTabbed panelTabbed;
    private JPanel body;
    private TabbedForm temp;
    private final int LIMIT = -1;
    private final boolean REMOVE_WHEN_LIMIT = false;

    public static WindowsTabbed getInstance() {
        if (instance == null) {
            instance = new WindowsTabbed();
        }
        return instance;
    }

    public void install(JFrame frame, JPanel body) {
        this.body = body;

        JPanel mainPanel = new JPanel(new BorderLayout());

        panelTabbed = new PanelTabbed();
        panelTabbed.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 0));
        panelTabbed.setPreferredSize(new Dimension(10000, 40)); // rất rộng để JScrollPane buộc phải scroll
        panelTabbed.putClientProperty(FlatClientProperties.STYLE, "background:$TitlePane.background;border:0,0,0,0");

        panelTabbedWrapper = new JPanel(new BorderLayout());
        panelTabbedWrapper.putClientProperty(FlatClientProperties.STYLE, "background:$TitlePane.background;border:0,0,0,0");

        JButton drawerButton = createDrawerButton();
        JPanel drawerPanel = new JPanel();
        drawerPanel.setOpaque(false);
        drawerPanel.add(drawerButton);

        JScrollPane scrollTab = createScroll(panelTabbed);

        panelTabbedWrapper.add(drawerPanel, BorderLayout.WEST);
        panelTabbedWrapper.add(scrollTab, BorderLayout.CENTER);

        mainPanel.add(panelTabbedWrapper, BorderLayout.NORTH);
        mainPanel.add(body, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JButton createDrawerButton() {
        JButton cmd = new JButton(new FlatSVGIcon("drawer/icon/menu.svg", 0.45f));
        cmd.addActionListener((ae) -> {
            Drawer.getInstance().showDrawer();
        });
        cmd.putClientProperty(FlatClientProperties.STYLE, "borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;arc:5");
        return cmd;
    }

    private JScrollPane createScroll(Component com) {
        JScrollPane scroll = new JScrollPane(com);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0,8));// Kích thước của scroll bar
        scroll.getHorizontalScrollBar().setUnitIncrement(7);// Tốc độ lướt của scrollbar
        scroll.putClientProperty(FlatClientProperties.STYLE, "border:0,0,0,0");
        
        // Ctrl + lăn chuột để cuộn ngang tab
        scroll.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    JScrollBar hBar = scroll.getHorizontalScrollBar();
                    int rotation = e.getWheelRotation();
                    hBar.setValue(hBar.getValue() + rotation * 30);
                    e.consume();
                }
            }
        });
        
        return scroll;
    }

    public boolean addTab(String title, TabbedForm component) {
        if (LIMIT != -1 && panelTabbed.getComponentCount() >= LIMIT) {
            if (REMOVE_WHEN_LIMIT) {
                panelTabbed.remove(0); // nếu có giới hạn và bật tự động xóa
            } else {
                return false; // nếu không cho xóa → không thêm tab
            }
        }
        TabbedItem item = new TabbedItem(title, component);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                showForm(item.getComponent());
            }
        });
        panelTabbed.addTab(item);
        showForm(component);
        item.setSelected(true);
        return true;
    }

    public void removeAllTabbed() {
        panelTabbed.removeAll();
        panelTabbed.repaint();
        panelTabbed.revalidate();
        body.removeAll();
        body.revalidate();
        body.repaint();
    }

    public void showTabbed(boolean show) {
        panelTabbedWrapper.setVisible(show);
        if (!show) {
            Drawer.getInstance().closeDrawer();
        }
    }

    public void removeTab(TabbedItem tab) {
        if (tab.getComponent().formClose()) {
            int index = panelTabbed.getComponentZOrder(tab);
            boolean removedCurrentView = index == getTabSelectedIndex();

            if (tab.isSelected()) {
                body.removeAll();
                body.revalidate();
                body.repaint();
            }
            panelTabbed.remove(tab);
            panelTabbed.revalidate();
            panelTabbed.repaint();
            if (removedCurrentView) {
                int selectedIndex = Math.min(index, panelTabbed.getComponentCount() - 1);
                if (selectedIndex >= 0) {
                    TabbedItem item = (TabbedItem) panelTabbed.getComponent(selectedIndex);
                    item.setSelected(true);
                    showForm(item.getComponent());
                }
            }
        }
    }

    public void removeTabAt(int index) {
        Component com = panelTabbed.getComponent(index);
        if (com instanceof TabbedItem) {
            removeTab((TabbedItem) com);
        }
    }

    public void removeTab(TabbedForm tab) {
        for (Component com : panelTabbed.getComponents()) {
            if (com instanceof TabbedItem) {
                TabbedForm form = ((TabbedItem) com).getComponent();
                if (form == tab) {
                    removeTab((TabbedItem) com);
                }
            }
        }
    }

    public String[] getTabName() {
        List<String> list = new ArrayList<>();
        for (Component com : panelTabbed.getComponents()) {
            if (com instanceof TabbedItem) {
                String name = ((TabbedItem) com).getTabbedName();
                list.add(name);
            }
        }
        return list.toArray(new String[0]);
    }

    public int getTabSelectedIndex() {
        for (Component com : panelTabbed.getComponents()) {
            if (com instanceof TabbedItem && ((TabbedItem) com).isSelected()) {
                return panelTabbed.getComponentZOrder(com);
            }
        }
        return -1;
    }

    public void showForm(TabbedForm component) {
        body.removeAll();
        body.add(component);
        body.repaint();
        body.revalidate();
        panelTabbed.repaint();
        panelTabbed.revalidate();
        component.formOpen();
        temp = component;
    }
}