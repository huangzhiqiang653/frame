package com.zx.rts.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @program: law-ibeas->MybatisGenerator
 * @description: 代码生成器
 * @author: 黄智强
 * @create: 2019-11-07 19:04
 **/
public class MybatisSqlGenerator {
    // 全局配置
    final static String GLOBAL_PATH = "/rts/src/main/java";
    final static String AUTHOR = "黄智强";
    final static String MODEL_NAME = "rts";
    final static String TABLE_PRE = "t_";
    // 数据库配置
    final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/zx_rts_db?useUnicode=true&useSSL=false&characterEncoding=utf8";
    final static String DB_DRIVER = "com.p6spy.engine.spy.P6SpyDriver";
    final static String DB_USERNAME = "root";
    final static String DB_PASSWORD = "root";
    // 包配置
    final static String PACKAGE_ROOT = "com.zx";
    final static String MAPPER_PATH = "/rts/src/main/resources/mapper/";
    // 实体类集成的公共基础类
    final static Boolean SUPER_ENTITY_FLAG = false;
    final static String SUPER_ENTITY = ""; //SUPER_ENTITY_FLAG = true时必填
    // controller集成的公共controller
    final static Boolean SUPER_CONTROLLER_FLAG = false;
    final static String SUPER_CONTROLLER = ""; //SUPER_CONTROLLER_FLAG = true时必填

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + GLOBAL_PATH);
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        // dsc.setSchemaName("public");
        dsc.setDriverName(DB_DRIVER);
        dsc.setUsername(DB_USERNAME);
        dsc.setPassword(DB_PASSWORD);
        dsc.setTypeConvert(new MySqlTypeConvert() {
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                //将数据库中datetime date转换成date,默认是localdate
                if (fieldType.toLowerCase().equals("date")) {
                    return DbColumnType.DATE;
                }

                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
            }
        });
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(MODEL_NAME);
        pc.setParent(PACKAGE_ROOT);

       /* pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        pc.setXml("mapper");*/
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + MAPPER_PATH + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        if (SUPER_ENTITY_FLAG) {
            strategy.setSuperEntityClass(SUPER_ENTITY);
        }
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        if (SUPER_CONTROLLER_FLAG) {
            strategy.setSuperControllerClass(SUPER_CONTROLLER);
        }
        // 写于父类中的公共字段
        strategy.setSuperControllerClass("import com.zx.common.common.BaseController");
        // strategy.setSuperEntityClass("com.zx.common.common.BaseSimpleEntityBean");
        strategy.setSuperEntityClass("com.zx.common.common.BaseEntityBean");
        strategy.setSuperEntityColumns("id", "creator", "update_time", "create_time", "delete_flag");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(TABLE_PRE);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
