package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDto;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    /*pega todos os pagamentos, devolve e depois mapeia novamente para DTO.*/
    public Page<PagamentoDto> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, PagamentoDto.class));
    }

    public PagamentoDto obterPorId(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    /*chega uma estrutura "JSON PagamentoDto" - transformado de DTO para pagamento
    * Depois de salvo no banco de dados, esse pagamento vai ser transformado para DTO
    *  e será devolvido novamente para a classe controladora*/
    public PagamentoDto criarPagamento(PagamentoDto dto){
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);
        return modelMapper.map(pagamento,PagamentoDto.class);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDto.class);
    }

    public void excluirPagamento(Long id) {
        repository.deleteById(id);
    }
}
