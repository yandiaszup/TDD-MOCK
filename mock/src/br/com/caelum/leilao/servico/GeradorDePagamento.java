package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.Repositorio.RepositorioDeLeiloes;
import br.com.caelum.leilao.Repositorio.RepositorioDePagamentos;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;

import java.util.Calendar;
import java.util.List;

public class GeradorDePagamento {

    private RepositorioDeLeiloes leiloes;
    private Avaliador avaliador;
    private RepositorioDePagamentos pagamentos;

    public GeradorDePagamento(RepositorioDeLeiloes leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador){
        this.leiloes = leiloes;
        this.avaliador = avaliador;
        this.pagamentos = pagamentos;
    }

    public void gera(){
        List<Leilao> leilaosEncerrados = this.leiloes.encerrados();

        for(Leilao lelao : leilaosEncerrados){
            this.avaliador.avalia(lelao);
            Pagamento novoPagamento = new Pagamento(avaliador.getMaiorLance(), Calendar.getInstance());
            this.pagamentos.salva(novoPagamento);
        }
    }

    private Calendar primeiroDiaUtil(){
        Calendar data = Calendar.getInstance();
        int diaDaSemana = data.get(Calendar.DAY_OF_WEEK);

        if (diaDaSemana == Calendar.SATURDAY)
            data.add(Calendar.DAY_OF_MONTH,2);
        else if(diaDaSemana == Calendar.SUNDAY)
            data.add(Calendar.DAY_OF_MONTH,1);

        return data;
    }
}
