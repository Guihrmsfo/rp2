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
import java.io.*;
import com.hp.hpl.jena.ontology.*;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;

import gr.aueb.cs.nlg.NLFiles.*;

public class ComparisonTree implements Cloneable
{
    /**A list with all the nodes*/
    private List <ComparisonNode>Tree;
    /**A Map with all the features and a value for each feature (it was used for an experimental version of the Melegkoglou algorithm
     * which each feature was automaticly evaluated -it isn't used in the final algorithm)*/
    private Map ajiologhsh;
    /**the number of nodes of the Tree (a.k.a. Tree.size())*/
    public int plh8os;
    
    /**when was the last comparison generated*/
    public int plh8osCompar;
    /**A array with the features that can be used for comparisons*/
    public String []xarakthristika;
    /**When was each feature used for a comparison*/
    public int[] hisCompar;
    
    
    /**This is the Tree used for Comparisons. Xar is a List<String> with the names of the features that can
     * can be used for caomparisons*/
    public ComparisonTree(List<String> xar)
    {
	xarakthristika = new String[xar.size()];
	hisCompar = new int[xar.size()];
	for (int i=0; i<xar.size(); i++)
	{
	    xarakthristika[i] = xar.get(i);
	}
	
	
	for (int i=0; i<hisCompar.length; i++)
	{
	    hisCompar[i] = 0;
	}
	
	
	Tree = new LinkedList<ComparisonNode>();
	ajiologhsh = new TreeMap();
	plh8os = 0;
	plh8osCompar = 0;
    }
    
    /** Creates a new instance of ComparisonTree same as oldTree*/
    public ComparisonTree(ComparisonTree oldTree)
    {
	this.plh8osCompar = oldTree.plh8osCompar;
	xarakthristika = new String[oldTree.xarakthristika.length];
	hisCompar = new int [oldTree.hisCompar.length];
	for (int i=0; i<hisCompar.length; i++)
	{
	    xarakthristika[i] = oldTree.xarakthristika[i];
	    hisCompar[i] = oldTree.hisCompar[i];
	}
	
	
	//ftiaxnw toys komboys
	Tree = new LinkedList<ComparisonNode>();
	ajiologhsh = new TreeMap();
	plh8os = oldTree.plh8os;
	plh8osCompar = oldTree.plh8osCompar;
	for (int i = 0; i<oldTree.Tree.size(); i++)
	{
	    Tree.add(new ComparisonNode(oldTree.Tree.get(i)));
	}
	//ftiaxnw tis sxeseis pateras-paidi
	for (int i=0; i<oldTree.Tree.size(); i++)
	{
	    ComparisonNode father = oldTree.Tree.get(i).getParent();
	    if (father != null)
	    {
		int positionOfFather = oldTree.Tree.indexOf(father);
		Tree.get(positionOfFather).parentChildRel(Tree.get(i));
	    }
	}
	
    }
    
    /**Performing the Methodius Comparison Selection Algorithm*/
    public ComparisonNode performMethodiusAlgorithm(String strURI, OntModel ontModel, List<String> tmp)
    {
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
tmpTree.cleanForest(strURI);	
//tmpTree.sortAttributes(UMQM, UserType);
	//System.out.println("To dendro mas arxika");
	//tmpTree.print();
	
	
	// briskw ton kombo ston opoio anhkei h perigrafomenh ontothta. se periptwsh poy den yparxei tote
	// epistrefei null
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	boolean parent = false;
	if (descNode == null)
	{
	    descNode = tmpTree.findParentExistingNode(strURI, ontModel);
	    parent = true;
	}
	if (descNode != null)
	{
	    //bres tis apostaseis kai aferaise ta anomoia
	    tmpTree.step1Methodius(descNode, describingInstance,0);
	    //  tmpTree.print();
	    
	    tmpTree.removeEmptyFeatures();
	    tmpTree.deVisit();
	    tmpTree.removeChildren(this);
	    //tmpTree.print();
	    tmpTree.computeMethodiusSum();
	    int max = tmpTree.findMaxSum();
	    tmpTree.removeNodesWithSumLessThan(max);
	    //tmpTree.print();
	    if (tmpTree.Tree.size()>0)
		comparator = tmpTree.Tree.get(((int)(Math.random()*10000))%tmpTree.Tree.size());
	    
	}
	return comparator;
    }
    
    /**Removes the comparisons (feature and entity) that have just been used*/
    public void removeFreqUsedCompar(int now)
    {
	if (this.plh8osCompar>0)
	{
	    for (int i=0; i<this.Tree.size(); i++)
	    {
		for (int j=0; j<this.Tree.get(i).getAttributeNames().size(); j++)
		{
		    if (this.Tree.get(i).getAttributeValues().get(j).history > 0 && (this.plh8osCompar - this.Tree.get(i).getAttributeValues().get(j).history) < now)
		    {
			this.Tree.get(i).removeAttribute(this.Tree.get(i).getAttributeNames().get(j));
		    }
		}
	    }
	}
    }
    
    /**Removes the comparisons (feature and entity) that have just been used*/
    public void removeLastUsedComparisonFeature()
    {
	int max = Integer.MIN_VALUE;
	for (int i=0; i<this.hisCompar.length; i++)
	{
	    if (max<this.hisCompar[i])
	    {
		max = this.hisCompar[i];
	    }
	}
	
	if (max>0)
	{
	    for (int i=0; i<this.Tree.size(); i++)
	    {
		
		for (int j=0; j<this.xarakthristika.length; j++)
		{
		    if (max == this.hisCompar[j])
		    {
			this.Tree.get(i).removeAttribute(this.xarakthristika[j]);
		    }
		}
	    }
	}
    }
    
    
    public void removeFreqUsedComparAll(int now)
    {
	if (this.plh8osCompar>0)
	{
	    
	    int []tmpHis = new int[hisCompar.length];
	    for (int k=0; k<hisCompar.length; k++)
	    {
		tmpHis[k]=this.hisCompar[k];
	    }
	    
	    for (int k=0; k<now; k++)
	    {
		int max = tmpHis[0];
		int pos = 1;
		for (int l = 1; l<tmpHis.length; l++)
		{
		    if (max<tmpHis[l])
		    {
			max = tmpHis[l];
			pos = l;
		    }
		}
		//wste na mhn to janaparw
		tmpHis[pos] = 0;
		boolean contin = false;
		do
		{
		    contin = false;
		    for (int i=0; i<this.Tree.size(); i++)
		    {
			int posXar = this.Tree.get(i).getIndexOfAttribute(this.xarakthristika[pos]);
			if (posXar>=0)
			{
			    if (this.Tree.get(i).getAttributeValues().get(posXar).history > 0 && (this.plh8osCompar - this.Tree.get(i).getAttributeValues().get(posXar).history) < now)
			    {
				this.Tree.get(i).removeAttribute(this.Tree.get(i).getAttributeNames().get(posXar));
			    }
			}
		    }
		    for (int l = 0; l<tmpHis.length; l++)
		    {
			if (max == tmpHis[l])
			{
			    pos = l;
			    tmpHis[pos] = 0;
			    contin = true;
			    break;
			}
		    }
		    
		}while (contin);
	    }
	}
    }
    
    public void updateComparHistory(ComparisonNode compar)
    {
	if (compar!=null)
	{
	    this.plh8osCompar+=1;
	    ComparisonNode updating = this.findNode(compar.getName());
	    for (int i = 0; i<compar.getAttributeNames().size(); i++)
	    {
		int pos = updating.getAttributeNames().indexOf(compar.getAttributeNames().get(i));
		updating.getAttributeValues().get(pos).history = this.plh8osCompar;
		//o deyteros tropos
		for (int k = 0; k< this.xarakthristika.length; k++)
		{
		    if (this.xarakthristika[k].equalsIgnoreCase(compar.getAttributeNames().get(i)))
		    {
			this.hisCompar[k]+=this.plh8osCompar;
		    }
		}
	    }
	}
    }
    
    public void removeNodesWithSumLessThan(int max)
    {
	for (int i = 0; i<this.Tree.size(); i++)
	{
	    if (max != this.Tree.get(i).sumMethodius)
	    {
		this.Tree.remove(i);
		i--;
	    }
	}
    }
    
    public int findMaxSum()
    {
	int max = Integer.MIN_VALUE;
	for (int i = 0; i<this.Tree.size(); i++)
	{
	    if (max<this.Tree.get(i).sumMethodius)
		max = this.Tree.get(i).sumMethodius;
	}
	return max;
    }
    
    
    public void computeMethodiusSum()
    {
	for (int i = 0; i<this.Tree.size(); i++)
	{
	    if (this.Tree.get(i).getAttributeNames().size()>0)
	    {
		//System.out.println()
		this.Tree.get(i).sumMethodius = this.Tree.get(i).getNumberOfObjects()+this.Tree.get(i).getAttributeNames().size() - this.Tree.get(i).hier - (this.plh8os - this.Tree.get(i).history);
	    }
	    else
	    {
		this.Tree.remove(i);
		
		i--;
	    }
	}
	
    }
    
    public void step1Methodius(ComparisonNode start,ComparisonNode describing, int hier)
    {
	start.hier = hier;
	start.visited =true;
	this.removeAttributesNodesMethodius(start,describing);
	for (int i = 0; i<start.getChildren().size(); i++)
	{
	    if (!start.getChildren().get(i).visited)
		step1Methodius(start.getChildren().get(i),describing, hier+1);
	}
	if (start.getParent()!=null && !start.getParent().visited)
	    step1Methodius(start.getParent(),describing, hier+1);
	
    }
    
