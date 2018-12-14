package com.cabi.driver.utils;

import android.content.Context;

import com.cabi.driver.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by developer on 21/2/17.
 */
public class ColorRestore {
public static  Context c;
    /**
     * Adding color files to Local hashmap
     */
    public static synchronized void getAndStoreColorValues(String result,Context c) {
        try {
            ColorRestore.c=c;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

          //  System.out.println("lislength" + nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
//                    if (element2.getAttribute("name").equals("pressBack"))
//                        System.out.println("size" + chhh + "___" + element2.getTextContent());

                    //  NC.nfields_value.add(element2.getTextContent());
                    CL.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            getColorValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Getting Color values from local hash map
     */
  public static  synchronized void getColorValueDetail() {
        Field[] fieldss = R.color.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        for (int i = 0; i < fieldss.length; i++) {
            int id = c.getResources().getIdentifier(fieldss[i].getName(), "color", c.getPackageName());
            if (CL.nfields_byName.containsKey(fieldss[i].getName())) {
                CL.fields.add(fieldss[i].getName());
                CL.fields_value.add(c.getResources().getString(id));
                CL.fields_id.put(fieldss[i].getName(), id);

            } else {
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }

        for (Map.Entry<String, String> entry : CL.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            CL.nfields_byID.put(CL.fields_id.get(h), CL.nfields_byName.get(h));
            // do stuff
        }

        // System.out.println("NagarrrBye"+CL.fields.size()+"___"+CL.fields_value.size()+"___"+ CL.fields_id.size());
    }
}
