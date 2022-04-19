package com.digitalbooking.projetointegrador.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe para configuracao do ModelMapper(Um framework para mapeamento de objetos).
 *
 * @version 1.0
 * @since 1.0
 */

@Configuration
public class ModelMapperConfig {

    /**
     * Metodo para instanciar e configurar o mapeador de objetos ModelMapper.
     *
     * @return ModelMapper que sera instanciado, montado e gerenciado pelo Spring.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

}