    /**Pros8etei ena stoixeio sto dendro (kai an xreiazetai ftiaxnei toys neoys komboys)*/
    public void addElement(String strURI, OntModel ontModel, List<String>tmp)
    {
	if (this.findNode(strURI)==null)
	{
	/*pairnw ena stoixeio.
	 *briskw to onoma toy
	 *an yparxei kombos me ayto to onoma, apla ton kanw update
	 *alliws ftiaxnw ton kombo
	 *briskw ton patera
	 *epanalmbanw mexri na eite na yparxei eite na mhn exei patera
	 *
	 */ // oso den exw ftasei sth riza h den yparxei hdh o kombos, ftiaxne neo kombo
	    ComparisonNode previousNode = null;
	    Individual MyInstance = ontModel.getIndividual(strURI);
	    
	    String name = strURI;
	    
	    int his = plh8os;
	    //ftiaxnw osoys komboys xreiazetai
	    
	    StmtIterator iter = MyInstance.listProperties(RDF.type);
	    
	    
	    ComparisonNode currentNode = new ComparisonNode(name,null, his, tmp);
	    Tree.add(currentNode);
	    if (previousNode!=null)
	    {
		currentNode.parentChildRel(previousNode);
	    }
	    previousNode = currentNode;
	    Statement stmt = (Statement)iter.next();
	    Resource superCls = (Resource)stmt.getObject().as(Resource.class);
	    //System.out.println("superclass " +superCls.getURI());
	    OntClass cls = ontModel.getOntClass(superCls.getURI());
	    name = superCls.getURI();
	    
	    
	    
	    while (returnNode(name) == null && name.indexOf("Thing")<0)
	    {
		currentNode = new ComparisonNode(name,null,his, tmp);
		Tree.add(currentNode);
		if (previousNode!=null)
		{
		    currentNode.parentChildRel(previousNode);
		}
		previousNode = currentNode;
		if (cls.listSuperClasses().hasNext())
		    for (Iterator i = cls.listSuperClasses( true ); i.hasNext(); )
		    {
		    OntClass c = (OntClass) i.next();
		    
		    if (c.isRestriction())
		    {
			Restriction r = c.asRestriction();
			
			if (r.isAllValuesFromRestriction())
			{
			    AllValuesFromRestriction av = r.asAllValuesFromRestriction();
			   // System.out.println( "AllValuesFrom class " +
			//	    av.getAllValuesFrom().getLocalName() +
			//	    " on property " + av.getOnProperty().getLocalName() );
			}
		    }
		    else
		    {
			cls = c;
			name = cls.getURI();
			break;
		    }
		    }
		else
		{
		    break;
		}
	    }
	    if (returnNode(name) != null &&name.indexOf("Thing")<0)
	    {
		if (previousNode!=null && !previousNode.getName().equalsIgnoreCase(name))
		{
		    returnNode(name).parentChildRel(previousNode);
		}
	    }
	    
	    // update toys komboys me to neo stoixeio
	    ComparisonNode New = this.returnNode(strURI);
	    New.addElement(strURI,ontModel, his);
	    plh8os++;
	}
    }
    
    
    /**Afairei ena stoixeio toy dendroy*/
    public void removeElement(String strURI, OntModel ontModel)
    {
	ComparisonNode toBeRemoved = this.returnNode(strURI);
	if (toBeRemoved != null)
	{
	    toBeRemoved.removeElement(strURI, ontModel);
	}
    }
    
    /**Epistrefei ton kombo me onoma name. se periptwsh poy den brei kombo me ayto to onoma
     * epistrefei null*/
    public ComparisonNode returnNode(String name)
    {
	for (int i=0; i<Tree.size(); i++)
	{
	    if (Tree.get(i).getName().equalsIgnoreCase(name))
	    {
		return Tree.get(i);
	    }
	}
	return null;
    }
    
    /** Ektypwnei ta periexomena toy dendroy*/
    public void print()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getParent() == null)
	    {
		Tree.get(i).print();
	    }
	}
    }
    
    /** Grafei ta periexomena toy dendroy sto arxeio name*/
    public void toFile(String name)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getParent() == null)
	    {
		Tree.get(i).toFile(name);
	    }
	    
	}
    }
    
    /** Afairei apo to dendro ta features poy einai adeia*/
    public void removeEmptyFeatures()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getParent() == null)
	    {
		Tree.get(i).removeEmptyFeatures();
		//break;
	    }
	}
    }
    
    public void removeLess()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getParent() == null)
	    {
		Tree.get(i).removeLess();
		//break;
	    }
	}
    }
    
    /** epistrefei th riza toy dendroy (an prokeitai gia dasos epistrefei th riza toy prwtoy dendroy)*/
    public ComparisonNode findRoot()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getParent() == null)
	    {
		return Tree.get(i);
	    }
	}
	//de 8a prepe na symbei pote
	return null;
    }
    
    /** H synarthsh epistrefei ton kombo gia ton opoio to strURI einai monadiko se kapoio xarakthristiko
     * strURI einai to URI toy stoixeioy gia to opoio elegxoyme*/
    public ComparisonNode performCommonSelection(String strURI, OntModel ontModel, List<String>tmp)
    {
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
	//System.out.println("To dendro mas arxika");
	//tmpTree.print();
	
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	tmpTree.cleanForest(strURI);
	if (descNode != null)
	{
	    //krata mono th diadromh apo th riza mexri ayto to fyllo
	    // kai sbhse osa xarakthristika einai megalytera toy 1
	    //epishs sbhse kai osa xarakthristika den einai koina me thn perigrafomenh ontothta
	    // telos sbhnei kai ena ta xarakthristika poy pairnoyn polles diaforetikes times
	    tmpTree.step1Common(descNode, describingInstance);
	    
	    tmpTree.klademaUnique(this,descNode);
	    //System.out.println("kladeyontas");
	    //tmpTree.print();
	    //System.out.println("telikh epilogh");
	    comparator = tmpTree.chooseCommonComparatorBest(this.ajiologhsh);
	    
	   // System.out.println("Meta to trito bhma");
	    //if (comparator !=null)
		//comparator.print();
	   // System.out.println();
	}
	else
	    System.out.println("EXEI GINEI LA8OS!!");
	return comparator;//return null;
    }
    
    /** H synarthsh epilegei ekeino to stoixeio poy einai kalytero gia sygkrish me
     *me ta perissotera apo ta ek8emata olhs ths sylloghs
     * strURI einai to URI toy stoixeioy gia to opoio elegxoyme*/
    public ComparisonNode performCommonSelectionBlured(String strURI, OntModel ontModel, List<String>tmp)
    {
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
	//System.out.println("To dendro mas arxika");
//	tmpTree.print();
	
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	tmpTree.cleanForest(strURI);
	if (descNode != null)
	{
	    //krata mono th diadromh apo th riza mexri ayto to fyllo
	    // kai sbhse osa xarakthristika einai megalytera toy 1
	    //epishs sbhse kai osa xarakthristika den einai koina me thn perigrafomenh ontothta
	    // telos sbhnei kai ena ta xarakthristika poy pairnoyn polles diaforetikes times
	    tmpTree.step1MostCommon(descNode, describingInstance);
	    //tmpTree.print();
	    /*System.out.println("(afairontas ta empty features)");
	    tmpTree.removeEmptyFeatures();
	    tmpTree.print();
	    tmpTree.deVisit();
	    tmpTree.step2(descNode, describingInstance);
	    System.out.println("Meta to deytero bhma");
	    tmpTree.print();
	    tmpTree.deVisit();*/
	    tmpTree.klademaUnique(this,descNode);
	   // System.out.println("kladeyontas");
	    //tmpTree.print();
	    //System.out.println("telikh epilogh");
	    comparator = tmpTree.chooseCommonComparatorBest(this.ajiologhsh);
	    
	    //System.out.println("Meta to trito bhma");
	   // if (comparator !=null)
	//	comparator.print();
	 //   System.out.println();
	}
	else
	    System.out.println("EXEI GINEI LA8OS!!");
	return comparator;//return null;
    }
    
    public void cleanForest(String treeNode)
    {
	this.deVisit();
	ComparisonNode tmp = this.findNode(treeNode);
	if (tmp!= null)
	{
	    while (tmp.getParent()!=null)
	    {
		tmp.visited = true;
		tmp = tmp.getParent();
	    }
	    tmp.visited = true;
	}
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (!this.Tree.get(i).visited)
	    {
		this.Tree.remove(i);
		i--;
	    }
	}
	this.deVisit();
    }
    
    public void cleanMelegkogloyForest(String treeNode)
    {
	this.deVisit();
	ComparisonNode tmp = this.findNode(treeNode);
	if (tmp!= null)
	{
	    while (tmp.getParent()!=null)
            {
		tmp = tmp.getParent();
	    }
	    tmp.visited = true;
	}
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (!this.Tree.get(i).visited && this.Tree.get(i).getParent() == null)
	    {
		this.Tree.remove(i);
		i--;
	    }
	}
	this.deVisit();
    }
    
    /** H synarthsh epistrefei ton kombo gia ton opoio to strURI einai monadiko se kapoio xarakthristiko
     * strURI einai to URI toy stoixeioy gia to opoio elegxoyme*/
    public ComparisonNode performUniqueSelection(String strURI, OntModel ontModel, List<String>tmp)
    {
	
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
	//System.out.println("To dendro mas arxika");
	//tmpTree.print();
	
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	tmpTree.cleanForest(strURI);
	if (descNode != null)
	{
	    
	    //krata mono th diadromh apo th riza mexri ayto to fyllo
	    // kai sbhse osa xarakthristika einai megalytera toy 1
	    //epishs sbhse kai osa xarakthristika den einai koina me thn perigrafomenh ontothta
	    // telos sbhnei kai ena ta xarakthristika poy pairnoyn polles diaforetikes times
	    tmpTree.print();
	    tmpTree.step1Unique(descNode, describingInstance);
	    for (int i=0; i<tmpTree.Tree.size(); i++)
	    {
		if (tmpTree.Tree.get(i).getChildren().size()==0 &&tmpTree.Tree.get(i).getParent()==null)
		{
		    tmpTree.Tree.remove(i);
		    i--;
		}
	    }
	    tmpTree.print();
	    tmpTree.klademaUnique(this,descNode);
	    tmpTree.print();
	    //System.out.println("kladeyontas");
	    //    tmpTree.print();
	    //System.out.println("telikh epilogh");
	    comparator = tmpTree.chooseUniqueComparatorBest(this.ajiologhsh);
	    
	    //System.out.println("Meta to trito bhma");
	    //if (comparator !=null)
	//	comparator.print();
	  //  System.out.println();
	}
	else
	    System.out.println("EXEI GINEI LA8OS!!");
	return comparator;//return null;
    }
    
    /**Epistrefei ekeinon ton kombo poy einai katallhlos gia sygkrish (random)*/
    public ComparisonNode chooseUniqueComparator()
    {
	ComparisonNode comp = null;
	ComparisonNode tmp = this.findRoot();
	int max = Integer.MIN_VALUE;
	int tmpInt=0;
	while(tmp.getChildren().size()>0)
	{
	    //an ayto einai to monadiko sto eidos toy
	    if(tmp.getNumberOfObjects() == 1)
	    {
		for(int i=0; i<tmp.getAttributeNames().size(); i++)
		{
		    tmp.removeAttribute(tmp.getAttributeNames().get(i));
		    i--;
		}
		tmpInt =(int)(Math.random()*1000);
		if (max<tmpInt)
		{
		    max=tmpInt;
		    comp = tmp;
		}
	    }
	    else
	    {
		if (tmp.getAttributeNames().size()>0)
		{
		    tmpInt =(int)(Math.random()*1000);
		    if (max<tmpInt)
		    {
			max=tmpInt;
			comp = tmp;
		    }
		}
	    }
	    tmp = tmp.getChildren().get(0);
	}
	
	if(tmp.getNumberOfObjects() == 1)
	{
	    for(int i=0; i<tmp.getAttributeNames().size(); i++)
	    {
		tmp.removeAttribute(tmp.getAttributeNames().get(i));
		i--;
	    }
	    tmpInt =(int)(Math.random()*1000);
	    if (max<tmpInt)
	    {
		max=tmpInt;
		comp = tmp;
	    }
	}
	else
	{
	    if (tmp.getAttributeNames().size()>0)
	    {
		tmpInt =(int)(Math.random()*1000);
		if (max<tmpInt)
		{
		    max=tmpInt;
		    comp = tmp;
		}
	    }
	}
	
	return comp;
    }
    
