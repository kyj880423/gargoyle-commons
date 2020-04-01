/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.nashorn
 *	작성일   : 2019. 2. 12.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.nashorn;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class DOMNode implements Node {

	private Node node;

	public DOMNode(Node node) {
		this.node = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return this.node.getNodeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeValue()
	 */
	@Override
	public String getNodeValue() throws DOMException {
		return this.node.getNodeValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#setNodeValue(java.lang.String)
	 */
	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		this.node.setNodeValue(nodeValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return this.node.getNodeType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getParentNode()
	 */
	@Override
	public Node getParentNode() {
		return this.node.getParentNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getChildNodes()
	 */
	@Override
	public NodeList getChildNodes() {
		return this.node.getChildNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getFirstChild()
	 */
	@Override
	public Node getFirstChild() {
		return this.node.getFirstChild();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getLastChild()
	 */
	@Override
	public Node getLastChild() {
		return this.node.getLastChild();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getPreviousSibling()
	 */
	@Override
	public Node getPreviousSibling() {
		return this.node.getPreviousSibling();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNextSibling()
	 */
	@Override
	public Node getNextSibling() {
		return this.node.getNextSibling();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getAttributes()
	 */
	@Override
	public NamedNodeMap getAttributes() {
		return this.node.getAttributes();
	}

	/**
	 * @작성자 : KYJ (callakrsos@naver.com)
	 * @작성일 : 2019. 2. 12.
	 * @param key
	 * @return
	 */
	public Node getAttribute(String key) {
		if (this.node != null) {
			NamedNodeMap attributes = this.node.getAttributes();
			if (attributes != null)
				return attributes.getNamedItem(key);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getOwnerDocument()
	 */
	@Override
	public Document getOwnerDocument() {
		return this.node.getOwnerDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#insertBefore(org.w3c.dom.Node, org.w3c.dom.Node)
	 */
	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return this.node.insertBefore(newChild, refChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#replaceChild(org.w3c.dom.Node, org.w3c.dom.Node)
	 */
	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return this.node.replaceChild(newChild, oldChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#removeChild(org.w3c.dom.Node)
	 */
	@Override
	public Node removeChild(Node oldChild) throws DOMException {

		return this.node.removeChild(oldChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
	 */
	@Override
	public Node appendChild(Node newChild) throws DOMException {
		return this.node.appendChild(newChild);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#hasChildNodes()
	 */
	@Override
	public boolean hasChildNodes() {
		return this.node.hasChildNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#cloneNode(boolean)
	 */
	@Override
	public Node cloneNode(boolean deep) {
		return this.node.cloneNode(deep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#normalize()
	 */
	@Override
	public void normalize() {
		this.node.normalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#isSupported(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isSupported(String feature, String version) {
		return this.node.isSupported(feature, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNamespaceURI()
	 */
	@Override
	public String getNamespaceURI() {
		return this.node.getNamespaceURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getPrefix()
	 */
	@Override
	public String getPrefix() {
		return this.node.getPrefix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#setPrefix(java.lang.String)
	 */
	@Override
	public void setPrefix(String prefix) throws DOMException {
		this.node.setPrefix(prefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getLocalName()
	 */
	@Override
	public String getLocalName() {
		return this.node.getLocalName();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#hasAttributes()
	 */
	@Override
	public boolean hasAttributes() {
		return this.node.hasAttributes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getBaseURI()
	 */
	@Override
	public String getBaseURI() {
		return this.node.getBaseURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#compareDocumentPosition(org.w3c.dom.Node)
	 */
	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		return this.node.compareDocumentPosition(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getTextContent()
	 */
	@Override
	public String getTextContent() throws DOMException {
		return this.node.getTextContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#setTextContent(java.lang.String)
	 */
	@Override
	public void setTextContent(String textContent) throws DOMException {
		this.node.setTextContent(textContent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#isSameNode(org.w3c.dom.Node)
	 */
	@Override
	public boolean isSameNode(Node other) {
		return this.node.isSameNode(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#lookupPrefix(java.lang.String)
	 */
	@Override
	public String lookupPrefix(String namespaceURI) {
		return this.node.lookupPrefix(namespaceURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#isDefaultNamespace(java.lang.String)
	 */
	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		return this.node.isDefaultNamespace(namespaceURI);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#lookupNamespaceURI(java.lang.String)
	 */
	@Override
	public String lookupNamespaceURI(String prefix) {
		return this.node.lookupNamespaceURI(prefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node)
	 */
	@Override
	public boolean isEqualNode(Node arg) {
		return this.node.isEqualNode(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getFeature(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getFeature(String feature, String version) {
		return this.node.getFeature(feature, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#setUserData(java.lang.String, java.lang.Object,
	 * org.w3c.dom.UserDataHandler)
	 */
	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		return this.node.setUserData(key, data, handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getUserData(java.lang.String)
	 */
	@Override
	public Object getUserData(String key) {
		return this.node.getUserData(key);
	}

}
