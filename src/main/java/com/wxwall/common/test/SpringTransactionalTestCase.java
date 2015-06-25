package com.wxwall.common.test;

import javax.sql.DataSource;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ActiveProfiles("production")
public abstract class SpringTransactionalTestCase extends
		AbstractTransactionalJUnit4SpringContextTests {

	protected DataSource dataSource;
	
	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.dataSource = dataSource;
	}
}