//    /**Epistrefei ekeinh th sygkrish poy einai kalyterh me basei to poses fores
//     * yparxei to xarakthristiko sta ek8emata kai poio brisketai pio psila*/
//    public ComparisonNode chooseUniqueComparatorBest(Map ajiologhsh)
//    {
//	ComparisonNode comp = null;
//	ComparisonNode tmp = this.findRoot();
//	double max = -10000;
//	String feature;
//	//int tmpInt=0;
//	
//	while(tmp.getChildren().size()>0)
//	{
//	    // an einai monadiko sto eidos toy
//	    if(tmp.getNumberOfObjects() == 1)
//	    {
//		for(int i=0; i<tmp.getAttributeNames().size(); i++)
//		{
//		    tmp.removeAttribute(tmp.getAttributeNames().get(i));
//		    i--;
//		}
//		return tmp;
//	    }
//	    tmp = tmp.getChildren().get(0);
//	}
//	
//	if(tmp.getNumberOfObjects() == 1)
//	{
//	    for(int i=0; i<tmp.getAttributeNames().size(); i++)
//	    {
//		tmp.removeAttribute(tmp.getAttributeNames().get(i));
//		i--;
//	    }
//	    return tmp;
//	}
//	
//	
//	tmp = this.findRoot();
//	while(tmp.getChildren().size()>0)
//	{
//	    if (tmp.getAttributeNames().size()>0)
//	    {
//		for (int i=0; i<tmp.getAttributeNames().size(); i++)
//		{
//		    String name = tmp.getAttributeNames().get(i);
//		    Iterator it = ajiologhsh.keySet().iterator();
//		    Object obj;
//		    while (it.hasNext())
//		    {
//			obj = it.next();
//			if (name.equalsIgnoreCase((String)obj))
//			{
//			    if (max <((Double)ajiologhsh.get(obj)))
//			    {
//				max = ((Double)ajiologhsh.get(obj));
//				comp = tmp;
//			    }
//			    else
//			    {
//				tmp.removeAttribute(name);
//				i--;
//			    }
//			}
//		    }
//		}
//		
//	    }
//	    
//	    tmp = tmp.getChildren().get(0);
//	}
//	//gia to katw katw sto dendro (fyllo)
//	if (tmp.getAttributeNames().size()>0)
//	{
//	    for (int i=0; i<tmp.getAttributeNames().size(); i++)
//	    {
//		String name = tmp.getAttributeNames().get(i);
//		Iterator it = ajiologhsh.keySet().iterator();
//		Object obj;
//		while (it.hasNext())
//		{
//		    obj = it.next();
//		    if (name.equalsIgnoreCase((String)obj))
//		    {
//			if (max <((Double)ajiologhsh.get(obj)))
//			{
//			    max = ((Double)ajiologhsh.get(obj));
//			    comp = tmp;
//			}
//			else
//			{
//			    tmp.removeAttribute(name);
//			    i--;
//			}
//		    }
//		}
//	    }
//	}
//	
//	return comp;
//    }
    
    /**Epistrefei ekeinh th sygkrish poy einai kalyterh me bash to poses fores
     yparxei to xarakthristiko sta ek8emata kai poio brisketai pio psila*/
    public ComparisonNode chooseUniqueComparatorBest(Map ajiologhsh)
    {
        ComparisonNode comp = null;
        ComparisonNode tmp = this.findRoot();
        double max = -10000;
        String feature;
        //int tmpInt=0;
  /*      
        while(tmp.getChildren().size()>0)
        {
            // an einai monadiko sto eidos toy
            if(tmp.getNumberOfObjects() == 1)
            {
                for(int i=0; i<tmp.getAttributeNames().size(); i++)
                {
                    tmp.removeAttribute(tmp.getAttributeNames().get(i));
                    i--;
                }
                return tmp;
            }
            tmp = tmp.getChildren().get(0);
         }
        
        if(tmp.getNumberOfObjects() == 1)
        {
            for(int i=0; i<tmp.getAttributeNames().size(); i++)
            {
                tmp.removeAttribute(tmp.getAttributeNames().get(i));
                i--;
            }
            return tmp;
        }
*/        
        
        //tmp = this.findRoot();
        while(tmp.getChildren().size()>0)
        {
            //de 8ymamai gia poio logo to ekana... kai de blepw th xrhsimothta
            //isws an dw problhmata sthn ektelesh na to epanaferw (an kai de nomizw)
            /*
            if(tmp.getNumberOfObjects() == 1)
            {
                for(int i=0; i<tmp.getAttributeNames().size(); i++)
                {
                    tmp.removeAttribute(tmp.getAttributeNames().get(i));
                    i--;
                }
                tmpInt =(int)(Math.random()*1000);
                if (max<tmpInt)
                {
                    max=tmpInt;
                    comp = tmp;
                }
            }
            else
            {*/
                if (tmp.getAttributeNames().size()>0)
                {
                    for (int i=0; i<tmp.getAttributeNames().size(); i++)
                    {
                        String name = tmp.getAttributeNames().get(i);
                        Iterator it = ajiologhsh.keySet().iterator();
                        Object obj;
                        while (it.hasNext()) {
                          obj = it.next();
                          if (name.equalsIgnoreCase((String)obj))
                          {
                              if (max <((Double)ajiologhsh.get(obj)))
                              {
                                  max = ((Double)ajiologhsh.get(obj));
                                  comp = tmp;
                              }
                              else
                              {
                                  tmp.removeAttribute(name);
                                  i--;
                              }
                          }
                        }
                    }
                    //return comp;
                }
        //    }
            tmp = tmp.getChildren().get(0);
        }
        //gia to katw katw sto dendro (fyllo)
        if (tmp.getAttributeNames().size()>0)
                {
                    for (int i=0; i<tmp.getAttributeNames().size(); i++)
                    {
                        String name = tmp.getAttributeNames().get(i);
                        Iterator it = ajiologhsh.keySet().iterator();
                        Object obj;
                        while (it.hasNext()) {
                          obj = it.next();
                          if (name.equalsIgnoreCase((String)obj))
                          {
                              if (max <((Double)ajiologhsh.get(obj)))
                              {
                                  max = ((Double)ajiologhsh.get(obj));
                                  comp = tmp;
                              }
                              else
                              {
                                  tmp.removeAttribute(name);
                                  i--;
                              }
                          }
                        }
                    }
                }
        /*
        if(tmp.getNumberOfObjects() == 1)
        {
            for(int i=0; i<tmp.getAttributeNames().size(); i++)
            {
                tmp.removeAttribute(tmp.getAttributeNames().get(i));
                i--;
            }
            tmpInt =(int)(Math.random()*1000);
            if (max<tmpInt)
            {
                max=tmpInt;
                comp = tmp;
            }
        }
        else
        {*/
     /*       if (tmp.getAttributeNames().size()>0)
            {
                tmpInt =(int)(Math.random()*1000);
                if (max<tmpInt)
                {
                    max=tmpInt;
                    comp = tmp;
                }
            }
        //}
       */
	
	if (comp != null)
	    return comp;
	    
	tmp = this.findRoot();
	while(tmp.getChildren().size()>0)
        {
            // an einai monadiko sto eidos toy
            if(tmp.getNumberOfObjects() == 1)
            {
                for(int i=0; i<tmp.getAttributeNames().size(); i++)
                {
                    tmp.removeAttribute(tmp.getAttributeNames().get(i));
                    i--;
                }
                return tmp;
            }
            tmp = tmp.getChildren().get(0);
         }
        
        if(tmp.getNumberOfObjects() == 1)
        {
            for(int i=0; i<tmp.getAttributeNames().size(); i++)
            {
                tmp.removeAttribute(tmp.getAttributeNames().get(i));
                i--;
            }
            return tmp;
        }
	
        return comp;
    }
    
    /**Epistrefei ekeinh th sygkrish poy einai kalyterh me basei to poses fores
     * yparxei to xarakthristiko sta ek8emata kai poio brisketai pio psila*/
    public ComparisonNode chooseCommonComparatorBest(Map ajiologhsh)
    {
	ComparisonNode comp = null;
	ComparisonNode tmp = this.findRoot();
	double max = -10000;
	String feature;
	
	while(tmp.getChildren().size()>0)
	{
	    if (tmp.getAttributeNames().size()>0)
	    {
		String nameBest="";
		for (int i=0; i<tmp.getAttributeNames().size(); i++)
		{
		    String name = tmp.getAttributeNames().get(i);
		    Iterator it = ajiologhsh.keySet().iterator();
		    Object obj;
		    while (it.hasNext())
		    {
			obj = it.next();
			if (name.equalsIgnoreCase((String)obj))
			{
			    if (max < ((Double)ajiologhsh.get(obj)))
			    {
				max = ((Double)ajiologhsh.get(obj));
				tmp.removeAttribute(nameBest);
				nameBest = name;
				comp = tmp;
			    }
			}
		    }
		}
		
	    }
	    
	    tmp = tmp.getChildren().get(0);
	}
	//gia to katw katw sto dendro (fyllo)
	if (tmp.getAttributeNames().size()>0)
	{
	    String nameBest="";
	    for (int i=0; i<tmp.getAttributeNames().size(); i++)
	    {
		String name = tmp.getAttributeNames().get(i);
		Iterator it = ajiologhsh.keySet().iterator();
		Object obj;
		while (it.hasNext())
		{
		    obj = it.next();
		    if (name.equalsIgnoreCase((String)obj))
		    {
			if (max < ((Double)ajiologhsh.get(obj)))
			{
			    max = ((Double)ajiologhsh.get(obj));
			    tmp.removeAttribute(nameBest);
			    nameBest = name;
			    comp = tmp;
			}
		    }
		}
	    }
	}
	
	return comp;
    }
    
    public void updateChoice(ComparisonNode comp, int choice)
    {
	ComparisonNode compar = this.findNode(comp.getName());
	
	for (int i =0; i<compar.getAttributeNames().size(); i++)
	{
	    if (compar.getAttributeNames().get(i).equalsIgnoreCase(comp.getAttributeNames().get(choice)))
	    {
		compar.getAttributeValues().get(i).chosen = true;
		break;
	    }
	}
    }
    
    /** H synarthsh epistrefei ton kombo me ton opoio 8a ginei h sygkrish mazi me to xarakthristiko
     * sto opoio 8a sygkri8oyn (to dendro omws exei krathsei ekeina ta stoixeia poy yparxoyn
     *se perissotera apo persent% stoixeia toy ka8e komboy)
     * strURI einai to URI toy stoixeioy me to opoio 8a ginei h sygkrish*/
    public ComparisonNode performComparatorSelectionBlured(String strURI, OntModel ontModel, int persent, UserModellingQueryManager UMQM, String UserType, List<String>tmp)
    {
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
	tmpTree.cleanForest(strURI);
	tmpTree.sortAttributes(UMQM, UserType);
	//System.out.println("To dendro mas arxika");
	//tmpTree.print();
	//allazw to dendro wste na exei mono ta kyriarxa xarakthristika
	tmpTree.performBluring(persent);
	//System.out.println("To dendro mas me ta kyriarxa xarakthristika");
	//tmpTree.print();
	// briskw ton kombo ston opoio anhkei h perigrafomenh ontothta. se periptwsh poy den yparxei tote
	// epistrefei null
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	boolean parent = false;
	if (descNode == null)
	{
	    descNode = tmpTree.findParentExistingNode(strURI, ontModel);
	    parent = true;
	}
	if (descNode != null)
	{
	    tmpTree.step1(descNode, describingInstance, parent);
	   // System.out.println("Meta to prwto bhma");
	   // tmpTree.print();
	   // System.out.println("(afairontas ta empty features)");
	    tmpTree.removeEmptyFeatures();
	    
	    tmpTree.deVisit();
	    tmpTree.removeChildren(this);
	  //  tmpTree.print();
	    tmpTree.step2(descNode, describingInstance);
	  //  System.out.println("Meta to deytero bhma");
	  //  tmpTree.print();
	    tmpTree.deVisit();
	    comparator = tmpTree.step3Interest(descNode);
	 //   System.out.println("Meta to trito bhma");
	   // if (comparator !=null)
		//comparator.print();
	   // System.out.println();
	}
	return comparator;//return null;
    }
    
    /** H synarthsh epistrefei ton kombo me ton opoio 8a ginei h sygkrish mazi me to xarakthristiko
     * sto opoio 8a sygkri8oyn
     * strURI einai to URI toy stoixeioy me to opoio 8a ginei h sygkrish*/
    public ComparisonNode performComparatorSelection(String strURI, OntModel ontModel, UserModellingQueryManager UMQM, String UserType,List<String>tmp)
    {
	// ftiaxnw enan kombo me ta xarakthristika ths perigrafomenhs ontothtas
	ComparisonNode describingInstance = new ComparisonNode(strURI, null, 0, tmp);
	describingInstance.addElement(strURI,ontModel, 0);
	
	// o kombos me ton opoio 8a ginei h sygkrish
	ComparisonNode comparator = null;
	
	//ftiaxnoyme ena neo dendro, etsi wste oi allages poy 8a kanoyme na mhn ephreasoyn to arxiko mas
	ComparisonTree tmpTree = new ComparisonTree(this);
	tmpTree.sortAttributes(UMQM, UserType);
	//System.out.println("To dendro mas arxika");
	//tmpTree.print();
	
	// briskw ton kombo ston opoio anhkei h perigrafomenh ontothta. se periptwsh poy den yparxei tote
	// epistrefei null
	ComparisonNode descNode = tmpTree.findNode(strURI);
	// an den yparxei sto dendro hdh
	boolean parent = false;
	if (descNode == null)
	{
	    descNode = tmpTree.findParentExistingNode(strURI, ontModel);
	    parent = true;
	}
            
	if (descNode != null)
	{
            tmpTree.cleanMelegkogloyForest(descNode.getName());
	    
	    tmpTree.step1(descNode, describingInstance, parent);
	   // System.out.println("Meta to prwto bhma");
	   // tmpTree.print();
	   // System.out.println("(afairontas ta empty features)");
	    tmpTree.removeEmptyFeatures();
	    
	    tmpTree.deVisit();
	    tmpTree.removeChildren(this);
//	    tmpTree.print();
	    
	    tmpTree.step2(descNode, describingInstance);
	    tmpTree.removeEmptyFeatures();
	    tmpTree.removeLess();
	  //  System.out.println("Meta to deytero bhma");
	  //  tmpTree.print();
	    
	    tmpTree.deVisit();
	    
	    comparator = tmpTree.step3MostWanted(describingInstance, descNode, parent);
	
	//    if (comparator !=null)
//		comparator.print();
//	    System.out.println();
	}
	return comparator;//return null;
    }
    
    public void step1Unique(ComparisonNode first,ComparisonNode describing)
    {
	ComparisonNode start = first;
	//afairei to fyllo
	
	start = start.getParent();
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    start.getChildren().remove(i);
	    i--;
	}
	start.visited = true;
	//krata th diadromh
	//start = start.getParent();
	//start.visited=true;
	while(start.getParent()!=null)
	{
	    for (int i=0; i<start.getParent().getChildren().size(); i++)
	    {
		if (!start.getParent().getChildren().get(i).visited)
		{
		    start.getParent().getChildren().remove(i);
		    i--;
		}
	    }
	    start = start.getParent();
	    start.visited = true;
	}
	
	//gia th riza (8a mporoysa na to eixa kanei olo mazi do{}while, alla den peirazei)
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    if (!start.getChildren().get(i).visited)
	    {
		start.getChildren().remove(i);
		i--;
	    }
	}
	start.visited = true;
	
	//osa xarakthristika den einai isa me 1
	while(start.getChildren().size()>0)
	{
	    if (start.getNumberOfObjects()>2)
	    {
		for (int i=0; i<start.getAttributeNames().size(); i++)
		{
		    int counter = 0;
		    for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		    {
			if (start.getAttributeValues().get(i).getDescription().get(j).Plh8os!=1)
			{
			    start.getAttributeValues().get(i).getDescription().remove(j);
			    j--;
			}
			else
			{
			    counter++;
			}
		    }
		    if (start.getAttributeValues().get(i).getDescription().size() == 0)
		    {
			start.getAttributeValues().remove(i);
			start.getAttributeNames().remove(i);
			i--;
		    }
		    
		    if (counter>2)
		    {
			start.getAttributeValues().remove(i);
			start.getAttributeNames().remove(i);
			i--;
		    }
		}
	    }
	    else
	    {
		for (int i=0; i<start.getAttributeNames().size(); i++)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		}
	    }
	    start = start.getChildren().get(0);
	}
	// an yparxei megalh poikilia timwn
	
	if (start.getNumberOfObjects()>2)
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		int counter = 0;
		for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    if (start.getAttributeValues().get(i).getDescription().get(j).Plh8os!=1)
		    {
			start.getAttributeValues().get(i).getDescription().remove(j);
			j--;
		    }
		    else
		    {
			counter++;
		    }
		}
		if (start.getAttributeValues().get(i).getDescription().size() == 0)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		}
		
		if (counter>2)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		}
	    }
	}
	else
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		start.getAttributeValues().remove(i);
		start.getAttributeNames().remove(i);
		i--;
	    }
	}
	
	//System.out.println("mono to monopati kai osa xarakthristika einai monadika");
