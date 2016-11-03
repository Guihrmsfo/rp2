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

import java.awt.Component;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.table.*;

 public class IntegerCellEditor extends AbstractCellEditor implements TableCellEditor {
        final JSpinner spinner = new JSpinner();
    
        // Initializes the spinner.
        public IntegerCellEditor() 
        {
            
        }
    
        // Prepares the spinner component and returns it.
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
        {
            spinner.setValue(value);
            return spinner;
        }
    
        // Enables the editor only for double-clicks.
        public boolean isCellEditable(EventObject evt) 
        {
            if (evt instanceof MouseEvent) {
                return ((MouseEvent)evt).getClickCount() >= 1;
            }
            return true;
        }
    
        // Returns the spinners current value.
        public Object getCellEditorValue() 
        {
            return spinner.getValue();
        }
    }
