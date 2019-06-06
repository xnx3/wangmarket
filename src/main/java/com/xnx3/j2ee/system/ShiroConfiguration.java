package com.xnx3.j2ee.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import com.xnx3.j2ee.shiro.CustomRealm;

@Configuration
public class ShiroConfiguration {

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     */
	@Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
    	ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //要求登录时的链接(可根据项目的URL进行替换),非必须的属性,默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login.do");
        //登录成功后要跳转的连接,逻辑也可以自定义，例如返回上次请求的页面
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //用户访问未对其授权的资源时,所显示的连接
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.do");
        /*定义shiro过滤器,例如实现自定义的FormAuthenticationFilter，需要继承FormAuthenticationFilter **本例中暂不自定义实现，在下一节实现验证码的例子中体现 */

        /*定义shiro过滤链 Map结构 * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest.getContextPath()的值来的 * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种 * authc：该过滤器下的页面必须验证后才能访问,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");

        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/login.do", "anon");//anon 可以理解为不拦截
        filterChainDefinitionMap.put("/index.jsp", "anon");
        filterChainDefinitionMap.put("/cache/**", "anon");
        filterChainDefinitionMap.put("/head/**", "anon");	//用户头像，如在线客服那里，上传的头像
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/*.jsp", "anon");
        filterChainDefinitionMap.put("/*.do", "anon");
        filterChainDefinitionMap.put("/default/**", "anon");	//系统默认的一些附件，如默认banner图等
//        filterChainDefinitionMap.put("/dns.cgi", "anon");	废弃
        filterChainDefinitionMap.put("/install/*.do", "anon");
        filterChainDefinitionMap.put("/*.html", "anon");
        filterChainDefinitionMap.put("/*.xml", "anon");	//针对sitemap.xml
        filterChainDefinitionMap.put("/style/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/ueditor/**", "anon");
        filterChainDefinitionMap.put("/module/ueditor/dialogs/**", "anon");
//        filterChainDefinitionMap.put("/bbs/list.do", "anon");
//        filterChainDefinitionMap.put("/bbs/view.do", "anon");
        filterChainDefinitionMap.put("/plugin/api/*.do", "anon");
        
        //help
        filterChainDefinitionMap.put("/help/*.do", "anon");
        
        //plugin bbs
        filterChainDefinitionMap.put("/plugin/bbs/*.do", "anon");
        
        //plugin 插件，都是可公开访问，自行在其中加是否登陆验证
        filterChainDefinitionMap.put("/plugin/**", "anon");
        
        //网站模版,v4.7增加的模版开发模式，模版放到本地
        filterChainDefinitionMap.put("/websiteTemplate/**", "anon");
        
        filterChainDefinitionMap.put("/*.*", "anon");
        
        //750套模板
        filterChainDefinitionMap.put("/template/templateExternalList.do", "anon");
        
        //模版列表接口，v4.8增加
        filterChainDefinitionMap.put("/template/getTemplateList.do", "anon");
         
        //v4.10
        filterChainDefinitionMap.put("/module/**", "anon");
         
        //因为如果用本地存储的话，生成的网站页面、上传图片，都会存储到网站根目录下site文件夹中，所以要对非.do结尾的文件，不能拦截
//        filterChainDefinitionMap.put("/site/*.do", "authc");
        filterChainDefinitionMap.put("/site/**", "anon");
        
        filterChainDefinitionMap.put("/sites/*.do", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // 自定义缓存实现 使用 ehcache
        securityManager.setCacheManager(ehCacheManager());////用户授权/认证信息Cache, 采用EhCache 缓存 
        // 自定义session管理
        securityManager.setSessionManager(sessionManager());
        //注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }
    
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        listeners.add(new SessionListener_());
        sessionManager.setSessionListeners(listeners);
        
        org.apache.shiro.web.servlet.Cookie cookie = new SimpleCookie("iwSID");
        cookie.setHttpOnly(true);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionIdCookieEnabled(true);
        
        //Session失效时长，毫秒
        sessionManager.setGlobalSessionTimeout(60000000);
        //sessionManager.setGlobalSessionTimeout(10000);
        
        return sessionManager;
    }

    /** 
     * EhCacheManager , 缓存管理 
     * 用户登陆成功后 , 把用户信息和权限信息缓存起来 , 然后每次用户请求时 , 放入用户的session中 , 如果不设置这个bean , 每个请求都会查询一次数据库 . 
     */ 
    @Bean 
    @DependsOn("lifecycleBeanPostProcessor") 
    public EhCacheManager ehCacheManager() { 
      EhCacheManager em = new EhCacheManager(); 
      em.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");//配置文件路径 
      return em;
    }
    
//    @Bean 
//    public SessionManager sessionManager() { 
//    	DefaultWebSessionManager sessionManager = new DefaultWebSessionManager(); 
//    	Collection<SessionListener> listeners = new ArrayList<>(); 
//    	listeners.add(new BDSessionListener()); 
//    	sessionManager.setSessionListeners(listeners); 
//    	sessionManager.setSessionDAO(sessionDAO()); 
//    	return sessionManager; 
//    } 
//    
//    @Bean 
//    SessionDAO sessionDAO() { 
//    	return new MemorySessionDAO(); 
//    } 
    
    public CustomRealm myShiroRealm(){
        CustomRealm realm = new CustomRealm();
        //告诉realm,使用credentialsMatcher加密算法类来验证密文
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
//        realm.setCachingEnabled(false);
        
        return realm;
    }

    /**
     * cookie对象;
     * @return
     */
    public SimpleCookie rememberMeCookie(){
       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
       //<!-- 记住我cookie生效时间30天 ,单位秒;-->
       simpleCookie.setMaxAge(2592000);
       return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     * @return
     */
    public CookieRememberMeManager rememberMeManager(){
       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
       cookieRememberMeManager.setCookie(rememberMeCookie());
       //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
//       cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
       return cookieRememberMeManager;
    }
    
//    @Bean
//    public FilterRegistrationBean delegatingFilterProxy(){
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//        proxy.setTargetFilterLifecycle(true);
//        proxy.setTargetBeanName("shiroFilter");
//        filterRegistrationBean.setFilter(proxy);
//        return filterRegistrationBean;
//    }
    
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}
	
	/**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    
    /*
     * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
     * 加入注解的使用，不加入这个注解不生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
          DefaultWebSecurityManager securityManager) {
       AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
       authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
       return authorizationAttributeSourceAdvisor;
    }
	
}