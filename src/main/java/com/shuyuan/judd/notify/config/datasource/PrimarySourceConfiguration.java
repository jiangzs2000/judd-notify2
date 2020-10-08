package com.shuyuan.judd.notify.config.datasource;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.shuyuan.judd.channel.ldlj.dataaccess.mapper"})
public class PrimarySourceConfiguration {

	@Autowired
	@Qualifier("pageHelper")
	private PageHelper pageHelper;

	@Bean
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource  dataSource ) {
		try {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource);
			sessionFactory.setPlugins(new Interceptor[]{pageHelper});
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

			sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapping/*.xml"));
			sessionFactory.setConfigLocation(resolver.getResource("classpath:/mybatis-config.xml"));
			return sessionFactory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Bean
	@Primary
	public DataSourceTransactionManager txManager(@Qualifier("dataSource")  DataSource dataSource ) {
		return new DataSourceTransactionManager(dataSource);
	}
	@Bean
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
		return template;
	}
}
