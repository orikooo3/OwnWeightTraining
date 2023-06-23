package com.my.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class MyJCalendar extends JPanel {

    boolean clickCloseFlg;

    Date clickDate;

    ArrayList<Component> clickDateListenerList = new ArrayList<Component>();

    JPanel calenderPanel;

    Calendar now;

    static final Color todayColor = new Color(61, 41, 176);

    ArrayList<DateLabel> labelList = new ArrayList<DateLabel>();

    JComboBox monthCombo = new JComboBox(new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
    JComboBox yearCombo;

    public static void main(String[] args) {
        JFrame sampleFrame = new JFrame();
        sampleFrame.setTitle("カレンダーサンプル");
        sampleFrame.setBounds(200, 100, 350, 300);
        sampleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sampleFrame.setLayout(new FlowLayout());

        JTextField datetf = new JTextField(10);
        JTextField datetf2 = new JTextField(10);
        JButton b = new JButton("カレンダー表示ボタン！");
        b.addActionListener(new ActionListener() {
            JTextField tf;
            JFrame frame;

            public ActionListener setParam(JTextField tf, JFrame frame) {
                this.tf = tf;
                this.frame = frame;
                return this;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                Date date = null;
                try {
                    date = df.parse(tf.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                date = MyJCalendar.showCalenderDialog(frame, b, "日付選択", date);
                if (date != null) {
                    tf.setText(df.format(date));
                }
            }
        }.setParam(datetf, sampleFrame));

        sampleFrame.add(datetf);
        sampleFrame.add(b);
        JSeparator sp = new JSeparator(JSeparator.HORIZONTAL);
        sp.setPreferredSize(new Dimension(320, 2));
        sampleFrame.add(sp);
        MyJCalendar jc = new MyJCalendar();
        jc.addClickDateListenerObject(datetf2);
        sampleFrame.add(jc);
        sampleFrame.add(datetf2);
        sampleFrame.setVisible(true);
    }

    public static Date showCalenderDialog(String title) {
        return showCalenderDialog(null, null, title, null);
    }

    public static Date showCalenderDialog(Window owner, Component component, String title) {
        return showCalenderDialog(owner, component, title, null);
    }

    public static Date showCalenderDialog(Window owner, Component component, String title, Date date) {
        JDialog f = new JDialog(owner, title);
        f.setSize(new Dimension(300, 250));
        if (component != null) {
            // ソースコンポーネント（ボタンとかテキストフィールドとか）がnullでなければソースコンポーネント上にダイアログを表示
            f.setLocationRelativeTo(component);
        }
        if (owner != null) {
            // 親ウィンドウ上にダイアログ表示、nullの場合画面中央に表示
            f.setLocationRelativeTo(owner);
        }

        return showCalenderDialog(f, date);
    }

    private static Date showCalenderDialog(JDialog calendarDialog, Date date) {

        MyJCalendar p = new MyJCalendar(true, date);

        p.addComponentListener(new ComponentAdapter() {
            JDialog d;

            public ComponentAdapter setOwner(JDialog d) {
                this.d = d;
                return this;
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                d.setVisible(false);
            }
        }.setOwner(calendarDialog));

        calendarDialog.add(p);
        calendarDialog.setModal(true);
        calendarDialog.setVisible(true);

        return p.getClickDate();
    }

    public MyJCalendar() {
        init(false, new Date());
    }

    public MyJCalendar(Date date) {
        init(false, date);
    }

    public MyJCalendar(boolean b) {
        init(b, new Date());
    }

    public MyJCalendar(boolean b, Date date) {
        init(b, date);
    }

    ItemListener yearComboItemListener;

    private void init(boolean clickCloseFlg, Date date) {
        this.clickCloseFlg = clickCloseFlg;
        setLayout(new BorderLayout());

        JPanel p = new JPanel();

        JButton bNext = new JButton(">>");
        bNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.MONTH, 1);
                setCalender(now.getTime());
            }
        });

        JButton bPrev = new JButton("<<");

        bPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.MONTH, -1);
                setCalender(now.getTime());
            }
        });

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        y -= 3;

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < 7; i++) {
            model.addElement(y);
            y++;
        }
        yearCombo = new JComboBox(model);

        yearComboItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setCalendarFromCombobox();
                }
            }
        };

        yearCombo.addItemListener(yearComboItemListener);
        monthCombo.addItemListener(yearComboItemListener);

        p.add(bPrev);
        p.add(yearCombo);
        p.add(new JLabel("年"));
        p.add(monthCombo);
        p.add(new JLabel("月"));
        p.add(bNext);
        add(p, BorderLayout.NORTH);

        if (date == null) {
            date = new Date();
        }
        setCalender(date);

        setVisible(true);
    }

    private void setCalendarFromCombobox() {
        int date = now.get(Calendar.DATE);
        int newMonth = (Integer) monthCombo.getSelectedItem() - 1;
        int newYear = (Integer) yearCombo.getSelectedItem();

        GregorianCalendar g = new GregorianCalendar();
        g.set(Calendar.DATE, 1);
        g.set(Calendar.MONTH, newMonth);
        g.set(Calendar.YEAR, newYear);

        int maxDate = g.getMaximum(Calendar.DATE);
        if (maxDate > date) {
            g.set(Calendar.DATE, maxDate);
        } else {
            g.set(Calendar.DATE, date);
        }

        now.setTime(g.getTime());

        setCalender(now.getTime());
    }

    private void setCalendar() {
        setCalender(new Date());
    }

    private JPanel createCalenderPanel() {
        JPanel pp = new JPanel();
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(250, 150));
        Dimension d = new Dimension(30, 20);
        for (int i = 0; i < 42; i++) {
            DateLabel dateLabel = new DateLabel();
            dateLabel.setPreferredSize(d);
            dateLabel.setHorizontalAlignment(JLabel.RIGHT);

            dateLabel.addMouseListener(new MouseAdapter() {
                DateLabel dateLabel;

                public MouseAdapter setParam(DateLabel dateLabel) {
                    this.dateLabel = dateLabel;
                    return this;
                }

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    setClickDate(dateLabel.date);
                };

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    JLabel l = (JLabel) e.getSource();
                    l.setBackground(Color.white);
                };

                public void mouseExited(java.awt.event.MouseEvent e) {
                    JLabel l = (JLabel) e.getSource();
                    int d1 = dateLabel.date.getDate();
                    int d2 = now.get(Calendar.DATE);
                    if (d1 == d2 && (l.getForeground().equals(Color.black) || l.getForeground().equals(Color.red))) {
                        l.setBackground(todayColor);
                    } else {
                        l.setBackground(null);
                    }
                };
            }.setParam(dateLabel));

            dateLabel.setOpaque(true);

            p.add(dateLabel);
            labelList.add(dateLabel);
        }
        pp.add(p);
        add(pp, BorderLayout.CENTER);
        calenderPanel = pp;
        return pp;
    }

    private void setCalender(Date date) {
        if (calenderPanel == null) {
            createCalenderPanel();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        now = (Calendar) c.clone();
        int today = c.get(Calendar.DATE);

        setCombobox(c);
        c.set(Calendar.DATE, 1);

        if (c.get(Calendar.DAY_OF_WEEK) < Calendar.WEDNESDAY) {
            c.add(Calendar.DATE, -c.get(Calendar.DAY_OF_WEEK) + 1);
            c.add(Calendar.DATE, -7);
        } else {
            c.add(Calendar.DATE, -c.get(Calendar.DAY_OF_WEEK) + 1);
        }

        Color color = Color.gray;
        Color sundayColor = Color.pink;
        for (int i = 0; i < 42; i++) {
            if (i < 14) {
                if (c.get(Calendar.DATE) == 1) {
                    color = Color.black;
                    sundayColor = Color.red;
                }
            }
            if (i > 20) {
                if (c.get(Calendar.DATE) == 1) {
                    color = Color.gray;
                    sundayColor = Color.pink;
                }
            }

            DateLabel dateLabel = labelList.get(i);
            dateLabel.setText("" + c.get(Calendar.DATE));
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dateLabel.setForeground(sundayColor);
            } else {
                dateLabel.setForeground(color);
            }
            if (c.get(Calendar.DATE) == today && color.equals(Color.black)) {
                dateLabel.setBackground(todayColor);
            } else {
                dateLabel.setBackground(null);
            }
            dateLabel.date = c.getTime();

            c.add(Calendar.DATE, 1);
        }
    }

    private void setCombobox(Calendar c) {
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        yearCombo.removeItemListener(yearComboItemListener);
        monthCombo.removeItemListener(yearComboItemListener);
        DefaultComboBoxModel model = (DefaultComboBoxModel) yearCombo.getModel();
        Integer min = (Integer) model.getElementAt(0);
        Integer max = (Integer) model.getElementAt(model.getSize() - 1);
        if (min >= year || max <= year) {

            if (min >= year) {
                min = year - 3;
            }
            if (max <= year) {
                max = year + 3;
            }
            yearCombo.removeAllItems();
            for (int i = min; i <= max; i++) {
                yearCombo.addItem(i);
            }
        }
        yearCombo.setSelectedItem(year);
        monthCombo.setSelectedItem(month + 1);
        yearCombo.addItemListener(yearComboItemListener);
        monthCombo.addItemListener(yearComboItemListener);
    }

    class DateLabel extends JLabel {
        Date date;
    }

    private void setClickDate(Date date) {
        clickDate = date;
        if (clickCloseFlg) {
            updateClickDate();
            setVisible(false);
        } else {
            updateClickDate();
            now.setTime(date);
            setCalender(date);
        }
    }

    public Date getClickDate() {
        return clickDate;
    }

    public void addClickDateListenerObject(Component c) {
        clickDateListenerList.add(c);
    }

    private void updateClickDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(clickDate);

        for (Component c : clickDateListenerList) {
            try {
                Method method = c.getClass().getMethod("setText", new Class[] { String.class });
                method.invoke(c, new Object[] { date });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
