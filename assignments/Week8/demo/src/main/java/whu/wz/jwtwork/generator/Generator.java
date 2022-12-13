package whu.wz.jwtwork.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

public class Generator {
    private static DataSourceConfig dataSourceConfig(){
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/commodities?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("nash741207");
        return dataSource;
    }

    private static GlobalConfig globalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir")
        + "/src/main/java");
        globalConfig.setOpen(false);
        globalConfig.setAuthor("wz");
        globalConfig.setFileOverride(true);
        globalConfig.setMapperName("%sDao");
        globalConfig.setIdType(IdType.AUTO);
        return globalConfig;
    }

    private static PackageConfig packageConfig(){
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("whu.wz.mybatisplus_work");
        packageConfig.setEntity("domain");
        packageConfig.setMapper("dao");
        return packageConfig;
    }
    private static StrategyConfig strategyConfig(){
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setInclude("commodity","supplier");
        strategyConfig.setTablePrefix("tbl_");
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setEntityBuilderModel(true);
        strategyConfig.setEntityLombokModel(true);
        return strategyConfig;

    }

    public static void main(String[] args){
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setDataSource(dataSourceConfig());
        autoGenerator.setGlobalConfig(globalConfig());
        autoGenerator.setPackageInfo(packageConfig());
        autoGenerator.setStrategy(strategyConfig());
        autoGenerator.execute();

    }

}
