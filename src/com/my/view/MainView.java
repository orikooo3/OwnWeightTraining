package com.my.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.my.model.database.DbAccess;

public class MainView extends JFrame implements ActionListener, ItemListener {

  // 定数
  private static String COMMA = ","; // コンマ
  private static String SINGLE_QUOTATION = "'"; // シングルクオート

  // コンテイナー
  private Container cp;

  // カードレイアウト
  private CardLayout cl;
  private JPanel cards;

  // メニュー
  private JMenuBar mb;
  private JMenu mn1;
  private JMenuItem mi1, mi2, mi3;

  // ホーム
  private JPanel homePanel, homeLabelPanel, calendarPanel1, todaysPanel1, todaysPanel2;
  private JButton calendarButton1, todaysButton1;
  private JTextField todaysTextField1;
  private JLabel homeLabel;
  private JTable table1;
  private JScrollPane scrollPane1;

  // 種目
  private JPanel exerciseAddPanel, screenTransitionPanel2, newExercisePanel;
  private JLabel exerciseAddLabel, newExercisePanelLabel;
  private JButton addRecordButton1, updataRecordButton1, deleteRecordButton1;
  private JTable table2;
  private JTextField newExerciseTextField1, newExerciseTextField2;
  private JScrollPane scrollPane2;
  private DefaultTableModel tm;

  // トレーニング
  private JPanel addRecordPanel, tittlePanel, setNumberPanel, screenTransitionPanel3, exercisePanel2;
  private JLabel addRecordLabel, label0, label1, label2, label3, label4;
  private JTextField textField1, textField2, textField3, textField4;
  private JButton addRecordButton2, updataRecordButton2, deleteRecordButton2, calendarButton2;
  private JTable table3;
  private JScrollPane scrollPane3;
  private JComboBox<String> exerciseComboBox1;
  private DefaultComboBoxModel<String> model;

