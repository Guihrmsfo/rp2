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

package gr.aueb.cs.nlg.NLGEngine;



import java.util.*;
import com.hp.hpl.jena.rdf.model.*;
import org.w3c.dom.*;
import java.io.*;

import com.hp.hpl.jena.ontology.*;


public class ComparisonNode
{
    /** to onoma toy komboy*/
    private String Name;
    /** o ari8mos twn antikeimenwn poy yparxoyn katw apo ton kombo*/
    private int NumObjects;
    /** h lista me ta paidia toy komboy*/
    private List <ComparisonNode>Children;
    /** deikths pros ton kombo patera*/
    private ComparisonNode Parent;
    /** lista me ta onomata twn attributes poy yparxoyn*/
    private List <String>AttributeNames;
    /** lista me tis times twn atributes*/
    private List <AttributeDesc>AttributeValues;
    /**lista me ta xarakthristika poy mporoyn na xrhsimopoih8oyn stis sygkriseis*/
    private List <String>xarakthristika;
    
    /** nai, nai kanei ayto poy nomizete... deixnei an enoyme episkef8ei to sygkekrimeno kombo.
     * thn afhsame public giati den yparxei logos na einai private -siga ta stoixeia poy exei-
     * kai ante na doyme pote 8a apofasisei h Java na apokthsei ki ayth Properties opws h C#...
     * to na ftiaxnoyme set kai get einai xasimo xronoy...*/
    public boolean visited;
    /**opws kai gia th visited...*/
    public int random;
    public int history;
    public int hier;
    public int sumMethodius;
    
    /** Creates a new instance of ComparisonNode
     * dinontas ws parent null, o kombos ginetai riza*/
    public ComparisonNode(String name, ComparisonNode parent, int hist, List<String> xar)
    {
	Name = name;
	NumObjects = 0;
	xarakthristika = new LinkedList<String>();
	Children = new LinkedList<ComparisonNode>();
	AttributeNames = new LinkedList<String>();
	AttributeValues = new LinkedList<AttributeDesc>();
	visited = false;
	random = 0;
	history = hist;
	hier = 0;
	sumMethodius = 0;
	// ton kanw paidi toy patera
	if (parent!=null)
	{
	    Parent = parent;
	    parent.addChild(this);
	}
	
	
	for (int i=0; i<xar.size();i++)
	{
	    xarakthristika.add(xar.get(i));
	}
    }
    
