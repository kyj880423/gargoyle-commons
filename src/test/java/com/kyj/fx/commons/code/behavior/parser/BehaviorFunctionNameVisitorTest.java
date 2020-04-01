package com.kyj.fx.commons.code.behavior.parser;

import org.junit.Test;

public class BehaviorFunctionNameVisitorTest {

	@Test
	public void test() {
		StringBuffer sb = new StringBuffer();
		sb.append("Function GetETTokenAndUpdate(iAction,oXML)\n");
		sb.append("	\n");
		sb.append("	Set oSA = Authenticate(iAction, \"DMI ET\", \"Equipment\", \"\")\n");
		sb.append("\n");
		sb.append("	If oSA.SelectSingleNode(\"//Token\") Is Nothing Then\n");
		sb.append("		CS_PARAMETERVALUE = \"Failed\"\n");
		sb.append("		CS_MSG = \"Unable to update equipment custom property. Failed to authenticate behavior user.\"\n");
		sb.append("		CS_RESULT = \"CS_EVENTFAILED\"\n");
		sb.append("		Set oXML = Nothing\n");
		sb.append("		Exit Function\n");
		sb.append("	End If\n");
		sb.append(" \n");
		sb.append("	strSAToken = oSA.SelectSingleNode(\"//Token\").Text\n");
		sb.append("	\n");
		sb.append("	'Creating the XML Object.\n");
		sb.append("	Set oUpdatePropXML = CreateObject(\"msxml2.DOMDocument.4.0\")\n");
		sb.append("	oUpdatePropXML.Async = False\n");
		sb.append("\n");
		sb.append("	'Set write/update custom properties value\n");
		sb.append("	Set oUpdatePropXML = ET_EquipmentTx_Update(strSAToken, oXML.XML)\n");
		sb.append("\n");
		sb.append("	If not (oUpdatePropXML.SelectSingleNode(\"//FailureReasonText\") Is Nothing) Then\n");
		sb.append("		CS_RESULT = \"CS_EVENTFAILED\"\n");
		sb.append("		CS_MSG =oUpdatePropXML.SelectSingleNode(\"//FailureReasonText\").Text\n");
		sb.append("		CS_PARAMETERVALUE = \"Failed\"\n");
		sb.append("		Set oXML = Nothing\n");
		sb.append("		Set oUpdatePropXML = Nothing\n");
		sb.append("		Exit Function\n");
		sb.append("	End If\n");
		sb.append("\n");
		sb.append("	Set GetETTokenAndUpdate = oUpdatePropXML\n");
		sb.append("\n");
		sb.append("End Function\n");
		

		BehaviorFunctionNameVisitor behaviorFunctionNameVisitor = new BehaviorFunctionNameVisitor() {

			@Override
			public void visiteMethodName(String name) {
				System.out.println(name);

			}
		};
		String string = sb.toString();
		
		behaviorFunctionNameVisitor.visite(string);
		int startIdx = behaviorFunctionNameVisitor.getStartIdx();
		int endIdx = behaviorFunctionNameVisitor.getEndIdx();
		
		System.out.println(string.substring(startIdx, endIdx));
		
	}

}