  public MainView() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        showExitDialog();
      }
    });

    // メニューバーの生成
    mb = new JMenuBar();

    // メニューの生成
    mn1 = new JMenu("メニュー");
    mn1.setFont(new Font("メイリオ", Font.BOLD, 15));

    // メニュー項目の生成
    mi1 = new JMenuItem("      ホーム ");
    mi1.setFont(new Font("メイリオ", Font.BOLD, 17));
    mi2 = new JMenuItem("      種  目 ");
    mi2.setFont(new Font("メイリオ", Font.BOLD, 17));
    mi3 = new JMenuItem(" トレーニング ");
    mi3.setFont(new Font("メイリオ", Font.BOLD, 17));

    // イベントリスナーの登録
    mi1.addActionListener(this);
    mi2.addActionListener(this);
    mi3.addActionListener(this);

    // メニューへの追加
    mn1.addSeparator();
    mn1.add(mi1);
    mn1.add(mi2);
    mn1.add(mi3);

    // メニューバーへの追加
    mb.add(mn1);

    // メニューバーをフレームへ追加
    setJMenuBar(mb);

    // トップページ
    homePanel = new JPanel();
    homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

    homeLabelPanel = new JPanel();
    homeLabel = new JLabel("ホーム");
    homeLabel.setFont(new Font("メイリオ", Font.BOLD, 30));
    homeLabelPanel.add(homeLabel);
    homePanel.add(homeLabelPanel);

    calendarPanel1 = new JPanel();
    calendarButton1 = new JButton("本日のトレーニング");
    calendarButton1.setFont(new Font("メイリオ", Font.BOLD, 15));
    calendarPanel1.add(calendarButton1);
    homePanel.add(calendarPanel1);

    todaysPanel1 = new JPanel();
    todaysTextField1 = new JTextField(10);
    todaysTextField1.setFont(new Font("メイリオ", Font.BOLD, 15));
    todaysPanel1.add(todaysTextField1);
    homePanel.add(todaysPanel1);

    calendarButton1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showCalendarDialog((JFrame) homePanel.getTopLevelAncestor(), todaysTextField1);
      }
    });

    todaysPanel2 = new JPanel();
    todaysButton1 = new JButton("表示");
    todaysButton1.setFont(new Font("メイリオ", Font.BOLD, 15));
    todaysPanel2.add(todaysButton1);
    todaysButton1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        todaysTrainingSelect();
      }
    });
    homePanel.add(todaysPanel2);

    table1 = new JTable();
    table1.setFont(new Font("メイリオ", Font.BOLD, 15));
    table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    scrollPane1 = new JScrollPane(table1);
    scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    homePanel.add(scrollPane1);

    // 種目登録
    exerciseAddPanel = new JPanel();
    exerciseAddPanel.setLayout(new BoxLayout(exerciseAddPanel, BoxLayout.Y_AXIS));
    exerciseAddLabel = new JLabel("    種  目       ");
    exerciseAddLabel.setFont(new Font("メイリオ", Font.BOLD, 30));
    exerciseAddPanel.add(exerciseAddLabel);

    newExercisePanel = new JPanel();
    newExercisePanelLabel = new JLabel("種目 :");
    newExercisePanelLabel.setFont(new Font("メイリオ", Font.BOLD, 20));
    newExercisePanel.add(newExercisePanelLabel);
    newExerciseTextField2 = new JTextField(15);
    newExerciseTextField2.setFont(new Font("メイリオ", Font.PLAIN, 15));
    newExercisePanel.add(newExerciseTextField2);
    exerciseAddPanel.add(newExercisePanel);

    screenTransitionPanel2 = new JPanel();
    addRecordButton1 = new JButton(" 追加 ");
    addRecordButton1.setFont(new Font("メイリオ", Font.BOLD, 15));
    addRecordButton1.addActionListener(this);
    screenTransitionPanel2.add(addRecordButton1);

    deleteRecordButton1 = new JButton(" 削除 ");
    deleteRecordButton1.setFont(new Font("メイリオ", Font.BOLD, 15));
    deleteRecordButton1.addActionListener(this);
    screenTransitionPanel2.add(deleteRecordButton1);

    exerciseAddPanel.add(screenTransitionPanel2);

    table2 = new JTable();
    table2.setFont(new Font("メイリオ", Font.BOLD, 15));
    table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    scrollPane2 = new JScrollPane(table2);
    scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    exerciseAddPanel.add(scrollPane2);

    // トレーニング記録
    addRecordPanel = new JPanel();
    addRecordPanel.setLayout(new BoxLayout(addRecordPanel, BoxLayout.Y_AXIS));

    addRecordLabel = new JLabel("トレーニング   ");
    addRecordLabel.setFont(new Font("メイリオ", Font.BOLD, 30));
    addRecordPanel.add(addRecordLabel);

    exercisePanel2 = new JPanel();
    label0 = new JLabel("種目 :");
    label0.setFont(new Font("メイリオ", Font.BOLD, 12));
    exercisePanel2.add(label0);

    String[] combodata = {};
    model = new DefaultComboBoxModel<>(combodata);
    exerciseComboBox1 = new JComboBox<>(model);
    exerciseComboBox1.setFont(new Font("メイリオ", Font.PLAIN, 1５));
    exerciseComboBox1.setPreferredSize(new Dimension(200, 26));
    exerciseComboBox1.addItemListener(this);
    exercisePanel2.add(exerciseComboBox1);
    addRecordPanel.add(exercisePanel2);

    // ラベル
    tittlePanel = new JPanel();
    label1 = new JLabel("種目 :");
    label1.setFont(new Font("メイリオ", Font.BOLD, 11));
    label1.setPreferredSize(new Dimension(100, 100));
    label1.setHorizontalAlignment(JLabel.LEFT);
    label1.setVerticalAlignment(JLabel.BOTTOM);
    tittlePanel.add(label1);

    label2 = new JLabel("セット:");
    label2.setFont(new Font("メイリオ", Font.BOLD, 11));
    label2.setPreferredSize(new Dimension(100, 100));
    label2.setHorizontalAlignment(JLabel.LEFT);
    label2.setVerticalAlignment(JLabel.BOTTOM);
    tittlePanel.add(label2);

    label3 = new JLabel("回 数:");
    label3.setFont(new Font("メイリオ", Font.BOLD, 11));
    label3.setPreferredSize(new Dimension(100, 100));
    label3.setHorizontalAlignment(JLabel.RIGHT);
    label3.setVerticalAlignment(JLabel.BOTTOM);
    tittlePanel.add(label3);

    label4 = new JLabel("日付:");
    label4.setFont(new Font("メイリオ", Font.BOLD, 12));
    label4.setPreferredSize(new Dimension(100, 100));
    label4.setHorizontalAlignment(JLabel.RIGHT);
    label4.setVerticalAlignment(JLabel.BOTTOM);
    tittlePanel.add(label4);

    addRecordPanel.add(tittlePanel);

    // フィールド
    setNumberPanel = new JPanel();
    textField1 = new JTextField(13);
    textField1.setFont(new Font("メイリオ", Font.BOLD, 12));
    setNumberPanel.add(textField1);

    textField2 = new JTextField(2);
    textField2.setFont(new Font("メイリオ", Font.BOLD, 12));
    setNumberPanel.add(textField2);

    textField3 = new JTextField(2);
    textField3.setFont(new Font("メイリオ", Font.BOLD, 12));
    setNumberPanel.add(textField3);

    textField4 = new JTextField(9);
    textField4.setFont(new Font("メイリオ", Font.BOLD, 12));
    setNumberPanel.add(textField4);

    addRecordPanel.add(setNumberPanel);

    calendarButton2 = new JButton("カレンダー");
    calendarButton2.setFont(new Font("メイリオ", Font.BOLD, 7));
    calendarButton2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showCalendarDialog((JFrame) addRecordPanel.getTopLevelAncestor(), textField4);
      }
    });
    setNumberPanel.add(calendarButton2);
    addRecordPanel.add(setNumberPanel);

    screenTransitionPanel3 = new JPanel();
    addRecordButton2 = new JButton(" 追加 ");
    addRecordButton2.setFont(new Font("メイリオ", Font.BOLD, 15));
    addRecordButton2.addActionListener(this);
    screenTransitionPanel3.add(addRecordButton2);

    deleteRecordButton2 = new JButton(" 削除 ");
    deleteRecordButton2.setFont(new Font("メイリオ", Font.BOLD, 15));
    deleteRecordButton2.addActionListener(this);
    screenTransitionPanel3.add(deleteRecordButton2);
    addRecordPanel.add(screenTransitionPanel3);

    table3 = new JTable();
    table3.setFont(new Font("メイリオ", Font.BOLD, 15));
    table3.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    scrollPane3 = new JScrollPane(table3);
    scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    addRecordPanel.add(scrollPane3);
    add(addRecordPanel);

    // CardLayout
    cards = new JPanel(new CardLayout());
    cards.add(homePanel, "home");
    cards.add(exerciseAddPanel, "exerciseAdd");
    cards.add(addRecordPanel, "addRecord");
    add(cards);

    // データ表示
    exerciseSelect(); // 種目表示
    trainingSelect(); // 記録表示
    comboBoxSelect(); // コンボボックスのデータ表示

  }

  public void actionPerformed(ActionEvent e) {
    cl = (CardLayout) (cards.getLayout());
    Object obj = e.getSource();
    /* メニューカード表示 */
    if (e.getSource() == mi1) {
      cl.show(cards, "home");
    } else if (e.getSource() == mi2) {
      cl.show(cards, "exerciseAdd");
    } else if (e.getSource() == mi3) {
      cl.show(cards, "addRecord");

      /*
       * newExercisePanel 追加、更新、削除
       */
    } else if (obj == addRecordButton1) {
      exerciseInsert();
    } else if (obj == deleteRecordButton1) {
      exerciseDelete();
      /*
       * addRecordPanel 追加、更新、削除
       */
    } else if (obj == addRecordButton2) {
      trainingInsert();
    } else if (obj == deleteRecordButton2) {
      trainingDelete();
    }
  }

  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      String data = (String) model.getSelectedItem();
      textField1.setText(data);
    }
  }

  public void showExitDialog() {
    // 終了ダイアログボックスの表示
    int ret = JOptionPane.showConfirmDialog(cp, "終了", "確認", JOptionPane.YES_NO_OPTION);

    if (ret == JOptionPane.YES_OPTION) {
      System.exit(0);
    }
  }

  public static void showCalendarDialog(JFrame parentFrame, JTextField textField) {

    JDialog dialog = new JDialog(parentFrame, "カレンダー選択", true);
    dialog.setLayout(new BorderLayout());
    dialog.setSize(300, 250);
    dialog.setLocationRelativeTo(parentFrame);

    MyJCalendar calendar = new MyJCalendar(true, Calendar.getInstance().getTime());
    calendar.addClickDateListenerObject(textField);

    JPanel calendarPanel = new JPanel(new BorderLayout());
    calendarPanel.add(calendar, BorderLayout.CENTER);
    dialog.add(calendarPanel, BorderLayout.CENTER);

    JButton closeButton = new JButton("閉じる");
    closeButton.addActionListener(e -> dialog.dispose());

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(closeButton);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setVisible(true);
  }

  // 種目表示
  public void todaysTrainingSelect() {
    // 全件表示
    String record_date = todaysTextField1.getText();

    try {
      // SQL文の組み立て
      // 画面で指定された条件を組み込む
      String mySql = "SELECT  *  FROM records WHERE date = '" + record_date + " ' ";
      System.out.println(mySql);

      // データを取得し、JTableにセットする TableModel の形に編集
      DbAccess db = new DbAccess();
      // データベースに接続
      db.open();
      // 検索するSQL実行
      ResultSet rs = db.executeQuery(mySql);
      // データ表示
      todaysTrainingDisplay(rs);
      // オブジェクトを解放
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void todaysTrainingDisplay(ResultSet rs) {
    try {
      ArrayList<String[]> list = new ArrayList<String[]>();
      while (rs.next()) {
        String[] items = new String[5];
        items[0] = rs.getString("record_id"); // IDを取得して配列に格納
        items[1] = rs.getString("exercise_id"); // 種目を取得して配列に格納
        items[2] = rs.getString("set_times"); // セット数を取得して配列に格納
        items[3] = rs.getString("training_reps"); // 回数を取得して配列に格納
        items[4] = rs.getString("record_date"); // 日付を取得して配列に格納
        list.add(items); // 配列をリストに追加
      }
      // JTable にセットするためのデータ準備
      String[] columnHeader = { "ID", "種目", "セット数", "回数", "日付" };

      String[][] data = new String[list.size()][5];
      for (int i = 0; i < list.size(); i++) {
        data[i] = list.get(i);
      }
      // JTable にデータをセット
      DefaultTableModel tm = new DefaultTableModel(data, columnHeader);
      table1.setModel(tm);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void comboBoxSelect() {
    // 全件表示
    try {
      // SQL文の組み立て
      // 画面で指定された条件を組み込む
      String mySql = " SELECT exercise_name  FROM exercises ORDER BY exercise_id";
      System.out.println("コンボボックス  " + mySql);

      // データを取得し、JTableにセットする TableModel の形に編集
      DbAccess db = new DbAccess();
      // データベースに接続
      db.open();
      // 検索するSQL実行
      ResultSet rs = db.executeQuery(mySql);
      // データ表示
      comboBoxDisplay(rs);
      // オブジェクトを解放
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void comboBoxDisplay(ResultSet rs) {
    // 追加
    try {
      ArrayList<String> list = new ArrayList<>();
      while (rs.next()) {
        String exercise_name = rs.getString("exercise_name");
        list.add(exercise_name);
      }

      // モデルをクリアしてから値を追加する
      model.removeAllElements();
      for (String exercise_name : list) {
        model.addElement(exercise_name);
      }

    } catch (Exception e) {
      e.printStackTrace();

    }
  }

  public void exerciseDisplay(ResultSet rs) {
    try {
      ArrayList<String[]> list = new ArrayList<String[]>();
      while (rs.next()) {
        String[] items = new String[2];
        items[0] = rs.getString("exercise_id");
        items[1] = rs.getString("exercise_name");
        list.add(items);
      }
      // JTable にセット
      String[] columnHeader = { "ID", "種目" };
      tm = new DefaultTableModel((String[][]) list.toArray(new String[0][0]), columnHeader);
      table2.setModel(tm);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void exerciseSelect() {
    // 全件表示
    try {
      // SQL文の組み立て
      // 画面で指定された条件を組み込む
      StringBuffer mySql = new StringBuffer();
      mySql.append("SELECT * FROM");
      mySql.append(" exercises");
      mySql.append(" ORDER BY");
      mySql.append(" exercise_id");
      System.out.println(mySql);

      // データを取得し、JTableにセットする TableModel の形に編集
      DbAccess db = new DbAccess();
      // データベースに接続
      db.open();
      // 検索するSQL実行
      ResultSet rs = db.executeQuery(mySql.toString());
      // データ表示
      exerciseDisplay(rs);
      // オブジェクトを解放
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void exerciseInsert() {
    String title = "エラー";
    try {
      String exercise_name = newExerciseTextField2.getText();
      // 種目が入力されていない
      if (exercise_name.isEmpty()) {
        JOptionPane.showMessageDialog(null, "種目が入力されていません", title, JOptionPane.ERROR_MESSAGE);
        return;
      }

      // 種目がすでに存在するとき
      if (exerciseComboBox1.getItemCount() > 0 && exerciseComboBox1.getSelectedItem().equals(exercise_name)) {
        JOptionPane.showMessageDialog(null, "種目がすでに存在します", title, JOptionPane.ERROR_MESSAGE);
        return;
      }

      StringBuffer mySql = new StringBuffer();
      mySql.append("INSERT INTO exercises VALUES(");
      mySql.append(SINGLE_QUOTATION + exercise_name + SINGLE_QUOTATION);
      mySql.append(")");
      System.out.println(mySql);

      DbAccess db = new DbAccess();
      db.open();
      db.executeUpdate(mySql.toString());

      // トレーニングパネルのコンボボックスにデータを追加
      model.insertElementAt(exercise_name);
      newExerciseTextField1.setText("");
      newExerciseTextField2.setText("");

      db.close();
      exerciseSelect();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void exerciseDelete() {
    // メッセージボックスのタイトル
    String title = "エラー";

    try {
      String exercise_name = newExerciseTextField2.getText();

      // コンボボックスのサイズを取得
      int comboSize = model.getSize();
      if ((exercise_id < 0) || (0 == comboSize)) {
        JOptionPane.showMessageDialog(null, "データがありません", title, JOptionPane.ERROR_MESSAGE);
        return;
      }

      // 削除対象の行のIDが存在するか確認
      boolean idExists = false;
      /*
       * iにIDを指定してそのIDに設定されてる種目を取得し、テキストフィールドに入力した種目と比較し同じだったらbreakする。
       */
      String tableName = model.getElementAt(exercise_id - 1).toString();
      if (exercise_name.equals(tableName)) {
        idExists = true;
        return;
      }

      // 削除対象の種目がない
      if (!idExists) {
        JOptionPane.showMessageDialog(null, "指定された種目は存在しません", title, JOptionPane.ERROR_MESSAGE);
        return;
      }

      // DELETE
      StringBuffer mySql = new StringBuffer();
      mySql.append("DELETE FROM");
      mySql.append(" exercises");
      mySql.append(" WHERE");
      mySql.append(" exercise_id = ");
      mySql.append(exercise_id);
      System.out.println(mySql);

      // コンボボックスのデータを削除
      model.removeElementAt(exercise_id - 1); // indexが0からだから
      newExerciseTextField1.setText("");
      newExerciseTextField2.setText("");

      DbAccess db = new DbAccess();
      db.open();
      db.executeUpdate(mySql.toString());
      db.close();
      exerciseSelect();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void trainingDisplay(ResultSet rs) {
    try {
      ArrayList<String[]> list = new ArrayList<String[]>();
      while (rs.next()) {
        String[] items = new String[5];
        items[0] = rs.getString("exercise_name");
        items[1] = rs.getString("set_times");
        items[2] = rs.getString("training_reps");
        items[3] = rs.getString("record_date");
        list.add(items);
      }

      String[] columnHeader = { "種目", "セット数", "回数", "日付" };
      DefaultTableModel tm = new DefaultTableModel((String[][]) list.toArray(new String[0][0]), columnHeader);
      // JTable にセット
      table3.setModel(tm);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void trainingSelect() {
    // 全件表示
    try {
      // SQL文の組み立て
      // 画面で指定された条件を組み込む
      StringBuffer mySql = new StringBuffer();
      mySql.append("SELECT * FROM");
      mySql.append(" records");
      mySql.append(" ORDER BY");
      mySql.append(" record_id");
      System.out.println(mySql);

      // データを取得し、JTableにセットする TableModel の形に編集
      DbAccess db = new DbAccess();
      // データベースに接続
      db.open();
      // 検索するSQL実行
      ResultSet rs = db.executeQuery(mySql.toString());
      // データ表示
      trainingDisplay(rs);
      // オブジェクトを解放
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void trainingInsert() {
    // 追加
    String exercise_name = textField1.getText();
    textField1.setText("");
    String set_times = textField2.getText();
    textField2.setText("");
    String training_reps = textField3.getText();
    textField3.setText("");
    String record_date = textField4.getText();
    textField4.setText("");
    try {

      StringBuffer mySql = new StringBuffer();
      mySql.append("INSERT INTO");
      mySql.append(" records");
      mySql.append(" (exercise_name, set_times, training_reps, record_date)");
      mySql.append(" VALUES(");
      mySql.append(SINGLE_QUOTATION + exercise_name + SINGLE_QUOTATION + COMMA);
      mySql.append(set_times + COMMA);
      mySql.append(training_reps + COMMA);
      mySql.append(SINGLE_QUOTATION + record_date + SINGLE_QUOTATION + ")");
      System.out.println(mySql);

      DbAccess db = new DbAccess();
      db.open();
      db.executeUpdate(mySql.toString());
      db.close();
      trainingSelect();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void trainingDelete() {
    // IDと種目名
    String exercise_name = textField1.getText();
    String record_date = textField4.getText();
    textField4.setText("");
    try {
      StringBuffer mySql = new StringBuffer();
      mySql.append(" DELETE FROM");
      mySql.append(" records");
      mySql.append(" WHERE");
      mySql.append(" exercise_name = ");
      mySql.append(SINGLE_QUOTATION + exercise_name + SINGLE_QUOTATION);
      mySql.append(" AND");
      mySql.append(" record_date =");
      mySql.append(SINGLE_QUOTATION + record_date + SINGLE_QUOTATION);
      System.out.println(mySql);

      DbAccess db = new DbAccess();
      db.open();
      db.executeUpdate(mySql.toString());
      // IDの更新
      StringBuffer updateSql = new StringBuffer();
      db.executeUpdate(updateSql.toString());
      db.close();
      trainingSelect();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    MainView frame = new MainView();

    frame.setTitle("    自重トレmemo    ");
    frame.setBounds(100, 100, 700, 550);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    frame.setVisible(true);
  }
}