//	this.print();
	
	//afairw apo to dendro osa xarakthristika den yparxoyn sthn perigrafomenh ontothta
	while(start!=null)
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		boolean removeAttribute = true;
		for (int j=0; j<first.getAttributeNames().size(); j++)
		{
		    if (first.getAttributeNames().get(j).equalsIgnoreCase(start.getAttributeNames().get(i)))
		    {
			for (int k=0; k<start.getAttributeValues().get(i).getDescription().size(); k++)
			{
			    boolean remove = true;
			    for (int l=0; l<first.getAttributeValues().get(j).getDescription().size(); l++)
			    {
				if (first.getAttributeValues().get(j).getDescription().get(l).Value.equalsIgnoreCase(start.getAttributeValues().get(i).getDescription().get(k).Value))
				{
				    remove = false;
				    removeAttribute = false;
				    //start.getAttributeValues().get(i).getDescription().remove(k);
				    //k--;
				}
			    }
			    if (remove)
			    {
				start.getAttributeValues().get(i).getDescription().remove(k);
				k--;
			    }
			    //remove = true;
			}
		    }
		    
		}
		if (start.getAttributeValues().get(i).getDescription().size()==0 || removeAttribute)
		{
		    start.getAttributeNames().remove(i);
		    start.getAttributeValues().remove(i);
		    i--;
		    //break;
		}
	    }
	    start=start.getParent();
	    
	}
	//System.out.println("to dendro telika");
