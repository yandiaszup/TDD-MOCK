package br.com.caelum.leilao.servico;

import br.com.caelum.leilao.Repositorio.RepositorioDeLeiloes;
import br.com.caelum.leilao.Repositorio.RepositorioDePagamentos;
import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeradorDePagamentoTest {
    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado(){
        RepositorioDeLeiloes leiloes = mock(RepositorioDeLeiloes.class);
        RepositorioDePagamentos pagamentos = mock(RepositorioDePagamentos.class);
        Avaliador avaliador = new Avaliador();

        Leilao leilao = new CriadorDeLeilao().para("Playstation")
                .lance(new Usuario("Jose da Silva"),2000.0)
                .lance(new Usuario("Maria Pereira"), 2500.00)
                .constroi();

        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));
        avaliador.avalia(leilao);

        GeradorDePagamento gerador = new GeradorDePagamento(leiloes,pagamentos,avaliador);
        gerador.gera();

        ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());

        Pagamento pagamentoGerado = argumento.getValue();

        assertEquals(2500.0, pagamentoGerado.getValor(),0.00001);
    }

    @Test
    public void deveEmpurrarParaOProximoDiaUtil(){
        RepositorioDeLeiloes leiloes = mock(RepositorioDeLeiloes.class);
        RepositorioDePagamentos pagamentos = mock(RepositorioDePagamentos.class);

        Leilao leilao = new CriadorDeLeilao().para("Playstation")
                .lance(new Usuario("Jose da Silva"),2000.0)
                .lance(new Usuario("Maria Pereira"), 2500.00)
                .constroi();

        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

        GeradorDePagamento gerador = new GeradorDePagamento(leiloes,pagamentos, new Avaliador());
        gerador.gera();

        ArgumentCaptor<Pagamento> argumeto = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumeto.capture());

        Pagamento pagamentoGerado = argumeto.getValue();

        assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
    }
}
