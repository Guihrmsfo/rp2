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
import java.awt.*;
import javax.swing.JFrame;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.event.*;
import edu.stanford.smi.protegex.owl.jena.*;
import edu.stanford.smi.protegex.owl.ui.*;
import edu.stanford.smi.protegex.owl.ui.widget.*;
import edu.stanford.smi.protegex.owl.ui.icons.*;
import edu.stanford.smi.protege.util.*;
import java.util.*;
import java.awt.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.util.*;
import javax.swing.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.resource.*;

public class ClsDescChooser extends JPanel implements Disposable 
{

        private ClsesPanel _clsesPanel;          
        private JPanel Buttons;
        private SlotPanel SP;
        
        private boolean _isUpdating; 
        Project p;
        
        private String selected_item = "";
        private JFrame jf;
         
        public void dispose() {
            //...
        }
        
        
        public String getSelectedItem(){
            return selected_item;
        }
        
        // constructor
        ClsDescChooser(SlotPanel SP, Project p, JFrame jf) {
            this.jf = jf;
            this.p = p;
            this.SP = SP;
            this.setLayout(new BorderLayout());
            
            Buttons = new JPanel();
            Buttons.setLayout(new BorderLayout());
            
            add(createClsesPanel(), BorderLayout.CENTER);
            add(Buttons, BorderLayout.SOUTH);
            
            OK = new JButton("OK");
            Buttons.add(OK);
         
            OK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    OKActionPerformed(evt);
                }
            });            
        }
        
        
        private void OKActionPerformed(java.awt.event.ActionEvent evt) {
            System.out.println("lex_entry:" + lex_entry);
            this.selected_item = lex_entry;
            //SP.set_LexEntry(selected_item);
            jf.hide();
            
            //return listPanel.getSelectedEntry();
        }
        
        String lex_entry;
        JButton OK;
        
        private JComponent createClsesPanel() {
            _clsesPanel = new ClsesPanel(p);        
            LabeledComponent LC = _clsesPanel.getLabeledComponent();

            //FrameRenderer renderer = FrameRenderer.createInstance();
            //renderer.setDisplayDirectInstanceCount(true);
            //_clsesPanel.setRenderer(renderer);

            _clsesPanel.addSelectionListener(new SelectionListener() {
                public void selectionChanged(SelectionEvent event) {
                    transmitClsSelection();
                }
            });

            _clsesPanel.getClsesTree().addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    transmitClsSelection();
                }
            });

            return _clsesPanel;
        }        

        private void transmitClsSelection() {
            System.out.println("Selection---");

            Collection selection = _clsesPanel.getSelection();
            Instance selectedInstance = null;
            Cls selectedCls = null;
            Cls selectedParent = null;
            if (selection.size() == 1) {            
                selectedInstance = (Instance) CollectionUtilities.getFirstItem(selection);
                if (selectedInstance instanceof Cls) {
                    selectedCls = (Cls) selectedInstance;
                    this.lex_entry = selectedCls.getBrowserText();
                    
                    selectedParent = _clsesPanel.getDisplayParent();
                }
            }
        }    
        
        
        public String getSelectedEntry(){
            
            return this.lex_entry;
        }
        
        //        
        public static ClsDescChooser showLexiconChooser(SlotPanel SP,Project P){
            
            JFrame frame = new JFrame("Lexicon Chooser");
            ClsDescChooser CDC = new ClsDescChooser(SP , P, frame);
            Container cont = frame.getContentPane();
            cont.setLayout(new BorderLayout());
            cont.add(CDC);

            frame.setBounds(100, 100, 300, 300);
            frame.setVisible(true);   
            
            return CDC;
        }
}//


