/********************************
 *	프로젝트 : gargoyle-commons
 *	패키지   : com.kyj.fx.commons.functions
 *	작성일   : 2018. 10. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.fx.commons.functions;

/**
 * @author KYJ
 *
 */
public class DatabaseInfoConnectionSupplier extends ConnectionSupplier {
	private DabaseInfo d;

	public DatabaseInfoConnectionSupplier(DabaseInfo d) {
		this.d = d;
	}

	@Override
	public String getUrl() {
		return d.getUrl();
	}

	@Override
	public String getUsername() {
		return d.getId();
	}

	@Override
	public String getPassword() {
		return d.getPassword();
	}

	@Override
	public String getDriver() {
		return d.getDriver();
	}

}