    /**dinei mia random timh ston eayto ths se periptesh poy exei kapoia xarakthristika katallhla pros sygkrish
     * kaleitai anadromika gia ton patera kai ta paidia*/
    public void step3Random(boolean FC02)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    if (FC02)
		for (int i=0; i<this.getChildren().size(); i++)
		{
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3Random(FC02);
		}
	}
	if (this.getParent()!=null)
	    this.getParent().step3Random(FC02);
    }
    
    public void fillChildren(List<String> children)
    {
	if (this.getChildren().size() == 0)
	{
	    children.add(this.getName());
	}
	else
	{
	    for (int i = 0; i<this.getChildren().size(); i++)
	    {
		this.getChildren().get(i).fillChildren(children);
	    }
	}
    }
    
    public void step3FC02ParentLot()
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1)
	    this.random = (int)(Math.random()*1000);
	
	if (this.getParent()!=null)
	    this.getParent().step3FC02ParentLot();
    }
    
    public void step3FC02ParentLotHistory(int his, int plh8os)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1&&plh8os - this.history <= his)
	    this.random = (int)(Math.random()*1000);
	
	if (this.getParent()!=null)
	    this.getParent().step3FC02ParentLotHistory(his, plh8os);
    }
    
    public void step3FC02Parent()
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0)
	    this.random = (int)(Math.random()*1000);
	
	if (this.getParent()!=null)
	    this.getParent().step3FC02Parent();
    }
    
    public void step3FC02ParentHistory(int his, int plh8os)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && plh8os - this.history<=his)
	    this.random = (int)(Math.random()*1000);
	
	if (this.getParent()!=null)
	    this.getParent().step3FC02ParentHistory(his, plh8os);
    }
    
    public boolean step3RandomOneChildren()
    {
	this.visited = true;
	boolean FC02=false;
	if (this.getAttributeNames().size()>0)
	{
	    this.random = (int)(Math.random()*1000);
	    FC02 = false;
	}
	
	{
	    
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		{
		    
		    if (FC02)
		    {
			FC02 = this.getChildren().get(i).step3RandomOneChildren();
		    }
		    else
			this.getChildren().get(i).step3RandomOneChildren();
		}
	    }
	}
	return FC02;
	
    }
    
    public boolean step3RandomLotChildren()
    {
	this.visited = true;
	boolean FC02=false;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1)
	{
	    this.random = (int)(Math.random()*1000);
	    FC02 = false;
	}
	
	{
	    
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		{
		    
		    if (FC02)
		    {
			FC02 = this.getChildren().get(i).step3RandomLotChildren();
		    }
		    else
			this.getChildren().get(i).step3RandomLotChildren();
		}
	    }
	}
	return FC02;
    }
    
    public void step3RandomLot(boolean FC02)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    if(FC02)
		for (int i=0; i<this.getChildren().size(); i++)
		{
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3RandomLot(FC02);
		}
	}
	if (this.getParent()!=null)
	    this.getParent().step3RandomLot(FC02);
    }
    
    public void step3FC02LotChildren()
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3FC02LotChildren();
	    }
	}
    }
    
    public void step3FC02LotChildrenHistory(int his, int plh8os)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && this.NumObjects>1 && plh8os - this.history <=his)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3FC02LotChildrenHistory(his, plh8os);
	    }
	}
    }
    
    public void step3FC02OneChildren()
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3FC02OneChildren();
	    }
	}
    }
    
    public void step3FC02OneChildrenHistory(int his, int plh8os)
    {
	this.visited = true;
	if (this.getAttributeNames().size()>0 && plh8os - this.history<=his)
	    this.random = (int)(Math.random()*1000);
	else
	{
	    for (int i=0; i<this.getChildren().size(); i++)
	    {
		if(!this.getChildren().get(i).visited)
		    this.getChildren().get(i).step3FC02OneChildrenHistory(his, plh8os);
	    }
	}
	
    }
    
    /** Creates a new instance of ComparisonNode same as oldNode
     * omws ta paidia kai o pateras 8a einai null*/
    public ComparisonNode(ComparisonNode oldNode)
    {
	Name = oldNode.Name;
	NumObjects = oldNode.NumObjects;
	Children = new LinkedList<ComparisonNode>();
	AttributeNames = new LinkedList<String>();
	AttributeValues = new LinkedList<AttributeDesc>();
	xarakthristika = new LinkedList<String>();
	visited = false;
	random = oldNode.random;
	history = oldNode.history;
	hier = oldNode.hier;
	sumMethodius = oldNode.sumMethodius;
	for (int i=0; i<oldNode.AttributeNames.size(); i++)
	{
	    AttributeNames.add(oldNode.AttributeNames.get(i));
	    AttributeValues.add(new AttributeDesc(oldNode.AttributeValues.get(i)));
	}
	
	for (int i=0; i<oldNode.xarakthristika.size();i++)
	{
	    xarakthristika.add(oldNode.xarakthristika.get(i));
	}
    }
    
    /**Dhmioyrgei mia sxesh anamesa se 2 komboys*/
    public void parentChildRel(ComparisonNode child)
    {
	child.Parent = this;
	this.addChild(child);
    }
    
    
    /** Epistrefei to onoma toy komboy*/
    public String getName()
    {
	return Name;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // arkei edw na bazw mono ekeina ta xarakthristika poy prokeitai na perigrafoyn
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Pairnei ena antikeimeno kai pros8etei ta stoixeia toy sto dendro (sto sygkekrimeno kombo
     * kai se oloys toys progonoys toy).
     * Sygkekrimena pros8etei to attribute name an den yparxei, thn timh gia ayto to attribute
     * kai ayjanei to plh8os twn stoixeiwn poy exoyn to attribute kata mia monada.
     * epishs ayjanei kai to plh8os twn object kata mia monada*/
    public void addElement(String strURI, OntModel ontModel, int his)
    {
	Individual MyInstance = ontModel.getIndividual(strURI);
	
	int counter = 0;
	Element msgElem = null;
	
	//  System.out.println("URI: " + MyInstance.getRDFType().getURI());
	OntClass c = ontModel.getOntClass(MyInstance.getRDFType().getURI());
	// System.out.println("URI: " +c.getURI());
	
	//if(c==null)
	//    System.out.println("URI: " +c.getURI() + "null");
	
	for(StmtIterator i = MyInstance.listProperties();  i.hasNext();)
	{
	    Statement t = (Statement)i.next();
	    RDFNode node = t.getObject();
	    
	    String pred = t.getPredicate().getLocalName();
	    
	    if( pred.compareTo("comment")!=0
		    && pred.compareTo("label")!=0
		    && pred.compareTo("sameAs")!=0)
	    {
		if(t.getPredicate().getLocalName().compareTo("type")!=0)
		{
		    //            value                -          attribute Name
		    // t.getPredicate().getNameSpace() - pref,t.getPredicate().getLocalName()
		    boolean stopped= false;
		    for (int j=0; j<AttributeNames.size(); j++)
		    {
			if (AttributeNames.get(j).compareTo(t.getPredicate().getLocalName()) == 0)
			{
			    AttributeValues.get(j).addAtribute(node.toString());
			    stopped = true;
			    break;
			}
		    }
		    if (!stopped)
		    {
			// edw gia ta unique kai ta alla 8a mporoysa na ta afhnw ola.... (me mia boolean an prokeitai
			// gia to unique kai to allo)
			//if (!all)
			if (naToBalw(t.getPredicate().getLocalName(),xarakthristika))
			{
			    AttributeNames.add(t.getPredicate().getLocalName());
			    AttributeValues.add(new AttributeDesc(node.toString(),1));
			}
		    }
		}
	    }
	}
	
	NumObjects++;
	history = his;
	//an den eimai se riza, aneba to dendro
	if (Parent != null)
	    Parent.addElement(strURI, ontModel, his);
    }
    
    /** Pairnei ena antikeimeno kai afairei ta stoixeia toy sto dendro (sto sygkekrimeno kombo
     * kai se oloys toys progonoys toy).
     * Sygkekrimena meiwnei to plh8os twn stoixeiwn poy exoyn to attribute kata mia monada ka8ws kai to
     * plh8os twn object*/
    public void removeElement(String strURI,OntModel ontModel)
    {
	Individual MyInstance = ontModel.getIndividual(strURI);
	
	int counter = 0;
	Element msgElem = null;
	
	// System.out.println("URI: " + MyInstance.getRDFType().getURI());
	OntClass c = ontModel.getOntClass(MyInstance.getRDFType().getURI());
	// System.out.println("URI: " +c.getURI());
	
	if(c==null)
	    System.out.println("URI: " +c.getURI() + "null");
	
	for(StmtIterator i = MyInstance.listProperties();  i.hasNext();)
	{
	    Statement t = (Statement)i.next();
	    RDFNode node = t.getObject();
	    
	    String pred = t.getPredicate().getLocalName();
	    
	    if( pred.compareTo("comment")!=0
		    && pred.compareTo("label")!=0
		    && pred.compareTo("sameAs")!=0)
	    {
		if(t.getPredicate().getLocalName().compareTo("type")!=0)
		{
		    for (int j=0; j<AttributeNames.size(); j++)
		    {
			if (AttributeNames.get(j).compareTo(t.getPredicate().getLocalName()) == 0)
			{
			    AttributeValues.get(j).removeAtribute(node.toString());
			    break;
			}
		    }
		}
	    }
	}
	
	NumObjects--;
	Parent.removeElement(strURI, ontModel);
    }
    
    /** Pros8etei ena paidi ston kombo*/
    public void addChild(ComparisonNode childNode)
    {
	Children.add(childNode);
    }
    
    /** Epistrefei ton gonio kombo*/
    public ComparisonNode getParent()
    {
	return this.Parent;
    }
    
    /** Ektypwnei ta periexomena toy komboy kai ta paidia toy*/
    public void print()
    {
	if (this.Parent !=null)
        {
	    //System.out.println("ta periexomena moy ("+this.Name+") "+this.NumObjects + "gonios moy: "+this.Parent.Name);
        }
	else
        {
	    //System.out.println("ta periexomena moy ("+this.Name+") "+this.NumObjects + "gonios moy: none");
        }
        
	for (int i=0; i<this.AttributeValues.size(); i++)
	{
	    //System.out.println("\tFeature Name: "+this.AttributeNames.get(i)+"\t-\t"+this.AttributeValues.get(i).toString());
	}
	
	if (this.AttributeValues.size() == 0 )
	{
	    //System.out.println("No Attributes");
	}
        
	//System.out.println("ta paidia moy");
        
	for (int i=0; i<this.Children.size(); i++)
	{
	    Children.get(i).print();
	}
	if (this.Children.size() == 0 )
	{
	    //System.out.println("none");
	}
    }
    
    /** Grafei ta periexomena toy komboy kai ta paidia toy sto arxeio name*/
    public void toFile(String name)
    {
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(name, true));
	    
	    if (this.AttributeValues.size() !=0)
	    {
		//out.write("\r\n\t\tΣτο επίπεδο του "+this.Name+"\r\n\r\n\t\t\tΓια τα χαρακτηριστικά:\r\n\r\n");
		
		
		for (int i=0; i<this.AttributeValues.size(); i++)
		{
		    //out.write("\t\t\t\t"+this.AttributeNames.get(i)+"\r\n");
		    
		}
	    }
	    for (int i=0; i<this.Children.size(); i++)
	    {
		Children.get(i).toFile(name);
	    }
	    out.close();
	}
	catch (IOException e)
	{
	    System.out.println("kati den phge kala sthn eggrafh toy dendroy sto arxeio");
	    e.printStackTrace();
	}
    }
    
    
    /** Afairei apo ton kombo ta features poy einai adeia*/
    public void removeEmptyFeatures()
    {
	for (int i=0; i<this.AttributeValues.size(); i++)
	{
	    int tmp = this.AttributeValues.get(i).getDescription().size();
	    if (tmp == 0)
	    {
		this.removeAttribute(this.getAttributeNames().get(i));
		i--;
	    }
	}
	
	for (int i=0; i<this.Children.size(); i++)
	{
	    Children.get(i).removeEmptyFeatures();
	}
	
    }
    
    public void removeLess()
    {
	for (int i=0; i<this.AttributeValues.size(); i++)
	{
	    int tmp = this.AttributeValues.get(i).getDescription().size();
	    if (tmp > 0)
	    {
		if (this.AttributeValues.get(i).getDescription().get(0).Plh8os!= this.getNumberOfObjects())
		{
		    this.removeAttribute(this.getAttributeNames().get(i));
		    i--;
		}
	    }
	}
	
	for (int i=0; i<this.Children.size(); i++)
	{
	    Children.get(i).removeLess();
	}
	
    }
    
    /**Epistrefei to plh8os twn paidiwn toy komboy*/
    public int numberOfChildren()
    {
	return this.Children.size();
    }
    
    /**Epistrefei to plh8os twn antikeimwn toy komboy*/
    public int getNumberOfObjects()
    {
	return this.NumObjects;
    }
    
    /**Epistrefei th lista me ta onomata twn xarakthristikwn toy komboy*/
    public List<String> getAttributeNames()
    {
	return this.AttributeNames;
    }
    
    /**Epistrefei th lista me tis times twn xarakthristikwn toy komboy*/
    public List<AttributeDesc> getAttributeValues()
    {
	return this.AttributeValues;
    }
    
    /**Epistrefei th lista me ta paidia toy komboy*/
    public List<ComparisonNode> getChildren()
    {
	return this.Children;
    }
    
    /**Epistrefei an yparxei to sygkekrimeno xarasthristiko ston kombo*/
    public boolean hasAttribute(String name)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    if (this.AttributeNames.get(i).equalsIgnoreCase(name))
	    {
		return true;
	    }
	}
	return false;
    }
    
    /**Epistrefei th 8esh toy sygkekrimenoy xarasthristikoy ston kombo*/
    public int getIndexOfAttribute(String name)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    if (this.AttributeNames.get(i).equalsIgnoreCase(name))
	    {
		return i;
	    }
	}
	return -1;
    }
    
    /**afairei ena attibute apo ton kombo*/
    public void removeAttribute(String name)
    {
	int pos = this.AttributeNames.indexOf(name);
	if(pos>=0)
	{
	    this.AttributeNames.remove(pos);
	    
	    this.AttributeValues.remove(pos);
	}
    }
    
    /** Kanei oles tis metablhtes visited toy komboy (kai twn paidiwn toy) na exoyn timh false*/
    public void deVisit()
    {
	this.visited = false;
	for (int i=0; i < this.Children.size(); i++)
	{
	    this.Children.get(i).deVisit();
	}
    }
    
    /**Epistrefei an o kombos exei kati mesa toy (kapoio xarakthristiko*/
    public boolean hasAttributes()
    {
	if (this.AttributeNames.size() == 0)
	    return false;
	else
	{
	    for (int i=0; i<AttributeValues.size(); i++)
	    {
		if (AttributeValues.get(i).getDescription().size() == 0)
		{
		    return false;
		}
	    }
	}
	
	return true;
    }
    
    /** briskei to kontinotero patera toy komboy poy mporei na xrhsimopoih8ei se sygkrish */
    public distanceNodes findParentStep3(int distance)
    {
	distanceNodes fatherNode = new distanceNodes(null,Integer.MAX_VALUE);
	// an den eimai riza
	if (this.Parent !=null)
	{
	    this.Parent.visited = true;
	    // gia ton gonio
	    if (this.Parent.hasAttributes())
		return new distanceNodes(this.Parent,distance);
	    else
	    {
		//gia toys progonoys
		distance++;
		fatherNode = this.Parent.findParentStep3(distance);
	    }
	    //gia ta adelfia moy
	    distanceNodes childNode = this.findChildStep3(distance);
	    if (fatherNode.Node == null)
		return childNode;
	    if (childNode.Node == null)
		return fatherNode;
	    if (childNode.Distance<fatherNode.Distance)
		return childNode;
	    else
		return fatherNode;
	}
	return fatherNode;
    }
    
    /** briskei to kontinotero paidi toy komboy poy mporei na xrhsimopoih8ei se sygkrish */
    public distanceNodes findChildStep3(int distance)
    {
	distanceNodes distNode = new distanceNodes(null,Integer.MAX_VALUE);
	for (int i=0; i<this.Children.size(); i++)
	{
	    if(!this.Children.get(i).visited)
	    {
		if (this.Children.get(i).hasAttributes())
		    return new distanceNodes(this.Children.get(i),distance);
		else
		{
		    distance++;
		    distanceNodes childNode = this.getChildren().get(i).findChildStep3(distance);
		    if (childNode.Distance<distNode.Distance)
			distNode = childNode;
		    distance--;
		}
	    }
	}
	return distNode;
    }
    
    public void removeFromChildren(String attribute, String value)
    {
	for (int i=0; i<this.Children.size(); i++)
	{
	    this.Children.get(i).removeFromChildren(attribute, value);
	}
	if(!this.visited)
	{
	    for (int i =0; i < this.AttributeNames.size(); i++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(attribute))
		{
		    for (int j=0; j<this.AttributeValues.get(i).getDescription().size(); j++)
		    {
			if (this.AttributeValues.get(i).getDescription().get(j).Value.equalsIgnoreCase(value))
			{
			    this.AttributeValues.get(i).getDescription().remove(j);
			}
		    }
		}
	    }
	    
	}
    }
    
    /** psaxnei sta paidia kai briskei an yparxei to xarakthristiko Attribute sthn posothta numberOfObjects*/
    public boolean foundInChildren(String attribute, String value, int numberOfObjects)
    {
	boolean found = false;
	
	for (int i=0; i < this.Children.size(); i++)
	{
	    found = this.Children.get(i).foundInChildren(attribute, value, numberOfObjects);
	    if (found)
	    {
		
		this.removeAttribute(attribute);
		
		return found;
	    }
	}
	
	int posAttribute = this.getAttributeNames().indexOf(attribute);
	if (posAttribute>=0)
	{
	    int posDescription = this.getAttributeValues().get(posAttribute).findName(value);
	    if (posDescription >=0)
	    {
		int plh8osDesciption = this.getAttributeValues().get(posAttribute).getDescription().get(posDescription).Plh8os;
		if (this.NumObjects == numberOfObjects && numberOfObjects == plh8osDesciption)
		{
		    // an den einai fyllo
		    {
			this.AttributeValues.get(posAttribute).chosen = true;
			return true;
			
		    }
		}
		else
		{
		    if(!this.AttributeValues.get(posAttribute).chosen)
			if (!this.visited)
			{
			this.removeAttribute(attribute);
			}
		}
	    }
	}
	return found;
    }
    
    public void removeAttributeFromParents(String attribute)//), String value, int numberOfObjects)
    {
	if (this.Parent != null)
	{
	    
	    {
		this.Parent.removeAttribute(attribute);
		this.Parent.removeAttributeFromParents(attribute);//, value, numberOfObjects);
	    }
	}
    }
    
    
    /**Krata mono ekeina ta xarakthristika toy dendroy poy yparxoyn sto x pososto twn antikeimenwn
     * toy komboy kai kalei thn idia synarthsh kai gia ola ta paidia toy komboy*/
    public void performBluring(int x)
    {
	if(this.getNumberOfObjects()>1)
	{
	    for (int i=0; i<this.AttributeNames.size(); i++)
	    {
		int plh8os =0;
		for (int j=0; j<this.AttributeValues.get(i).getDescription().size(); j++)
		{
		    plh8os+=this.AttributeValues.get(i).getDescription().get(j).Plh8os;
		}
		for (int j=0; j<this.AttributeValues.get(i).getDescription().size(); j++)
		{
		    if ((float)(this.AttributeValues.get(i).getDescription().get(j).Plh8os/(float)this.getNumberOfObjects())<((float)x/(float)100))
		    {
			this.AttributeValues.get(i).getDescription().remove(j);
			j--;
		    }
		    else
		    {
			this.AttributeValues.get(i).getDescription().get(j).Plh8os = 2;
		    }
		}
		if (this.AttributeValues.get(i).getDescription().size() == 0)
		{
		    this.removeAttribute(this.AttributeNames.get(i));
		    i--;
		}
	    }
	    this.NumObjects = 2;
	    for(int i =0; i<this.Children.size(); i++)
	    {
		this.Children.get(i).performBluring(x);
	    }
	}
	else
	{
	    this.AttributeNames.clear();
	    this.AttributeValues.clear();
	    this.NumObjects = 0;
	    for(int i =0; i<this.Children.size(); i++)
	    {
		this.Children.get(i).performBluring(x);
	    }
	    this.Children.clear();
	}
    }
    
    /**Epistrefei to plh8os twn ontothtwn poy paroysiazoyn th sygkekrimenh idiothta sto sygkekrimeno kombo*/
    public int getNumberOf(String attributeName)
    {
	int plh8os = 0;
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    if (this.AttributeNames.get(i).equalsIgnoreCase(attributeName))
	    {
		for (int j=0; j<this.AttributeValues.get(i).getDescription().size(); j++)
		{
		    plh8os += this.AttributeValues.get(i).getDescription().get(j).Plh8os;
		}
		return plh8os;
	    }
	}
	return 0;
    }
    
    /**Epistrefei to plh8os twn idiwn xarakthristikwn*/
    public int omoiothtes(String strURI, OntModel ontModel)
    {
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, this.xarakthristika);
	describingInstance.addElement(strURI,ontModel, 0);
	int counter = 0;
	for (int i = 0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<describingInstance.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(describingInstance.AttributeNames.get(j)))
		{
		    if (this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(describingInstance.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			counter++;
		    }
		}
	    }
	}
	return counter;
    }
    
    /**afairei ta koina xarakthristika*/
    public void removeOmoiothtes(String strURI, OntModel ontModel,List<String>tmp)
    {
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	for (int i = 0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<describingInstance.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(describingInstance.AttributeNames.get(j)))
		{
		    if (this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(describingInstance.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			this.removeAttribute(this.getAttributeNames().get(i));
			i--;
			break;
		    }
		}
	    }
	}
	
    }
    
    /**afairei ta diaforetika xarakthristika*/
    public void removeDiafores(String strURI, OntModel ontModel)
    {
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, this.xarakthristika);
	describingInstance.addElement(strURI,ontModel, 0);
	
	for (int i = 0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<describingInstance.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(describingInstance.AttributeNames.get(j)))
		{
		    if (!this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(describingInstance.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			this.removeAttribute(this.getAttributeNames().get(i));
			i--;
			break;
		    }
		}
	    }
	}
    }
    
    public boolean naToBalw(String www)
    {
	String []xarakthristika = new String[4];
	xarakthristika[0]="made-of";
	xarakthristika[1]="original-location";
	xarakthristika[2]="painting-techinique-used";
	xarakthristika[3]="creation-period";
	for (int i=0; i < xarakthristika.length; i++)
	{
	    if(www.equalsIgnoreCase(xarakthristika[i]))
		return true;
	}
	
	return false;
    }
    
    public boolean naToBalw(String www, java.util.List<String> xarakthristika)
    {
	for (int i=0; i < xarakthristika.size(); i++)
	{
	    if(www.equalsIgnoreCase(xarakthristika.get(i)))
		return true;
	}
	
	return false;
    }
    
    public boolean hasDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<start.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(start.AttributeNames.get(j)))
		{
		    if(!this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(start.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			return true;
		    }
		    //break;
		}
	    }
	}
	return false;
    }
    
    
    public void removeDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<start.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(start.AttributeNames.get(j)))
		{
		    if(!this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(start.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			this.removeAttribute(AttributeNames.get(i));
			i--;
			break;
		    }
		}
	    }
	}
	
    }
    
    
    public void keepDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<start.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(start.AttributeNames.get(j)))
		{
		    if(this.AttributeValues.get(i).getDescription().get(0).Value.equalsIgnoreCase(start.AttributeValues.get(j).getDescription().get(0).Value))
		    {
			this.removeAttribute(AttributeNames.get(i));
			i--;
			break;
		    }
		}
	    }
	}
	
    }
    
    public void keepOneObject(ComparisonNode start)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<start.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(start.AttributeNames.get(j)))
		{
		    if(this.NumObjects>1)
		    {
			this.removeAttribute(AttributeNames.get(i));
			i--;
			break;
		    }
		}
	    }
	}
	
    }
    
    public void keepMultiObject(ComparisonNode start)
    {
	for (int i=0; i<this.AttributeNames.size(); i++)
	{
	    for (int j=0; j<start.AttributeNames.size(); j++)
	    {
		if (this.AttributeNames.get(i).equalsIgnoreCase(start.AttributeNames.get(j)))
		{
		    if(this.NumObjects == 1)
		    {
			this.removeAttribute(AttributeNames.get(i));
			i--;
			break;
		    }
		}
	    }
	}
	
    }
    
    public void membersOf(Vector<String> members)
    {
	if (this.numberOfChildren()>0)
	{
	    for (int i=0; i<this.numberOfChildren(); i++)
	    {
		this.getChildren().get(i).membersOf(members);
	    }
	}
	else
	    members.add(this.getName());
    }
    
}

/** Mia xazh clash poy 8a apo8hkeyei kapoion Node mazi me kapoia apostash*/
class distanceNodes
{
    public ComparisonNode Node;
    public int Distance;
    /** Creates a new instance of AttributeDesc */
    public distanceNodes(ComparisonNode node, int dist)
    {
	Node = node;
	Distance = dist;
    }
}