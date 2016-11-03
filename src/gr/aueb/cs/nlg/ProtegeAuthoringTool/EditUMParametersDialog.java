/*
    NaturalOWL. 
    Copyright (C) 2008  Dimitrios Galanis and Giorgos Karakatsiotis.
    Natural Language Processing Group, Department of Informatics, 
    Athens University of Economics and Business, Greece.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
package gr.aueb.cs.nlg.ProtegeAuthoringTool;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.awt.Dimension;        
import java.awt.event.*;
import java.awt.*;

import gr.aueb.cs.nlg.Languages.*;

public class EditUMParametersDialog extends JFrame implements ActionListener
{	
    private static JDialog dialog;
    private String frameTitle;
    
    private JPanel ButtonsPanel;
    private JButton okButton;
    private JButton cancelButton;
    private EditUMParametersDialogPnl EdtUMParamPnl;    
    private JEditorPane infoBar;    
    private boolean OKclicked;
            
    public EditUMParametersDialog(Vector<String> names)
    {         
        try
        {
            dialog = new JDialog(this, "mitsos", true);

            EdtUMParamPnl = new EditUMParametersDialogPnl(names);

            ButtonsPanel = new JPanel(); 

            okButton = new JButton("  Ok  ");
            cancelButton = new JButton("Cancel");
            infoBar = new JEditorPane();
            infoBar.setContentType("text/html");
            infoBar.setEditable(false);
            
            JScrollPane scroller = new JScrollPane();
            JViewport vp = scroller.getViewport();
            vp.add(infoBar);
            
            
            infoBar.setSize(300, 50);
            infoBar.setMaximumSize(new Dimension(300,50));
            infoBar.setPreferredSize(new Dimension(300,50));
            //infoBar 
                    
            ButtonsPanel.setLayout(new javax.swing.BoxLayout(ButtonsPanel, javax.swing.BoxLayout.X_AXIS));
            ButtonsPanel.add(okButton);
            ButtonsPanel.add(cancelButton);
            
            Container contentPane = dialog.getContentPane();
            contentPane.setLayout(new javax.swing.BoxLayout(contentPane, javax.swing.BoxLayout.Y_AXIS));        
            
            contentPane.add(scroller);
            contentPane.add(EdtUMParamPnl);
            contentPane.add(ButtonsPanel);        
            
            okButton.addActionListener(this);
            cancelButton.addActionListener(this);
            
            dialog.setSize(300, 100);
                    
  
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean showDlg(String title, String info)
    {
        // show the dialog
        dialog.setTitle(title);
        dialog.setResizable(false);
        
        infoBar.setText("<html><font color='black'>" +  info + "</html>");
        
        
        dialog.setAlwaysOnTop(true);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = dialog.getSize();
        dialog.setLocation( (screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2 );
        dialog.setVisible(true);
        
        return OKclicked;
    }
            
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == okButton) 
        {
            OKclicked = true;
            dialog.dispose();
        }

        if (e.getSource() == cancelButton) 
        {
            OKclicked = false;
            dialog.dispose();
        }        
    }
    
    public Vector getData()
    {
        Vector data = EdtUMParamPnl.getData();
                
        for(int i = 0; i < data.size(); i++)
        {
            Vector row = (Vector)data.get(i);
            for(int j = 0; j < row.size(); j++)
            {
                if(row.get(j).toString().compareTo("undefined") == 0)
                {
                    row.setElementAt("-1", j);
                }
            }
        }        
        
        return data;
    }

    public void setData(Vector data)
    {
        EdtUMParamPnl.setData(data);
    }
    

    
    public void setCheckEditableFromTable(boolean b)
    {
        this.EdtUMParamPnl.CheckEditableFromTable = b;
    }
            
    public void setEditable(Vector editableV)
    {
        this.EdtUMParamPnl.editableVector = editableV;
    }
    
    // A main mathod for testing

    public static void main(String args[])
    {        
        Vector<String> x = new Vector();
        x.add("xxxx");
        x.add("yyy");
        EditUMParametersDialog edit = new EditUMParametersDialog(x);
                        
        Vector v = new Vector();
        Vector row = new Vector();
        
        row.add("UT");
        row.add("20");
        row.add("21");
        
        v.add(row);
        row = new Vector();  
        
        row.add("UT");
        row.add("30");
        row.add("32");
        
        v.add(row);
        edit.setData(v);
        
        edit.showDlg("testing...", "dsfhsldhfl;fkhaksdhfaksdfkasdfk;asdh;fkahskdfha;sdkfhasdflaksd" +
                "dsfhsldhfl;fkhaksdhfaksdfkasdfk;asdh;fkahskdfha;sdkfhasdflaksd");
    }   
}

/***********************************************/

