package com.digitalbooking.projetointegrador.config;

import com.digitalbooking.projetointegrador.dto.ClienteDTO;
import com.digitalbooking.projetointegrador.dto.UsuarioDTO;
import com.digitalbooking.projetointegrador.model.Cliente;
import com.digitalbooking.projetointegrador.model.Funcao;
import com.digitalbooking.projetointegrador.model.Usuario;
import com.digitalbooking.projetointegrador.model.enums.NomeFuncao;
import com.digitalbooking.projetointegrador.repository.IFuncaoRepository;
import com.digitalbooking.projetointegrador.service.ClienteService;
import com.digitalbooking.projetointegrador.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration//classe que inicia um usuario ao subir a aplicação (para testes)
public class IniciarUsuarioAdmin implements CommandLineRunner {

   @Autowired
   private UsuarioService usuarioService;

   @Autowired
   private ClienteService clienteService;

   @Autowired
   private IFuncaoRepository funcaoRepository;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private ModelMapper modelMapper;

   @Override
   public void run(String... args) throws Exception {
       try {

           Funcao funcaoAdmin = new Funcao();
           funcaoAdmin.setNomeFuncaoEnum(NomeFuncao.ADMIN);
//            Funcao funcaoAdminSalva = funcaoRepository.saveAndFlush(funcaoAdmin);

//            Funcao funcaoUser = new Funcao();
//          funcaoUser.setId(1);
//            funcaoUser.setNomeFuncaoEnum(NomeFuncao.USER);
//            Funcao funcaoUserSalva = funcaoRepository.saveAndFlush(funcaoUser);

           Usuario usuarioAdmin = new Usuario();
           usuarioAdmin.setNome("Admin");
           usuarioAdmin.setSobrenome("Admin");
           usuarioAdmin.setEmail("admindb@gmail.com");
           usuarioAdmin.setSenha("admin123");
           usuarioAdmin.setFuncao(funcaoUser);

//            Cliente usuarioComum = new Cliente();
//            usuarioComum.setNome("Cliente");
//            usuarioComum.setSobrenome("Cliente");
//            usuarioComum.setEmail("jardelsilvaoli@outlook.com");
//            usuarioComum.setFuncao(funcaoUserSalva);
//            usuarioComum.setSenha("cliente123");
//            usuarioComum.setEndereco("Rua Luiz Boali N100 - Floresta, Itaipé/MG");

           usuarioService.salvar(modelMapper.map(usuarioAdmin, UsuarioDTO.class));
//            clienteService.salvar(modelMapper.map(usuarioComum, ClienteDTO.class));


       } catch (Exception e) {
           e.printStackTrace();
       }

   }
}
