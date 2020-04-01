/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.fx.controls.html
 *	작성일   : 2019. 3. 3.
 *	작성자   : KYJ (callakrsos@naver.com)
 *******************************/
package com.kyj.fx.commons.fx.controls.html.fxconvert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.kyj.fx.commons.fx.controls.grid.MapToTableViewGenerator;
import com.kyj.fx.commons.utils.FxUtil;
import com.kyj.fx.commons.utils.ValueUtil;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;

/**
 * @author KYJ (callakrsos@naver.com)
 *
 */
public class FromHtmlTable {

	public TableView<Map<String, Object>> fromHtml(String html) {

		Document parse = Jsoup.parse(html);
		Elements select = parse.select("Table");
		ArrayList<Map<String, Object>> items = new ArrayList<>();
		MapToTableViewGenerator mapToTableViewGenerator = new MapToTableViewGenerator(items);

		for (Element tableTag : select) {

			Elements trRows = tableTag.select("tbody tr");
			for (int x = 0, xmax = trRows.size(); x < xmax; x++)
				items.add(new LinkedHashMap<String, Object>());

			Elements headers = tableTag.select("thead th");

			if (headers.size() != 0) {
				for (int hidx = 0, hmax = headers.size(); hidx < hmax; hidx++) {
					Elements rows = tableTag.select("tbody tr");
					Element header = headers.get(hidx);
					String headerId = header.text();
					// row
					for (int idx = 0, max = rows.size(); idx < max; idx++) {
						Map<String, Object> map = items.get(idx);
						Element row = rows.get(idx);
						Elements datas = row.getElementsByTag("td");
						if(datas.size() == 0)
							continue;
						Element data = datas.get(hidx);
						map.put(headerId, data.text());
					}
				}
			}
		}
		
		
		TbSupplier tbSupplier = new TbSupplier();
		mapToTableViewGenerator.setUserCustom(tbSupplier);

		TableView<Map<String, Object>> load = mapToTableViewGenerator.load();

		Elements caption = parse.select("table thead caption");
		String txtCaption = caption.text();
		if (ValueUtil.isNotEmpty(txtCaption))
			Tooltip.install(load, new Tooltip(txtCaption));

		MenuItem miExportExcel = FxUtil.createMenuItemExcelExport(load);
		load.setContextMenu(new ContextMenu(miExportExcel));
		return load;

	}

	/**
	 * @author KYJ (callakrsos@naver.com)
	 *
	 */
	private class TbSupplier implements Function<TableColumn<Map<String, Object>, Object>, TableColumn<Map<String, Object>, Object>> {

		@Override
		public TableColumn<Map<String, Object>, Object> apply(TableColumn<Map<String, Object>, Object> t) {
			int indexOf = t.getColumns().indexOf(t);
			t.setId("tablecolumn." + indexOf);
			return t;
		}

	};

	public void printAnalysis(String html) {
		Document parse = Jsoup.parse(html);
		Elements select = parse.select("Table");
		select.forEach(tableTag -> {

			Elements heads = tableTag.select("thead th");
			heads.forEach(th -> {
				System.out.println(" table header : " + th.text());
			});

			Elements rows = tableTag.select("tbody tr");

			Stream.iterate(0, a -> a + 1).limit(rows.size()).forEach(idx -> {

				Element row = rows.get(idx);
				Elements datas = row.getElementsByTag("td");
				System.out.print(idx + " ] ");
				datas.forEach(data -> {
					System.out.print(data.text() + " ,");
				});
				System.out.println();
			});
		});
	}

}
