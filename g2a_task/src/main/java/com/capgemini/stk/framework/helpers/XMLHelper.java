package com.capgemini.stk.framework.helpers;

import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.response.Response;

import static io.restassured.path.xml.config.XmlPathConfig.xmlPathConfig;
import static org.junit.Assert.assertNotNull;

public class XMLHelper {
    public static Node getNodeIgnoreCaseAndNamespace(Node parentNode, String childNodeName) {
        for (Node childNode : parentNode.children().list()) {
            String childName = childNode.name();
            childName = childName.substring(childName.indexOf(":") + 1);
            if (childName.equalsIgnoreCase(childNodeName)) {
                return childNode;
            }
        }
        return null;
    }

    public static String getNodeValueIgnoreCaseAndNamespace(Node parentNode, String childNodeName) {
        for (Node childNode : parentNode.children().list()) {
            String childName = childNode.name();
            childName = childName.substring(childName.indexOf(":") + 1);
            if (childName.equalsIgnoreCase(childNodeName)) {
                return childNode.value();
            }
        }
        return null;
    }

    private static Node getRootNode(Response response) {
        XmlPath xmlPath = new XmlPath(response.getBody().asString()).using(xmlPathConfig().namespaceAware(false));
        assertNotNull("XmlPath is null", xmlPath);
        Node root = xmlPath.get();
        assertNotNull("Response root node is incorrect", root);
        return root;
    }

    private static Node getBody(Node envelope) {
        Node body = getNodeIgnoreCaseAndNamespace(envelope, "Body");
        assertNotNull("Body node is incorrect", body);
        return body;
    }

    private static Node getFault(Response response) {
        Node root = getRootNode(response);
        if (root.name().toLowerCase().equals("fault")) {
            return root;
        }
        Node body = getBody(root);
        if (null == body) {
            return null;
        }
        return getNodeIgnoreCaseAndNamespace(body, "Fault");
    }

    private static Node getFaultBasic(Response response) {
        Node fault = getFault(response);
        if (null == fault) {
            return null;
        }
        Node detail = getNodeIgnoreCaseAndNamespace(fault, "detail");
        if (null == detail) {
            return null;
        }
        Node faultChild = getNodeIgnoreCaseAndNamespace(detail, "Fault");
        if (null == faultChild) {
            return null;
        }
        return getNodeIgnoreCaseAndNamespace(faultChild, "FaultBasic");
    }

    private static String getFaultBasicDetails(Response response, String nodeName) {
        Node faultBasic = getFaultBasic(response);
        if (null == faultBasic) {
            return "";
        }
        Node faultCode = getNodeIgnoreCaseAndNamespace(faultBasic, nodeName);
        if (null == faultCode) {
            return "";
        }
        return faultCode.value();
    }

    public static String getFaultDescription(Response response) {
        return getFaultBasicDetails(response, "FaultDescription");
    }

    public static String getFaultCode(Response response) {
        return getFaultBasicDetails(response, "FaultCode");
    }

    public static String getFaultClass(Response response) {
        return getFaultBasicDetails(response, "FaultClass");
    }

    public static String getFaultLevel(Response response) {
        return getFaultBasicDetails(response, "FaultLevel");
    }

    public static String getFaultReason(Response response) {
        return getFaultBasicDetails(response, "FaultReason");
    }

    public static String getFaultString(Response response) {
        Node fault = getFault(response);
        if (null == fault) {
            return "";
        }
        Node faultString = getNodeIgnoreCaseAndNamespace(fault, "faultstring");
        if (null == faultString) {
            return "";
        }
        return faultString.value();
    }
}