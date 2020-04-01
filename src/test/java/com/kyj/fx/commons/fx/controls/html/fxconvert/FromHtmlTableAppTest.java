/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.html.fxconvert
 *	작성일   : 2019. 3. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.html.fxconvert;

import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FromHtmlTableAppTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane borderPane = new BorderPane();
		primaryStage.setScene(new Scene(borderPane));
		primaryStage.show();

//		StringBuffer sb = new StringBuffer();
//		sb.append("<table>\n");
//		
//		sb.append("<caption>시도별 인구 변동 현황 [단위 : 천명]</caption>\n");
//		sb.append("	<thead>\n");
//		sb.append("		<tr>\n");
//		sb.append("			<th>학교</th>\n");
//		sb.append("			<th>창립년도</th>\n");
//		sb.append("		</tr>\n");
//		sb.append("	</thead>\n");
//		sb.append("	<tbody>\n");
//		sb.append("		<tr>\n");
//		sb.append("			<td>서울대학교</td>\n");
//		sb.append("			<td>1946</td>\n");
//		sb.append("		</tr>\n");
//		sb.append("		<tr>\n");
//		sb.append("			<td>고려대학교</td>\n");
//		sb.append("			<td>1905</td>\n");
//		sb.append("		</tr>\n");
//		sb.append("		<tr>\n");
//		sb.append("			<td>연세대학교</td>\n");
//		sb.append("			<td>1885</td>\n");
//		sb.append("		</tr>\n");
//		sb.append("	</tbody>\n");
//		sb.append("</table>\n");
		// sb.toString();
		StringBuffer sb = new StringBuffer();
		sb.append("<head><style>table{border : '1'; background-color : rgb(52,73,94);min-width : 400px;border-spacing : 2px;border-collapse : separate;}th{color : rgb(221,221,85);}td{color : white;}</style> </head><body> <table> <thead> <tr> <th>ENV_KEY</th>  <th>ENV_VALUE</th>  <th>ENV_DESC</th>  <th>APPLY_START_DT</th>  <th>APPLY_FIN_DT</th>  <th>USE_YN</th>  <th>DEL_YN</th>  <th>FST_REG_DT</th>  <th>FST_REGER_ID</th>  <th>FNL_UPD_DT</th>  <th>FNL_UPDER_ID</th>  </tr> </thead> <tbody>   <tr>  <td>idu.smtp.id</td>    <td>callakrsos-test</td>    <td>smtp id</td>    <td>N/A</td>    <td>N/A</td>    <td>Y</td>    <td>N</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>   </tr>  <tr>  <td>idu.smtp.password</td>    <td>gmes2011!</td>    <td>smtp pass</td>    <td>N/A</td>    <td>N/A</td>    <td>Y</td>    <td>N</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>   </tr>  <tr>  <td>idu.smtp.port</td>    <td>465</td>    <td>smtp port</td>    <td>N/A</td>    <td>N/A</td>    <td>Y</td>    <td>N</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>   </tr>  <tr>  <td>idu.smtp.ssl</td>    <td>true</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>Y</td>    <td>N</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>   </tr>  <tr>  <td>idu.smtp.url</td>    <td>smtp.naver.com</td>    <td>smtp url</td>    <td>N/A</td>    <td>N/A</td>    <td>Y</td>    <td>N</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>    <td>N/A</td>   </tr>   </tbody> </table>  </body>");
		new FromHtmlTable().printAnalysis(sb.toString());
		TableView<Map<String, Object>> fromHtml = new FromHtmlTable().fromHtml(sb.toString());
		System.out.println(fromHtml);

		borderPane.setCenter(fromHtml);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
