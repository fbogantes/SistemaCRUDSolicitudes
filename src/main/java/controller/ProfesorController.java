/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gestion.ProfesorGestion;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Profesor;


/**
 *
 * @author Frey
 */
@Named(value = "profesorController")
@RequestScoped
public class ProfesorController extends Profesor implements Serializable{
    
    private int id; 
    private String tiraJson = "xxxx";
    private String salida; 


    private final String URI = "http://localhost:8080/PruebaExamen-1.0-SNAPSHOT/resources/profesor";
    /**
     * Creates a new instance of ProfesorController
     */
    public ProfesorController() {
    }
    
    public List<Profesor> getProfesores() {
        return ProfesorGestion.getProfesores();
    }

    public void recupera(String codigo) {
        Profesor e = ProfesorGestion.getProfesor(codigo);
        if (e != null) {
            this.setCodigo(e.getCodigo());
            this.setNombreCompleto(e.getNombreCompleto());
            this.setMateria(e.getMateria());
            this.setNotaProfesor(e.getNotaProfesor());
            this.setFechaCreacion(e.getFechaCreacion());
            this.setFechaNaci(e.getFechaNaci());
            this.setGenero(e.getGenero());
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Posiblemente el registro no exista");
            FacesContext.getCurrentInstance().addMessage("profesorJsonForm:identificacion", msg);

        }
    }

    public String insertProfesor() {
        if (ProfesorGestion.insertProfesor(this)) {
            return "profesor.xhtml";

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al insertar el profesor");
            FacesContext.getCurrentInstance().addMessage("editaProfesorForm:identificacion", msg);
            return "profesor.xhtml";
        }
    }

    public String updateProfesor() {
        if (ProfesorGestion.updateProfesor(this)) {
            return "profesor.xhtml";

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al actualizar el profesor");
            FacesContext.getCurrentInstance().addMessage("editaProfesorForm:identificacion", msg);
            return "profesor.xhtml";
        }
    }

    public String deleteProfesor() {
        if (ProfesorGestion.deleteProfesor(this)) {
            return "profesor.xhtml";
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Ocurrio un error al eliminar el profesor");
            FacesContext.getCurrentInstance().addMessage("editaProfesorForm:identificacion", msg);
            return "profesor.xhtml";
        }
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTiraJson() {
        return tiraJson;
    }

    public void setTiraJson(String tiraJson) {
        this.tiraJson = tiraJson;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }
    
    public void hacerGetAll() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI);
        JsonArray response = target.request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);
        salida = response.toString();
    }


    public void hacerGet() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI + "/" + id);
        JsonObject response = target.request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);
        salida = response.toString();
    }


    public void hacerDelete() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI + "/" + id);
        JsonObject response = target.request(MediaType.APPLICATION_JSON)
                .delete(JsonObject.class);
        salida = response.asJsonObject().toString();
    }


    public void hacerPut() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI);
        JsonReader lectorJson = Json.createReader(new StringReader(tiraJson));
        JsonObject jsonObject = lectorJson.readObject();
        Response response = target.request(MediaType.APPLICATION_JSON)
                .put(Entity.json(jsonObject));
        salida = response.readEntity(String.class);
    }


    public void hacerPost() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI);
        JsonReader lectorJson = Json.createReader(new StringReader(tiraJson));
        JsonObject jsonObject = lectorJson.readObject();
        Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonObject));
        salida = response.readEntity(String.class);
    }
    
}
