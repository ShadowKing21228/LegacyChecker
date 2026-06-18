package com.shadowking21.legacychecker.config;

import com.shadowking21.legacychecker.LCReference;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.exstensions.yaml.SCYamlConfig;

public class ConfigRegistry {

    public static BaseShadowConfig<ConfigModel> config;

    public static void init()
    {
        config = SCYamlConfig.Builder.builder(ConfigModel.class)
                .defaults(new ConfigModel())
                .modId(LCReference.MOD_ID)
                .build();
    }
}