class EditUMParametersDialogPnl extends JPanel 
{
    private JTable UMPropertyParametersTbl;
    private DefaultTableModel dataModel;
    
    private JScrollPane scrollpane;
    private Vector myDATA;
    private Vector<String> names;
    private int spacing = 5;
    private int height = 30;
       
    private JDialog dialog;
    
    private JComboBox combobox;
    private DefaultCellEditor editor;
    private DefaultTableCellRenderer colRenderer;
                
    public Vector editableVector;
    public boolean CheckEditableFromTable = false;
    
    public EditUMParametersDialogPnl(Vector<String> columnNames) 
    {
        CheckEditableFromTable = false;
        initComponents();

        
        myDATA  = new Vector(); 
        names = new Vector();
       
        names.add("User Type");

        for(int i = 0; i < columnNames.size(); i++)
        {
            names.add(columnNames.get(i));
        }

        //this is only testing
        //for(int i = 0; i < 10; i++ )
        //{
        //    Vector row = new Vector();
        //    row.add("Mitsos");
        //    row.add("-1");
        //    row.add("2");
            
        //    myDATA.add(row);                    
        //}
        
        
        dataModel = new DefaultTableModel(myDATA, names)  
        {
             public boolean isCellEditable(int row, int col) 
             {               
                 if(col !=0)
                 {
                    if(CheckEditableFromTable)
                    {
                        if( (Boolean)((Vector)editableVector.get(row)).get(col) == true)
                            return true;
                        else
                            return false;
                    }
                    else
                    {
                        return true;
                    }
                 }
                 else
                 {                     
                     return false;
                 }
             } 

       };        
       
        UMPropertyParametersTbl = new JTable(dataModel);
        
        UMPropertyParametersTbl.setShowHorizontalLines(false);
        UMPropertyParametersTbl.setShowVerticalLines(false);                
        UMPropertyParametersTbl.setIntercellSpacing(new Dimension(spacing, spacing));                                                        
        UMPropertyParametersTbl.setRowHeight(height); 
        UMPropertyParametersTbl.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);   
        UMPropertyParametersTbl.getTableHeader().setReorderingAllowed(false); 
        
        //UMPropertyParametersTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                
        combobox = new JComboBox();
        
        combobox.addItem("undefined");        
        for(int i = 0; i < 20; i++)
            combobox.addItem(i + "");
                             
        editor = new DefaultCellEditor(combobox){
            public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) 
            {
                //System.out.println(" value" + value.toString());            
                combobox.setSelectedItem(value);
                return combobox;
            }
        };
        
        
        TableColumn tc = null;
        
        for(int i = 0; i <names.size(); i++)
        {
            tc = UMPropertyParametersTbl.getColumn(names.get(i));
            tc.setCellRenderer(colRenderer);
        }
                
        scrollpane = new JScrollPane(UMPropertyParametersTbl);
        this.add(scrollpane);                
        
    }
    
    public void setData(Vector data)
    {
        Vector the_data = data;
        for(int i = 0; i < the_data.size(); i++)
        {
            Vector row = (Vector)the_data.get(i);
            
            for(int j = 0; j < row.size(); j++)
            {
                if(row.get(j).toString().compareTo("-1") == 0)
                {
                    row.setElementAt("undefined", j);
                }
                
                if( j == 0)
                {
                    int index = row.get(j).toString().indexOf("#");
                    String userType = row.get(j).toString().substring(index+1);
                    row.setElementAt(userType, j);
                }
            }
        }
            
        dataModel.setDataVector(the_data, names);
        
        for(int i = 1; i <names.size(); i++)
        {
            UMPropertyParametersTbl.getColumn(names.get(i)).setCellEditor(editor);
            FixWidth(names.get(i), 100); 
        }
        

    }
    
    private void FixWidth(String columnName, int width)
    {
        UMPropertyParametersTbl.getColumn(columnName).setPreferredWidth(width);
        UMPropertyParametersTbl.getColumn(columnName).setMinWidth(width);            
        UMPropertyParametersTbl.getColumn(columnName).setMaxWidth(width);   
        
    }
    
    public Vector getData()
    {        
        return dataModel.getDataVector();                
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}






