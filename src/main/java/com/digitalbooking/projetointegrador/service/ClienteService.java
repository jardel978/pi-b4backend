package com.digitalbooking.projetointegrador.service;

import com.digitalbooking.projetointegrador.dto.ClienteDTO;
import com.digitalbooking.projetointegrador.model.Cliente;
import com.digitalbooking.projetointegrador.repository.IClienteRepository;
import com.digitalbooking.projetointegrador.security.JwtUtil;
import com.digitalbooking.projetointegrador.service.exception.DadoNaoEncontradoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Classe de service para <strong>Cliente</strong>.
 *
 * @version 1.0
 * @since 1.0
 */

@Service
public class ClienteService extends UsuarioService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private EmailService emailService;

    /**
     * Metodo para salvar um cliente.
     *
     * @param clienteDTO Cliente que deve ser persistido no banco de dados.
     * @since 1.0
     */
    public void salvar(ClienteDTO clienteDTO) {
        Cliente clienteModel = modelMapper.map(clienteDTO, Cliente.class);
        clienteModel.setSenha(passwordEncoder.encode(clienteModel.getSenha()));
        clienteModel.setUsuarioEstaValidado(false);
        Cliente clienteSalvo = clienteRepository.saveAndFlush(clienteModel);
        System.out.println("token validação cliente: " + jwtUtil.gerarTokenDeValidacaoDeRegistro(clienteSalvo));//gerando token
        String tokenValidacao = jwtUtil.gerarTokenDeValidacaoDeRegistro(clienteSalvo);//gerando token
        emailService.enviarEmail(emailService.gerarEmailDeValidacao(tokenValidacao, clienteSalvo.getEmail()));
    }

    /**
     * Metodo para busca de cliente por id.
     *
     * @param id Chave identificadora do cliente a ser buscado
     * @return Cliente encontrado
     * @since 1.0
     */
    public ClienteDTO buscarPorId(Long id) {
        Cliente clienteModel = clienteRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException(
                "Cliente não encontrado. Tipo: " + Cliente.class.getName()));
        return modelMapper.map(clienteModel, ClienteDTO.class);
    }

    /**
     * Metodo para atualizar um usuario.
     *
     * @param clienteDTO Cliente a ser atualizado.
     * @since 1.0
     */
    public void atualizar(ClienteDTO clienteDTO) {
        Cliente clienteModel =
                clienteRepository.findById(clienteDTO.getId()).orElseThrow(() -> new DadoNaoEncontradoException("Usuário " +
                        "não encontrado. Tipo: " + Cliente.class.getName()));
        clienteDTO.setSenha(clienteModel.getSenha());
        salvar(clienteDTO);
    }

//    /**
//     * Metodo para deletar um usuario.
//     *
//     * @param id Chave identificadora do usuario que deve ser deletado.
//     * @since 1.0
//     */
//    @Override
//    public void deletar(Long id) {
//        clienteRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Usuário não " +
//                "encontrado. Tipo: " + Cliente.class.getName()));
//        clienteRepository.deleteById(id);
//    }

}
