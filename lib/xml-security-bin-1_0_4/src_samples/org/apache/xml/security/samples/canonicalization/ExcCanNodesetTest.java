/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "<WebSig>" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2001, Institute for
 * Data Communications Systems, <http://www.nue.et-inf.uni-siegen.de/>.
 * The development of this software was partly funded by the European
 * Commission in the <WebSig> project in the ISIS Programme.
 * For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.xml.security.samples.canonicalization;



import org.apache.xml.security.c14n.*;
import org.w3c.dom.*;
import org.apache.xpath.XPathAPI;
import org.apache.xml.security.utils.*;


/**
 * Class ExcCanNodesetTest
 *
 * @author René Kollmorgen
 * @version $Revision: 1.1 $
 */
public class ExcCanNodesetTest {

   /**
    * Method main
    *
    * @param args
    */
   public static void main(String[] args) {
      org.apache.xml.security.Init.init();

      try {
         //J-
      String node = ""
        + "<doc xmlns=\"urn:default\" xmlns:x=\"urn:xns\"><!-- comment inside -->\n"
        + "   <x:e1   />\n"
        + "   <x:e2   ></x:e2>\n"
        + "   <x:e3    name = \"elem3\"   id=\"elem3\"    />\n"
        + "   <x:e4    name=\"elem4\"   id=\"elem4\"    ></x:e4>\n"
        + "   <x:e5 a:attr=\"out\" b:attr=\"sorted\" attr2=\"all\" attr=\"I'm\"\n"
        + "       xmlns=\"http://example.org\">\n"
        + "       <e6> e6 tag </e6>\n"
        + "       <x:e7> e7 tag </x:e7>\n"
        + "   </x:e5>\n"
        + "   <x:e8 xmlns=\"\" xmlns:a=\"http://www.w3.org\">\n"
        + "       <x:e9 xmlns=\"http://www.ietf.org\">\n"
        + "           <x:e10 xmlns=\"\" xmlns:a=\"http://www.w3.org\">\n"
        + "               <x:e11 xmlns=\"http://www.ietf.org\"/>\n"
        + "               <text>&#169;</text>\n"
        + "           </x:e10>\n"
        + "       </x:e9>\n"
        + "   </x:e8>\n"
        + "   <x:e1   />\n"
        + "   <x:e2   ></x:e2>\n"
        + "   <x:e3    name = \"elem3\"   id=\"elem3\"    />\n"
        + "   <x:e4    name=\"elem4\"   id=\"elem4\"    ></x:e4>\n"
        + "   <x:e5 a:attr=\"out\" b:attr=\"sorted\" attr2=\"all\" attr=\"I'm\"\n"
        + "       xmlns=\"http://example.org\">\n"
        + "       <e6> e6 tag </e6>\n"
        + "       <x:e7> e7 tag </x:e7>\n"
        + "   </x:e5>\n"
        + "   <x:e8 xmlns=\"\" xmlns:a=\"http://www.w3.org\">\n"
        + "       <x:e9 xmlns=\"http://www.ietf.org\">\n"
        + "           <x:e10 xmlns=\"\" xmlns:a=\"http://www.w3.org\">\n"
        + "               <x:e11 xmlns=\"http://www.ietf.org\"/>\n"
        + "               <text>&#169;</text>\n"
        + "           </x:e10>\n"
        + "       </x:e9>\n"
        + "   </x:e8>\n"
        + "</doc><!-- Comment 3 -->\n";
      //J+
         java.io.InputStream in =
            new java.io.ByteArrayInputStream(node.getBytes());
         javax.xml.parsers.DocumentBuilderFactory dbf =
            javax.xml.parsers.DocumentBuilderFactory.newInstance();

         dbf.setNamespaceAware(true);

         javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
         org.w3c.dom.Document doc = db.parse(in);
         Element nscontext = XMLUtils.createDSctx(doc, "x",
                                                  "xmlns:x=\"urn:xns\"");
         NodeList nsList = XPathAPI.selectNodeList(
            doc.getDocumentElement(),
            "(//. | //@* | //namespace::*)[ancestor-or-self::x:e8]");    //,nscontext);
         Canonicalizer can =
            Canonicalizer
               .getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

         //Canonicalizer can = Canonicalizer.getInstance( Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS );
         //can.setIncludingPrefixes("#default");
         long start = System.currentTimeMillis();

         System.out.println("Start to canonicalize.");

         byte[] canSubset = can.canonicalizeXPathNodeSet(nsList);

         System.out.println("Finished canonicalization. Time: "
                            + (System.currentTimeMillis() - start) + " ms");
         System.out.println();
         System.out.println("canSubset: \n" + new String(canSubset));
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
}
