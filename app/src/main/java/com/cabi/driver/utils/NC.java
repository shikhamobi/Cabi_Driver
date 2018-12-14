package com.cabi.driver.utils;

import com.cabi.driver.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 29/11/16.
 */

/**
 * Used to Store and get string files from Hash map
 */
public class NC {
    public static HashMap<Integer, String> nfields_byID = new HashMap<>();
    public static HashMap<String, String> nfields_byName = new HashMap<>();


    public static ArrayList<String> fields = new ArrayList<>();
    public static ArrayList<String> fields_value = new ArrayList<>();

    public static HashMap<String, Integer> fields_id = new HashMap<>();


    static NC NC = null;

    static NC getInstance() {
        if (NC == null)
            NC = new NC();
        else
            NC = NC;
        return NC;
    }

    public static NC getResources() {
        return getInstance();
    }

    public static NC getActivity() {
        return getInstance();
    }

    public static String getString(int c) {
        String text = "";
        if (fields_value.contains(text)) {
//            System.out.println("nagaaaaaaa___"+SplashAct.fields_value.indexOf(text));
//            System.out.println("nagaaaaaaa___"+SplashAct.fields.get(SplashAct.fields_value.indexOf(text)));


            //nfields_byID

            // getString();
        }

        if (nfields_byID.get(c) == null && MainActivity.context != null)
            return MainActivity.context.getString(c);

        else
            return nfields_byID.get(c);
    }


    /*private synchronized void getAndStoreStringValues(String result) {
        try {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");

            System.out.println("nagaSsss___ize" + nList.getLength());
            int chhh = 0;
            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                System.out.println("nagaSsss___agg" + node.getTextContent() + "___");
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
                    if (element2.getAttribute("name").equals("pressBack"))
                        System.out.println("nagaSsss___ize" + chhh + "___" + element2.getTextContent());

                    //  NC.nfields_value.add(element2.getTextContent());
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            System.out.println("nagaSsss___izelllll" + NC.fields_id.size());
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* synchronized void getValueDetail() {
        System.out.println("NagarrrHIIIIIII");
        Field[] fieldss = R.string.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        System.out.println("NagarrrHIIIIIII***" + fieldss.length + "___" + NC.nfields_byName.size());
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", getActivity());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                NC.fields.add(fieldss[i].getName());
                NC.fields_value.add(NC.getResources().getString(id));
                NC.fields_id.put(fieldss[i].getName(), id);

            } else {
                System.out.println("Imissedthepunchrefree" + fieldss[i].getName());
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }


        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();
            System.out.println("NagarrrHIIIIIII&&&" + NC.fields_id.get(h) + "____" + NC.nfields_byName.get(h));
            NC.nfields_byID.put(NC.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }

        System.out.println("NagarrrBye" + NC.fields.size() + "___" + NC.fields_value.size() + "___" + NC.fields_id.size());
    }*/
}
