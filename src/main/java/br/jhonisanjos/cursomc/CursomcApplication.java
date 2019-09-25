package br.jhonisanjos.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.jhonisanjos.cursomc.domain.Categoria;
import br.jhonisanjos.cursomc.domain.Cidade;
import br.jhonisanjos.cursomc.domain.Cliente;
import br.jhonisanjos.cursomc.domain.Endereco;
import br.jhonisanjos.cursomc.domain.Estado;
import br.jhonisanjos.cursomc.domain.ItemPedido;
import br.jhonisanjos.cursomc.domain.Pagamento;
import br.jhonisanjos.cursomc.domain.PagamentoComBoleto;
import br.jhonisanjos.cursomc.domain.PagamentoComCartao;
import br.jhonisanjos.cursomc.domain.Pedido;
import br.jhonisanjos.cursomc.domain.Produto;
import br.jhonisanjos.cursomc.domain.enums.EstadoPagamento;
import br.jhonisanjos.cursomc.domain.enums.TipoCliente;
import br.jhonisanjos.cursomc.repository.CategoriaRepository;
import br.jhonisanjos.cursomc.repository.CidadeRepository;
import br.jhonisanjos.cursomc.repository.ClienteRepository;
import br.jhonisanjos.cursomc.repository.EnderecoRepository;
import br.jhonisanjos.cursomc.repository.EstadoRepository;
import br.jhonisanjos.cursomc.repository.ItemPedidoRepository;
import br.jhonisanjos.cursomc.repository.PagamentoRepository;
import br.jhonisanjos.cursomc.repository.PedidoRepository;
import br.jhonisanjos.cursomc.repository.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository; 
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired 
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new  Categoria(null,"Informática");
		Categoria cat2 = new  Categoria(null,"Escritório");
		Categoria cat3 = new  Categoria(null,"Papelaria");
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2,cat3));
		
		Produto p1 = new Produto( null,"Computador", 200.00);
		Produto p2 = new Produto(null, "impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
//		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
//		cat2.getProdutos().add(p2);
		
		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().add(cat1);
		
		
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null,  "Minas Gerais");
		Estado est2 = new Estado(null,  "São Paulo");
		
		Cidade c1= new Cidade(null, "Uberlândia", est1);
		Cidade c2= new Cidade(null, "São Paulo", est2);
		Cidade c3= new Cidade(null, "Campinas", est2);
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1= new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("33452181", "992208823"));
		
		clienteRepository.save(cli1);
		
		Endereco e1= new Endereco(null, "Rua Flores", "380", "Apartamento 303", "jardim", "38220854", cli1, c1);
		Endereco e2= new Endereco(null, "Rua Ismael Ribeiro", "12", "casa", "Tororó", "40050250", cli1, c2);
		
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:ss");
		
		Pedido ped1 = new Pedido(sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		
		
		Pagamento pag1= new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		Pagamento pag2= new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);

		ped1.setPagamento(pag1);
		ped2.setPagamento(pag2);
		pedidoRepository.saveAll(Arrays.asList(ped1,ped2));
		pagamentoRepository.saveAll(Arrays.asList(pag1,pag2));
		
		
		ItemPedido ip1 = new ItemPedido(p1, ped1, 0.00, 1, 200.00);
		ItemPedido ip2 = new ItemPedido(p3, ped1, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(p2, ped2, 100.00, 1, 800.00);
		
		this.itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}

}