//	this.print();
//	System.out.print("");
	
    }
    
    /**krata mono th diadromh apo th riza mexri ayto to fyllo
     * kai sbhnei osa xarakthristika exoyn pollaples times
     * epishs sbhnei kai osa xarakthristika den einai koina me thn perigrafomenh ontothta*/
    public void step1Common(ComparisonNode first,ComparisonNode describing)
    {
	ComparisonNode start = first;
	//afairei to fyllo
	start = start.getParent();
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    start.getChildren().remove(i);
	    i--;
	}
	start.visited = true;
	//krata th diadromh
	//start = start.getParent();
	//start.visited=true;
	while(start.getParent()!=null)
	{
	    for (int i=0; i<start.getParent().getChildren().size(); i++)
	    {
		if (!start.getParent().getChildren().get(i).visited)
		{
		    start.getParent().getChildren().remove(i);
		    i--;
		}
	    }
	    start = start.getParent();
	    start.visited = true;
	}
	
	//gia th riza (8a mporoysa na to eixa kanei olo mazi do{}while, alla den peirazei)
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    if (!start.getChildren().get(i).visited)
	    {
		start.getChildren().remove(i);
		i--;
	    }
	}
	start.visited = true;
	
	//osa xarakthristika den pairnoyn mono mia timh
	while(start.getChildren().size()>0)
	{
	    //na einai toylaxiston 3 ek8emata
	    if (start.getNumberOfObjects()>2)
	    {
		for (int i=0; i<start.getAttributeNames().size(); i++)
		{
		    boolean ignore = false;
		    if (start.getAttributeValues().get(i).getDescription().size() != 1)
		    {
			start.getAttributeValues().remove(i);
			start.getAttributeNames().remove(i);
			i--;
			ignore = true;
		    }
		    // an h timh den einai koinh gia ola
		    if (!ignore && start.getAttributeValues().get(i).getDescription().get(0).Plh8os != start.getNumberOfObjects())
		    {
			start.getAttributeValues().remove(i);
			start.getAttributeNames().remove(i);
			i--;
			ignore = true;
		    }
		    //an to exw hdh pei
		    if (!ignore && start.getAttributeValues().get(i).chosen)
		    {
			start.getAttributeValues().remove(i);
			start.getAttributeNames().remove(i);
			i--;
		    }
		}
	    }
	    else
	    {// ta afairw
		for (int i=0; i<start.getAttributeNames().size(); i++)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		    
		}
	    }
	    
	    start = start.getChildren().get(0);
	}
	
	if (start.getNumberOfObjects()>2)
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		boolean ignore = false;
		// an den pairnei mono mia timh
		if (start.getAttributeValues().get(i).getDescription().size() != 1)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		    ignore = true;
		}
		// an h timh den einai koinh gia ola
		if (!ignore &&start.getAttributeValues().get(i).getDescription().get(0).Plh8os != start.getNumberOfObjects())
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		    ignore = true;
		}
		//an to exw hdh pei
		if (!ignore && start.getAttributeValues().get(i).chosen)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		}
		//}
	    }
	}
	else
	{// ta afairw
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		start.getAttributeValues().remove(i);
		start.getAttributeNames().remove(i);
		i--;
		
	    }
	}
	
	//System.out.println("mono to monopati kai osa xarakthristika einai koina gia oloys");
	//this.print();
	
	//afairw apo to dendro osa xarakthristika den yparxoyn sthn perigrafomenh ontothta
	while(start!=null)
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		boolean removeAttribute = true;
		for (int j=0; j<first.getAttributeNames().size(); j++)
		{
		    if (first.getAttributeNames().get(j).equalsIgnoreCase(start.getAttributeNames().get(i)))
		    {
			for (int k=0; k<start.getAttributeValues().get(i).getDescription().size(); k++)
			{
			    boolean remove = true;
			    for (int l=0; l<first.getAttributeValues().get(j).getDescription().size(); l++)
			    {
				if (first.getAttributeValues().get(j).getDescription().get(l).Value.equalsIgnoreCase(start.getAttributeValues().get(i).getDescription().get(k).Value))
				{
				    remove = false;
				    removeAttribute = false;
				}
			    }
			    if (remove)
			    {
				start.getAttributeValues().get(i).getDescription().remove(k);
				k--;
			    }
			}
		    }
		    
		}
		if (start.getAttributeValues().get(i).getDescription().size()==0 || removeAttribute)
		{
		    start.getAttributeNames().remove(i);
		    start.getAttributeValues().remove(i);
		    i--;
		}
	    }
	    start=start.getParent();
	    
	}
	//System.out.println("to dendro telika");
	//this.print();
//	System.out.print("");
	
    }
    
    /**krata mono th diadromh apo th riza mexri ayto to fyllo
     * kai krata mono ekeina ta xarakthristika poy emfanizontai sto percent twn periptosewn
     *(to persent 8a eprepe na to pairnei ws eisodo, alla den peirazei)
     * epishs sbhse kai osa xarakthristika den einai koina me thn perigrafomenh ontothta*/
    public void step1MostCommon(ComparisonNode first,ComparisonNode describing)
    {
	double percent = 0.9;
	ComparisonNode start = first;
	//afairei to fyllo
	start = start.getParent();
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    start.getChildren().remove(i);
	    i--;
	}
	start.visited = true;
	//krata th diadromh
	//start = start.getParent();
	//start.visited=true;
	while(start.getParent()!=null)
	{
	    for (int i=0; i<start.getParent().getChildren().size(); i++)
	    {
		if (!start.getParent().getChildren().get(i).visited)
		{
		    start.getParent().getChildren().remove(i);
		    i--;
		}
	    }
	    start = start.getParent();
	    start.visited = true;
	}
	
	//gia th riza (8a mporoysa na to eixa kanei olo mazi do{}while, alla den peirazei)
	for (int i=0; i<start.getChildren().size(); i++)
	{
	    if (!start.getChildren().get(i).visited)
	    {
		start.getChildren().remove(i);
		i--;
	    }
	}
	start.visited = true;
	//this.print();
	//osa xarakthristika den emfanizoyn koinh timh sto percent (80%) twn periptwsewn
	while(start.getChildren().size()>0)
	{
	    percent = 0.9-(start.getNumberOfObjects()/5)*0.1;
	    if (percent<0.7)
		percent = 0.7;
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		
		for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    if (start.getNumberOfObjects()<3||start.getAttributeValues().get(i).getDescription().get(j).Plh8os/(double)start.getNumberOfObjects()<percent)
		    {
			start.getAttributeValues().get(i).getDescription().remove(j);
			j--;
		    }
		}
		boolean ignore = false;
		if (start.getAttributeValues().get(i).getDescription().size() == 0)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		    ignore = true;
		}
		//an to exw hdh pei
		if (!ignore && start.getAttributeValues().get(i).chosen)
		{
		    start.getAttributeValues().remove(i);
		    start.getAttributeNames().remove(i);
		    i--;
		}
	    }
	    start = start.getChildren().get(0);
	}
	
	percent = 0.9-(start.getNumberOfObjects()/5)*0.1;
	if (percent<0.7)
	    percent = 0.7;
	
	for (int i=0; i<start.getAttributeNames().size(); i++)
	{
	    for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
	    {
		if (start.getNumberOfObjects()<3||start.getAttributeValues().get(i).getDescription().get(j).Plh8os/(double)start.getNumberOfObjects()<percent)
		{
		    start.getAttributeValues().get(i).getDescription().remove(j);
		    j--;
		    
		}
	    }
	    boolean  ignore = false;
	    if (start.getAttributeValues().get(i).getDescription().size() == 0)
	    {
		start.getAttributeValues().remove(i);
		start.getAttributeNames().remove(i);
		i--;
		ignore = true;
	    }
	    //an to exw hdh pei
	    if (!ignore && start.getAttributeValues().get(i).chosen)
	    {
		start.getAttributeValues().remove(i);
		start.getAttributeNames().remove(i);
		i--;
	    }
	}
	//System.out.println("mono to monopati kai osa xarakthristika einai kyriarxa");
