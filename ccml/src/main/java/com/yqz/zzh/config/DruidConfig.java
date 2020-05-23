package com.yqz.zzh.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author zzh
 * @Description 使用代码，可验证登录的方案 druid数据连接池
 * Druid的最强大之处在于它有着强大的监控，可以监控我们发送到数据库的所有sql语句。
 * @date 20200523-
 */
@Configuration
//@PropertySource("classpath:application.properties") //可以不用写默认application
//@ConfigurationProperties(prefix="spring.datasource")
@Slf4j
@Data
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary//当出现相同dataSource时以当前为主
    public DataSource getDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        //ServletRegistrationBean提供类的进行注册
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //添加初始化参数：initParams
        //白名单：
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        //IP黑名单（同时存在时，deny优先于allow）
        //如果满足deny，就提示：sorry，you are not permitted to view this page
        servletRegistrationBean.addInitParameter("deny", "192.168.1.73");
        //登录查看信息的账号和密码
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean =
                new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //添加需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif," +
                "*.jpg,*.png, *.css,*.ico,/druid/*");
        return filterRegistrationBean;

    }
}
