package org.apache.xml.security.samples.transforms;



import java.io.*;
import org.apache.xml.security.c14n.*;
import org.apache.xml.security.signature.*;
import org.apache.xml.security.transforms.*;
import org.apache.xml.security.transforms.params.*;
import org.apache.xml.security.utils.*;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.crypto.*;


/**
 * Sample for the <I>XML Signature XPath Filter v2.0</I>
 *
 * @author $Author: geuerp $
 * @see <A HREF="http://www.w3.org/TR/xmldsig-filter2/">XPath Filter v2.0 (TR)</A>
 * @see <A HREF=http://www.w3.org/Signature/Drafts/xmldsig-xfilter2/">XPath Filter v2.0 (editors copy)</A>
 */
public class SampleTransformXPath2Filter2 {

   /**
    * Method main
    *
    * @param args
    * @throws Exception
    */
   public static void main(String args[]) throws Exception {

      org.apache.xml.security.Init.init();

      boolean verbose = true;

      create("withComments.xml", true, verbose);
      System.out.println();
      System.out.println();
      System.out.println();
      create("omitComments.xml", false, verbose);
      System.out.println();
      System.out.println();
      System.out.println();
      check("withComments.xml");
   }

   /**
    * Method create
    *
    * @param filename
    * @param withComments
    * @param verbose
    * @throws Exception
    */
   public static void create(
           String filename, boolean withComments, boolean verbose)
              throws Exception {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setNamespaceAware(true);

      DocumentBuilder db = dbf.newDocumentBuilder();
      //J-
      String inputDoc = "<A><UNSIGNED><B><SIGNED><MORE_SIGNED_STUFF/></SIGNED><C><UNSIGNED/></C></B><D><UNSIGNED/></D><UNSIGNED><E><SIGNED><MORE_SIGNED_STUFF/></SIGNED></E><UNSIGNED><F><G><H/></G></F></UNSIGNED></UNSIGNED></UNSIGNED></A>";

      //J+
      Document doc = db.parse(new ByteArrayInputStream(inputDoc.getBytes()));
      XMLSignature sig = new XMLSignature(doc, null,
                                          XMLSignature.ALGO_ID_MAC_HMAC_SHA1);

      doc.getDocumentElement().appendChild(sig.getElement());
      sig.getElement().setAttributeNS(Constants.NamespaceSpecNS, "xmlns:" + ElementProxy.getDefaultPrefix(Transforms.TRANSFORM_XPATH2FILTER), Transforms.TRANSFORM_XPATH2FILTER);
      doc.getDocumentElement().appendChild(doc.createTextNode("\n"));

      Transforms transforms = new Transforms(doc);
      //J-

      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
            XPath2FilterContainer.newInstanceIntersect(doc, "//E").getElement());

      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
            XPath2FilterContainer.newInstanceUnion(doc, "//B").getElement());
      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
         XPath2FilterContainer.newInstanceSubtract(doc, "//C").getElement());

      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
            XPath2FilterContainer.newInstanceUnion(doc, "//F").getElement());
      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
         XPath2FilterContainer.newInstanceSubtract(doc, "//G").getElement());
      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER,
            XPath2FilterContainer.newInstanceUnion(doc, "//H").getElement());


      /*
      XPath2FilterContainer x = XPath2FilterContainer.newInstanceSubtract(doc, "here()/ancestor::ds:Signature[1]");
      x.setXPathNamespaceContext("ds", Constants.SignatureSpecNS);
      transforms.addTransform(Transforms.TRANSFORM_XPATH2FILTER, x.getElement());
      if (withComments) {
         transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
      }
      */
      //J+
      sig.addDocument("", transforms);

      String secretKey = "secret";

      sig.getKeyInfo().addKeyName("The UTF-8 octets of \"" + secretKey
                                  + "\" are used for signing ("
                                  + secretKey.length() + " octets)");
      sig.sign(sig.createSecretKey(secretKey.getBytes()));

      Canonicalizer c14n =
         Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS);
      byte[] full = c14n.canonicalizeSubtree(doc);
      FileOutputStream fos = new FileOutputStream(filename);

      try {
         fos.write(full);
      } finally {
         fos.close();
      }

      if (verbose) {
         System.out.println(
            "-------------------------------------------------------------");
         System.out.println("Input to the transforms is");
         System.out.println(
            "-------------------------------------------------------------");
         System.out
            .println(new String(sig.getSignedInfo().item(0).getTransformsInput()
               .getBytes()));
         System.out.println(
            "-------------------------------------------------------------");
         System.out
            .println("The signed octets (output of the transforms) are ");
         System.out.println(
            "-------------------------------------------------------------");
         System.out
            .println(new String(sig.getSignedInfo().item(0)
               .getTransformsOutput().getBytes()));
         System.out.println(
            "-------------------------------------------------------------");
         System.out.println("The document is ");
         System.out.println(
            "-------------------------------------------------------------");
         System.out.println(new String(full));
         System.out.println(
            "-------------------------------------------------------------");
      }
   }

   /**
    * Method check
    *
    * @param filename
    * @throws Exception
    */
   public static void check(String filename) throws Exception {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setNamespaceAware(true);

      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(new FileInputStream(filename));
      NodeList sigs = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);

      XMLSignature sig = new XMLSignature((Element)sigs.item(0), new File(filename).toURL().toString());
      boolean check = sig.checkSignatureValue(sig.createSecretKey("secret".getBytes()));

         System.out.println(
            "-------------------------------------------------------------");
      System.out.println("Verification of " + filename + ": " + check);
         System.out.println(
            "-------------------------------------------------------------");
         System.out
            .println(new String(sig.getSignedInfo().item(0)
               .getTransformsOutput().getBytes()));
         System.out.println(
            "-------------------------------------------------------------");

   }
}
