/**
 *
 */
package com.kyj.fx.commons.word.msword.parser;

/**
 * 1.8 버젼 pdf박스
 *
 * @author KYJ
 *
 */
public class PDFReader {

//	public static void main(String[] args) throws IOException {
//		PDDocument doc = new PDDocument();
//		try {
//			doc = PDDocument.load("구글 원격 데스크톱 사용기.pdf");
//			PDDocumentCatalog catal = doc.getDocumentCatalog();
//			List<PDPage> allPages = catal.getAllPages();
//			PDPage pdPage = allPages.get(0);
//
//			BufferedImage convertToImage = pdPage.convertToImage();
//
//			ByteArrayInputStream inputStream = toInputStream(convertToImage);
//
//			new ImageViewPane(inputStream);
//			// ImageIO.write(convertToImage, "png", new File("구글원격데스크톱사용기이미지"));
//
//		} finally {
//			if (doc != null) {
//				doc.close();
//			}
//		}
//	}
//
//	public static ByteArrayInputStream toInputStream(BufferedImage bufferedImage) throws IOException {
//		final ByteArrayOutputStream output = new ByteArrayOutputStream() {
//			@Override
//			public synchronized byte[] toByteArray() {
//				return this.buf;
//			}
//		};
//		ImageIO.write(bufferedImage, "png", output);
//		return new ByteArrayInputStream(output.toByteArray(), 0, output.size());
//	}
}
