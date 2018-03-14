package com.citic.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.citic.common.persistence.BaseEntity;

/**
 * Created by summer on 2016/11/25.
 */
@Configuration
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
        bean.setTypeAliasesPackage("com.citic.modules");
        bean.setTypeAliasesSuperType(BaseEntity.class);
        bean.setTypeAliases(new Class[]{BaseEntity.class});  
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml"));
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mybatis-config.xml")[0]);
        bean.setVfs(SpringBootVFS.class);  
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
