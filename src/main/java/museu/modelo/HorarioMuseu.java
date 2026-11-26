package museu.modelo;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "horario_museu")
public class HorarioMuseu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * LAZY: só busca a entidade associada. 
     * Quando o código acessa o atributo explicitamente (ex.: pedido.getCliente().
     * 
     * Sobre o ManyToOne:
     * Cada horário pertence a um museu, e vários horários podem apontar para o mesmo museu pela coluna museu_id. 
     * 
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "museu_id", nullable = false)
    private Museu museu;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    protected HorarioMuseu() {
    }

    public Long getId() {
        return id;
    }

    public Museu getMuseu() {
        return museu;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
}//class HorarioMuseu.