//	this.print();
	
	//afairw apo to dendro osa xarakthristika den yparxoyn sthn perigrafomenh ontothta
	while(start!=null)
	{
	    for (int i=0; i<start.getAttributeNames().size(); i++)
	    {
		boolean removeAttribute = true;
		for (int j=0; j<first.getAttributeNames().size(); j++)
		{
		    if (first.getAttributeNames().get(j).equalsIgnoreCase(start.getAttributeNames().get(i)))
		    {
			removeAttribute = false;
			break;
		    }
		    
		}
		if (start.getAttributeValues().get(i).getDescription().size()==0 || removeAttribute)
		{
		    start.getAttributeNames().remove(i);
		    start.getAttributeValues().remove(i);
		    i--;
		    //break;
		}
	    }
	    start=start.getParent();
	}
	
    }
    
    /**8a krathsw ta xarakthristika se ekeino to ypsos opoy fainetai na mhn allazoyn oi times toys
     * (na mhn prosti8entai dhladh nea ek8emata me ta idia xarakthristika), wste na jerw poiotika ti xarakthristika
     * einai (genika h eidikoy skopoy)*/
    public void klademaUnique(ComparisonTree real, ComparisonNode starting)
    {
	//kobontas ta xarakthristika sto ypsos ekeino poy den prosti8ontai nea ek8emata
	ComparisonNode startNode = starting;
	ComparisonNode nodeRealTree = real.findNode(startNode.getName());
	// gia ka8e xarakthristiko
	for (int i=0; i<startNode.getAttributeNames().size(); i++)
	{
	    //bres poses ontothtes exoyn to xarakthristiko se ayto to epipedo
	    int plh8os = nodeRealTree.getNumberOf(startNode.getAttributeNames().get(i));
	    ComparisonNode futureNode = startNode.getParent();
	    ComparisonNode klademaNode = starting;
	    while (futureNode != null)
	    {
		ComparisonNode futureNodeReal = real.findNode(futureNode.getName());
		int newPlh8os = futureNodeReal.getNumberOf(startNode.getAttributeNames().get(i));
		if (newPlh8os > plh8os)
		{
		    plh8os = newPlh8os;
		    klademaNode = futureNode;
		}
		futureNode = futureNode.getParent();
	    }
	    klademaNode.removeAttributeFromParents(startNode.getAttributeNames().get(i));
	}
    }
    
    /**Afairw ta fylla toy dendroy*/
    public void removeChildren(ComparisonTree original)
    {
	for (int i=0; i<original.Tree.size(); i++)
	{
	    if (original.Tree.get(i).getChildren().size() == 0)
	    {
		this.findNode(original.Tree.get(i).getName()).getParent().getChildren().remove(this.findNode(original.Tree.get(i).getName()));
		//this.Tree.get(i).getParent().getChildren().remove(this.findNode(original.Tree.get(i).getName()));
		this.Tree.remove(this.findIndexOfNode(original.Tree.get(i).getName()));
		//i--;
	    }
	}
    }
    
    /** ektelei to prwto bhma toy algori8moy Melegkogloy*/
    public void step1(ComparisonNode start,ComparisonNode describing, boolean parent)
    {
	//eimai ston kombo opoy anhkei h perigrafomenh ontothta
	removeAttributesNodesRelatives(start, describing);
	for (int i=0; i<start.numberOfChildren(); i++)
	{
	    if (!start.getChildren().get(i).visited)
		step1Children(start.getChildren().get(i), describing);
	}
	//anebainw ena akoma epipedo
//************************************************************************************//
	//edw mpainei o elegxos me to scope
	if (start.getParent() != null/*&&scope*/)
	    step1(start.getParent(),describing, parent);
    }
    
    /** ektelei to prwto bhma toy algori8moy Melegkogloy gia ola ta paidia poy den anhkoyn
     * sthn idia kathgoria*/
    public void step1Children(ComparisonNode start,ComparisonNode describing)
    {
	removeAttributesNodes(start, describing);
	// gia ka8e paidi epanelabe
	for (int i=0; i<start.numberOfChildren(); i++)
	{
	    if (!start.getChildren().get(i).visited)
		step1Children(start.getChildren().get(i), describing);
	}
    }
    
    /** Afairei apo ka8e kombo ekeina ta xarakthristika poy den yparxoyn sthn perigrafomenh ontothta*/
    public void removeAttributesNodes(ComparisonNode start,ComparisonNode describing)
    {
	//this.findRoot().print();
	// gia ka8e xarakthristiko
	for (int i=0; i<start.getAttributeNames().size(); i++)
	{
	    //an to xarakthristiko poy koitame den yparxei ka8oloy sthn perigrafomenh ontothta
	    // to afairoyme teleiws
	    if (describing.hasAttribute(start.getAttributeNames().get(i)))
	    {// osa xarakthristika exoyn diaforetikh timh
		for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    String desc = describing.getAttributeValues().get(describing.getAttributeNames().indexOf(start.getAttributeNames().get(i))).getDescription().get(0).Value;
		    if (!(start.getAttributeValues().get(i).getDescription().get(j).Value.equalsIgnoreCase(desc)))
		    {
			start.getAttributeValues().get(i).getDescription().remove(j);
			j--;
		    }
		}
	    }
	    else
	    {
		start.removeAttribute(start.getAttributeNames().get(i));
		i--;
	    }
	}
	
	start.visited = true;
    }
    
    /** Afairei apo ka8e kombo ekeina ta xarakthristika poy den yparxoyn sthn perigrafomenh ontothta
     * kai emfanizontai ligoteres apo 2 fores*/
    public void removeAttributesNodesMethodius(ComparisonNode start,ComparisonNode describing)
    {
	// gia ka8e xarakthristiko
	for (int i=0; i<start.getAttributeNames().size();i++)
	{
	    //an to xarakthristiko poy koitame den yparxei ka8oloy sthn perigrafomenh ontothta
	    // to afairoyme teleiws
	    if (describing.hasAttribute(start.getAttributeNames().get(i)))
	    {// osa xarakthristika exoyn diaforetikh timh
		for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    String desc = describing.getAttributeValues().get(describing.getAttributeNames().indexOf(start.getAttributeNames().get(i))).getDescription().get(0).Value;
		    if (!(start.getAttributeValues().get(i).getDescription().get(j).Value.equalsIgnoreCase(desc)))
		    {
			if (start.getAttributeValues().get(i).getDescription().get(j).Plh8os != start.getNumberOfObjects()||start.getNumberOfObjects() == 1)
			{
			    start.getAttributeValues().get(i).getDescription().remove(j);
			    j--;
			}
		    }
		    else
		    {
			if (start.getAttributeValues().get(i).getDescription().get(j).Plh8os != start.getNumberOfObjects() )
			{
			    start.getAttributeValues().get(i).getDescription().remove(j);
			    j--;
			}
		    }
		}
	    }
	    else
	    {
		start.removeAttribute(start.getAttributeNames().get(i));
		i--;
	    }
	}
	
	start.visited = true;
    }
    
    /** Afairei apo ka8e kombo ekeina ta xarakthristika poy den yparxoyn sthn perigrafomenh ontothta
     * kai emfanizontai ligoteres apo 2 fores*/
    public void removeAttributesNodesRelatives(ComparisonNode start,ComparisonNode describing)
    {
	// gia ka8e xarakthristiko
	for (int i=0; i<start.getAttributeNames().size();i++)
	{
	    //an to xarakthristiko poy koitame den yparxei ka8oloy sthn perigrafomenh ontothta
	    // to afairoyme teleiws
	    if (describing.hasAttribute(start.getAttributeNames().get(i)))
	    {// osa xarakthristika exoyn diaforetikh timh
		for (int j=0; j<start.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    String desc = describing.getAttributeValues().get(describing.getAttributeNames().indexOf(start.getAttributeNames().get(i))).getDescription().get(0).Value;
		    if (!(start.getAttributeValues().get(i).getDescription().get(j).Value.equalsIgnoreCase(desc)))
		    {
			if (start.getAttributeValues().get(i).getDescription().get(j).Plh8os <= 1)
			{
			    start.getAttributeValues().get(i).getDescription().remove(j);
			    j--;
			}
		    }
		}
	    }
	    else
	    {
		start.removeAttribute(start.getAttributeNames().get(i));
		i--;
	    }
	}
	
	start.visited = true;
    }
    
    /** ektelei to deytero bhma toy algori8moy Melegkogloy*/
    public void step2(ComparisonNode start, ComparisonNode describing)
    {
	for (int i=0; i < start.getAttributeValues().size(); i++)
	{
	    int size = start.getAttributeValues().get(i).getDescription().size();
	    for (int j = 0; j < size; j++)
	    {
		boolean found = false;
		if (start.getNumberOfObjects() == start.getAttributeValues().get(i).getDescription().get(j).Plh8os)
		{
		    // phgaine sta paidia kai bres an yparxei sthn idia posothta kai gia oles tis ontothtes parakatw
		    for (int k=0; k < start.getChildren().size(); k++)
		    {
			found = start.getChildren().get(k).foundInChildren(start.getAttributeNames().get(i), start.getAttributeValues().get(i).getDescription().get(j).Value, start.getNumberOfObjects());
			if (found)
			{
			    start.removeAttribute(start.getAttributeNames().get(i));
			    
			    //enas xazos tropos gia break.... alla mono ayto moy erxotan :-(
			    j=size;
			    i--;
			    break;
			}
		    }
		}
		else
		{
		    // phgaine sta paidia kai bres an yparxei sthn idia posothta kai gia oles tis ontothtes parakatw
		    for (int k=0; k < start.getChildren().size(); k++)
		    {
			found = start.getChildren().get(k).foundInChildren(start.getAttributeNames().get(i), start.getAttributeValues().get(i).getDescription().get(j).Value, start.getAttributeValues().get(i).getDescription().get(j).Plh8os);
			if (found)
			{
			    break;
			}
		    }
		    if (!found)
		    {
			start.removeFromChildren(start.getAttributeNames().get(i),start.getAttributeValues().get(i).getDescription().get(j).Value);
			j--;
			size--;
		    }
		    else
		    {
			start.removeAttribute(start.getAttributeNames().get(i));
			i--;
			break;
		    }
		    
		    
		}
	    }
	}
	
	start.visited = true;
	//anebainw ena akoma epipedo
//************************************************************************************//
	//edw mpainei o elegxos me to scope
	if (start.getParent() != null/*&&scope*/)
	    step2(start.getParent(),describing);
    }
    
    /** to trito bhma toy algori8moy Melegkogloy, dialegontas random */
    public ComparisonNode step3Random(ComparisonNode start, boolean parent)
    {
	this.deVisit();
	start.visited = true;
	boolean FC02 = true;
	if (start.getAttributeNames().size()>0)
	{
	    start.random = (int)(Math.random()*1000);
	    FC02 = false;
	}
	if (!parent)
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    if (FC02)
			FC02 = start.getChildren().get(i).step3RandomOneChildren();
		    else
			start.getChildren().get(i).step3RandomOneChildren();
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3Random(FC02);
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    public ComparisonNode chooseFC02Lot(ComparisonNode start, boolean parent)
    {
	this.deVisit();
	boolean FC02 = true;
	start.visited = true;
	if (start.getAttributeNames().size()>0 && start.getNumberOfObjects()>1)
	{
	    start.random = (int)(Math.random()*1000);
	}
	if (!parent)
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    start.getChildren().get(i).step3FC02LotChildren();
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3FC02ParentLot();
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    public ComparisonNode chooseFC02LotHistory(ComparisonNode start, boolean parent, int his, int plh8os)
    {
	this.deVisit();
	boolean FC02 = true;
	start.visited = true;
	if (start.getAttributeNames().size()>0 && start.getNumberOfObjects()>1&&plh8os-start.history<=his)
	{
	    start.random = (int)(Math.random()*1000);
	}
	if (!parent)
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    start.getChildren().get(i).step3FC02LotChildrenHistory(his, plh8os);
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3FC02ParentLotHistory(his, plh8os);
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    public ComparisonNode chooseFC02One(ComparisonNode start, boolean parent)
    {
	this.deVisit();
	boolean FC02 = true;
	start.visited = true;
	if (start.getAttributeNames().size()>0)
	{
	    start.random = (int)(Math.random()*1000);
	}
	if (!parent)
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    start.getChildren().get(i).step3FC02OneChildren();
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3FC02Parent();
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    public ComparisonNode chooseFC02OneHistory(ComparisonNode start, boolean parent, int his, int plh8os)
    {
	this.deVisit();
	boolean FC02 = true;
	start.visited = true;
	if (start.getAttributeNames().size()>0&& plh8os-start.history<=his)
	{
	    start.random = (int)(Math.random()*1000);
	}
	if (!parent)
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    start.getChildren().get(i).step3FC02OneChildrenHistory(his,plh8os);
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3FC02ParentHistory(his,plh8os);
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    public ComparisonNode chooseRandomLot(ComparisonNode start)
    {
	this.deVisit();
	boolean FC02 = true;
	start.visited = true;
	if (start.getAttributeNames().size()>0 && start.getNumberOfObjects()>1)
	{
	    start.random = (int)(Math.random()*1000);
	    FC02 = false;
	}
	//else
	{
	    for (int i=0; i<start.getChildren().size(); i++)
	    {
		if(!start.getChildren().get(i).visited)
		    if (FC02)
			FC02 = start.getChildren().get(i).step3RandomLotChildren();
		    else
			start.getChildren().get(i).step3RandomLotChildren();
	    }
	}
	if (start.getParent()!=null)
	    start.getParent().step3RandomLot(FC02);
	
	int max = 0;
	ComparisonNode ret = null;
	for (int i =0; i<this.Tree.size();i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		ret = this.Tree.get(i);
	    }
	}
	this.deRandom();
	this.deVisit();
	
	return ret;
    }
    
    /** to trito bhma toy algori8moy Melegkogloy, dialegontas basei toy interest */
    public ComparisonNode step3Interest(ComparisonNode start)
    {
	double max = -10000;
	int interest = -1;
	String nameBest="";
	ComparisonNode comp=null;
	//gia ka8e kombo toy dendroy
	for (int i = 0; i<this.Tree.size(); i++)
	{
	    //gia ka8e xarakthristiko toy
	    for (int j=0; j<this.Tree.get(i).getAttributeNames().size(); j++)
	    {
		if (this.Tree.get(i).getAttributeValues().get(j).getDescription().get(0).Plh8os == this.Tree.get(i).getNumberOfObjects())
		{
		    String name = this.Tree.get(i).getAttributeNames().get(j);
		    Iterator it = ajiologhsh.keySet().iterator();
		    Object obj;
		    while (it.hasNext())
		    {
			obj = it.next();
			if (name.equalsIgnoreCase((String)obj))
			{
			    if (max < ((Double)ajiologhsh.get(obj)))
			    {
				max = ((Double)ajiologhsh.get(obj));
				this.Tree.get(i).removeAttribute(nameBest);
				nameBest = name;
				comp = this.Tree.get(i);
			    }
			}
		    }
		}
	    }
	}
	return comp;
    }
    
    public boolean hasDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (this.Tree.get(i).hasDiffs(start))
		return true;
	}
	return false;
    }
    
    public void keepDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    this.Tree.get(i).keepDiffs(start);
	    
	}
    }
    
    public void removeDiffs(ComparisonNode start)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    this.Tree.get(i).removeDiffs(start);
	    
	}
    }
    
    public void keepOneObject(ComparisonNode start)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    this.Tree.get(i).keepOneObject(start);
	    
	}
    }
    
    public void keepMultiObject(ComparisonNode start)
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    this.Tree.get(i).keepMultiObject(start);
	    
	}
    }
    
    public ComparisonNode step3MostWanted(ComparisonNode descr, ComparisonNode start, boolean parent)
    {
	ComparisonNode returning = null;
	if(this.hasDiffs(descr))
	{
	    this.keepDiffs(descr);
	    returning = step3Random(start, parent);
	}
	else
	{
	    this.cleaning();
	    //gia to peirama me tis epanalhpseis
	    this.removeLastUsedComparisonFeature();
	    
	    //returning = chooseFC02Lot(start,parent);
	    returning = chooseFC02LotHistory(start,parent, 4, this.plh8os);
	    if (returning == null)
	    {
		returning = this.chooseFC02OneHistory(start, parent, 4, this.plh8os);
		//returning = this.chooseFC02One(start, parent);
	    }
	    if (returning == null)
	    {
		returning = this.chooseNoFC02History(start, 4);
		//returning = this.chooseRandom();
	    }
	}
	
	return returning;
    }
    
    public ComparisonNode chooseRandom()
    {
	this.deRandom();
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (this.Tree.get(i).getAttributeNames().size()>0)
	    {
		this.Tree.get(i).random = (int)(Math.random()*1000);
	    }
	}
	
	
	int max = 0;
	int pos = -1;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		pos = i;
	    }
	}
	
	this.deRandom();
	
	if (pos>-1)
	{
	    return this.Tree.get(pos);
	}
	else
	    return null;
    }
    
    
    public ComparisonNode chooseNoFC02History(ComparisonNode start, int his)
    {
	this.deRandom();
	for (int j = 1; j<=his; j++)
	{
	    for (int i=0; i<this.Tree.size(); i++)
	    {
		if (this.plh8os - this.Tree.get(i).history == j && this.Tree.get(i).getAttributeNames().size()>0 && this.Tree.get(i).getNumberOfObjects()>1)
		{
		    this.Tree.get(i).random = (int)(Math.random()*1000);
		}
	    }
	}
	
	
	int max = 0;
	int pos = -1;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		pos = i;
	    }
	}
	this.deRandom();
	if (pos>-1)
	{
	    return this.Tree.get(pos);
	}
	
	for (int j = 1; j<=his; j++)
	{
	    for (int i=0; i<this.Tree.size(); i++)
	    {
		if (this.plh8os - this.Tree.get(i).history == j && this.Tree.get(i).getAttributeNames().size()>0)
		{
		    this.Tree.get(i).random = (int)(Math.random()*1000);
		}
	    }
	}
	
	max = 0;
	pos = -1;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (max<this.Tree.get(i).random)
	    {
		max = this.Tree.get(i).random;
		pos = i;
	    }
	}
	this.deRandom();
	if (pos>-1)
	{
	    return this.Tree.get(pos);
	}
	
	this.deRandom();
	return null;
    }
    
    public void cleaning()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    for (int j=0; j<this.Tree.get(i).getAttributeValues().size();j++)
	    {
		if (this.Tree.get(i).getNumberOfObjects()!=this.Tree.get(i).getAttributeValues().get(j).getDescription().get(0).Plh8os)
		{
		    this.Tree.get(i).removeAttribute(this.Tree.get(i).getAttributeNames().get(j));
		}
	    }
	}
    }
    
    /** ektelei to trito bhma toy algori8moy Melegkogloy dialegontas to pio syggenes*/
    public ComparisonNode step3(ComparisonNode start)
    {
	if (start.getAttributeNames().size()>0&&start.getChildren().size()>0)
	    return start;
	else
	{
	    distanceNodes fatherNode = start.findParentStep3(1);
	    distanceNodes childNode = start.findChildStep3(1);
	    if (fatherNode.Node == null)
		return childNode.Node;
	    if (childNode.Node == null)
		return fatherNode.Node;
	    if (childNode.Distance<fatherNode.Distance)
		return childNode.Node;
	    else
		return fatherNode.Node;
	}
	
    }
    
    /**Epistrefei ean o kombos me onoma pateras einai gonios toy komboy me onoma child*/
    public boolean isParent(String child, String pateras)
    {
	ComparisonNode childNode = null;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (this.Tree.get(i).getName().equalsIgnoreCase(child))
	    {
		childNode = this.Tree.get(i);
		break;
	    }
	}
	if (childNode == null)
	    return false;
	while (childNode.getParent()!=null)
	{
	    if(childNode.getParent().getName().equalsIgnoreCase(pateras))
	    {
		return true;
	    }
	    childNode = childNode.getParent();
	}
	return false;
    }
    
    /** briskei ton kombo ston opoio anhkei h perigrafomenh ontothta (me onoma strURI).
     * Se periptwsh poy den yparxei epistrefei null*/
    public ComparisonNode findNode(String strURI)
    {
	ComparisonNode node = null;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getName().equalsIgnoreCase(strURI))
	    {
		node = Tree.get(i);
		break;
	    }
	}
	return node;
    }
    
    /** briskei ton ari8mo toy komboy ston opoio anhkei h perigrafomenh ontothta (me onoma strURI).
     * Se periptwsh poy den yparxei epistrefei null*/
    public int findIndexOfNode(String strURI)
    {
	int node = 0;
	for (int i=0; i<this.Tree.size(); i++)
	{
	    if (Tree.get(i).getName().equalsIgnoreCase(strURI))
	    {
		node = i;
		break;
	    }
	}
	return node;
    }
    
    /** briskei ton prwto gonea kombo ston opoio anhkei h perigrafomenh ontothta (me onoma strURI).
     * Se periptwsh poy den yparxei epistrefei null*/
    public ComparisonNode findParentExistingNode(String strURI, OntModel ontModel)
    {
	ComparisonNode node = null;
	Individual MyInstance = ontModel.getIndividual(strURI);
	
	String name = strURI;
	
	StmtIterator iter = MyInstance.listProperties(RDF.type);
	
	Statement stmt = (Statement)iter.next();
	Resource superCls = (Resource)stmt.getObject().as(Resource.class);
	//System.out.println("superclass " +superCls.getURI());
	node = findNode(superCls.getURI());
	if (node != null)
	    return node;
	
	OntClass cls = ontModel.getOntClass(superCls.getURI());
	name = superCls.getURI();
	
	while (returnNode(name) == null&&name.indexOf("Thing")<0)
	{
	    if (cls.listSuperClasses().hasNext())
            {
                Iterator i = cls.listSuperClasses();
                while (i.hasNext())
		{
		OntClass c = (OntClass) i.next();
		
		if (c.isRestriction())
		{
		    Restriction r = c.asRestriction();
		    
		    if (r.isAllValuesFromRestriction())
		    {
			AllValuesFromRestriction av = r.asAllValuesFromRestriction();
		//	System.out.println( "AllValuesFrom class " +
	//			av.getAllValuesFrom().getLocalName() +
	//			" on property " + av.getOnProperty().getLocalName() );
		    }
		}
		else
		{
		    cls = c;
		    name = cls.getURI();
		    break;
		}
		}
            }
	    else
		cls = null;
	    
	    if(cls !=null)
	    {
		name = cls.getURI();
		node = findNode(name);
		if (node != null)
		{
		    return node;
		}
	    }
	    else
	    {
		break;
	    }
	}
	return null;
    }
    
    /** Kanei oles tis metablhtes visited toy dendroy na exoyn timh false*/
    public void deVisit()
    {
	ComparisonNode root = this.findRoot();
	if (root != null)
	{
	    root.visited = false;
	    for (int i=0; i < root.getChildren().size(); i++)
	    {
		root.getChildren().get(i).deVisit();
	    }
	}
    }
    
    /** Kanei oles tis metablhtes random toy dendroy na exoyn timh 0*/
    public void deRandom()
    {
	for (int i=0; i<this.Tree.size(); i++)
	{
	    this.Tree.get(i).random = 0;
	}
    }
    
    /**Krata mono ekeina ta xarakthristika toy dendroy poy yparxoyn sto x pososto twn antikeimenwn
     * toy ka8e komboy*/
    public void performBluring(int x)
    {
	ComparisonNode root = this.findRoot();
	if (root!=null)
	    root.performBluring(x);
    }
    
    /** epistrefei ola ta fylla poy briskontai katw apo ton kombo me to sygkekrimeno onoma*/
    public List <String> getAllChildren(String name)
    {
	List <String> children = new LinkedList<String>();
	ComparisonNode tmp = this.findNode(name);
	for (int i = 0; i<tmp.getChildren().size(); i++)
	{
	    tmp.getChildren().get(i).fillChildren(children);
	}
	return children;
    }
    
    /**arxikopoiei to Map ajiologhsh poy pairnei ws orisma kai toy bazei ola ta xarakthristika poy yparxoyn sth
     * riza toy dendro analoga me thn ajia toys (poso syxna emfanizontai)*/
    public void sortAttributes()
    {
	ComparisonNode root = this.findRoot();
	if (root!=null)
	{
	    for(int i=0; i<root.getAttributeNames().size(); i++)
	    {
		int plh8os = 0;
		for (int j = 0; j<root.getAttributeValues().get(i).getDescription().size(); j++)
		{
		    plh8os+=root.getAttributeValues().get(i).getDescription().get(j).Plh8os;
		}
		double value = plh8os/(double)root.getNumberOfObjects();
		ajiologhsh.put(root.getAttributeNames().get(i),value);
	    }
	    
	    Iterator it = ajiologhsh.keySet().iterator();
	    String obj;
	    while (it.hasNext())
	    {
		obj = (String)it.next();
	//	System.out.println(obj + ": " + ajiologhsh.get(obj));
	    }
	}
    }
    
    public void sortAttributes(UserModellingQueryManager UMQM, String user)
    {
	ComparisonNode root = this.findRoot();
	if (root!=null)
	{
	    for(int i=0; i<root.getAttributeNames().size(); i++)
	    {
		double value = UMQM.getDInterest(root.getAttributeNames().get(i), user);
		ajiologhsh.put(root.getAttributeNames().get(i),value);
	    }
	    
	    Iterator it = ajiologhsh.keySet().iterator();
	    String obj;
	    while (it.hasNext())
	    {
		obj = (String)it.next();
	//	System.out.println(obj + ": " + ajiologhsh.get(obj));
	    }
	}
    }
    
    /**epistrefei an h riza toy dendroy exei paidi kapoio kombo me onoma childName*/
    public boolean rootHasChild(String childName)
    {
	ComparisonNode root = this.findRoot();
	for (int i=0; i<root.getChildren().size(); i++)
	{
	    if (root.getChildren().get(i).getName().equalsIgnoreCase(childName))
		return true;
	}
	return false;
    }
    
    /**epistrefei ti typoy einai o kombos me onoma nodeName (epistrefei dhladh to onoma toy progonoy toy poy einai paidi ths rizas)*/
    public String kindOf(String nodeName)
    {
	ComparisonNode node = this.findNode(nodeName);
	ComparisonNode root = this.findRoot();
	
	while (node.getParent()!=root)
	{
	    node = node.getParent();
	}
	
	return node.getName();
    }
    
    public Vector<String> membersOf(String name)
    {
	Vector<String> members = new Vector();
	ComparisonNode node = this.findNode(name);
	if(node!=null)
	    node.membersOf(members);
	return members;
    }
}
