package cn.com.flaginfo;

import cn.com.flaginfo.platform.common.support.PropertiesSourceInitializer;
import com.taobao.pandora.boot.PandoraBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author tiantian
 */

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@ServletComponentScan("cn.com.flaginfo.platform.statistics")
public class SSOServerApplication {
    public static void main(String[] args) {
        PandoraBootstrap.run(args);
        SpringApplication sa = new SpringApplication(SSOServerApplication.class);
        sa.addInitializers(new PropertiesSourceInitializer());
        sa.run(args);
        PandoraBootstrap.markStartupAndWait();
    }
}
