package com.citic.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.citic.annotation.MyBatisDao;
import com.citic.common.persistence.BaseEntity;

/**
 * Created by summer on 2016/11/25.
 */
@Configuration
@MapperScan(basePackages = "com.citic", sqlSessionTemplateRef  = "sqlSessionTemplate",annotationClass=MyBatisDao.class )
public class DataSourceFirst {

  /*  @Bean(name = "firstDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource firstDataSource() {
    	 
        return DataSourceBuilder.create().build();
    }
*/
    @Bean(name = "firstSqlSessionFactory")
    @Primary
    public SqlSessionFactory firstSqlSessionFactory(@Qualifier("firstDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml"));
        bean.setTypeAliasesPackage("com.citic");
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mybatis-config.xml")[0]);
        bean.setTypeAliasesSuperType(BaseEntity.class);
        return bean.getObject();
    }

    @Bean(name = "firstTransactionManager")
    @Primary
    public DataSourceTransactionManager firstTransactionManager(@Qualifier("firstDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("firstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
