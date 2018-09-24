import br.com.caelum.leilao.dominio.Palindromo;
import org.junit.Assert;
import org.junit.Test;

public class PalindromoTest {

    @Test
    public void verificapalindromo(){
        String palindromo = "Socorram-me subi no onibus em Marrocos";
        Palindromo a = new Palindromo();

        Assert.assertTrue(a.ehPalindromo(palindromo));
    }

    @Test
    public void verificanaopalindromo(){
        String palindromo = "carro";
        Palindromo a = new Palindromo();

        Assert.assertFalse(a.ehPalindromo(palindromo));
    }




}
