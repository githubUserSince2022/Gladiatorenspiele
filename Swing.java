import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Swing extends JFrame implements IReporter {
    private JButton neueTab = new JButton("Neue Tab");
    private JButton resetTab = new JButton("Reset Tab");
    private JButton ladeTab = new JButton("Lade Tab");
    private JButton sichereTab = new JButton("Sichere Tab");
    private JButton neuerGLad = new JButton("Neuer Gladiator");
    private JButton loescheGlad = new JButton("Loesche Gladiator");
    private JButton neuerKampf = new JButton("Neuer Kampf");
    private JPanel panel;
    private JTextArea area;
    private Arena arena;
    private GuiThreadDecoupler _guiThreadDecoup1;
    private JScrollPane scroll;
    private JComboBox combobox;
    private JComboBox comboBox2;
    private KampfStatTabelle tabelle;
    private GladStat _glad1;
    private GladStat _glad2;
    private JTable table = new JTable();
    private DefaultTableModel model = new DefaultTableModel();

    public Swing(String titel) {

        super(titel);
        area = new JTextArea();

        this.add(area, java.awt.BorderLayout.CENTER);
        area.setEditable(false);
        scroll = new JScrollPane(area);
        this.add(scroll, java.awt.BorderLayout.CENTER);

        arena = new Arena(this);
        tabelle = new KampfStatTabelle();
        panel = new JPanel();
        add(panel, java.awt.BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.add(neueTab);
        panel.add(resetTab);
        panel.add(ladeTab);
        panel.add(sichereTab);
        panel.add(neuerGLad);
        panel.add(loescheGlad);
        panel.add(neuerKampf);
        tabelle.sortArray();
        createTabelle();

        this.setSize(1000, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        neueTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                while (tabelle.gladiatoren.length >= 1) {
                    tabelle.remove(tabelle.gladiatoren[0]);
                }
                updateTabelle();
            }
        });
        resetTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tabelle.reset();
                updateTabelle();
            }
        });
        sichereTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setDialogTitle("Eine Datei speichern");
                int userSelection = fileChooser.showSaveDialog(Swing.this);

                if(userSelection == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser.getSelectedFile();
                    tabelle.schreibeDatei(fileToSave);
                }
            }   
        });
        ladeTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser(".");
                chooser.setDialogTitle("Eine Datei laden");
                int userSelection = chooser.showSaveDialog(Swing.this);
              
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        tabelle.ladeDatei(file);
                        updateTabelle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
               
            }
        });
      
        neuerGLad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JDialog dialog= new JDialog();
                dialog.setTitle("Gladiator hinzufuegen");
                dialog.setVisible(true);
                dialog.setSize(500,100);

                JLabel label= new JLabel("Bitte einen Gladiatorennamen eintragen, der in der Tabelle hinzugefuegt wird");
                dialog.add(label, java.awt.BorderLayout.NORTH);

                JTextField field= new JTextField();

                dialog.add(field,java.awt.BorderLayout.CENTER);
                JButton ok= new JButton("OK");
                dialog.add(ok, java.awt.BorderLayout.SOUTH);

                ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try{
                            for(int i=0; i<tabelle.gladiatoren.length;i++){
                                if(field.getText().equals(tabelle.gladiatoren[i].name)){
                                    field.setText("");
                                   throw new RuntimeException();
                                }
                            }
                            if(field.getText().length()==0) {                       
                               throw new RuntimeException();
                            }
                            else{
                                dialog.setVisible(false);
                                GladStat neu= new GladStat(field.getText());
                                tabelle.add(neu);
                                tabelle.sortArray();
                                model.addRow(new Object[]{
                                    neu.name, neu.gemachteKämpfe, neu.anzahlSiege,neu.niederlagen,neu.siegqoute,neu.status
                                });
                                JOptionPane.showMessageDialog(Swing.this, "Gladiator erfolgreich hinzugefuegt"); 
                                revalidate();
                                 
                            }
                        }
                        catch(Exception ex){
                            JOptionPane.showMessageDialog(Swing.this, "Bitte gib einen Namen im Textfeld ein, der nicht existiert", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                 });
            }
        });
      
        neuerKampf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                JDialog meinJDialog = new JDialog();
                meinJDialog.setTitle("Neuer Kampf");
                meinJDialog.setSize(300, 100);

                JButton starteKampf = new JButton("Starte Kampf");
                meinJDialog.add(starteKampf, java.awt.BorderLayout.SOUTH);
                combobox = new JComboBox<GladStat>(tabelle.gladiatoren);
                comboBox2 = new JComboBox<GladStat>(tabelle.gladiatoren);

                combobox.removeAllItems();
                comboBox2.removeAllItems();


                for(int i = 0; i < tabelle.gladiatoren.length; i++){
                    if(tabelle.gladiatoren[i].status == true){
                        combobox.addItem( tabelle.gladiatoren[i]);
                        comboBox2.addItem( tabelle.gladiatoren[i]);
                    }
                }
                
                JPanel panel= new JPanel();
                meinJDialog.add(panel, java.awt.BorderLayout.CENTER);
                panel.setLayout(new FlowLayout());
                panel.add(combobox);
                panel.add(comboBox2);
                meinJDialog.setVisible(true);
                combobox.setSelectedIndex(-1);
                comboBox2.setSelectedIndex(-1);

                starteKampf.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                   
                        try {
                            _glad1 = (GladStat) combobox.getSelectedItem();
                            _glad2 = (GladStat) comboBox2.getSelectedItem();
                            if (_glad1.equals(_glad2) ||_glad1 == null || _glad2 == null) {
                                throw new RuntimeException();
                            }
                            else {
                                arena.neuerGlad(_glad1,_glad2);
                                meinJDialog.setVisible(false);
                                arena.starteKampf();

                                tabelle.berechne(_glad1,_glad2);
                                ausgabeDesSiegers();
                                updateTabelle();

                                _glad1._le= 30 +((int) (Math.random() * 6 + 1));    // Falls ein Gladiator erneut zum Kämpfen ausgewählt wird, werden seine Lebenspunkte vollständig
                                _glad2._le= 30 +((int) (Math.random() * 6 + 1));
                            }
                        } 
                        catch (Exception ex) {
                            JOptionPane.showMessageDialog(Swing.this, "Waehle zwei verschiedene Gladiatoren aus!", "Neuer Versuch!!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        });

        loescheGlad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JDialog loeschen= new JDialog();
                loeschen.setTitle("Gladitor loeschen");
                loeschen.setSize(300,150);
                loeschen.setVisible(true);
                JButton gladLoeschen=new JButton("Löschen");
                loeschen.add(gladLoeschen,java.awt.BorderLayout.SOUTH);
                JComboBox loeschenInComboBox= new JComboBox<GladStat>(tabelle.gladiatoren);
                loeschen.add(loeschenInComboBox,java.awt.BorderLayout.CENTER);
             
                loeschenInComboBox.setSelectedIndex(-1);
               
                gladLoeschen.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try{
                            if(loeschenInComboBox.getSelectedItem()!=null){
                                int antwort = JOptionPane.showConfirmDialog(Swing.this,"Willst du wirklich den Gladiator " +loeschenInComboBox.getSelectedItem()+" loeschen?","Sicher?", JOptionPane.YES_NO_OPTION);
                                if (antwort == JOptionPane.YES_OPTION) {
                                    int index=loeschenInComboBox.getSelectedIndex();
                                    tabelle.remove((GladStat)loeschenInComboBox.getSelectedItem());
                                    updateTabelle();
                                    loeschen.setVisible(false);
                                    JOptionPane.showMessageDialog(Swing.this, "Gladiator erfolgreich geloescht");  
                                } 
                                else {
                                    loeschen.setVisible(false);
                                }
                            }
                            else{
                                throw new RuntimeException();
                            }
                        }
                        catch(Exception ex){
                            JOptionPane.showMessageDialog(Swing.this, "Sie muessen einen Gladiator zum Loeschen auswaehlen","Fehler",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void giveNewMessage(String str) {
        if (_guiThreadDecoup1 == null) {
            area.setText(area.getText() + "\n" + str);
        } else {
            _guiThreadDecoup1.startGuiUpdate(new Runnable() {
                public void run() {
                    area.setText(area.getText() + "\n" + str);
                }
            }, 100);
        }
    }

    public void createTabelle(){                    // Tabelle erzeugen
        tabelle.sortArray();
        String [] columnNames={"Gladiatoren","Kaempfe","Siege","Niederlagen","Siegquote","Lebendig"};
                                       
        model.setColumnIdentifiers(columnNames);
        table.setModel(model);
        int row=model.getRowCount();
     
        for (int i = row - 1; i >= 0; i--) {
            model.removeRow(i);
        }
      
        for(int i=0;i<tabelle.gladiatoren.length;i++){
            GladStat gl= tabelle.gladiatoren[i];
            model.addRow(new Object[]{
                gl.name, gl.gemachteKämpfe, gl.anzahlSiege,gl.niederlagen,gl.siegqoute,gl.status
            });
            
        }
       
        add( new JScrollPane(table),java.awt.BorderLayout.EAST);
        revalidate();
    }
    public void updateTabelle(){                // Tabelle updaten
        int row=model.getRowCount();
                             
        for (int i = row - 1; i >= 0; i--) {
                model.removeRow(i);
        }    
        tabelle.sortArray();
        for(int i=0;i<tabelle.gladiatoren.length;i++){
            GladStat gl= tabelle.gladiatoren[i];
            model.addRow(new Object[]{
                gl.name, gl.gemachteKämpfe, gl.anzahlSiege,gl.niederlagen,gl.siegqoute,gl.status
            });
        }
        revalidate(); repaint();
    }
    public void ausgabeDesSiegers(){
        if(_glad1.gewonnen==false){
            String a= area.getText();
            area.setText(a + "\n" +_glad2 +" hat gewonnen");
            area.setText(area.getText() + "\n" +_glad1 +" hat verloren");
        }
        else if(_glad1.gewonnen==true){
            String a= area.getText();
            area.setText(a + "\n" +_glad1 +" hat gewonnen");
            area.setText(area.getText() + "\n" +_glad2 +" hat verloren");
        }
        repaint();
    }
    public static void main(String [] args){
        new Swing("Gladiatorenspiel");
    }
    
}