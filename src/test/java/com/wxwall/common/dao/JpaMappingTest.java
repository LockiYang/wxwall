package com.wxwall.common.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-wxwall.xml" })
public class JpaMappingTest
// extends SpringTransactionalTestCase //继承事务测试 不能使用多数据源，无法按类型注入DataSource
{

	/**
	 * validate | update | create | create-drop
	 * create: 删除后重建表结构
	 * create-drop： 创建表结构session结束自动删除
	 * update: 更新表结构， 表中已有且model中删除的列不会被删除
	 * validate: 验证创建数据库表结构
	 */
	private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

	@Autowired
	private EntityManager em;

	/**
	 * 重建所有表结构
	 * @throws Exception
	 */
	@Test
	public void allClassMapping() throws Exception {
//		EntityManagerFactory emf = em.getEntityManagerFactory();
//		emf.getProperties().put("hibernate.hbm2ddl.auto", "create");
		Metamodel model = em.getEntityManagerFactory().getMetamodel();
		assertThat(model.getEntities()).as("No entity mapping found").isNotEmpty();

		for (EntityType entityType : model.getEntities()) {
			String entityName = entityType.getName();
			em.createQuery("select o from " + entityName + " o").getResultList();
			logger.info("!!!Mapping OK: " + entityName);
		}
	}
}
