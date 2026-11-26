package museu.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import museu.dao.DaoGenerico;
import museu.dao.HorarioMuseuDAO;
import museu.modelo.Museu;
import museu.util.JPAProvider;
import museu.validacao.AgendamentoValidacao;

@Named
@ViewScoped
public class AgendamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private JPAProvider provider;

    private Long museuId;
    private Long horarioId;
    private Long museuIdDosHorarios;
    private String dataInformada;

    private List<SelectItem> museus;
    private List<OpcaoHorario> horarios = List.of();
    
    /*
     * Carrega a lista de museus do banco de dados logo após a criação da classe e a injeção de dependências, 
     * transformando cada registro em um SelectItem para preencher campos de seleção.
     * 
     * O @PostConstruct
     * 
     * Marca o método carregarMuseus() para rodar automaticamente assim que o contêiner (CDI, Spring, EJB etc.) 
     * instancia o bean e injeta as dependências (como provider).
     * 
     * Executa apenas uma vez no ciclo de vida daquele objeto, antes de ele começar a atender requisições.
     * Evita fazer consultas pesadas no construtor, onde as dependências podem ainda não estar prontas
     */

    @PostConstruct
    public void carregarMuseus() {
        EntityManager manager = provider.getEntityManager();
        try {
            museus = new DaoGenerico<>(manager, Museu.class).listarTodos()
                    .stream()
                    .map(museu -> new SelectItem(museu.getId(), museu.getNome()))
                    .toList();
        } finally {
            manager.close();
        }
    }

    public void carregarHorarios() {
        horarioId = null;
        museuIdDosHorarios = null;
        horarios = List.of();

        if (museuId == null) {
            return;
        }

        EntityManager manager = provider.getEntityManager();
        try {
            horarios = new HorarioMuseuDAO(manager).listarPorMuseu(museuId)
                    .stream()
                    .map(horario -> new OpcaoHorario(
                            horario.getId(), horario.getHoraInicio()))
                    .toList();

            museuIdDosHorarios = museuId;
        } finally {
            manager.close();
        }
    }
  
    public void validarSelecao() {
        LocalDate data;
        try {
            data = LocalDate.parse(dataInformada);
        } catch (DateTimeParseException e) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR, "Informe uma data válida.");
            return;
        }

        if (!AgendamentoValidacao.dataAgendamentoValida(data)) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR,
                    "Escolha uma data de terça a domingo, a partir de hoje.");
            return;
        }

        OpcaoHorario horario = horarios.stream()
                .filter(opcao -> opcao.getId().equals(horarioId))
                .findFirst()
                .orElse(null);

        if (museuId == null || !museuId.equals(museuIdDosHorarios) || horario == null) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR,
                    "Selecione o museu e um horário desse museu.");
            return;
        }

        if (!data.atTime(horario.getHoraInicio()).isAfter(LocalDateTime.now())) {
            exibirMensagem(FacesMessage.SEVERITY_ERROR,
                    "Esse horário já começou. Escolha outro horário.");
            return;
        }

        exibirMensagem(FacesMessage.SEVERITY_INFO, "Seleção válida.");
    }

    private void exibirMensagem(FacesMessage.Severity gravidade, String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(gravidade, texto, null));
    }

    public Long getMuseuId() {
        return museuId;
    }

    public void setMuseuId(Long museuId) {
        this.museuId = museuId;
    }

    public Long getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Long horarioId) {
        this.horarioId = horarioId;
    }

    public String getDataInformada() {
        return dataInformada;
    }

    public void setDataInformada(String dataInformada) {
        this.dataInformada = dataInformada;
    }

    public List<SelectItem> getMuseus() {
        return museus;
    }

    public List<OpcaoHorario> getHorarios() {
        return horarios;
    }

    public static class OpcaoHorario implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Long id;
        private final LocalTime horaInicio;

        public OpcaoHorario(Long id, LocalTime horaInicio) {
            this.id = id;
            this.horaInicio = horaInicio;
        }

        public Long getId() {
            return id;
        }

        public LocalTime getHoraInicio() {
            return horaInicio;
        }
    }
    
}//class AgendamentoBean