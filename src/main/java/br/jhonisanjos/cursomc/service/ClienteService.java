package br.jhonisanjos.cursomc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.jhonisanjos.cursomc.domain.Cidade;
import br.jhonisanjos.cursomc.domain.Cliente;
import br.jhonisanjos.cursomc.domain.Endereco;
import br.jhonisanjos.cursomc.domain.enums.TipoCliente;
import br.jhonisanjos.cursomc.dto.ClienteDTO;
import br.jhonisanjos.cursomc.dto.ClienteNewDTO;
import br.jhonisanjos.cursomc.repository.CidadeRepository;
import br.jhonisanjos.cursomc.repository.ClienteRepository;
import br.jhonisanjos.cursomc.repository.EnderecoRepository;
import br.jhonisanjos.cursomc.service.exceptions.DataIntegrityException;
import br.jhonisanjos.cursomc.service.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente buscar(Integer id)  {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	

	public void delete(Integer id) {
		this.find(id);
		try {
			this.repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há entidades relacionadas");
		}
	}
	
	public Cliente find(Integer id)  {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		
	}
	
	public List<Cliente> findAll(){
		return this.repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(),null,null);
	}
	
	public Cliente fromDTO(ClienteNewDTO obj) {
		Cliente cliente = new Cliente(null, obj.getNome(), obj.getEmail(),obj.getCpfOuCnpj(), TipoCliente.toEnum(obj.getTipo()));
		cliente.getTelefones().addAll(obj.getTelefones());
		Optional<Cidade> cidade = cidadeRepository.findById(obj.getCidadeId());
		Endereco endereco = new Endereco(null, obj.getLogradouro(), obj.getNumero(), obj.getComplemento(), obj.getBairro(), obj.getCep(), cliente, cidade.get());
		cliente.getEnderecos().add(endereco);
		return cliente;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
