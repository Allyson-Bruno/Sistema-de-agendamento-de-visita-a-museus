package museu.validacao;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class AgendamentoValidacao {
	
	/*
	 * A data deve ser informada.
	 * A data não pode estar no passado.
	 * O museu não funciona às segundas-feiras.7
	 * Agendamentos para o dia atual são permitidos, desde que o horário escolhido ainda seja válido.
	 */

    public static boolean dataAgendamentoValida(LocalDate data) {

        if (data == null) {
            return false;
        }

        if (data.isBefore(LocalDate.now())) {
            return false;
        }

        return data.getDayOfWeek() != DayOfWeek.MONDAY;
    }

}//class AgendamentoValidacao.