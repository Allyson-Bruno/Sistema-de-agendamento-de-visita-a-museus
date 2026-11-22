package museu.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class TesteCdiBean {

    public String getMensagem() {
        return "JSF e CDI funcionando!";
    }
}