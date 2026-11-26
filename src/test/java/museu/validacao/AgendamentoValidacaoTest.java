package museu.validacao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Test;

public class AgendamentoValidacaoTest {
	
	/*
	 * TemporalAdjusters.next() para que os testes continuem funcionando independentemente do dia em que forem executados
	 */

    @Test
    public void deveRejeitarDataNula() {

        assertFalse(AgendamentoValidacao.dataAgendamentoValida(null));
    }

    @Test
    public void deveRejeitarDataPassada() {

        LocalDate ontem = LocalDate.now().minusDays(1);

        assertFalse(AgendamentoValidacao.dataAgendamentoValida(ontem));
    }

    @Test
    public void deveRejeitarSegundaFeira() {

        LocalDate segunda = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        assertFalse(AgendamentoValidacao.dataAgendamentoValida(segunda));
    }

    @Test
    public void deveAceitarTercaFeiraAteDomingo() {

        LocalDate terca = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY));

        for (int i = 0; i < 6; i++) {

            assertTrue(AgendamentoValidacao.dataAgendamentoValida(terca.plusDays(i)));
        }
    }

    @Test
    public void deveValidarDataDeHoje() {

        LocalDate hoje = LocalDate.now();

        if (hoje.getDayOfWeek() == DayOfWeek.MONDAY) {

            assertFalse(AgendamentoValidacao.dataAgendamentoValida(hoje));

        } else {

            assertTrue(AgendamentoValidacao.dataAgendamentoValida(hoje));
        }
    }

}//class AgendamentoValidacaoTest.