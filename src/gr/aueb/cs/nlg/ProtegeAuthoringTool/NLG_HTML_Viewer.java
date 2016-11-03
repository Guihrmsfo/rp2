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
import javax.swing.event.*;
import javax.swing.text.html.*;

import java.awt.*;
import javax.swing.JFrame;

public class NLG_HTML_Viewer extends JPanel {
    
    private JEditorPane html;
    
    public NLG_HTML_Viewer() 
    {
            
        html = new JEditorPane();
        html.setContentType("text/html");
        //html.setContentType("text/plain");
        //html.setText("<B><a href='mitsos.html'>Dimitrios Galanis</a></B>");

        Insets newInsets = new Insets(20, 20, 20, 20);

        html.setMargin(newInsets);
        //html.setFont(new java.awt.Font("Verdana", 0, 20));
        html.setEditable(false);
        html.addHyperlinkListener(createHyperLinkListener());

        JScrollPane scroller = new JScrollPane();
        JViewport vp = scroller.getViewport();
        vp.add(html);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, scroller);             

    }
    
    public void setText(String text) 
    {
        html.setText("<html><body bgcolor='#ffffff' style='font-family: Cambria; font-size: 12px; text-align: justify; font-weight: normal'>"
                + text + "</body></html>");
    }
    
    public HyperlinkListener createHyperLinkListener() {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
                                (HTMLFrameHyperlinkEvent)e);
                    } else {
                        //try {
                        // System.out.println(e.getURL());
                        // html.setText("<A>Mitsos</A>");
                        // html.setPage(e.getURL());
                        //} catch (IOException ioe) {
                        //    System.out.println("IOE: " + ioe);
                        //}
                    }
                }
            }
        };
    }
    
    void updateDragEnabled(boolean dragEnabled) {
        html.setDragEnabled(dragEnabled);
    }
    
    public static void main(String args[]) {
        
        JFrame frame = new JFrame("NL Generation");
        NLG_HTML_Viewer myNLG_HTML_Viewer = new NLG_HTML_Viewer();
        
        String str = "<?xml version=\"1.0\"?><x><y>ffff</y></x>";
                
        str.replaceAll("<", "&lt;");
        str.replaceAll(">", "&gt;");
        str.replaceAll("\"", "&quot;");
        
        myNLG_HTML_Viewer.setText("This is an amphora handle, created during the Hellenistic period. It originates from Rhodes and it is made of clay. The handles of Rhodian amphorae, which have been found dispersed in the Mediterranean, reveal the island's trade relations with different regions in that area. This amphora handle has a seal on which there is a representation of Helios on his four-horsed chariot. Currently this amphora handle is exhibited in the Archaeological Museum of Thassos."+ "<BR><BR><BR>" + "<pre>" +  str +"</pre>");
        
        Container cont = frame.getContentPane();
        cont.setLayout(new BorderLayout());
        cont.add(BorderLayout.CENTER, myNLG_HTML_Viewer);
        
        frame.setBounds(100, 100, 300, 300);
        frame.setVisible(true);
    }
}
